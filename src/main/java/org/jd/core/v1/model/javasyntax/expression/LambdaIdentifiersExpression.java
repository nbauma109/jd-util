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

    /**
     * The types the parameters of the lambda have in the class file (the erased types of the parameters of the synthetic method
     * 'lambda$...' javac generates), one per name of {@link #getParameterNames()}; null when the producer of the syntax tree does not know them.
     *
     * <p>They are only printed when {@link #isExplicitParameterTypes()} is set.
     */
    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Type> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * A lambda whose parameters are only named ('params -&gt; params.contains(x)') gets its parameter types from the type javac expects
     * the lambda to be. When nothing gives that type (the lambda is the argument of a generic method whose type variable it alone decides,
     * and the call is the receiver of another call, so no assignment tells it either) javac infers 'Object' and the body no longer compiles.
     * Setting this flag prints the types the lambda was compiled with instead: '(List params) -&gt; params.contains(x)'.
     *
     * @return true if the types of {@link #getParameterTypes()} are printed in front of the names of the parameters
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
