package jd.core.links;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReferenceDataTest {

    @Test
    public void test() throws Exception {
        assertTrue(new ReferenceData("java/lang/Object", null, null, null).isAType());
        assertTrue(new ReferenceData("java/lang/Object", "hashCode", "()I", null).isAMethod());
        assertTrue(new ReferenceData("java/lang/Object", "<init>", "()V", null).isAConstructor());
        ReferenceData referenceData = new ReferenceData("java/lang/String", "value", "[B", null);
        assertTrue(referenceData.isAField());
        assertEquals("java/lang/String", referenceData.getTypeName());
        assertEquals("ReferenceData [typeName=java/lang/String, name=value, descriptor=[B, owner=null, enabled=false]", referenceData.toString());
        assertNull(referenceData.getOwner());
        referenceData.setEnabled(true);
        assertTrue(referenceData.isEnabled());
    }
}
