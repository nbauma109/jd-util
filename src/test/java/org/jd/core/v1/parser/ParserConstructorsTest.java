/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertNotNull;

/** The generated parser and the line number comment scanner can be built, and reused, from a stream or a reader. */
public class ParserConstructorsTest {

    private static final String SOURCE = "class T {}";

    private static ByteArrayInputStream stream() {
        return new ByteArrayInputStream(SOURCE.getBytes(UTF_8));
    }

    @Test
    public void parserFromStreamAndReader() {
        JdJavaSourceParser fromStream = new JdJavaSourceParser(stream(), UTF_8);
        fromStream.ReInit(stream(), UTF_8);
        fromStream.ReInit(new StringReader(SOURCE));

        JdJavaSourceParser fromReader = new JdJavaSourceParser(new StringReader(SOURCE));
        fromReader.ReInit(new StringReader(SOURCE));

        assertNotNull(fromStream);
        assertNotNull(fromReader);
    }

    @Test
    public void parserFromTokenManager() {
        JdJavaSourceParserTokenManager tokenManager = new JdJavaSourceParserTokenManager(new JavaCharStream(new StringReader(SOURCE)));
        JdJavaSourceParser parser = new JdJavaSourceParser(tokenManager);

        parser.ReInit(tokenManager);

        assertNotNull(parser);
    }

    @Test
    public void scannerFromStreamAndReader() {
        JavaLineNumberCommentScanner fromStream = new JavaLineNumberCommentScanner(stream(), UTF_8);
        fromStream.ReInit(stream(), UTF_8);
        fromStream.ReInit(new StringReader(SOURCE));

        JavaLineNumberCommentScanner fromReader = new JavaLineNumberCommentScanner(new StringReader(SOURCE));
        fromReader.ReInit(new StringReader(SOURCE));

        assertNotNull(fromStream);
        assertNotNull(fromReader);
    }

    @Test
    public void scannerFromTokenManager() {
        JavaLineNumberCommentScannerTokenManager tokenManager = new JavaLineNumberCommentScannerTokenManager(new JavaCharStream(new StringReader(SOURCE)));
        JavaLineNumberCommentScanner scanner = new JavaLineNumberCommentScanner(tokenManager);

        scanner.ReInit(tokenManager);

        assertNotNull(scanner);
    }
}
