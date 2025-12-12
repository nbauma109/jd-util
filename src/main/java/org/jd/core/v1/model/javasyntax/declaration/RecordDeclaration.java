/*
 * Copyright (c) 2008-2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.reference.BaseAnnotationReference;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeParameter;
import org.jd.core.v1.model.javasyntax.type.ObjectType;

import java.util.List;

import static org.apache.bcel.Const.ACC_FINAL;

public class RecordDeclaration extends TypeDeclaration {
    private final BaseTypeParameter typeParameters;
    private final List<RecordComponent> components;
    private final BaseType interfaces;

    public RecordDeclaration(BaseAnnotationReference annotationReferences,
                             int flags,
                             String internalName,
                             String name,
                             BaseTypeParameter typeParameters,
                             List<RecordComponent> components,
                             BaseType interfaces,
                             BodyDeclaration bodyDeclaration) {
        super(annotationReferences, ensureFinal(flags), internalName, name, bodyDeclaration);
        this.typeParameters = typeParameters;
        this.components = components;
        this.interfaces = interfaces;
    }

    public BaseTypeParameter getTypeParameters() {
        return typeParameters;
    }

    public List<RecordComponent> getComponents() {
        return components;
    }

    public BaseType getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean isClassDeclaration() {
        return true;
    }

    @Override
    public void accept(DeclarationVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "RecordDeclaration{" + getInternalTypeName() + "}";
    }

    private static int ensureFinal(int flags) {
        return flags | ACC_FINAL;
    }

    // --- Record Component ---
    public static class RecordComponent {
        private final BaseAnnotationReference annotationReferences;
        private final org.jd.core.v1.model.javasyntax.type.Type type;
        private final String name;

        public RecordComponent(BaseAnnotationReference annotationReferences,
                               org.jd.core.v1.model.javasyntax.type.Type type,
                               String name) {
            this.annotationReferences = annotationReferences;
            this.type = type;
            this.name = name;
        }

        public BaseAnnotationReference getAnnotationReferences() {
            return annotationReferences;
        }

        public org.jd.core.v1.model.javasyntax.type.Type getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    public BaseType getSuperType() {
        return ObjectType.TYPE_RECORD;
    }
}
