package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhileStatementTest {

	@Test
	public void testWhileStatement() {
		// Test constructor
		Expression condition = new IntegerConstantExpression(1, 10);
		BaseStatement statements = new Statements();
		WhileStatement whileStatement = new WhileStatement(condition, statements);

		assertTrue(whileStatement.getCondition() instanceof IntegerConstantExpression);
		assertEquals(10, whileStatement.getCondition().getIntegerValue());
		assertTrue(whileStatement.getStatements() instanceof Statements);
		assertTrue(whileStatement.isWhileStatement());

		// Test the setCondition method
		Expression newCondition = new IntegerConstantExpression(1, 20);
		whileStatement.setCondition(newCondition);
		assertEquals(20, whileStatement.getCondition().getIntegerValue());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		whileStatement.accept(visitor);
		assertEquals(1, visitor.getWhileStatementCount());
	}
}
