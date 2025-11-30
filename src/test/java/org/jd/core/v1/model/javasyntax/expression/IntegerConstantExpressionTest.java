package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class IntegerConstantExpressionTest {

	@Test
	public void testIntegerConstantExpression() {
		// Test constructor with integer value
		IntegerConstantExpression integerConstantExpression = new IntegerConstantExpression(PrimitiveType.TYPE_INT, 10);
		assertEquals(10, integerConstantExpression.getIntegerValue());
		assertTrue(integerConstantExpression.getType() instanceof PrimitiveType);
		assertTrue(integerConstantExpression.isIntegerConstantExpression());

		// Test constructor with lineNumber and integer value
		IntegerConstantExpression integerConstantExpressionWithLineNumber = new IntegerConstantExpression(2, 10);
		assertEquals(10, integerConstantExpressionWithLineNumber.getIntegerValue());
		assertEquals(2, integerConstantExpressionWithLineNumber.getLineNumber());

		// Test the copyTo method
		Expression copiedExpression = integerConstantExpression.copyTo(3);
		assertEquals(3, copiedExpression.getLineNumber());
		assertTrue(copiedExpression instanceof IntegerConstantExpression);
		assertEquals(10, ((IntegerConstantExpression) copiedExpression).getIntegerValue());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getIntegerConstantExpressionCount());

		// Test exception
		assertThrows(IllegalArgumentException.class, () -> new IntegerConstantExpression(ObjectType.TYPE_OBJECT, 0));
	}

	@Test
	public void testToString() {
		IntegerConstantExpression integerConstantExpression = new IntegerConstantExpression(10);
		assertEquals("IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=10}", integerConstantExpression.toString()); //$NON-NLS-1$

		IntegerConstantExpression integerConstantExpressionWithLineNumber = new IntegerConstantExpression(2, 10);
		assertEquals("IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=10}", integerConstantExpressionWithLineNumber.toString()); //$NON-NLS-1$
	}
}
