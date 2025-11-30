package jd.core.links;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReferenceDataTest {

    @Test
    public void test() throws Exception {
        assertTrue(new ReferenceData("java/lang/Object", null, null, null).isAType()); //$NON-NLS-1$
        assertTrue(new ReferenceData("java/lang/Object", "hashCode", "()I", null).isAMethod()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(new ReferenceData("java/lang/Object", "<init>", "()V", null).isAConstructor()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ReferenceData referenceData = new ReferenceData("java/lang/String", "value", "[B", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(referenceData.isAField());
        assertEquals("java/lang/String", referenceData.getTypeName()); //$NON-NLS-1$
        assertEquals("ReferenceData [typeName=java/lang/String, name=value, descriptor=[B, owner=null, enabled=false]", referenceData.toString()); //$NON-NLS-1$
        assertNull(referenceData.getOwner());
        referenceData.setEnabled(true);
        assertTrue(referenceData.isEnabled());
    }
}
