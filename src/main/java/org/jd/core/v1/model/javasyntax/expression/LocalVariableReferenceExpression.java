/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;

public class LocalVariableReferenceExpression extends AbstractLineNumberTypeExpression {
    private String name;

    public LocalVariableReferenceExpression(Type type, String name) {
        this(UNKNOWN_LINE_NUMBER, type, name);
    }

    public LocalVariableReferenceExpression(int lineNumber, Type type, String name) {
        super(lineNumber, type);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isLocalVariableReferenceExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "LocalVariableReferenceExpression{type=" + getType() + ", name=" + name + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new LocalVariableReferenceExpression(lineNumber, getType(), name);
    }
}
