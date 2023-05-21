package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DoubleConstantExpressionTest {

    @Test
    public void testDoubleConstantExpression() {
        // Test constructor with double value
        DoubleConstantExpression doubleConstantExpression = new DoubleConstantExpression(10D);
        assertEquals(10D, doubleConstantExpression.getDoubleValue(), 0.00001D);
        assertTrue(doubleConstantExpression.getType() instanceof PrimitiveType);
        assertTrue(doubleConstantExpression.isDoubleConstantExpression());

        // Test constructor with lineNumber and double value
        DoubleConstantExpression doubleConstantExpressionWithLineNumber = new DoubleConstantExpression(2, 10D);
        assertEquals(10D, doubleConstantExpressionWithLineNumber.getDoubleValue(), 0.00001D);
        assertEquals(2, doubleConstantExpressionWithLineNumber.getLineNumber());

        // Test the copyTo method
        Expression copiedExpression = doubleConstantExpression.copyTo(3);
        assertEquals(3, copiedExpression.getLineNumber());
        assertTrue(copiedExpression instanceof DoubleConstantExpression);
        assertEquals(10D, ((DoubleConstantExpression) copiedExpression).getDoubleValue(), 0.00001D);

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        copiedExpression.accept(visitor);
        assertEquals(1, visitor.getDoubleConstantExpressionCount());
    }

    @Test
    public void testToString() {
        DoubleConstantExpression doubleConstantExpression = new DoubleConstantExpression(10D);
        assertEquals("DoubleConstantExpression{10.0}", doubleConstantExpression.toString());

        DoubleConstantExpression doubleConstantExpressionWithLineNumber = new DoubleConstantExpression(2, 10D);
        assertEquals("DoubleConstantExpression{10.0}", doubleConstantExpressionWithLineNumber.toString());
    }
}
