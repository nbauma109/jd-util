package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.reference.AnnotationElementValue;
import org.jd.core.v1.model.javasyntax.reference.AnnotationReference;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormalParameterTest {

    @Test
    public void testFormalParameter() {
        Type type = ObjectType.TYPE_STRING;
        boolean varargs = false;
        String name = "param";

        FormalParameter formalParameter = new FormalParameter(type, varargs, name);

        // Getters
        assertNull(formalParameter.getAnnotationReferences());
        assertFalse(formalParameter.isFinal());
        assertEquals(type, formalParameter.getType());
        assertFalse(formalParameter.isVarargs());
        assertEquals(name, formalParameter.getName());

        // Setters
        formalParameter.setFinal(true);
        formalParameter.setName("newParam");

        assertTrue(formalParameter.isFinal());
        assertEquals("newParam", formalParameter.getName());

        // Accept method
        TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
        formalParameter.accept(testVisitor);

        assertEquals(1, testVisitor.getFormalParameterCount());

        // toString method
        String expectedToString = "FormalParameter{ObjectType{java/lang/String} newParam}";
        assertEquals(expectedToString, formalParameter.toString());
    }

    @Test
    public void testFormalParameterConstructor() {
        Type type = ObjectType.TYPE_INTEGER;
        String name = "param";

        FormalParameter formalParameter = new FormalParameter(type, name);

        // Getters
        assertNull(formalParameter.getAnnotationReferences());
        assertFalse(formalParameter.isFinal());
        assertEquals(type, formalParameter.getType());
        assertFalse(formalParameter.isVarargs());
        assertEquals(name, formalParameter.getName());

        // Setters
        formalParameter.setFinal(true);
        formalParameter.setName("newParam");

        assertTrue(formalParameter.isFinal());
        assertEquals("newParam", formalParameter.getName());

        // Accept method
        TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
        formalParameter.accept(testVisitor);

        assertEquals(1, testVisitor.getFormalParameterCount());

        // toString method
        String expectedToString = "FormalParameter{ObjectType{java/lang/Integer} newParam}";
        assertEquals(expectedToString, formalParameter.toString());
    }

    @Test
    public void testFormalParameterConstructor2() {
        Type type = ObjectType.TYPE_INTEGER.createType(1);
        String name = "param";
        TypeMaker typeMaker = new TypeMaker();
        ObjectType annoType = typeMaker.makeFromInternalTypeName("java/lang/SuppressWarnings");
        AnnotationReference annotationReference = new AnnotationReference(annoType);
        AnnotationElementValue annotationElementValue = new AnnotationElementValue(annotationReference);
        FormalParameter formalParameter = new FormalParameter(annotationElementValue, type, true, name);

        // Getters
        assertNotNull(formalParameter.getAnnotationReferences());
        assertFalse(formalParameter.isFinal());
        assertEquals(type, formalParameter.getType());
        assertTrue(formalParameter.isVarargs());
        assertEquals(name, formalParameter.getName());

        // Setters
        formalParameter.setFinal(true);
        formalParameter.setName("newParam");

        assertTrue(formalParameter.isFinal());
        assertEquals("newParam", formalParameter.getName());

        // Accept method
        TestDeclarationVisitor testVisitor = new TestDeclarationVisitor();
        formalParameter.accept(testVisitor);

        assertEquals(1, testVisitor.getFormalParameterCount());

        // toString method
        String expectedToString = "FormalParameter{AnnotationElementValue{type=ObjectType{java/lang/SuppressWarnings}, elementValue=null, elementValuePairs=null} ObjectType{java/lang/Integer}... newParam}";
        assertEquals(expectedToString, formalParameter.toString());
    }
}
