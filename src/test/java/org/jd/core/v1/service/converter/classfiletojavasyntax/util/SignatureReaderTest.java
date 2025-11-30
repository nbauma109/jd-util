package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.SignatureReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SignatureReaderTest {
    private SignatureReader signatureReader;

    @Before
    public void setUp() {
        signatureReader = new SignatureReader("Ljava/lang/String;", 0); //$NON-NLS-1$
    }

    @Test
    public void testRead() {
        Assert.assertEquals('L', signatureReader.read());
        Assert.assertEquals('j', signatureReader.read());
    }

    @Test
    public void testNextEqualsTo() {
        Assert.assertTrue(signatureReader.nextEqualsTo('L'));
        Assert.assertFalse(signatureReader.nextEqualsTo('j'));
    }

    @Test
    public void testSearch() {
        Assert.assertTrue(signatureReader.search(';'));
        Assert.assertEquals("Ljava/lang/String;".indexOf(';'), signatureReader.index); //$NON-NLS-1$
        Assert.assertFalse(signatureReader.search('Z'));
    }

    @Test
    public void testAvailable() {
        Assert.assertTrue(signatureReader.available());
        signatureReader = new SignatureReader("", 0); //$NON-NLS-1$
        Assert.assertFalse(signatureReader.available());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("SignatureReader{index=0, nextChars=Ljava/lang/String;}", signatureReader.toString()); //$NON-NLS-1$
        signatureReader.read(); // Move index
        Assert.assertEquals("SignatureReader{index=1, nextChars=java/lang/String;}", signatureReader.toString()); //$NON-NLS-1$
    }
}
