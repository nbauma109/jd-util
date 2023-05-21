package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentStatementTest {
    
    private CommentStatement commentStatement;
    private TestVisitor visitor;
    
    @Before
    public void setUp() {
        commentStatement = new CommentStatement();
        commentStatement.setText("This is a comment");
        visitor = new TestVisitor();
    }
    
    @Test
    public void testCommentStatement() {
        // Act
        commentStatement.accept(visitor);
        assertEquals(1, visitor.getCommentStatementCount());

        // Assert
        assertEquals("This is a comment", commentStatement.getText());
    }
}
