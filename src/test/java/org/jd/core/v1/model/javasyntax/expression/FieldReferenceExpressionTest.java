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
        String internalTypeName = "java.lang.String"; //$NON-NLS-1$
        String name = "length"; //$NON-NLS-1$
        String descriptor = "I"; //$NON-NLS-1$

        FieldReferenceExpression fieldReferenceExpression = new FieldReferenceExpression(type, expression, internalTypeName, name, descriptor);

        // Getters
        assertEquals(type, fieldReferenceExpression.getType());
        assertEquals(expression, fieldReferenceExpression.getExpression());
        assertEquals(internalTypeName, fieldReferenceExpression.getInternalTypeName());
        assertEquals(name, fieldReferenceExpression.getName());
        assertEquals(descriptor, fieldReferenceExpression.getDescriptor());
        assertTrue(fieldReferenceExpression.isFieldReferenceExpression());
        assertEquals("FieldReferenceExpression{type=" + type + ", expression=" + expression + ", name=" + name + ", descriptor=" + descriptor + "}", fieldReferenceExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

        // Setters
        Expression newExpression = new FieldReferenceExpression(3, type, null, internalTypeName, name, descriptor);
        fieldReferenceExpression.setExpression(newExpression);
        fieldReferenceExpression.setName("newName"); //$NON-NLS-1$

        assertEquals(newExpression, fieldReferenceExpression.getExpression());
        assertEquals("newName", fieldReferenceExpression.getName()); //$NON-NLS-1$

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
        assertEquals("newName", copiedExpression.getName()); //$NON-NLS-1$
        assertEquals(descriptor, copiedExpression.getDescriptor());
        assertTrue(copiedExpression.isFieldReferenceExpression());
        assertEquals("FieldReferenceExpression{type=" + type + ", expression=" + newExpression + ", name=newName, descriptor=" + descriptor + "}", copiedExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
