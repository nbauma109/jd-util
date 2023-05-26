package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LabelStatementTest {

    @Test
    public void test() throws Exception {
        LabelStatement labelStatement = new LabelStatement("label", null);
        assertTrue(labelStatement.isLabelStatement());
        assertEquals("LabelStatement{label: null}", labelStatement.toString());
        TestVisitor testVisitor = new TestVisitor();
        labelStatement.accept(testVisitor);
        assertEquals(1, testVisitor.getLabelStatementCount());
    }
}
