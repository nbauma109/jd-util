/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1;

import org.apache.bcel.classfile.Method;
import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.InnerObjectType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.MethodTypes;
import org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;
import org.jd.core.v1.services.javasyntax.type.visitor.PrintTypeVisitor;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;

import java.io.InputStream;

import junit.framework.TestCase;

public class SignatureParserTest extends TestCase {
    protected ClassFileDeserializer deserializer = new ClassFileDeserializer();

    @Test
    public void testAnnotatedClass() throws Exception {
        PrintTypeVisitor visitor = new PrintTypeVisitor();
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/AnnotatedClass");

            // Check type
            TypeMaker.TypeTypes typeTypes = typeMaker.parseClassFileSignature(classFile);

            // Check type parameterTypes
            assertNull(typeTypes.getTypeParameters());

            // Check super type
            assertNotNull(typeTypes.getSuperType());
            visitor.reset();

            BaseType superType = typeTypes.getSuperType();

            superType.accept(visitor);
            String source = visitor.toString();

            assertEquals("java.util.ArrayList", source);

            // Check interfaces
            assertNotNull(typeTypes.getInterfaces());
            visitor.reset();
            typeTypes.getInterfaces().accept(visitor);
            source = visitor.toString();

            assertEquals("java.io.Serializable, java.lang.Cloneable", source);

            // Check field 'list1'
            //  public List<List<? extends Generic>> list1
            BaseType type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[0]);
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();

            assertEquals("boolean", source);

