package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.util.DefaultList;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseTypeArgumentTest {

    private static class TestBaseTypeArgument extends DefaultList<TypeArgument> implements TypeArgument {

        private static final long serialVersionUID = 1L;

        @Override
        public void accept(TypeArgumentVisitor visitor) {
            visitor.visit(ObjectType.TYPE_OBJECT);
        }
    }

    @Test
    public void testBaseTypeArgument() {
        BaseTypeArgument baseTypeArgument = new TestBaseTypeArgument();

        // Test the overridden accept method
        baseTypeArgument.accept(new AbstractTypeArgumentVisitor() {
            @Override
            public void visit(ObjectType type) {
                assertEquals(ObjectType.TYPE_OBJECT, type);
            }
        });

        // Test isTypeArgumentAssignableFrom
        assertFalse(baseTypeArgument.isTypeArgumentAssignableFrom(null, null, null, null));

        // Test getTypeArgumentFirst
        assertEquals(baseTypeArgument, baseTypeArgument.getTypeArgumentFirst());

        // Test getTypeArgumentList
        assertEquals(baseTypeArgument, baseTypeArgument.getTypeArgumentList());

        // Test isTypeArgumentList
        assertFalse(baseTypeArgument.isTypeArgumentList());

        // Test typeArgumentSize
        assertEquals(1, baseTypeArgument.typeArgumentSize());

        // Test isGenericTypeArgument
        assertFalse(baseTypeArgument.isGenericTypeArgument());

        // Test isInnerObjectTypeArgument
        assertFalse(baseTypeArgument.isInnerObjectTypeArgument());

        // Test isObjectTypeArgument
        assertFalse(baseTypeArgument.isObjectTypeArgument());

        // Test isPrimitiveTypeArgument
        assertFalse(baseTypeArgument.isPrimitiveTypeArgument());

        // Test isWildcardExtendsTypeArgument
        assertFalse(baseTypeArgument.isWildcardExtendsTypeArgument());

        // Test isWildcardSuperTypeArgument
        assertFalse(baseTypeArgument.isWildcardSuperTypeArgument());

        // Test isWildcardTypeArgument
        assertFalse(baseTypeArgument.isWildcardTypeArgument());

        // Test type
        assertEquals(ObjectType.TYPE_UNDEFINED_OBJECT, baseTypeArgument.type());

        // Test findTypeParametersInType
        assertTrue(baseTypeArgument.findTypeParametersInType().isEmpty());

        // Test findTypeParametersInType with generic types
        Set<String> typeParameters = ObjectType.TYPE_ITERABLE.createType(new GenericType("T")).findTypeParametersInType(); //$NON-NLS-1$
        assertFalse(typeParameters.isEmpty());
        assertTrue(typeParameters.contains("T")); //$NON-NLS-1$
    }
}
