package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.type.DiamondTypeArgument;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BindTypeArgumentsToTypeArgumentsVisitorTest {

    @Test
    public void testDiamond() throws Exception {
        BindTypeArgumentsToTypeArgumentsVisitor visitor = new BindTypeArgumentsToTypeArgumentsVisitor();
        visitor.visit(DiamondTypeArgument.DIAMOND);
        assertEquals(DiamondTypeArgument.DIAMOND, visitor.getTypeArgument());
    }
    
    @Test
    public void testPrimitiveType() throws Exception {
        BindTypeArgumentsToTypeArgumentsVisitor visitor = new BindTypeArgumentsToTypeArgumentsVisitor();
        visitor.visit(PrimitiveType.TYPE_INT);
        assertEquals(PrimitiveType.TYPE_INT, visitor.getTypeArgument());
    }
}
