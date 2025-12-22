/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.reference.BaseAnnotationReference;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeParameter;

public class RecordConstructorDeclaration extends ConstructorDeclaration {

    public RecordConstructorDeclaration(BaseAnnotationReference annotationReferences, int flags,
            BaseTypeParameter typeParameters, BaseFormalParameter formalParameters, BaseType exceptionTypes,
            String descriptor, BaseStatement statements) {
        super(annotationReferences, flags, typeParameters, formalParameters, exceptionTypes, descriptor, statements);
    }

}
