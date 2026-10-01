/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.layouter;

import org.jd.core.v1.model.fragment.FixedFragment;
import org.jd.core.v1.model.fragment.FlexibleFragment;
import org.jd.core.v1.model.fragment.Fragment;
import org.jd.core.v1.model.javafragment.FloatingLineNumberTokensFragment;
import org.jd.core.v1.model.javafragment.LineNumberTokensFragment;
import org.jd.core.v1.model.javafragment.SpacerBetweenMembersFragment;
import org.jd.core.v1.model.javafragment.StartBodyFragment;
import org.jd.core.v1.model.javafragment.TokensFragment;
import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.EndBlockToken;
import org.jd.core.v1.model.token.StartMarkerToken;
import org.jd.core.v1.model.token.EndMarkerToken;
import org.jd.core.v1.model.token.KeywordToken;
import org.jd.core.v1.model.token.NewLineToken;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.StartBlockToken;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.service.layouter.model.Section;
import org.jd.core.v1.service.layouter.util.VisitorsHolder;
import org.jd.core.v1.service.layouter.visitor.BuildSectionsVisitor;
import org.jd.core.v1.service.layouter.visitor.UpdateSpacerBetweenMovableBlocksVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.jd.core.v1.api.printer.Printer.UNKNOWN_LINE_NUMBER;

/**
 * Lays out a list of fragments by compacting, expanding, or moving them.
 *
 * <p><b>Input:</b> List of {@link Fragment}</p>
 * <p><b>Output:</b> List of {@link Fragment}</p>
 */
public class LayoutFragmentProcessor {
    /** Weight of a fragment which starts a statement, compared to 1 for a piece in the middle of a statement */
    private static final int ANCHOR_WEIGHT = 3;
    /** Label given by {@code JavaFragmentFactory.addStartSingleStatementMethodBody} */
    private static final String SINGLE_STATEMENT_METHOD_BODY = "Start single statement method body";


    public void process(DecompileContext decompileContext) {
        int maxLineNumber = decompileContext.getMaxLineNumber();
        boolean realignLineNumbers = decompileContext.isRealignLineNumbers();

        List<Fragment> fragments = decompileContext.getBody();

        if (maxLineNumber != UNKNOWN_LINE_NUMBER && realignLineNumbers) {
            demoteOutOfOrderFragments(fragments);

            BuildSectionsVisitor buildSectionsVisitor = new BuildSectionsVisitor();

            // Create sections
            for (Fragment fragment : fragments) {
                fragment.accept(buildSectionsVisitor);
            }

            List<Section> sections = buildSectionsVisitor.getSections();
            VisitorsHolder holder = new VisitorsHolder();
            UpdateSpacerBetweenMovableBlocksVisitor visitor = new UpdateSpacerBetweenMovableBlocksVisitor();

            // Try to release constraints twice for each section
            int sumOfRates = Integer.MAX_VALUE;
            int max = sections.size() * 2;

            if (max > 20) {
                max = 20;
            }

            for (int loop=0; loop<max; loop++) {
                // Update spacers
                visitor.reset();

                for (Section section : sections) {
                    for (FlexibleFragment fragment : section.getFlexibleFragments()) {
                        fragment.accept(visitor);
                    }
                    if (section.getFixedFragment() != null) {
                        section.getFixedFragment().accept(visitor);
                    }
                }

                // Layout sections
                for (int redo=0; redo<10; redo++) {
                    boolean changed = false;

                    for (Section section : sections) {
                        changed |= section.layout(false);
                    }
                    if (!changed) {
                        // Nothing changed -> Quit loop
                        break;
                    }
                }

                // Update the ratings
                int newSumOfRates = 0;
                Section mostConstrainedSection = sections.get(0);

                for (Section section : sections) {
                    section.updateRate();

                    if (mostConstrainedSection.getRate() < section.getRate()) {
                        mostConstrainedSection = section;
                    }

                    newSumOfRates += section.getRate();
                }

                //  Move fragments from the most constrained section
                if (mostConstrainedSection.getRate() == 0) {
                    // No more constrained section -> Quit loop
                    break;
                }

                if (sumOfRates <= newSumOfRates) {
                    // The sum of the constraints does not decrease -> Quit loop
                    break;
                }
                sumOfRates = newSumOfRates;

                if (! mostConstrainedSection.releaseConstraints(holder)) {
                    break;
                }
            }

            // Force layout
            for (Section section : sections) {
                section.layout(true);
            }

            if (decompileContext.getClassFile() != null) {
                // Decompiling a class file: the line of a method header is unknown (unlike when realigning a source file)
                separateHeadersFromSingleStatementBodies(sections);
            }

            breakBeforeTheFirstLineNumberOfUnreachableFragments(sections);

            // Update fragments
            fragments.clear();

            for (Section section : sections) {
                fragments.addAll(section.getFlexibleFragments());

                FixedFragment fixedFragment = section.getFixedFragment();

                if (fixedFragment != null) {
                    fragments.add(fixedFragment);
                }
            }
        }
    }

