/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.util;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javafragment.ImportsFragment;
import org.jd.core.v1.model.javasyntax.CompilationUnit;
import org.jd.core.v1.model.javasyntax.JavaImport;
import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.parser.JavaParseResult;
import org.jd.core.v1.parser.JdJavaSourceParser;
import org.jd.core.v1.parser.ParseException;
import org.jd.core.v1.printer.LineNumberStringBuilderPrinter;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util.JavaFragmentFactory;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor.CompilationUnitVisitor;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor.SearchImportsVisitor;
import org.jd.core.v1.service.layouter.LayoutFragmentProcessor;
import org.jd.core.v1.service.tokenizer.javafragmenttotoken.JavaFragmentToTokenProcessor;
import org.jd.core.v1.service.writer.WriteTokenProcessor;

import java.util.Collections;
import java.util.List;

public class ParserRealigner {

    private final LayoutFragmentProcessor layouter = new LayoutFragmentProcessor();
    private final JavaFragmentToTokenProcessor tokenizer = new JavaFragmentToTokenProcessor();
    private final WriteTokenProcessor writer = new WriteTokenProcessor();

    public String realign(String source, String internalTypeName) throws ParseException {
        JavaParseResult parseResult = JdJavaSourceParser.parse(source);
        List<JavaImport> imports = parseResult.imports();
        ImportsFragment importsFragment = JavaFragmentFactory.newImportsFragment();
        for (JavaImport javaImport : imports) {
            importsFragment.addImport(javaImport);
        }
        importsFragment.initLineCounts();
        CompilationUnit compilationUnit = new CompilationUnit(parseResult.typeDeclaration());
        DecompileContext decompileContext = new DecompileContext();
        LineNumberStringBuilderPrinter printer = new LineNumberStringBuilderPrinter();
        printer.setShowLineNumbers(true);
        printer.setRealignmentLineNumber(true);
        ClassPathLoader loader = new ClassPathLoader();
        MaxLineNumberVisitor maxLineNumberVisitor = new MaxLineNumberVisitor(loader, internalTypeName);
        maxLineNumberVisitor.visit(compilationUnit);
        decompileContext.setMaxLineNumber(maxLineNumberVisitor.getMaxLineNumber());
        decompileContext.setMainInternalTypeName(internalTypeName);
        decompileContext.setConfiguration(Collections.singletonMap("realignLineNumbers", Boolean.TRUE));
        decompileContext.setLoader(loader);
        decompileContext.setPrinter(printer);
        CompilationUnitVisitor visitor = new CompilationUnitVisitor(loader, internalTypeName, importsFragment);
        visitor.visit(compilationUnit);
        decompileContext.setBody(visitor.getFragments());
        layouter.process(decompileContext);
        DefaultList<Token> tokens = tokenizer.process(decompileContext.getBody());
        decompileContext.setTokens(tokens);
        writer.process(decompileContext);
        return printer.toString();
    }

    static class MaxLineNumberVisitor extends SearchImportsVisitor {
        public MaxLineNumberVisitor(Loader loader, String mainInternalName) {
            super(loader, mainInternalName);
        }
    }
}
