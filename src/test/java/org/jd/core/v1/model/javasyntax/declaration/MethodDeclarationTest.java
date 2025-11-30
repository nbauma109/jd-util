package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnExpressionStatement;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;

public class MethodDeclarationTest {

	private MethodDeclaration methodDeclaration;

	@Before
	public void setUp() {
		BaseStatement statements = new ReturnExpressionStatement(new StringConstantExpression("Hello World")); //$NON-NLS-1$
		methodDeclaration = new MethodDeclaration(0, "methodName", ObjectType.TYPE_STRING, "()V", statements); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testGetAnnotationReferences() {
		Assert.assertNull(methodDeclaration.getAnnotationReferences());
	}

	@Test
	public void testGetFlags() {
		Assert.assertEquals(0, methodDeclaration.getFlags());
	}

	@Test
	public void testIsStatic() {
		Assert.assertFalse(methodDeclaration.isStatic());
	}

	@Test
	public void testIsPublic() {
		Assert.assertFalse(methodDeclaration.isPublic());
	}

	@Test
	public void testIsAbstract() {
		Assert.assertFalse(methodDeclaration.isAbstract());
	}

	@Test
	public void testGetName() {
		Assert.assertEquals("methodName", methodDeclaration.getName()); //$NON-NLS-1$
	}

	@Test
	public void testGetTypeParameters() {
		Assert.assertNull(methodDeclaration.getTypeParameters());
	}

	@Test
	public void testGetReturnedType() {
		Assert.assertEquals(ObjectType.TYPE_STRING, methodDeclaration.getReturnedType());
	}

	@Test
	public void testGetFormalParameters() {
		Assert.assertNull(methodDeclaration.getFormalParameters());
	}

	@Test
	public void testGetExceptionTypes() {
		Assert.assertNull(methodDeclaration.getExceptionTypes());
	}

	@Test
	public void testGetDescriptor() {
		Assert.assertEquals("()V", methodDeclaration.getDescriptor()); //$NON-NLS-1$
	}

	@Test
	public void testGetStatements() {
		Assert.assertNotNull(methodDeclaration.getStatements());
		Assert.assertTrue(methodDeclaration.getStatements().isReturnExpressionStatement());
		methodDeclaration = new MethodDeclaration(methodDeclaration.getFlags(), methodDeclaration.getName(), methodDeclaration.getReturnedType(), methodDeclaration.getDescriptor());
		Assert.assertNull(methodDeclaration.getStatements());
	}

	@Test
	public void testGetDefaultAnnotationValue() {
		Assert.assertNull(methodDeclaration.getDefaultAnnotationValue());
	}

	@Test
	public void testToString() {
		Assert.assertEquals("MethodDeclaration{methodName ()V}", methodDeclaration.toString()); //$NON-NLS-1$
	}

	@Test
	public void testAccept() {
		TestDeclarationVisitor visitor = new TestDeclarationVisitor();
		methodDeclaration.accept(visitor);

		Assert.assertEquals(1, visitor.getMethodDeclarationCount());
	}
}
