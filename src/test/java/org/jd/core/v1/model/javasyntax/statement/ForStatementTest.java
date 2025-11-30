package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclaration;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ForStatementTest {
	@Test
	public void testForStatement() {
		Expression init = new IntegerConstantExpression(1, 1);
		Expression condition = new IntegerConstantExpression(1, 2);
		Expression update = new IntegerConstantExpression(1, 3);
		BaseStatement statements = new ReturnExpressionStatement(new IntegerConstantExpression(1, 4));

		ForStatement forStatement = new ForStatement(init, condition, update, statements);

		// Getters
		assertNull(forStatement.getDeclaration());
		assertEquals(init, forStatement.getInit());
		assertEquals(condition, forStatement.getCondition());
		assertEquals(update, forStatement.getUpdate());
		assertEquals(statements, forStatement.getStatements());

		// toString method
		String expectedToString = "ForStatement{null or " + init + "; " + condition + "; " + update + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(expectedToString, forStatement.toString());

		// Setters
		LocalVariableDeclaration declaration = new LocalVariableDeclaration(ObjectType.TYPE_PRIMITIVE_INT, null);
		Expression newInit = new IntegerConstantExpression(1, 5);
		Expression newCondition = new IntegerConstantExpression(1, 6);
		Expression newUpdate = new IntegerConstantExpression(1, 7);

		forStatement.setDeclaration(declaration);
		forStatement.setInit(newInit);
		forStatement.setCondition(newCondition);
		forStatement.setUpdate(newUpdate);

		assertEquals(declaration, forStatement.getDeclaration());
		assertEquals(newInit, forStatement.getInit());
		assertEquals(newCondition, forStatement.getCondition());
		assertEquals(newUpdate, forStatement.getUpdate());

		// Accept method
		TestVisitor testVisitor = new TestVisitor();
		forStatement.accept(testVisitor);
		assertEquals(1, testVisitor.getForStatementCount());
	}
}
