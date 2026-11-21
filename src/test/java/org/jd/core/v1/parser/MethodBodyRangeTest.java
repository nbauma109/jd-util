/*
 * Copyright (c) 2026 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MethodBodyRangeTest {

    private static Map<String, String> bodies(String source) throws ParseException {
        return JdJavaSourceParser.parseMethodBodyRanges(source).stream()
                .collect(Collectors.toMap(MethodBodyRange::key, r -> source.substring(r.bodyStart(), r.bodyEnd()), (a, b) -> a + "|" + b));
    }

    private static final String SOURCE = """
            package p;
            import java.util.List;
            public class Outer<T> {
                private int f;
                public Outer() { f = 1; }
                public Outer(String s, int... rest) { f = 2; }
                public abstract int abstractOne(int a);
                public <U extends Comparable<U>> U generic(List<? extends U> list, java.util.Map<String, int[]> m, T[] arr) { return null; }
                void overload(java.lang.String s) { }
                void overload(int i) { }
                static class Nested {
                    Nested(long l) { }
                    int get() { return 1; }
                }
                Runnable r = new Runnable() { public void run() { } };
            }
            """;

    @Test
    public void everyMethodAndConstructorBodyIsLocatedWithAKeyThatIgnoresHowTypesAreWritten() throws ParseException {
        Map<String, String> bodies = bodies(SOURCE);

        assertEquals("{ f = 1; }", bodies.get("p/Outer#<init>()"));
        assertEquals("{ f = 2; }", bodies.get("p/Outer#<init>(String,int[])"));
        assertEquals("{ return null; }", bodies.get("p/Outer#generic(List,Map,T[])"));
        assertEquals("{ }", bodies.get("p/Outer#overload(String)"));
        assertEquals("{ }", bodies.get("p/Outer#overload(int)"));
        assertEquals("{ }", bodies.get("p/Outer$Nested#<init>(long)"));
        assertEquals("{ return 1; }", bodies.get("p/Outer$Nested#get()"));
        assertFalse(bodies.keySet().stream().anyMatch(k -> k.contains("abstractOne")));
    }

    @Test
    public void methodsOfAnonymousClassesAreLocatedToo() throws ParseException {
        Map<String, String> bodies = bodies(SOURCE);

        assertTrue(bodies.keySet().toString(), bodies.keySet().stream().anyMatch(k -> k.endsWith("#run()")));
    }

    @Test
    public void offsetsAreRightWithCrLfTabsAndUnicodeEscapes() throws ParseException {
        String source = "package p;\r\n"
                + "class A {\r\n"
                + "\tint a() {\r\n\t\treturn 1;\r\n\t}\r\n"
                + "\tString s() { return \"\\u00e9\\t\"; }\r\n"
                + "\tint b() { return 2; }\r\n"
                + "}\r\n";

        Map<String, String> bodies = bodies(source);

        assertEquals("{\r\n\t\treturn 1;\r\n\t}", bodies.get("p/A#a()"));
        assertEquals("{ return \"\\u00e9\\t\"; }", bodies.get("p/A#s()"));
        assertEquals("{ return 2; }", bodies.get("p/A#b()"));
    }

    @Test
    public void offsetsAreRightWithLoneCarriageReturns() throws ParseException {
        String source = "class A {\rint a() { return 1; }\rint b() { return 2; }\r}";

        Map<String, String> bodies = bodies(source);

        assertEquals("{ return 1; }", bodies.get("A#a()"));
        assertEquals("{ return 2; }", bodies.get("A#b()"));
    }

    @Test
    public void compactRecordConstructorsAndEnumMethodsAreLocated() throws ParseException {
        String source = "record R(int x) { R { if (x < 0) throw new IllegalArgumentException(); } int twice() { return x * 2; } }\n"
                + "enum E { A, B; int one() { return 1; } }\n";

        Map<String, String> bodies = bodies(source);

        assertEquals("{ return x * 2; }", bodies.get("R#twice()"));
        assertEquals("{ return 1; }", bodies.get("E#one()"));
        assertTrue(bodies.keySet().toString(), bodies.containsKey("R#<init>()"));
    }

    @Test
    public void theRangesAreImmutableAndReportTheirParametersInOrder() throws ParseException {
        List<MethodBodyRange> ranges = JdJavaSourceParser.parseMethodBodyRanges("class A { void m(int a, String b) { } }");

        assertEquals(1, ranges.size());
        assertEquals(List.of("int", "String"), ranges.get(0).parameterTypes());
        org.junit.Assert.assertThrows(UnsupportedOperationException.class, () -> ranges.get(0).parameterTypes().add("x"));
        org.junit.Assert.assertThrows(UnsupportedOperationException.class, () -> ranges.add(ranges.get(0)));
    }

    @Test
    public void theSameMethodHasTheSameKeyWhetherTypesAreQualifiedOrImported() throws ParseException {
        Function<String, String> key = src -> {
            try {
                return JdJavaSourceParser.parseMethodBodyRanges(src).get(0).key();
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        };

        assertEquals(
                key.apply("package p; class A { void m(java.util.List<java.lang.String> l, java.util.Map.Entry<String, String>[] e) { } }"),
                key.apply("package p; import java.util.*; class A { void m(List<String> l, Entry[] e) { } }"));
    }
}
