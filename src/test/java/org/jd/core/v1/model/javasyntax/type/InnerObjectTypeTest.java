package org.jd.core.v1.model.javasyntax.type;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerObjectTypeTest {

	@Test
	public void testInnerObjectType() {
		String internalName = "Inner"; //$NON-NLS-1$
		String qualifiedName = "org.jd.core.v1.model.javasyntax.type.Inner"; //$NON-NLS-1$
		String name = "InnerClass"; //$NON-NLS-1$
		ObjectType outerType = ObjectType.TYPE_CLASS;

		Set<String> innerTypeNames = new HashSet<String>();
		innerTypeNames.add("InnerClass"); //$NON-NLS-1$

		InnerObjectType innerObjectType = new InnerObjectType(internalName, qualifiedName, name, innerTypeNames, outerType);

		// Test getOuterType
		assertEquals(outerType, innerObjectType.getOuterType());

		// Test createType with dimension
		Type type = innerObjectType.createType(1);
		assertTrue(type instanceof InnerObjectType);

		// Test isInnerObjectType
		assertTrue(innerObjectType.isInnerObjectType());

		// Test isInnerObjectTypeArgument
		assertTrue(innerObjectType.isInnerObjectTypeArgument());

		// Test toString
		assertEquals("InnerObjectType{" + outerType + "." + innerObjectType.getDescriptor() + "}", innerObjectType.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// Create a BaseTypeArgument
		WildcardExtendsTypeArgument wildcardExtendsTypeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_CLASS);

		// Pass it as an argument when you create a new InnerObjectType
		InnerObjectType innerObjectTypeWithArgument = new InnerObjectType(internalName, qualifiedName, name, wildcardExtendsTypeArgument, outerType);

		// Test toString with typeArguments
		assertEquals("InnerObjectType{" + outerType + "." + innerObjectTypeWithArgument.getDescriptor() + "<" + wildcardExtendsTypeArgument + ">}", innerObjectTypeWithArgument.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckArguments() {
		new InnerObjectType("1Inner", "org.jd.core.v1.model.javasyntax.type.1Inner", "1InnerClass", ObjectType.TYPE_CLASS); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTypeNegativeDimension() {
		InnerObjectType innerObjectType = new InnerObjectType("Inner", "org.jd.core.v1.model.javasyntax.type.Inner", "InnerClass", ObjectType.TYPE_CLASS); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		innerObjectType.createType(-1);
	}
}
