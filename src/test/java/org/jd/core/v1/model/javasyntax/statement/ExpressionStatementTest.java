package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExpressionStatementTest {

    @Test
    public void testExpressionStatement() {
        // Arrange
        IntegerConstantExpression expression = new IntegerConstantExpression(123);
        TestVisitor visitor = new TestVisitor();

        // Act
        ExpressionStatement expressionStatement = new ExpressionStatement(expression);

        // Assert
        assertEquals(expression, expressionStatement.getExpression());
        assertTrue(expressionStatement.isExpressionStatement());

        // Act
        expressionStatement.setExpression(new IntegerConstantExpression(456));

        // Assert
        assertEquals(456, ((IntegerConstantExpression)expressionStatement.getExpression()).getIntegerValue());

        // Act
        expressionStatement.accept(visitor);

        // Assert
        assertEquals(1, visitor.getExpressionStatementCount());
    }

    @Test
    public void testToString() {
        // Arrange
        ExpressionStatement expressionStatement = new ExpressionStatement(new IntegerConstantExpression(123));

        // Act & Assert
        assertEquals("ExpressionStatement{IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=123}}", expressionStatement.toString()); //$NON-NLS-1$
    }
}
