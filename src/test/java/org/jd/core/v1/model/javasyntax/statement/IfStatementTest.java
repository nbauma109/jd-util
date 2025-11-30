package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IfStatementTest {

	@Test
	public void testIfStatement() {
		// Test constructor
		Expression condition = new IntegerConstantExpression(1, 10);
		BaseStatement statements = new Statements();
		IfStatement ifStatement = new IfStatement(condition, statements);

		assertTrue(ifStatement.getCondition() instanceof IntegerConstantExpression);
		assertEquals(10, ifStatement.getCondition().getIntegerValue());
		assertTrue(ifStatement.getStatements() instanceof Statements);
		assertTrue(ifStatement.isIfStatement());

		// Test the setCondition method
		Expression newCondition = new IntegerConstantExpression(1, 20);
		ifStatement.setCondition(newCondition);
		assertEquals(20, ifStatement.getCondition().getIntegerValue());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		ifStatement.accept(visitor);
		assertEquals(1, visitor.getIfStatementCount());
	}
}
