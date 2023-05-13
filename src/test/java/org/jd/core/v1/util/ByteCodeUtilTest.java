package org.jd.core.v1.util;

import org.apache.bcel.Const;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import jd.core.model.instruction.bytecode.ByteCodeConstants;
import jd.core.process.analyzer.instruction.bytecode.util.ByteCodeUtil;

public class ByteCodeUtilTest {

    @Test
    public void testOpCodeIn() {
        byte[] code = { (byte) Const.ILOAD_0, (byte) Const.ILOAD_1, (byte) Const.ARETURN, (byte) Const.ASTORE, (byte) Const.DUP };
        assertTrue(ByteCodeUtil.opCodeIn(code, 0, Const.ILOAD_0, Const.ILOAD_1, Const.ARETURN));
        assertFalse(ByteCodeUtil.opCodeIn(code, 0, Const.DUP, Const.ASTORE, Const.GETFIELD));
    }

    @Test
    public void testGetOpCode() {
        byte[] code = { (byte) Const.ILOAD_0, (byte) Const.ILOAD_1, (byte) Const.ARETURN, (byte) Const.ASTORE, (byte) Const.DUP };
        assertEquals(Const.ILOAD_0, ByteCodeUtil.getOpCode(code, 0));
        assertEquals(Const.ILOAD_1, ByteCodeUtil.getOpCode(code, 1));
    }

    @Test
    public void testIsLoadIntValue() {
        assertTrue(ByteCodeUtil.isLoadIntValue(ByteCodeConstants.ICONST));
        assertTrue(ByteCodeUtil.isLoadIntValue(Const.BIPUSH));
        assertTrue(ByteCodeUtil.isLoadIntValue(Const.SIPUSH));
        assertFalse(ByteCodeUtil.isLoadIntValue(Const.DUP));
    }

    @Test
    public void testIsIfInstruction() {
        assertTrue(ByteCodeUtil.isIfInstruction(ByteCodeConstants.IFCMP, true));
        assertTrue(ByteCodeUtil.isIfInstruction(ByteCodeConstants.IFXNULL, true));
        assertFalse(ByteCodeUtil.isIfInstruction(Const.GOTO, false));
    }

    @Test
    public void testIsIfOrGotoInstruction() {
        assertTrue(ByteCodeUtil.isIfOrGotoInstruction(ByteCodeConstants.IFCMP, true));
        assertTrue(ByteCodeUtil.isIfOrGotoInstruction(Const.GOTO, false));
        assertFalse(ByteCodeUtil.isIfOrGotoInstruction(Const.DUP, false));
    }

    @Test
    public void testGetCmpPriority() {
        assertEquals(7, ByteCodeUtil.getCmpPriority(ByteCodeConstants.CMP_EQ));
        assertEquals(6, ByteCodeUtil.getCmpPriority(ByteCodeConstants.CMP_LT));
    }

    @Test
    public void testGetArrayRefIndex() {
        byte[] code = { (byte) Const.ILOAD_0, (byte) Const.ANEWARRAY, (byte) 0, (byte) 0, (byte) Const.ARETURN };
        assertTrue(ByteCodeUtil.getArrayRefIndex(code));
        code = new byte[] { (byte) Const.ILOAD_1, (byte) Const.ANEWARRAY, (byte) 0, (byte) 0, (byte) Const.ARETURN };
        assertFalse(ByteCodeUtil.getArrayRefIndex(code));
    }

    @Test
    public void testGetLoadOpCode() {
        assertEquals(Const.ALOAD, ByteCodeUtil.getLoadOpCode(Const.ASTORE));
        assertEquals(Const.ILOAD, ByteCodeUtil.getLoadOpCode(Const.ISTORE));
        assertEquals(ByteCodeConstants.LOAD, ByteCodeUtil.getLoadOpCode(ByteCodeConstants.STORE));
        assertEquals(Const.GETFIELD, ByteCodeUtil.getLoadOpCode(Const.PUTFIELD));
        assertEquals(Const.GETSTATIC, ByteCodeUtil.getLoadOpCode(Const.PUTSTATIC));
        assertEquals(-1, ByteCodeUtil.getLoadOpCode(Const.DUP));
    }

    @Test
    public void testIsIfInstructionComplex() {
        assertTrue(ByteCodeUtil.isIfInstruction(ByteCodeConstants.COMPLEXIF, true));
        assertFalse(ByteCodeUtil.isIfInstruction(ByteCodeConstants.COMPLEXIF, false));
        assertFalse(ByteCodeUtil.isIfInstruction(Const.IFEQ, true));
    }

    @Test
    public void testGetCmpPriorityNotEqOrNe() {
        assertEquals(6, ByteCodeUtil.getCmpPriority(ByteCodeConstants.CMP_GT));
        assertEquals(6, ByteCodeUtil.getCmpPriority(ByteCodeConstants.CMP_LT));
        assertEquals(7, ByteCodeUtil.getCmpPriority(ByteCodeConstants.CMP_NE));
    }

    @Test
    public void testGetArrayRefIndexInvalidCode() {
        byte[] code1 = { (byte) Const.ILOAD_0, (byte) Const.ILOAD_1, (byte) 0, (byte) 0, (byte) Const.ARETURN };
        assertFalse(ByteCodeUtil.getArrayRefIndex(code1));
        byte[] code2 = { (byte) Const.ILOAD_0, (byte) Const.ANEWARRAY, (byte) 0, (byte) 0, (byte) Const.ILOAD_0 };
        assertFalse(ByteCodeUtil.getArrayRefIndex(code2));
        byte[] code3 = { (byte) Const.ILOAD_0, (byte) Const.ANEWARRAY, (byte) 0, (byte) Const.ILOAD_0 };
        assertFalse(ByteCodeUtil.getArrayRefIndex(code3));
    }

}