package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.junit.Test;

import static org.apache.bcel.Const.T_BOOLEAN;
import static org.apache.bcel.Const.T_BYTE;
import static org.apache.bcel.Const.T_CHAR;
import static org.apache.bcel.Const.T_DOUBLE;
import static org.apache.bcel.Const.T_FLOAT;
import static org.apache.bcel.Const.T_INT;
import static org.apache.bcel.Const.T_LONG;
import static org.apache.bcel.Const.T_SHORT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_BOOLEAN;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_BYTE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_CHAR;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_DOUBLE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_FLOAT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_INT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_LONG;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_SHORT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_VOID;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_BOOLEAN_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_BYTE_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_CHAR_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_INT_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_NEGATIVE_BOOLEAN_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_NEGATIVE_BYTE_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_NEGATIVE_SHORT_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_SHORT_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_BOOLEAN;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_BYTE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_CHAR;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_DOUBLE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_FLOAT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_INT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_LONG;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_SHORT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_VOID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PrimitiveTypeUtilTest {

    @Test
    public void testGetPrimitiveTypeFromDescriptor() {
        assertEquals(TYPE_INT, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("I"));
        assertEquals(TYPE_DOUBLE, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("D"));
        assertEquals(TYPE_FLOAT, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("F"));
        assertEquals(TYPE_LONG, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("J"));
        assertEquals(TYPE_CHAR, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("C"));
        assertEquals(TYPE_SHORT, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("S"));
        assertEquals(TYPE_BYTE, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("B"));
        assertEquals(TYPE_BOOLEAN, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("Z"));
        assertTrue(PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("[I") instanceof ObjectType);
    }

    @Test
    public void testGetPrimitiveTypeFromValue() {
        assertEquals(MAYBE_BOOLEAN_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(1));
        assertEquals(MAYBE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(127));
        assertEquals(MAYBE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(32767));
        assertEquals(MAYBE_CHAR_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(65535));
        assertEquals(MAYBE_NEGATIVE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(-128));
        assertEquals(MAYBE_NEGATIVE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(-32768));
        assertEquals(MAYBE_INT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(-65536));
    }

    @Test
    public void testGetCommonPrimitiveType() {
        assertEquals(TYPE_INT, PrimitiveTypeUtil.getCommonPrimitiveType(TYPE_INT, TYPE_INT));
        assertNull(PrimitiveTypeUtil.getCommonPrimitiveType(TYPE_INT, TYPE_VOID));
    }

    @Test
    public void testGetPrimitiveTypeFromFlags() {
        assertEquals(TYPE_BOOLEAN, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_BOOLEAN));
        assertEquals(TYPE_CHAR, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_CHAR));
        assertEquals(TYPE_FLOAT, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_FLOAT));
        assertEquals(TYPE_DOUBLE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_DOUBLE));
        assertEquals(TYPE_BYTE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_BYTE));
        assertEquals(TYPE_SHORT, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_SHORT));
        assertEquals(TYPE_INT, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_INT));
        assertEquals(TYPE_LONG, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_LONG));
        assertEquals(TYPE_VOID, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_VOID));
        assertEquals(MAYBE_CHAR_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_CHAR|FLAG_INT));
        assertEquals(MAYBE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_CHAR|FLAG_SHORT|FLAG_INT));
        assertEquals(MAYBE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_BYTE|FLAG_CHAR|FLAG_SHORT|FLAG_INT));
        assertEquals(MAYBE_BOOLEAN_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_BOOLEAN|FLAG_BYTE|FLAG_CHAR|FLAG_SHORT|FLAG_INT));
        assertEquals(MAYBE_NEGATIVE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_BYTE|FLAG_SHORT|FLAG_INT));
        assertEquals(MAYBE_NEGATIVE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_SHORT|FLAG_INT));
        assertEquals(MAYBE_NEGATIVE_BOOLEAN_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(FLAG_BOOLEAN|FLAG_BYTE|FLAG_SHORT|FLAG_INT));
        assertNull(PrimitiveTypeUtil.getPrimitiveTypeFromFlags(0));
    }

    @Test
    public void testGetPrimitiveTypeFromTag() {
        assertEquals(TYPE_INT, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_INT));
        assertEquals(TYPE_DOUBLE, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_DOUBLE));
        assertEquals(TYPE_FLOAT, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_FLOAT));
        assertEquals(TYPE_LONG, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_LONG));
        assertEquals(TYPE_CHAR, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_CHAR));
        assertEquals(TYPE_SHORT, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_SHORT));
        assertEquals(TYPE_BYTE, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_BYTE));
        assertEquals(TYPE_BOOLEAN, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_BOOLEAN));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPrimitiveTypeFromTag_IllegalStateException() {
        PrimitiveTypeUtil.getPrimitiveTypeFromTag(999);
    }
}
