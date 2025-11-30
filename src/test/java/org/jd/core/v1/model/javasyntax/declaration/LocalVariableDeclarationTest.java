package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalVariableDeclarationTest {

	private LocalVariableDeclaration localVariableDeclaration;
	private TestDeclarationVisitor visitor;

	@Before
	public void setUp() {
		ObjectType objectType = new ObjectType("org/jd/core/v1/service/test/LocalVariableTest", "org.jd.core.v1.service.test.LocalVariableTest", "LocalVariableTest"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(0, "testVariable", null); //$NON-NLS-1$

		localVariableDeclaration = new LocalVariableDeclaration(objectType, localVariableDeclarator);

		visitor = new TestDeclarationVisitor();
	}

	@Test
	public void testLocalVariableDeclaration() {
		// Act
		localVariableDeclaration.accept(visitor);

		// Assert
		assertEquals(1, visitor.getLocalVariableDeclarationCount());
		assertFalse(localVariableDeclaration.isFinal());
		assertEquals("LocalVariableTest", ((ObjectType)localVariableDeclaration.getType()).getName()); //$NON-NLS-1$
		assertEquals("testVariable", localVariableDeclaration.getLocalVariableDeclarators().getFirst().getName()); //$NON-NLS-1$
	}

	@Test
	public void testSetFinal() {
		// Act
		localVariableDeclaration.setFinal(true);

		// Assert
		assertTrue(localVariableDeclaration.isFinal());
	}
}
