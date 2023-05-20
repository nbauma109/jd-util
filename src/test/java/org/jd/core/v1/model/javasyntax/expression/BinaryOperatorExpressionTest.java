package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BinaryOperatorExpressionTest {

    @Test
    public void testBinaryOperatorExpression() {
        int lineNumber = 1;
        Expression leftExpression = new IntegerConstantExpression(lineNumber, 1);
        Expression rightExpression = new IntegerConstantExpression(lineNumber, 2);
        String operator = "+";
        int priority = 2;
        ObjectType type = ObjectType.TYPE_PRIMITIVE_INT;

        BinaryOperatorExpression binaryOperatorExpression = new BinaryOperatorExpression(lineNumber, type, leftExpression, operator, rightExpression, priority);

        // Getters
        assertEquals(lineNumber, binaryOperatorExpression.getLineNumber());
        assertEquals(leftExpression, binaryOperatorExpression.getLeftExpression());
        assertEquals(operator, binaryOperatorExpression.getOperator());
        assertEquals(rightExpression, binaryOperatorExpression.getRightExpression());
        assertEquals(priority, binaryOperatorExpression.getPriority());

        // isBinaryOperatorExpression method
        assertTrue(binaryOperatorExpression.isBinaryOperatorExpression());

        // toString method
        String expectedToString = "BinaryOperatorExpression{" + leftExpression.toString() + ' ' + operator + ' ' + rightExpression.toString() + "}";
        assertEquals(expectedToString, binaryOperatorExpression.toString());

        // copyTo method
        BinaryOperatorExpression copiedExpression = (BinaryOperatorExpression) binaryOperatorExpression.copyTo(2);
        assertEquals(2, copiedExpression.getLineNumber());
        assertEquals(binaryOperatorExpression.getLeftExpression(), copiedExpression.getLeftExpression());
        assertEquals(binaryOperatorExpression.getOperator(), copiedExpression.getOperator());
        assertEquals(binaryOperatorExpression.getRightExpression(), copiedExpression.getRightExpression());
        assertEquals(binaryOperatorExpression.getPriority(), copiedExpression.getPriority());

        // Setters
        Expression newLeftExpression = new IntegerConstantExpression(lineNumber, 3);
        Expression newRightExpression = new IntegerConstantExpression(lineNumber, 4);
        String newOperator = "-";
        int newPriority = 3;

        binaryOperatorExpression.setLeftExpression(newLeftExpression);
        binaryOperatorExpression.setOperator(newOperator);
        binaryOperatorExpression.setRightExpression(newRightExpression);
        binaryOperatorExpression.setPriority(newPriority);

        assertEquals(newLeftExpression, binaryOperatorExpression.getLeftExpression());
        assertEquals(newOperator, binaryOperatorExpression.getOperator());
        assertEquals(newRightExpression, binaryOperatorExpression.getRightExpression());
        assertEquals(newPriority, binaryOperatorExpression.getPriority());

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        binaryOperatorExpression.accept(testVisitor);
        assertEquals(1, testVisitor.getBinaryOperatorExpressionCount());
    }
}
