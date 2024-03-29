/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;

public class MethodReferenceExpression extends AbstractLineNumberTypeExpression {
    protected Expression expression;
    private final String internalTypeName;
    protected final String name;
    protected final String descriptor;

    public MethodReferenceExpression(Type type, Expression expression, String internalTypeName, String name, String descriptor) {
        this(UNKNOWN_LINE_NUMBER, type, expression, internalTypeName, name, descriptor);
    }

    public MethodReferenceExpression(int lineNumber, Type type, Expression expression, String internalTypeName, String name, String descriptor) {
        super(lineNumber, type);
        this.setExpression(expression);
        this.internalTypeName = internalTypeName;
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String getInternalTypeName() {
        return internalTypeName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new MethodReferenceExpression(lineNumber, getType(), expression, internalTypeName, name, descriptor);
    }

    @Override
    public String toString() {
        return "MethodReferenceExpression{call " + expression + " :: " + name + "(" + descriptor + ")}";
    }
}
