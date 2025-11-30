package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UnmodifiableTypesTest {
	@Test
	public void testUnmodifiableTypes() {
		UnmodifiableTypes unmodifiableTypes = new UnmodifiableTypes(Arrays.asList(ObjectType.TYPE_PRIMITIVE_INT, ObjectType.TYPE_PRIMITIVE_BOOLEAN));

		assertEquals(2, unmodifiableTypes.size());

		// Test listIterator methods
		ListIterator<Type> iterator = unmodifiableTypes.listIterator();
		assertTrue(iterator.hasNext());
		assertFalse(iterator.hasPrevious());

		// Test next and previous
		assertEquals(ObjectType.TYPE_PRIMITIVE_INT, iterator.next());
		assertEquals(ObjectType.TYPE_PRIMITIVE_BOOLEAN, iterator.next());

		assertThrows(NoSuchElementException.class, iterator::next);

		assertTrue(iterator.hasPrevious());
		assertEquals(ObjectType.TYPE_PRIMITIVE_BOOLEAN, iterator.previous());
		assertEquals(ObjectType.TYPE_PRIMITIVE_INT, iterator.previous());

		assertThrows(NoSuchElementException.class, iterator::previous);

		// Test listIterator(int) method
		ListIterator<Type> iteratorAtOne = unmodifiableTypes.listIterator(1);
		assertEquals(ObjectType.TYPE_PRIMITIVE_BOOLEAN, iteratorAtOne.next());

		// Test methods that should throw UnsupportedOperationException
		assertThrows(UnsupportedOperationException.class, unmodifiableTypes::removeFirst);
		assertThrows(UnsupportedOperationException.class, unmodifiableTypes::removeLast);
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.remove(0));
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.remove(null));
		List<ObjectType> types = Collections.singletonList(ObjectType.TYPE_PRIMITIVE_INT);
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.removeAll(types));
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.retainAll(types));
		assertThrows(UnsupportedOperationException.class, unmodifiableTypes::clear);
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.removeRange(0, 1));
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.set(0, ObjectType.TYPE_PRIMITIVE_INT));
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.removeIf(type -> type == ObjectType.TYPE_PRIMITIVE_INT));
		assertThrows(UnsupportedOperationException.class, () -> unmodifiableTypes.replaceAll(type -> ObjectType.TYPE_PRIMITIVE_CHAR));
		assertThrows(UnsupportedOperationException.class, () -> iterator.set(ObjectType.TYPE_PRIMITIVE_CHAR));
		assertThrows(UnsupportedOperationException.class, () -> iterator.add(ObjectType.TYPE_PRIMITIVE_CHAR));
		assertThrows(UnsupportedOperationException.class, iterator::remove);

		// Test construction of UnmodifiableTypes with default constructor
		UnmodifiableTypes emptyUnmodifiableTypes = new UnmodifiableTypes();
		assertEquals(0, emptyUnmodifiableTypes.size());

		// Test construction of UnmodifiableTypes with capacity
		UnmodifiableTypes unmodifiableTypesWithCapacity = new UnmodifiableTypes(10);
		assertEquals(0, unmodifiableTypesWithCapacity.size());
	}

	@Test
	public void testListIteratorIndices() {
		UnmodifiableTypes unmodifiableTypes = new UnmodifiableTypes(Arrays.asList(ObjectType.TYPE_PRIMITIVE_INT, ObjectType.TYPE_PRIMITIVE_BOOLEAN));

		// Test listIterator methods
		ListIterator<Type> iterator = unmodifiableTypes.listIterator();
		assertTrue(iterator.hasNext());
		assertFalse(iterator.hasPrevious());

		assertEquals(0, iterator.nextIndex());
		assertEquals(-1, iterator.previousIndex());

		// Move iterator forward
		iterator.next();
		assertEquals(1, iterator.nextIndex());
		assertEquals(0, iterator.previousIndex());

		// Move iterator to the end
		iterator.next();
		assertEquals(2, iterator.nextIndex());
		assertEquals(1, iterator.previousIndex());
	}
}
