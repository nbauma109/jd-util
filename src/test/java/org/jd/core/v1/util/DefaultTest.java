/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.util;

import static org.junit.Assert.assertEquals;

public interface DefaultTest {

    default void assertEqualsIgnoreEOL(String expected, String actual) {
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(actual));
    }
    private static String normalizeLineEndings(String text) {
        StringBuilder result = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (character == '\n') {
                int end = result.length();
                if (end > 0 && result.charAt(end - 1) == '\r') {
                    end--;
                }
                while (end > 0 && result.charAt(end - 1) == ' ') {
                    end--;
                }
                result.setLength(end);
            }
            result.append(character);
        }
        return result.toString();
    }
}
