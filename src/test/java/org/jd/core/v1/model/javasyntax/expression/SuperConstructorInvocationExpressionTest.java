package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SuperConstructorInvocationExpressionTest {

	@Test
	public void testSuperConstructorInvocationExpression() {
		// Arrange
		int lineNumber = 10;
		ObjectType type = ObjectType.TYPE_STRING_BUILDER;
		String descriptor = "(Ljava/lang/String;)V"; //$NON-NLS-1$
		BaseExpression parameters = new StringConstantExpression("test"); //$NON-NLS-1$
		boolean varArgs = true;

		// Act
		SuperConstructorInvocationExpression expression = new SuperConstructorInvocationExpression(lineNumber, type, descriptor, parameters, varArgs);

		// Assert
		assertEquals(lineNumber, expression.getLineNumber());
		assertEquals(PrimitiveType.TYPE_VOID, expression.getType());
		assertEquals(descriptor, expression.getDescriptor());
		assertEquals(parameters, expression.getParameters());
		assertEquals(varArgs, expression.isVarArgs());
		assertEquals(1, expression.getPriority());
		assertTrue(expression.isSuperConstructorInvocationExpression());

		// Test accept method
		TestVisitor visitor = new TestVisitor();
		expression.accept(visitor);
		assertEquals(1, visitor.getSuperConstructorInvocationExpressionCount());

		// Test toString method
		String expectedToString = "SuperConstructorInvocationExpression{call super(" + descriptor + ")}"; //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(expectedToString, expression.toString());

		// Test setParameters method
		BaseExpression newParameters = new StringConstantExpression("newTest"); //$NON-NLS-1$
		expression.setParameters(newParameters);
		assertEquals(newParameters, expression.getParameters());
	}
}
