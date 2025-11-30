package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReturnStatementTest {

	private ReturnStatement returnStatement;
	private TestVisitor visitor;

	@Before
	public void setUp() {
		returnStatement = ReturnStatement.RETURN;
		visitor = new TestVisitor();
	}

	@Test
	public void testReturnStatement() {
		// Act
		returnStatement.accept(visitor);

		// Assert
		assertEquals(1, visitor.getReturnStatementCount());
		assertTrue(returnStatement.isReturnStatement());
		assertEquals("ReturnStatement{}", returnStatement.toString()); //$NON-NLS-1$
	}
}
