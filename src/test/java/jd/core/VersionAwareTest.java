package jd.core;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VersionAwareTest implements VersionAware {

	@Test
	public void test() throws Exception {
		assertEquals("Apache Commons BCEL", this.getMainAttribute(JavaClass.class, "Bundle-Name")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("SNAPSHOT", this.getVersion()); //$NON-NLS-1$
		assertNotNull(this.getMainAttribute("JD-Util-Version")); //$NON-NLS-1$
		assertEquals("Plexus Archiver", this.getMainAttribute("Archiver-Version")); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
