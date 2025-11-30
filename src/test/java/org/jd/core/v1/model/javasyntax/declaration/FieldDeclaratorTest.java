package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FieldDeclaratorTest {

    @Test
    public void testFieldDeclarator() {
        String name = "field"; //$NON-NLS-1$

        FieldDeclarator fieldDeclarator = new FieldDeclarator(name);

        // Getters
        assertNull(fieldDeclarator.getFieldDeclaration());
        assertEquals(name, fieldDeclarator.getName());
        assertNull(fieldDeclarator.getVariableInitializer());

        // Setters
        FieldDeclaration fieldDeclaration = new FieldDeclaration(0, PrimitiveType.TYPE_INT, fieldDeclarator);
        fieldDeclarator.setFieldDeclaration(fieldDeclaration);
        fieldDeclarator.setVariableInitializer(null);

        assertNotNull(fieldDeclarator.getFieldDeclaration());
        assertEquals(fieldDeclaration, fieldDeclarator.getFieldDeclaration());
        assertNull(fieldDeclarator.getVariableInitializer());

        // Accept method
        TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
        fieldDeclarator.accept(testVisitor);

        assertEquals(1, testVisitor.getFieldDeclaratorCount());

        // toString method
        String expectedToString = "FieldDeclarator{" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expectedToString, fieldDeclarator.toString());

        // Equals method
        FieldDeclarator otherFieldDeclarator = new FieldDeclarator(name);
        assertEquals(fieldDeclarator, otherFieldDeclarator);
        assertEquals(fieldDeclarator.hashCode(), otherFieldDeclarator.hashCode());

        // Equals method with different object
        Object obj = new Object();
        assertNotEquals(fieldDeclarator, obj);

        // physically equal
        obj = fieldDeclarator;
        assertEquals(fieldDeclarator, obj);
    }
}
