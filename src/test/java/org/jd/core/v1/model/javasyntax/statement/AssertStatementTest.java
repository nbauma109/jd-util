package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssertStatementTest {

    @Test
    public void testAssertStatement() {
        // Test constructor
        Expression condition = new IntegerConstantExpression(1, 1);
        Expression message = new StringConstantExpression("Assertion failed"); //$NON-NLS-1$
        AssertStatement assertStatement = new AssertStatement(condition, message);

        assertTrue(assertStatement.getCondition() instanceof IntegerConstantExpression);
        assertTrue(assertStatement.getMessage() instanceof StringConstantExpression);

        // Test setCondition method
        Expression newCondition = new IntegerConstantExpression(2, 0);
        assertStatement.setCondition(newCondition);
        assertTrue(assertStatement.getCondition() instanceof IntegerConstantExpression);
        assertEquals(2, assertStatement.getCondition().getLineNumber());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        assertStatement.accept(visitor);
        assertEquals(1, visitor.getAssertStatementCount());
    }
}
