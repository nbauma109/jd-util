package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemberDeclarationsTest {
	@Test
	public void testMemberDeclarations() {
		// Arrange
		MemberDeclaration declaration1 = new StaticInitializerDeclaration("", null); //$NON-NLS-1$
		MemberDeclaration declaration2 = new StaticInitializerDeclaration("", null); //$NON-NLS-1$
		MemberDeclarations declarations = new MemberDeclarations(declaration1, declaration2);

		// Assert
		assertEquals(2, declarations.size());
		assertEquals(declaration1, declarations.get(0));
		assertEquals(declaration2, declarations.get(1));
		assertEquals(0, new MemberDeclarations(10).size());

		// Test accept
		TestDeclarationVisitor visitor = new TestDeclarationVisitor();
		declarations.accept(visitor);
		assertEquals(1, visitor.getMemberDeclarationsCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMemberDeclarationsException() {
		new MemberDeclarations(new StaticInitializerDeclaration("", null)); //$NON-NLS-1$
	}
}
