/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.layouter;

import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.model.fragment.Fragment;
import org.jd.core.v1.model.javafragment.LineNumberTokensFragment;
import org.jd.core.v1.model.javafragment.SpacerBetweenMembersFragment;
import org.jd.core.v1.model.javafragment.StartBodyFragment;
import org.jd.core.v1.model.javafragment.TokensFragment;
import org.jd.core.v1.model.message.DecompileContext;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.StartBlockToken;
import org.jd.core.v1.model.token.TextToken;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util.JavaFragmentFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class LayoutFragmentProcessorClassFileTest {

    /** Lines of the spacer and of the start of the body after the layout of 'member; spacer; header { statement }' */
    private static int[] layout(boolean fromClassFile, boolean singleStatement, int distance, int spacerMinimum) {
        List<Fragment> body = new ArrayList<>();

        body.add(new TokensFragment(new TextToken("class")));
        body.add(new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(1), new TextToken("a")));
        // The members of a one-liner source are separated by a line at least
        SpacerBetweenMembersFragment spacer = new SpacerBetweenMembersFragment(spacerMinimum, 3, Integer.MAX_VALUE, 7, "Spacer between members");
        body.add(spacer);
        StartBodyFragment startBody = singleStatement
                ? JavaFragmentFactory.addStartSingleStatementMethodBody(body)
                : JavaFragmentFactory.addStartMethodBody(body);
        body.add(new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(1 + distance), new TextToken("b")));
        if (singleStatement) {
            JavaFragmentFactory.addEndSingleStatementMethodBody(body, startBody);
        } else {
            JavaFragmentFactory.addEndMethodBody(body, startBody);
        }
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setConfiguration(Map.of("realignLineNumbers", "true"));
        decompileContext.setMaxLineNumber(1 + distance);
        decompileContext.setBody(body);
        if (fromClassFile) {
            decompileContext.setClassFile(new ClassFile(null));
        }

        new LayoutFragmentProcessor().process(decompileContext);

        return new int[] { spacer.getLineCount(), startBody.getLineCount() };
    }

    @Test
    public void headerOfASingleStatementMethodIsSeparatedWhenDecompilingAClassFile() {
        // A blank line pays for the line break between the header and the body
        assertArrayEquals(new int[] { 2, 0 }, layout(false, true, 2, 1));
        assertArrayEquals(new int[] { 1, 1 }, layout(true, true, 2, 1));
    }

    @Test
    public void headerIsNotSeparatedWhenNoBlankLineCanPayForIt() {
        // The spacer is already at its minimum: the physical lines of the following fragments must not move
        assertArrayEquals(new int[] { 2, 0 }, layout(false, true, 2, 2));
        assertArrayEquals(new int[] { 2, 0 }, layout(true, true, 2, 2));
    }

    @Test
    public void otherBodiesAreLeftAlone() {
        assertArrayEquals(layout(false, false, 2, 1), layout(true, false, 2, 1));
    }
}
