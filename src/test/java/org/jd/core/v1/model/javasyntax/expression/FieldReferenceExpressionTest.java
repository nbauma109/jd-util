package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldReferenceExpressionTest {

    @Test
    public void testFieldReferenceExpression() {
        Type type = ObjectType.TYPE_STRING;
        Expression expression = new ThisExpression(2, type);
        String internalTypeName = "java.lang.String";
        String name = "length";
        String descriptor = "I";

        FieldReferenceExpression fieldReferenceExpression = new FieldReferenceExpression(type, expression, internalTypeName, name, descriptor);

        // Getters
        assertEquals(type, fieldReferenceExpression.getType());
        assertEquals(expression, fieldReferenceExpression.getExpression());
        assertEquals(internalTypeName, fieldReferenceExpression.getInternalTypeName());
        assertEquals(name, fieldReferenceExpression.getName());
        assertEquals(descriptor, fieldReferenceExpression.getDescriptor());
        assertTrue(fieldReferenceExpression.isFieldReferenceExpression());
        assertEquals("FieldReferenceExpression{type=" + type + ", expression=" + expression + ", name=" + name + ", descriptor=" + descriptor + "}", fieldReferenceExpression.toString());

        // Setters
        Expression newExpression = new FieldReferenceExpression(3, type, null, internalTypeName, name, descriptor);
        fieldReferenceExpression.setExpression(newExpression);
        fieldReferenceExpression.setName("newName");

        assertEquals(newExpression, fieldReferenceExpression.getExpression());
        assertEquals("newName", fieldReferenceExpression.getName());

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        fieldReferenceExpression.accept(testVisitor);

        assertEquals(1, testVisitor.getFieldReferenceExpressionCount());

        // Copy method
        FieldReferenceExpression copiedExpression = (FieldReferenceExpression) fieldReferenceExpression.copyTo(4);
        assertEquals(4, copiedExpression.getLineNumber());
        assertEquals(type, copiedExpression.getType());
        assertEquals(newExpression, copiedExpression.getExpression());
        assertEquals(internalTypeName, copiedExpression.getInternalTypeName());
        assertEquals("newName", copiedExpression.getName());
        assertEquals(descriptor, copiedExpression.getDescriptor());
        assertTrue(copiedExpression.isFieldReferenceExpression());
        assertEquals("FieldReferenceExpression{type=" + type + ", expression=" + newExpression + ", name=newName, descriptor=" + descriptor + "}", copiedExpression.toString());
    }
}