    /**
     * Fixed fragments (statements anchored on a source line) must appear in increasing line number order.
     * Some code is not (e.g. the copies of a 'finally' block inlined by the compiler in the middle of a method
     * carry the line of the 'finally' itself, a 'do ... while' condition carries the line of the 'do', or the
     * update of a 'for' loop carries the line of its header). Such an out-of-order fragment cannot be honoured
     * and, worse, makes the layout of every following section collapse.
     * <p>The fragments are ordered by the line they start on. Keep the heaviest chain of fragments (the start of a
     * statement is a much better anchor than a piece in the middle of a statement, e.g. the elements of an array
     * initializer, which only inherit a line, so it weighs more; between chains of the same weight, the one made of
     * the earliest fragments is kept) and turn the others into flexible ones, so that they simply follow their
     * neighbours.</p>
     * <p>Then, going through the fragments in order, the line numbers which are before the end of the previous
     * fragment (or after the start of the next kept one) are dropped: they are stray ones (e.g. inherited from another
     * statement), and the fragment stays aligned on the remaining ones. The earlier fragment wins.</p>
     */
    static void demoteOutOfOrderFragments(List<Fragment> fragments) {
        int[] indexes = IntStream.range(0, fragments.size())
                .filter(i -> fragments.get(i) instanceof LineNumberTokensFragment)
                .toArray();
        int size = indexes.length;

        if (size < 2) {
            return;
        }

        int[] first = new int[size];
        int[] weight = new int[size];
        boolean sorted = true;
        int previousLast = 0;

        for (int i = 0; i < size; i++) {
            LineNumberTokensFragment fragment = (LineNumberTokensFragment) fragments.get(indexes[i]);

            first[i] = fragment.getFirstLineNumber();
            weight[i] = isAnchor(fragment.getTokens()) ? ANCHOR_WEIGHT : 1;
            sorted &= first[i] >= previousLast;
            previousLast = fragment.getLastLineNumber();
        }

        if (!sorted) {
            trimFragments(fragments, indexes, selectHeaviestChain(first, weight));
        }
    }

