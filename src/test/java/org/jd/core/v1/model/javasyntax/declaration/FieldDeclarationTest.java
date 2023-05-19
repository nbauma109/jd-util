package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class FieldDeclarationTest {

    @Test
    public void testFieldDeclaration() {
        int flags = 1;
        Type type = PrimitiveType.TYPE_INT;
        BaseFieldDeclarator fieldDeclarators = new FieldDeclarators(1);

        FieldDeclaration fieldDeclaration = new FieldDeclaration(flags, type, fieldDeclarators);

        // Getters
        assertEquals(flags, fieldDeclaration.getFlags());
        assertNull(fieldDeclaration.getAnnotationReferences());
        assertEquals(type, fieldDeclaration.getType());
        assertEquals(fieldDeclarators, fieldDeclaration.getFieldDeclarators());

        // Setters
        int newFlags = 2;
        fieldDeclaration.setFlags(newFlags);
        assertEquals(newFlags, fieldDeclaration.getFlags());

        assertNull(fieldDeclaration.getAnnotationReferences());

        // Accept method
        TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
        fieldDeclaration.accept(testVisitor);

        assertEquals(1, testVisitor.getFieldDeclarationCount());

        // ToString
        assertEquals("FieldDeclaration{" + type + " " + fieldDeclarators + "}", fieldDeclaration.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        int flags = 1;
        Type type = PrimitiveType.TYPE_INT;
        BaseFieldDeclarator fieldDeclarators = new FieldDeclarators(1);

        FieldDeclaration fieldDeclaration1 = new FieldDeclaration(flags, type, fieldDeclarators);
        FieldDeclaration fieldDeclaration2 = new FieldDeclaration(flags, type, fieldDeclarators);

        // Equality checks
        assertEquals(fieldDeclaration1, fieldDeclaration2);
        assertEquals(fieldDeclaration2, fieldDeclaration1);
        assertEquals(fieldDeclaration1, fieldDeclaration1);
        assertNotEquals(fieldDeclaration1, null);
        assertNotEquals(fieldDeclaration1, new Object());

        // Hashcode checks
        assertEquals(fieldDeclaration1.hashCode(), fieldDeclaration2.hashCode());

        // Change a field and recheck equality and hashcode
        fieldDeclaration1.setFlags(2);
        assertNotEquals(fieldDeclaration1, fieldDeclaration2);
        assertNotEquals(fieldDeclaration1.hashCode(), fieldDeclaration2.hashCode());
    }
}
