package jd.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ClassUtilTest {

	@Test
	public void testGetInternalNameValid() {
		assertEquals("MyClass", ClassUtil.getInternalName("MyClass.class")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testGetInternalNameInvalid() {
		assertThrows(IllegalArgumentException.class, () -> ClassUtil.getInternalName("NotClassFile.txt")); //$NON-NLS-1$
	}

	@Test
	public void testGetInternalNameNull() {
		assertThrows(IllegalArgumentException.class, () -> ClassUtil.getInternalName(null));
	}

	@Test
	public void testGetInternalNameEdgeCase() {
		assertEquals("", ClassUtil.getInternalName(".class")); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
