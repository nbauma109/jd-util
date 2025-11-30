package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.declaration.BaseLocalVariableDeclarator;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclarator;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclarators;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalVariableDeclarationStatementTest {
    private LocalVariableDeclarationStatement localVariableDeclarationStatement;
    private TestVisitor visitor;

    @Before
    public void setUp() {
        visitor = new TestVisitor();

        Type type = PrimitiveType.TYPE_INT;
        BaseLocalVariableDeclarator localVariableDeclarators = new LocalVariableDeclarators(1);
        ((LocalVariableDeclarators)localVariableDeclarators).add(new LocalVariableDeclarator("a")); //$NON-NLS-1$

        localVariableDeclarationStatement = new LocalVariableDeclarationStatement(type, localVariableDeclarators);
    }

    @Test
    public void testLocalVariableDeclarationStatement() {
        // Act
        localVariableDeclarationStatement.accept(visitor);

        // Assert
        assertEquals(1, visitor.getLocalVariableDeclarationStatementCount());
        assertTrue(localVariableDeclarationStatement.isLocalVariableDeclarationStatement());
        assertEquals("LocalVariableDeclarationStatement{PrimitiveType{primitive=int} [LocalVariableDeclarator{name=a, dimension0, variableInitializer=null}]}", localVariableDeclarationStatement.toString()); //$NON-NLS-1$
    }
}
