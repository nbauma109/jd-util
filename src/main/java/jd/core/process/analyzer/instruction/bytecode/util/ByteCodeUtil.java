/*******************************************************************************
 * Copyright (C) 2007-2019 Emmanuel Dupuy GPLv3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package jd.core.process.analyzer.instruction.bytecode.util;

import org.apache.bcel.Const;

import java.util.Arrays;

import static jd.core.model.instruction.bytecode.ByteCodeConstants.COMPLEXIF;
import static jd.core.model.instruction.bytecode.ByteCodeConstants.ICONST;
import static jd.core.model.instruction.bytecode.ByteCodeConstants.IF;
import static jd.core.model.instruction.bytecode.ByteCodeConstants.IFXNULL;
import static org.apache.bcel.Const.ACONST_NULL;
import static org.apache.bcel.Const.ALOAD;
import static org.apache.bcel.Const.ALOAD_0;
import static org.apache.bcel.Const.ALOAD_1;
import static org.apache.bcel.Const.ALOAD_2;
import static org.apache.bcel.Const.ALOAD_3;
import static org.apache.bcel.Const.ASTORE;
import static org.apache.bcel.Const.ASTORE_0;
import static org.apache.bcel.Const.ASTORE_1;
import static org.apache.bcel.Const.ASTORE_2;
import static org.apache.bcel.Const.ASTORE_3;
import static org.apache.bcel.Const.BIPUSH;
import static org.apache.bcel.Const.CHECKCAST;
import static org.apache.bcel.Const.GETFIELD;
import static org.apache.bcel.Const.GOTO;
import static org.apache.bcel.Const.INVOKEINTERFACE;
import static org.apache.bcel.Const.INVOKESPECIAL;
import static org.apache.bcel.Const.INVOKESTATIC;
import static org.apache.bcel.Const.INVOKEVIRTUAL;
import static org.apache.bcel.Const.SIPUSH;

import jd.core.model.instruction.bytecode.ByteCodeConstants;

public final class ByteCodeUtil
{
    private ByteCodeUtil() {
    }

    public static int nextTableSwitchOffset(byte[] code, int index)
    {
        // Skip padding
        int i = index+4 & 0xFFFC;

        i += 4;

        final int low =
            (code[i  ] & 255) << 24 | (code[i+1] & 255) << 16 |
            (code[i+2] & 255) << 8 |  code[i+3] & 255;

        i += 4;

        final int high =
            (code[i  ] & 255) << 24 | (code[i+1] & 255) << 16 |
            (code[i+2] & 255) << 8 |  code[i+3] & 255;

        i += 4;
        i += 4 * (high - low + 1);

        return i - 1;
    }

    public static int nextLookupSwitchOffset(byte[] code, int index)
    {
        // Skip padding
        int i = index+4 & 0xFFFC;

        i += 4;

        final int npairs =
            (code[i  ] & 255) << 24 | (code[i+1] & 255) << 16 |
            (code[i+2] & 255) << 8 |  code[i+3] & 255;

        i += 4;
        i += 8*npairs;

        return i - 1;
    }

    public static int nextWideOffset(byte[] code, int index)
    {
        final int opcode = code[index+1] & 255;

        return index + (opcode == Const.IINC ? 5 : 3);
    }

    public static int nextInstructionOffset(byte[] code, int index)
    {
        final int opcode = code[index] & 255;

        return switch (opcode)
        {
            case Const.TABLESWITCH  -> nextTableSwitchOffset(code, index);
            case Const.LOOKUPSWITCH -> nextLookupSwitchOffset(code, index);
            case Const.WIDE         -> nextWideOffset(code, index);
            default                 -> index + 1 + Const.getNoOfOperands(opcode);
        };
    }

    public static boolean jumpTo(byte[] code, int offset, int targetOffset) {
        if (offset != -1) {
            int codeLength = code.length;

            for (int i = 0; i < 10; i++) {
                if (offset == targetOffset) {
                    return true;
                }
                if (offset >= codeLength) {
                    break;
                }

                int opcode = code[offset] & 255;

                if (opcode == Const.GOTO) {
                    offset += (short) ((code[offset + 1] & 255) << 8 | code[offset + 2] & 255);
                } else if (opcode == Const.GOTO_W) {

                    offset += (code[offset + 1] & 255) << 24 | (code[offset + 2] & 255) << 16
                            | (code[offset + 3] & 255) << 8 | code[offset + 4] & 255;
                } else {
                    return false;
                }
            }
        }

        return false;
    }


    public static byte[] cleanUpByteCode(byte[] code) {
        // Remove local variable clutter introduced by bytecode manipulation.
        for (int i = 0; i < code.length; i++) {
            cleanUpPattern(code, i);
        }
        return code;
    }

    private static void cleanUpPattern(byte[] code, int i) {
        int paramEndIdx = findParameterEnd(code, i);
        if (paramEndIdx < 0) {
            return;
        }
        int offset = skipStore(code, paramEndIdx);
        if (offset < 0 || offset >= code.length || getOpCode(code, offset) != ACONST_NULL) {
            return;
        }
        int astore1stIdxParam = localVariableIndex(code, paramEndIdx, ASTORE, ASTORE_0);
        offset = skipStore(code, offset + 1);
        int invokeVirtualIdx = skipMatchingLoad(code, offset, astore1stIdxParam);
        if (invokeVirtualIdx < 0 || invokeVirtualIdx >= code.length
                || getOpCode(code, invokeVirtualIdx) != INVOKEVIRTUAL) {
            return;
        }
        int invokeStaticIdx = findStaticInvocation(code, invokeVirtualIdx, astore1stIdxParam);
        if (invokeStaticIdx < 0) {
            return;
        }
        offset = skipStore(code, invokeStaticIdx + 3);
        offset = skipMatchingLoad(code, offset, astore1stIdxParam + 1);
        if (offset >= 0) {
            replacePattern(code, i, paramEndIdx, invokeVirtualIdx, invokeStaticIdx, offset);
        }
    }

    private static int findParameterEnd(byte[] code, int offset) {
        if (!isLoad(code, offset)) {
            return -1;
        }
        if (getOpCode(code, offset) == ALOAD) {
            offset++;
            if (offset >= code.length || opCodeIn(code, offset, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                return -1;
            }
        }
        offset++;
        while (offset < code.length && opCodeIn(code, offset, GETFIELD, INVOKEVIRTUAL,
                INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, CHECKCAST)) {
            offset += 1 + Const.getNoOfOperands(getOpCode(code, offset));
        }
        if (skipStore(code, offset) < 0) {
            return -1;
        }
        return offset;
    }

    private static int findStaticInvocation(byte[] code, int invokeVirtualIdx, int localVariableIndex) {
        int offset = skipStore(code, invokeVirtualIdx + 3);
        offset = skipMatchingLoad(code, offset, localVariableIndex);
        offset = skipMatchingLoad(code, offset, localVariableIndex + 2);
        if (offset < 0 || offset >= code.length || getOpCode(code, offset) != INVOKESTATIC) {
            return -1;
        }
        return offset;
    }

    private static boolean isLoad(byte[] code, int offset) {
        return offset >= 0 && offset < code.length && opCodeIn(code, offset, ALOAD, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3);
    }

    private static int skipStore(byte[] code, int offset) {
        if (offset < 0 || offset >= code.length || !opCodeIn(code, offset, ASTORE, ASTORE_0, ASTORE_1, ASTORE_2, ASTORE_3)) {
            return -1;
        }
        int nextOffset = offset + 1 + Const.getNoOfOperands(getOpCode(code, offset));
        if (nextOffset > code.length) {
            return -1;
        }
        return nextOffset;
    }

    private static int skipMatchingLoad(byte[] code, int offset, int localVariableIndex) {
        if (!isLoad(code, offset)) {
            return -1;
        }
        int nextOffset = offset + 1 + Const.getNoOfOperands(getOpCode(code, offset));
        if (nextOffset > code.length || localVariableIndex != localVariableIndex(code, offset, ALOAD, ALOAD_0)) {
            return -1;
        }
        return nextOffset;
    }

    private static int localVariableIndex(byte[] code, int offset, int indexedOpcode, int firstCompactOpcode) {
        if (getOpCode(code, offset) == indexedOpcode) {
            return code[offset + 1];
        }
        return code[offset] - firstCompactOpcode;
    }

    private static void replacePattern(byte[] code, int i, int paramEndIdx, int invokeVirtualIdx,
            int invokeStaticIdx, int offset) {
        int paramLength = paramEndIdx - i;
        int newParamEndIdx = paramEndIdx + paramLength;
        int newInvokeVirtualIdx = offset - 6;
        int newInvokeStaticIdx = offset - 3;
        // check available space before applying changes
        boolean canCopy = newInvokeVirtualIdx >= newParamEndIdx;
        int clearFromIdx;
        if (canCopy) {
            clearFromIdx = newParamEndIdx;
        } else {
            int astoreIdx = paramEndIdx;
            // subtract 33 for ASTORE to ALOAD conversion
            byte aloadCode = (byte) (code[astoreIdx] - 33);
            if (getOpCode(code, astoreIdx) == ASTORE) {
                // rarest case
                // skip ASTORE and parameter
                // copy ALOAD and parameter twice
                // for following invocations
                code[astoreIdx + 2] = aloadCode;
                code[astoreIdx + 3] = code[astoreIdx + 1];
                code[astoreIdx + 4] = code[astoreIdx + 2];
                code[astoreIdx + 5] = code[astoreIdx + 3];
                clearFromIdx = astoreIdx + 6;
            } else {
                // copy ALOAD code twice for following invocations
                code[astoreIdx + 1] = aloadCode;
                code[astoreIdx + 2] = code[astoreIdx + 1];
                clearFromIdx = astoreIdx + 3;
            }
        }
        // move invoke virtual/static down to make space to copy parameters
        System.arraycopy(code, invokeStaticIdx, code, newInvokeStaticIdx, 3);
        System.arraycopy(code, invokeVirtualIdx, code, newInvokeVirtualIdx, 3);
        // copy parameters
        if (canCopy) {
            System.arraycopy(code, i, code, paramEndIdx, paramLength);
        }
        // clear what's left in the middle
        Arrays.fill(code, clearFromIdx, newInvokeVirtualIdx, (byte)Const.NOP);
    }

    /** @return false if the index is past the end of the code, where a truncated pattern leads */
    public static boolean opCodeIn(byte[] code, int index, int... values) {
        return index < code.length && Arrays.binarySearch(values, getOpCode(code, index)) >= 0;
    }

    public static int getOpCode(byte[] code, int index) {
        return code[index] & 255;
    }

    public static boolean isLoadIntValue(int opcode) {
        return opcode == ICONST || opcode == BIPUSH || opcode == SIPUSH;
    }

    public static boolean isIfInstruction(int opcode, boolean includeComplex) {
        return opcode >= IF && opcode <= IFXNULL || includeComplex && opcode == COMPLEXIF;
    }

    public static boolean isIfOrGotoInstruction(int opcode, boolean includeComplex) {
        return isIfInstruction(opcode, includeComplex) || opcode == GOTO;
    }

    public static int getCmpPriority(int cmp) {
        return cmp == ByteCodeConstants.CMP_EQ || cmp == ByteCodeConstants.CMP_NE ? 7 : 6;
    }

    public static boolean getArrayRefIndex(byte[] code) {
        return code.length == 5
                && (code[0] & 255) == Const.ILOAD_0
                && (code[1] & 255) == Const.ANEWARRAY
                && (code[4] & 255) == Const.ARETURN;
    }

    public static int getLoadOpCode(int storeOpCode) {
        return switch (storeOpCode) {
            case Const.ASTORE -> Const.ALOAD;
            case Const.ISTORE -> Const.ILOAD;
            case ByteCodeConstants.STORE -> ByteCodeConstants.LOAD;
            case Const.PUTFIELD -> Const.GETFIELD;
            case Const.PUTSTATIC -> Const.GETSTATIC;
            default -> -1;
        };
    }
}
