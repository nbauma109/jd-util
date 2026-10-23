/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.reference;

import org.jd.core.v1.model.javasyntax.type.ObjectType;

/**
 * An annotation which is on the type of a declaration ('@Nullable String value'), as the class file keeps it apart from the annotations of the
 * declaration. It is written inside the type, where it belongs when the name of the type has a qualifier: 'java.lang.@Nullable String'.
 * <p>
 * It is the annotation of the type as a whole, the one a class file has with an empty type path: for an array, the array itself
 * ('String @Nullable []'), not its elements. The annotations of the other parts of a type (the elements of an array, a type argument, a bound)
 * are not represented.
 */
public class TypeUseAnnotationReference extends AnnotationReference {
    public TypeUseAnnotationReference(ObjectType type) {
        super(type);
    }

    public TypeUseAnnotationReference(ObjectType type, BaseElementValue elementValue) {
        super(type, elementValue);
    }

    public TypeUseAnnotationReference(ObjectType type, BaseElementValuePair elementValuePairs) {
        super(type, elementValuePairs);
    }

    public TypeUseAnnotationReference(ObjectType type, BaseElementValue elementValue, BaseElementValuePair elementValuePairs) {
        super(type, elementValue, elementValuePairs);
    }
}
