/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ClassFormatException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TypeMakerConstantPoolTest {
    @Test
    public void testConstantPoolEntryWidths() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream output = new DataOutputStream(bytes)) {
            output.writeShort(20);
            output.writeByte(Const.CONSTANT_Utf8);
            output.writeUTF("example/Test");
            output.writeByte(Const.CONSTANT_Class);
            output.writeShort(1);
            writeEntries(output, 2, Const.CONSTANT_String, Const.CONSTANT_MethodType,
                    Const.CONSTANT_Module, Const.CONSTANT_Package);
            writeEntries(output, 3, Const.CONSTANT_MethodHandle);
            writeEntries(output, 4, Const.CONSTANT_Integer, Const.CONSTANT_Float,
                    Const.CONSTANT_Fieldref, Const.CONSTANT_Methodref, Const.CONSTANT_InterfaceMethodref,
                    Const.CONSTANT_NameAndType, Const.CONSTANT_Dynamic, Const.CONSTANT_InvokeDynamic);
            writeEntries(output, 8, Const.CONSTANT_Long, Const.CONSTANT_Double);
            output.writeShort(0x1234);
        }

        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            Object[] expected = new Object[20];
            expected[1] = "example/Test";
            expected[2] = Integer.valueOf(1);
            assertArrayEquals(expected, loadConstants(input));
            assertEquals(0x1234, input.readUnsignedShort());
        }
    }

    @Test
    public void testInvalidConstantPoolTag() throws Exception {
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(new byte[] {0, 2, 0}))) {
            InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> loadConstants(input));
            assertTrue(exception.getCause() instanceof ClassFormatException);
            assertEquals("Invalid constant pool entry", exception.getCause().getMessage());
        }
    }

    private static void writeEntries(DataOutputStream output, int width, int... tags) throws Exception {
        for (int tag : tags) {
            output.writeByte(tag);
            output.write(new byte[width]);
        }
    }

    private static Object[] loadConstants(DataInput input) throws ReflectiveOperationException {
        Method method = TypeMaker.class.getDeclaredMethod("loadConstants", DataInput.class);
        assertTrue(method.trySetAccessible());
        return (Object[]) method.invoke(null, input);
    }
}
