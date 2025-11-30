package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LongConstantExpressionTest {

	@Test
	public void testLongConstantExpression() {
		// Test constructor with long value
		LongConstantExpression longConstantExpression = new LongConstantExpression(10L);
		assertEquals(10L, longConstantExpression.getLongValue());
		assertTrue(longConstantExpression.getType() instanceof PrimitiveType);
		assertTrue(longConstantExpression.isLongConstantExpression());

		// Test constructor with lineNumber and long value
		LongConstantExpression longConstantExpressionWithLineNumber = new LongConstantExpression(2, 10L);
		assertEquals(10L, longConstantExpressionWithLineNumber.getLongValue());
		assertEquals(2, longConstantExpressionWithLineNumber.getLineNumber());

		// Test the copyTo method
		Expression copiedExpression = longConstantExpression.copyTo(3);
		assertEquals(3, copiedExpression.getLineNumber());
		assertTrue(copiedExpression instanceof LongConstantExpression);
		assertEquals(10L, ((LongConstantExpression) copiedExpression).getLongValue());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getLongConstantExpressionCount());
	}

	@Test
	public void testToString() {
		LongConstantExpression longConstantExpression = new LongConstantExpression(10L);
		assertEquals("LongConstantExpression{10}", longConstantExpression.toString()); //$NON-NLS-1$

		LongConstantExpression longConstantExpressionWithLineNumber = new LongConstantExpression(2, 10L);
		assertEquals("LongConstantExpression{10}", longConstantExpressionWithLineNumber.toString()); //$NON-NLS-1$
	}
}
