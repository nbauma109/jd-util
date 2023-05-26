package org.jd.core.v1.model.javasyntax.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeParametersTest {

    @Test
    public void test() throws Exception {
        TypeParameters typeParameters = new TypeParameters();
        typeParameters.add(new TypeParameter("K"));
        typeParameters.add(new TypeParameter("V"));
        assertEquals("TypeParameter{identifier=K} & TypeParameter{identifier=V}", typeParameters.toString());
    }
}
