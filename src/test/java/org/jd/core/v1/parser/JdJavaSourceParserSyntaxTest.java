package org.jd.core.v1.parser;

import org.jd.core.v1.util.ParserRealigner;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** The syntax of the sources of the JDK that the parser has to read (see {@link JdJavaSourceParserJdkSourcesWithRealignerTest}) */
public class JdJavaSourceParserSyntaxTest {
    private static String realign(String source) throws ParseException {
        JavaParseResult result = JdJavaSourceParser.parse(source);

        assertNotNull(result);

        String realigned = new ParserRealigner().format(result);

        // (what is written can be read again)
        assertNotNull(JdJavaSourceParser.parse(realigned));
        return realigned;
    }

    @Test
    public void testVarargsOfARecordComponent() throws ParseException {
        String realigned = realign("""
                package a;
                public record Nested(String name, int hash, Object... values) {
                }
                """);

        assertTrue(realigned, realigned.contains("Object... values"));
    }

    @Test
    public void testAnnotationOfATypeParameter() throws ParseException {
        realign("""
                package a;
                public class Holder<@Deprecated T, @Deprecated U extends Number> {
                    <@Deprecated V> V identity(V value) {
                        return value;
                    }
                }
                """);
    }

    @Test
    public void testCaseLabelsWhichAreCastsToAPrimitiveType() throws ParseException {
        // (the arrow after the operand of the cast is the one of the case, not the one of a lambda)
        realign("""
                package a;
                public class Labels {
                    static final int A = 1;
                    static final int B = 2;

                    static int pick(long mechanism) {
                        return switch ((int) mechanism) {
                            case (int) A, (int) B -> 16;
                            case (int) 3L -> 20;
                            default -> 0;
                        };
                    }
                }
                """);
    }

    @Test
    public void testLambdaAsTheOperandOfACastToAReferenceType() throws ParseException {
        realign("""
                package a;
                public class Casts {
                    Runnable run = (Runnable & java.io.Serializable) () -> { };
                    java.util.function.Function<String, String> same = (java.util.function.Function<String, String>) value -> value;
                }
                """);
    }

    @Test
    public void testRuleOfASwitchExpressionWhoseBlockHasOnlyATryStatement() throws ParseException {
        // (the braces of the block stay: a 'try' cannot follow the arrow)
        String realigned = realign("""
                package a;
                public class Rules {
                    static String pick(Object o) {
                        return switch (o) {
                            case String s -> {
                                try {
                                    yield s.trim();
                                } catch (RuntimeException e) {
                                    throw new IllegalArgumentException(e);
                                }
                            }
                            default -> "none";
                        };
                    }
                }
                """);

        assertTrue(realigned, realigned.replaceAll("\\s+", " ").contains("case String s -> { try {"));
    }
}
