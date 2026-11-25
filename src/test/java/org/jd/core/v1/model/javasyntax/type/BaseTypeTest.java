package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BaseTypeTest implements BaseType {

    @Test
    public void test() {
        assertFalse(isGenericType());
        assertFalse(isInnerObjectType());
        assertFalse(isObjectType());
        assertFalse(isPrimitiveType());
        assertFalse(isTypes());
        assertEquals(ObjectType.TYPE_UNDEFINED_OBJECT, getOuterType());
        assertEquals("", getInternalName());
    }

    @Override
    public void accept(TypeVisitor visitor) {
        // This fixture tests the interface defaults rather than visitor dispatch.
    }

}
