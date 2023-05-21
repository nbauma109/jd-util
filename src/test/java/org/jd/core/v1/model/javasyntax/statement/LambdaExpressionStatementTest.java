package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

public class LambdaExpressionStatementTest {

    @Test
    public void testLambdaExpressionStatement() {
        // Test constructor
        Expression expression = new IntegerConstantExpression(1, 10);
        LambdaExpressionStatement lambdaExpressionStatement = new LambdaExpressionStatement(expression);

        assertTrue(lambdaExpressionStatement.getExpression() instanceof IntegerConstantExpression);

        // Test setExpression method
        Expression newExpression = new IntegerConstantExpression(1, 20);
        lambdaExpressionStatement.setExpression(newExpression);

        assertEquals(newExpression, lambdaExpressionStatement.getExpression());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        lambdaExpressionStatement.accept(visitor);
        assertEquals(1, visitor.getLambdaExpressionStatementCount());

        // Test toString method
        assertEquals("LambdaExpressionStatement{IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=20}}",
                lambdaExpressionStatement.toString());

        // Test isLambdaExpressionStatement method
        assertTrue(lambdaExpressionStatement.isLambdaExpressionStatement());
    }
}
