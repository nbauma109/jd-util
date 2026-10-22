/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.model.javasyntax.reference.BaseAnnotationReference;

public class TypeParameterWithTypeBounds extends TypeParameter {
    private final BaseType typeBounds;
    /** The type annotations written in front of the bounds: 'K extends @Nullable Object' */
    private final BaseAnnotationReference boundAnnotations;

    public TypeParameterWithTypeBounds(String identifier, BaseType typeBounds) {
        this(identifier, typeBounds, null);
    }

    public TypeParameterWithTypeBounds(String identifier, BaseType typeBounds, BaseAnnotationReference boundAnnotations) {
        super(identifier);
        this.typeBounds = typeBounds;
        this.boundAnnotations = boundAnnotations;
    }

    public BaseAnnotationReference getBoundAnnotations() {
        return boundAnnotations;
    }

    public BaseType getTypeBounds() {
        return typeBounds;
    }

    @Override
    public void accept(TypeParameterVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "TypeParameter{identifier=" + identifier + ", typeBounds=" + typeBounds + "}";
    }
}
