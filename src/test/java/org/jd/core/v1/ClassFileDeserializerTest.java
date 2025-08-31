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
import org.jd.core.v1.util.StringConstants;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClassFileDeserializerTest {

    @Test
    public void testMissingClass() throws Exception {
        class NoOpLoader implements Loader {
            @Override
            public boolean canLoad(String internalName) {
                return false;
            }
            @Override
            public byte[] load(String internalName) throws IOException {
                fail("Loader cannot load anything");
                return null;
            }
        }

        try {
            ClassFileDeserializer.loadClassFile(new NoOpLoader(), "DoesNotExist");
            fail("Expected exception");
        } catch (IllegalArgumentException expected) {
            assertEquals("Class DoesNotExist could not be loaded", expected.getMessage());
        }
    }

    @Test
    public void testAnnotatedClass_AllAnnotations() throws Exception {
        try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            assertNotNull("Test resource not found", is);

            ZipLoader loader = new ZipLoader(is);

            ClassFile cf = ClassFileDeserializer.loadClassFile(loader, "org/jd/core/test/AnnotatedClass");
            assertNotNull(cf);

            // ----- Class-level annotations -----
            AnnotationNode quality = findAnn(cf.getClassNode().visibleAnnotations, cf.getClassNode().invisibleAnnotations,
                    "Lorg/jd/core/test/annotation/Quality;");
            assertNotNull("Missing @Quality", quality);

            AnnotationNode author = findAnn(cf.getClassNode().visibleAnnotations, cf.getClassNode().invisibleAnnotations,
                    "Lorg/jd/core/test/annotation/Author;");
            assertNotNull("Missing @Author", author);

            // @Author(value=@Name(...), contributors={@Name("Huey"), @Name("Dewey"), @Name("Louie")})
            // value: nested annotation @Name with fields salutation, value, last
            Object authorValue = getAnnValue(author, "value");
            assertTrue("author.value should be an AnnotationNode", authorValue instanceof AnnotationNode);
            AnnotationNode nameValue = (AnnotationNode) authorValue;
            assertEquals("Lorg/jd/core/test/annotation/Name;", nameValue.desc);
            assertEquals("Mr", getAnnValue(nameValue, "salutation"));
            assertEquals("Donald", getAnnValue(nameValue, "value"));
            assertEquals("Duck", getAnnValue(nameValue, "last"));

            // contributors: List<AnnotationNode> of @Name
            Object contributorsObj = getAnnValue(author, "contributors");
            assertTrue("author.contributors should be a List", contributorsObj instanceof List<?>);
            @SuppressWarnings("unchecked")
            List<Object> contributors = (List<Object>) contributorsObj;
            assertEquals(3, contributors.size());
            assertNameLiteral(contributors.get(0), "Huey");
            assertNameLiteral(contributors.get(1), "Dewey");
            assertNameLiteral(contributors.get(2), "Louie");

            // ----- Field-level annotations -----
            List<FieldNode> fields = cf.getClassNode().fields;
            assertEquals(10, fields.size());

            FieldNode b1 = fields.stream()
                    .filter(f -> f.name.equals("b1"))
                    .findFirst()
                    .orElseThrow();
            assertNotNull("Missing @Value on field b1",
                    findAnn(b1.visibleAnnotations, b1.invisibleAnnotations, "Lorg/jd/core/test/annotation/Value;"));

            FieldNode str2 = fields.stream()
                    .filter(f -> f.name.equals("str2"))
                    .findFirst()
                    .orElseThrow();
            assertNotNull("Missing @Value on field str2",
                    findAnn(str2.visibleAnnotations, str2.invisibleAnnotations, "Lorg/jd/core/test/annotation/Value;"));

            // ----- Methods, constructor + cleaned code -----
            List<MethodNode> methods = cf.getClassNode().methods;
            assertEquals(3, methods.size());

            MethodNode ctor = methods.get(0);
            assertEquals(StringConstants.INSTANCE_CONSTRUCTOR, ctor.name);
            assertEquals("()V", ctor.desc);
            assertNotNull("cleaned code must exist", cf.getCleanedCode(ctor.name, ctor.desc));
        }
    }

    @Test
    public void testLoaderCannotLoadThrows() {
        Loader badLoader = new Loader() {
            @Override
            public boolean canLoad(String internalName) {
                return false;
            }

            @Override
            public byte[] load(String internalName) {
                return null;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> ClassFileDeserializer.loadClassFile(badLoader, "some/InvalidClass"));
    }

    @Test
    public void testLoaderReturnsNullBytesThrows() {
        Loader badLoader = new Loader() {
            @Override
            public boolean canLoad(String internalName) {
                return true;
            }

            @Override
            public byte[] load(String internalName) {
                return null;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> ClassFileDeserializer.loadClassFile(badLoader, "org/jd/core/SomeClass"));
    }

    @Test
    public void testNullLoaderThrows() {
        assertThrows(IllegalArgumentException.class, () -> ClassFileDeserializer.loadClassFile(null, "org/jd/core/SomeClass"));
    }

    // ---------- Helpers ----------

    private static void assertNameLiteral(Object o, String expectedValue) {
        assertTrue("Expected an AnnotationNode", o instanceof AnnotationNode);
        AnnotationNode n = (AnnotationNode) o;
        assertEquals("Lorg/jd/core/test/annotation/Name;", n.desc);
        Object v = getAnnValue(n, "value"); // @Name("Huey") stores under 'value'
        assertEquals(expectedValue, v);
    }

    private static AnnotationNode findAnn(List<AnnotationNode> visible, List<AnnotationNode> invisible, String desc) {
        return Stream.concat(streamOf(visible), streamOf(invisible))
                .filter(Objects::nonNull)
                .filter(a -> desc.equals(a.desc))
                .findFirst()
                .orElse(null);
    }

    /** ASM stores AnnotationNode.values as [name1, val1, name2, val2, ...] or null. */
    private static Object getAnnValue(AnnotationNode ann, String key) {
        if (ann == null || ann.values == null) return null;
        List<?> v = ann.values;
        for (int i = 0; i + 1 < v.size(); i += 2) {
            Object k = v.get(i);
            if (key.equals(k)) {
                return v.get(i + 1);
            }
        }
        return null;
    }

    private static <T> Stream<T> streamOf(List<T> list) {
        return list == null ? Stream.empty() : list.stream();
    }
}
