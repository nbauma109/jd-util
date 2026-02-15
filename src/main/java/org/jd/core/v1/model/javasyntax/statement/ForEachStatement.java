/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.pattern.Pattern;
import org.jd.core.v1.model.javasyntax.pattern.TypePattern;
import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.Objects;

public class ForEachStatement implements Statement {
    protected final Pattern pattern;
    private boolean fina1;
    protected Expression expression;
    private final BaseStatement statements;

    public ForEachStatement(Type type, String name, Expression expression, BaseStatement statements) {
        this(new TypePattern(type, name), expression, statements);
    }

    public ForEachStatement(Pattern pattern, Expression expression, BaseStatement statements) {
        this.pattern = Objects.requireNonNull(pattern, "pattern");
        this.setExpression(expression);
        this.statements = statements;
    }

    public boolean isFinal() {
        return fina1;
    }

    public void setFinal(boolean fina1) {
        this.fina1 = fina1;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Type getType() {
        return pattern.type();
    }

    public String getName() {
        return pattern.variableName();
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public BaseStatement getStatements() {
        return statements;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }
}
