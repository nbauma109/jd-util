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
import static org.junit.Assert.assertEquals;

public class LayoutFragmentProcessorClassFileTest {

    /** Lines of the spacer and of the start of the body after the layout of 'member; spacer; header { statement }' */
    private static int[] layout(boolean fromClassFile, boolean singleStatement) {
        List<Fragment> body = new ArrayList<>();

        body.add(new TokensFragment(new TextToken("class")));
        body.add(new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(1), new TextToken("a")));
        // The members of a one-liner source are separated by a line at least
        SpacerBetweenMembersFragment spacer = new SpacerBetweenMembersFragment(2, 2, Integer.MAX_VALUE, 7, "Spacer between members");
        body.add(spacer);
        StartBodyFragment startBody = singleStatement
                ? JavaFragmentFactory.addStartSingleStatementMethodBody(body)
                : JavaFragmentFactory.addStartMethodBody(body);
        body.add(new LineNumberTokensFragment(StartBlockToken.START_DECLARATION_OR_STATEMENT_BLOCK, new LineNumberToken(3), new TextToken("b")));
        if (singleStatement) {
            JavaFragmentFactory.addEndSingleStatementMethodBody(body, startBody);
        } else {
            JavaFragmentFactory.addEndMethodBody(body, startBody);
        }
        DecompileContext decompileContext = new DecompileContext();
        decompileContext.setConfiguration(Map.of("realignLineNumbers", "true"));
        decompileContext.setMaxLineNumber(3);
        decompileContext.setBody(body);
        if (fromClassFile) {
            decompileContext.setClassFile(new ClassFile(null));
        }

        new LayoutFragmentProcessor().process(decompileContext);

        return new int[] { spacer.getLineCount(), startBody.getLineCount() };
    }

    @Test
    public void headerOfASingleStatementMethodIsSeparatedWhenDecompilingAClassFile() {
        int[] fromSource = layout(false, true);
        int[] fromClassFile = layout(true, true);

        // A blank line pays for the line break between the header and the body
        assertEquals(0, fromSource[1]);
        assertEquals(1, fromClassFile[1]);
    }

    @Test
    public void otherBodiesAreLeftAlone() {
        assertArrayEquals(layout(false, false), layout(true, false));
    }
}
