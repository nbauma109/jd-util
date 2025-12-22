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

public class ParserRealignerTest {

    @Test
    public void testRealignCompact() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestCompact_input.txt", "/txt/TestCompact_output.txt", "test/TestCompact");
    }

    @Test
    public void testRealignMediumCompact() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestMediumCompact_input.txt", "/txt/TestMediumCompact_output.txt", "test/TestCompact");
    }
    
    @Test
    public void testRealignMediumExpanded() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestMediumExpanded_input.txt", "/txt/TestMediumExpanded_output.txt", "test/TestCompact");
    }
    
    @Test
    public void testRealignFullyExpanded() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestFullyExpanded_input.txt", "/txt/TestFullyExpanded_output.txt", "test/TestCompact");
    }

    @Test
    public void testRealignUltraCompact() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/TestUltraCompact_input.txt", "/txt/TestUltraCompact_output.txt", "test/TestCompact");
    }
    
    @Test
    public void testParseAllJavaSyntaxes() throws IOException, URISyntaxException, ParseException {
        testParseRealign("/txt/AllJavaSyntaxes_input.txt", "/txt/AllJavaSyntaxes_output.txt", "demo/AllJavaSyntaxes");
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
    
    private void testParseRealign(String inputName, String outputName, String internalTypeName)
            throws IOException, URISyntaxException, ParseException {
        String input = toString(getClass().getResource(inputName));
        String expected = toString(getClass().getResource(outputName));
        ParserRealigner realigner = new ParserRealigner();
        String actual = realigner.realign(input, internalTypeName);
        // re-parse output to validate
        JdJavaSourceParser.parse(actual);
        assertEquals(expected, actual);
    }

    private String toString(URL resource) throws IOException, URISyntaxException {
        return IOUtils.toString(resource.toURI(), UTF_8);
    }
}
