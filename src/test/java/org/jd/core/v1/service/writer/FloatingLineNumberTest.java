/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.writer;

import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.NewLineToken;
import org.jd.core.v1.model.token.TextToken;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.printer.LineNumberStringBuilderPrinter;
import org.jd.core.v1.util.DefaultList;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class FloatingLineNumberTest {

    private static String write(Token... tokens) {
        LineNumberStringBuilderPrinter printer = new LineNumberStringBuilderPrinter();
        printer.setShowLineNumbers(true);
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setPrinter(printer);
        decompileContext.setMaxLineNumber(99);
        decompileContext.setTokens(new DefaultList<>(Arrays.asList(tokens)));
        new WriteTokenProcessor().process(decompileContext);
        return printer.toString();
    }

    @Test
    public void floatingLineNumberIsUsedWhenTheLineHasNoOther() {
        // A statement which could not be aligned keeps its line number, which is only used as a last resort
        String source = write(new TextToken("a"), NewLineToken.NEWLINE_1, new LineNumberToken(7, true), new TextToken("b"));

        assertTrue(source, source.split("\n")[1].matches("/\\*\\s+7 \\*/.*"));
    }

    @Test
    public void alignedLineNumberWinsOverFloatingOne() {
        String source = write(new TextToken("a"), NewLineToken.NEWLINE_1, new LineNumberToken(7, true), new LineNumberToken(9), new TextToken("b"));

        assertTrue(source, source.split("\n")[1].matches("/\\*\\s+9 \\*/.*"));
    }

    @Test
    public void floatingLineNumberOfTheFollowingTokensIsUsedToo() {
        String source = write(new TextToken("a"), NewLineToken.NEWLINE_1, new TextToken("b"), new LineNumberToken(7, true));

        assertTrue(source, source.split("\n")[1].matches("/\\*\\s+7 \\*/.*"));
    }
}