    /**
     * @return for each fragment, true if it belongs to the heaviest chain of fragments whose first line numbers do
     *         not decrease (the fragments are ordered by the line they start on)
     */
    private static boolean[] selectHeaviestChain(int[] first, int[] weight) {
        int size = first.length;

        // Fenwick tree over the (compressed) first line numbers, storing the heaviest chain ending at or before a line number
        int[] sortedFirst = Arrays.stream(first).distinct().sorted().toArray();
        int[] bestLength = new int[sortedFirst.length + 1];
        int[] bestIndex = new int[sortedFirst.length + 1];
        int[] length = new int[size];
        int[] previous = new int[size];

        Arrays.fill(bestIndex, -1);

        for (int i = 0; i < size; i++) {
            int best = 0;
            int bestPrevious = -1;

            for (int k = upperBound(sortedFirst, first[i]); k > 0; k -= k & -k) {
                if (bestLength[k] > best) {
                    best = bestLength[k];
                    bestPrevious = bestIndex[k];
                }
            }

            previous[i] = bestPrevious;
            length[i] = best + weight[i];

            for (int k = Arrays.binarySearch(sortedFirst, first[i]) + 1; k <= sortedFirst.length; k += k & -k) {
                if (length[i] > bestLength[k]) {
                    bestLength[k] = length[i];
                    bestIndex[k] = i;
                }
            }
        }

        int end = 0;

        for (int i = 1; i < size; i++) {
            if (length[i] > length[end]) {
                end = i;
            }
        }

        boolean[] kept = new boolean[size];

        for (int i = end; i >= 0; i = previous[i]) {
            kept[i] = true;
        }
        return kept;
    }

    /**
     * Going through the fragments in order, drop the line numbers which are before the end of the previous fragment
     * (or after the start of the next kept one) and replace the fragments which lost all of them.
     */
    private static void trimFragments(List<Fragment> fragments, int[] indexes, boolean[] kept) {
        int size = indexes.length;
        int[] nextKeptFirst = new int[size];
        int nextFirst = Integer.MAX_VALUE;

        for (int i = size - 1; i >= 0; i--) {
            nextKeptFirst[i] = nextFirst;
            if (kept[i]) {
                nextFirst = ((LineNumberTokensFragment) fragments.get(indexes[i])).getFirstLineNumber();
            }
        }

        int previousLast = 0;

        for (int i = 0; i < size; i++) {
            List<Token> tokens = ((LineNumberTokensFragment) fragments.get(indexes[i])).getTokens();
            List<Token> trimmed = trimLineNumbers(tokens, previousLast, kept[i] ? Integer.MAX_VALUE : nextKeptFirst[i]);

            if (!hasLineNumber(trimmed)) {
                fragments.set(indexes[i], withoutKnownLineNumbers(tokens));
                continue;
            }
            if (trimmed.size() != tokens.size()) {
                fragments.set(indexes[i], new LineNumberTokensFragment(trimmed));
            }
            previousLast = lastLineNumber(trimmed, previousLast);
        }
    }

    /** @return the tokens without the line numbers which are lower than {@code lower}, or than the previous kept one, or greater than {@code upper} */
    private static List<Token> trimLineNumbers(List<Token> tokens, int lower, int upper) {
        List<Token> trimmed = new ArrayList<>(tokens.size());
        int last = lower;

        for (Token token : tokens) {
            if (!(token instanceof LineNumberToken lineNumberToken)) {
                trimmed.add(token);
            } else if (lineNumberToken.lineNumber() >= last && lineNumberToken.lineNumber() <= upper) {
                last = lineNumberToken.lineNumber();
                trimmed.add(token);
            }
        }
        return trimmed;
    }

    private static int lastLineNumber(List<Token> tokens, int defaultLineNumber) {
        int last = defaultLineNumber;

        for (Token token : tokens) {
            if (token instanceof LineNumberToken lineNumberToken) {
                last = lineNumberToken.lineNumber();
            }
        }
        return last;
    }

    private static boolean hasLineNumber(List<Token> tokens) {
        return tokens.stream().anyMatch(LineNumberToken.class::isInstance);
    }

    private static Fragment withoutKnownLineNumbers(List<Token> tokens) {
        if (isAnchor(tokens)) {
            // A whole statement: keep its line number to report it to the printer
            return new FloatingLineNumberTokensFragment(tokens);
        }
        // The continuation of a statement (e.g. after the body of an anonymous class): a line number alone would be
        // misleading
        return new TokensFragment(tokens.stream().filter(token -> !(token instanceof LineNumberToken)).toList());
    }

