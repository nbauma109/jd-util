/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Regression tests for the multi-release-jar handling in {@link ClassFileDeserializer}: the
 * lookup key used to fetch bytecode (which may carry a "META-INF/versions/N/" prefix) must not
 * be confused with the real (always unprefixed) bytecode names read from the constant pool.
 */
public class ClassFileDeserializerMultiReleaseTest extends TestCase {

    private static final String SOURCE =
            "package test.mrjar;\n" +
            "public class Outer {\n" +
            "    public class Inner {\n" +
            "    }\n" +
            "}\n";

    private static Map<String, byte[]> compile() throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        Map<String, byte[]> classBytes = new HashMap<>();

        JavaFileObject source = new SimpleJavaFileObject(URI.create("string:///test/mrjar/Outer.java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return SOURCE;
            }
        };

        javax.tools.StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
        javax.tools.ForwardingJavaFileManager<javax.tools.StandardJavaFileManager> fileManager =
                new javax.tools.ForwardingJavaFileManager<>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, javax.tools.FileObject sibling) {
                return new SimpleJavaFileObject(URI.create("string:///" + className.replace('.', '/') + ".class"), kind) {
                    @Override
                    public OutputStream openOutputStream() {
                        return new ByteArrayOutputStream() {
                            @Override
                            public void close() {
                                classBytes.put(className.replace('.', '/'), toByteArray());
                            }
                        };
                    }
                };
            }
        };

        boolean success = compiler.getTask(null, fileManager, null, null, null, List.of(source)).call();
        assertTrue("test source must compile", success);
        assertTrue(classBytes.containsKey("test/mrjar/Outer"));
        assertTrue(classBytes.containsKey("test/mrjar/Outer$Inner"));
        return classBytes;
    }

    private static Loader loaderOf(Map<String, byte[]> entries) {
        return new Loader() {
            @Override
            public boolean canLoad(String internalName) {
                return entries.containsKey(internalName);
            }

            @Override
            public byte[] load(String internalName) {
                return entries.get(internalName);
            }
        };
    }

    @Test
    public void testInnerClassVersionedAtSamePathAsOuter() throws Exception {
        Map<String, byte[]> classes = compile();
        Map<String, byte[]> versioned = new HashMap<>();
        versioned.put("META-INF/versions/9/test/mrjar/Outer", classes.get("test/mrjar/Outer"));
        versioned.put("META-INF/versions/9/test/mrjar/Outer$Inner", classes.get("test/mrjar/Outer$Inner"));

        ClassFile classFile = new ClassFileDeserializer().loadClassFile(loaderOf(versioned), "META-INF/versions/9/test/mrjar/Outer");

        assertNotNull(classFile);
        assertEquals("test/mrjar/Outer", classFile.getInternalTypeName());
        assertNotNull(classFile.getInnerClassFiles());
        assertEquals(1, classFile.getInnerClassFiles().size());
        assertEquals("test/mrjar/Outer$Inner", classFile.getInnerClassFiles().get(0).getInternalTypeName());
    }

    @Test
    public void testInnerClassNotVersionedFallsBackToBasePath() throws Exception {
        Map<String, byte[]> classes = compile();
        Map<String, byte[]> mixed = new HashMap<>();
        // Only the outer class is versioned; the (unchanged) inner class stays at its base path.
        mixed.put("META-INF/versions/9/test/mrjar/Outer", classes.get("test/mrjar/Outer"));
        mixed.put("test/mrjar/Outer$Inner", classes.get("test/mrjar/Outer$Inner"));

        ClassFile classFile = new ClassFileDeserializer().loadClassFile(loaderOf(mixed), "META-INF/versions/9/test/mrjar/Outer");

        assertNotNull(classFile);
        assertNotNull(classFile.getInnerClassFiles());
        assertEquals(1, classFile.getInnerClassFiles().size());
        assertEquals("test/mrjar/Outer$Inner", classFile.getInnerClassFiles().get(0).getInternalTypeName());
    }
}
