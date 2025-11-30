/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1;

import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.DiamondTypeArgument;
import org.jd.core.v1.model.javasyntax.type.InnerObjectType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
import org.jd.core.v1.model.javasyntax.type.WildcardExtendsTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardSuperTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardTypeArgument;
import org.jd.core.v1.services.javasyntax.type.visitor.PrintTypeVisitor;
import org.junit.Test;

import java.util.Arrays;

import junit.framework.TestCase;

public class TypeTest extends TestCase {
    @Test
    public void testSimpleClassOrInterfaceType() throws Exception {
        BaseType scoit = new ObjectType("org/project/Test", "org.project.Test", "Test"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        PrintTypeVisitor visitor = new PrintTypeVisitor();

        scoit.accept(visitor);

        String source = visitor.toString();

        printSource(source);

        assertEquals("org.project.Test", source); //$NON-NLS-1$
    }

    @Test
    public void testSimpleClassOrInterfaceType2() throws Exception {
        Type scoit1 = new ObjectType("org/project/Test", "org.project.Test", "Test"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Type scoit2 = new ObjectType(
            "org/project/OtherTest", "org.project.OtherTest", "OtherTest", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            new TypeArguments(
                Arrays.asList(
                    scoit1,
                    WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT,
                    new WildcardSuperTypeArgument(scoit1),
                    new WildcardExtendsTypeArgument(scoit1)
                )
            )
        );

        PrintTypeVisitor visitor = new PrintTypeVisitor();
        BaseType baseType2 = scoit2;

        baseType2.accept(visitor);

        String source = visitor.toString();

        printSource(source);

        assertEquals("org.project.OtherTest<org.project.Test, ?, ? super org.project.Test, ? extends org.project.Test>", source); //$NON-NLS-1$
    }

    @Test
    public void testDiamond() throws Exception {
        BaseType scoit = new ObjectType("org/project/Test", "org.project.Test", "Test", DiamondTypeArgument.DIAMOND); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        PrintTypeVisitor visitor = new PrintTypeVisitor();

        scoit.accept(visitor);

        String source = visitor.toString();

        printSource(source);

        assertEquals("org.project.Test<>", source); //$NON-NLS-1$
    }

    @Test
    public void testInnerClass() throws Exception {
        BaseType scoit = new InnerObjectType("org/project/Test$InnerTest", "org.project.Test.InnerTest", "InnerTest", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            new ObjectType("org/project/Test", "org.project.Test", "Test")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        PrintTypeVisitor visitor = new PrintTypeVisitor();

        scoit.accept(visitor);

        String source = visitor.toString();

        printSource(source);

        assertEquals("org.project.Test.InnerTest", source); //$NON-NLS-1$
    }

    protected void printSource(String source) {
        System.out.println("- - - - - - - - "); //$NON-NLS-1$
        System.out.println(source);
        System.out.println("- - - - - - - - "); //$NON-NLS-1$
    }
}