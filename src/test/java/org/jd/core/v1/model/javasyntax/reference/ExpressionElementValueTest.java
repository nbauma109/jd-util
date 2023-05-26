package org.jd.core.v1.model.javasyntax.reference;

import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionElementValueTest {

    @Test
    public void test() throws Exception {
        StringConstantExpression expression = new StringConstantExpression("Hello World!");
        ExpressionElementValue expressionElementValue = new ExpressionElementValue(expression);
        assertEquals(expression, expressionElementValue.getExpression());
        assertEquals("ExpressionElementValue{StringConstantExpression{\"Hello World!\"}}", expressionElementValue.toString());
        TestReferenceVisitor referenceVisitor = new TestReferenceVisitor();
        expressionElementValue.accept(referenceVisitor);
        assertEquals(1, referenceVisitor.getVisitExpressionElementValueCount());
    }
}
