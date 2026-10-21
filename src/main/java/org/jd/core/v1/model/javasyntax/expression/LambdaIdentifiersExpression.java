/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.List;

public class LambdaIdentifiersExpression extends AbstractLambdaExpression {
    private final Type returnedType;
    private final List<String> parameterNames;
    private List<Type> parameterTypes;
    private boolean explicitParameterTypes;

    public LambdaIdentifiersExpression(int lineNumber, Type type, Type returnedType, List<String> parameterNames, BaseStatement statements) {
        super(lineNumber, type, statements);
        this.returnedType = returnedType;
        this.parameterNames = parameterNames;
    }

    public Type getReturnedType() {
        return returnedType;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    /** The erased types the lambda was compiled with, one per parameter name; null if unknown. Only printed when {@link #isExplicitParameterTypes()}. */
    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Type> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * Print the parameter types: '(List params) -&gt; ...'. Needed when nothing types an implicit lambda (it is the argument of a generic
     * call which is the receiver of another call): javac would infer Object for the parameters.
     */
    public boolean isExplicitParameterTypes() {
        return explicitParameterTypes;
    }

    public void setExplicitParameterTypes(boolean explicitParameterTypes) {
        this.explicitParameterTypes = explicitParameterTypes;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "LambdaIdentifiersExpression{" + parameterNames + " -> " + statements + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        LambdaIdentifiersExpression copy = new LambdaIdentifiersExpression(lineNumber, getType(), returnedType, parameterNames, statements);

        copy.parameterTypes = parameterTypes;
        copy.explicitParameterTypes = explicitParameterTypes;
        return copy;
    }
}