    /**
     * A single statement method is printed on one line ('public T m() { return x; }') when the lines are short, at
     * the expense of the header: the line of the body is right, but the header, which is one line above in the source,
     * is not. When a blank line between the members can pay for it (the members remain separated by at least a
     * line break, so a source with one-liner methods keeps them), print the header on its own line instead.
     */
    private static void separateHeadersFromSingleStatementBodies(List<Section> sections) {
        for (Section section : sections) {
            StartBodyFragment startBody = null;
            SpacerBetweenMembersFragment spacer = null;

            for (FlexibleFragment fragment : section.getFlexibleFragments()) {
                if (fragment instanceof SpacerBetweenMembersFragment candidate && startBody == null) {
                    spacer = candidate;
                } else if (fragment instanceof StartBodyFragment candidate && SINGLE_STATEMENT_METHOD_BODY.equals(candidate.getLabel())) {
                    startBody = candidate;
                }
            }

            if (startBody != null && spacer != null && startBody.getLineCount() == 0 && spacer.getLineCount() >= 2
             && startBody.getLineCount() < startBody.getMaximalLineCount() && spacer.decLineCount(true)) {
                startBody.incLineCount(true);
            }
        }
    }

    /**
     * The flexible fragments of a section cannot always take as many lines as the ones which separate the fixed
     * fragments (e.g. no fragment offers a line break between the ':' of a ternary and the expression which follows the
     * body of an anonymous class). The fixed fragment then starts with tokens of the previous line: break the line
     * between them and its first line number, so that the line numbers stay aligned.
     */
    private static void breakBeforeTheFirstLineNumberOfUnreachableFragments(List<Section> sections) {
        for (Section section : sections) {
            int missingLineCount = section.getTargetLineCount() - section.getLineCount();

            if (missingLineCount > 0 && section.getFixedFragment() instanceof LineNumberTokensFragment fragment) {
                List<Token> tokens = fragment.getTokens();
                int index = indexOfFirstLineNumber(tokens);

                if (index > 0 && hasText(tokens.subList(0, index))) {
                    List<Token> broken = new ArrayList<>(tokens.size() + missingLineCount);

                    broken.addAll(tokens.subList(0, index));
                    for (int i = 0; i < missingLineCount; i++) {
                        broken.add(NewLineToken.NEWLINE_1);
                    }
                    broken.addAll(tokens.subList(index, tokens.size()));
                    section.setFixedFragment(new LineNumberTokensFragment(broken));
                }
            }
        }
    }

    private static int indexOfFirstLineNumber(List<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i) instanceof LineNumberToken) {
                return i;
            }
        }
        return -1;
    }

    /** @return true if the tokens print something on the line (anything but the markers of blocks, the line numbers and the line breaks) */
    private static boolean hasText(List<Token> tokens) {
        return tokens.stream().anyMatch(token -> !(isSilentBlockToken(token) || token instanceof StartMarkerToken || token instanceof EndMarkerToken
                || token instanceof LineNumberToken || token instanceof NewLineToken));
    }

    /** @return true for the block tokens which print nothing (the parameters, array and resources ones print a delimiter) */
    private static boolean isSilentBlockToken(Token token) {
        return token instanceof StartBlockToken start && start.text().isEmpty()
                || token instanceof EndBlockToken end && end.text().isEmpty();
    }

    /** @return true if the fragment starts a statement ('return ...;', 'x = ...;', 'if (...)', 'while (...)'...) */
    private static boolean isAnchor(List<Token> tokens) {
        for (Token token : tokens) {
            if (token instanceof LineNumberToken) {
                continue;
            }
            return token == StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK || token instanceof KeywordToken;
        }
        return false;
    }

    /** @return the number of elements of the sorted array which are lower than or equal to the value */
    private static int upperBound(int[] sortedValues, int value) {
        int low = 0;
        int high = sortedValues.length;

        while (low < high) {
            int middle = (low + high) >>> 1;

            if (sortedValues[middle] <= value) {
                low = middle + 1;
            } else {
                high = middle;
            }
        }
        return low;
    }
}
