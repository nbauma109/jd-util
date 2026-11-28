/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.tokenizer.javafragmenttotoken.visitor;

import org.jd.core.v1.model.javafragment.EndBlockFragment;
import org.jd.core.v1.model.javafragment.EndBlockInParameterFragment;
import org.jd.core.v1.model.javafragment.EndBodyInParameterFragment;
import org.jd.core.v1.model.javafragment.EndStatementsBlockFragment;
import org.jd.core.v1.model.javafragment.JavaFragment;
import org.jd.core.v1.model.javafragment.SpaceSpacerFragment;
import org.jd.core.v1.model.javafragment.SpacerBetweenMembersFragment;
import org.jd.core.v1.model.javafragment.StartBlockFragment;
import org.jd.core.v1.model.javafragment.StartBodyFragment;
import org.jd.core.v1.model.javafragment.StartStatementsBlockFragment;
import org.jd.core.v1.model.javafragment.StartStatementsDoWhileBlockFragment;
import org.jd.core.v1.model.token.EndBlockToken;
import org.jd.core.v1.model.token.KeywordToken;
import org.jd.core.v1.model.token.NewLineToken;
import org.jd.core.v1.model.token.StartBlockToken;
import org.jd.core.v1.model.token.TextToken;
import org.jd.core.v1.model.token.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TokenizeJavaFragmentVisitorTest {
    private static final Token SPACE = TextToken.SPACE;
    private static final Token COMMA = TextToken.COMMA;
    private static final Token START = StartBlockToken.START_BLOCK;
    private static final Token END = EndBlockToken.END_BLOCK;
    private static final Token DO = new KeywordToken("do");
    private static final Token ONE = NewLineToken.NEWLINE_1;
    private static final Token TWO = NewLineToken.NEWLINE_2;
    private static final Token THREE = new NewLineToken(3);

    @Test
    public void testArrayBlocksAndSpacers() {
        List<Token> spaces = List.of(SPACE, ONE, TWO, THREE);
        for (int lines = 0; lines < spaces.size(); lines++) {
            Token spacing = spaces.get(lines);
            StartBlockFragment start = new StartBlockFragment(0, lines, 3, 1, "Array start");
            assertTokens(start, START, spacing);
            assertTokens(new EndBlockFragment(0, lines, 3, 1, "Array end", start), spacing, END);
            assertTokens(new EndBlockInParameterFragment(0, lines, 3, 1, "Parameter end", start), spacing, END, COMMA);
            assertTokens(new SpaceSpacerFragment(0, lines, 3, 1, "Space"), spacing);
            assertTokens(new SpacerBetweenMembersFragment(0, lines, 3, 1, "Member space"), spacing);
        }
    }

    @Test
    public void testBodySpacing() {
        assertBody(0, List.of(SPACE, START, SPACE), List.of(SPACE, END, COMMA));
        assertBody(1, List.of(SPACE, START, ONE), List.of(ONE, END, COMMA, SPACE));
        assertBody(2, List.of(ONE, START, ONE), List.of(ONE, END, COMMA, ONE));
        assertBody(3, List.of(ONE, START, TWO), List.of(TWO, END, COMMA, ONE));
        assertBody(4, List.of(ONE, START, THREE), List.of(THREE, END, COMMA, ONE));
    }

    @Test
    public void testStatementBlockSpacing() {
        assertBlock(0, 0, List.of(SPACE, START, SPACE), List.of(SPACE, END, SPACE), List.of(DO, SPACE, START, SPACE));
        assertBlock(1, 0, List.of(ONE, START, SPACE), List.of(SPACE, END, ONE), List.of(ONE, DO, SPACE, START, SPACE));
        assertBlock(1, 1, List.of(SPACE, START, ONE), List.of(ONE, END, SPACE), List.of(DO, SPACE, START, ONE));
        assertBlock(2, 0, List.of(TWO, START, SPACE), List.of(SPACE, END, TWO), List.of(TWO, DO, SPACE, START, SPACE));
        assertBlock(2, 1, List.of(SPACE, START, TWO), List.of(ONE, END, ONE), List.of(ONE, DO, SPACE, START, ONE));
        assertBlock(2, 2, List.of(ONE, START, ONE), List.of(ONE, END, ONE), List.of(DO, ONE, START, ONE));
        assertBlock(3, 0, List.of(THREE, START, SPACE), List.of(SPACE, END, THREE), List.of(THREE, DO, SPACE, START, SPACE));
        assertBlock(3, 1, List.of(SPACE, START, THREE), List.of(THREE, END), List.of(DO, SPACE, START, THREE));
        assertBlock(3, 2, List.of(ONE, START, TWO), List.of(TWO, END, ONE), List.of(DO, ONE, START, TWO));
        assertBlock(3, 3, List.of(ONE, START, TWO), List.of(TWO, END, ONE), List.of(DO, ONE, START, TWO));
    }

    private static void assertBody(int lines, List<Token> expectedStart, List<Token> expectedEnd) {
        StartBodyFragment start = new StartBodyFragment(0, lines, 4, 1, "Body start");
        assertTokens(start, expectedStart.toArray(Token[]::new));
        assertTokens(new EndBodyInParameterFragment(0, lines, 4, 1, "Body end", start),
                expectedEnd.toArray(Token[]::new));
    }

    private static void assertBlock(int lines, int minimum, List<Token> expectedStart,
                                    List<Token> expectedEnd, List<Token> expectedDoStart) {
        StartStatementsBlockFragment start = new StartStatementsBlockFragment(0, lines, 3, 1, "Block start");
        new EndStatementsBlockFragment(0, minimum, 3, 1, "Group end", start.getGroup());
        assertTokens(start, expectedStart.toArray(Token[]::new));

        StartStatementsBlockFragment groupStart = new StartStatementsBlockFragment(0, minimum, 3, 1, "Group start");
        EndStatementsBlockFragment end = new EndStatementsBlockFragment(0, lines, 3, 1, "Block end", groupStart.getGroup());
        assertTokens(end, expectedEnd.toArray(Token[]::new));

        StartStatementsDoWhileBlockFragment doStart = new StartStatementsDoWhileBlockFragment(0, lines, 3, 1, "Do start");
        new EndStatementsBlockFragment(0, minimum, 3, 1, "Do end", doStart.getGroup());
        assertTokens(doStart, expectedDoStart.toArray(Token[]::new));
    }

    private static void assertTokens(JavaFragment fragment, Token... expected) {
        TokenizeJavaFragmentVisitor visitor = new TokenizeJavaFragmentVisitor(expected.length);
        fragment.accept(visitor);
        assertEquals(List.of(expected), visitor.getTokens());
    }
}
