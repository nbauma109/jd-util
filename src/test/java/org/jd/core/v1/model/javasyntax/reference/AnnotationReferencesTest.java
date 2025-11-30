package org.jd.core.v1.model.javasyntax.reference;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnnotationReferencesTest {

    private AnnotationReferences<AnnotationReference> annotationReferences;
    private AnnotationReferences<AnnotationReference> annotationReferencesWithCapacity;
    private TestReferenceVisitor visitor;

    @Before
    public void setUp() {
        annotationReferences = new AnnotationReferences<AnnotationReference>();
        annotationReferencesWithCapacity = new AnnotationReferences<AnnotationReference>(10);
        visitor = new TestReferenceVisitor();
    }

    @Test
    public void testAnnotationReferences() {
        // Act
        annotationReferences.accept(visitor);

        // Assert
        assertEquals(1, visitor.getVisitAnnotationReferencesCount());
    }

    @Test
    public void testAnnotationReferencesWithCapacity() {
        // Act
        annotationReferencesWithCapacity.accept(visitor);

        // Assert
        assertEquals(1, visitor.getVisitAnnotationReferencesCount());
        assertEquals(0, annotationReferencesWithCapacity.size());
    }
}
