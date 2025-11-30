package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BaseTypeTest implements BaseType {

    @Test
    public void test() throws Exception {
        assertFalse(isGenericType());
        assertFalse(isInnerObjectType());
        assertFalse(isObjectType());
        assertFalse(isPrimitiveType());
        assertFalse(isTypes());
        assertEquals(ObjectType.TYPE_UNDEFINED_OBJECT, getOuterType());
        assertEquals("", getInternalName()); //$NON-NLS-1$
    }

    @Override
    public void accept(TypeVisitor visitor) {
    }

}
