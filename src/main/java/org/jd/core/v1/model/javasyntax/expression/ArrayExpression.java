/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;

public class ArrayExpression extends AbstractLineNumberTypeExpression {
    private Expression expression;
    private Expression index;

    public ArrayExpression(Expression expression, Expression index) {
        this(UNKNOWN_LINE_NUMBER, expression, index);
    }

    public ArrayExpression(int lineNumber, Expression expression, Expression index) {
        super(lineNumber, createItemType(expression));
        this.expression = expression;
        this.index = index;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    protected static Type createItemType(Expression expression) {
        Type type = expression.getType();
        int dimension = type.getDimension();

        return type.createType(dimension > 0 ? dimension-1 : 0);
    }

    @Override
    public boolean isArrayExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ArrayExpression{" + expression + "[" + index + "]}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new ArrayExpression(lineNumber, expression, index);
    }
}
