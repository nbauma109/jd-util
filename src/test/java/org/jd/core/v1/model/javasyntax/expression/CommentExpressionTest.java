package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentExpressionTest {

    @Test
    public void testCommentExpression() {
        // Test constructor
        String text = "This is a comment"; //$NON-NLS-1$
        CommentExpression commentExpression = new CommentExpression(text);

        assertEquals(text, commentExpression.text());
        assertEquals(PrimitiveType.TYPE_VOID, commentExpression.getType());
        assertEquals(Expression.UNKNOWN_LINE_NUMBER, commentExpression.getLineNumber());
        assertEquals(0, commentExpression.getPriority());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        commentExpression.accept(visitor);
        assertEquals(1, visitor.getCommentExpressionCount());

        // Test toString method
        String expectedToString = "CommentExpression{" + text + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expectedToString, commentExpression.toString());

        // Test copyTo method
        CommentExpression copiedExpression = (CommentExpression) commentExpression.copyTo(2);
        assertEquals(copiedExpression, commentExpression);
        assertEquals(copiedExpression.text(), commentExpression.text());
        assertEquals(copiedExpression.getType(), commentExpression.getType());
        assertEquals(copiedExpression.getLineNumber(), commentExpression.getLineNumber());
        assertEquals(copiedExpression.getPriority(), commentExpression.getPriority());
    }
}
