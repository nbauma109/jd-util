/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.classfile;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ClassFileTest {

    private static ClassFile loadFrom(Class<?> anchor) throws IOException {
        // Works for nested classes too: "/java/util/Map$Entry.class"
        String resource = "/" + anchor.getName().replace('.', '/') + ".class";

        try (InputStream in = ClassFileTest.class.getResourceAsStream(resource)) {
            assertNotNull("Cannot load bytecode for " + anchor.getName(), in);

            ClassReader cr = new ClassReader(in);
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            return new ClassFile(cn);
        }
    }

    @Test
    public void testClassFile() throws Exception {
        // Load java.util.Map and its inner java.util.Map$Entry
        ClassFile outerClassFile = loadFrom(java.util.Map.class);
        ClassFile innerClassFile = loadFrom(java.util.Map.Entry.class);

        // Wire outer/inner relationship for our model
        innerClassFile.setOuterClassFile(outerClassFile);
        outerClassFile.setInnerClassFiles(Collections.singletonList(innerClassFile));

        // Basic flags on the outer type (Map is a public abstract interface)
        assertFalse("Map must not be a module", outerClassFile.isModule());
        assertTrue("Map must be abstract", outerClassFile.isAbstract());
        assertTrue("Map must be an interface", outerClassFile.isInterface());
        assertTrue("Map must be public", outerClassFile.isPublic());

        // Outer/inner linkage checks
        assertEquals(outerClassFile, innerClassFile.getOuterClassFile());
        assertEquals(innerClassFile, outerClassFile.getInnerClassFiles().get(0));
        assertTrue(innerClassFile.isAInnerClass());
        assertFalse(outerClassFile.isAInnerClass());

        // Names
        assertEquals("java/util/Map", outerClassFile.getInternalTypeName());
        assertEquals("java/util/Map$Entry", innerClassFile.getInternalTypeName());

        // Inner type properties (Entry is also an interface; not an enum)
        assertTrue(innerClassFile.isInterface());
        assertFalse(innerClassFile.isEnum());
        assertTrue(innerClassFile.isAbstract());
        // Not asserting isStatic() — encoding can vary.

        // Class vs interface
        assertFalse("Map is an interface, not a class", outerClassFile.isClass());

        // Access flags round-trip
        int originalAccess = outerClassFile.getAccessFlags();
        assertEquals(originalAccess, outerClassFile.getAccessFlags());
        outerClassFile.setAccessFlags(0);
        assertEquals(0, outerClassFile.getAccessFlags());
        outerClassFile.setAccessFlags(originalAccess);

        // Not an annotation
        assertFalse(outerClassFile.isAnnotation());

        // Methods and fields presence (compare against underlying ASM node sizes)
        assertNotNull(outerClassFile.getMethods());
        assertEquals(outerClassFile.getClassNode().methods.size(), outerClassFile.getMethods().size());

        assertNotNull(outerClassFile.getFields());
        assertEquals(outerClassFile.getClassNode().fields.size(), outerClassFile.getFields().size());

        // Interfaces implemented/extended count
        List<String> ifaceNames = outerClassFile.getInterfaceTypeNames();
        assertNotNull(ifaceNames);
        assertEquals(outerClassFile.getClassNode().interfaces.size(), ifaceNames.size());

        // Super type name present (for interfaces ASM usually sets java/lang/Object)
        assertNotNull(outerClassFile.getSuperTypeName());

        // toString
        assertEquals("ClassFile{java/util/Map}", outerClassFile.toString());
        assertEquals("ClassFile{java/util/Map$Entry}", innerClassFile.toString());
    }
}
