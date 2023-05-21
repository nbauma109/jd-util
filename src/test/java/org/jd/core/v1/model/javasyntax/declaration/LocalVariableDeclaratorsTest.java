package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LocalVariableDeclaratorsTest {

    private LocalVariableDeclarators localVariableDeclarators;
    private TestDeclarationVisitor visitor;

    @Before
    public void setUp() {
        localVariableDeclarators = new LocalVariableDeclarators(10);
        visitor = new TestDeclarationVisitor();
    }

    @Test
    public void testLocalVariableDeclarators() {
        // Arrange
        LocalVariableDeclarator declarator = new LocalVariableDeclarator(1, "test", null);
        localVariableDeclarators.add(declarator);

        // Act
        localVariableDeclarators.accept(visitor);

        // Assert
        assertEquals(1, visitor.getLocalVariableDeclaratorsCount());
        assertEquals(1, localVariableDeclarators.size());
        assertEquals(1, localVariableDeclarators.getLineNumber());
        assertFalse(localVariableDeclarators.isEmpty());
        localVariableDeclarators.clear();
        assertEquals(0, localVariableDeclarators.getLineNumber());
    }
}
