/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.javasyntax.type;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class InnerObjectType extends ObjectType {
	private ObjectType outerType;

	public InnerObjectType(String internalName, String qualifiedName, String name, Set<String> innerTypeNames, ObjectType outerType) {
		super(internalName, qualifiedName, name, innerTypeNames);
		setOuterType(outerType);
		this.checkArguments(qualifiedName, name);
	}

	public InnerObjectType(String internalName, String qualifiedName, String name, Set<String> innerTypeNames, BaseTypeArgument typeArguments, int dimension, ObjectType outerType) {
		super(internalName, qualifiedName, name, innerTypeNames, typeArguments, dimension);
		setOuterType(outerType);
		this.checkArguments(qualifiedName, name);
	}

	public InnerObjectType(String internalName, String qualifiedName, String name, ObjectType outerType) {
		super(internalName, qualifiedName, name, Collections.emptySet());
		setOuterType(outerType);
		this.checkArguments(qualifiedName, name);
	}

	public InnerObjectType(String internalName, String qualifiedName, String name, BaseTypeArgument typeArguments, ObjectType outerType) {
		super(internalName, qualifiedName, name, Collections.emptySet(), typeArguments);
		setOuterType(outerType);
		this.checkArguments(qualifiedName, name);
	}

	public InnerObjectType(String internalName, String qualifiedName, String name, BaseTypeArgument typeArguments, int dimension, ObjectType outerType) {
		super(internalName, qualifiedName, name, Collections.emptySet(), typeArguments, dimension);
		this.outerType = outerType;
		this.checkArguments(qualifiedName, name);
	}

	protected void checkArguments(String qualifiedName, String name) {
		if (name != null && Character.isDigit(name.charAt(0)) && qualifiedName != null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public ObjectType getOuterType() {
		return outerType;
	}

	public void setOuterType(ObjectType outerType) {
		this.outerType = outerType;
	}

	@Override
	public Type createType(int dimension) {
		if (dimension < 0) {
			throw new IllegalArgumentException("InnerObjectType.createType(dim) : create type with negative dimension"); //$NON-NLS-1$
		}
		return new InnerObjectType(internalName, qualifiedName, name, innerTypeNames, typeArguments, dimension, outerType);
	}

	@Override
	public ObjectType createType(BaseTypeArgument typeArguments) {
		return new InnerObjectType(internalName, qualifiedName, name, innerTypeNames, typeArguments, dimension, outerType);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		InnerObjectType that = (InnerObjectType) o;
		return Objects.equals(outerType, that.outerType);
	}

	@Override
	public int hashCode() {
		int result = 111_476_860 + super.hashCode();
		return 31 * result + Objects.hashCode(outerType);
	}

	@Override
	public boolean isInnerObjectType() {
		return true;
	}

	@Override
	public boolean isInnerObjectTypeArgument() {
		return true;
	}

	@Override
	public void accept(TypeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void accept(TypeArgumentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		if (typeArguments == null) {
			return "InnerObjectType{" + outerType + "." + descriptor + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return "InnerObjectType{" + outerType + "." + descriptor + "<" + typeArguments + ">}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
