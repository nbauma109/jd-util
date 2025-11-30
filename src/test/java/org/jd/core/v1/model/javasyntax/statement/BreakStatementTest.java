package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BreakStatementTest {

    @Test
    public void testBreakStatement() {
        // Test BreakStatement with label
        BreakStatement breakStatementWithLabel = new BreakStatement("myLabel"); //$NON-NLS-1$
        assertEquals("myLabel", breakStatementWithLabel.getLabel()); //$NON-NLS-1$
        assertTrue(breakStatementWithLabel.isBreakStatement());

        // Test BreakStatement without label (using static BREAK)
        BreakStatement breakStatementWithoutLabel = BreakStatement.BREAK;
        assertNull(breakStatementWithoutLabel.getLabel());
        assertTrue(breakStatementWithoutLabel.isBreakStatement());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        breakStatementWithoutLabel.accept(visitor);
        assertEquals(1, visitor.getBreakStatementCount());
    }
}
