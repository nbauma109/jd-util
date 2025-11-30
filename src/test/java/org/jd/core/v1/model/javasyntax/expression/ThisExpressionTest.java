package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThisExpressionTest {

    @Test
    public void testThisExpression() {
        int lineNumber = 1;
        Type type = ObjectType.TYPE_STRING;

        ThisExpression thisExpression = new ThisExpression(lineNumber, type);

        // Getters
        assertEquals(type, thisExpression.getType());
        assertTrue(thisExpression.isExplicit());
        assertTrue(thisExpression.isThisExpression());
        assertEquals("ThisExpression{" + type + "}", thisExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        thisExpression.accept(testVisitor);

        assertEquals(1, testVisitor.getThisExpressionCount());

        // Copy method
        ThisExpression copiedExpression = (ThisExpression) thisExpression.copyTo(2);
        assertEquals(2, copiedExpression.getLineNumber());
        assertEquals(type, copiedExpression.getType());
        assertTrue(copiedExpression.isExplicit());
        assertTrue(copiedExpression.isThisExpression());
        assertEquals("ThisExpression{" + type + "}", copiedExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
