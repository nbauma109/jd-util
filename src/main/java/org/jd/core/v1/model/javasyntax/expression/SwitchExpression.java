/*
 * Copyright (c) 2008-2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.pattern.Pattern;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SwitchExpression extends AbstractLineNumberExpression {
    public static final DefaultLabel DEFAULT_LABEL = new DefaultLabel();

    private final Expression selector;
    private final List<Rule> rules;
    private final Type type;

    public SwitchExpression(int lineNumber, Expression selector, List<Rule> rules, Type type) {
        super(lineNumber);
        this.selector = selector;
        this.rules = rules;
        this.type = type;
    }

    public Expression getSelector() {
        return selector;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new SwitchExpression(lineNumber, selector, rules, type);
    }

    @Override
    public String toString() {
        return "SwitchExpression{" + selector + "}";
    }

    // ----------------------------------------------------------------------
    // Labels
    // ----------------------------------------------------------------------

    public interface Label {
        void accept(ExpressionVisitor visitor);
    }

    public static final class DefaultLabel implements Label {
        protected DefaultLabel() {}

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "DefaultLabel";
        }
    }

    public static final class ExpressionLabel implements Label {
        private Expression expression;

        public ExpressionLabel(Expression expression) {
            this.expression = expression;
        }

        public Expression getExpression() {
            return expression;
        }

        public void setExpression(Expression expression) {
            this.expression = expression;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "ExpressionLabel{" + expression + '}';
        }
    }

    public static final class PatternLabel implements Label {
        private Pattern pattern;

        public PatternLabel(Pattern pattern) {
            this.pattern = Objects.requireNonNull(pattern, "pattern");
        }

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = Objects.requireNonNull(pattern, "pattern");
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "PatternLabel{" + pattern + '}';
        }
    }

    // ----------------------------------------------------------------------
    // Rules
    // ----------------------------------------------------------------------

    public interface Rule {
        List<Label> getLabels();
        void accept(ExpressionVisitor visitor);
    }

    /**
     * case A -> expr
     */
    public static final class RuleExpression implements Rule {
        private final List<Label> labels;
        private final Expression whenCondition;
        private Expression expression;

        public RuleExpression(final List<Label> labels, final Expression expression) {
            this(labels, NoExpression.NO_EXPRESSION, expression);
        }

        public RuleExpression(final List<Label> labels, final Expression whenCondition, final Expression expression) {
            this.labels = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(labels, "labels")));
            this.whenCondition = Objects.requireNonNull(whenCondition, "whenCondition");
            this.expression = Objects.requireNonNull(expression, "expression");
        }

        @Override
        public List<Label> getLabels() {
            return labels;
        }

        public Expression getWhenCondition() {
            return whenCondition;
        }

        public Expression getExpression() {
            return expression;
        }

        public void setExpression(Expression expression) {
            this.expression = expression;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "RuleExpression{" + labels + " -> " + expression + '}';
        }
    }

    /**
     * case A -> { statements }
     * must contain YieldExpressionStatement
     */
    public static final class RuleStatement implements Rule {
        private final List<Label> labels;
        private final Expression whenCondition;
        private final BaseStatement statements;

        public RuleStatement(List<Label> labels, BaseStatement statements) {
            this(labels, NoExpression.NO_EXPRESSION, statements);
        }

        public RuleStatement(List<Label> labels, Expression whenCondition, BaseStatement statements) {
            this.labels = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(labels, "labels")));
            this.whenCondition = Objects.requireNonNull(whenCondition, "whenCondition");
            this.statements = Objects.requireNonNull(statements, "statements");
        }

        public Expression getWhenCondition() {
            return whenCondition;
        }

        @Override
        public List<Label> getLabels() {
            return labels;
        }

        public BaseStatement getStatements() {
            return statements;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "RuleStatement{" + labels + ": " + statements + '}';
        }
    }
}
