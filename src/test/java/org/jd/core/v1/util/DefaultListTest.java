package org.jd.core.v1.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultListTest {

    @Test
    public void testDefaultConstructor() {
        DefaultList<Integer> list = new DefaultList<>();
        assertTrue(list.isList());
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        DefaultList<Integer> list = new DefaultList<>(10);
        assertTrue(list.isList());
        assertEquals(0, list.size());
    }

    @Test
    public void testAddAndRemove() {
        DefaultList<Integer> list = new DefaultList<>();
        list.add(10);
        list.add(20);
        assertEquals(Integer.valueOf(10), list.getFirst());
        assertEquals(Integer.valueOf(20), list.getLast());

        assertEquals(Integer.valueOf(10), list.removeFirst());
        assertEquals(Integer.valueOf(20), list.removeLast());
        assertTrue(list.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIterator() {
        DefaultList<Integer> list = new DefaultList<>();
        list.iterator().next();
    }

    @Test
    public void testConstructorWithCollection() {
        List<Integer> initialList = Arrays.asList(10, 20, 30);
        DefaultList<Integer> list = new DefaultList<>(initialList);
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(10), list.getFirst());
        assertEquals(Integer.valueOf(30), list.getLast());
    }

    @Test
    public void testConstructorWithElementAndVarargs() {
        DefaultList<Integer> list = new DefaultList<>(10, 20, 30);
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(10), list.getFirst());
        assertEquals(Integer.valueOf(30), list.getLast());
    }

    @Test
    public void testConstructorWithArray() {
        Integer[] initialArray = { 10, 20, 30 };
        DefaultList<Integer> list = new DefaultList<>(initialArray);
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(10), list.getFirst());
        assertEquals(Integer.valueOf(30), list.getLast());
    }

    @Test
    public void testConstructorWithNullArray() {
        Integer[] initialArray = null;
        DefaultList<Integer> list = new DefaultList<>(initialArray);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithEmptyArray() {
        Integer[] initialArray = {};
        DefaultList<Integer> list = new DefaultList<>(initialArray);
        assertEquals(0, list.size());
    }

    @Test
    public void testGetList() {
        DefaultList<Integer> list = new DefaultList<>();
        assertEquals(list, list.getList());
    }

    @Test
    public void testEmptyList() {
        DefaultList<Integer> list = DefaultList.emptyList();
        assertEquals(0, list.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyListAdd() {
        DefaultList<Integer> list = DefaultList.emptyList();
        list.add(10, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyListSet() {
        DefaultList<Integer> list = DefaultList.emptyList();
        list.set(0, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyListRemove() {
        DefaultList<Integer> list = DefaultList.emptyList();
        list.remove(0);
    }

    @Test
    public void testEmptyIteratorHasNext() {
        DefaultList<Integer> list = DefaultList.emptyList();
        Iterator<Integer> iterator = list.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIteratorNext() {
        DefaultList<Integer> list = DefaultList.emptyList();
        list.iterator().next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyIteratorRemove() {
        DefaultList<Integer> list = DefaultList.emptyList();
        list.iterator().remove();
    }
}
