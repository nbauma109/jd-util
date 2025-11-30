package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InstanceOfExpressionTest {

	@Test
	public void testInstanceOfExpression() {
		Expression expression = new LocalVariableReferenceExpression(ObjectType.TYPE_STRING, "var"); //$NON-NLS-1$
		Type instanceOfType = ObjectType.TYPE_ITERABLE;
		int lineNumber = 10;

		InstanceOfExpression instanceOfExpression = new InstanceOfExpression(lineNumber, expression, instanceOfType);

		// Test getExpression
		assertEquals(expression, instanceOfExpression.getExpression());

		// Test getInstanceOfType
		assertEquals(instanceOfType, instanceOfExpression.getInstanceOfType());

		// Test getType
		assertEquals(PrimitiveType.TYPE_BOOLEAN, instanceOfExpression.getType());

		// Test getPriority
		assertEquals(8, instanceOfExpression.getPriority());

		// Test copyTo
		InstanceOfExpression copiedExpression = (InstanceOfExpression) instanceOfExpression.copyTo(20);
		assertEquals(expression, copiedExpression.getExpression());
		assertEquals(instanceOfType, copiedExpression.getInstanceOfType());
		assertEquals(20, copiedExpression.getLineNumber());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getInstanceOfExpressionCount());
	}
}
