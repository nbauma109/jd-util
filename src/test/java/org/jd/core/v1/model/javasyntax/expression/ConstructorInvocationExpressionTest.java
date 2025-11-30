package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConstructorInvocationExpressionTest {

    @Test
    public void testConstructorInvocationExpression() {
        int lineNumber = 0;
        ObjectType type = ObjectType.TYPE_ITERABLE;
        String descriptor = "descriptor"; //$NON-NLS-1$
        BaseExpression parameters = new Expressions();
        boolean varArgs = true;

        ConstructorInvocationExpression constructorInvocationExpression = new ConstructorInvocationExpression(lineNumber, type, descriptor, parameters, varArgs);

        // Test isVarArgs
        assertTrue(constructorInvocationExpression.isVarArgs());

        // Test getParameters
        assertEquals(parameters, constructorInvocationExpression.getParameters());

        // Test getType
        assertEquals(PrimitiveType.TYPE_VOID, constructorInvocationExpression.getType());

        // Test getPriority
        assertEquals(1, constructorInvocationExpression.getPriority());

        // Test isConstructorInvocationExpression
        assertTrue(constructorInvocationExpression.isConstructorInvocationExpression());

        // Test toString
        assertEquals("ConstructorInvocationExpression{call this(" + descriptor + ")}", constructorInvocationExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        constructorInvocationExpression.accept(visitor);
        assertEquals(1, visitor.getConstructorInvocationExpressionCount());
    }
}
