package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnExpressionStatement;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StaticInitializerDeclarationTest {

	@Test
	public void testStaticInitializerDeclaration() {
		// Arrange
		String descriptor = "void"; //$NON-NLS-1$
		IntegerConstantExpression returnExpression = new IntegerConstantExpression(1);
		BaseStatement baseStatement = new ReturnExpressionStatement(returnExpression);

		// Act
		StaticInitializerDeclaration staticInitializerDeclaration = new StaticInitializerDeclaration(descriptor, baseStatement);

		// Assert
		assertEquals(descriptor, staticInitializerDeclaration.getDescriptor());
		assertEquals(baseStatement, staticInitializerDeclaration.getStatements());
	}

	@Test
	public void testAccept() {
		// Arrange
		String descriptor = "void"; //$NON-NLS-1$
		IntegerConstantExpression returnExpression = new IntegerConstantExpression(1);
		BaseStatement baseStatement = new ReturnExpressionStatement(returnExpression);
		StaticInitializerDeclaration staticInitializerDeclaration = new StaticInitializerDeclaration(descriptor, baseStatement);

		TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
		staticInitializerDeclaration.accept(testVisitor);

		// Assert
		assertEquals(1, testVisitor.getStaticInitializerDeclarationCount());
	}

	@Test
	public void testToString() {
		// Arrange
		String descriptor = "void"; //$NON-NLS-1$
		IntegerConstantExpression returnExpression = new IntegerConstantExpression(1);
		BaseStatement baseStatement = new ReturnExpressionStatement(returnExpression);
		StaticInitializerDeclaration staticInitializerDeclaration = new StaticInitializerDeclaration(descriptor, baseStatement);

		// Act
		String result = staticInitializerDeclaration.toString();

		// Assert
		assertEquals("StaticInitializerDeclaration{}", result); //$NON-NLS-1$
	}
}
