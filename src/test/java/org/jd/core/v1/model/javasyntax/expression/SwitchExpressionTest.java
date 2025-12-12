package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.statement.YieldExpressionStatement;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SwitchExpressionTest {

    private SwitchExpression switchExpression;
    private YieldExpressionStatement yieldExpressionStatement;
    private Expression localVariable;
    private TestVisitor testVisitor;

    @Before
    public void setUp() {
        localVariable =
                new LocalVariableReferenceExpression(
                        ObjectType.TYPE_INTEGER,
                        "input"
                );

        yieldExpressionStatement =
                new YieldExpressionStatement(
                        new IntegerConstantExpression(42)
                );

        SwitchExpression.RuleStatement rule =
                new SwitchExpression.RuleStatement(
                        Collections.singletonList(
                                new SwitchExpression.DefaultLabel()
                        ),
                        yieldExpressionStatement
                );

        switchExpression =
                new SwitchExpression(
                        0,
                        localVariable,
                        Collections.singletonList(rule),
                        localVariable.getType()
                );

        testVisitor = new TestVisitor();
    }

    @Test
    public void testRuleStatementWithYield() {
        switchExpression.accept(testVisitor);
        yieldExpressionStatement.accept(testVisitor);

        assertEquals(1, testVisitor.getSwitchExpressionCount());
        assertEquals(1, testVisitor.getYieldExpressionStatementCount());
        assertEquals(localVariable, switchExpression.getSelector());
        assertEquals(ObjectType.TYPE_INTEGER, switchExpression.getType());
        assertEquals(1, switchExpression.getRules().size());
        assertEquals(
                "SwitchExpression{LocalVariableReferenceExpression{type=ObjectType{java/lang/Integer}, name=input}}",
                switchExpression.toString()
        );
    }

    @Test
    public void testDefaultLabel() {
        SwitchExpression.DefaultLabel label =
                new SwitchExpression.DefaultLabel();

        label.accept(testVisitor);

        assertEquals("DefaultLabel", label.toString());
    }

    @Test
    public void testExpressionLabel() {
        Expression constant =
                new IntegerConstantExpression(7);

        SwitchExpression.ExpressionLabel label =
                new SwitchExpression.ExpressionLabel(constant);

        assertSame(constant, label.getExpression());

        Expression replacement =
                new IntegerConstantExpression(9);

        label.setExpression(replacement);
        label.accept(testVisitor);

        assertSame(replacement, label.getExpression());
        assertEquals(
                "ExpressionLabel{IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=9}}",
                label.toString()
        );
    }

    @Test
    public void testRuleExpression() {
        Expression constant =
                new IntegerConstantExpression(1);

        SwitchExpression.RuleExpression rule =
                new SwitchExpression.RuleExpression(
                        Collections.singletonList(
                                new SwitchExpression.DefaultLabel()
                        ),
                        constant
                );

        assertEquals(1, rule.getLabels().size());
        assertSame(constant, rule.getExpression());

        Expression replacement =
                new IntegerConstantExpression(2);

        rule.setExpression(replacement);
        rule.accept(testVisitor);

        assertSame(replacement, rule.getExpression());
        assertEquals(
                "RuleExpression{[DefaultLabel] -> IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=2}}",
                rule.toString()
        );
    }

    @Test
    public void testRuleStatement() {
        SwitchExpression.RuleStatement rule =
                new SwitchExpression.RuleStatement(
                        Collections.singletonList(
                                new SwitchExpression.DefaultLabel()
                        ),
                        yieldExpressionStatement
                );

        rule.accept(testVisitor);

        assertEquals(1, rule.getLabels().size());
        assertSame(yieldExpressionStatement, rule.getStatements());
        assertEquals(
                "RuleStatement{[DefaultLabel]: YieldExpressionStatement{yield IntegerConstantExpression{type=PrimitiveType{primitive=maybe_byte}, value=42}}}",
                rule.toString()
        );
    }

    @Test
    public void testCopyTo() {
        Expression copied =
                switchExpression.copyTo(100);

        assertSame(switchExpression.getSelector(),
                   ((SwitchExpression) copied).getSelector());
        assertSame(switchExpression.getRules(),
                   ((SwitchExpression) copied).getRules());
        assertSame(switchExpression.getType(),
                   copied.getType());
    }
}
