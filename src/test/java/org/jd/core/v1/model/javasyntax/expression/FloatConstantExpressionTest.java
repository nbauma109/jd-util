package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FloatConstantExpressionTest {

    @Test
    public void testFloatConstantExpression() {
        // Test constructor with float value
        FloatConstantExpression floatConstantExpression = new FloatConstantExpression(10F);
        assertEquals(10F, floatConstantExpression.getFloatValue(), 0.00001F);
        assertTrue(floatConstantExpression.getType() instanceof PrimitiveType);
        assertTrue(floatConstantExpression.isFloatConstantExpression());

        // Test constructor with lineNumber and float value
        FloatConstantExpression floatConstantExpressionWithLineNumber = new FloatConstantExpression(2, 10F);
        assertEquals(10F, floatConstantExpressionWithLineNumber.getFloatValue(), 0.00001F);
        assertEquals(2, floatConstantExpressionWithLineNumber.getLineNumber());

        // Test the copyTo method
        Expression copiedExpression = floatConstantExpression.copyTo(3);
        assertEquals(3, copiedExpression.getLineNumber());
        assertTrue(copiedExpression instanceof FloatConstantExpression);
        assertEquals(10F, ((FloatConstantExpression) copiedExpression).getFloatValue(), 0.00001F);

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        copiedExpression.accept(visitor);
        assertEquals(1, visitor.getFloatConstantExpressionCount());
    }

    @Test
    public void testToString() {
        FloatConstantExpression floatConstantExpression = new FloatConstantExpression(10F);
        assertEquals("FloatConstantExpression{10.0}", floatConstantExpression.toString());

        FloatConstantExpression floatConstantExpressionWithLineNumber = new FloatConstantExpression(2, 10F);
        assertEquals("FloatConstantExpression{10.0}", floatConstantExpressionWithLineNumber.toString());
    }
}
