package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostOperatorExpressionTest {

	@Test
	public void testPostOperatorExpression() {
		Expression expression = new IntegerConstantExpression(10, PrimitiveType.TYPE_INT, 5);
		String operator = "--"; //$NON-NLS-1$
		PostOperatorExpression postOperatorExpression = new PostOperatorExpression(10, expression, operator);

		// Test getOperator
		assertEquals(operator, postOperatorExpression.getOperator());

		// Test getExpression
		assertEquals(expression, postOperatorExpression.getExpression());

		// Test getType
		Type expectedType = PrimitiveType.TYPE_INT;
		assertEquals(expectedType, postOperatorExpression.getType());

		// Test getPriority
		assertEquals(1, postOperatorExpression.getPriority());

		// Test isPostOperatorExpression
		assertTrue(postOperatorExpression.isPostOperatorExpression());

		// Test toString
		assertEquals("PostOperatorExpression{IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=5} --}", postOperatorExpression.toString()); //$NON-NLS-1$

		// Test copyTo
		PostOperatorExpression copiedExpression = (PostOperatorExpression) postOperatorExpression.copyTo(20);
		assertEquals(postOperatorExpression.getOperator(), copiedExpression.getOperator());
		assertEquals(postOperatorExpression.getExpression(), copiedExpression.getExpression());
		assertEquals(20, copiedExpression.getLineNumber());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getPostOperatorExpressionCount());
	}
}
