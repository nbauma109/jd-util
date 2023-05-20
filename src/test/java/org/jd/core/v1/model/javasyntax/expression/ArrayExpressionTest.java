package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrayExpressionTest {

    @Test
    public void testArrayExpression() {
        int lineNumber = 0;
        Expression expression = new IntegerConstantExpression(lineNumber, 1);
        Expression index = new IntegerConstantExpression(lineNumber, 0);

        ArrayExpression arrayExpression = new ArrayExpression(expression, index);

        // Getters
        assertEquals(lineNumber, arrayExpression.getLineNumber());
        assertEquals(expression, arrayExpression.getExpression());
        assertEquals(index, arrayExpression.getIndex());
        assertEquals(1, arrayExpression.getPriority());

        // isArrayExpression method
        assertTrue(arrayExpression.isArrayExpression());

        // toString method
        String expectedToString = "ArrayExpression{" + expression.toString() + "[" + index.toString() + "]}";
        assertEquals(expectedToString, arrayExpression.toString());

        // copyTo method
        ArrayExpression copiedExpression = (ArrayExpression) arrayExpression.copyTo(2);
        assertEquals(2, copiedExpression.getLineNumber());
        assertEquals(arrayExpression.getExpression(), copiedExpression.getExpression());
        assertEquals(arrayExpression.getIndex(), copiedExpression.getIndex());

        // Setters
        Expression newExpression = new IntegerConstantExpression(lineNumber, 2);
        Expression newIndex = new IntegerConstantExpression(lineNumber, 1);
        arrayExpression.setExpression(newExpression);
        arrayExpression.setIndex(newIndex);

        assertEquals(newExpression, arrayExpression.getExpression());
        assertEquals(newIndex, arrayExpression.getIndex());

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        arrayExpression.accept(testVisitor);
        assertEquals(1, testVisitor.getArrayExpressionCount());
    }
}
