package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnnotationDeclarationTest {

    private AnnotationDeclaration annotationDeclaration;
    private TestDeclarationVisitor visitor;

    @Before
    public void setUp() {
        annotationDeclaration = new AnnotationDeclaration(
                null,
                0,
                "org/jd/core/v1/service/test/AnnotationTest", //$NON-NLS-1$
                "AnnotationTest", //$NON-NLS-1$
                null,
                null
        );

        visitor = new TestDeclarationVisitor();
    }

    @Test
    public void testAnnotationDeclaration() {
        // Act
        annotationDeclaration.accept(visitor);

        // Assert
        assertEquals(1, visitor.getAnnotationDeclarationCount());
        assertNull(annotationDeclaration.getAnnotationDeclarators());
    }

    @Test
    public void testToString() {
        // Act & Assert
        assertEquals("AnnotationDeclaration{org/jd/core/v1/service/test/AnnotationTest}", annotationDeclaration.toString()); //$NON-NLS-1$
    }
}
