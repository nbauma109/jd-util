/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JavaImportTest {

    @Test
    public void sourceFormOfASimpleImport() {
        JavaImport javaImport = new JavaImport("java.util.List", false, false);

        assertEquals("import java.util.List;", javaImport.toSourceForm());
        assertEquals("java/util/List", javaImport.getInternalName());
        assertEquals("java.util.List", javaImport.getQualifiedName());
        assertFalse(javaImport.isStatic());
        assertFalse(javaImport.isOnDemand());
    }

    @Test
    public void sourceFormOfAStaticOnDemandImport() {
        JavaImport javaImport = new JavaImport("java.lang.Math", true, true);

        assertEquals("import static java.lang.Math.*;", javaImport.toSourceForm());
        assertTrue(javaImport.isStatic());
        assertTrue(javaImport.isOnDemand());
    }

    @Test
    public void importFromAnInternalName() {
        JavaImport javaImport = new JavaImport("java/util/Map", "java.util.Map");

        assertEquals("java/util/Map", javaImport.getInternalName());
        assertEquals("import java.util.Map;", javaImport.toSourceForm());
    }

    @Test
    public void counter() {
        JavaImport javaImport = new JavaImport("java.util.List", false, false);

        assertEquals(1, javaImport.getCounter());
        javaImport.incCounter();
        assertEquals(2, javaImport.getCounter());
    }
}
