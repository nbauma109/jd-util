package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PrimitiveTypeTest {

    @Test
    public void test() throws Exception {
        assertEquals("Z", PrimitiveType.TYPE_BOOLEAN.getDescriptor());
        assertTrue(PrimitiveType.TYPE_BOOLEAN.isPrimitiveTypeArgument());
        assertThrows(IllegalArgumentException.class, () -> PrimitiveType.TYPE_BOOLEAN.createType(-1));
        assertEquals(PrimitiveType.FLAG_BOOLEAN, PrimitiveType.TYPE_BOOLEAN.getLeftFlags());
        assertEquals(PrimitiveType.FLAG_BOOLEAN, PrimitiveType.TYPE_BOOLEAN.getRightFlags());
        assertTrue(PrimitiveType.TYPE_BOOLEAN.isTypeArgumentAssignableFrom(null, null, null, PrimitiveType.TYPE_BOOLEAN));
    }
}
