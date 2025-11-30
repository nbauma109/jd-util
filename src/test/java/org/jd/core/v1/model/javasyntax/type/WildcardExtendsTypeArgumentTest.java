package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WildcardExtendsTypeArgumentTest {

	@Test
	public void test() throws Exception {
		TypeMaker typeMaker = new TypeMaker();
		WildcardExtendsTypeArgument wildcardExtendsTypeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_NUMBER);
		assertTrue(wildcardExtendsTypeArgument.isTypeArgumentAssignableFrom(typeMaker, null, null, new WildcardExtendsTypeArgument(ObjectType.TYPE_NUMBER)));
		assertFalse(wildcardExtendsTypeArgument.isTypeArgumentAssignableFrom(typeMaker, null, null, WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT));
		assertFalse(wildcardExtendsTypeArgument.isTypeArgumentAssignableFrom(typeMaker, null, null, PrimitiveType.TYPE_INT));
	}
}
