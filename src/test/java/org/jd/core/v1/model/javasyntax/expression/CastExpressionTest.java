package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CastExpressionTest {

    @Test
    public void testCastExpression() {
        int lineNumber = 0;
        Type type = PrimitiveType.TYPE_BYTE;
        Expression expression = new IntegerConstantExpression(2, PrimitiveType.TYPE_INT, 42);
        CastExpression castExpression = new CastExpression(type, expression);

        // Getters
        assertEquals(lineNumber, castExpression.getLineNumber());
        assertEquals(type, castExpression.getType());
        assertEquals(expression, castExpression.getExpression());
        assertTrue(castExpression.isExplicit());
        assertFalse(castExpression.isByteCodeCheckCast());
        assertTrue(castExpression.isCastExpression());
        assertEquals(3, castExpression.getPriority());
        assertEquals("CastExpression{cast (PrimitiveType{primitive=byte}) IntegerConstantExpression{type=PrimitiveType{primitive=int}, value=42}}", castExpression.toString());

        // Setters
        Expression newExpression = new StringConstantExpression(3, "value");
        castExpression.setExpression(newExpression);
        castExpression.setExplicit(false);
        castExpression.setByteCodeCheckCast(true);

        assertEquals(newExpression, castExpression.getExpression());
        assertFalse(castExpression.isExplicit());
        assertTrue(castExpression.isByteCodeCheckCast());

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        castExpression.accept(testVisitor);

        assertEquals(1, testVisitor.getCastExpressionCount());

        // Copy method
        CastExpression copiedExpression = (CastExpression) castExpression.copyTo(4);
        assertEquals(4, copiedExpression.getLineNumber());
        assertEquals(type, copiedExpression.getType());
        assertEquals(newExpression, copiedExpression.getExpression());
        assertFalse(copiedExpression.isExplicit());
        assertTrue(copiedExpression.isByteCodeCheckCast());
        assertTrue(copiedExpression.isCastExpression());
        assertEquals(3, copiedExpression.getPriority());
        assertEquals("CastExpression{cast (PrimitiveType{primitive=byte}) StringConstantExpression{\"value\"}}", copiedExpression.toString());
    }
}
