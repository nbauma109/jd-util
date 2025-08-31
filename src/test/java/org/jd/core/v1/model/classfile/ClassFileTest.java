/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.classfile;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

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

    @Test
    public void testAccessFlagsAndKinds() throws Exception {
        ClassFile cf = loadFrom(java.util.ArrayList.class);

        // Basic access
        assertTrue(cf.isPublic());
        assertFalse(cf.isInterface());
        assertFalse(cf.isEnum());
        assertFalse(cf.isAnnotation());
        assertFalse(cf.isModule());

        // Classes are classes (not interfaces or enums)
        assertTrue(cf.isClass());
        assertFalse(cf.isStatic()); // ArrayList is top-level, not static

        int flags = cf.getAccessFlags();
        assertTrue("Flags should include public", (flags & ACC_PUBLIC) != 0);

        // setAccessFlags should take effect
        cf.setAccessFlags(ACC_FINAL | ACC_PUBLIC);
        assertEquals(ACC_FINAL | ACC_PUBLIC, cf.getAccessFlags());
    }

    @Test
    public void testInterfaceAndEnumAndAnnotation() throws Exception {
        ClassFile iface = loadFrom(java.util.Map.class);
        assertTrue(iface.isInterface());
        assertTrue(iface.isAbstract());
        assertFalse(iface.isClass());

        ClassFile en = loadFrom(Thread.State.class);
        assertTrue(en.isEnum());
        assertFalse(en.isInterface());
        assertFalse(en.isAnnotation());

        ClassFile ann = loadFrom(Deprecated.class);
        assertTrue(ann.isAnnotation());
        assertFalse(ann.isEnum());
        assertFalse(ann.isClass());
    }

    @Test
    public void testModule() throws Exception {
        // Load the real module-info.class from java.base
        // module-info is in the root of the module package
        ClassFile moduleInfo;
        try (InputStream in = Module.class.getModule()
                                          .getResourceAsStream("module-info.class")) {
            assertNotNull("Could not load module-info.class", in);
            ClassReader cr = new ClassReader(in);
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            moduleInfo = new ClassFile(cn);
        }

        // module-info is not a class/interface/enum/annotation
        assertTrue("module-info must be a module", moduleInfo.isModule());
        assertFalse(moduleInfo.isClass());
        assertFalse(moduleInfo.isInterface());
        assertFalse(moduleInfo.isEnum());
        assertFalse(moduleInfo.isAnnotation());
    }

    @Test
    public void testVersions() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            assertNotNull("Missing test zip resource", is);
            ZipLoader loader = new ZipLoader(is);

            // Load java/lang/String from the JDK 1.7.0 zip
            ClassFile stringCf = ClassFileDeserializer.loadClassFile(loader, "org/jd/core/test/AnnotatedClass");
            assertEquals(51, stringCf.getMajorVersion());
            assertEquals(0, stringCf.getMinorVersion());
        }
    }

    @Test
    public void testCleanedCodeRoundTrip() throws Exception {
        // Load java.lang.String (always present in JDK)
        ClassFile classFile = ClassFileDeserializer.loadClassFile(new ClassPathLoader(), "java/lang/String");

        // Find a real method with a descriptor
        List<MethodNode> methods = classFile.getMethods();
        MethodNode method = methods.stream()
                                   .filter(m -> m.name.equals("substring") && m.desc.equals("(II)Ljava/lang/String;"))
                                   .findFirst()
                                   .orElseThrow(() -> new AssertionError("substring(int,int) not found in String"));

        String name = method.name;
        String desc = method.desc;

        // Initially: nothing stored in cleanedCode
        byte[] initial = classFile.getCleanedCode(name, desc);
        assertNotNull("Should return default empty array or real bytecode", initial);

        // Override with cleaned version (dummy: one NOP byte)
        byte[] cleaned = new byte[] { 0x00 };
        classFile.setCleanedCode(name, desc, cleaned);

        // Must return cleaned code now
        byte[] retrieved = classFile.getCleanedCode(name, desc);
        assertArrayEquals(cleaned, retrieved);

        // Reset with null → implementation stores empty array
        classFile.setCleanedCode(name, desc, null);
        byte[] afterReset = classFile.getCleanedCode(name, desc);
        assertEquals(0, afterReset.length);
    }

    @Test
    public void testInterfaceFlags() throws Exception {
        ClassFile cf = ClassFileDeserializer.loadClassFile(new ClassPathLoader(), "java/util/List");

        assertTrue(cf.isPublic());
        assertTrue(cf.isInterface());
        assertTrue(cf.isAbstract());
        assertFalse(cf.isEnum());
        assertFalse(cf.isStatic());
        assertFalse(cf.isClass());
    }

    @Test
    public void testEnumFlags() throws Exception {
        ClassFile cf = ClassFileDeserializer.loadClassFile(new ClassPathLoader(), "java/lang/annotation/RetentionPolicy");

        assertTrue(cf.isPublic());
        assertTrue(cf.isEnum());
        assertFalse(cf.isInterface());
        assertFalse(cf.isAbstract());
        assertFalse(cf.isStatic());
        assertFalse(cf.isClass());
    }

    @Test
    public void testAbstractClassFlags() throws Exception {
        ClassFile cf = ClassFileDeserializer.loadClassFile(new ClassPathLoader(), "java/util/AbstractList");

        assertTrue(cf.isPublic());
        assertTrue(cf.isAbstract());
        assertFalse(cf.isInterface());
        assertFalse(cf.isEnum());
        assertFalse(cf.isStatic());
        assertTrue(cf.isClass());
    }

    @Test
    public void testStaticNestedClassFlags() throws Exception {
        ClassFile cf = ClassFileDeserializer.loadClassFile(new ClassPathLoader(), "java/util/Collections$EmptyList");

        assertFalse(cf.isPublic());
        assertFalse(cf.isStatic());
        assertFalse(cf.isEnum());
        assertFalse(cf.isInterface());
        assertTrue(cf.isClass());
    }
}
