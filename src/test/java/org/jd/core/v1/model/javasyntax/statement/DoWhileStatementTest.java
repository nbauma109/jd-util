package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DoWhileStatementTest {

	@Test
	public void testDoWhileStatement() {
		// Test constructor
		Expression condition = new IntegerConstantExpression(1, 10);
		BaseStatement statements = new Statements();
		DoWhileStatement doWhileStatement = new DoWhileStatement(condition, statements);

		assertTrue(doWhileStatement.getCondition() instanceof IntegerConstantExpression);
		assertEquals(10, doWhileStatement.getCondition().getIntegerValue());
		assertTrue(doWhileStatement.getStatements() instanceof Statements);
		assertTrue(doWhileStatement.isDoWhileStatement());

		// Test the setCondition method
		Expression newCondition = new IntegerConstantExpression(1, 20);
		doWhileStatement.setCondition(newCondition);
		assertEquals(20, doWhileStatement.getCondition().getIntegerValue());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		doWhileStatement.accept(visitor);
		assertEquals(1, visitor.getDoWhileStatementCount());
	}
}
