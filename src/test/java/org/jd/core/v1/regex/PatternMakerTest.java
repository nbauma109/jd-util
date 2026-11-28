package org.jd.core.v1.regex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternMakerTest {

    @Test
    public void preservesWhitespaceOutsideBraceMatches() {
        assertEquals("  name  ", PatternMaker.replace("  name  "));
        assertEquals("a[^\\n\\r]*\\{[^\\n\\r]*b",
                PatternMaker.replace("a \t{\r\n b"));
        assertEquals("a[^\\n\\r]*\\}[^\\n\\r]*b",
                PatternMaker.replace("a \t}\r\n b"));
    }

    @Test
    public void handlesAdjacentBracesAndEscapedMetacharacters() {
        String opening = "[^\\n\\r]*\\{[^\\n\\r]*";
        String closing = "[^\\n\\r]*\\}[^\\n\\r]*";
        assertEquals(opening + opening + closing + closing,
                PatternMaker.replace(" { \t{ \n} } "));
        assertEquals("\\[x\\][^\\n\\r]*,\\(y\\)",
                PatternMaker.replace("[x],(y)"));
    }

    @Test
    public void handlesLongWhitespaceRuns() {
        String whitespace = " ".repeat(100_000);
        assertEquals(whitespace + "value", PatternMaker.replace(whitespace + "value"));
        assertEquals("[^\\n\\r]*\\{[^\\n\\r]*", PatternMaker.replace(whitespace + "{" + whitespace));
    }
}
