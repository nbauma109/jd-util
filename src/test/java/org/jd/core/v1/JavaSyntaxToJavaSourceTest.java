/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1;

import org.apache.bcel.Const;
import org.jd.core.v1.loader.NopLoader;
import org.jd.core.v1.model.javasyntax.CompilationUnit;
import org.jd.core.v1.model.javasyntax.declaration.BodyDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ClassDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ConstructorDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.EnumDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ExpressionVariableInitializer;
import org.jd.core.v1.model.javasyntax.declaration.FieldDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.FieldDeclarator;
import org.jd.core.v1.model.javasyntax.declaration.FormalParameter;
import org.jd.core.v1.model.javasyntax.declaration.FormalParameters;
import org.jd.core.v1.model.javasyntax.declaration.InterfaceDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclarator;
import org.jd.core.v1.model.javasyntax.declaration.MemberDeclarations;
import org.jd.core.v1.model.javasyntax.declaration.MethodDeclaration;
import org.jd.core.v1.model.javasyntax.expression.ArrayExpression;
import org.jd.core.v1.model.javasyntax.expression.BinaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.BooleanExpression;
import org.jd.core.v1.model.javasyntax.expression.DoubleConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.Expressions;
import org.jd.core.v1.model.javasyntax.expression.FieldReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.LambdaIdentifiersExpression;
import org.jd.core.v1.model.javasyntax.expression.LengthExpression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.NewExpression;
import org.jd.core.v1.model.javasyntax.expression.NullExpression;
import org.jd.core.v1.model.javasyntax.expression.ObjectTypeReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.ParenthesesExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.ThisExpression;
import org.jd.core.v1.model.javasyntax.statement.ExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.ForEachStatement;
import org.jd.core.v1.model.javasyntax.statement.IfStatement;
import org.jd.core.v1.model.javasyntax.statement.LambdaExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.LocalVariableDeclarationStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnStatement;
import org.jd.core.v1.model.javasyntax.statement.Statements;
import org.jd.core.v1.model.javasyntax.statement.SwitchStatement;
import org.jd.core.v1.model.javasyntax.reference.AnnotationReference;
import org.jd.core.v1.model.javasyntax.reference.ExpressionElementValue;
import org.jd.core.v1.model.javasyntax.reference.ElementValuePair;
import org.jd.core.v1.model.javasyntax.reference.AnnotationReferences;
import org.jd.core.v1.model.javasyntax.reference.TypeUseAnnotationReference;
import org.jd.core.v1.model.javasyntax.type.InnerObjectType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.GenericType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeParameterWithTypeBounds;
import org.jd.core.v1.model.javasyntax.type.Types;
import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.printer.PlainTextMetaPrinter;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.JavaSyntaxToJavaFragmentProcessor;
import org.jd.core.v1.service.layouter.LayoutFragmentProcessor;
import org.jd.core.v1.service.tokenizer.javafragmenttotoken.JavaFragmentToTokenProcessor;
import org.jd.core.v1.service.writer.WriteTokenProcessor;
import org.jd.core.v1.util.DefaultList;
import org.jd.core.v1.util.StringConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.apache.bcel.Const.ACC_FINAL;
import static org.apache.bcel.Const.ACC_PRIVATE;
import static org.apache.bcel.Const.ACC_PUBLIC;
import static org.apache.bcel.Const.ACC_STATIC;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import junit.framework.TestCase;

public class JavaSyntaxToJavaSourceTest extends TestCase {

    protected JavaSyntaxToJavaFragmentProcessor fragmenter = new JavaSyntaxToJavaFragmentProcessor();
    protected LayoutFragmentProcessor layouter = new LayoutFragmentProcessor();
    //protected TestTokenizeJavaFragmentProcessor tokenizer = new TestTokenizeJavaFragmentProcessor();
    protected JavaFragmentToTokenProcessor tokenizer = new JavaFragmentToTokenProcessor();
    protected WriteTokenProcessor writer = new WriteTokenProcessor();

