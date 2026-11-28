package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.type.DiamondTypeArgument;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.WildcardExtendsTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardSuperTypeArgument;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BindTypeArgumentsToTypeArgumentsVisitorTest {

    @Test
    public void testDiamond() {
        BindTypeArgumentsToTypeArgumentsVisitor visitor = new BindTypeArgumentsToTypeArgumentsVisitor();
        visitor.visit(DiamondTypeArgument.DIAMOND);
        assertEquals(DiamondTypeArgument.DIAMOND, visitor.getTypeArgument());
    }

    @Test
    public void testPrimitiveType() {
        BindTypeArgumentsToTypeArgumentsVisitor visitor = new BindTypeArgumentsToTypeArgumentsVisitor();
        visitor.visit(PrimitiveType.TYPE_INT);
        assertEquals(PrimitiveType.TYPE_INT, visitor.getTypeArgument());
    }

    @Test
    public void testWildcardExtendsTypeArgument() {
        BindTypeArgumentsToTypeArgumentsVisitor visitor = new BindTypeArgumentsToTypeArgumentsVisitor();
        WildcardExtendsTypeArgument wildcardExtendsTypeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_OBJECT);
        visitor.visit(wildcardExtendsTypeArgument);
        assertEquals(wildcardExtendsTypeArgument, visitor.getTypeArgument());
    }

    @Test
    public void testWildcardSuperTypeArgument() {
        BindTypeArgumentsToTypeArgumentsVisitor visitor = new BindTypeArgumentsToTypeArgumentsVisitor();
        WildcardSuperTypeArgument wildcardSuperTypeArgument = new WildcardSuperTypeArgument(ObjectType.TYPE_OBJECT);
        visitor.visit(wildcardSuperTypeArgument);
        assertEquals(wildcardSuperTypeArgument, visitor.getTypeArgument());
    }
}
