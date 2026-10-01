/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javafragment;

import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlockFragmentsTest {

    /** A visitor which records the types of the fragments it visits */
    private static JavaFragmentVisitor recorder(List<Class<?>> visited) {
        return (JavaFragmentVisitor) Proxy.newProxyInstance(JavaFragmentVisitor.class.getClassLoader(), new Class<?>[] { JavaFragmentVisitor.class },
                (proxy, method, args) -> {
                    visited.add(args[0].getClass());
                    return null;
                });
    }

    @Test
    public void infiniteLoopStartFragmentsAreVisited() {
        List<Class<?>> visited = new ArrayList<>();
        StartStatementsInfiniteForBlockFragment infiniteFor = new StartStatementsInfiniteForBlockFragment(0, 1, 1, 1, "for");
        StartStatementsInfiniteWhileBlockFragment infiniteWhile = new StartStatementsInfiniteWhileBlockFragment(0, 1, 1, 1, "while");
        StartStatementsBlockFragment.Group group = new StartStatementsBlockFragment.Group(infiniteFor);

        infiniteFor.accept(recorder(visited));
        infiniteWhile.accept(recorder(visited));
        new StartStatementsInfiniteForBlockFragment(0, 1, 1, 1, "for", group).accept(recorder(visited));
        new StartStatementsInfiniteWhileBlockFragment(0, 1, 1, 1, "while", group).accept(recorder(visited));

        assertEquals(List.of(StartStatementsInfiniteForBlockFragment.class, StartStatementsInfiniteWhileBlockFragment.class,
                StartStatementsInfiniteForBlockFragment.class, StartStatementsInfiniteWhileBlockFragment.class), visited);
    }

    @Test
    public void endBodyInParameterLineCount() {
        StartBodyFragment start = new StartBodyFragment(0, 1, 2, 1, "start");
        EndBodyInParameterFragment end = new EndBodyInParameterFragment(0, 1, 2, 1, "end", start);

        assertTrue(end.incLineCount(false));
        assertEquals(2, end.getLineCount());
        assertFalse(end.incLineCount(false));
        assertTrue(end.decLineCount(false));
        assertTrue(end.decLineCount(false));
        assertEquals(0, end.getLineCount());
        assertFalse(end.decLineCount(false));
    }
}
