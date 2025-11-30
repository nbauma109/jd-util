package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeParametersTest {

    @Test
    public void test() throws Exception {
        TypeParameters typeParameters = new TypeParameters();
        typeParameters.add(new TypeParameter("K")); //$NON-NLS-1$
        typeParameters.add(new TypeParameter("V")); //$NON-NLS-1$
        assertEquals("TypeParameter{identifier=K} & TypeParameter{identifier=V}", typeParameters.toString()); //$NON-NLS-1$
    }
}
