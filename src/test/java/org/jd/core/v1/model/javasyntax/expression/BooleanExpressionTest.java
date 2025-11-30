package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanExpressionTest {

	@Test
	public void testBooleanExpression() {
		BooleanExpression booleanExpression = new BooleanExpression(10, true);

		// Test getType
		assertEquals(PrimitiveType.TYPE_BOOLEAN, booleanExpression.getType());

		// Test isTrue
		assertTrue(booleanExpression.isTrue());

		// Test isFalse
		assertFalse(booleanExpression.isFalse());

		// Test isBooleanExpression
		assertTrue(booleanExpression.isBooleanExpression());

		// Test toString
		assertEquals("BooleanExpression{true}", booleanExpression.toString()); //$NON-NLS-1$

		// Test copyTo
		BooleanExpression copiedExpression = (BooleanExpression) booleanExpression.copyTo(20);
		assertEquals(booleanExpression.isTrue(), copiedExpression.isTrue());
		assertEquals(20, copiedExpression.getLineNumber());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getBooleanExpressionCount());

		// Test the static instance TRUE
		assertTrue(BooleanExpression.TRUE.isTrue());
		assertFalse(BooleanExpression.TRUE.isFalse());
	}
}
