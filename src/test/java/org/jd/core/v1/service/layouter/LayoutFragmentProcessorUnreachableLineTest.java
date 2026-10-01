/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.layouter;

import org.jd.core.v1.model.fragment.Fragment;
import org.jd.core.v1.model.token.StringConstantToken;
import org.jd.core.v1.model.token.StartMarkerToken;
import org.jd.core.v1.model.token.ReferenceToken;
import org.jd.core.v1.model.token.NumericConstantToken;
import org.jd.core.v1.model.token.KeywordToken;
import org.jd.core.v1.model.token.EndMarkerToken;
import org.jd.core.v1.model.token.EndBlockToken;
import org.jd.core.v1.model.token.DeclarationToken;
import org.jd.core.v1.model.token.CharacterConstantToken;
import org.jd.core.v1.model.token.BooleanConstantToken;
import org.jd.core.v1.api.printer.Printer;
import org.jd.core.v1.model.javafragment.LineNumberTokensFragment;
import org.jd.core.v1.model.javafragment.SpacerFragment;
import org.jd.core.v1.model.javafragment.TokensFragment;
import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.NewLineToken;
import org.jd.core.v1.model.token.StartBlockToken;
import org.jd.core.v1.model.token.TextToken;
import org.jd.core.v1.model.token.Token;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/** The flexible fragments between two fixed fragments cannot always take as many lines as the line numbers need. */
public class LayoutFragmentProcessorUnreachableLineTest {

    private static LineNumberTokensFragment statement(int lineNumber) {
        return new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(lineNumber), new TextToken("a"));
    }

    private static List<Fragment> layout(LineNumberTokensFragment second) {
        List<Fragment> body = new ArrayList<>();

        body.add(new TokensFragment(new TextToken("class")));
        body.add(statement(10));
        // At most two line breaks between the two statements, which are three lines apart
        body.add(new SpacerFragment(0, 1, 1, 1, "first"));
        body.add(new SpacerFragment(0, 1, 1, 2, "second"));
        body.add(second);
        body.add(new TokensFragment(new TextToken("end")));

        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setConfiguration(Map.of("realignLineNumbers", "true"));
        decompileContext.setMaxLineNumber(13);
        decompileContext.setBody(body);

        new LayoutFragmentProcessor().process(decompileContext);

        return decompileContext.getBody();
    }

    private static LineNumberTokensFragment fixedFragmentAfter(List<Fragment> body, int lineNumber) {
        return (LineNumberTokensFragment) body.stream()
                .filter(fragment -> fragment instanceof LineNumberTokensFragment lineNumberFragment && lineNumberFragment.getLastLineNumber() == lineNumber)
                .findFirst()
                .orElseThrow();
    }

    @Test
    public void textBeforeTheFirstLineNumberIsBrokenFromIt() {
        // e.g. '} : ' of a ternary, then 'new X()' on the next line
        LineNumberTokensFragment second = new LineNumberTokensFragment(new TextToken(":"), new LineNumberToken(13), new TextToken("new"));

        List<Token> tokens = fixedFragmentAfter(layout(second), 13).getTokens();

        assertEquals(List.of(new TextToken(":"), NewLineToken.NEWLINE_1, new LineNumberToken(13), new TextToken("new")), tokens);
    }

    @Test
    public void fragmentWhichStartsWithItsLineNumberIsLeftAlone() {
        LineNumberTokensFragment second = statement(13);

        assertSame(second, fixedFragmentAfter(layout(second), 13));
    }

    @Test
    public void fragmentWhichStartsWithMarkersOnlyIsLeftAlone() {
        LineNumberTokensFragment second = new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(13), new TextToken("a"));

        assertSame(second, fixedFragmentAfter(layout(second), 13));
    }

    @Test
    public void everyPrintedTokenBeforeTheFirstLineNumberIsBrokenFromIt() {
        List<Token> printed = List.of(
                new KeywordToken("new"),
                new DeclarationToken(Printer.FIELD, "test/T", "f", "I"),
                new ReferenceToken(Printer.TYPE, "java/lang/String", "String"),
                new StringConstantToken("\"a\"", "test/T"),
                new NumericConstantToken("1"),
                new BooleanConstantToken(true),
                new CharacterConstantToken("'a'", "test/T"));

        for (Token token : printed) {
            LineNumberTokensFragment second = new LineNumberTokensFragment(token, new LineNumberToken(13), new TextToken("new"));

            assertEquals(token.toString(), List.of(token, NewLineToken.NEWLINE_1, new LineNumberToken(13), new TextToken("new")),
                    fixedFragmentAfter(layout(second), 13).getTokens());
        }
    }

    @Test
    public void structuralTokensBeforeTheFirstLineNumberAreNotText() {
        for (Token token : List.<Token>of(EndBlockToken.END_DECLARATION_OR_STATEMENT_BLOCK, StartMarkerToken.IMPORT_STATEMENTS, EndMarkerToken.IMPORT_STATEMENTS, NewLineToken.NEWLINE_1)) {
            LineNumberTokensFragment second = new LineNumberTokensFragment(token, new LineNumberToken(13), new TextToken("new"));

            assertSame(token.toString(), second, fixedFragmentAfter(layout(second), 13));
        }
    }
}
