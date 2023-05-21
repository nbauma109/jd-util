package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IfElseStatementTest {

    @Test
    public void testIfElseStatement() {
        // Arrange
        Expression condition = new IntegerConstantExpression(1);
        BaseStatement ifStatement = new ReturnExpressionStatement(new IntegerConstantExpression(10));
        BaseStatement elseStatement = new ReturnExpressionStatement(new IntegerConstantExpression(20));
        TestVisitor visitor = new TestVisitor();

        // Act
        IfElseStatement ifElseStatement = new IfElseStatement(condition, ifStatement, elseStatement);

        // Assert
        assertTrue(ifElseStatement.isIfElseStatement());
        assertEquals(ifStatement, ifElseStatement.getStatements());
        assertEquals(elseStatement, ifElseStatement.getElseStatements());

        // Act
        ifElseStatement.accept(visitor);

        // Assert
        assertEquals(1, visitor.getIfElseStatementCount());
    }
}
