package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ContinueStatementTest {

    @Test
    public void testContinueStatement() {
        // Test ContinueStatement with label
        ContinueStatement continueStatementWithLabel = new ContinueStatement("myLabel");
        assertEquals("myLabel", continueStatementWithLabel.getLabel());
        assertTrue(continueStatementWithLabel.isContinueStatement());

        // Test ContinueStatement without label (using static CONTINUE)
        ContinueStatement continueStatementWithoutLabel = ContinueStatement.CONTINUE;
        assertNull(continueStatementWithoutLabel.getLabel());
        assertTrue(continueStatementWithoutLabel.isContinueStatement());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        continueStatementWithoutLabel.accept(visitor);
        assertEquals(1, visitor.getContinueStatementCount());
    }
}
