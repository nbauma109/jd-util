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
        assertEquals(expected.replaceAll("\s*\r?\n", "\n"), actual.replaceAll("\s*\r?\n", "\n"));
    }
}
