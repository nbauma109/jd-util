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

import org.objectweb.asm.Opcodes;

import java.util.Arrays;

import static jd.core.model.instruction.bytecode.ByteCodeConstants.COMPLEXIF;
import static jd.core.model.instruction.bytecode.ByteCodeConstants.ICONST;
import static jd.core.model.instruction.bytecode.ByteCodeConstants.IF;
import static jd.core.model.instruction.bytecode.ByteCodeConstants.IFXNULL;

import jd.core.model.instruction.bytecode.ByteCodeConstants;

public final class ByteCodeUtil implements Opcodes
{

    public static final short UNDEFINED = -1;
    public static final short UNPREDICTABLE = -2;
    public static final short RESERVED = -3;

    public static final short LDC_W = 19;
    public static final short LDC2_W = 20;
    public static final short ILOAD_0 = 26;
    public static final short ILOAD_1 = 27;
    public static final short ILOAD_2 = 28;
    public static final short ILOAD_3 = 29;
    public static final short LLOAD_0 = 30;
    public static final short LLOAD_1 = 31;
    public static final short LLOAD_2 = 32;
    public static final short LLOAD_3 = 33;
    public static final short FLOAD_0 = 34;
    public static final short FLOAD_1 = 35;
    public static final short FLOAD_2 = 36;
    public static final short FLOAD_3 = 37;
    public static final short DLOAD_0 = 38;
    public static final short DLOAD_1 = 39;
    public static final short DLOAD_2 = 40;
    public static final short DLOAD_3 = 41;
    public static final short ALOAD_0 = 42;
    public static final short ALOAD_1 = 43;
    public static final short ALOAD_2 = 44;
    public static final short ALOAD_3 = 45;
    public static final short ISTORE_0 = 59;
    public static final short ISTORE_1 = 60;
    public static final short ISTORE_2 = 61;
    public static final short ISTORE_3 = 62;
    public static final short LSTORE_0 = 63;
    public static final short LSTORE_1 = 64;
    public static final short LSTORE_2 = 65;
    public static final short LSTORE_3 = 66;
    public static final short FSTORE_0 = 67;
    public static final short FSTORE_1 = 68;
    public static final short FSTORE_2 = 69;
    public static final short FSTORE_3 = 70;
    public static final short DSTORE_0 = 71;
    public static final short DSTORE_1 = 72;
    public static final short DSTORE_2 = 73;
    public static final short DSTORE_3 = 74;
    public static final short ASTORE_0 = 75;
    public static final short ASTORE_1 = 76;
    public static final short ASTORE_2 = 77;
    public static final short ASTORE_3 = 78;
    public static final short WIDE = 196;
    public static final short GOTO_W = 200;
    public static final short JSR_W = 201;


