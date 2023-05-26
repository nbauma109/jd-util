package org.jd.core.v1.model.javasyntax.reference;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ElementValuePairTest {

    @Test
    public void test() throws Exception {
        ElementValuePair elementValuePair = new ElementValuePair(null, null);
        assertEquals("ElementValuePair{name=null, elementValue=null}", elementValuePair.toString());
        TestReferenceVisitor referenceVisitor = new TestReferenceVisitor();
        elementValuePair.accept(referenceVisitor);
        assertEquals(1, referenceVisitor.getVisitElementValuePairCount());
    }
}
