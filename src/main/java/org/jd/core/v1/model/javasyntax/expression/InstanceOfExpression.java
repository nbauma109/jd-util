/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.pattern.Pattern;
import org.jd.core.v1.model.javasyntax.pattern.TypePattern;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;

public class InstanceOfExpression extends AbstractLineNumberExpression {
    private Expression expression;
    private final Pattern pattern;

    public InstanceOfExpression(int lineNumber, Expression expression, Type instanceOfType, String variableName, boolean isFinal) {
        this(lineNumber, expression, new TypePattern(instanceOfType, variableName, isFinal));
    }

    public InstanceOfExpression(int lineNumber, Expression expression, Pattern pattern) {
        super(lineNumber);
        this.setExpression(expression);
        this.pattern = pattern;
    }

    public InstanceOfExpression(int lineNumber, Expression expression, Type instanceOfType) {
        this(lineNumber, expression, instanceOfType, null, false);
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Type getInstanceOfType() {
        return pattern.getType();
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Type getType() {
        return PrimitiveType.TYPE_BOOLEAN;
    }

    @Override
    public int getPriority() {
        return 8;
    }

    public boolean isFinal() {
        return pattern instanceof TypePattern typePattern && typePattern.isFinal();
    }

    public String getVariableName() {
        return pattern.getVariableName();
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new InstanceOfExpression(lineNumber, expression, pattern);
    }
}
