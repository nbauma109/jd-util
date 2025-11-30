package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalVariableReferenceExpressionTest {

	@Test
	public void testLocalVariableReferenceExpression() {
		// Arrange
		Type type = ObjectType.TYPE_OBJECT;
		String name = "variable"; //$NON-NLS-1$
		int lineNumber = 0;

		// Act
		LocalVariableReferenceExpression localVariableReferenceExpression = new LocalVariableReferenceExpression(type, name);

		// Assert
		assertEquals(lineNumber, localVariableReferenceExpression.getLineNumber());
		assertEquals(type, localVariableReferenceExpression.getType());
		assertEquals(name, localVariableReferenceExpression.getName());
		assertTrue(localVariableReferenceExpression.isLocalVariableReferenceExpression());

		// Act & Assert
		LocalVariableReferenceExpression copy = (LocalVariableReferenceExpression) localVariableReferenceExpression.copyTo(lineNumber + 1);
		assertEquals(localVariableReferenceExpression.getType(), copy.getType());
		assertEquals(localVariableReferenceExpression.getName(), copy.getName());
		assertNotEquals(localVariableReferenceExpression.getLineNumber(), copy.getLineNumber());

		// Test setName
		String newName = "newVariable"; //$NON-NLS-1$
		localVariableReferenceExpression.setName(newName);
		assertEquals(newName, localVariableReferenceExpression.getName());

		// Test accept method
		TestVisitor visitor = new TestVisitor();
		localVariableReferenceExpression.accept(visitor);
		assertEquals(1, visitor.getLocalVariableReferenceExpressionCount());

		// Test toString method
		String expectedToString = "LocalVariableReferenceExpression{type=" + type + ", name=" + newName + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(expectedToString, localVariableReferenceExpression.toString());
	}
}
