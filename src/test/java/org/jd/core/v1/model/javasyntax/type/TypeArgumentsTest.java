package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TypeArgumentsTest {

	@Test
	public void test() throws Exception {
		TypeArguments typeArguments = new TypeArguments(Arrays.asList(ObjectType.TYPE_OBJECT, ObjectType.TYPE_STRING));
		assertTrue(typeArguments.isList());
		assertEquals(typeArguments.size(), typeArguments.typeArgumentSize());
		assertEquals(typeArguments.getFirst(), typeArguments.getTypeArgumentFirst());
		assertFalse(typeArguments.isTypeArgumentAssignableFrom(null, null, null, ObjectType.TYPE_OBJECT));
		assertFalse(typeArguments.isTypeArgumentAssignableFrom(null, null, null, new TypeArguments(Collections.singletonList(ObjectType.TYPE_OBJECT))));
		assertFalse(typeArguments.isTypeArgumentAssignableFrom(null, null, null, new TypeArguments(Arrays.asList(ObjectType.TYPE_OBJECT, ObjectType.TYPE_OBJECT))));
	}
}
