package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.reference.BaseAnnotationReference;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeParameter;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.apache.bcel.Const.ACC_PUBLIC;
import static org.apache.bcel.Const.ACC_STATIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClassDeclarationTest {

    private ClassDeclaration classDeclaration;
    private TestDeclarationVisitor visitor;

    @Before
    public void setUp() {
        visitor = new TestDeclarationVisitor();

        BaseAnnotationReference annotationReferences = null;
        int flags = ACC_PUBLIC | ACC_STATIC;
        String internalName = "org/jd/core/v1/model/test/TestClass";
        String name = "TestClass";
        BaseTypeParameter typeParameters = null;
        ObjectType superType = new ObjectType("java/lang/Object", "java.lang.Object", "Object");
        BaseType interfaces = null;
        BodyDeclaration bodyDeclaration = null;

        classDeclaration = new ClassDeclaration(annotationReferences, flags, internalName, name, typeParameters, superType, interfaces, bodyDeclaration);
    }

    @Test
    public void testClassDeclaration() {
        // Act
        classDeclaration.accept(visitor);

        // Assert
        assertEquals(1, visitor.getClassDeclarationCount());
        assertEquals("org/jd/core/v1/model/test/TestClass", classDeclaration.getInternalTypeName());
        assertTrue(classDeclaration.isClassDeclaration());
        assertTrue(classDeclaration.isStatic());
        assertNotNull(classDeclaration.getSuperType());
        assertEquals("ClassDeclaration{org/jd/core/v1/model/test/TestClass}", classDeclaration.toString());
    }
}
