/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.token;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LineNumberTokenTest {

    @Test
    public void testToString() {
        assertEquals("LineNumberToken{5}", new LineNumberToken(5).toString());
        assertEquals("LineNumberToken{5, floating}", new LineNumberToken(5, true).toString());
    }

    @Test
    public void testFloating() {
        assertFalse(new LineNumberToken(5).floating());
        assertTrue(new LineNumberToken(5, true).floating());
    }
}
