package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParenthesesExpressionTest {

	@Test
	public void testParenthesesExpression() {
		// Test constructor with Expression
		Expression expression = new IntegerConstantExpression(1, 10);
		ParenthesesExpression parenthesesExpression = new ParenthesesExpression(expression);

		assertTrue(parenthesesExpression.getExpression() instanceof IntegerConstantExpression);
		assertTrue(parenthesesExpression.getType() instanceof PrimitiveType);

		// Test constructor with lineNumber and Expression
		ParenthesesExpression parenthesesExpressionWithLineNumber = new ParenthesesExpression(2, expression);
		assertTrue(parenthesesExpressionWithLineNumber.getExpression() instanceof IntegerConstantExpression);
		assertEquals(2, parenthesesExpressionWithLineNumber.getLineNumber());

		// Test the copyTo method
		Expression copiedExpression = parenthesesExpression.copyTo(3);
		assertEquals(3, copiedExpression.getLineNumber());
		assertTrue(copiedExpression instanceof ParenthesesExpression);

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getParenthesesExpressionCount());
	}
}
