/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;

public class BinaryOperatorExpression extends AbstractLineNumberTypeExpression {
    private Expression leftExpression;
    private String operator;
    private Expression rightExpression;
    private int priority;

    public BinaryOperatorExpression(int lineNumber, Type type, Expression leftExpression, String operator, Expression rightExpression, int priority) {
        super(lineNumber, type);
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.priority = priority;
    }

    public BinaryOperatorExpression(int lineNumber, Type type, Expression leftExpression, String operator, Expression rightExpression) {
        this(lineNumber, type, leftExpression, operator, rightExpression, computePriority(operator));
    }

    private static int computePriority(String operator) {
        if (operator == null) {
            return 0;
        }
        return switch (operator) {
            case "*", "/", "%" -> 5;
            case "+", "-" -> 6;
            case "<<", ">>", ">>>" -> 7;
            case "<", "<=", ">", ">=", "instanceof" -> 8;
            case "==", "!=" -> 9;
            case "&" -> 10;
            case "^" -> 11;
            case "|" -> 12;
            case "&&" -> 13;
            case "||" -> 14;

            case "=", "+=", "-=", "*=", "/=", "%=",
                 "<<=", ">>=", ">>>=",
                 "&=", "^=", "|=" -> 16;

            default -> 0;
        };
    }

    @Override
    public Expression getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Expression leftExpression) {
        this.leftExpression = leftExpression;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public Expression getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(Expression rightExpression) {
        this.rightExpression = rightExpression;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isBinaryOperatorExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "BinaryOperatorExpression{" + leftExpression.toString() + ' ' + operator + ' ' + rightExpression.toString() + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new BinaryOperatorExpression(lineNumber, getType(), leftExpression, operator, rightExpression, priority);
    }
}
