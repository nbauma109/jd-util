package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeReferenceDotClassExpressionTest {

	@Test
	public void testTypeReferenceDotClassExpression() {
		Type typeDotClass = ObjectType.TYPE_STRING;
		int lineNumber = 0;

		TypeReferenceDotClassExpression typeReferenceDotClassExpression = new TypeReferenceDotClassExpression(typeDotClass);

		// Test getLineNumber
		assertEquals(lineNumber, typeReferenceDotClassExpression.getLineNumber());

		// Test getTypeDotClass
		assertEquals(typeDotClass, typeReferenceDotClassExpression.getTypeDotClass());

		// Test getType
		Type expectedType = ObjectType.TYPE_CLASS.createType(typeDotClass);
		assertEquals(expectedType, typeReferenceDotClassExpression.getType());

		// Test getPriority
		assertEquals(0, typeReferenceDotClassExpression.getPriority());

		// Test toString
		assertEquals("TypeReferenceDotClassExpression{" + typeDotClass + "}", typeReferenceDotClassExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$

		// Test copyTo
		TypeReferenceDotClassExpression copiedExpression = (TypeReferenceDotClassExpression) typeReferenceDotClassExpression.copyTo(20);
		assertEquals(20, copiedExpression.getLineNumber());
		assertEquals(typeDotClass, copiedExpression.getTypeDotClass());

		// Test the accept method with a simple visitor
		TestVisitor visitor = new TestVisitor();
		copiedExpression.accept(visitor);
		assertEquals(1, visitor.getTypeReferenceDotClassExpressionCount());
	}
}
