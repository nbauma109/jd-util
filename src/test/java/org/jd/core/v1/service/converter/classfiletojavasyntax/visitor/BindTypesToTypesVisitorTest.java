package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.GenericType;
import org.jd.core.v1.model.javasyntax.type.InnerObjectType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.TypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardTypeArgument;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BindTypesToTypesVisitorTest {
	private BindTypesToTypesVisitor visitor;
	private Map<String, TypeArgument> bindings;
	private TypeMaker typeMaker;

	@Before
	public void setup() {
		visitor = new BindTypesToTypesVisitor();
		bindings = new HashMap<String, TypeArgument>();
		typeMaker = new TypeMaker();
		visitor.setBindings(bindings);
	}

	@Test
	public void visitGenericTypeWhenTypeArgumentIsNull() {
		// Given
		GenericType genericType = new GenericType("Test", 0); //$NON-NLS-1$
		bindings.put("Test", null); //$NON-NLS-1$

		// When
		visitor.init();
		genericType.accept(visitor);

		// Then
		BaseType result = visitor.getType();
		assertEquals(ObjectType.TYPE_OBJECT.createType(0), result);
	}

	@Test
	public void visitGenericTypeWhenTypeArgumentIsWildcard() {
		// Given
		GenericType genericType = new GenericType("Test", 0); //$NON-NLS-1$
		bindings.put("Test", WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT); //$NON-NLS-1$

		// When
		visitor.init();
		genericType.accept(visitor);

		// Then
		BaseType result = visitor.getType();
		assertEquals(ObjectType.TYPE_OBJECT.createType(0), result);
	}

	@Test
	public void visitGenericTypeWhenTypeArgumentIsNotWildcardNorNull() {
		// Given
		GenericType genericType = new GenericType("Test", 0); //$NON-NLS-1$
		ObjectType typeArgument = ObjectType.TYPE_OBJECT;
		bindings.put("Test", typeArgument); //$NON-NLS-1$

		// When
		visitor.init();
		genericType.accept(visitor);

		// Then
		BaseType result = visitor.getType();

		// Assuming that result is equivalent to what
		// typeArgumentToTypeVisitor.getType() returns, with increased dimension
		assertEquals(typeArgument.createType(0 + genericType.getDimension()), result);
	}

	@Test
	public void visitInnerObjectTypeWhenResultIsNotEqualOuterTypeAndTypeArgumentsIsNotWildcardNorNull() {
		// Given
		ObjectType outerType = typeMaker.makeFromInternalTypeName("OuterType<T>"); //$NON-NLS-1$
		InnerObjectType innerObjectType = (InnerObjectType) typeMaker.makeFromInternalTypeName("OuterType<T>$InnerType<U>"); //$NON-NLS-1$

		outerType.accept(visitor);

		// When
		visitor.init();
		innerObjectType.accept(visitor);

		// Then
		BaseType result = visitor.getType();

		// Assuming that when typeArguments is not WildcardTypeArgument or null and
		// result is not equal to outerType,
		// the result is a new InnerObjectType with the same typeArguments
		assertEquals(innerObjectType, result);
	}
}
