package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NullExpressionTest {

    @Test
    public void testNullExpression() {
        test(ObjectType.TYPE_OBJECT, 10, new NullExpression(10, ObjectType.TYPE_OBJECT));
        test(ObjectType.TYPE_OBJECT, 0, new NullExpression(ObjectType.TYPE_OBJECT));
    }

    private static void test(Type type, int lineNumber, NullExpression nullExpression) {
        // Assert
        assertEquals(lineNumber, nullExpression.getLineNumber());
        assertEquals(type, nullExpression.getType());
        assertEquals("NullExpression{type=ObjectType{java/lang/Object}}", nullExpression.toString());
        assertEquals(true, nullExpression.isNullExpression());

        // Act & Assert
        NullExpression copy = (NullExpression) nullExpression.copyTo(lineNumber + 1);
        assertEquals(nullExpression.getType(), copy.getType());
        assertNotEquals(nullExpression.getLineNumber(), copy.getLineNumber());

        // Act & Assert for visitor
        TestVisitor visitor = new TestVisitor();
        nullExpression.accept(visitor);
        assertEquals(1, visitor.getNullExpressionCount());
    }
}
