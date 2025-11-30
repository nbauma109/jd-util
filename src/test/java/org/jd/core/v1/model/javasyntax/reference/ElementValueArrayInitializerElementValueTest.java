package org.jd.core.v1.model.javasyntax.reference;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ElementValueArrayInitializerElementValueTest {

	@Test
	public void testElementValueArrayInitializerElementValue() {
		// Arrange & Act
		ElementValueArrayInitializerElementValue noArgsElementValue = new ElementValueArrayInitializerElementValue();

		// Assert
		assertNull(noArgsElementValue.getElementValueArrayInitializer());
		assertEquals("ElementValueArrayInitializerElementValue{null}", noArgsElementValue.toString()); //$NON-NLS-1$

		// Act
		ElementValueArrayInitializerElementValue elementValueArrayInitializerElementValue = new ElementValueArrayInitializerElementValue();

		// Assert
		assertNull(elementValueArrayInitializerElementValue.getElementValueArrayInitializer());
		assertEquals("ElementValueArrayInitializerElementValue{null}", elementValueArrayInitializerElementValue.toString()); //$NON-NLS-1$

		// Act & Assert for visitor
		TestReferenceVisitor visitor = new TestReferenceVisitor();
		elementValueArrayInitializerElementValue.accept(visitor);
		assertEquals(1, visitor.getVisitElementValueArrayInitializerElementValueCount());
	}
}
