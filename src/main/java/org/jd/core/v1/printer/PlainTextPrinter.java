/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.printer;

import org.jd.core.v1.api.printer.Printer;

public class PlainTextPrinter implements Printer {
    protected static final String TAB = "  ";
    protected static final String NEWLINE = "\n";

    protected int indentationCount;
    protected StringBuilder sb = new StringBuilder();
    protected int realLineNumber;
    protected String format;

    protected boolean escapeUnicodeCharacters;

    public PlainTextPrinter() {
        this.escapeUnicodeCharacters = false;
    }

    public PlainTextPrinter(boolean escapeUnicodeCharacters) {
        this.escapeUnicodeCharacters = escapeUnicodeCharacters;
    }

    public void init() {
        sb.setLength(0);
        realLineNumber = 0;
        indentationCount = 0;
    }

    @Override
    public String toString() { return sb.toString(); }

    // --- Printer --- //
    @Override
    public void start(int maxLineNumber, int majorVersion, int minorVersion) {
        this.indentationCount = 0;

        if (maxLineNumber == 0) {
            format = "%4d";
        } else {
            int width = 2;

            while (maxLineNumber >= 10) {
                width++;
                maxLineNumber /= 10;
            }

            format = "%" + width + "d";
        }
    }

    @Override
    public void end() {
        // No finalization is needed for the accumulated text.
    }

    @Override
    public void printText(String text) {
        if (!escapeUnicodeCharacters) {
            sb.append(text);
            return;
        }
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (character < 127) {
                sb.append(character);
            } else {
                sb.append("\\u");
                for (int shift = 12; shift >= 0; shift -= 4) {
                    int digit = (character >> shift) & 15;
                    sb.append("0123456789ABCDEF".charAt(digit));
                }
            }
        }
    }

    @Override
    public void printNumericConstant(String constant) { sb.append(constant); }

    @Override
    public void printStringConstant(String constant, String ownerInternalName) { printText(constant); }

    @Override
    public void printKeyword(String keyword) { sb.append(keyword); }

    @Override
    public void printDeclaration(int type, String internalTypeName, String name, String descriptor) { printText(name); }

    @Override
    public void printReference(int type, String internalTypeName, String name, String descriptor, String ownerInternalName) { printText(name); }

    @Override
    public void indent() {
        this.indentationCount++;
    }
    @Override
    public void unindent() {
        if (this.indentationCount > 0) {
            this.indentationCount--;
        }
    }

    @Override
    public void startLine(int lineNumber) {
        printLineNumber(lineNumber);

        for (int i=0; i<indentationCount; i++) {
            sb.append(TAB);
        }
    }

    @Override
    public void endLine() {
        sb.append(NEWLINE);
    }

    @Override
    public void extraLine(int count) {
        while (count-- > 0) {
            printLineNumber(0);
            sb.append(NEWLINE);
        }
    }

    @Override
    public void startMarker(int type) {
        // Plain text omits marker metadata.
    }

    @Override
    public void endMarker(int type) {
        // Plain text omits marker metadata.
    }

    protected void printLineNumber(int lineNumber) {
        sb.append("/*");
        sb.append(String.format(format, ++realLineNumber));
        sb.append(':');
        sb.append(String.format(format, lineNumber));
        sb.append(" */ ");
    }
}
