/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.token;

/**
 * Must be created between StartStatementToken and EndStatementToken.
 * <p>A <i>floating</i> line number belongs to a statement which could not be aligned on its line: it is only reported
 * to the printer when no other, aligned, line number is found on the same physical line.</p>
 */
public record LineNumberToken(int lineNumber, boolean floating) implements Token {

    public LineNumberToken(int lineNumber) {
        this(lineNumber, false);
    }

    @Override
    public String toString() {
        return "LineNumberToken{" + lineNumber + (floating ? ", floating" : "") + "}";
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
