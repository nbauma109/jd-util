package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.NoExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.jd.core.v1.util.DefaultList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BaseStatementTest {

	@Test
	public void testAccept() {
		TestVisitor visitor = new TestVisitor();
		NoStatement statement = new NoStatement();

		statement.accept(visitor);

		assertEquals(1, visitor.getNoStatementCount());
	}

	@Test
	public void testIsMethods() {
		NoStatement statement = new NoStatement();

		assertFalse(statement.isBreakStatement());
		assertFalse(statement.isContinueStatement());
		assertFalse(statement.isDoWhileStatement());
		assertFalse(statement.isExpressionStatement());
		assertFalse(statement.isForStatement());
		assertFalse(statement.isIfStatement());
		assertFalse(statement.isIfElseStatement());
		assertFalse(statement.isLabelStatement());
		assertFalse(statement.isLambdaExpressionStatement());
		assertFalse(statement.isLocalVariableDeclarationStatement());
		assertFalse(statement.isMonitorEnterStatement());
		assertFalse(statement.isMonitorExitStatement());
		assertFalse(statement.isReturnStatement());
		assertFalse(statement.isReturnExpressionStatement());
		assertFalse(statement.isStatements());
		assertFalse(statement.isSwitchStatement());
		assertFalse(statement.isSwitchStatementLabelBlock());
		assertFalse(statement.isSwitchStatementMultiLabelsBlock());
		assertFalse(statement.isThrowStatement());
		assertFalse(statement.isTryStatement());
		assertFalse(statement.isWhileStatement());
	}

	@Test
	public void testGetMethods() {
		NoStatement statement = new NoStatement();

		assertEquals(NoExpression.NO_EXPRESSION, statement.getCondition());
		assertEquals(NoExpression.NO_EXPRESSION, statement.getExpression());
		assertEquals(NoExpression.NO_EXPRESSION, statement.getMonitor());
		assertEquals(NoStatement.NO_STATEMENT, statement.getElseStatements());
		assertEquals(NoStatement.NO_STATEMENT, statement.getFinallyStatements());
		assertEquals(NoStatement.NO_STATEMENT, statement.getStatements());
		assertEquals(NoStatement.NO_STATEMENT, statement.getTryStatements());
		assertEquals(NoExpression.NO_EXPRESSION, statement.getInit());
		assertEquals(NoExpression.NO_EXPRESSION, statement.getUpdate());
	}

	@Test
	public void testGetCatchClauses() {
		NoStatement statement = new NoStatement();

		assertEquals(DefaultList.emptyList(), statement.getCatchClauses());
	}

	@Test
	public void testGetLineNumber() {
		NoStatement statement = new NoStatement();

		assertEquals(Expression.UNKNOWN_LINE_NUMBER, statement.getLineNumber());
	}
}
