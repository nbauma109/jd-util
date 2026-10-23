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
 */
public class TypeUseAnnotationReference extends AnnotationReference {
    public TypeUseAnnotationReference(ObjectType type) {
        super(type);
    }
}
