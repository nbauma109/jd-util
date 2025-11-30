package org.jd.core.v1.model.javasyntax.reference;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ElementValuesTest {

    @Test
    public void testElementValues() {
        // Arrange
        BaseElementValue value1 = new ElementValueArrayInitializerElementValue();
        BaseElementValue value2 = new ElementValueArrayInitializerElementValue();

        // Act
        ElementValues values = new ElementValues();
        values.add(value1);
        values.add(value2);

        // Assert
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testElementValues2() {
        // Arrange
        BaseElementValue value1 = new ElementValueArrayInitializerElementValue();
        BaseElementValue value2 = new ElementValueArrayInitializerElementValue();

        // Act
        ElementValues values = new ElementValues(2);
        values.add(value1);
        values.add(value2);

        // Assert
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testAccept() {

        TestReferenceVisitor testVisitor = new TestReferenceVisitor();
        new ElementValues().accept(testVisitor);

        // Assert
        assertEquals(1, testVisitor.getVisitElementValuesCount());

    }

    @Test
    public void testToString() {
        // Arrange
        BaseElementValue value1 = new ElementValueArrayInitializerElementValue();
        ElementValues values = new ElementValues(1);
        values.add(value1);

        // Act
        String expected = "ElementValues{[" + value1.toString() + "]}";
        String actual = values.toString();

        // Assert
        assertEquals(expected, actual);
    }
}
