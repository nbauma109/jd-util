package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SwitchStatementTest {
    private TestVisitor testVisitor;
    private IntegerConstantExpression condition;
    private SwitchStatement.LabelBlock labelBlock;
    private SwitchStatement.DefaultLabel defaultLabel;
    private SwitchStatement.ExpressionLabel expressionLabel;

    @Before
    public void setup() {
        testVisitor = new TestVisitor();
        condition = new IntegerConstantExpression(PrimitiveType.TYPE_INT, 10);
        defaultLabel = new SwitchStatement.DefaultLabel();
        expressionLabel = new SwitchStatement.ExpressionLabel(condition);
        labelBlock = new SwitchStatement.LabelBlock(defaultLabel, new ReturnExpressionStatement(condition));
    }

    @Test
    public void testSwitchStatement() {
        SwitchStatement switchStatement = new SwitchStatement(condition, Arrays.asList(labelBlock));
        switchStatement.accept(testVisitor);
        assertEquals(1, testVisitor.getSwitchStatementCount());
        assertTrue(switchStatement.isSwitchStatement());
    }

    @Test
    public void testConditionMethods() {
        SwitchStatement switchStatement = new SwitchStatement(condition, Arrays.asList(labelBlock));
        assertEquals(condition, switchStatement.getCondition());
        IntegerConstantExpression newCondition = new IntegerConstantExpression(PrimitiveType.TYPE_INT, 20);
        switchStatement.setCondition(newCondition);
        assertEquals(newCondition, switchStatement.getCondition());
    }

    @Test
    public void testGetBlocks() {
        List<SwitchStatement.Block> blocks = Arrays.asList(labelBlock);
        SwitchStatement switchStatement = new SwitchStatement(condition, blocks);
        assertEquals(blocks, switchStatement.getBlocks());
    }

    @Test
    public void testLabelBlock() {
        labelBlock.accept(testVisitor);
        assertEquals(1, testVisitor.getLabelBlockCount());
        assertTrue(labelBlock.isSwitchStatementLabelBlock());
        assertEquals(defaultLabel, labelBlock.getLabel());
    }

    @Test
    public void testDefaultLabel() {
        defaultLabel.accept(testVisitor);
        assertEquals(1, testVisitor.getDefaultLabelCount());
        assertEquals("DefaultLabel", defaultLabel.toString());
    }

    @Test
    public void testExpressionLabel() {
        expressionLabel.accept(testVisitor);
        assertEquals(1, testVisitor.getExpressionLabelCount());
        assertEquals("ExpressionLabel{" + condition.toString() + '}', expressionLabel.toString());
        assertEquals(condition, expressionLabel.getExpression());
        IntegerConstantExpression newExpression = new IntegerConstantExpression(PrimitiveType.TYPE_INT, 30);
        expressionLabel.setExpression(newExpression);
        assertEquals(newExpression, expressionLabel.getExpression());
    }

    @Test
    public void testMultiLabelsBlock() {
        SwitchStatement.MultiLabelsBlock multiLabelsBlock = new SwitchStatement.MultiLabelsBlock(Arrays.asList(defaultLabel, expressionLabel),
                new ReturnExpressionStatement(condition));
        multiLabelsBlock.accept(testVisitor);
        assertEquals(1, testVisitor.getMultiLabelsBlockCount());
        assertTrue(multiLabelsBlock.isSwitchStatementMultiLabelsBlock());
        assertEquals(Arrays.asList(defaultLabel, expressionLabel), multiLabelsBlock.getLabels());
    }

    @Test
    public void testLabelBlockGetStatementsAndToString() {
        ReturnExpressionStatement returnExpressionStatement = new ReturnExpressionStatement(condition);
        labelBlock = new SwitchStatement.LabelBlock(defaultLabel, returnExpressionStatement);
        assertEquals(returnExpressionStatement, labelBlock.getStatements());
        assertEquals("LabelBlock{label=DefaultLabel}", labelBlock.toString());
    }

    @Test
    public void testMultiLabelsBlockGetStatementsAndToString() {
        ReturnExpressionStatement returnExpressionStatement = new ReturnExpressionStatement(condition);
        SwitchStatement.MultiLabelsBlock multiLabelsBlock = new SwitchStatement.MultiLabelsBlock(Arrays.asList(defaultLabel, expressionLabel), returnExpressionStatement);
        assertEquals(returnExpressionStatement, multiLabelsBlock.getStatements());
        assertEquals("MultiLabelsBlock{labels=[DefaultLabel, ExpressionLabel{IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=10}}]}", multiLabelsBlock.toString());
    }
}
