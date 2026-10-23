package org.jd.core.v1.model.javafragment;

import org.jd.core.v1.model.javasyntax.JavaImport;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;

public class ImportsFragmentKeepTest {
    private static ImportsFragment fragmentOfThree() {
        ImportsFragment fragment = new ImportsFragment(1);

        fragment.addImport("a/First", "a.First");
        fragment.addImport("b/Second", "b.Second");
        fragment.addImport("c/Third", "c.Third");
        // the first is the most used
        fragment.addImport("a/First", "a.First");
        fragment.addImport("a/First", "a.First");
        fragment.initLineCounts();
        return fragment;
    }

    @Test
    public void testLessUsedImportsAreRemovedWhenTheLinesAreCut() {
        ImportsFragment fragment = fragmentOfThree();

        fragment.decLineCount(true);
        fragment.decLineCount(true);

        Assert.assertEquals("a.First", fragment.getImports().stream().map(JavaImport::getQualifiedName).collect(Collectors.joining(",")));
        Assert.assertFalse(fragment.contains("b/Second"));
    }

    @Test
    public void testKeptImportIsNotRemovedWhenTheLinesAreCut() {
        ImportsFragment fragment = fragmentOfThree();

        fragment.keep("c/Third");
        Assert.assertTrue(fragment.decLineCount(true));
        Assert.assertTrue(fragment.decLineCount(true));
        // (the layout cannot cut the line of the kept import)
        Assert.assertFalse(fragment.decLineCount(true));
        Assert.assertEquals(1, fragment.getLineCount());

        // (one line is left: it is for the kept import, though another one is more used)
        Assert.assertEquals("c.Third", fragment.getImports().stream().map(JavaImport::getQualifiedName).collect(Collectors.joining(",")));
        Assert.assertTrue(fragment.contains("c/Third"));
        Assert.assertFalse(fragment.contains("a/First"));
        Assert.assertFalse(fragment.contains("b/Second"));
    }
}
