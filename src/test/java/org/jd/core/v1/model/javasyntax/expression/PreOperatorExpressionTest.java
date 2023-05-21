package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PreOperatorExpressionTest {

    @Test
    public void testPreOperatorExpression() {
        Expression expression = new IntegerConstantExpression(10, PrimitiveType.TYPE_INT, 5);
        String operator = "++";
        PreOperatorExpression preOperatorExpression = new PreOperatorExpression(10, operator, expression);

        // Test getOperator
        assertEquals(operator, preOperatorExpression.getOperator());

        // Test getExpression
        assertEquals(expression, preOperatorExpression.getExpression());

        // Test getType
        Type expectedType = PrimitiveType.TYPE_INT;
        assertEquals(expectedType, preOperatorExpression.getType());

        // Test getPriority
        assertEquals(2, preOperatorExpression.getPriority());

        // Test isPreOperatorExpression
        assertTrue(preOperatorExpression.isPreOperatorExpression());

        // Test toString
        assertEquals("PreOperatorExpression{++ IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=5}}", preOperatorExpression.toString());

        // Test copyTo
        PreOperatorExpression copiedExpression = (PreOperatorExpression) preOperatorExpression.copyTo(20);
        assertEquals(preOperatorExpression.getOperator(), copiedExpression.getOperator());
        assertEquals(preOperatorExpression.getExpression(), copiedExpression.getExpression());
        assertEquals(20, copiedExpression.getLineNumber());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        copiedExpression.accept(visitor);
        assertEquals(1, visitor.getPreOperatorExpressionCount());
    }
}
