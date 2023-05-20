package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TernaryOperatorExpressionTest {
    @Test
    public void testTernaryOperatorExpression() {
        int lineNumber = 1;
        Expression condition = new IntegerConstantExpression(lineNumber, 1);
        Expression trueExpression = new IntegerConstantExpression(lineNumber, 2);
        Expression falseExpression = new IntegerConstantExpression(lineNumber, 3);
        ObjectType type = ObjectType.TYPE_PRIMITIVE_INT;

        TernaryOperatorExpression ternaryOperatorExpression = new TernaryOperatorExpression(lineNumber, type, condition, trueExpression, falseExpression);

        // Getters
        assertEquals(lineNumber, ternaryOperatorExpression.getLineNumber());
        assertEquals(condition, ternaryOperatorExpression.getCondition());
        assertEquals(trueExpression, ternaryOperatorExpression.getTrueExpression());
        assertEquals(falseExpression, ternaryOperatorExpression.getFalseExpression());
        assertEquals(15, ternaryOperatorExpression.getPriority());

        // isTernaryOperatorExpression method
        assertTrue(ternaryOperatorExpression.isTernaryOperatorExpression());

        // toString method
        String expectedToString = "TernaryOperatorExpression{" + condition + " ? " + trueExpression + " : " + falseExpression + "}";
        assertEquals(expectedToString, ternaryOperatorExpression.toString());

        // copyTo method
        TernaryOperatorExpression copiedExpression = (TernaryOperatorExpression) ternaryOperatorExpression.copyTo(2);
        assertEquals(2, copiedExpression.getLineNumber());
        assertEquals(ternaryOperatorExpression.getCondition(), copiedExpression.getCondition());
        assertEquals(ternaryOperatorExpression.getTrueExpression(), copiedExpression.getTrueExpression());
        assertEquals(ternaryOperatorExpression.getFalseExpression(), copiedExpression.getFalseExpression());

        // Setters
        Expression newCondition = new IntegerConstantExpression(lineNumber, 4);
        Expression newTrueExpression = new IntegerConstantExpression(lineNumber, 5);
        Expression newFalseExpression = new IntegerConstantExpression(lineNumber, 6);

        ternaryOperatorExpression.setCondition(newCondition);
        ternaryOperatorExpression.setTrueExpression(newTrueExpression);
        ternaryOperatorExpression.setFalseExpression(newFalseExpression);

        assertEquals(newCondition, ternaryOperatorExpression.getCondition());
        assertEquals(newTrueExpression, ternaryOperatorExpression.getTrueExpression());
        assertEquals(newFalseExpression, ternaryOperatorExpression.getFalseExpression());

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        ternaryOperatorExpression.accept(testVisitor);
        assertEquals(1, testVisitor.getTernaryOperatorExpressionCount());
    }
}
