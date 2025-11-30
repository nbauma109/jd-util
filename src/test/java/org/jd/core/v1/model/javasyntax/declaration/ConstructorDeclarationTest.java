package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.expression.SuperConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.ExpressionStatement;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ConstructorDeclarationTest {

    @Test
    public void testConstructorDeclaration() {
        Type type = ObjectType.TYPE_STRING;
        boolean varargs = false;
        String name = "param"; //$NON-NLS-1$

        FormalParameter formalParameter = new FormalParameter(type, varargs, name);
        String descriptor = "()V"; //$NON-NLS-1$
        BaseStatement statements = new ExpressionStatement(new SuperConstructorInvocationExpression(0, ObjectType.TYPE_OBJECT, descriptor, null, false));

        ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(0, formalParameter, descriptor, statements);

        // Getters
        assertEquals(0, constructorDeclaration.getFlags());
        assertFalse(constructorDeclaration.isStatic());
        assertNull(constructorDeclaration.getAnnotationReferences());
        assertNull(constructorDeclaration.getTypeParameters());
        assertEquals(formalParameter, constructorDeclaration.getFormalParameters());
        assertNull(constructorDeclaration.getExceptionTypes());
        assertEquals(descriptor, constructorDeclaration.getDescriptor());
        assertEquals(statements, constructorDeclaration.getStatements());

        // Setters
        constructorDeclaration.setFlags(0);
        assertEquals(0, constructorDeclaration.getFlags());

        // Accept method
        TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
        constructorDeclaration.accept(testVisitor);
        assertEquals(1, testVisitor.getConstructorDeclarationCount());

        // toString method
        String expectedToString = "ConstructorDeclaration{" + descriptor + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expectedToString, constructorDeclaration.toString());
    }
}