    @Test
    public void testClassDeclaration() throws Exception {
        ObjectType stringArrayType = new ObjectType(StringConstants.JAVA_LANG_STRING, "java.lang.String", "String", 1);
        ObjectType printStreamType = new ObjectType("java/io/PrintStream", "java.io.PrintStream", "PrintStream");

        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/TokenWriterTest",
                "TokenWriterTest",
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/TokenWriterTest",
                    new MethodDeclaration(
                        Const.ACC_PUBLIC | Const.ACC_STATIC,
                        "main",
                        PrimitiveType.TYPE_VOID,
                        new FormalParameter(ObjectType.TYPE_STRING.createType(1), "args"),
                        "([Ljava/lang/String;)V",
                        new Statements(
                            new IfStatement(
                                new BinaryOperatorExpression(
                                    8,
                                    PrimitiveType.TYPE_BOOLEAN,
                                    new LocalVariableReferenceExpression(8, stringArrayType, "args"),
                                    "==",
                                    new NullExpression(8, stringArrayType),
                                    9
                                ),
                                ReturnStatement.RETURN
                            ),
                            new LocalVariableDeclarationStatement(
                                PrimitiveType.TYPE_INT,
                                new LocalVariableDeclarator(0,
                                    "i",
                                    new ExpressionVariableInitializer(
                                        new MethodInvocationExpression(
                                            10,
                                            PrimitiveType.TYPE_INT,
                                            new ThisExpression(10, new ObjectType("org/jd/core/v1/service/test/TokenWriterTest", "org.jd.core.v1.service.writer.TokenWriterTest", "TokenWriterTest")),
                                            "org/jd/core/v1/service/test/TokenWriterTest",
                                            "call",
                                            "(Ljava/lang/String;ILjava/util/Enumeration;C)I",
                                            new Expressions(
                                                new StringConstantExpression(11, "aaaa"),
                                                new LocalVariableReferenceExpression(12, PrimitiveType.TYPE_INT, "b"),
                                                new NewExpression(
                                                    13,
                                                    new ObjectType("java/util/Enumeration", "java.util.Enumeration", "Enumeration"),
                                                    "()V",
                                                    new BodyDeclaration(
                                                        "java/util/Enumeration",
                                                        new MemberDeclarations(
                                                            new MethodDeclaration(
                                                                Const.ACC_PUBLIC,
                                                                "hasMoreElements",
                                                                PrimitiveType.TYPE_BOOLEAN,
                                                                "()Z",
                                                                new ReturnExpressionStatement(new BooleanExpression(15, false))
                                                            ),
                                                            new MethodDeclaration(
                                                                Const.ACC_PUBLIC,
                                                                "nextElement",
                                                                ObjectType.TYPE_OBJECT,
                                                                "()Ljava/lang/Object;",
                                                                new ReturnExpressionStatement(new NullExpression(18, ObjectType.TYPE_OBJECT))
                                                            )
                                                        )
                                                    ),
                                                    false,
                                                    false
                                                ),
                                                new LocalVariableReferenceExpression(21, PrimitiveType.TYPE_CHAR, "c")
                                            ),
                                            null
                                        )
                                    )
                                )
                            ),
                            new ExpressionStatement(
                                new MethodInvocationExpression(
                                    22,
                                    PrimitiveType.TYPE_VOID,
                                    new FieldReferenceExpression(
                                        22,
                                        printStreamType,
                                        new ObjectTypeReferenceExpression(22, new ObjectType(StringConstants.JAVA_LANG_SYSTEM, "java.lang.System", "System")),
                                        StringConstants.JAVA_LANG_SYSTEM,
                                        "out",
                                        "Ljava/io/PrintStream;"
                                    ),
                                    "java/io/PrintStream",
                                    "println",
                                    "(I)V",
                                    new LocalVariableReferenceExpression(22, PrimitiveType.TYPE_INT, "i"),
                                    null
                                )
                            )
                        )
                    )
                )
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        Map<String, Object> configuration = Collections.singletonMap("realignLineNumbers", Boolean.TRUE);
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);

        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/TokenWriterTest");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(configuration);
        decompileContext.setMaxLineNumber(22);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);
        writer.process(decompileContext);

        String source = printer.toString();

        System.out.println("- - - - - - - - ");
        System.out.print(source);
        System.out.println("- - - - - - - - ");

        Assert.assertNotEquals(-1, source.indexOf("/* 22: 22 */"));
        Assert.assertEquals(-1, source.indexOf("java.lang.System"));
    }

    @Test
    public void testBlockLambdaAsOnlyParameterOfInvocationNestedInAnotherParameter() throws Exception {
        ObjectType operatorType = new ObjectType("java/util/function/BinaryOperator", "java.util.function.BinaryOperator", "BinaryOperator");
        ObjectType supplierType = new ObjectType("java/util/function/Supplier", "java.util.function.Supplier", "Supplier");
        ObjectType accumulatorType = new ObjectType("org/jd/core/v1/service/test/Accumulator", "org.jd.core.v1.service.test.Accumulator", "Accumulator");
        ObjectType testType = new ObjectType("org/jd/core/v1/service/test/NestedLambdaTest", "org.jd.core.v1.service.test.NestedLambdaTest", "NestedLambdaTest");

        NewExpression newAccumulator = new NewExpression(7, accumulatorType, "(Ljava/util/function/BinaryOperator;)V", false, false);
        newAccumulator.setParameters(new LambdaIdentifiersExpression(7, operatorType, PrimitiveType.TYPE_VOID, Arrays.asList("a", "b"), new Statements(ReturnStatement.RETURN, ReturnStatement.RETURN)));
        LambdaIdentifiersExpression supplier = new LambdaIdentifiersExpression(7, supplierType, accumulatorType, Collections.emptyList(), new LambdaExpressionStatement(newAccumulator));

        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/NestedLambdaTest",
                "NestedLambdaTest",
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/NestedLambdaTest",
                    new MethodDeclaration(
                        Const.ACC_PUBLIC | Const.ACC_STATIC,
                        "run",
                        PrimitiveType.TYPE_VOID,
                        "()V",
                        new ExpressionStatement(
                            new MethodInvocationExpression(
                                7,
                                PrimitiveType.TYPE_VOID,
                                new ObjectTypeReferenceExpression(7, testType),
                                "org/jd/core/v1/service/test/NestedLambdaTest",
                                "use",
                                "(Ljava/util/function/Supplier;I)V",
                                new Expressions(supplier, new IntegerConstantExpression(PrimitiveType.TYPE_INT, 1)),
                                null
                            )
                        )
                    )
                )
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);
        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/NestedLambdaTest");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(Collections.emptyMap());
        decompileContext.setMaxLineNumber(7);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);
        writer.process(decompileContext);

        String source = printer.toString();

        // The closing brace of the lambda is followed by the closing parenthesis of the instance creation, not by a separator
        Assert.assertTrue(source, source.contains("}), 1)"));
        Assert.assertFalse(source, source.contains(", )"));
    }

    @Test
    public void testAnnotationOnTheBoundOfATypeParameter() throws Exception {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");

        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                null,
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/Holder",
                "Holder",
                new TypeParameterWithTypeBounds("K", ObjectType.TYPE_OBJECT, new AnnotationReference(nullableType)),
                null,
                null,
                new BodyDeclaration("org/jd/core/v1/service/test/Holder", new MethodDeclaration(Const.ACC_PUBLIC, "run", PrimitiveType.TYPE_VOID, "()V", ReturnStatement.RETURN))
            )
        );

        org.jd.core.v1.printer.PlainTextPrinter printer = new org.jd.core.v1.printer.PlainTextPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);
        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/Holder");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(Collections.emptyMap());
        decompileContext.setMaxLineNumber(7);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        decompileContext.setTokens(tokenizer.process(decompileContext.getBody()));
        writer.process(decompileContext);

        String source = printer.toString();

        // the annotation is imported and written in front of the bound
        Assert.assertTrue(source, source.contains("import org.jspecify.annotations.Nullable;"));
        Assert.assertTrue(source, source.contains("class Holder<K extends @Nullable Object>"));
    }

    private String printClassWithTypeUseAnnotations(org.jd.core.v1.api.loader.Loader loader) {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        ObjectType stringType = ObjectType.TYPE_STRING;
        TypeUseAnnotationReference nullable = new TypeUseAnnotationReference(nullableType);

        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/Annotated",
                "Annotated",
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/Annotated",
                    new MemberDeclarations(
                        new FieldDeclaration(nullable, Const.ACC_PRIVATE, stringType, new FieldDeclarator("value")),
                        new FieldDeclaration(nullable, Const.ACC_PRIVATE, new InnerObjectType("java/util/Map$Entry", "java.util.Map$Entry", "Entry", new ObjectType("java/util/Map", "java.util.Map", "Map")), new FieldDeclarator("entry")),
                        new MethodDeclaration(
                            nullable, Const.ACC_PUBLIC, "find", null, stringType,
                            new FormalParameter(nullable, stringType, false, "key"), null, "(Ljava/lang/String;)Ljava/lang/String;", ReturnStatement.RETURN, null)
                    )
                )
            )
        );

        org.jd.core.v1.printer.PlainTextPrinter printer = new org.jd.core.v1.printer.PlainTextPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);
        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/Annotated");
        decompileContext.setLoader(loader);
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(Collections.emptyMap());
        decompileContext.setMaxLineNumber(7);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        decompileContext.setTokens(tokenizer.process(decompileContext.getBody()));
        writer.process(decompileContext);

        return printer.toString().replaceAll("/\\*[^*]*\\*/", "").replaceAll("\\s+", " ");
    }

    @Test
    public void testTypeUseAnnotationsAreInFrontOfTheNameOfTheType() {
        String source = printClassWithTypeUseAnnotations(new NopLoader());

        Assert.assertTrue(source, source.contains("private @Nullable String value;"));
        // (a nested class is annotated after its outer class)
        Assert.assertTrue(source, source.contains("private Map.@Nullable Entry entry;"));
        Assert.assertTrue(source, source.contains("public @Nullable String find(@Nullable String key)"));
        Assert.assertTrue(source, source.contains("import org.jspecify.annotations.Nullable;"));
    }

    @Test
    public void testAnnotationOfDeclarationsAndTypesIsWrittenOnceOnTheType() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        AnnotationReferences<AnnotationReference> both = new AnnotationReferences<>();
        ExpressionElementValue why = new ExpressionElementValue(new StringConstantExpression("why"));

        both.add(new AnnotationReference(nullableType, why));
        both.add(new TypeUseAnnotationReference(nullableType, why));
        String source = print(new FieldDeclaration(both, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")));

        // the same annotation (with the same elements) once, in front of the type
        Assert.assertTrue(source, source.contains("private @Nullable(\"why\") String value;"));
        Assert.assertEquals(source, 1, source.split("@Nullable", -1).length - 1);
    }

    @Test
    public void testAnnotationWithAnEmptyListOfElementsIsTheSameAsOneWithoutElements() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        AnnotationReferences<AnnotationReference> both = new AnnotationReferences<>();

        // (the converter of the class files gives an empty list to an annotation without elements)
        both.add(new AnnotationReference(nullableType, new org.jd.core.v1.model.javasyntax.reference.ElementValuePairs(0)));
        both.add(new TypeUseAnnotationReference(nullableType));
        String source = print(new FieldDeclaration(both, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")));

        Assert.assertTrue(source, source.contains("private @Nullable String value;"));
        Assert.assertEquals(source, 1, source.split("@Nullable", -1).length - 1);
    }

    @Test
    public void testAnnotationOfAConstructorAndOfItsResultIsWrittenOnce() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        AnnotationReferences<AnnotationReference> both = new AnnotationReferences<>();

        both.add(new AnnotationReference(nullableType));
        both.add(new TypeUseAnnotationReference(nullableType));
        String twins = print(new ConstructorDeclaration(both, Const.ACC_PUBLIC, null, null, null, "()V", ReturnStatement.RETURN));
        // (a type annotation of the result which is not also on the constructor stays)
        String alone = print(new ConstructorDeclaration(new TypeUseAnnotationReference(nullableType), Const.ACC_PUBLIC, null, null, null, "()V", ReturnStatement.RETURN));

        Assert.assertTrue(twins, twins.contains("@Nullable public Annotated()"));
        Assert.assertEquals(twins, 1, twins.split("@Nullable", -1).length - 1);
        Assert.assertTrue(alone, alone.contains("@Nullable public Annotated()"));
    }

    @Test
    public void testAnnotationsWhoseElementsAreEqualButNotTheSameObjectsAreTheSame() {
        ObjectType sizeType = new ObjectType("p/Size", "p.Size", "Size");
        ObjectType unitType = new ObjectType("p/Unit", "p.Unit", "Unit");
        AnnotationReferences<AnnotationReference> both = new AnnotationReferences<>();

        // (the elements of the attribute of the declaration and of the one of the type are decoded separately)
        both.add(new AnnotationReference(sizeType, elementsOf(unitType, "x")));
        both.add(new TypeUseAnnotationReference(sizeType, elementsOf(unitType, "x")));
        String same = print(new FieldDeclaration(both, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("a")));

        AnnotationReferences<AnnotationReference> different = new AnnotationReferences<>();

        different.add(new AnnotationReference(sizeType, elementsOf(unitType, "x")));
        different.add(new TypeUseAnnotationReference(sizeType, elementsOf(unitType, "y")));
        String other = print(new FieldDeclaration(different, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("b")));

        Assert.assertEquals(same, 1, same.split("@Size", -1).length - 1);
        Assert.assertEquals(other, 2, other.split("@Size", -1).length - 1);
    }

    private static org.jd.core.v1.model.javasyntax.reference.ElementValuePairs elementsOf(ObjectType unitType, String text) {
        org.jd.core.v1.model.javasyntax.reference.ElementValues values = new org.jd.core.v1.model.javasyntax.reference.ElementValues();

        values.add(new ExpressionElementValue(new StringConstantExpression(text)));
        values.add(new org.jd.core.v1.model.javasyntax.reference.AnnotationElementValue(new AnnotationReference(unitType)));

        org.jd.core.v1.model.javasyntax.reference.ElementValuePairs pairs = new org.jd.core.v1.model.javasyntax.reference.ElementValuePairs();

        pairs.add(new ElementValuePair("values", new org.jd.core.v1.model.javasyntax.reference.ElementValueArrayInitializerElementValue(values)));
        return pairs;
    }

    @Test
    public void testAnnotationsOfTheSameSimpleNameButOfAnotherPackageAreNotTwins() {
        TypeUseAnnotationReference jspecify = new TypeUseAnnotationReference(new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable"));
        AnnotationReference javax = new AnnotationReference(new ObjectType("javax/annotation/Nullable", "javax.annotation.Nullable", "Nullable"));
        AnnotationReferences<AnnotationReference> two = new AnnotationReferences<>();

        two.add(javax);
        two.add(jspecify);
        String source = print(new FieldDeclaration(two, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")));

        // (neither is dropped for the other)
        Assert.assertTrue(source, source.contains("javax.annotation.Nullable") && source.contains("org.jspecify.annotations.Nullable"));
    }

    @Test
    public void testPartiallyOverlappingAnnotationsAreWrittenWithAQualifiedType() {
        ObjectType annotationType = new ObjectType("p/Tag", "p.Tag", "Tag");
        AnnotationReferences<AnnotationReference> three = new AnnotationReferences<>();
        ExpressionElementValue one = new ExpressionElementValue(new StringConstantExpression("1"));
        ExpressionElementValue two = new ExpressionElementValue(new StringConstantExpression("2"));

        // @Tag("1") on the declaration; @Tag("1") and @Tag("2") on the type
        three.add(new AnnotationReference(annotationType, one));
        three.add(new TypeUseAnnotationReference(annotationType, new ExpressionElementValue(new StringConstantExpression("1"))));
        three.add(new TypeUseAnnotationReference(annotationType, two));
        String source = print(new FieldDeclaration(three, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")));

        Assert.assertTrue(source, source.contains("@Tag(\"1\") private java.lang.@Tag(\"1\") @Tag(\"2\") String value;"));
    }

    /** The classes of the class path, for the annotations whose targets are read */
    private static final org.jd.core.v1.api.loader.Loader CLASS_PATH_LOADER = new org.jd.core.v1.api.loader.Loader() {
        @Override
        public boolean canLoad(String internalName) {
            return getClass().getClassLoader().getResource(internalName + ".class") != null;
        }

        @Override
        public byte[] load(String internalName) throws java.io.IOException {
            try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream(internalName + ".class")) {
                return in.readAllBytes();
            }
        }
    };

    @Test
    public void testTypeAnnotationOfDeclarationsToo() {
        ObjectType dual = new ObjectType("org/jd/core/v1/annotation/DualTarget", "org.jd.core.v1.annotation.DualTarget", "DualTarget");
        ObjectType typesOnly = new ObjectType("org/jd/core/v1/annotation/TypesOnly", "org.jd.core.v1.annotation.TypesOnly", "TypesOnly");

        // only on the type, but it can be on a declaration: in front of the name it would be on the declaration too
        String qualified = print(CLASS_PATH_LOADER, new FieldDeclaration(new TypeUseAnnotationReference(dual), Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("a")));
        String simple = print(CLASS_PATH_LOADER, new FieldDeclaration(new TypeUseAnnotationReference(typesOnly), Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("b")));
        // (its class cannot be read: it is taken for an annotation of types)
        String unknown = print(new FieldDeclaration(new TypeUseAnnotationReference(dual), Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("c")));

        Assert.assertTrue(qualified, qualified.contains("private java.lang.@DualTarget String a;"));
        Assert.assertTrue(simple, simple.contains("private @TypesOnly String b;"));
        Assert.assertTrue(unknown, unknown.contains("private @DualTarget String c;"));
    }

    @Test
    public void testTypeAnnotationOfAnInnerClassOfTheCurrentClassFollowsTheOuterClassWhenItMustBeQualified() {
        ObjectType dual = new ObjectType("org/jd/core/v1/annotation/DualTarget", "org.jd.core.v1.annotation.DualTarget", "DualTarget");
        ObjectType typesOnly = new ObjectType("org/jd/core/v1/annotation/TypesOnly", "org.jd.core.v1.annotation.TypesOnly", "TypesOnly");
        ObjectType annotated = new ObjectType("org/jd/core/v1/service/test/Annotated", "org.jd.core.v1.service.test.Annotated", "Annotated");
        InnerObjectType inner = new InnerObjectType("org/jd/core/v1/service/test/Annotated$Inner", "org.jd.core.v1.service.test.Annotated$Inner", "Inner", annotated);

        // (the class is declared in the current class, which is not written in front of its name unless the annotation needs it)
        String qualified = print(CLASS_PATH_LOADER, new FieldDeclaration(new TypeUseAnnotationReference(dual), Const.ACC_PRIVATE, inner, new FieldDeclarator("a")));
        String simple = print(CLASS_PATH_LOADER, new FieldDeclaration(new TypeUseAnnotationReference(typesOnly), Const.ACC_PRIVATE, inner, new FieldDeclarator("b")));

        Assert.assertTrue(qualified, qualified.contains("private Annotated.@DualTarget Inner a;"));
        Assert.assertTrue(simple, simple.contains("private @TypesOnly Inner b;"));
    }

    @Test
    public void testClassLiteralsOfTheSameSimpleNameAreNotTheSame() {
        ObjectType annotationType = new ObjectType("p/Uses", "p.Uses", "Uses");
        ObjectType first = new ObjectType("one/Foo", "one.Foo", "Foo");
        ObjectType second = new ObjectType("two/Foo", "two.Foo", "Foo");
        AnnotationReferences<AnnotationReference> both = new AnnotationReferences<>();

        both.add(new AnnotationReference(annotationType, new ExpressionElementValue(new org.jd.core.v1.model.javasyntax.expression.TypeReferenceDotClassExpression(first))));
        both.add(new TypeUseAnnotationReference(annotationType, new ExpressionElementValue(new org.jd.core.v1.model.javasyntax.expression.TypeReferenceDotClassExpression(second))));
        String source = print(new FieldDeclaration(both, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")));

        // (they are not the same annotation: each one is written)
        Assert.assertEquals(source, 2, source.split("@Uses", -1).length - 1);
    }

    @Test
    public void testAnnotationsWhichAreNotTheSameAreAllWritten() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        AnnotationReferences<AnnotationReference> different = new AnnotationReferences<>();

        different.add(new AnnotationReference(nullableType, new ExpressionElementValue(new StringConstantExpression("one"))));
        different.add(new TypeUseAnnotationReference(nullableType, new ExpressionElementValue(new StringConstantExpression("two"))));
        String source = print(new FieldDeclaration(different, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")));

        // (they would both apply to the declaration if the type were not written with its package)
        Assert.assertTrue(source, source.contains("@Nullable(\"one\") private java.lang.@Nullable(\"two\") String value;"));
    }

    @Test
    public void testAnnotationOfDeclarationsAndTypesIsWrittenTwiceWhenTheNameHasAQualifier() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        AnnotationReferences<AnnotationReference> both = new AnnotationReferences<>();

        both.add(new AnnotationReference(nullableType));
        both.add(new TypeUseAnnotationReference(nullableType));
        // a class of the package is named String: java.lang.String is written with its package, where an annotation of the type is after it and the
        // one of the declaration before
        String source = print(new FieldDeclaration(both, Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("value")), new NopLoader() {
            @Override
            public boolean canLoad(String internalName) {
                return "org/jd/core/v1/service/test/String".equals(internalName);
            }
        });

        Assert.assertTrue(source, source.contains("@Nullable private java.lang.@Nullable String value;"));
    }

    @Test
    public void testTypeUseAnnotationsOfAnArrayAreBesideItsBrackets() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        TypeUseAnnotationReference nullable = new TypeUseAnnotationReference(nullableType);

        String objects = print(new FieldDeclaration(nullable, Const.ACC_PRIVATE, ObjectType.TYPE_STRING.createType(1), new FieldDeclarator("names")));
        String matrix = print(new FieldDeclaration(nullable, Const.ACC_PRIVATE, PrimitiveType.TYPE_INT.createType(2), new FieldDeclarator("matrix")));
        String variables = print(new FieldDeclaration(nullable, Const.ACC_PRIVATE, new GenericType("T", 1), new FieldDeclarator("values")));

        Assert.assertTrue(objects, objects.contains("private String @Nullable [] names;"));
        Assert.assertTrue(matrix, matrix.contains("private int @Nullable [][] matrix;"));
        Assert.assertTrue(variables, variables.contains("private T @Nullable [] values;"));
    }

    @Test
    public void testTypeUseAnnotationsOfVarargsAreBeforeTheEllipsis() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        String source = print(new FieldDeclaration(null, Const.ACC_PRIVATE, PrimitiveType.TYPE_INT, new FieldDeclarator("unused")),
            new MethodDeclaration(0, "run", PrimitiveType.TYPE_VOID,
                new FormalParameter(new TypeUseAnnotationReference(nullableType), ObjectType.TYPE_STRING.createType(1), true, "names"), "([Ljava/lang/String;)V", ReturnStatement.RETURN));

        Assert.assertTrue(source, source.contains("void run(String @Nullable ... names)"));
    }

    @Test
    public void testTypeUseAnnotationOnAnImportedType() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        ObjectType listType = new ObjectType("java/util/List", "java.util.List", "List");
        String source = print(new FieldDeclaration(new TypeUseAnnotationReference(nullableType), Const.ACC_PRIVATE, listType, new FieldDeclarator("values")));

        // the name of the type is simple because of its import
        Assert.assertTrue(source, source.contains("import java.util.List;"));
        Assert.assertTrue(source, source.contains("private @Nullable List values;"));
    }

    @Test
    public void testTypeUseAnnotationOnATypeVariableAndOnAPrimitiveType() {
        ObjectType nullableType = new ObjectType("org/jspecify/annotations/Nullable", "org.jspecify.annotations.Nullable", "Nullable");
        TypeUseAnnotationReference nullable = new TypeUseAnnotationReference(nullableType);

        String variable = print(new FieldDeclaration(nullable, Const.ACC_PRIVATE, new GenericType("T"), new FieldDeclarator("value")));
        String primitive = print(new FieldDeclaration(nullable, Const.ACC_PRIVATE, PrimitiveType.TYPE_INT, new FieldDeclarator("count")));

        // (neither has a qualifier)
        Assert.assertTrue(variable, variable.contains("private @Nullable T value;"));
        Assert.assertTrue(primitive, primitive.contains("private @Nullable int count;"));
    }

    @Test
    public void testTypeUseAnnotationWithElementValuePairs() {
        ObjectType sizeType = new ObjectType("p/Size", "p.Size", "Size");
        ElementValuePair max = new ElementValuePair("max", new ExpressionElementValue(new StringConstantExpression("ten")));

        String pairs = print(new FieldDeclaration(new TypeUseAnnotationReference(sizeType, max), Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("a")));
        String both = print(new FieldDeclaration(new TypeUseAnnotationReference(sizeType, new ExpressionElementValue(new StringConstantExpression("one")), max),
                Const.ACC_PRIVATE, ObjectType.TYPE_STRING, new FieldDeclarator("b")));

        Assert.assertTrue(pairs, pairs.contains("@Size(max") && pairs.contains("String a;"));
        Assert.assertTrue(both, both.contains("@Size(\"one\") String b;"));
    }

    private String print(FieldDeclaration field) {
        return print(field, new NopLoader());
    }

    private String print(ConstructorDeclaration constructor) {
        return print(new NopLoader(), constructor);
    }

    private String print(FieldDeclaration field, org.jd.core.v1.api.loader.Loader loader) {
        return print(loader, field);
    }

    private String print(FieldDeclaration field, MethodDeclaration method) {
        return print(new NopLoader(), field, method);
    }

    private String print(org.jd.core.v1.api.loader.Loader loader, org.jd.core.v1.model.javasyntax.declaration.MemberDeclaration... members) {
        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(Const.ACC_PUBLIC, "org/jd/core/v1/service/test/Annotated", "Annotated",
                new BodyDeclaration("org/jd/core/v1/service/test/Annotated", members.length == 1 ? members[0]
                    : new MemberDeclarations(members[0], java.util.Arrays.copyOfRange(members, 1, members.length)))));
        org.jd.core.v1.printer.PlainTextPrinter printer = new org.jd.core.v1.printer.PlainTextPrinter();
        DecompileContext decompileContext = new DecompileContext();

        decompileContext.setCompilationUnit(compilationUnit);
        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/Annotated");
        decompileContext.setLoader(loader);
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(Collections.emptyMap());
        decompileContext.setMaxLineNumber(7);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        decompileContext.setTokens(tokenizer.process(decompileContext.getBody()));
        writer.process(decompileContext);

        return printer.toString().replaceAll("/\\*[^*]*\\*/", "").replaceAll("\\s+", " ");
    }

    @Test
    public void testTypeUseAnnotationsFollowTheQualifierOfTheName() {
        // a class of the package is named String: java.lang.String is written with its package, which an annotation cannot precede
        String source = printClassWithTypeUseAnnotations(new NopLoader() {
            @Override
            public boolean canLoad(String internalName) {
                return "org/jd/core/v1/service/test/String".equals(internalName);
            }
        });

        Assert.assertTrue(source, source.contains("private java.lang.@Nullable String value;"));
        Assert.assertTrue(source, source.contains("public java.lang.@Nullable String find(java.lang.@Nullable String key)"));
    }

    private String printCallWithParameters(int majorVersion, org.jd.core.v1.model.javasyntax.expression.Expression... parameters) throws Exception {
        ObjectType testType = new ObjectType("org/jd/core/v1/service/test/ReferenceTest", "org.jd.core.v1.service.test.ReferenceTest", "ReferenceTest");

        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/ReferenceTest",
                "ReferenceTest",
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/ReferenceTest",
                    new MethodDeclaration(
                        Const.ACC_PUBLIC | Const.ACC_STATIC,
                        "run",
                        PrimitiveType.TYPE_VOID,
                        "()V",
                        new ExpressionStatement(
                            new MethodInvocationExpression(
                                7,
                                PrimitiveType.TYPE_VOID,
                                new ObjectTypeReferenceExpression(7, testType),
                                "org/jd/core/v1/service/test/ReferenceTest",
                                "use",
                                "()V",
                                new Expressions(Arrays.asList(parameters)),
                                null
                            )
                        )
                    )
                )
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);
        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/ReferenceTest");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(Collections.emptyMap());
        decompileContext.setMaxLineNumber(7);
        decompileContext.setMajorVersion(majorVersion);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);
        writer.process(decompileContext);

        return printer.toString().replaceAll("<META-[^>]*/>", "");
    }

    @Test
    public void testTypeArgumentsOfConstructorAndMethodReferences() throws Exception {
        ObjectType builderType = new ObjectType("org/jd/core/v1/service/test/Builder", "org.jd.core.v1.service.test.Builder", "Builder");
        ObjectType parameterizedBuilderType = builderType.createType(new org.jd.core.v1.model.javasyntax.type.TypeArguments(Arrays.<org.jd.core.v1.model.javasyntax.type.TypeArgument>asList(
            new org.jd.core.v1.model.javasyntax.type.GenericType("K"), new org.jd.core.v1.model.javasyntax.type.GenericType("V"))));
        ObjectType supplierType = new ObjectType("java/util/function/Supplier", "java.util.function.Supplier", "Supplier");

        org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression plainConstructor =
            new org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression(7, supplierType, builderType, "()V");
        org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression typedConstructor =
            new org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression(7, supplierType, parameterizedBuilderType, "()V");
        org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression plainMethod =
            new org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression(7, supplierType, new ObjectTypeReferenceExpression(7, builderType), builderType.getInternalName(), "create", "()V");
        org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression typedMethod =
            new org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression(7, supplierType, new ObjectTypeReferenceExpression(7, builderType), builderType.getInternalName(), "create", "()V");

        typedMethod.setNonWildcardTypeArguments(new org.jd.core.v1.model.javasyntax.type.TypeArguments(Arrays.<org.jd.core.v1.model.javasyntax.type.TypeArgument>asList(
            new org.jd.core.v1.model.javasyntax.type.GenericType("K"), new org.jd.core.v1.model.javasyntax.type.GenericType("V"))));
        Assert.assertNull(plainMethod.getNonWildcardTypeArguments());
        Assert.assertEquals(typedMethod.getNonWildcardTypeArguments(), ((org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression) typedMethod.copyTo(8)).getNonWildcardTypeArguments());

        String source = printCallWithParameters(52, plainConstructor, typedConstructor, plainMethod, typedMethod);

        Assert.assertTrue(source, source.contains("Builder::new, Builder<K, V>::new"));
        Assert.assertTrue(source, source.contains("Builder::create, Builder::<K, V>create"));
    }

    @Test
    public void testExplicitParameterTypesOfLambdas() throws Exception {
        ObjectType functionType = new ObjectType("java/util/function/BiFunction", "java.util.function.BiFunction", "BiFunction");
        ObjectType listType = new ObjectType("java/util/List", "java.util.List", "List");
        ObjectType stringType = new ObjectType("java/lang/String", "java.lang.String", "String");

        LambdaIdentifiersExpression implicit = new LambdaIdentifiersExpression(7, functionType, stringType, Arrays.asList("a", "b"), new LambdaExpressionStatement(new IntegerConstantExpression(PrimitiveType.TYPE_INT, 1)));
        LambdaIdentifiersExpression explicit = new LambdaIdentifiersExpression(7, functionType, stringType, Arrays.asList("a", "b"), new LambdaExpressionStatement(new IntegerConstantExpression(PrimitiveType.TYPE_INT, 2)));

        implicit.setParameterTypes(Arrays.<Type>asList(listType, stringType));
        explicit.setParameterTypes(Arrays.<Type>asList(listType, stringType));
        explicit.setExplicitParameterTypes(true);
        Assert.assertTrue(explicit.isExplicitParameterTypes());
        Assert.assertFalse(implicit.isExplicitParameterTypes());
        Assert.assertEquals(explicit.getParameterTypes(), ((LambdaIdentifiersExpression) explicit.copyTo(8)).getParameterTypes());
        Assert.assertTrue(((LambdaIdentifiersExpression) explicit.copyTo(8)).isExplicitParameterTypes());

        String source = printCallWithParameters(52, implicit, explicit);

        Assert.assertTrue(source, source.contains("(a, b) -> 1, (java.util.List a, String b) -> 2"));
    }

    @Test
    public void testInterfaceDeclaration() throws Exception {
        ObjectType cloneableType = new ObjectType(StringConstants.JAVA_LANG_CLONEABLE, "java.lang.Cloneable", "Cloneable");
        ObjectType stringType = new ObjectType(StringConstants.JAVA_LANG_STRING, "java.lang.String", "String");
        ObjectType listType = new ObjectType("java/util/List", "java.util.List", "List", stringType);

        CompilationUnit compilationUnit = new CompilationUnit(
            new InterfaceDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/InterfaceTest",
                "InterfaceTest",
                new Types(listType, cloneableType)
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);

        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/InterfaceTest");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setMaxLineNumber(0);
        decompileContext.setMajorVersion(49);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);

        writer.process(decompileContext);

        String source = printer.toString();

        System.out.println("- - - - - - - - ");
        System.out.print(source);
        System.out.println("- - - - - - - - ");

        Assert.assertNotEquals(-1, source.indexOf("/*   1:   0 */ package org.jd.core.v1.service.test;"));
        Assert.assertNotEquals(-1, source.indexOf("interface InterfaceTest"));
        Assert.assertNotEquals(-1, source.indexOf("extends List"));
        Assert.assertNotEquals(-1, source.indexOf("<String"));
        Assert.assertNotEquals(-1, source.indexOf(", Cloneable"));
    }

    @Test
    public void testEnumDayDeclaration() throws Exception {
        CompilationUnit compilationUnit = new CompilationUnit(
            new EnumDeclaration(
                null,
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/Day",
                "Day",
                null,
                Arrays.asList(
                    new EnumDeclaration.Constant("SUNDAY"),
                    new EnumDeclaration.Constant("MONDAY"),
                    new EnumDeclaration.Constant("TUESDAY"),
                    new EnumDeclaration.Constant("WEDNESDAY"),
                    new EnumDeclaration.Constant("THURSDAY"),
                    new EnumDeclaration.Constant("FRIDAY"),
                    new EnumDeclaration.Constant("SATURDAY")
                ),
                null
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);

        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/Day");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setMaxLineNumber(0);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);

        writer.process(decompileContext);

        String source = printer.toString();

        System.out.println("- - - - - - - - ");
        System.out.print(source);
        System.out.println("- - - - - - - - ");

        Assert.assertTrue(source.indexOf(", MONDAY") != -1 || source.indexOf("-->MONDAY") != -1);
    }

    @Test
    @SuppressFBWarnings
    @SuppressWarnings("all")
    public void testEnumPlanetDeclaration() throws Exception {
        Type cloneableType = new ObjectType(StringConstants.JAVA_LANG_CLONEABLE, "java.lang.Cloneable", "Cloneable");
        Type stringType = ObjectType.TYPE_STRING;
        Type arrayOfStringType = stringType.createType(1);
        Type listType = new ObjectType("java/util/List", "java.util.List", "List", stringType);
        Type printStreamType = new ObjectType("java/io/PrintStream", "java.io.PrintStream", "PrintStream");
        Type planetType = new ObjectType("org/jd/core/v1/service/test/Planet", "org.jd.core.v1.service.test.Planet", "Planet");
        ThisExpression thisExpression = new ThisExpression(new ObjectType("org/jd/core/v1/service/test/Planet", "org.jd.core.v1.service.test.Planet", "Planet"));

        CompilationUnit compilationUnit = new CompilationUnit(
            new EnumDeclaration(
                ACC_PUBLIC,
                "org/jd/core/v1/service/test/Planet",
                "Planet",
                Arrays.asList(
                    new EnumDeclaration.Constant(
                        "MERCURY",
                        new Expressions(
                            new DoubleConstantExpression(3.303e+23),
                            new DoubleConstantExpression(2.4397e6)
                        )
                    ),
                    new EnumDeclaration.Constant(
                        "VENUS",
                        new Expressions(
                            new DoubleConstantExpression(4.869e+24),
                            new DoubleConstantExpression(6.0518e6)
                        )
                    ),
                    new EnumDeclaration.Constant(
                        "EARTH",
                        new Expressions(
                            new DoubleConstantExpression(5.976e+24),
                            new DoubleConstantExpression(6.37814e6)
                        )
                    )
                ),
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/Planet",
                    new MemberDeclarations(
                        new FieldDeclaration(ACC_PRIVATE | ACC_FINAL, PrimitiveType.TYPE_DOUBLE, new FieldDeclarator("mass")),
                        new FieldDeclaration(ACC_PRIVATE | ACC_FINAL, PrimitiveType.TYPE_DOUBLE, new FieldDeclarator("radius")),
                        new ConstructorDeclaration(
                            0,
                            new FormalParameters(
                                new FormalParameter(PrimitiveType.TYPE_DOUBLE, "mass"),
                                new FormalParameter(PrimitiveType.TYPE_DOUBLE, "radius")
                            ),
                            "(DD)V",
                            new Statements(
                                new ExpressionStatement(new BinaryOperatorExpression(
                                    0,
                                    PrimitiveType.TYPE_DOUBLE,
                                    new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "mass", "D"),
                                    "=",
                                    new LocalVariableReferenceExpression(PrimitiveType.TYPE_DOUBLE, "mass"),
                                    16
                                )),
                                new ExpressionStatement(new BinaryOperatorExpression(
                                    0,
                                    PrimitiveType.TYPE_DOUBLE,
                                    new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "radius", "D"),
                                    "=",
                                    new LocalVariableReferenceExpression(PrimitiveType.TYPE_DOUBLE, "radius"),
                                    16
                                ))
                            )
                        ),
                        new MethodDeclaration(
                            ACC_PRIVATE,
                            "mass",
                            PrimitiveType.TYPE_DOUBLE,
                            "()D",
                            new ReturnExpressionStatement(new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "mass", "D"))
                        ),
                        new MethodDeclaration(
                            ACC_PRIVATE,
                            "radius",
                            PrimitiveType.TYPE_DOUBLE,
                            "()D",
                            new ReturnExpressionStatement(new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "radius", "D"))
                        ),
                        new FieldDeclaration(
                            ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                            PrimitiveType.TYPE_DOUBLE,
                            new FieldDeclarator("G", new ExpressionVariableInitializer(new DoubleConstantExpression(6.67300E-11)))
                        ),
                        new MethodDeclaration(
                            0,
                            "surfaceGravity",
                            PrimitiveType.TYPE_DOUBLE,
                            "()D",
                            new ReturnExpressionStatement(
                                new BinaryOperatorExpression(
                                    0,
                                    PrimitiveType.TYPE_DOUBLE,
                                    new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "G", "D"),
                                    "*",
                                    new BinaryOperatorExpression(
                                        0,
                                        PrimitiveType.TYPE_DOUBLE,
                                        new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "mass", "D"),
                                        "/",
                                        new ParenthesesExpression(new BinaryOperatorExpression(
                                            0,
                                            PrimitiveType.TYPE_DOUBLE,
                                            new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "radius", "D"),
                                            "*",
                                            new FieldReferenceExpression(PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "radius", "D"),
                                            4
                                        )),
                                        4
                                    ),
                                    4
                                )
                            )
                        ),
                        new MethodDeclaration(
                            0,
                            "surfaceWeight",
                            PrimitiveType.TYPE_DOUBLE,
                            new FormalParameter(PrimitiveType.TYPE_DOUBLE, "otherMass"),
                            "(D)D",
                            new ReturnExpressionStatement(
                                new BinaryOperatorExpression(
                                    0,
                                    PrimitiveType.TYPE_DOUBLE,
                                    new LocalVariableReferenceExpression(PrimitiveType.TYPE_DOUBLE, "otherMass"),
                                    "*",
                                    new MethodInvocationExpression(
                                        PrimitiveType.TYPE_DOUBLE, thisExpression, "org/jd/core/v1/service/test/Planet", "surfaceGravity", "()D", null
                                    ),
                                    4
                                )
                            )
                        ),
                        new MethodDeclaration(
                            ACC_PUBLIC | ACC_STATIC,
                            "surfaceWeight",
                            PrimitiveType.TYPE_VOID,
                            new FormalParameter(arrayOfStringType, "args"),
                            "([Ljava/lan/String;)V",
                            new Statements(
                                new IfStatement(
                                    new BinaryOperatorExpression(
                                        0,
                                        PrimitiveType.TYPE_BOOLEAN,
                                        new LengthExpression(new LocalVariableReferenceExpression(arrayOfStringType, "args")),
                                        "!=",
                                        new IntegerConstantExpression(PrimitiveType.TYPE_INT, 1),
                                        9
                                    ),
                                    new Statements(
                                        new ExpressionStatement(new MethodInvocationExpression(
                                            PrimitiveType.TYPE_VOID,
                                            new FieldReferenceExpression(
                                                printStreamType,
                                                new ObjectTypeReferenceExpression(new ObjectType(StringConstants.JAVA_LANG_SYSTEM, "java.lang.System", "System")),
                                                StringConstants.JAVA_LANG_SYSTEM,
                                                "out",
                                                "Ljava/io/PrintStream;"
                                            ),
                                            "java/io/PrintStream",
                                            "println",
                                            "(Ljava/lang/String;)V",
                                            new StringConstantExpression("Usage: java Planet <earth_weight>"),
                                            null
                                        )),
                                        new ExpressionStatement(new MethodInvocationExpression(
                                            PrimitiveType.TYPE_VOID,
                                            new ObjectTypeReferenceExpression(new ObjectType(StringConstants.JAVA_LANG_SYSTEM, "java.lang.System", "System")),
                                            StringConstants.JAVA_LANG_SYSTEM,
                                            "exit",
                                            "(I)V",
                                            new IntegerConstantExpression(PrimitiveType.TYPE_INT, -1),
                                            null
                                        ))
                                    )
                                ),
                                new LocalVariableDeclarationStatement(
                                    PrimitiveType.TYPE_DOUBLE,
                                    new LocalVariableDeclarator(0, "earthWeight", new ExpressionVariableInitializer(
                                        new MethodInvocationExpression(
                                            PrimitiveType.TYPE_DOUBLE,
                                            new ObjectTypeReferenceExpression(new ObjectType(StringConstants.JAVA_LANG_DOUBLE, "java.lang.Double", "Double")),
                                            StringConstants.JAVA_LANG_DOUBLE,
                                            "parseDouble",
                                            "(Ljava/lang/String;)D",
                                            new ArrayExpression(
                                                new LocalVariableReferenceExpression(arrayOfStringType, "args"),
                                                new IntegerConstantExpression(PrimitiveType.TYPE_INT, 0)
                                            ),
                                            null
                                        )
                                    ))
                                ),
                                new LocalVariableDeclarationStatement(
                                    PrimitiveType.TYPE_DOUBLE,
                                    new LocalVariableDeclarator(0, "mass", new ExpressionVariableInitializer(
                                        new BinaryOperatorExpression(
                                            0,
                                            PrimitiveType.TYPE_DOUBLE,
                                            new LocalVariableReferenceExpression(PrimitiveType.TYPE_DOUBLE, "earthWeight"),
                                            "/",
                                            new MethodInvocationExpression(
                                                PrimitiveType.TYPE_DOUBLE,
                                                new FieldReferenceExpression(
                                                    planetType,
                                                    new ObjectTypeReferenceExpression(new ObjectType("org/jd/core/v1/service/test/Planet", "org.jd.core.v1.service.test.Planet", "Planet")),
                                                    "org/jd/core/v1/service/test/Planet",
                                                    "EARTH",
                                                    "org/jd/core/v1/service/test/Planet"),
                                                "org/jd/core/v1/service/test/Planet",
                                                "surfaceGravity",
                                                "()D",
                                                null
                                            ),
                                            4
                                        )
                                    ))
                                ),
                                new ForEachStatement(
                                    planetType,
                                    "p",
                                    new MethodInvocationExpression(
                                        planetType,
                                        new ObjectTypeReferenceExpression(new ObjectType("org/jd/core/v1/service/test/Planet", "org.jd.core.v1.service.test.Planet", "Planet")),
                                        "org/jd/core/v1/service/test/Planet",
                                        "values",
                                        "()[Lorg/jd/core/v1/service/test/Planet;",
                                        null),
                                    new ExpressionStatement(new MethodInvocationExpression(
                                        PrimitiveType.TYPE_VOID,
                                        new FieldReferenceExpression(
                                            printStreamType,
                                            new ObjectTypeReferenceExpression(new ObjectType(StringConstants.JAVA_LANG_SYSTEM, "java.lang.System", "System")),
                                            StringConstants.JAVA_LANG_SYSTEM,
                                            "out",
                                            "Ljava/io/PrintStream;"
                                        ),
                                        "java/io/PrintStream",
                                        "printf",
                                        "(Ljava/lang/String;[Ljava/lang/Object;)V",
                                        new Expressions(
                                            new StringConstantExpression("Your weight on %s is %f%n"),
                                            new LocalVariableReferenceExpression(planetType, "p"),
                                            new MethodInvocationExpression(
                                                PrimitiveType.TYPE_DOUBLE,
                                                new LocalVariableReferenceExpression(planetType, "p"),
                                                "org/jd/core/v1/service/test/Planet",
                                                "surfaceWeight",
                                                "(D)D",
                                                new LocalVariableReferenceExpression(PrimitiveType.TYPE_DOUBLE, "mass"),
                                                null
                                            )
                                        ),
                                        null
                                    ))
                                )
                            )
                        )
                    )
                )
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);

        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/Planet");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setMaxLineNumber(0);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);

        //tokenizer.process(message);
        tokens = new JavaFragmentToTokenProcessor().process(decompileContext.getBody());
        writer.process(decompileContext);

        String source = printer.toString();

        System.out.println("- - - - - - - - ");
        System.out.print(source);
        System.out.println("- - - - - - - - ");
    }

    @Test
    public void testSwitch() throws Exception {
        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/SwitchTest",
                "SwitchTest",
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/SwitchTest",
                    new MethodDeclaration(
                        Const.ACC_PUBLIC | Const.ACC_STATIC,
                        "translate",
                        PrimitiveType.TYPE_INT,
                        new FormalParameter(PrimitiveType.TYPE_INT, "i"),
                        "(I)Ljava/lang/String;",
                        new SwitchStatement(
                            new LocalVariableReferenceExpression(PrimitiveType.TYPE_INT, "i"),
                            Arrays.asList(
                                (SwitchStatement.Block)new SwitchStatement.LabelBlock(
                                    new SwitchStatement.ExpressionLabel(new IntegerConstantExpression(PrimitiveType.TYPE_INT, 0)),
                                    new Statements(
                                        new LocalVariableDeclarationStatement(
                                            ObjectType.TYPE_STRING,
                                            new LocalVariableDeclarator(0, "zero", new ExpressionVariableInitializer(new StringConstantExpression("zero")))
                                        ),
                                        new ReturnExpressionStatement(new LocalVariableReferenceExpression(ObjectType.TYPE_STRING, "zero"))
                                    )
                                ),
                                new SwitchStatement.MultiLabelsBlock(
                                    Arrays.asList(
                                        (SwitchStatement.Label)new SwitchStatement.ExpressionLabel(new IntegerConstantExpression(PrimitiveType.TYPE_INT, 1)),
                                        new SwitchStatement.ExpressionLabel(new IntegerConstantExpression(PrimitiveType.TYPE_INT, 2))
                                    ),
                                    new ReturnExpressionStatement(new StringConstantExpression("one or two"))
                                ),
                                new SwitchStatement.LabelBlock(
                                    SwitchStatement.DEFAULT_LABEL,
                                    new ReturnExpressionStatement(new StringConstantExpression("other"))
                                )
                            )
                        )
                    )
                )
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);

        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/SwitchTest");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setMaxLineNumber(0);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);

        writer.process(decompileContext);

        String source = printer.toString();

        System.out.println("- - - - - - - - ");
        System.out.print(source);
        System.out.println("- - - - - - - - ");

        Assert.assertNotEquals(-1, source.indexOf("switch (i)"));
    }

    @Test
    public void testBridgeAndSyntheticAttributes() throws Exception {
        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                Const.ACC_PUBLIC,
                "org/jd/core/v1/service/test/SyntheticAttributeTest",
                "SyntheticAttributeTest",
                new BodyDeclaration(
                    "org/jd/core/v1/service/test/SyntheticAttributeTest",
                    new MemberDeclarations(
                        new FieldDeclaration(
                            Const.ACC_PUBLIC|Const.ACC_BRIDGE,
                            PrimitiveType.TYPE_INT,
                            new FieldDeclarator("i")
                        ),
                        new MethodDeclaration(
                            Const.ACC_PUBLIC|Const.ACC_BRIDGE,
                            "testBridgeAttribute",
                            PrimitiveType.TYPE_VOID,
                            "()V"
                        ),
                        new MethodDeclaration(
                            Const.ACC_PUBLIC|Const.ACC_SYNTHETIC,
                            "testSyntheticAttribute",
                            PrimitiveType.TYPE_VOID,
                            "()V"
                        )
                    )
                )
            )
        );

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);

        decompileContext.setMainInternalTypeName("org/jd/core/v1/service/test/SyntheticAttributeTest");
        decompileContext.setLoader(new NopLoader());
        decompileContext.setPrinter(printer);
        decompileContext.setMaxLineNumber(0);
        decompileContext.setMajorVersion(0);
        decompileContext.setMinorVersion(0);

        fragmenter.process(compilationUnit, decompileContext);
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);

        writer.process(decompileContext);

        String source = printer.toString();

        System.out.println("- - - - - - - - ");
        System.out.print(source);
        System.out.println("- - - - - - - - ");

        Assert.assertEquals(-1, source.indexOf("/* bridge */"));
        Assert.assertEquals(-1, source.indexOf("/* synthetic */"));
    }
}
