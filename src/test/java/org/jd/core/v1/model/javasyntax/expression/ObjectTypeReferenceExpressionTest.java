package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObjectTypeReferenceExpressionTest {
	@Test
	public void testObjectTypeReferenceExpression() {
		int lineNumber = 0;
		ObjectType type = ObjectType.TYPE_PRIMITIVE_INT;
		boolean explicit = true;

		ObjectTypeReferenceExpression objectTypeReferenceExpression = new ObjectTypeReferenceExpression(type);

		// Getters
		assertEquals(lineNumber, objectTypeReferenceExpression.getLineNumber());
		assertEquals(type, objectTypeReferenceExpression.getObjectType());
		assertEquals(type, objectTypeReferenceExpression.getType());
		assertEquals(explicit, objectTypeReferenceExpression.isExplicit());
		assertEquals(0, objectTypeReferenceExpression.getPriority());  // Testing getPriority()

		// isObjectTypeReferenceExpression method
		assertTrue(objectTypeReferenceExpression.isObjectTypeReferenceExpression());

		// toString method
		String expectedToString = "ObjectTypeReferenceExpression{" + type + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(expectedToString, objectTypeReferenceExpression.toString());

		// copyTo method
		ObjectTypeReferenceExpression copiedExpression = (ObjectTypeReferenceExpression) objectTypeReferenceExpression.copyTo(2);
		assertEquals(2, copiedExpression.getLineNumber());
		assertEquals(objectTypeReferenceExpression.getObjectType(), copiedExpression.getObjectType());
		assertEquals(objectTypeReferenceExpression.isExplicit(), copiedExpression.isExplicit());

		// Setters
		boolean newExplicit = false;
		objectTypeReferenceExpression.setExplicit(newExplicit);
		assertEquals(newExplicit, objectTypeReferenceExpression.isExplicit());

		// Accept method
		TestVisitor testVisitor = new TestVisitor();
		objectTypeReferenceExpression.accept(testVisitor);
		assertEquals(1, testVisitor.getObjectTypeReferenceExpressionCount());
	}
}
