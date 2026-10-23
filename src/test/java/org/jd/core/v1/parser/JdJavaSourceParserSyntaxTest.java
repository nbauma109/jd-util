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

    @Test
    public void testTypeWithArgumentsOfItsOuterClass() throws ParseException {
        // 'Outer<A, B>.Inner': the type arguments belong to the outer class
        realign("""
                package a;
                public class Nested {
                    Outer<String, Integer>.Inner field;

                    void use(Outer<String, Integer>.Inner parameter) {
                        Outer<String, Integer>.Inner local = parameter;
                        Outer<String, Integer>.Inner[] array = new Outer[0];
                        for (Outer<String, Integer>.Inner each : array) {
                            local = each;
                        }
                    }

                    Outer<String, Integer>.Inner result() {
                        return field;
                    }
                }
                """);
    }

    @Test
    public void testArgumentsAfterAnArrayInitializer() throws ParseException {
        String realigned = realign("""
                package a;
                public class Arguments {
                    Arguments(Object[] locations, String name, Class<?> type) {
                    }

                    Arguments(Object location, String name, Class<?> type) {
                        this(location == null ? new Object[0] : new Object[] { location }, name, type);
                    }

                    void use(Object[] array, String name, boolean flag) {
                        use(new Object[] { name }, name, flag);
                        use(flag ? array : new Object[] { name, name }, name, flag);
                        use(flag ? new Object[] { name } : array, name, flag);
                    }
                }
                """);

        // (a comma follows each initializer)
        assertTrue(realigned, realigned.replaceAll("\\s+", " ").contains("{ location }, name, type)"));
        assertTrue(realigned, realigned.replaceAll("\\s+", " ").contains("{ name, name }, name, flag)"));
    }

    @Test
    public void testServiceWithoutProvider() throws Exception {
        String source = "module m {\n    provides a.B with ;\n    provides a.C with d.E;\n}\n";

        assertNotNull(realign(source));
    }

    @Test
    public void testSwitchRulesWhichThrowOrYield() throws ParseException {
        realign("""
                package a;
                public class Rules {
                    static int pick(int kind) {
                        return switch (kind) {
                            case 1 -> throw new IllegalStateException();
                            case 2 -> {
                                if (kind > 1) {
                                    yield 3;
                                }
                                yield 4;
                            }
                            default -> 0;
                        };
                    }
                }
                """);
    }

    @Test
    public void testAnnotationsOfTypeParametersAreKept() throws ParseException {
        String realigned = realign("""
                package a;
                public class Holder<@Deprecated T, @Deprecated U extends Number> {
                }
                """);

        assertTrue(realigned, realigned.contains("<@Deprecated T, @Deprecated U extends Number>"));
    }

    @Test
    public void testAnnotatedEllipsisOfARecordComponent() throws ParseException {
        String realigned = realign("""
                package a;
                public record R(String @Deprecated ... values) {
                }
                """);

        assertTrue(realigned, realigned.contains("String @Deprecated ... values"));
    }

    @Test
    public void testParameterizedMemberTypesInLocalDeclarations() throws ParseException {
        realign("""
                package a;
                public class Members {
                    void m() {
                        Outer<java.util.List<String>>.Inner value = null;
                        Outer<String>.Middle.Inner other = null;
                    }
                }
                """);
    }
}
