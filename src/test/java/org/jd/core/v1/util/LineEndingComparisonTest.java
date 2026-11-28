package org.jd.core.v1.util;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class LineEndingComparisonTest implements DefaultTest {

    @Test
    public void ignoresTrailingSpacesAndCarriageReturnsBeforeNewlines() {
        assertEqualsIgnoreEOL("first  \r\nsecond \n", "first\nsecond\n");
        assertEqualsIgnoreEOL(" ".repeat(100_000) + "\r\n", "\n");
    }

    @Test
    public void preservesTabsStandaloneCarriageReturnsAndTrailingSpaces() {
        assertThrows(AssertionError.class, () -> assertEqualsIgnoreEOL("text\t\n", "text\n"));
        assertThrows(AssertionError.class, () -> assertEqualsIgnoreEOL("text\r", "text"));
        assertThrows(AssertionError.class, () -> assertEqualsIgnoreEOL("text ", "text"));
    }
}
