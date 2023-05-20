package org.jd.core.v1.model.javasyntax.reference;

import org.jd.core.v1.model.javasyntax.expression.NullExpression;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class AnnotationReferenceTest {

    private AnnotationReference annotationReference;
    private ObjectType objectType;
    private BaseElementValue elementValue;
    private BaseElementValuePair elementValuePairs;
    private TypeMaker typeMaker;

    @Before
    public void setUp() {
        typeMaker = new TypeMaker();
        objectType = typeMaker.makeFromInternalTypeName("java/lang/Deprecated");
        elementValuePairs = new ElementValuePairs();
        elementValue = null;
        annotationReference = new AnnotationReference(objectType, elementValue, elementValuePairs);
    }

    @Test
    public void testAnnotationReferenceTypeConstructor() {
        AnnotationReference ar = new AnnotationReference(objectType);
        assertEquals(objectType, ar.getType());
        assertNull(ar.getElementValue());
        assertNull(ar.getElementValuePairs());
    }

    @Test
    public void testAnnotationReferenceTypeElementValueConstructor() {
        AnnotationReference ar = new AnnotationReference(objectType, elementValue);
        assertEquals(objectType, ar.getType());
        assertEquals(elementValue, ar.getElementValue());
        assertNull(ar.getElementValuePairs());
    }

    @Test
    public void testAnnotationReferenceTypeElementValuePairConstructor() {
        AnnotationReference ar = new AnnotationReference(objectType, elementValuePairs);
        assertEquals(objectType, ar.getType());
        assertNull(ar.getElementValue());
        assertEquals(elementValuePairs, ar.getElementValuePairs());
    }

    @Test
    public void testGetType() {
        assertEquals(objectType, annotationReference.getType());
    }

    @Test
    public void testGetElementValue() {
        assertEquals(elementValue, annotationReference.getElementValue());
    }

    @Test
    public void testGetElementValuePairs() {
        assertEquals(elementValuePairs, annotationReference.getElementValuePairs());
    }

    @Test
    public void testEquals() {
        AnnotationReference equalAnnotationReference = new AnnotationReference(objectType, elementValue, elementValuePairs);
        ObjectType differentObjectType = typeMaker.makeFromInternalTypeName("java/lang/Override");
        AnnotationReference differentAnnotationReference = new AnnotationReference(differentObjectType, elementValue, elementValuePairs);
        assertEquals(annotationReference, equalAnnotationReference);
        assertNotEquals(annotationReference, differentAnnotationReference);
        AnnotationReference ar1 = new AnnotationReference(objectType, elementValue);
        AnnotationReference ar2 = new AnnotationReference(objectType, elementValue);
        AnnotationReference ar3 = new AnnotationReference(objectType, elementValue, new ElementValuePairs());
        AnnotationReference ar4 = new AnnotationReference(objectType, new ExpressionElementValue(new NullExpression(ObjectType.TYPE_UNDEFINED_OBJECT)));
        assertEquals(ar1, ar2);
        assertEquals(ar2, ar1);
        assertEquals(ar1, ar1);
        assertNotEquals(ar1, ar3);
        assertNotEquals(ar1, ar4);
        assertNotEquals(ar1, null);
        assertNotEquals(ar1, "Non-AnnotationReference Object");
    }

    @Test
    public void testHashCode() {
        AnnotationReference equalAnnotationReference = new AnnotationReference(objectType, elementValue, elementValuePairs);
        assertEquals(annotationReference.hashCode(), equalAnnotationReference.hashCode());
    }

    @Test
    public void testAccept() {
        TestReferenceVisitor testVisitor = new TestReferenceVisitor();
        annotationReference.accept(testVisitor);
        assertEquals(1, testVisitor.getVisitAnnotationReferenceCount());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(annotationReference.isEmpty());
    }
}
