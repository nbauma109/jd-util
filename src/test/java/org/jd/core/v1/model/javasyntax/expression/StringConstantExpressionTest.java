package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Assert;
import org.junit.Test;

public class StringConstantExpressionTest {

	@Test
	public void testStringConstantExpression() {
		// Arrange
		int lineNumber = 10;
		String string = "Hello, World!"; //$NON-NLS-1$
		TestVisitor visitor = new TestVisitor();

		// Act
		StringConstantExpression expression = new StringConstantExpression(lineNumber, string);
		expression.accept(visitor);

		// Assert
		Assert.assertEquals(lineNumber, expression.getLineNumber());
		Assert.assertEquals(string, expression.getStringValue());
		Assert.assertEquals(ObjectType.TYPE_STRING, expression.getType());
		Assert.assertTrue(expression.isStringConstantExpression());
		Assert.assertEquals(1, visitor.getStringConstantExpressionCount());

		// Test toString method
		String expectedToString = "StringConstantExpression{\"Hello, World!\"}"; //$NON-NLS-1$
		Assert.assertEquals(expectedToString, expression.toString());

		// Test copyTo method
		int newLineNumber = 20;
		StringConstantExpression copiedExpression = (StringConstantExpression) expression.copyTo(newLineNumber);
		Assert.assertEquals(newLineNumber, copiedExpression.getLineNumber());
		Assert.assertEquals(string, copiedExpression.getStringValue());
	}
}
