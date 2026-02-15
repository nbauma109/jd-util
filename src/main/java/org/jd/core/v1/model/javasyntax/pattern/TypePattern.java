/*
 * Copyright (c) 2008-2026 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.pattern;

import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.Objects;

public class TypePattern implements Pattern {
    private final Type type;
    private final String variableName;
    private final boolean fina1;

    public TypePattern(Type type, String variableName, boolean fina1) {
        this.type = Objects.requireNonNull(type, "type");
        this.variableName = variableName;
        this.fina1 = fina1;
    }

    public TypePattern(Type type, String variableName) {
        this(type, variableName, false);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

    public boolean isFinal() {
        return fina1;
    }

    @Override
    public String toString() {
        return "TypePattern{type=" + type + ", variableName='" + variableName + "'}";
    }
}
