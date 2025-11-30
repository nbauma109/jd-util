package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NewExpressionTest {

    @Test
    public void testNewExpression() {
        int lineNumber = 1;
        Type type = ObjectType.TYPE_STRING;
        String descriptor = "Ljava/lang/String;"; //$NON-NLS-1$
        boolean varArgs = true;
        boolean diamondPossible = false;

        NewExpression newExpression = new NewExpression(lineNumber, (ObjectType) type, descriptor, varArgs, diamondPossible);

        // Getters
        assertEquals(lineNumber, newExpression.getLineNumber());
        assertEquals(type, newExpression.getObjectType());
        assertEquals(type, newExpression.getType());
        assertEquals(descriptor, newExpression.getDescriptor());
        assertNull(newExpression.getParameters());
        assertNull(newExpression.getBodyDeclaration());
        assertTrue(newExpression.isVarArgs());
        assertFalse(newExpression.isDiamondPossible());
        assertTrue(newExpression.isNewExpression());
        assertTrue(newExpression.isNew());
        assertEquals("NewExpression{new " + type + "}", newExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$

        // Setters
        BaseExpression parameters = new IntegerConstantExpression(2, 42);
        newExpression.setParameters(parameters);
        newExpression.setDiamondPossible(true);
        newExpression.setQualifier(new ThisExpression(3, type));

        assertEquals(parameters, newExpression.getParameters());
        assertTrue(newExpression.isDiamondPossible());
        assertNotNull(newExpression.getQualifier());

        // Accept method
        TestVisitor testVisitor = new TestVisitor();
        newExpression.accept(testVisitor);

        assertEquals(1, testVisitor.getNewExpressionCount());

        // Copy method
        NewExpression copiedExpression = (NewExpression) newExpression.copyTo(4);
        assertEquals(4, copiedExpression.getLineNumber());
        assertEquals(type, copiedExpression.getType());
        assertEquals(descriptor, copiedExpression.getDescriptor());
        assertNull(copiedExpression.getParameters());
        assertNull(copiedExpression.getBodyDeclaration());
        assertTrue(copiedExpression.isVarArgs());
        assertTrue(copiedExpression.isDiamondPossible());
        assertTrue(copiedExpression.isNewExpression());
        assertEquals("NewExpression{new " + type + "}", copiedExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$

        // Priority
        assertEquals(0, newExpression.getPriority());
    }

}
