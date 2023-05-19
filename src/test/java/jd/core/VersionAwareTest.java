package jd.core;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VersionAwareTest implements VersionAware {

    @Test
    public void test() throws Exception {
        assertEquals("Apache Commons BCEL", getMainAttribute(JavaClass.class, "Bundle-Name"));
        assertEquals("SNAPSHOT", getVersion());
        assertNotNull(getMainAttribute("JD-Util-Version"));
    }
}
