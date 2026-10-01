/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser.util;

import org.jd.core.v1.parser.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TextUtilitiesTest {

    @Test
    public void charLiterals() throws ParseException {
        assertEquals('a', TextUtilities.parseCharLiteral("'a'"));
        assertEquals('\n', TextUtilities.parseCharLiteral("'\\n'"));
    }

    @Test
    public void invalidCharLiterals() {
        assertThrows(ParseException.class, () -> TextUtilities.parseCharLiteral(null));
        assertThrows(ParseException.class, () -> TextUtilities.parseCharLiteral("''"));
        assertThrows(ParseException.class, () -> TextUtilities.parseCharLiteral("a'a'"));
        assertThrows(ParseException.class, () -> TextUtilities.parseCharLiteral("'ab'"));
    }

    @Test
    public void stringLiterals() throws ParseException {
        assertEquals("a\tb", TextUtilities.parseStringLiteral("\"a\\tb\""));
        assertEquals("", TextUtilities.parseStringLiteral(null));
    }

    @Test
    public void invalidStringLiterals() {
        assertThrows(ParseException.class, () -> TextUtilities.parseStringLiteral("a"));
        assertThrows(ParseException.class, () -> TextUtilities.parseStringLiteral("\"a"));
    }
}
