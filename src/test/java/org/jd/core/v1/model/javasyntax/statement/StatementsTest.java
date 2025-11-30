package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class StatementsTest {
	@Test
	public void testStatements() {
		// Test constructor with List
		Statement statement1 = new ExpressionStatement(null);
		Statement statement2 = new ExpressionStatement(null);
		Statements statementsWithList = new Statements(Arrays.asList(statement1, statement2));
		assertTrue(statementsWithList.isStatements());

		// Test constructor with single statement and statements array
		Statements statementsWithStatementArray = new Statements(statement1, statement2);
		assertTrue(statementsWithStatementArray.isStatements());

		// Test IllegalArgumentException
		List<Statement> statements = Collections.singletonList(statement1);
		assertThrows(IllegalArgumentException.class, () -> new Statements(statements));
		assertThrows(IllegalArgumentException.class, () -> new Statements(statement1));

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		statementsWithStatementArray.accept(visitor);
		assertEquals(1, visitor.getStatementsCount());
	}
}
