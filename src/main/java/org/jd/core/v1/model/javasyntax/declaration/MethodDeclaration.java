/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.reference.BaseAnnotationReference;
import org.jd.core.v1.model.javasyntax.reference.BaseElementValue;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeParameter;
import org.jd.core.v1.model.javasyntax.type.Type;

import static org.apache.bcel.Const.ACC_ABSTRACT;
import static org.apache.bcel.Const.ACC_PUBLIC;
import static org.apache.bcel.Const.ACC_STATIC;

public class MethodDeclaration implements MemberDeclaration {
    private final BaseAnnotationReference annotationReferences;
    protected int flags;
    protected final String name;
    private final BaseTypeParameter typeParameters;
    private final Type returnedType;
    protected BaseFormalParameter formalParameters;
    private final BaseType exceptionTypes;
    protected final String descriptor;
    protected BaseStatement statements;
    private final BaseElementValue defaultAnnotationValue;

    public MethodDeclaration(int flags, String name, Type returnedType, String descriptor) {
        this(flags, name, returnedType, descriptor, null);
    }

    public MethodDeclaration(int flags, String name, Type returnedType, String descriptor, BaseStatement statements) {
        this(flags, name, returnedType, null, descriptor, statements);
    }

    public MethodDeclaration(int flags, String name, Type returnedType, BaseFormalParameter formalParameters, String descriptor, BaseStatement statements) {
        this(null, flags, name, null, returnedType, formalParameters, null, descriptor, statements, null);
    }

    public MethodDeclaration(BaseAnnotationReference annotationReferences, int flags, String name, BaseTypeParameter typeParameters, Type returnedType, BaseFormalParameter formalParameters, BaseType exceptionTypes, String descriptor, BaseStatement statements, BaseElementValue defaultAnnotationValue) {
        this.annotationReferences = annotationReferences;
        this.flags = flags;
        this.name = name;
        this.typeParameters = typeParameters;
        this.returnedType = returnedType;
        this.formalParameters = formalParameters;
        this.exceptionTypes = exceptionTypes;
        this.descriptor = descriptor;
        this.statements = statements;
        this.defaultAnnotationValue = defaultAnnotationValue;
    }

    public BaseAnnotationReference getAnnotationReferences() {
        return annotationReferences;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isStatic() { return (flags & ACC_STATIC) != 0; }
    public boolean isPublic() { return (flags & ACC_PUBLIC) != 0; }
    public boolean isAbstract() { return (flags & ACC_ABSTRACT) != 0; }

    public String getName() {
        return name;
    }

    public BaseTypeParameter getTypeParameters() {
        return typeParameters;
    }

    public Type getReturnedType() {
        return returnedType;
    }

    public BaseFormalParameter getFormalParameters() {
        return formalParameters;
    }

    public BaseType getExceptionTypes() {
        return exceptionTypes;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public BaseStatement getStatements() {
        return statements;
    }

    public BaseElementValue getDefaultAnnotationValue() {
        return defaultAnnotationValue;
    }

    @Override
    public void accept(DeclarationVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "MethodDeclaration{" + name + " " + descriptor + "}";
    }
}
