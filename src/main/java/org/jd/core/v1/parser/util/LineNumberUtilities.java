/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser.util;

import org.jd.core.v1.model.javasyntax.declaration.MemberDeclaration;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.SearchFirstKnownLineNumberVisitor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LineNumberUtilities {

    private LineNumberUtilities() {
    }

    public static void sortMembersByFirstLineNumber(final List<MemberDeclaration> members) {
        if (members == null || members.size() < 2) {
            return;
        }

        Collections.sort(members, Comparator.comparing(LineNumberUtilities::firstLineNumber));
    }

    public static int firstLineNumber(final MemberDeclaration member) {
        if (member == null) {
            return Integer.MAX_VALUE;
        }

        final SearchFirstKnownLineNumberVisitor v = new SearchFirstKnownLineNumberVisitor();
        v.safeAccept(member);

        final int ln = v.getLineNumber();
        if (ln <= 0) {
            return Integer.MAX_VALUE;
        }
        return ln;
    }
}
