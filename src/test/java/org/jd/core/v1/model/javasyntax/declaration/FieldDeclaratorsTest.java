package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldDeclaratorsTest {
	@Test
	public void testFieldDeclarators() {
		// Arrange
		FieldDeclarator declarator1 = new FieldDeclarator("field1", new ExpressionVariableInitializer(null)); //$NON-NLS-1$
		FieldDeclarator declarator2 = new FieldDeclarator("field2", new ExpressionVariableInitializer(null)); //$NON-NLS-1$
		FieldDeclarators declarators = new FieldDeclarators(declarator1, declarator2);

		// Assert
		assertEquals(2, declarators.size());
		assertEquals(declarator1, declarators.get(0));
		assertEquals(declarator2, declarators.get(1));

		// Act
		FieldDeclaration fieldDeclaration = new FieldDeclaration(1, PrimitiveType.TYPE_INT, declarators);
		declarators.setFieldDeclaration(fieldDeclaration);

		// Assert - setFieldDeclaration
		assertEquals(fieldDeclaration, declarator1.getFieldDeclaration());
		assertEquals(fieldDeclaration, declarator2.getFieldDeclaration());

		// Act & Assert - accept
		TestDeclarationVisitor visitor = new TestDeclarationVisitor();
		declarators.accept(visitor);
		assertEquals(1, visitor.getFieldDeclaratorsCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFieldDeclaratorsException() {
		new FieldDeclarators(new FieldDeclarator("name")); //$NON-NLS-1$
	}
}
