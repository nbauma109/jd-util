package org.jd.core.v1;

import static org.junit.Assert.*;

import org.jd.core.v1.util.DefaultStack;
import org.junit.Before;
import org.junit.Test;

public class DefaultStackTest {

    private DefaultStack<Integer> stack;

    @Before
    public void setUp() {
        stack = new DefaultStack<>();
    }

    @Test
    public void testPush() {
        stack.push(1);
        assertEquals(1, stack.size());
        assertEquals(1, stack.peek().intValue());
    }

    @Test
    public void testPushAndResize() {
        for(int i = 0; i < 20; i++) {
            stack.push(i);
        }
        assertEquals(20, stack.size());
        assertEquals(19, stack.peek().intValue());
    }

    @Test
    public void testPop() {
        stack.push(1);
        Integer popped = stack.pop();
        assertEquals(1, popped.intValue());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push(2);
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testSize() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.size());
    }

    @Test
    public void testPeek() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.peek().intValue());
    }

    @Test
    public void testReplace() {
        stack.push(1);
        stack.push(1);
        stack.replace(1, 2);
        assertEquals(2, stack.peek().intValue());
    }

    @Test
    public void testReplaceNonExistingElement() {
        // push a non-matching element
        stack.push(1);
        // attempt to replace a non-existing element
        stack.replace(2, 3);
        // verify that the original element hasn't changed
        assertEquals(1, stack.peek().intValue());
    }

    @Test
    public void testCopy() {
        DefaultStack<Integer> other = new DefaultStack<>();
        other.push(1);
        other.push(2);
        stack.copy(other);
        assertEquals(2, stack.size());
        assertEquals(2, stack.peek().intValue());
    }

    @Test
    public void testCopyAndResize() {
        DefaultStack<Integer> other = new DefaultStack<>();
        for(int i = 0; i < 20; i++) {
            other.push(i);
        }
        stack.copy(other);
        assertEquals(20, stack.size());
        assertEquals(19, stack.peek().intValue());
    }

    @Test
    public void testCopyConstructor() {
        stack.push(1);
        DefaultStack<Integer> copiedStack = new DefaultStack<>(stack);
        assertNotNull(copiedStack);
        assertFalse(copiedStack.isEmpty());
        assertEquals(stack.size(), copiedStack.size());
        assertEquals(stack.peek().intValue(), copiedStack.peek().intValue());
    }

    @Test
    public void testToString() {
        assertEquals("Stack{head=0, elements=[]}", stack.toString());
        stack.push(1);
        stack.push(2);
        String expected = "Stack{head=2, elements=[1, 2]}";
        assertEquals(expected, stack.toString());
    }
}
