package jd.core.links;

import org.jd.core.v1.util.StringConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HyperlinkReferenceDataTest {

	@Test
	public void test() throws Exception {
		ReferenceData referenceData = new ReferenceData(StringConstants.JAVA_LANG_OBJECT, "hashCode", "()I", null); //$NON-NLS-1$ //$NON-NLS-2$
		HyperlinkReferenceData hyperlinkReferenceData = new HyperlinkReferenceData(1, 2, referenceData);
		assertEquals(1, hyperlinkReferenceData.getStartPosition());
		assertEquals(3, hyperlinkReferenceData.getEndPosition());
		assertFalse(hyperlinkReferenceData.isEnabled());
		hyperlinkReferenceData.setEnabled(true);
		assertTrue(hyperlinkReferenceData.isEnabled());
		assertEquals(referenceData, hyperlinkReferenceData.getReference());
		assertEquals("ReferenceData [typeName=java/lang/Object, name=hashCode, descriptor=()I, owner=null, enabled=false]", hyperlinkReferenceData.toString()); //$NON-NLS-1$
	}
}
