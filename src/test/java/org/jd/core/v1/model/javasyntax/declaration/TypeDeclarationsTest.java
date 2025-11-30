package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeDeclarationsTest {

	@Test
	public void test() throws Exception {
		TypeDeclarations typeDeclarations = new TypeDeclarations();
		TestDeclarationVisitor declarationVisitor = new TestDeclarationVisitor();
		typeDeclarations.accept(declarationVisitor);
		assertEquals(1, declarationVisitor.getTypeDeclarationsCount());
	}
}
