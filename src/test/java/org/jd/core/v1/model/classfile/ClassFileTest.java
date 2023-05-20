package org.jd.core.v1.model.classfile;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClassFileTest {

    @Test
    public void testClassFile() throws Exception {
        JavaClass outerJavaClass = Repository.lookupClass("java.util.Map");
        JavaClass innerJavaClass = Repository.lookupClass("java.util.Map$Entry");
        
        ClassFile outerClassFile = new ClassFile(outerJavaClass);
        ClassFile innerClassFile = new ClassFile(innerJavaClass);

        // set outer class for inner class
        innerClassFile.setOuterClassFile(outerClassFile);

        // set inner classes for outer class
        outerClassFile.setInnerClassFiles(Collections.singletonList(innerClassFile));

        // Verify isModule, isAbstract, isInterface, and isPublic
        assertFalse(outerClassFile.isModule());
        assertTrue(outerClassFile.isAbstract());
        assertTrue(outerClassFile.isInterface());
        assertTrue(outerClassFile.isPublic());

        // Verify outer and inner class relationship
        assertEquals(outerClassFile, innerClassFile.getOuterClassFile());
        assertEquals(innerClassFile, outerClassFile.getInnerClassFiles().get(0));
        assertTrue(innerClassFile.isAInnerClass());
        assertFalse(outerClassFile.isAInnerClass());

        // Verify other properties of the classes
        assertEquals("java/util/Map", outerClassFile.getInternalTypeName());
        assertEquals("java/util/Map$Entry", innerClassFile.getInternalTypeName());
        assertTrue(innerClassFile.isInterface());
        assertFalse(innerClassFile.isEnum());
        assertTrue(innerClassFile.isAbstract());
        assertFalse(innerClassFile.isStatic());

        // Test major and minor version
        assertEquals(outerJavaClass.getMajor(), outerClassFile.getMajorVersion());
        assertEquals(outerJavaClass.getMinor(), outerClassFile.getMinorVersion());

        // Test class
        assertFalse(outerClassFile.isClass());

        // Test access flags
        assertEquals(outerJavaClass.getAccessFlags(), outerClassFile.getAccessFlags());

        // Test setAccessFlags
        outerClassFile.setAccessFlags(0);
        assertEquals(0, outerClassFile.getAccessFlags());

        // Test annotation
        assertFalse(outerClassFile.isAnnotation());

        // Test methods
        assertNotNull(outerClassFile.getMethods());
        assertEquals(outerJavaClass.getMethods().length, outerClassFile.getMethods().length);

        // Test fields
        assertNotNull(outerClassFile.getFields());
        assertEquals(outerJavaClass.getFields().length, outerClassFile.getFields().length);

        // Test getInterfaceTypeNames
        assertNotNull(outerClassFile.getInterfaceTypeNames());
        assertEquals(outerJavaClass.getInterfaces().length, outerClassFile.getInterfaceTypeNames().length);

        // Test getSuperTypeName
        assertNotNull(outerClassFile.getSuperTypeName());

        // Test getAttributes
        assertNotNull(outerClassFile.getAttributes());

        // Test getAnnotationEntries
        assertNotNull(outerClassFile.getAnnotationEntries());

        // Test getAttribute
        assertNotNull(outerClassFile.getAttribute(Const.ATTR_SOURCE_FILE));

        // Verify the toString method
        assertEquals("ClassFile{java/util/Map}", outerClassFile.toString());
        assertEquals("ClassFile{java/util/Map$Entry}", innerClassFile.toString());
    }
}
