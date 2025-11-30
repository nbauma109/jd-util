package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FormalParametersTest {

    @Test
    public void testFormalParameters() {
        // Arrange
        FormalParameter fp1 = new FormalParameter(ObjectType.TYPE_OBJECT, "param1"); //$NON-NLS-1$
        FormalParameter fp2 = new FormalParameter(ObjectType.TYPE_OBJECT, "param2"); //$NON-NLS-1$
        TestDeclarationVisitor visitor = new TestDeclarationVisitor();

        // Act
        FormalParameters formalParameters = new FormalParameters(fp1, fp2);

        // Assert
        assertEquals(2, formalParameters.size());
        assertEquals(fp1, formalParameters.get(0));
        assertEquals(fp2, formalParameters.get(1));
        assertEquals(0, new FormalParameters().size());

        // Act
        formalParameters.accept(visitor);

        // Assert
        assertEquals(1, visitor.getFormalParametersCount());
    }

    @Test
    public void testFormalParametersException() {
        // Arrange
        FormalParameter fp1 = new FormalParameter(ObjectType.TYPE_OBJECT, "param1"); //$NON-NLS-1$

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new FormalParameters(fp1));
    }
}
