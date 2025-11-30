/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1;

import org.jd.core.v1.api.loader.Loader;
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
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/AnnotatedClass"); //$NON-NLS-1$

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

			assertEquals("java.util.ArrayList", source); //$NON-NLS-1$

			// Check interfaces
			assertNotNull(typeTypes.getInterfaces());
			visitor.reset();
			typeTypes.getInterfaces().accept(visitor);
			source = visitor.toString();

			assertEquals("java.io.Serializable, java.lang.Cloneable", source); //$NON-NLS-1$

			// Check field 'list1'
			//  public List<List<? extends Generic>> list1
			BaseType type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[0]);
			visitor.reset();
			type.accept(visitor);
			source = visitor.toString();

			assertEquals("boolean", source); //$NON-NLS-1$

			type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[1]);
			visitor.reset();
			type.accept(visitor);
			source = visitor.toString();

			assertEquals("byte", source); //$NON-NLS-1$

			type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[2]);
			visitor.reset();
			type.accept(visitor);
			source = visitor.toString();

			assertEquals("short", source); //$NON-NLS-1$

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

			assertEquals("int", source); //$NON-NLS-1$

			// Check return type
			assertNotNull(methodTypes.getReturnedType());

			BaseType returnedType = methodTypes.getReturnedType();
			visitor.reset();
			returnedType.accept(visitor);
			source = visitor.toString();

			assertEquals("int", source); //$NON-NLS-1$

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

			assertEquals("java.lang.String", source); //$NON-NLS-1$

			// Check return type
			assertNotNull(methodTypes.getReturnedType());

			returnedType = methodTypes.getReturnedType();
			visitor.reset();
			returnedType.accept(visitor);
			source = visitor.toString();

			assertEquals("void", source); //$NON-NLS-1$

			// Check exceptions
			assertNotNull(methodTypes.getExceptionTypes());

			visitor.reset();
			methodTypes.getExceptionTypes().accept(visitor);
			source = visitor.toString();

			assertEquals("java.net.UnknownHostException, java.lang.UnsatisfiedLinkError", source); //$NON-NLS-1$
		}
	}

	@Test
	public void testGenericClass() throws Exception {
		PrintTypeVisitor visitor = new PrintTypeVisitor();
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/GenericClass"); //$NON-NLS-1$

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
					"""
					T1, \
					T2, \
					T3 extends org.jd.core.test.AnnotatedClass, \
					T4 extends java.io.Serializable, \
					T5 extends java.io.Serializable & java.lang.Comparable, \
					T6 extends org.jd.core.test.AnnotatedClass & java.io.Serializable & java.lang.Comparable<org.jd.core.test.GenericClass>, \
					T7 extends java.util.Map<?, ?>, \
					T8 extends java.util.Map<? extends java.lang.Number, ? super java.io.Serializable>, \
					T9 extends T8"""; //$NON-NLS-1$

					assertEquals(expected, source);

			// Check super type
			BaseType superType = typeTypes.getSuperType();
			assertNotNull(superType);
			visitor.reset();
			superType.accept(visitor);
			source = visitor.toString();

			assertEquals("java.util.ArrayList<T7>", source); //$NON-NLS-1$

			// Check interfaces
			assertNotNull(typeTypes.getInterfaces());
			visitor.reset();
			typeTypes.getInterfaces().accept(visitor);
			source = visitor.toString();

			assertEquals("java.io.Serializable, java.lang.Comparable<T1>", source); //$NON-NLS-1$

			// Check field 'list1'
			//  public List<List<? extends Generic>> list1
			BaseType type = typeMaker.parseFieldSignature(classFile, classFile.getFields()[0]);
			visitor.reset();
			type.accept(visitor);
			source = visitor.toString();

			assertEquals("java.util.List<java.util.List<? extends org.jd.core.test.GenericClass>>", source); //$NON-NLS-1$

			// Check method 'copy2'
			//  public <T, S extends T> List<? extends Number> copy2(List<? super T> dest, List<S> src) throws InvalidParameterException, ClassCastException
			TypeMaker.MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[3]);

			// Check type parameterTypes
			assertNotNull(methodTypes.getTypeParameters());
			visitor.reset();
			methodTypes.getTypeParameters().accept(visitor);
			source = visitor.toString();

			assertEquals("T, S extends T", source); //$NON-NLS-1$

			// Check parameterTypes
			assertNotNull(methodTypes.getParameterTypes());
			assertEquals(2, methodTypes.getParameterTypes().size());

			type = methodTypes.getParameterTypes().getFirst();
			visitor.reset();
			type.accept(visitor);
			source = visitor.toString();

			assertEquals("java.util.List<? super T>", source); //$NON-NLS-1$

			// Check return type
			assertNotNull(methodTypes.getReturnedType());

			BaseType returnedType = methodTypes.getReturnedType();
			visitor.reset();
			returnedType.accept(visitor);
			source = visitor.toString();

			assertEquals("java.util.List<? extends java.lang.Number>", source); //$NON-NLS-1$

			// Check exceptions
			assertNotNull(methodTypes.getExceptionTypes());

			visitor.reset();
			methodTypes.getExceptionTypes().accept(visitor);
			source = visitor.toString();

			assertEquals("java.security.InvalidParameterException, java.lang.ClassCastException", source); //$NON-NLS-1$

			// Check method 'print'
			//  public <T1, T2 extends Exception> List<? extends Number> print(List<? super T1> list) throws InvalidParameterException, T2
			methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[4]);

			// Check type parameterTypes
			assertNotNull(methodTypes.getTypeParameters());
			visitor.reset();
			methodTypes.getTypeParameters().accept(visitor);
			source = visitor.toString();

			assertEquals("T1, T2 extends java.lang.Exception", source); //$NON-NLS-1$

			// Check parameterTypes
			assertNotNull(methodTypes.getParameterTypes());
			assertEquals(1, methodTypes.getParameterTypes().size());

			type = methodTypes.getParameterTypes().getFirst();
			visitor.reset();
			type.accept(visitor);
			source = visitor.toString();

			assertEquals("java.util.List<? super T1>", source); //$NON-NLS-1$

			// Check return type
			assertNotNull(methodTypes.getReturnedType());

			returnedType = methodTypes.getReturnedType();
			visitor.reset();
			returnedType.accept(visitor);
			source = visitor.toString();

			assertEquals("java.util.List<? extends java.lang.Number>", source); //$NON-NLS-1$

			// Check exceptions
			assertNotNull(methodTypes.getExceptionTypes());

			visitor.reset();
			methodTypes.getExceptionTypes().accept(visitor);
			source = visitor.toString();

			assertEquals("T2, java.security.InvalidParameterException", source); //$NON-NLS-1$
		}
	}

	@Test
	public void testParseReturnedVoid() throws Exception {
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			assertEquals(PrimitiveType.TYPE_VOID, typeMaker.makeMethodTypes("org/jd/core/test/Array", "declarations", "()V").getReturnedType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@Test
	public void testParseReturnedPrimitiveType() throws Exception {
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			assertEquals(PrimitiveType.TYPE_BOOLEAN, typeMaker.makeMethodTypes("org/jd/core/test/annotation/Value", "z", "()Z").getReturnedType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@Test
	public void testParseReturnedStringType() throws Exception {
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			assertEquals(ObjectType.TYPE_STRING, typeMaker.makeMethodTypes("org/jd/core/test/annotation/Name", "value", "()Ljava/lang/String;").getReturnedType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@Test
	public void testGenericInnerClass() throws Exception {
		ClassPathLoader loader = new ClassPathLoader();
		TypeMaker typeMaker = new TypeMaker(loader);

		Type type = typeMaker.makeFromSignature("Lorg/apache/commons/collections4/multimap/AbstractMultiValuedMap<TK;TV;>.AsMap.AsMapEntrySetIterator;"); //$NON-NLS-1$

		assertEquals("Lorg/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator;", type.getDescriptor()); //$NON-NLS-1$
		assertEquals("Lorg/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator;", type.getDescriptor()); //$NON-NLS-1$

		ObjectType ot = (ObjectType)type;

		assertEquals("org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator", ot.getInternalName()); //$NON-NLS-1$
		assertEquals("org.apache.commons.collections4.multimap.AbstractMultiValuedMap.AsMap.AsMapEntrySetIterator", ot.getQualifiedName()); //$NON-NLS-1$
		assertEquals("AsMapEntrySetIterator", ot.getName()); //$NON-NLS-1$
		assertNull(ot.getTypeArguments());

		ot = ((InnerObjectType)ot).getOuterType();

		assertEquals("org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap", ot.getInternalName()); //$NON-NLS-1$
		assertEquals("org.apache.commons.collections4.multimap.AbstractMultiValuedMap.AsMap", ot.getQualifiedName()); //$NON-NLS-1$
		assertEquals("AsMap", ot.getName()); //$NON-NLS-1$
		assertNull(ot.getTypeArguments());

		ot = ((InnerObjectType)ot).getOuterType();

		assertEquals("org/apache/commons/collections4/multimap/AbstractMultiValuedMap", ot.getInternalName()); //$NON-NLS-1$
		assertEquals("org.apache.commons.collections4.multimap.AbstractMultiValuedMap", ot.getQualifiedName()); //$NON-NLS-1$
		assertEquals("AbstractMultiValuedMap", ot.getName()); //$NON-NLS-1$
		assertNotNull(ot.getTypeArguments());

		TypeArguments typeArguments = (TypeArguments)ot.getTypeArguments();

		assertEquals(2, typeArguments.size());
		assertEquals("GenericType{K}", typeArguments.getFirst().toString()); //$NON-NLS-1$
		assertEquals("GenericType{V}", typeArguments.getLast().toString()); //$NON-NLS-1$
	}

	@Test
	public void testInputStream() throws Exception {
		PrintTypeVisitor visitor = new PrintTypeVisitor();
		ClassPathLoader loader = new ClassPathLoader();
		TypeMaker typeMaker = new TypeMaker(loader);

		ClassFile classFile = deserializer.loadClassFile(loader, "java/io/InputStream"); //$NON-NLS-1$

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

		assertEquals("java.io.Closeable", source); //$NON-NLS-1$

		MethodTypes methodTypes = typeMaker.makeMethodTypes("java/io/InputStream", "read", "()I"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// Check exceptions
		assertNotNull(methodTypes.getExceptionTypes());

		visitor.reset();
		methodTypes.getExceptionTypes().accept(visitor);
		source = visitor.toString();

		assertEquals("java.io.IOException", source); //$NON-NLS-1$
	}

	@Test
	public void testAbstractMap() throws Exception {
		PrintTypeVisitor visitor = new PrintTypeVisitor();
		ClassPathLoader loader = new ClassPathLoader();
		TypeMaker typeMaker = new TypeMaker(loader);

		ClassFile classFile = deserializer.loadClassFile(loader, "java/util/AbstractMap"); //$NON-NLS-1$

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

		assertEquals("java.util.Map<K, V>", source); //$NON-NLS-1$
	}

	@Test
	public void testQualityLevel() throws Exception {
		PrintTypeVisitor visitor = new PrintTypeVisitor();
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/annotation/Quality$Level"); //$NON-NLS-1$

			MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[2]);

			// Check parameterTypes
			assertNotNull(methodTypes.getParameterTypes());
			assertEquals(2, methodTypes.getParameterTypes().size());

			BaseType type = methodTypes.getParameterTypes();
			type.accept(visitor);
			String source = visitor.toString();

			assertEquals("java.lang.String, int", source); //$NON-NLS-1$
		}
	}

	@Test
	public void testExceptionTypes() throws Exception {
		PrintTypeVisitor visitor = new PrintTypeVisitor();
		Loader loader = new ClassPathLoader();
		TypeMaker typeMaker = new TypeMaker(loader);

		ClassFile classFile = deserializer.loadClassFile(loader, "org/apache/commons/lang3/function/FailableBooleanSupplier"); //$NON-NLS-1$

		MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[0]);

		// Check exceptionTypes
		assertNotNull(methodTypes.getExceptionTypes());
		assertEquals(1, methodTypes.getExceptionTypes().size());

		BaseType type = methodTypes.getExceptionTypes();
		type.accept(visitor);
		String source = visitor.toString();

		assertEquals("E", source); //$NON-NLS-1$
	}

	@Test
	public void testEnumPlanet() throws Exception {
		PrintTypeVisitor visitor = new PrintTypeVisitor();
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			TypeMaker typeMaker = new TypeMaker(loader);

			ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/Enum$Planet"); //$NON-NLS-1$

			MethodTypes methodTypes = typeMaker.parseMethodSignature(classFile, classFile.getMethods()[2]);

			// Check parameterTypes
			assertNotNull(methodTypes.getParameterTypes());
			assertEquals(4, methodTypes.getParameterTypes().size());

			BaseType type = methodTypes.getParameterTypes();
			type.accept(visitor);
			String source = visitor.toString();

			assertEquals("java.lang.String, int, double, double", source); //$NON-NLS-1$
		}
	}
}