            type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[1]);
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();
            
            assertEquals("byte", source);
            
            type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[2]);
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();
            
            assertEquals("short", source);

            // Check method 'add'
            //  public int add(int i1, int i2)
            TypeMaker.MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[1]);

            // Check type parameterTypes
            assertNull(methodTypes.getTypeParameters());

            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(2, methodTypes.getParameterTypes().size());

            type = methodTypes.getParameterTypes().getFirst();
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();

            assertEquals("int", source);

            // Check return type
            assertNotNull(methodTypes.getReturnedType());

            BaseType returnedType = methodTypes.getReturnedType();
            visitor.reset();
            returnedType.accept(visitor);
            source = visitor.toString();

            assertEquals("int", source);

            // Check exceptions
            assertNull(methodTypes.getExceptionTypes());

            // Check method 'ping'
            //  public void ping(String host) throws UnknownHostException, UnsatisfiedLinkError
            methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[2]);

            // Check type parameterTypes
            assertNull(methodTypes.getTypeParameters());

            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(3, methodTypes.getParameterTypes().size());

            type = methodTypes.getParameterTypes().getList().get(1);
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();

            assertEquals("java.lang.String", source);

            // Check return type
            assertNotNull(methodTypes.getReturnedType());

            returnedType = methodTypes.getReturnedType();
            visitor.reset();
            returnedType.accept(visitor);
            source = visitor.toString();

            assertEquals("void", source);

            // Check exceptions
            assertNotNull(methodTypes.getExceptionTypes());

            visitor.reset();
            methodTypes.getExceptionTypes().accept(visitor);
            source = visitor.toString();

            assertEquals("java.net.UnknownHostException, java.lang.UnsatisfiedLinkError", source);
        }
    }

    @Test
    public void testGenericClass() throws Exception {
        PrintTypeVisitor visitor = new PrintTypeVisitor();
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/GenericClass");

            // Check type
            TypeMaker.TypeTypes typeTypes = typeMaker.parseClassFileSignature(classFile);

            // Check type parameterTypes
            // See "org.jd.core.test.resources.java.Generic"
            //  T1:Ljava/lang/Object;
            //  T2:Ljava/lang/Object;
            //  T3:Lorg/jd/core/v1/test/resources/java/AnnotatedClass;
            //  T4::Ljava/io/Serializable;
            //  T5::Ljava/io/Serializable;:Ljava/lang/Comparable;
            //  T6:Lorg/jd/core/v1/test/resources/java/AnnotatedClass;:Ljava/io/Serializable;:Ljava/lang/Comparable<Lorg/jd/core/v1/test/resources/java/GenericClass;>;
            //  T7::Ljava/util/Map<**>;
            //  T8::Ljava/util/Map<+Ljava/lang/Number;-Ljava/io/Serializable;>;
            //  T9:TT8;
            assertNotNull(typeTypes.getTypeParameters());
            typeTypes.getTypeParameters().accept(visitor);

            String source = visitor.toString();
            String expected =
                    "T1, " +
                            "T2, " +
                            "T3 extends org.jd.core.test.AnnotatedClass, " +
                            "T4 extends java.io.Serializable, " +
                            "T5 extends java.io.Serializable & java.lang.Comparable, " +
                            "T6 extends org.jd.core.test.AnnotatedClass & java.io.Serializable & java.lang.Comparable<org.jd.core.test.GenericClass>, " +
                            "T7 extends java.util.Map<?, ?>, " +
                            "T8 extends java.util.Map<? extends java.lang.Number, ? super java.io.Serializable>, " +
                            "T9 extends T8";

            assertEquals(expected, source);

            // Check super type
            BaseType superType = typeTypes.getSuperType();
            assertNotNull(superType);
            visitor.reset();
            superType.accept(visitor);
            source = visitor.toString();

            assertEquals("java.util.ArrayList<T7>", source);

            // Check interfaces
            assertNotNull(typeTypes.getInterfaces());
            visitor.reset();
            typeTypes.getInterfaces().accept(visitor);
            source = visitor.toString();

            assertEquals("java.io.Serializable, java.lang.Comparable<T1>", source);

            // Check field 'list1'
            //  public List<List<? extends Generic>> list1
            BaseType type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[0]);
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();

            assertEquals("java.util.List<java.util.List<? extends org.jd.core.test.GenericClass>>", source);

            // Check method 'copy2'
            //  public <T, S extends T> List<? extends Number> copy2(List<? super T> dest, List<S> src) throws InvalidParameterException, ClassCastException
            TypeMaker.MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[3]);

            // Check type parameterTypes
            assertNotNull(methodTypes.getTypeParameters());
            visitor.reset();
            methodTypes.getTypeParameters().accept(visitor);
            source = visitor.toString();

            assertEquals("T, S extends T", source);

            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(2, methodTypes.getParameterTypes().size());

            type = methodTypes.getParameterTypes().getFirst();
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();

            assertEquals("java.util.List<? super T>", source);

            // Check return type
            assertNotNull(methodTypes.getReturnedType());

            BaseType returnedType = methodTypes.getReturnedType();
            visitor.reset();
            returnedType.accept(visitor);
            source = visitor.toString();

            assertEquals("java.util.List<? extends java.lang.Number>", source);

            // Check exceptions
            assertNotNull(methodTypes.getExceptionTypes());

            visitor.reset();
            methodTypes.getExceptionTypes().accept(visitor);
            source = visitor.toString();

            assertEquals("java.security.InvalidParameterException, java.lang.ClassCastException", source);

            // Check method 'print'
            //  public <T1, T2 extends Exception> List<? extends Number> print(List<? super T1> list) throws InvalidParameterException, T2
            methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[4]);

            // Check type parameterTypes
            assertNotNull(methodTypes.getTypeParameters());
            visitor.reset();
            methodTypes.getTypeParameters().accept(visitor);
            source = visitor.toString();

            assertEquals("T1, T2 extends java.lang.Exception", source);

            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(1, methodTypes.getParameterTypes().size());

            type = methodTypes.getParameterTypes().getFirst();
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();

            assertEquals("java.util.List<? super T1>", source);

            // Check return type
            assertNotNull(methodTypes.getReturnedType());

            returnedType = methodTypes.getReturnedType();
            visitor.reset();
            returnedType.accept(visitor);
            source = visitor.toString();

            assertEquals("java.util.List<? extends java.lang.Number>", source);

            // Check exceptions
            assertNotNull(methodTypes.getExceptionTypes());

            visitor.reset();
            methodTypes.getExceptionTypes().accept(visitor);
            source = visitor.toString();

            assertEquals("T2, java.security.InvalidParameterException", source);
        }
    }

    @Test
    public void testParseReturnedVoid() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            assertEquals(PrimitiveType.TYPE_VOID, typeMaker.makeMethodTypes("org/jd/core/test/Array", "declarations", "()V").getReturnedType());
        }
    }

    @Test
    public void testParseReturnedPrimitiveType() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            assertEquals(PrimitiveType.TYPE_BOOLEAN, typeMaker.makeMethodTypes("org/jd/core/test/annotation/Value", "z", "()Z").getReturnedType());
        }
    }

    @Test
    public void testParseReturnedStringType() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            assertEquals(ObjectType.TYPE_STRING, typeMaker.makeMethodTypes("org/jd/core/test/annotation/Name", "value", "()Ljava/lang/String;").getReturnedType());
        }
    }

    @Test
    public void testGenericInnerClass() throws Exception {
        ClassPathLoader loader = new ClassPathLoader();
        TypeMaker typeMaker = new TypeMaker(loader);

        Type type = typeMaker.makeFromSignature("Lorg/apache/commons/collections4/multimap/AbstractMultiValuedMap<TK;TV;>.AsMap.AsMapEntrySetIterator;");

        assertEquals("Lorg/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator;", type.getDescriptor());
        assertEquals("Lorg/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator;", type.getDescriptor());

        ObjectType ot = (ObjectType)type;

        assertEquals("org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator", ot.getInternalName());
        assertEquals("org.apache.commons.collections4.multimap.AbstractMultiValuedMap.AsMap.AsMapEntrySetIterator", ot.getQualifiedName());
        assertEquals("AsMapEntrySetIterator", ot.getName());
        assertNull(ot.getTypeArguments());

        ot = ((InnerObjectType)ot).getOuterType();

        assertEquals("org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap", ot.getInternalName());
        assertEquals("org.apache.commons.collections4.multimap.AbstractMultiValuedMap.AsMap", ot.getQualifiedName());
        assertEquals("AsMap", ot.getName());
        assertNull(ot.getTypeArguments());

        ot = ((InnerObjectType)ot).getOuterType();

        assertEquals("org/apache/commons/collections4/multimap/AbstractMultiValuedMap", ot.getInternalName());
        assertEquals("org.apache.commons.collections4.multimap.AbstractMultiValuedMap", ot.getQualifiedName());
        assertEquals("AbstractMultiValuedMap", ot.getName());
        assertNotNull(ot.getTypeArguments());

        TypeArguments typeArguments = (TypeArguments)ot.getTypeArguments();

        assertEquals(2, typeArguments.size());
        assertEquals("GenericType{K}", typeArguments.getFirst().toString());
        assertEquals("GenericType{V}", typeArguments.getLast().toString());
    }

    @Test
    public void testInputStream() throws Exception {
        PrintTypeVisitor visitor = new PrintTypeVisitor();
        ClassPathLoader loader = new ClassPathLoader();
        TypeMaker typeMaker = new TypeMaker(loader);

        ClassFile classFile = deserializer.loadClassFile(loader, "java/io/InputStream");

        // Check type
        TypeMaker.TypeTypes typeTypes = typeMaker.parseClassFileSignature(classFile);

        // Check type parameterTypes
        assertNull(typeTypes.getTypeParameters());

        // Check super type
        assertNull(typeTypes.getSuperType());
        visitor.reset();

        // Check interfaces
        assertNotNull(typeTypes.getInterfaces());
        visitor.reset();
        typeTypes.getInterfaces().accept(visitor);
        String source = visitor.toString();

        assertEquals("java.io.Closeable", source);

        MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[2]);

        // Check exceptions
        assertNotNull(methodTypes.getExceptionTypes());

        visitor.reset();
        methodTypes.getExceptionTypes().accept(visitor);
        source = visitor.toString();

        assertEquals("java.io.IOException", source);

        methodTypes = typeMaker.makeMethodTypes("java/io/InputStream", "read", "()I");

        // Check exceptions
        assertNotNull(methodTypes.getExceptionTypes());

        visitor.reset();
        methodTypes.getExceptionTypes().accept(visitor);
        source = visitor.toString();

        assertEquals("java.io.IOException", source);
    }
    
    @Test
    public void testAbstractMap() throws Exception {
        PrintTypeVisitor visitor = new PrintTypeVisitor();
        ClassPathLoader loader = new ClassPathLoader();
        TypeMaker typeMaker = new TypeMaker(loader);
        
        ClassFile classFile = deserializer.loadClassFile(loader, "java/util/AbstractMap");
        
        // Check type
        TypeMaker.TypeTypes typeTypes = typeMaker.parseClassFileSignature(classFile);
        
        // Check type parameterTypes
        assertNotNull(typeTypes.getTypeParameters());
        
        // Check super type
        assertEquals(ObjectType.TYPE_OBJECT, typeTypes.getSuperType());
        visitor.reset();
        
        // Check interfaces
        assertNotNull(typeTypes.getInterfaces());
        visitor.reset();
        typeTypes.getInterfaces().accept(visitor);
        String source = visitor.toString();
        
        assertEquals("java.util.Map<K, V>", source);
    }
    
    @Test
    public void testQualityLevel() throws Exception {
        PrintTypeVisitor visitor = new PrintTypeVisitor();
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/annotation/Quality$Level");

            MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[2]);
    
            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(2, methodTypes.getParameterTypes().size());
    
            BaseType type = methodTypes.getParameterTypes();
            type.accept(visitor);
            String source = visitor.toString();
    
            assertEquals("java.lang.String, int", source);
        }
    }
    
    @Test
    public void testEnumPlanet() throws Exception {
        PrintTypeVisitor visitor = new PrintTypeVisitor();
        try (InputStream is = getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) {
            ZipLoader loader = new ZipLoader(is);
            TypeMaker typeMaker = new TypeMaker(loader);

            ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/Enum$Planet");
            Method method = classFile.getMethods()[2];
            MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, method);
            
            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(4, methodTypes.getParameterTypes().size());
            
            BaseType type = methodTypes.getParameterTypes();
            type.accept(visitor);
            String source = visitor.toString();
            
            assertEquals("java.lang.String, int, double, double", source);

            methodTypes = typeMaker.makeMethodTypes(classFile.getInternalTypeName(), method.getName(), method.getSignature());

            // Check parameterTypes
            assertNotNull(methodTypes.getParameterTypes());
            assertEquals(4, methodTypes.getParameterTypes().size());
            
            type = methodTypes.getParameterTypes();
            visitor.reset();
            type.accept(visitor);
            source = visitor.toString();
            
            assertEquals("java.lang.String, int, double, double", source);


        }
    }
}
