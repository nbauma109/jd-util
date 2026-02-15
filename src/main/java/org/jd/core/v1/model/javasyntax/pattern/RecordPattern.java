/*
 * Copyright (c) 2008-2026 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.pattern;

import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.List;

public record RecordPattern(Type type, List<Pattern> componentPatterns, String variableName) implements Pattern {

    @Override
    public String toString() {
        return "RecordPattern{type=" + type + ", componentPatterns=" + componentPatterns + ", variableName='" + variableName + "'}";
    }
}
