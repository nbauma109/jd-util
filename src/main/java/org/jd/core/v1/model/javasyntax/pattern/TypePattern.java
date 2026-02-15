/*
 * Copyright (c) 2008-2026 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.pattern;

import org.jd.core.v1.model.javasyntax.type.Type;

public record TypePattern(Type type, String variableName, boolean fina1) implements Pattern {

    public TypePattern(Type type, String variableName) {
        this(type, variableName, false);
    }

    @Override
    public String toString() {
        return "TypePattern{type=" + type + ", variableName='" + variableName + "'}";
    }
}
