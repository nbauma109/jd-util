/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;

public class CastExpression extends AbstractLineNumberTypeExpression {
    private Expression expression;
    private boolean explicit;
    private boolean byteCodeCheckCast;

    public CastExpression(Type type, Expression expression) {
        this(UNKNOWN_LINE_NUMBER, type, expression);
    }

    public CastExpression(int lineNumber, Type type, Expression expression) {
        this(lineNumber, type, expression, true);
    }

    public CastExpression(int lineNumber, Type type, Expression expression, boolean explicit) {
        this(lineNumber, type, expression, explicit, false);
    }
    
    public CastExpression(int lineNumber, Type type, Expression expression, boolean explicit, boolean byteCodeCheckCast) {
        super(lineNumber, type);
        this.expression = expression;
        this.explicit = explicit;
        this.byteCodeCheckCast = byteCodeCheckCast;
    }
    
    @Override
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public boolean isByteCodeCheckCast() {
        return byteCodeCheckCast;
    }

    public void setByteCodeCheckCast(boolean byteCodeCheckCast) {
        this.byteCodeCheckCast = byteCodeCheckCast;
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isCastExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "CastExpression{cast (" + getType() + ") " + expression + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new CastExpression(lineNumber, getType(), expression, explicit, byteCodeCheckCast);
    }
}
