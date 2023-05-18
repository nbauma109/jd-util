package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewArrayTest {
    private static final int LINE_NUMBER = 1;
    private static final Type TEST_TYPE = ObjectType.TYPE_OBJECT;

    @Test
    public void testNewArray() {
        BaseExpression dimensionExpressionList = new IntegerConstantExpression(LINE_NUMBER, 0);
        NewArray newArray = new NewArray(LINE_NUMBER, TEST_TYPE, dimensionExpressionList);

        assertTrue(newArray.isEmptyNewArray());
        assertSame(dimensionExpressionList, newArray.getDimensionExpressionList());

        BaseExpression newDimensionExpressionList = new IntegerConstantExpression(LINE_NUMBER, 1);
        newArray.setDimensionExpressionList(newDimensionExpressionList);

        assertFalse(newArray.isEmptyNewArray());
        assertSame(newDimensionExpressionList, newArray.getDimensionExpressionList());

        assertEquals(0, newArray.getPriority());
        assertTrue(newArray.isNewArray());
        assertTrue(newArray.isNew());

        String expectedString = "NewArray{" + TEST_TYPE + "}";
        assertEquals(expectedString, newArray.toString());

        int newLineNumber = 2;
        Expression copiedNewArray = newArray.copyTo(newLineNumber);

        assertTrue(copiedNewArray instanceof NewArray);
        assertEquals(newLineNumber, copiedNewArray.getLineNumber());
        assertEquals(TEST_TYPE, ((NewArray) copiedNewArray).getType());
        assertSame(newDimensionExpressionList, ((NewArray) copiedNewArray).getDimensionExpressionList());

        TestVisitor testVisitor = new TestVisitor();
        newArray.accept(testVisitor);
        assertEquals(1, testVisitor.getNewArrayCount());
    }
}
