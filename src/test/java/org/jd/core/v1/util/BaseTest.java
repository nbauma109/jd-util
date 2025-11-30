package org.jd.core.v1.util;

import org.junit.Test;
import org.junit.Assert;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BaseTest {
	static class BaseImpl implements Base<BaseImpl> {}

	@Test
	public void isListTest() {
		Base<BaseImpl> base = new BaseImpl();
		Assert.assertFalse(base.isList());
	}

	@Test
	public void getFirstTest() {
		Base<BaseImpl> base = new BaseImpl();
		Assert.assertEquals(base, base.getFirst());
	}

	@Test
	public void getLastTest() {
		Base<BaseImpl> base = new BaseImpl();
		Assert.assertEquals(base, base.getLast());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getListTest() {
		Base<BaseImpl> base = new BaseImpl();
		base.getList();
	}

	@Test
	public void sizeTest() {
		Base<BaseImpl> base = new BaseImpl();
		Assert.assertEquals(1, base.size());
	}

	@Test
	public void iteratorTest() {
		Base<BaseImpl> base = new BaseImpl();
		Iterator<BaseImpl> iterator = base.iterator();

		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(base, iterator.next());
		Assert.assertFalse(iterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void iteratorNextNoElementTest() {
		Base<BaseImpl> base = new BaseImpl();
		Iterator<BaseImpl> iterator = base.iterator();
		iterator.next();
		iterator.next(); // This will throw NoSuchElementException
	}

	@Test(expected = UnsupportedOperationException.class)
	public void iteratorRemoveTest() {
		Base<BaseImpl> base = new BaseImpl();
		Iterator<BaseImpl> iterator = base.iterator();
		iterator.remove(); // This will throw UnsupportedOperationException
	}
}