    static final short[] NO_OF_OPERANDS = {0/* nop */, 0/* aconst_null */, 0/* iconst_m1 */, 0/* iconst_0 */, 0/* iconst_1 */, 0/* iconst_2 */,
            0/* iconst_3 */, 0/* iconst_4 */, 0/* iconst_5 */, 0/* lconst_0 */, 0/* lconst_1 */, 0/* fconst_0 */, 0/* fconst_1 */, 0/* fconst_2 */, 0/* dconst_0 */,
            0/* dconst_1 */, 1/* bipush */, 2/* sipush */, 1/* ldc */, 2/* ldc_w */, 2/* ldc2_w */, 1/* iload */, 1/* lload */, 1/* fload */, 1/* dload */,
            1/* aload */, 0/* iload_0 */, 0/* iload_1 */, 0/* iload_2 */, 0/* iload_3 */, 0/* lload_0 */, 0/* lload_1 */, 0/* lload_2 */, 0/* lload_3 */,
            0/* fload_0 */, 0/* fload_1 */, 0/* fload_2 */, 0/* fload_3 */, 0/* dload_0 */, 0/* dload_1 */, 0/* dload_2 */, 0/* dload_3 */, 0/* aload_0 */,
            0/* aload_1 */, 0/* aload_2 */, 0/* aload_3 */, 0/* iaload */, 0/* laload */, 0/* faload */, 0/* daload */, 0/* aaload */, 0/* baload */, 0/* caload */,
            0/* saload */, 1/* istore */, 1/* lstore */, 1/* fstore */, 1/* dstore */, 1/* astore */, 0/* istore_0 */, 0/* istore_1 */, 0/* istore_2 */,
            0/* istore_3 */, 0/* lstore_0 */, 0/* lstore_1 */, 0/* lstore_2 */, 0/* lstore_3 */, 0/* fstore_0 */, 0/* fstore_1 */, 0/* fstore_2 */, 0/* fstore_3 */,
            0/* dstore_0 */, 0/* dstore_1 */, 0/* dstore_2 */, 0/* dstore_3 */, 0/* astore_0 */, 0/* astore_1 */, 0/* astore_2 */, 0/* astore_3 */, 0/* iastore */,
            0/* lastore */, 0/* fastore */, 0/* dastore */, 0/* aastore */, 0/* bastore */, 0/* castore */, 0/* sastore */, 0/* pop */, 0/* pop2 */, 0/* dup */,
            0/* dup_x1 */, 0/* dup_x2 */, 0/* dup2 */, 0/* dup2_x1 */, 0/* dup2_x2 */, 0/* swap */, 0/* iadd */, 0/* ladd */, 0/* fadd */, 0/* dadd */, 0/* isub */,
            0/* lsub */, 0/* fsub */, 0/* dsub */, 0/* imul */, 0/* lmul */, 0/* fmul */, 0/* dmul */, 0/* idiv */, 0/* ldiv */, 0/* fdiv */, 0/* ddiv */,
            0/* irem */, 0/* lrem */, 0/* frem */, 0/* drem */, 0/* ineg */, 0/* lneg */, 0/* fneg */, 0/* dneg */, 0/* ishl */, 0/* lshl */, 0/* ishr */,
            0/* lshr */, 0/* iushr */, 0/* lushr */, 0/* iand */, 0/* land */, 0/* ior */, 0/* lor */, 0/* ixor */, 0/* lxor */, 2/* iinc */, 0/* i2l */,
            0/* i2f */, 0/* i2d */, 0/* l2i */, 0/* l2f */, 0/* l2d */, 0/* f2i */, 0/* f2l */, 0/* f2d */, 0/* d2i */, 0/* d2l */, 0/* d2f */, 0/* i2b */,
            0/* i2c */, 0/* i2s */, 0/* lcmp */, 0/* fcmpl */, 0/* fcmpg */, 0/* dcmpl */, 0/* dcmpg */, 2/* ifeq */, 2/* ifne */, 2/* iflt */, 2/* ifge */,
            2/* ifgt */, 2/* ifle */, 2/* if_icmpeq */, 2/* if_icmpne */, 2/* if_icmplt */, 2/* if_icmpge */, 2/* if_icmpgt */, 2/* if_icmple */, 2/* if_acmpeq */,
            2/* if_acmpne */, 2/* goto */, 2/* jsr */, 1/* ret */, UNPREDICTABLE/* tableswitch */, UNPREDICTABLE/* lookupswitch */, 0/* ireturn */, 0/* lreturn */,
            0/* freturn */, 0/* dreturn */, 0/* areturn */, 0/* return */, 2/* getstatic */, 2/* putstatic */, 2/* getfield */, 2/* putfield */,
            2/* invokevirtual */, 2/* invokespecial */, 2/* invokestatic */, 4/* invokeinterface */, 4/* invokedynamic */, 2/* new */, 1/* newarray */,
            2/* anewarray */, 0/* arraylength */, 0/* athrow */, 2/* checkcast */, 2/* instanceof */, 0/* monitorenter */, 0/* monitorexit */,
            UNPREDICTABLE/* wide */, 3/* multianewarray */, 2/* ifnull */, 2/* ifnonnull */, 4/* goto_w */, 4/* jsr_w */, 0/* breakpoint */, UNDEFINED, UNDEFINED,
            UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
            UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
            UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
            UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, RESERVED/* impdep1 */,
            RESERVED/* impdep2 */
    };

