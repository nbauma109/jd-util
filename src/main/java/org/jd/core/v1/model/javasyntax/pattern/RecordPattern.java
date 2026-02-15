/*
 * Copyright (c) 2008-2026 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.pattern;

import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RecordPattern implements Pattern {
    private final Type type;
    private final List<Pattern> componentPatterns;
    private final String variableName;

    public RecordPattern(Type type, List<Pattern> componentPatterns, String variableName) {
        this.type = Objects.requireNonNull(type, "type");
        this.componentPatterns = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(componentPatterns, "componentPatterns")));
        this.variableName = variableName;
    }

    @Override
    public Type getType() {
        return type;
    }

    public List<Pattern> getComponentPatterns() {
        return componentPatterns;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return "RecordPattern{type=" + type + ", componentPatterns=" + componentPatterns + ", variableName='" + variableName + "'}";
    }
}
