/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.util;

import org.apache.commons.io.IOUtils;
import org.jd.core.v1.parser.JdJavaSourceParser;
import org.jd.core.v1.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class ParserRealignerTest implements DefaultTest {

    @Test
    public void testRealignCompact() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestCompact_input.txt", "/txt/TestCompact_output.txt");
    }

    @Test
    public void testRealignMediumCompact() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestMediumCompact_input.txt", "/txt/TestMediumCompact_output.txt");
    }

    @Test
    public void testRealignMediumExpanded() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestMediumExpanded_input.txt", "/txt/TestMediumExpanded_output.txt");
    }

    @Test
    public void testRealignFullyExpanded() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestFullyExpanded_input.txt", "/txt/TestFullyExpanded_output.txt");
    }

    @Test
    public void testRealignUltraCompact() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestUltraCompact_input.txt", "/txt/TestUltraCompact_output.txt");
    }

    @Test
    public void testRealignUltraCompactMini() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestUltraCompactMini_input.txt", "/txt/TestUltraCompactMini_output.txt");
    }

    @Test
    public void testRealignCompactFor() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestCompactFor_input.txt", "/txt/TestCompactFor_output.txt");
    }

    @Test
    public void testRealignCompactIfElse() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestCompactIfElse_input.txt", "/txt/TestCompactIfElse_output.txt");
    }

    @Test
    public void testParseAllJavaSyntaxes() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/AllJavaSyntaxes_input.txt", "/txt/AllJavaSyntaxes_output.txt");
    }

    @Test
    public void testParseUnusualComment() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/UnusualComment_input.txt", "/txt/UnusualComment_output.txt");
    }

    @Test
    public void testParseTryCatchFinally370() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TryCatchFinally370_input.txt", "/txt/TryCatchFinally370_output.txt");
    }

    @Test
    public void testParseModule() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/Module_input.txt", "/txt/Module_output.txt");
    }

    @Test
    public void testParseOuter() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/Outer_input.txt", "/txt/Outer_output.txt");
    }

    @Test
    public void testParseOpenModule() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/OpenModule_input.txt", "/txt/OpenModule_output.txt");
    }

    @Test
    public void testParsePackageInfo() throws Exception {
        assertNotNull(JdJavaSourceParser.parse("package a.b;"));
        assertNotNull(JdJavaSourceParser.parse("""
                @Deprecated
                package a.b;

                import java.util.List;
                """));
        assertNotNull(JdJavaSourceParser.parse("""
                @ParametersAreNonnullByDefault
                @ReturnValuesAreNonnullByDefault
                package a.b;
                """));
        assertNotNull(JdJavaSourceParser.parse("""
                @com.acme.annotations.Version(major = 1, minor = 2)
                package com.acme.foo.bar;
                """));
    }

    @Test
    public void testParseMultiTopLevelTypes() throws Exception {
        testParseRealign("/txt/MultiTopLevelTypes_input.txt", "/txt/MultiTopLevelTypes_output.txt");
    }

    @Test
    public void testParseTopLevelSemicolon() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TopLevelSemicolon_input.txt", "/txt/TopLevelSemicolon_output.txt");
    }

    @Test
    public void testParseMethodReferenceExpression() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/MethodReferenceExpression_input.txt", "/txt/MethodReferenceExpression_output.txt");
    }

    @Test
    public void testParseAnnotatedModifiers() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/AnnotatedModifiers_input.txt", "/txt/AnnotatedModifiers_output.txt");
    }

    @Test
    public void testParseEnumAnnotatedConstants() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/EnumAnnotatedConstants_input.txt", "/txt/EnumAnnotatedConstants_output.txt");
    }

    @Test
    public void testParseEnumKeywordConstants() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/EnumKeywordConstants_input.txt", "/txt/EnumKeywordConstants_output.txt");
    }

    @Test
    public void testParseFloatDoubleZeroLiteral() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/FloatDoubleZeroLiteral_input.txt", "/txt/FloatDoubleZeroLiteral_output.txt");
    }

    @Test
    public void testParseIntHexOverflow() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/IntHexOverflow_input.txt", "/txt/IntHexOverflow_output.txt");
    }

    @Test
    public void testParseRecordIdentifierCall() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/RecordIdentifierCall_input.txt", "/txt/RecordIdentifierCall_output.txt");
    }

    @Test
    public void testParseForInitWildcard() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/ForInitWildcard_input.txt", "/txt/ForInitWildcard_output.txt");
    }

    @Test
    public void testParseAnonymousClassArgumentComma() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/AnonymousClassArgumentComma_input.txt", "/txt/AnonymousClassArgumentComma_output.txt");
    }

    @Test
    public void testParseParenthesesExpressionInArgs() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/ParenthesesExpressionInArgs_input.txt", "/txt/ParenthesesExpressionInArgs_output.txt");
    }

    @Test
    public void testParseElseEmptyStatement() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/ElseEmptyStatement_input.txt", "/txt/ElseEmptyStatement_output.txt");
    }

    @Test
    public void testParseTryCatchEmptyBlock() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TryCatchEmptyBlock_input.txt", "/txt/TryCatchEmptyBlock_output.txt");
    }

    @Test
    public void testParseLargeNumericCommentIgnored() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/LargeNumericCommentIgnored_input.txt", "/txt/LargeNumericCommentIgnored_output.txt");
    }

    @Test
    public void testParseSwitchExpressionLabels() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/SwitchExpressionLabels_input.txt", "/txt/SwitchExpressionLabels_output.txt");
    }

    @Test
    public void testParseLineCommentEof() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/LineCommentEof_input.txt", "/txt/LineCommentEof_output.txt");
    }

    @Test
    public void testParseTryStatementEmptyBlocks() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TryStatementEmptyBlocks_input.txt", "/txt/TryStatementEmptyBlocks_output.txt");
    }

    @Test
    public void testParseTryResourcesAutoCloseable() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TryResourcesAutoCloseable_input.txt", "/txt/TryResourcesAutoCloseable_output.txt");
    }

    @Test
    public void testParseTryResourcesGeneric() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TryResourcesGeneric_input.txt", "/txt/TryResourcesGeneric_output.txt");
    }

    private void testParseRealign(String inputName, String outputName)
            throws IOException, URISyntaxException, ParseException {
        String input = toString(getClass().getResource(inputName));
        String expected = toString(getClass().getResource(outputName));
        ParserRealigner realigner = new ParserRealigner();
        String actual = realigner.realign(input);
        // re-parse output to validate
        JdJavaSourceParser.parse(actual);
        assertEqualsIgnoreEOL(expected, actual);
    }

    private String toString(URL resource) throws IOException, URISyntaxException {
        return IOUtils.toString(resource.toURI(), UTF_8);
    }
}
