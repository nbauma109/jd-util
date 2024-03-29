/*
 * Copyright (c) 2008, 2022 Emmanuel Dupuy and other contributors.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;

public class QualifiedSuperExpression extends AbstractLineNumberExpression {
    private final ObjectType type;

    public QualifiedSuperExpression(int lineNumber, ObjectType type) {
        super(lineNumber);
        this.type = type;
    }

    @Override
    public ObjectType getType() {
        return type;
    }

    @Override
    public boolean isSuperExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "QualifiedSuperExpression{" + type + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new QualifiedSuperExpression(lineNumber, type);
    }
}
