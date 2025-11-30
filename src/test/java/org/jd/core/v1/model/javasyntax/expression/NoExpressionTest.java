package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;
import static org.junit.Assert.*;

public class NoExpressionTest {

	@Test
	public void testNoExpression() {
		// Test default constructor
		NoExpression noExpression = new NoExpression();
		assertEquals(PrimitiveType.TYPE_VOID, noExpression.getType());
		assertEquals("NoExpression", noExpression.toString()); //$NON-NLS-1$

		// Test constructor with line number
		int lineNumber = 10;
		NoExpression noExpressionWithLineNumber = new NoExpression(lineNumber);
		assertEquals(lineNumber, noExpressionWithLineNumber.getLineNumber());
		assertEquals(PrimitiveType.TYPE_VOID, noExpressionWithLineNumber.getType());
		assertEquals("NoExpression", noExpressionWithLineNumber.toString()); //$NON-NLS-1$

		// Test copyTo method
		int newLineNumber = 20;
		NoExpression copiedNoExpression = (NoExpression) noExpressionWithLineNumber.copyTo(newLineNumber);
		assertEquals(newLineNumber, copiedNoExpression.getLineNumber());
		assertEquals(PrimitiveType.TYPE_VOID, copiedNoExpression.getType());
		assertEquals("NoExpression", copiedNoExpression.toString()); //$NON-NLS-1$

		// Test accept method with TestVisitor
		TestVisitor visitor = new TestVisitor();
		noExpression.accept(visitor);
		assertEquals(1, visitor.getNoExpressionCount());
	}

	@Test
	public void testNoExpressionSingleton() {
		NoExpression noExpression1 = NoExpression.NO_EXPRESSION;
		NoExpression noExpression2 = NoExpression.NO_EXPRESSION;

		assertSame(noExpression1, noExpression2);
		assertEquals(PrimitiveType.TYPE_VOID, noExpression1.getType());
		assertEquals("NoExpression", noExpression1.toString()); //$NON-NLS-1$
	}
}
