package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConstructorReferenceExpressionTest {

    @Test
    public void testConstructorReferenceExpression() {
        // Test constructor
        int lineNumber = 10;
        ObjectType objectType = ObjectType.TYPE_STRING_BUILDER;
        String descriptor = "TestDescriptor"; //$NON-NLS-1$
        Type type = objectType;
        ConstructorReferenceExpression expression = new ConstructorReferenceExpression(lineNumber, type, objectType, descriptor);

        assertEquals(lineNumber, expression.getLineNumber());
        assertEquals(objectType, expression.getObjectType());
        assertEquals(descriptor, expression.getDescriptor());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        expression.accept(visitor);
        assertEquals(1, visitor.getConstructorReferenceExpressionCount());

        // Test copyTo method
        ConstructorReferenceExpression copiedExpression = (ConstructorReferenceExpression) expression.copyTo(2);
        assertNotEquals(copiedExpression, expression);
        assertEquals(2, copiedExpression.getLineNumber());
        assertEquals(copiedExpression.getType(), expression.getType());
        assertEquals(copiedExpression.getObjectType(), expression.getObjectType());
        assertEquals(copiedExpression.getDescriptor(), expression.getDescriptor());
    }
}
