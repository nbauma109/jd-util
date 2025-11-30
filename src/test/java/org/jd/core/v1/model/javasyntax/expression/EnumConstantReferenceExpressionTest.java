package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EnumConstantReferenceExpressionTest {

    @Test
    public void testEnumConstantReferenceExpression() {
        // Test constructor
        ObjectType objectType = new ObjectType("MyEnum", "MyEnum", "MyEnum"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        EnumConstantReferenceExpression enumConstantReferenceExpression = new EnumConstantReferenceExpression(1, objectType, "ENUM_CONSTANT"); //$NON-NLS-1$

        assertEquals(1, enumConstantReferenceExpression.getLineNumber());
        assertEquals("ENUM_CONSTANT", enumConstantReferenceExpression.getName()); //$NON-NLS-1$
        assertNotNull(enumConstantReferenceExpression.getObjectType());
        assertEquals(enumConstantReferenceExpression.getObjectType(), enumConstantReferenceExpression.getType());
        assertEquals("MyEnum", enumConstantReferenceExpression.getObjectType().getName()); //$NON-NLS-1$

        // Test the copyTo method
        Expression copiedExpression = enumConstantReferenceExpression.copyTo(2);
        assertTrue(copiedExpression instanceof EnumConstantReferenceExpression);
        assertEquals(2, copiedExpression.getLineNumber());

        // Test toString method
        assertEquals("EnumConstantReferenceExpression{type=ObjectType{MyEnum}, name=ENUM_CONSTANT}", copiedExpression.toString()); //$NON-NLS-1$

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        enumConstantReferenceExpression.accept(visitor);
        assertEquals(1, visitor.getEnumConstantReferenceExpressionCount());
    }
}
