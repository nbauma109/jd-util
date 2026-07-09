/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1;

import org.apache.bcel.Const;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.javasyntax.CompilationUnit;
import org.jd.core.v1.model.javasyntax.declaration.BodyDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ClassDeclaration;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.printer.PlainTextMetaPrinter;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.JavaSyntaxToJavaFragmentProcessor;
import org.jd.core.v1.service.layouter.LayoutFragmentProcessor;
import org.jd.core.v1.service.tokenizer.javafragmenttotoken.JavaFragmentToTokenProcessor;
import org.jd.core.v1.service.writer.WriteTokenProcessor;
import org.jd.core.v1.util.DefaultList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Regression test for {@link org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor.SearchImportsVisitor}:
 * a referenced type whose simple name collides with a supertype (extends/implements) of a type
 * declared in this compilation unit must not be imported, since an unqualified reference would
 * otherwise resolve (per JLS 6.5.5) to that supertype/inherited member type instead of the
 * intended one.
 */
public class SearchImportsVisitorShadowingTest extends TestCase {

    private static final Loader NO_OP_LOADER = new Loader() {
        @Override
        public boolean canLoad(String internalName) {
            return false;
        }

        @Override
        public byte[] load(String internalName) {
            return null;
        }
    };

    private static String decompile(CompilationUnit compilationUnit, String mainInternalTypeName) {
        JavaSyntaxToJavaFragmentProcessor fragmenter = new JavaSyntaxToJavaFragmentProcessor();
        LayoutFragmentProcessor layouter = new LayoutFragmentProcessor();
        JavaFragmentToTokenProcessor tokenizer = new JavaFragmentToTokenProcessor();
        WriteTokenProcessor writer = new WriteTokenProcessor();

        PlainTextMetaPrinter printer = new PlainTextMetaPrinter();
        Map<String, Object> configuration = Collections.singletonMap("realignLineNumbers", Boolean.TRUE);
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setCompilationUnit(compilationUnit);
        decompileContext.setMainInternalTypeName(mainInternalTypeName);
        decompileContext.setLoader(NO_OP_LOADER);
        decompileContext.setPrinter(printer);
        decompileContext.setConfiguration(configuration);
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
        return source;
    }

    /**
     * The direct supertype itself is named "Builder" (extends Outer.Builder): an unrelated,
     * externally-imported "Builder" interface must still print fully-qualified.
     */
    @Test
    public void testShadowedBySupertypeOwnName() {
        ObjectType selfType = new ObjectType("test/shadow/MyBuilder", "test.shadow.MyBuilder", "MyBuilder");
        ObjectType outerBuilderType = new ObjectType("test/shadow/Outer$Builder", "test.shadow.Outer.Builder", "Builder", selfType);
        ObjectType targetType = new ObjectType("test/shadow/Target", "test.shadow.Target", "Target");
        ObjectType utilBuilderType = new ObjectType("test/shadow/util/Builder", "test.shadow.util.Builder", "Builder", targetType);

        CompilationUnit compilationUnit = new CompilationUnit(
            new ClassDeclaration(
                null,
                Const.ACC_PUBLIC,
                "test/shadow/MyBuilder",
                "MyBuilder",
                null,
                outerBuilderType,
                utilBuilderType,
                new BodyDeclaration("test/shadow/MyBuilder", null)
            )
        );

        String source = decompile(compilationUnit, "test/shadow/MyBuilder");

        // The referenced "util.Builder" must stay fully-qualified, since its simple name "Builder"
        // is shadowed by the class's own direct supertype ("Outer.Builder", also named "Builder"):
        // an unqualified reference here would incorrectly resolve to that supertype instead.
        Assert.assertTrue(source.contains("implements test.shadow.util.Builder"));
        Assert.assertFalse(source.contains("import test.shadow.util.Builder;"));
    }
}
