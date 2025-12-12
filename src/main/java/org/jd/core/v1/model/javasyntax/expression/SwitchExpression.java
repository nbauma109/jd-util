package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.List;

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
        private Expression expression;

        public RuleExpression(List<Label> labels, Expression expression) {
            this.labels = labels;
            this.expression = expression;
        }

        @Override
        public List<Label> getLabels() {
            return labels;
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
        private final BaseStatement statements;

        public RuleStatement(List<Label> labels, BaseStatement statements) {
            this.labels = labels;
            this.statements = statements;
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
