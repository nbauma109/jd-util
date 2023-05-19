package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EnumDeclarationTest {
    private EnumDeclaration enumDeclaration;
    private EnumDeclaration.Constant constant;

    @Before
    public void setup() {
        constant = new EnumDeclaration.Constant("CONSTANT_NAME");
        enumDeclaration = new EnumDeclaration(1, "INTERNAL_NAME", "NAME", Arrays.asList(constant), null);
    }

    @Test
    public void testGetInterfaces() {
        assertNull(enumDeclaration.getInterfaces());
    }

    @Test
    public void testGetConstants() {
        assertTrue(enumDeclaration.getConstants().contains(constant));
    }

    @Test
    public void testToString() {
        String expected = "EnumDeclaration{INTERNAL_NAME}";
        assertEquals(expected, enumDeclaration.toString());
    }

    @Test
    public void testConstantGetLineNumber() {
        assertEquals(0, constant.getLineNumber());
    }

    @Test
    public void testConstantGetName() {
        assertEquals("CONSTANT_NAME", constant.getName());
    }

    @Test
    public void testConstantGetArguments() {
        assertNull(constant.getArguments());
    }

    @Test
    public void testConstantGetBodyDeclaration() {
        assertNull(constant.getBodyDeclaration());
    }

    @Test
    public void testAccept() throws Exception {
        TestDeclarationVisitor testDeclarationVisitor = new TestDeclarationVisitor();
        enumDeclaration.accept(testDeclarationVisitor);
        assertEquals(1, testDeclarationVisitor.getEnumDeclarationCount());
        constant.accept(testDeclarationVisitor);
        assertEquals(1, testDeclarationVisitor.getConstantCount());
    }
}
