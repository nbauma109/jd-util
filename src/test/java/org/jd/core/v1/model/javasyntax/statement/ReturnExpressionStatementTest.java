package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReturnExpressionStatementTest {

    @Test
    public void testReturnExpressionStatement() {
        // Test constructor with Expression
        Expression expression = new IntegerConstantExpression(1, PrimitiveType.TYPE_INT, 10);
        ReturnExpressionStatement returnExpressionStatement = new ReturnExpressionStatement(expression);

        assertTrue(returnExpressionStatement.getExpression() instanceof IntegerConstantExpression);
        assertEquals(1, returnExpressionStatement.getLineNumber());
        assertTrue(returnExpressionStatement.isReturnExpressionStatement());
        assertEquals("ReturnExpressionStatement{return IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=10}}", returnExpressionStatement.toString());

        // Test constructor with lineNumber and Expression
        ReturnExpressionStatement returnExpressionStatementWithLineNumber = new ReturnExpressionStatement(2, expression);
        assertTrue(returnExpressionStatementWithLineNumber.getExpression() instanceof IntegerConstantExpression);
        assertEquals(2, returnExpressionStatementWithLineNumber.getLineNumber());

        // Test setLineNumber and setExpression methods
        returnExpressionStatementWithLineNumber.setLineNumber(3);
        Expression newExpression = new IntegerConstantExpression(1, 20);
        returnExpressionStatementWithLineNumber.setExpression(newExpression);

        assertEquals(3, returnExpressionStatementWithLineNumber.getLineNumber());
        assertEquals(newExpression, returnExpressionStatementWithLineNumber.getExpression());

        // Test the getGenericExpression method
        Expression genericExpression = returnExpressionStatementWithLineNumber.getGenericExpression();
        assertEquals(newExpression, genericExpression);
        assertTrue(genericExpression instanceof IntegerConstantExpression);

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        returnExpressionStatementWithLineNumber.accept(visitor);
        assertEquals(1, visitor.getReturnExpressionStatementCount());
    }

    @Test(expected = NullPointerException.class)
    public void testSetExpressionWithNull() {
        // Test the setExpression method with null
        ReturnExpressionStatement returnExpressionStatement = new ReturnExpressionStatement(new IntegerConstantExpression(1, 10));
        returnExpressionStatement.setExpression(null);
    }
}
