package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
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
		assertEquals(PrimitiveType.TYPE_INT, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("I")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_DOUBLE, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("D")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_FLOAT, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("F")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_LONG, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("J")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_CHAR, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("C")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_SHORT, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("S")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_BYTE, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("B")); //$NON-NLS-1$
		assertEquals(PrimitiveType.TYPE_BOOLEAN, PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("Z")); //$NON-NLS-1$
		assertTrue(PrimitiveTypeUtil.getPrimitiveTypeFromDescriptor("[I") instanceof ObjectType); //$NON-NLS-1$
	}

	@Test
	public void testGetPrimitiveTypeFromValue() {
		assertEquals(PrimitiveType.MAYBE_BOOLEAN_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(1));
		assertEquals(PrimitiveType.MAYBE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(127));
		assertEquals(PrimitiveType.MAYBE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(32767));
		assertEquals(PrimitiveType.MAYBE_CHAR_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(65535));
		assertEquals(PrimitiveType.MAYBE_NEGATIVE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(-128));
		assertEquals(PrimitiveType.MAYBE_NEGATIVE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(-32768));
		assertEquals(PrimitiveType.MAYBE_INT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromValue(-65536));
	}

	@Test
	public void testGetCommonPrimitiveType() {
		assertEquals(PrimitiveType.TYPE_INT, PrimitiveTypeUtil.getCommonPrimitiveType(PrimitiveType.TYPE_INT, PrimitiveType.TYPE_INT));
		assertNull(PrimitiveTypeUtil.getCommonPrimitiveType(PrimitiveType.TYPE_INT, PrimitiveType.TYPE_VOID));
	}

	@Test
	public void testGetPrimitiveTypeFromFlags() {
		assertEquals(PrimitiveType.TYPE_BOOLEAN, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_BOOLEAN));
		assertEquals(PrimitiveType.TYPE_CHAR, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_CHAR));
		assertEquals(PrimitiveType.TYPE_FLOAT, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_FLOAT));
		assertEquals(PrimitiveType.TYPE_DOUBLE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_DOUBLE));
		assertEquals(PrimitiveType.TYPE_BYTE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_BYTE));
		assertEquals(PrimitiveType.TYPE_SHORT, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_SHORT));
		assertEquals(PrimitiveType.TYPE_INT, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.TYPE_LONG, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_LONG));
		assertEquals(PrimitiveType.TYPE_VOID, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_VOID));
		assertEquals(PrimitiveType.MAYBE_CHAR_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.MAYBE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.MAYBE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.MAYBE_BOOLEAN_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.MAYBE_NEGATIVE_BYTE_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.MAYBE_NEGATIVE_SHORT_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT));
		assertEquals(PrimitiveType.MAYBE_NEGATIVE_BOOLEAN_TYPE, PrimitiveTypeUtil.getPrimitiveTypeFromFlags(PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT));
		assertNull(PrimitiveTypeUtil.getPrimitiveTypeFromFlags(0));
	}

	@Test
	public void testGetPrimitiveTypeFromTag() {
		assertEquals(PrimitiveType.TYPE_INT, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_INT));
		assertEquals(PrimitiveType.TYPE_DOUBLE, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_DOUBLE));
		assertEquals(PrimitiveType.TYPE_FLOAT, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_FLOAT));
		assertEquals(PrimitiveType.TYPE_LONG, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_LONG));
		assertEquals(PrimitiveType.TYPE_CHAR, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_CHAR));
		assertEquals(PrimitiveType.TYPE_SHORT, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_SHORT));
		assertEquals(PrimitiveType.TYPE_BYTE, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_BYTE));
		assertEquals(PrimitiveType.TYPE_BOOLEAN, PrimitiveTypeUtil.getPrimitiveTypeFromTag(T_BOOLEAN));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetPrimitiveTypeFromTag_IllegalStateException() {
		PrimitiveTypeUtil.getPrimitiveTypeFromTag(999);
	}
}
