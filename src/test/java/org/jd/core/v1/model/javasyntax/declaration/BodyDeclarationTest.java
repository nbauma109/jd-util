package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BodyDeclarationTest {

    @Test
    public void testBodyDeclaration() {
        // Test constructor
        BaseMemberDeclaration memberDeclarations = new ClassDeclaration(0, null, null, null);
        BodyDeclaration bodyDeclaration = new BodyDeclaration("MyType", memberDeclarations);

        assertEquals("MyType", bodyDeclaration.getInternalTypeName());
        assertTrue(bodyDeclaration.getMemberDeclarations() instanceof ClassDeclaration);
        assertFalse(bodyDeclaration.isAnonymous());

        // Test the setAnonymous method
        bodyDeclaration.setAnonymous(true);
        assertTrue(bodyDeclaration.isAnonymous());

        // Test the accept method with a simple visitor
        TestDeclarationVisitor visitor = new TestDeclarationVisitor();
        bodyDeclaration.accept(visitor);
        assertEquals(1, visitor.getBodyDeclarationCount());
    }
}
