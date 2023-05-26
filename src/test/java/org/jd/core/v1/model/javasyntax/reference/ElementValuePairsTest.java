package org.jd.core.v1.model.javasyntax.reference;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ElementValuePairsTest {

    @Test
    public void test() throws Exception {
        ElementValuePairs elementValuePairs = new ElementValuePairs(10);
        TestReferenceVisitor referenceVisitor = new TestReferenceVisitor();
        elementValuePairs.accept(referenceVisitor);
        assertEquals(1, referenceVisitor.getVisitElementValuePairsCount());
        assertEquals("ElementValuePairs{[]}", elementValuePairs.toString());
    }
}
