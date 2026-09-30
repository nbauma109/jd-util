/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.message;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DecompileContextTest {

    @Test
    public void realignLineNumbersIsOffByDefault() {
        DecompileContext context = new DecompileContext();

        assertFalse(context.isRealignLineNumbers());
    }

    @Test
    public void realignLineNumbersWithoutConfiguration() {
        DecompileContext context = new DecompileContext();
        context.setConfiguration(null);

        assertFalse(context.isRealignLineNumbers());
    }

    @Test
    public void realignLineNumbersFollowsTheConfiguration() {
        DecompileContext context = new DecompileContext();

        context.setConfiguration(Map.of("realignLineNumbers", Boolean.TRUE));
        assertTrue(context.isRealignLineNumbers());

        context.setConfiguration(Map.of("realignLineNumbers", "true"));
        assertTrue(context.isRealignLineNumbers());

        context.setConfiguration(Map.of("realignLineNumbers", Boolean.FALSE));
        assertFalse(context.isRealignLineNumbers());

        context.setConfiguration(Map.of("other", Boolean.TRUE));
        assertFalse(context.isRealignLineNumbers());
    }
}
