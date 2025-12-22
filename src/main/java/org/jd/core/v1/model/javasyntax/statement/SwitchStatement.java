/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.NoExpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SwitchStatement implements Statement {
    public static final DefaultLabel DEFAULT_LABEL = new DefaultLabel();

    private Expression condition;
    private final List<Block> blocks;

    public SwitchStatement(Expression condition, List<Block> blocks) {
        this.condition = condition;
        this.blocks = blocks;
    }

    @Override
    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public boolean isSwitchStatement() { return true; }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    // --- Label --- //
    public interface Label extends Statement {
        void accept(StatementVisitor visitor);
    }

    public static class DefaultLabel implements Label {
        protected DefaultLabel() {}

        @Override
        public void accept(StatementVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "DefaultLabel";
        }
    }

    public static class ExpressionLabel implements Label {
        private Expression expression;

        public ExpressionLabel(Expression expression) {
            this.expression = expression;
        }

        @Override
        public Expression getExpression() {
            return expression;
        }

        public void setExpression(Expression expression) {
            this.expression = expression;
        }

        @Override
        public void accept(StatementVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "ExpressionLabel{" + expression.toString() + '}';
        }
    }

    // --- Block --- //
    public abstract static class Block implements Statement {
        protected final BaseStatement statements;
        protected final Expression whenCondition;

        protected Block(BaseStatement statements) {
            this(statements, NoExpression.NO_EXPRESSION);
        }

        protected Block(BaseStatement statements, Expression whenCondition) {
            this.statements = Objects.requireNonNull(statements, "statements");
            this.whenCondition = Objects.requireNonNull(whenCondition, "whenCondition");
        }

        @Override
        public BaseStatement getStatements() {
            return statements;
        }

        public Expression getWhenCondition() {
            return whenCondition;
        }
    }

    public static final class LabelBlock extends Block {
        private final Label label;

        public LabelBlock(Label label, BaseStatement statements) {
            this(label, NoExpression.NO_EXPRESSION, statements);
        }

        public LabelBlock(Label label, Expression whenCondition, BaseStatement statements) {
            super(statements, whenCondition);
            this.label = Objects.requireNonNull(label, "label");
        }

        public Label getLabel() {
            return label;
        }

        @Override
        public boolean isSwitchStatementLabelBlock() { return true; }

        @Override
        public void accept(StatementVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "LabelBlock{label=" + label.toString() + '}';
        }
    }

    public static final class MultiLabelsBlock extends Block {
        private final List<Label> labels;

        public MultiLabelsBlock(List<Label> labels, BaseStatement statements) {
            this(labels, NoExpression.NO_EXPRESSION, statements);
        }

        public MultiLabelsBlock(List<Label> labels, Expression whenCondition,
                final BaseStatement statements) {
            super(statements, whenCondition);
            this.labels = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(labels, "labels")));
        }

        public List<Label> getLabels() {
            return labels;
        }

        @Override
        public boolean isSwitchStatementMultiLabelsBlock() { return true; }

        @Override
        public void accept(StatementVisitor visitor) {
            visitor.visit(this);
        }
        
        @Override
        public String toString() {
            return "MultiLabelsBlock{labels=" + labels.toString() + '}';
        }
    }
}
