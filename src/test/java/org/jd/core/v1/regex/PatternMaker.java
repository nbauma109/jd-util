/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.regex;

public class PatternMaker {
    public static String make (String first, String... next) {
        StringBuilder sb = new StringBuilder("(?s).*");

        sb.append(replace(first));

        for (String s : next) {
            sb.append("[^\\n\\r]*").append(replace(s));
        }

        sb.append(".*");

        return sb.toString();
    }

    public static String make(String s) {
        return "(?s).*" + replace(s) + ".*";
    }

    protected static String replace(String s) {
        String escaped = s
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace(".", "\\.")
                .replace("?", "\\?")
                .replace("+", "\\+")
                .replace("*", "\\*")
                .replace("|", "\\|")
                .replace("^", "\\^");
        escaped = replaceBrace(escaped, '{');
        return replaceBrace(escaped, '}').replace(",", "[^\\n\\r]*,");
    }

    private static String replaceBrace(String text, char brace) {
        StringBuilder result = new StringBuilder(text.length());
        int pendingWhitespace = 0;
        boolean afterBrace = false;
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (isRegexWhitespace(character)) {
                if (!afterBrace) {
                    pendingWhitespace++;
                }
            } else if (character == brace) {
                pendingWhitespace = 0;
                result.append("[^\\n\\r]*\\").append(brace).append("[^\\n\\r]*");
                afterBrace = true;
            } else {
                result.append(text, index - pendingWhitespace, index);
                pendingWhitespace = 0;
                result.append(character);
                afterBrace = false;
            }
        }
        result.append(text, text.length() - pendingWhitespace, text.length());
        return result.toString();
    }

    private static boolean isRegexWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n'
                || character == '\r' || character == '\f' || character == '\u000b';
    }
}
