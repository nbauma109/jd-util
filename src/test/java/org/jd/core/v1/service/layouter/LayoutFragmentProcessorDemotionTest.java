/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.layouter;

import org.jd.core.v1.model.fragment.Fragment;
import org.jd.core.v1.model.javafragment.FloatingLineNumberTokensFragment;
import org.jd.core.v1.model.javafragment.LineNumberTokensFragment;
import org.jd.core.v1.model.javafragment.TokensFragment;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.StartBlockToken;
import org.jd.core.v1.model.token.TextToken;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LayoutFragmentProcessorDemotionTest {

    private static LineNumberTokensFragment statement(int firstLineNumber, int lastLineNumber) {
        return new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(firstLineNumber), new TextToken("a"), new LineNumberToken(lastLineNumber), new TextToken("b"));
    }

    private static LineNumberTokensFragment statement(int lineNumber) {
        return statement(lineNumber, lineNumber);
    }

    @Test
    public void increasingFragmentsAreKept() {
        List<Fragment> fragments = new ArrayList<>(List.of(statement(10), statement(11), statement(11), statement(15, 16), statement(16)));
        List<Fragment> expected = new ArrayList<>(fragments);

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        assertEquals(expected, fragments);
        for (int i = 0; i < expected.size(); i++) {
            assertSame(expected.get(i), fragments.get(i));
        }
    }

    @Test
    public void spikeIsDemotedAndFollowingFragmentsAreKept() {
        // A copy of a 'finally' block inlined in the middle of a method carries the line of the 'finally' block
        List<Fragment> fragments = new ArrayList<>(List.of(statement(10), statement(11), statement(1201), statement(12), statement(13)));
        List<Fragment> original = new ArrayList<>(fragments);

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        assertSame(original.get(0), fragments.get(0));
        assertSame(original.get(1), fragments.get(1));
        assertTrue(fragments.get(2) instanceof TokensFragment);
        assertSame(original.get(3), fragments.get(3));
        assertSame(original.get(4), fragments.get(4));
    }

    @Test
    public void demotedFragmentKeepsItsTokensAndItsLineNumber() {
        List<Fragment> fragments = new ArrayList<>(List.of(statement(20), statement(5), statement(6), statement(21)));
        LineNumberTokensFragment original = (LineNumberTokensFragment) fragments.get(0);

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        // It floats between its neighbours, but its line number is still reported to the printer
        FloatingLineNumberTokensFragment demoted = (FloatingLineNumberTokensFragment) fragments.get(0);

        assertEquals(original.getTokens(), demoted.getTokens());
        assertEquals(new LineNumberToken(20), demoted.getTokens().get(1));
    }

    @Test
    public void demotedContinuationOfAStatementLosesItsLineNumber() {
        // e.g. '} + "3");' after the body of an anonymous class
        LineNumberTokensFragment continuation = new LineNumberTokensFragment(new LineNumberToken(5), new TextToken("}"));
        List<Fragment> fragments = new ArrayList<>(List.of(statement(20), continuation, statement(21)));

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        assertSame(TokensFragment.class, fragments.get(1).getClass());
        assertEquals(List.of(new TextToken("}")), ((TokensFragment) fragments.get(1)).getTokens());
    }

    @Test
    public void earlierFragmentWinsATie() {
        // 'for (...) { a = f(x); i++; }': the update 'i++' carries the line of the header, after the two lines of the body
        List<Fragment> fragments = new ArrayList<>(List.of(statement(188), statement(188), statement(189, 190), statement(188)));

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        assertTrue(fragments.get(0) instanceof LineNumberTokensFragment);
        assertTrue(fragments.get(1) instanceof LineNumberTokensFragment);
        assertTrue(fragments.get(2) instanceof LineNumberTokensFragment);
        assertTrue(fragments.get(3) instanceof FloatingLineNumberTokensFragment);
    }

    @Test
    public void overlappingFragmentIsDemoted() {
        // 10-14 overlaps 12: only one of them can be kept, the following fragment is kept anyway
        List<Fragment> fragments = new ArrayList<>(List.of(statement(10, 14), statement(12), statement(15)));

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        assertTrue(fragments.get(0) instanceof TokensFragment ^ fragments.get(1) instanceof TokensFragment);
        assertTrue(fragments.get(2) instanceof LineNumberTokensFragment);
    }

    @Test
    public void strayLowLineNumberIsTrimmedInsteadOfDemotingTheWholeFragment() {
        // 'if (a) { x(); if (b) ... }' merged into a single fragment spanning 1426-1428, next to the kept statement 1427:
        // the stray 1426 is dropped and the fragment stays anchored on 1428
        LineNumberTokensFragment merged = new LineNumberTokensFragment(
                StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(1426), new TextToken("a"), new LineNumberToken(1428), new TextToken("b"));
        List<Fragment> fragments = new ArrayList<>(List.of(statement(1425), statement(1427), merged, statement(1429)));

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        LineNumberTokensFragment trimmed = (LineNumberTokensFragment) fragments.get(2);

        assertEquals(1428, trimmed.getFirstLineNumber());
        assertEquals(1428, trimmed.getLastLineNumber());
        assertEquals(List.of(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(1428), new TextToken("a"), new TextToken("b")).size(), trimmed.getTokens().size());
    }

    private static LineNumberTokensFragment piece(int lineNumber) {
        return new LineNumberTokensFragment(new LineNumberToken(lineNumber), new TextToken("x"));
    }

    @Test
    public void statementStartOutweighsInterleavedPiecesOfSimilarCount() {
        // 'return new X().add(new String[] {"a"...}).addAttributes(...)': the pieces of the array initializers inherit
        // the line of the previous call and interleave with the pieces of the chain; the statement start must stay anchored
        LineNumberTokensFragment start = statement(135, 136);
        List<Fragment> fragments = new ArrayList<>(List.of(start, piece(135), piece(135), piece(135), piece(141), piece(136),
                piece(142), piece(141), piece(143), piece(142)));

        LayoutFragmentProcessor.demoteOutOfOrderFragments(fragments);

        assertSame(start, fragments.get(0));
    }
}
