/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static String escapeString(String s) {
        for (int index = 0; index < s.length(); index++) {
            char character = s.charAt(index);
            if (character == '\\' || character == '"' || character < ' ' || Character.isSurrogate(character)) {
                return escapeFrom(s, index);
            }
        }
        return s;
    }

    private static String escapeFrom(String text, int start) {
        StringBuilder result = new StringBuilder(text.length() * 2);
        result.append(text, 0, start);
        boolean lowOfPair = false;
        for (int index = start; index < text.length(); index++) {
            char character = text.charAt(index);
            if (lowOfPair) {
                result.append(character);
                lowOfPair = false;
            } else {
                lowOfPair = appendEscaped(text, index, result);
            }
        }
        return result.toString();
    }

    private static boolean appendEscaped(String text, int index, StringBuilder result) {
        char character = text.charAt(index);
        switch (character) {
            case '\\':
                result.append("\\\\");
                break;
            case '\b':
                result.append("\\b");
                break;
            case '\f':
                result.append("\\f");
                break;
            case '\n':
                result.append("\\n");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\t':
                result.append("\\t");
                break;
            case '"':
                result.append("\\\"");
                break;
            default:
                return appendOtherCharacter(text, index, result);
        }
        return false;
    }

    private static boolean appendOtherCharacter(String text, int index, StringBuilder result) {
        char character = text.charAt(index);
        if (character < ' ') {
            result.append("\\0");
            result.append((char) ('0' + (character >> 3)));
            result.append((char) ('0' + (character & 7)));
        } else if (Character.isHighSurrogate(character) && index + 1 < text.length()
                && Character.isLowSurrogate(text.charAt(index + 1))) {
            result.append(character);
            return true;
        } else if (Character.isSurrogate(character)) {
            result.append(CharacterUtil.unicode(character));
        } else {
            result.append(character);
        }
        return false;
    }
}
