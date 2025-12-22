package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YieldExpressionStatementTest {

    @Test
    public void testYieldExpressionStatement() {
        // Test constructor with Expression
        Expression expression = new IntegerConstantExpression(1, PrimitiveType.TYPE_INT, 10);
        YieldExpressionStatement yieldExpressionStatement = new YieldExpressionStatement(expression);

        assertTrue(yieldExpressionStatement.getExpression() instanceof IntegerConstantExpression);
        assertEquals(1, yieldExpressionStatement.getLineNumber());
        assertTrue(yieldExpressionStatement.isYieldExpressionStatement());
        assertEquals("YieldExpressionStatement{yield IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=10}}", yieldExpressionStatement.toString());

        // Test constructor with lineNumber and Expression
        YieldExpressionStatement yieldExpressionStatementWithLineNumber = new YieldExpressionStatement(2, expression);
        assertTrue(yieldExpressionStatementWithLineNumber.getExpression() instanceof IntegerConstantExpression);
        assertEquals(2, yieldExpressionStatementWithLineNumber.getLineNumber());

        // Test setLineNumber and setExpression methods
        yieldExpressionStatementWithLineNumber.setLineNumber(3);
        Expression newExpression = new IntegerConstantExpression(1, 20);
        yieldExpressionStatementWithLineNumber.setExpression(newExpression);

        assertEquals(3, yieldExpressionStatementWithLineNumber.getLineNumber());
        assertEquals(newExpression, yieldExpressionStatementWithLineNumber.getExpression());

        // Test the getGenericExpression method
        Expression genericExpression = yieldExpressionStatementWithLineNumber.getGenericExpression();
        assertEquals(newExpression, genericExpression);
        assertTrue(genericExpression instanceof IntegerConstantExpression);

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        yieldExpressionStatementWithLineNumber.accept(visitor);
        assertEquals(1, visitor.getYieldExpressionStatementCount());
    }

    @Test(expected = NullPointerException.class)
    public void testSetExpressionWithNull() {
        // Test the setExpression method with null
        YieldExpressionStatement yieldExpressionStatement = new YieldExpressionStatement(new IntegerConstantExpression(1, 10));
        yieldExpressionStatement.setExpression(null);
    }
}
