package org.jd.core.v1.model.javasyntax.declaration;

import org.apache.bcel.Const;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Types;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.jd.core.v1.util.StringConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InterfaceDeclarationTest {

	private InterfaceDeclaration interfaceDeclaration;
	private TestDeclarationVisitor visitor;

	@Before
	public void setUp() {
		ObjectType cloneableType = new ObjectType(StringConstants.JAVA_LANG_CLONEABLE, "java.lang.Cloneable", "Cloneable"); //$NON-NLS-1$ //$NON-NLS-2$
		ObjectType stringType = new ObjectType(StringConstants.JAVA_LANG_STRING, "java.lang.String", "String"); //$NON-NLS-1$ //$NON-NLS-2$
		ObjectType listType = new ObjectType("java/util/List", "java.util.List", "List", stringType); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		interfaceDeclaration = new InterfaceDeclaration(
				Const.ACC_PUBLIC,
				"org/jd/core/v1/service/test/InterfaceTest", //$NON-NLS-1$
				"InterfaceTest", //$NON-NLS-1$
				new Types(listType, cloneableType)
				);

		visitor = new TestDeclarationVisitor();
	}

	@Test
	public void testInterfaceDeclaration() {
		// Act
		interfaceDeclaration.accept(visitor);

		// Assert
		assertEquals(1, visitor.getInterfaceDeclarationCount());
		assertNull(interfaceDeclaration.getTypeParameters());
		assertEquals(2, ((Types)interfaceDeclaration.getInterfaces()).size());
	}

	@Test
	public void testToString() {
		// Act & Assert
		assertEquals("InterfaceDeclaration{org/jd/core/v1/service/test/InterfaceTest}", interfaceDeclaration.toString()); //$NON-NLS-1$
	}
}
