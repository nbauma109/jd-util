package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LengthExpressionTest {

	@Test
	public void testLengthExpression() {
		// Array expression
		ObjectType type = (ObjectType) ObjectType.TYPE_PRIMITIVE_INT.createType(1);
		String name = "array"; //$NON-NLS-1$
		Expression expression = new LocalVariableReferenceExpression(type, name);
		int lineNumber = 0;
		Expression index = new IntegerConstantExpression(lineNumber, PrimitiveType.TYPE_INT, 0);
		Expression arrayExpression = new ArrayExpression(lineNumber, expression, index);

		LengthExpression lengthExpression = new LengthExpression(arrayExpression);

		// Test getExpression
		assertEquals(arrayExpression, lengthExpression.getExpression());

		// Test getType
		assertEquals(PrimitiveType.TYPE_INT, lengthExpression.getType());

		// Test isLengthExpression
		assertTrue(lengthExpression.isLengthExpression());

		// Test toString
		assertEquals("LengthExpression{ArrayExpression{LocalVariableReferenceExpression{type=ObjectType{I, dimension=1}, name=array}[IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=0}]}}", lengthExpression.toString()); //$NON-NLS-1$

		// Test copyTo
		LengthExpression copiedExpression = (LengthExpression) lengthExpression.copyTo(20);
		assertEquals(lengthExpression.getExpression(), copiedExpression.getExpression());
		assertEquals(20, copiedExpression.getLineNumber());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getLengthExpressionCount());
	}
}
