package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WildcardSuperTypeArgumentTest {

	@Test
	public void test() throws Exception {
		TypeMaker typeMaker = new TypeMaker();
		WildcardSuperTypeArgument wildcardSuperTypeArgument = new WildcardSuperTypeArgument(ObjectType.TYPE_NUMBER);
		assertTrue(wildcardSuperTypeArgument.isTypeArgumentAssignableFrom(typeMaker, null, null, new WildcardSuperTypeArgument(ObjectType.TYPE_NUMBER)));
		assertFalse(wildcardSuperTypeArgument.isTypeArgumentAssignableFrom(typeMaker, null, null, WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT));
		assertFalse(wildcardSuperTypeArgument.isTypeArgumentAssignableFrom(typeMaker, null, null, PrimitiveType.TYPE_INT));
	}
}
