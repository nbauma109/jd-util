package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.Statements;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LambdaIdentifiersExpressionTest {

    @Test
    public void testLambdaIdentifiersExpression() {
        // Test constructor
        Type type = PrimitiveType.TYPE_INT;
        Type returnedType = PrimitiveType.TYPE_VOID;
        List<String> parameterNames = Arrays.asList("a", "b"); //$NON-NLS-1$ //$NON-NLS-2$
        BaseStatement statements = new Statements();
        LambdaIdentifiersExpression lambdaIdentifiersExpression = new LambdaIdentifiersExpression(1, type, returnedType, parameterNames, statements);

        assertEquals(type, lambdaIdentifiersExpression.getType());
        assertEquals(returnedType, lambdaIdentifiersExpression.getReturnedType());
        assertEquals(parameterNames, lambdaIdentifiersExpression.getParameterNames());
        assertEquals(statements, lambdaIdentifiersExpression.getStatements());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        lambdaIdentifiersExpression.accept(visitor);
        assertEquals(1, visitor.getLambdaIdentifiersExpressionCount());

        // Test toString method
        String expectedToString = "LambdaIdentifiersExpression{" + parameterNames + " -> " + statements + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals(expectedToString, lambdaIdentifiersExpression.toString());

        // Test copyTo method
        LambdaIdentifiersExpression copiedExpression = (LambdaIdentifiersExpression) lambdaIdentifiersExpression.copyTo(2);
        assertNotEquals(copiedExpression, lambdaIdentifiersExpression);
        assertEquals(2, copiedExpression.getLineNumber());
        assertEquals(lambdaIdentifiersExpression.getType(), copiedExpression.getType());
        assertEquals(lambdaIdentifiersExpression.getReturnedType(), copiedExpression.getReturnedType());
        assertEquals(lambdaIdentifiersExpression.getParameterNames(), copiedExpression.getParameterNames());
        assertEquals(lambdaIdentifiersExpression.getStatements(), copiedExpression.getStatements());
    }
}
