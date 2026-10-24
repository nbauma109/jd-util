package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EscapeSurrogatesTest {
    @Test
    public void testSurrogateCharacterIsEscaped() {
        assertEquals("\\uD800", CharacterUtil.escapeChar(0xD800));
        assertEquals("\\uDFFF", CharacterUtil.escapeChar(0xDFFF));
        assertEquals("é", CharacterUtil.escapeChar(0xE9));
    }

    @Test
    public void testLoneSurrogatesOfStringAreEscaped() {
        assertEquals("a\\uD800b", StringUtil.escapeString("a\ud800b"));
        assertEquals("\\uDC00", StringUtil.escapeString("\udc00"));
        assertEquals("\\uD800", StringUtil.escapeString("\ud800"));
    }

    @Test
    public void testPairOfSurrogatesIsKept() {
        assertEquals("x😀y", StringUtil.escapeString("x😀y"));
        assertEquals("😀\\uDC00", StringUtil.escapeString("😀\udc00"));
        assertEquals("\\uD83D\\\\", StringUtil.escapeString("\ud83d\\"));
    }
}
