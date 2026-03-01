/*
 * Copyright (c) 2026 @nbauma109.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.jd.core.v1.model.javasyntax.reference.AnnotationReference;
import org.jd.core.v1.model.javasyntax.reference.AnnotationReferences;
import org.jd.core.v1.model.javasyntax.reference.BaseAnnotationReference;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Types;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void testIsEmpty() {
        assertTrue(Utils.isEmpty((BaseType) null));
        assertTrue(Utils.isEmpty(new Types()));
        assertFalse(Utils.isEmpty(PrimitiveType.TYPE_INT));
    }

    @Test
    public void testIsEmptyAnnotationReferences() {
        assertTrue(Utils.isEmpty((BaseAnnotationReference) null));
        assertTrue(Utils.isEmpty(new AnnotationReferences<>()));
        assertFalse(Utils.isEmpty(new AnnotationReference(ObjectType.TYPE_OBJECT)));
    }

    @Test
    public void testIsEmptyCollection() {
        assertTrue(Utils.isEmptyCollection((Collection<?>) null));
        assertTrue(Utils.isEmptyCollection(Collections.emptyList()));
        assertFalse(Utils.isEmptyCollection(List.of("x")));
    }

    @Test
    public void testIsSingleton() {
        assertFalse(Utils.isSingleton((BaseType) null));
        assertTrue(Utils.isSingleton(PrimitiveType.TYPE_INT));
        assertFalse(Utils.isSingleton(new Types(PrimitiveType.TYPE_INT, PrimitiveType.TYPE_LONG)));
    }

    @Test
    public void testIsList() {
        assertFalse(Utils.isList((BaseType) null));
        assertFalse(Utils.isList(PrimitiveType.TYPE_INT));
        assertTrue(Utils.isList(new Types(PrimitiveType.TYPE_INT, PrimitiveType.TYPE_LONG)));
    }
}
