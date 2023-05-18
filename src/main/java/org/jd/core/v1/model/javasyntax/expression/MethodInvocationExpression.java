/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeArgument;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeArgument;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.MethodTypes;

import java.util.Map;
import java.util.Optional;

public class MethodInvocationExpression extends MethodReferenceExpression {
    private BaseTypeArgument nonWildcardTypeArguments;
    private BaseExpression parameters;
    private final MethodTypes methodTypes;
    private Map<String, BaseType> typeBounds;
    private Map<String, TypeArgument> typeBindings;

    public MethodInvocationExpression(Type type, Expression expression, String internalTypeName, String name, String descriptor, MethodTypes methodTypes) {
        this(type, expression, internalTypeName, name, descriptor, null, methodTypes);
    }

    public MethodInvocationExpression(Type type, Expression expression, String internalTypeName, String name, String descriptor, BaseExpression parameters, MethodTypes methodTypes) {
        this(UNKNOWN_LINE_NUMBER, type, expression, internalTypeName, name, descriptor, parameters, methodTypes);
    }

    public MethodInvocationExpression(int lineNumber, Type type, Expression expression, String internalTypeName, String name, String descriptor, BaseExpression parameters, MethodTypes methodTypes) {
        super(lineNumber, type, expression, internalTypeName, name, descriptor);
        this.parameters = parameters;
        this.methodTypes = methodTypes;
    }

    public boolean isVarArgs() {
        return Optional.ofNullable(methodTypes).map(MethodTypes::isVarArgs).orElse(false);
    }

    public BaseType getExceptionTypes() {
        return Optional.ofNullable(methodTypes).map(MethodTypes::getExceptionTypes).orElse(null);
    }
    
    public BaseTypeArgument getNonWildcardTypeArguments() {
        return nonWildcardTypeArguments;
    }

    public void setNonWildcardTypeArguments(BaseTypeArgument nonWildcardTypeArguments) {
        this.nonWildcardTypeArguments = nonWildcardTypeArguments;
    }

    @Override
    public BaseExpression getParameters() {
        return parameters;
    }

    public void setParameters(BaseExpression parameters) {
        this.parameters = parameters;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    public Map<String, BaseType> getTypeBounds() {
        return typeBounds;
    }
    
    public void setTypeBounds(Map<String, BaseType> typeBounds) {
        this.typeBounds = typeBounds;
    }

    public Map<String, TypeArgument> getTypeBindings() {
        return typeBindings;
    }

    public void setTypeBindings(Map<String, TypeArgument> typeBindings) {
        this.typeBindings = typeBindings;
    }

    @Override
    public boolean isMethodInvocationExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "MethodInvocationExpression{call " + expression + " . " + name + "(" + descriptor + ")}";
    }
}
