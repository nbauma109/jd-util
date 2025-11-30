package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Concurrency regression test for TypeMaker's global caches.
 * We drive class loading, field/method scanning, and descriptor/signature parsing from many threads.
 *
 * This intentionally targets the following caches by exercising their population paths:
 *  - internalTypeNameFieldNameToType
 *  - descriptorToObjectType
 *  - internalTypeNameToObjectType
 *  - internalTypeNameToTypeTypes
 *  - internalTypeNameMethodNameParameterCountToDeclaredParameterTypes
 *  - internalTypeNameMethodNameParameterCountToParameterTypes
 *  - internalTypeNameMethodNameDescriptorToMethodTypes
 *  - hierarchy
 */
public class TypeMakerConcurrencyTest {

    /**
     * We construct a TypeMaker over the JDK 7 test archive used elsewhere in the suite.
     * This gives us a large, stable classpath without relying on the runtime JDK.
     */
    private static TypeMaker newTypeMakerOnTestZip() throws Exception {
        InputStream is = TypeMakerConcurrencyTest.class
                .getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        if (is == null) {
            throw new IllegalStateException("Missing test resource: /zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        }
        Loader loader = new ZipLoader(is);
        return new TypeMaker(loader);
    }

    @Test
    public void concurrentClassLoadingAndParsingPopulatesAllCachesWithoutError() throws Exception {
        final TypeMaker typeMaker = TypeMakerConcurrencyTest.newTypeMakerOnTestZip();

        // A broad set of internal names likely present in the JDK 7 zip (and used in existing tests).
        // These hit core language, collections, I/O, reflection, and concurrency packages.
        final List<String> internalNames = List.of(
                "java/lang/Object", "java/lang/String", "java/lang/Integer", "java/lang/Long", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "java/lang/Throwable", "java/lang/Exception", "java/lang/RuntimeException", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "java/lang/Thread", "java/lang/Class", "java/lang/ClassLoader", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "java/util/List", "java/util/ArrayList", "java/util/LinkedList", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "java/util/Map", "java/util/HashMap", "java/util/LinkedHashMap", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "java/util/TreeMap", "java/util/Set", "java/util/HashSet", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "java/util/Collections", "java/util/Comparator", "java/util/Iterator", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "java/util/concurrent/ConcurrentHashMap", "java/util/concurrent/CopyOnWriteArrayList", //$NON-NLS-1$ //$NON-NLS-2$
                "java/io/InputStream", "java/io/OutputStream", "java/io/Reader", "java/io/Writer", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "java/nio/ByteBuffer", "java/nio/charset/Charset", //$NON-NLS-1$ //$NON-NLS-2$
                "javax/crypto/Cipher", "javax/crypto/spec/SecretKeySpec" //$NON-NLS-1$ //$NON-NLS-2$
        );

        // Representative descriptors and signatures to drive other cache paths.
        final List<String> descriptors = List.of(
                "Ljava/lang/String;", "Ljava/util/List;", "Ljava/util/Map;", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "[I", "[[Ljava/lang/Object;", "[Ljava/lang/String;" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        );

        final List<String> signaturesOrInternal = List.of(
                "Ljava/util/List<Ljava/lang/String;>;", //$NON-NLS-1$
                "Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;", //$NON-NLS-1$
                "Ljava/util/List<+Ljava/lang/Number;>;", //$NON-NLS-1$
                "Ljava/util/List<-Ljava/lang/Number;>;", //$NON-NLS-1$
                "java/util/List",         // internal type fallback path //$NON-NLS-1$
                "Ljava/lang/Integer;",    // descriptor fallback path //$NON-NLS-1$
                "[[Ljava/lang/String;"    // array type signature //$NON-NLS-1$
        );

        final int threads = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);
        final ExecutorService pool = Executors.newFixedThreadPool(threads);
        final CountDownLatch start = new CountDownLatch(1);
        final List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();

        // Tasks that force classfile loads and member scans, populating:
        //  - internalTypeNameToObjectType
        //  - internalTypeNameToTypeTypes
        //  - internalTypeNameFieldNameToType
        //  - internalTypeNameMethodName* maps
        //  - hierarchy
        for (int i = 0; i < 400; i++) {
            final String name = internalNames.get(i % internalNames.size());
            tasks.add(() -> {
                TypeMakerConcurrencyTest.await(start);
                ObjectType ot = typeMaker.makeFromInternalTypeName(name);
                // We assert some basic invariants to touch the resulting objects.
                assertTrue("Expected ObjectType", ot instanceof ObjectType); //$NON-NLS-1$
                // Accessing the qualified name forces name computation paths too.
                final String qn = ot.getQualifiedName();
                assertTrue(qn != null && !qn.isEmpty());
                return null;
            });
        }

        // Tasks that populate descriptorToObjectType (and array object types).
        for (int i = 0; i < 300; i++) {
            final String desc = descriptors.get(i % descriptors.size());
            tasks.add(() -> {
                TypeMakerConcurrencyTest.await(start);
                ObjectType t = typeMaker.makeFromDescriptor(desc);
                assertTrue("Descriptor must yield ObjectType for L-types", //$NON-NLS-1$
                        (desc.charAt(0) == 'L' && t != null) || desc.charAt(0) == '[');
                return null;
            });
        }

        // Tasks that populate signatureToType and exercise the signature-or-internal gateway.
        for (int i = 0; i < 300; i++) {
            final String s = signaturesOrInternal.get(i % signaturesOrInternal.size());
            tasks.add(() -> {
                TypeMakerConcurrencyTest.await(start);
                Type t = typeMaker.makeFromSignatureOrInternalTypeName(s);
                // We just ensure parsing does not throw and returns something sensible.
                assertTrue(t != null || s.startsWith("java/")); //$NON-NLS-1$
                return null;
            });
        }

        // Randomize and run all tasks under contention.
        Collections.shuffle(tasks);
        List<Future<Void>> futures = new ArrayList<Future<Void>>(tasks.size());
        for (Callable<Void> c : tasks) {
            futures.add(pool.submit(c));
        }
        start.countDown();

        for (Future<Void> f : futures) {
            // Will throw if any task failed with an exception (e.g., ConcurrentModificationException).
            f.get();
        }

        pool.shutdown();

        // A couple of sanity checks on stable types.
        ObjectType s1 = typeMaker.makeFromInternalTypeName("java/lang/String"); //$NON-NLS-1$
        ObjectType s2 = typeMaker.makeFromDescriptor("Ljava/lang/String;"); //$NON-NLS-1$
        assertEquals("java/lang/String", s2.getInternalName()); //$NON-NLS-1$
        assertEquals(s1.getQualifiedName(), s2.getQualifiedName());
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Interrupted", ie); //$NON-NLS-1$
        }
    }
}
