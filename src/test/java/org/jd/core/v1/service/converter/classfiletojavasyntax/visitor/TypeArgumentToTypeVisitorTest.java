package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.type.DiamondTypeArgument;
import org.jd.core.v1.model.javasyntax.type.GenericType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
import org.jd.core.v1.model.javasyntax.type.WildcardExtendsTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardSuperTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardTypeArgument;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeArgumentToTypeVisitorTest {

    @Test
    public void testTypeArgumentToTypeVisitor() {
        // Arrange
        TypeArgumentToTypeVisitor visitor = new TypeArgumentToTypeVisitor();
        visitor.init();

        // Act & Assert - PrimitiveType
        PrimitiveType primitiveType = PrimitiveType.TYPE_INT;
        primitiveType.accept(visitor);
        assertEquals(primitiveType, visitor.getType());

        // Act & Assert - ObjectType
        ObjectType objectType = ObjectType.TYPE_STRING;
        objectType.accept(visitor);
        assertEquals(objectType, visitor.getType());

        // Act & Assert - InnerObjectType
        ObjectType innerObjectType = new TypeMaker().makeFromInternalTypeName("java/util/Map$Entry");
        innerObjectType.accept(visitor);
        assertEquals(innerObjectType, visitor.getType());

        // Act & Assert - GenericType
        GenericType genericType = new GenericType("T");
        genericType.accept(visitor);
        assertEquals(genericType, visitor.getType());

        // Act & Assert - WildcardExtendsTypeArgument
        WildcardExtendsTypeArgument wildcardExtendsTypeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_OBJECT);
        wildcardExtendsTypeArgument.accept(visitor);
        assertEquals(ObjectType.TYPE_OBJECT, visitor.getType());

        // Act & Assert - WildcardSuperTypeArgument
        WildcardSuperTypeArgument wildcardSuperTypeArgument = new WildcardSuperTypeArgument(ObjectType.TYPE_OBJECT);
        wildcardSuperTypeArgument.accept(visitor);
        assertEquals(ObjectType.TYPE_OBJECT, visitor.getType());

        // Act & Assert - DiamondTypeArgument
        DiamondTypeArgument diamondTypeArgument = DiamondTypeArgument.DIAMOND;
        diamondTypeArgument.accept(visitor);
        assertEquals(ObjectType.TYPE_OBJECT, visitor.getType());

        // Act & Assert - WildcardTypeArgument
        WildcardTypeArgument wildcardTypeArgument = WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT;
        wildcardTypeArgument.accept(visitor);
        assertEquals(ObjectType.TYPE_OBJECT, visitor.getType());

        // Act & Assert - TypeArguments
        TypeArguments typeArguments = new TypeArguments();
        typeArguments.accept(visitor);
        assertEquals(ObjectType.TYPE_UNDEFINED_OBJECT, visitor.getType());
    }
}
