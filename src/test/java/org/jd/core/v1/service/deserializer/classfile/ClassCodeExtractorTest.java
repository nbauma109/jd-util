package org.jd.core.v1.service.deserializer.classfile;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.loader.ClassPathLoader;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClassCodeExtractorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownConstantPoolTagThrows() {
        // Minimal fake classfile:
        // CAFEBABE (magic)
        // minor_version = 0x0000
        // major_version = 0x0034 (Java 8)
        // constant_pool_count = 2 (means 1 entry)
        // bogus constant pool entry tag = 99 (not valid)
        byte[] bogusClass = new byte[] {
            (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE,  // magic
            0x00, 0x00,  // minor_version
            0x00, 0x34,  // major_version
            0x00, 0x02,  // cp_count = 2 (means 1 entry follows)
            99           // bogus tag
            // no more bytes needed, parser should fail right here
        };

        // Should throw IllegalArgumentException("Unknown CP tag: 99")
        ClassCodeExtractor.extractCode(bogusClass);
    }


    @Test
    public void testConstantPoolPackageTag() throws Exception {
        // Use a module-info.class from the JDK itself, e.g. java.base module
        Loader loader = new ClassPathLoader();

        // Extract byte[] of the module-info class
        byte[] classBytes = loader.load("module-info");
        assertNotNull("Could not load module-info.class", classBytes);

        // Run through extractor
        Map<ClassCodeExtractor.MethodKey, ClassCodeExtractor.Code> map =
                ClassCodeExtractor.extractCode(classBytes);

        // module-info has no methods, so result is empty, but CP contained CONSTANT_Package
        assertTrue(map.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUtf8NotAStringThrows() {
        Object[] cp = new Object[2];
        cp[1] = 123; // not a String
        // should throw IllegalArgumentException
        invokeUtf8(cp, 1);
    }

    private static String invokeUtf8(Object[] cp, int index) {
        try {
            var m = ClassCodeExtractor.class.getDeclaredMethod("utf8", Object[].class, int.class);
            m.setAccessible(true);
            return (String) m.invoke(null, cp, index);
        } catch (ReflectiveOperationException e) {
            if (e.getCause() instanceof RuntimeException re) throw re;
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMethodKeyEqualsAndHashCode() {
        ClassCodeExtractor.MethodKey k1 = new ClassCodeExtractor.MethodKey("foo", "()V");
        ClassCodeExtractor.MethodKey k2 = new ClassCodeExtractor.MethodKey("foo", "()V");
        ClassCodeExtractor.MethodKey k3 = new ClassCodeExtractor.MethodKey("bar", "()V");
        ClassCodeExtractor.MethodKey k4 = new ClassCodeExtractor.MethodKey("foo", "(I)V");

        // same instance
        assertTrue(k1.equals(k1));

        // null
        assertFalse(k1.equals(null));

        // different type
        assertFalse(k1.equals("not a key"));

        // equal contents
        assertTrue(k1.equals(k2));
        assertEquals(k1.hashCode(), k2.hashCode());

        // different name
        assertFalse(k1.equals(k3));

        // different descriptor
        assertFalse(k1.equals(k4));

        // consistent behavior in collections
        HashSet<ClassCodeExtractor.MethodKey> set = new HashSet<>();
        set.add(k1);
        assertTrue(set.contains(k2));
        assertFalse(set.contains(k3));
        assertFalse(set.contains(k4));
    }
}
