package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

public class SynchronizedStatementTest {

	@Test
	public void testSynchronizedStatement() {
		// Test constructor
		Expression monitor = new IntegerConstantExpression(1, 10);
		Statements statements = new Statements();
		SynchronizedStatement synchronizedStatement = new SynchronizedStatement(monitor, statements);

		assertTrue(synchronizedStatement.getMonitor() instanceof IntegerConstantExpression);
		assertEquals(statements, synchronizedStatement.getStatements());

		// Test setMonitor method
		Expression newMonitor = new IntegerConstantExpression(1, 20);
		synchronizedStatement.setMonitor(newMonitor);

		assertEquals(newMonitor, synchronizedStatement.getMonitor());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		synchronizedStatement.accept(visitor);
		assertEquals(1, visitor.getSynchronizedStatementCount());
	}
}
