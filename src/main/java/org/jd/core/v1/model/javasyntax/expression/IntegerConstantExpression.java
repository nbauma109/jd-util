/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.PrimitiveTypeUtil;

public class IntegerConstantExpression extends AbstractLineNumberTypeExpression {
    private final int value;

    public IntegerConstantExpression(Type type, int value) {
        this(UNKNOWN_LINE_NUMBER, type, value);
    }

    public IntegerConstantExpression(int lineNumber, Type type, int value) {
        super(lineNumber, type);
        this.value = value;
    }

    public IntegerConstantExpression(int lineNumber, int value) {
        this(lineNumber, PrimitiveTypeUtil.getPrimitiveTypeFromValue(value), value);
    }

    public IntegerConstantExpression(int value) {
        this(UNKNOWN_LINE_NUMBER, value);
    }

    @Override
    public int getIntegerValue() {
        return value;
    }

    @Override
    public void setType(Type type) {
        if (!checkType(type)) {
            throw new IllegalArgumentException("IntegerConstantExpression.setType(type) : incompatible types");
        }
        super.setType(type);
    }

    protected boolean checkType(Type type) {
        if (type.isPrimitiveType()) {
            PrimitiveType valueType = PrimitiveTypeUtil.getPrimitiveTypeFromValue(value);
            return (((PrimitiveType)type).getFlags() & valueType.getFlags()) != 0;
        }

        return false;
    }

    @Override
    public boolean isIntegerConstantExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "IntegerConstantExpression{type=" + getType() + ", value=" + value + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new IntegerConstantExpression(lineNumber, getType(), value);
    }
}
