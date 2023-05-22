package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GenericTypeTest {

    @Test
    public void test() throws Exception {
        GenericType genericType = new GenericType("T");
        assertTrue(genericType.isGenericTypeArgument());
        assertThrows(IllegalArgumentException.class, () -> genericType.createType(-1));
        assertEquals("GenericType{T, dimension=1}", genericType.createType(1).toString());
    }
}