    private ByteCodeUtil() {
        super();
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

        return index + (opcode == IINC ? 5 : 3);
    }

    public static int nextInstructionOffset(byte[] code, int index)
    {
        final int opcode = code[index] & 255;

        return switch (opcode)
        {
            case TABLESWITCH  -> nextTableSwitchOffset(code, index);
            case LOOKUPSWITCH -> nextLookupSwitchOffset(code, index);
            case WIDE         -> nextWideOffset(code, index);
            default                 -> index + 1 + getNoOfOperands(opcode);
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

                if (opcode == GOTO) {
                    offset += (short) ((code[offset + 1] & 255) << 8 | code[offset + 2] & 255);
                } else if (opcode == GOTO_W) {

                    offset += (code[offset + 1] & 255) << 24 | (code[offset + 2] & 255) << 16
                            | (code[offset + 3] & 255) << 8 | code[offset + 4] & 255;
                } else {
                    break;
                }
            }
        }

        return false;
    }


    public static byte[] cleanUpByteCode(byte[] code) {
        /*
         * Matching a bytecode pattern that is probably the result of bytecode
         * manipulation and preventing the decompiler from structuring the source code
         * properly. Getting rid of all that local variable clutter confusing the decompiler.
         */
        for (int i = 0; i < code.length; i++) {
            int offset = i;
            // check if opCode is ALOAD or in ALOAD_0..3 which is the beginning of the pattern
            if (!opCodeIn(code, offset, ALOAD, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                continue;
            }
            if (getOpCode(code, offset) == ALOAD) {
                // skip ALOAD
                offset++;
                if (opCodeIn(code, offset, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                    // false start, this is the actual beginning, not the local variable index of an ALOAD
                    continue;
                }
            }
            // skip ALOAD local variable index parameter or skip ALOAD_0..3
            offset++;
            if (offset >= code.length) {
                continue;
            }
            while (offset < code.length && opCodeIn(code, offset, GETFIELD, INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, CHECKCAST)) {
                // skip GETFIELD, INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, CHECKCAST and parameters
                offset += 1 + getNoOfOperands(getOpCode(code, offset));
            }
            if (offset >= code.length || !opCodeIn(code, offset, ASTORE, ASTORE_0, ASTORE_1, ASTORE_2, ASTORE_3)) {
                continue;
            }
            final int paramEndIdx = offset;
            final int astore1stIdxParam;
            if (getOpCode(code, offset) == ASTORE) {
                // skip ASTORE
                offset++;
                // store ASTORE parameter
                astore1stIdxParam = code[offset];
            } else {
                // store ASTORE_0..3 parameter
                astore1stIdxParam = code[offset] - ASTORE_0;
            }
            // skip ASTORE parameter or skip ALOAD_0..3
            offset++;
            if (offset >= code.length || getOpCode(code, offset) != ACONST_NULL) {
                continue;
            }
            offset++;
            if (offset >= code.length || !opCodeIn(code, offset, ASTORE, ASTORE_0, ASTORE_1, ASTORE_2, ASTORE_3)) {
                continue;
            }
            offset += 1 + getNoOfOperands(getOpCode(code, offset));
            if (offset >= code.length || !opCodeIn(code, offset, ALOAD, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                continue;
            }
            if (getOpCode(code, offset) == ALOAD) {
                // skip ALOAD
                offset++;
                // check ALOAD local variable index parameter matches that of ASTORE
                if (astore1stIdxParam != code[offset]) {
                    continue;
                }
            } else // check ALOAD local variable index parameter matches that of ASTORE
            if (astore1stIdxParam != code[offset] - ALOAD_0) {
                continue;
            }
            // skip ALOAD parameter or skip ALOAD_0..3
            offset++;
            if (offset >= code.length || getOpCode(code, offset) != INVOKEVIRTUAL) {
                continue;
            }
            final int invokeVirtualIdx = offset;
            // skip INVOKEVIRTUAL and parameters
            offset += 3;
            if (!opCodeIn(code, offset, ASTORE, ASTORE_0, ASTORE_1, ASTORE_2, ASTORE_3)) {
                continue;
            }
            offset += 1 + getNoOfOperands(getOpCode(code, offset));
            if (offset >= code.length || !opCodeIn(code, offset, ALOAD, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                continue;
            }
            if (getOpCode(code, offset) == ALOAD) {
                // skip ALOAD
                offset++;
                // check ALOAD local variable index parameter matches that of ASTORE
                if (astore1stIdxParam != code[offset]) {
                    continue;
                }
            } else // check ALOAD local variable index parameter matches that of ASTORE
            if (astore1stIdxParam != code[offset] - ALOAD_0) {
                continue;
            }
            // skip ALOAD parameter or skip ALOAD_0..3
            offset++;
            if (offset >= code.length) {
                continue;
            }
            if (!opCodeIn(code, offset, ALOAD, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                continue;
            }
            if (getOpCode(code, offset) == ALOAD) {
                // skip ALOAD
                offset++;
                // check ALOAD local variable index parameter matches that of ASTORE
                if (astore1stIdxParam + 2 != code[offset]) {
                    continue;
                }
            } else // check ALOAD local variable index parameter matches that of ASTORE
            if (astore1stIdxParam + 2 != code[offset] - ALOAD_0) {
                continue;
            }
            // skip ALOAD parameter or skip ALOAD_0..3
            offset++;
            if (offset >= code.length) {
                continue;
            }
            if (getOpCode(code, offset) != INVOKESTATIC) {
                continue;
            }
            final int invokeStaticIdx = offset;
            // skip INVOKESTATIC and parameters
            offset += 3;
            if (!opCodeIn(code, offset, ASTORE, ASTORE_0, ASTORE_1, ASTORE_2, ASTORE_3)) {
                continue;
            }
            offset += 1 + getNoOfOperands(getOpCode(code, offset));
            if (offset >= code.length) {
                continue;
            }
            if (!opCodeIn(code, offset, ALOAD, ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3)) {
                continue;
            }
            if (getOpCode(code, offset) == ALOAD) {
                // skip ALOAD
                offset++;
                // check ALOAD local variable index parameter matches that of ASTORE
                if (astore1stIdxParam + 1 != code[offset]) {
                    continue;
                }
            } else // check ALOAD local variable index parameter matches that of ASTORE
            if (astore1stIdxParam + 1 != code[offset] - ALOAD_0) {
                continue;
            }
            // skip ALOAD parameter or skip ALOAD_0..3
            offset++;
            // at this point, pattern is fully matched
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
            Arrays.fill(code, clearFromIdx, newInvokeVirtualIdx, (byte)NOP);
        }

        return code;
    }

    public static boolean opCodeIn(byte[] code, int index, int... values) {
        return Arrays.binarySearch(values, getOpCode(code, index)) >= 0;
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
                && (code[0] & 255) == ILOAD_0 
                && (code[1] & 255) == ANEWARRAY
                && (code[4] & 255) == ARETURN;
    }

    public static int getLoadOpCode(int storeOpCode) {
        return switch (storeOpCode) {
            case ASTORE -> ALOAD;
            case ISTORE -> ILOAD;
            case ByteCodeConstants.STORE -> ByteCodeConstants.LOAD;
            case PUTFIELD -> GETFIELD;
            case PUTSTATIC -> GETSTATIC;
            default -> -1;
        };
    }

    public static short getNoOfOperands(final int index) {
        return NO_OF_OPERANDS[index];
    }
}
