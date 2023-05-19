/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.apache.bcel.Const;
import org.apache.commons.collections4.bidimap.AbstractDualBidiMap;
import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.expression.BaseExpression;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.Expressions;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.NullExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeArgument;
import org.jd.core.v1.model.javasyntax.type.GenericType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeArgument;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
import org.jd.core.v1.model.javasyntax.type.TypeParameter;
import org.jd.core.v1.model.javasyntax.type.TypeParameterWithTypeBounds;
import org.jd.core.v1.model.javasyntax.type.WildcardExtendsTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardSuperTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardTypeArgument;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.MethodTypes;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.SignatureReader;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.TypeTypes;
import org.jd.core.v1.util.StringConstants;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;

import static org.junit.Assert.assertThrows;

import junit.framework.TestCase;

@SuppressWarnings("all")
public class TypeMakerTest extends TestCase {
    protected TypeMaker typeMaker = new TypeMaker(new ClassPathLoader());

    protected ObjectType otAbstractUntypedIteratorDecorator = makeObjectType(AbstractUntypedIteratorDecorator.class);
    protected ObjectType otArrayList = makeObjectType(ArrayList.class);
    protected ObjectType otInteger = makeObjectType(Integer.class);
    protected ObjectType otIterator = makeObjectType(Iterator.class);
    protected ObjectType otList = makeObjectType(List.class);
    protected ObjectType otNumber = makeObjectType(Number.class);
    protected ObjectType otPrimitiveIterator = makeObjectType(PrimitiveIterator.class);

    protected ObjectType makeObjectType(Class<?> clazz) {
        return typeMaker.makeFromInternalTypeName(clazz.getName().replace('.', '/'));
    }

    @Test
    public void testOuterClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass");

        assertEquals("org/jd/core/test/OuterClass", ot.getInternalName());
        assertEquals("org.jd.core.test.OuterClass", ot.getQualifiedName());
        assertEquals("OuterClass", ot.getName());
    }

    @Test
    public void testOuterClass$InnerClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$InnerClass");

        assertEquals("org/jd/core/test/OuterClass$InnerClass", ot.getInternalName());
        assertEquals("org.jd.core.test.OuterClass.InnerClass", ot.getQualifiedName());
        assertEquals("InnerClass", ot.getName());
    }

    @Test
    public void testOuterClass$StaticInnerClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$StaticInnerClass");

        assertEquals("org/jd/core/test/OuterClass$StaticInnerClass", ot.getInternalName());
        assertEquals("org.jd.core.test.OuterClass.StaticInnerClass", ot.getQualifiedName());
        assertEquals("StaticInnerClass", ot.getName());
    }

    @Test
    public void testOuterClass$1() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$1");

        assertEquals("org/jd/core/test/OuterClass$1", ot.getInternalName());
        assertNull(ot.getQualifiedName());
        assertNull(ot.getName());
    }

    @Test
    public void testOuterClass$1LocalClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$1LocalClass");

        assertEquals("org/jd/core/test/OuterClass$1LocalClass", ot.getInternalName());
        assertNull(ot.getQualifiedName());
        assertEquals("LocalClass", ot.getName());
    }

    @Test
    public void testThread() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName(StringConstants.JAVA_LANG_THREAD);

        assertEquals(StringConstants.JAVA_LANG_THREAD, ot.getInternalName());
        assertEquals("java.lang.Thread", ot.getQualifiedName());
        assertEquals("Thread", ot.getName());
    }

    @Test
    public void testThreadState() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName("java/lang/Thread$State");

        assertEquals("java/lang/Thread$State", ot.getInternalName());
        assertEquals("java.lang.Thread.State", ot.getQualifiedName());
        assertEquals("State", ot.getName());
    }

    @Test
    public void testUnknownClass() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/unknown/Class");

        assertEquals("org/unknown/Class", ot.getInternalName());
        assertEquals("org.unknown.Class", ot.getQualifiedName());
        assertEquals("Class", ot.getName());
    }

    @Test
    public void testUnknownInnerClass() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/unknown/Class$InnerClass");

        assertEquals("org/unknown/Class$InnerClass", ot.getInternalName());
        assertEquals("org.unknown.Class.InnerClass", ot.getQualifiedName());
        assertEquals("InnerClass", ot.getName());
    }

    @Test
    public void testListIsAssignableFromArrayList() throws Exception {
        ObjectType parent = typeMaker.makeFromInternalTypeName("java/util/List");
        ObjectType child = typeMaker.makeFromInternalTypeName("java/util/ArrayList");

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testClassIsAssignableFromObject() throws Exception {
        ObjectType parent = typeMaker.makeFromInternalTypeName(StringConstants.JAVA_LANG_CLASS);
        ObjectType child = typeMaker.makeFromInternalTypeName(StringConstants.JAVA_LANG_OBJECT);

        assertNotNull(parent);
        assertNotNull(child);
        assertFalse(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testObjectIsAssignableFromSafeNumberComparator() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName(StringConstants.JAVA_LANG_OBJECT);
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$SafeNumberComparator");

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testComparatorIsAssignableFromSafeNumberComparator() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName("java/util/Comparator");
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$SafeNumberComparator");

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testNumberComparatorIsAssignableFromSafeNumberComparator() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$NumberComparator");
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$SafeNumberComparator");

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testOuterClassIsAssignableFromSimpleClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip");
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass");
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/SimpleClass");

        assertNotNull(parent);
        assertNotNull(child);
        assertFalse(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testListAssignment() throws Exception {
        List list1 = null;
        List list2 = null;

        ObjectType ot1 = otList;
        ObjectType ot2 = otList;

        // Valid:   list1 = list2;
        assertTrue(typeMaker.isAssignable(ot1, ot2));

        // Valid:   list2 = list1;
        assertTrue(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testListAndArrayListAssignment() throws Exception {
        List list1 = null;
        List list2 = null;

        ObjectType ot1 = otList;
        ObjectType ot2 = otArrayList;

        // Valid:   list1 = list2;
        assertTrue(typeMaker.isAssignable(ot1, ot2));

        // Invalid: list2 = list1;
        assertFalse(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testListNumberAndArrayListNumberAssignment() throws Exception {
        List<Number> list1 = null;
        List<Number> list2 = null;

        ObjectType ot1 = otList.createType(otNumber);
        ObjectType ot2 = otArrayList.createType(otNumber);

        // Valid:   list1 = list2;
        assertTrue(typeMaker.isAssignable(ot1, ot2));

        // Invalid: list2 = list1;
        assertFalse(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testListNumberAndListIntegerAssignment() throws Exception {
        List<Number> list1 = null;
        List<Integer> list2 = null;

        ObjectType ot1 = otList.createType(otNumber);
        ObjectType ot2 = otList.createType(otInteger);

        // Invalid:   list1 = list2;
        assertFalse(typeMaker.isAssignable(ot1, ot2));

        // Invalid: list2 = list1;
        assertFalse(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testListNumberAndListExtendsNumberAssignment() throws Exception {
        List<Number> list1 = null;
        List<? extends Number> list2 = null;

        ObjectType ot1 = otList.createType(otNumber);
        ObjectType ot2 = otList.createType(new WildcardExtendsTypeArgument(otNumber));

        // Invalid:   list1 = list2;
        assertFalse(typeMaker.isAssignable(ot1, ot2));

        // Valid: list2 = list1;
        assertTrue(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testListNumberAndListSuperNumberAssignment() throws Exception {
        List<Number> list1 = null;
        List<? super Number> list2 = null;

        ObjectType ot1 = otList.createType(otNumber);
        ObjectType ot2 = otList.createType(new WildcardSuperTypeArgument(otNumber));

        // Invalid:   list1 = list2;
        assertFalse(typeMaker.isAssignable(ot1, ot2));

        // Valid: list2 = list1;
        assertTrue(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testListNumberAndArrayListIntegerAssignment() throws Exception {
        List<Number> list1 = null;
        List<Integer> list2 = null;

        ObjectType ot1 = otList.createType(otNumber);
        ObjectType ot2 = otArrayList.createType(otInteger);

        // Invalid:   list1 = list2;
        assertFalse(typeMaker.isAssignable(ot1, ot2));

        // Invalid: list2 = list1;
        assertFalse(typeMaker.isAssignable(ot2, ot1));
    }

    @Test
    public void testArrayNumberAndArrayIntegerAssignment() throws Exception {
        Number[] arr1 = null;
        Integer[] arr2 = null;
        
        ObjectType ot1 = (ObjectType) otNumber.createType(1);
        ObjectType ot2 = (ObjectType) otInteger.createType(1);
        
        // Valid:   arr1 = arr2;
        assertTrue(typeMaker.isAssignable(ot1, ot2));
        
        // Invalid: arr2 = arr1;
        assertFalse(typeMaker.isAssignable(ot2, ot1));
    }
    
    @Test
    public void testIteratorNumberAndPrimitiveIteratorNumberAssignment() throws Exception {
        Iterator<Number> iterator1 = null;
        PrimitiveIterator<Number, List> iterator2 = null;

        TypeArguments tas = new TypeArguments();
        tas.add(otNumber);
        tas.add(otList);

        ObjectType ot1 = otIterator.createType(otNumber);
        ObjectType ot2 = otPrimitiveIterator.createType(tas);

        // Valid:   iterator1 = iterator2;
        assertTrue(typeMaker.isAssignable(ot1, ot2));
    }

    @Test
    public void testIteratorNumberAndAbstractUntypedIteratorDecoratorNumberAssignment() throws Exception {
        Iterator<Number> iterator1 = null;
        AbstractUntypedIteratorDecorator<List, Number> iterator2 = null;

        TypeArguments tas = new TypeArguments();
        tas.add(otList);
        tas.add(otNumber);

        ObjectType ot1 = otIterator.createType(otNumber);
        ObjectType ot2 = otAbstractUntypedIteratorDecorator.createType(tas);

        // Valid:   iterator1 = iterator2;
        assertTrue(typeMaker.isAssignable(ot1, ot2));
    }

    @Test
    public void testIsAClassTypeSignature() {
        // Case: Signature doesn't start with 'L'
        assertFalse(typeMaker.isAClassTypeSignature(new SignatureReader("X")));

        // Case: Signature starts with 'L' but no class name
        assertTrue(typeMaker.isAClassTypeSignature(new SignatureReader("L;")));

        // Case: Signature with a class name without TypeArguments
        assertTrue(typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/lang/String;")));

        // Case: Signature with a class name with TypeArguments
        assertTrue(typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;")));

        // Case: Signature with multiple class names separated by '.' without TypeArguments
        assertTrue(typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry;")));

        // Case: Signature with multiple class names separated by '.' with TypeArguments
        assertTrue(typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry<Ljava/lang/String;Ljava/lang/Integer;>;")));

        // Case: Class name starting with a digit
        assertTrue(typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.1Entry;")));

        // Case: Signature with class name but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/lang/String")));

        // Case: Signature with class name and TypeArguments but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer")));

        // Case: Signature with multiple class names separated by '.' and TypeArguments but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Entry.Map<Ljava/lang/String;Ljava/lang/Integer")));

        // Case: Class name starting with a digit but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Entry.1Map")));

        // Case: Signature with TypeArguments but missing closing '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;;")));

        // Case: Signature with multiple class names separated by '.' with TypeArguments but missing closing '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry<Ljava/lang/String;Ljava/lang/Integer;;")));

        // Case: '+' as an extends wildcard followed by a reference type
        SignatureReader reader = new SignatureReader("Ljava/util/List<+Ljava/lang/Number;>;");
        assertTrue(typeMaker.isAClassTypeSignature(reader));

        // Case: '-' as a super wildcard followed by a reference type
        reader = new SignatureReader("Ljava/util/List<-Ljava/lang/Number;>;");
        assertTrue(typeMaker.isAClassTypeSignature(reader));

        // Case: '*' as an unbounded wildcard
        reader = new SignatureReader("Ljava/util/List<*>;");
        assertTrue(typeMaker.isAClassTypeSignature(reader));

        // Case: Type argument starts with a valid reference type signature (ClassTypeSignature)
        reader = new SignatureReader("Ljava/util/List<Ljava/lang/String;>;");
        assertTrue(typeMaker.isAClassTypeSignature(reader));

        // Case: Type argument starts with a valid reference type signature (ArrayTypeSignature)
        reader = new SignatureReader("Ljava/util/List<[I>;");
        assertTrue(typeMaker.isAClassTypeSignature(reader));
    }

    @Test
    public void testParseClassTypeSignature() {
        // Case: Signature doesn't start with 'L'
        assertNull(typeMaker.parseClassTypeSignature(new SignatureReader("X"), 0));

        // Case: Signature starts with 'L' but no class name
        assertNotNull(typeMaker.parseClassTypeSignature(new SignatureReader("L;"), 0));

        // Case: Signature with a class name without TypeArguments
        ObjectType objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String;"), 0);
        assertNotNull(objectType);
        assertEquals("java.lang.String", objectType.getQualifiedName());

        // Case: Signature with a class name with TypeArguments
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;"), 0);
        assertNotNull(objectType);

        // Case: Signature with multiple class names separated by '.' without TypeArguments
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Entry.Map;"), 0);
        assertNotNull(objectType);
        assertEquals("java.util.Entry.Map", objectType.getQualifiedName());

        // Case: Signature with multiple class names separated by '.' with TypeArguments
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Entry.Map<Ljava/lang/String;Ljava/lang/Integer;>;"), 0);
        assertNotNull(objectType);

        // Case: Class name starting with a digit
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Entry.1Map;"), 0);
        assertNotNull(objectType);
        assertNull(objectType.getQualifiedName());

        // Case: Dimension greater than 0
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String;"), 2);
        assertNotNull(objectType);

        // Case: Dimension greater than 2
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String;"), 3);
        assertNotNull(objectType);

        // Case: Signature ends unexpectedly after 'L'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String"), 0));

        // Case: Signature has TypeArgument but does not close it with '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;;"), 0));

        // Case: Signature ends unexpectedly after '.'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry"), 0));

        // Case: Signature has TypeArgument in ClassTypeSignatureSuffix but does not close it with '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry<Ljava/lang/String;Ljava/lang/Integer;;"), 0));

        // Case: '+' stands for an extends wildcard
        SignatureReader reader = new SignatureReader("Ljava/util/List<+Ljava/lang/Number;>;");
        Type result = typeMaker.parseClassTypeSignature(reader, 0);
        assertTrue(result.isObjectType());
        assertEquals("java.util.List", ((ObjectType) result).getQualifiedName());
        assertTrue(((ObjectType) result).getTypeArguments().isWildcardExtendsTypeArgument());
        TypeArgument typeArgument = ((ObjectType) result).getTypeArguments().getTypeArgumentFirst();
        assertTrue(typeArgument.isWildcardExtendsTypeArgument());
        assertEquals("java.lang.Number", ((ObjectType) ((WildcardExtendsTypeArgument) typeArgument).type()).getQualifiedName());

        // Case: '-' stands for a super wildcard
        reader = new SignatureReader("Ljava/util/List<-Ljava/lang/Number;>;");
        result = typeMaker.parseClassTypeSignature(reader, 0);
        assertTrue(result.isObjectType());
        assertEquals("java.util.List", ((ObjectType) result).getQualifiedName());
        assertTrue(((ObjectType) result).getTypeArguments().isWildcardSuperTypeArgument());
        typeArgument = ((ObjectType) result).getTypeArguments().getTypeArgumentFirst();
        assertTrue(typeArgument.isWildcardSuperTypeArgument());
        assertEquals("java.lang.Number", ((ObjectType) ((WildcardSuperTypeArgument) typeArgument).type()).getQualifiedName());

        // Case: '*' stands for an unbounded wildcard
        reader = new SignatureReader("Ljava/util/List<*>;");
        result = typeMaker.parseClassTypeSignature(reader, 0);
        assertTrue(result.isObjectType());
        assertEquals("java.util.List", ((ObjectType) result).getQualifiedName());
        assertTrue(((ObjectType) result).getTypeArguments().isWildcardTypeArgument());
        typeArgument = ((ObjectType) result).getTypeArguments().getTypeArgumentFirst();
        assertTrue(typeArgument.isWildcardTypeArgument());
    }

    @Test
    public void testIsAReferenceTypeSignature() {
        // Test when reader is empty
        assertFalse(typeMaker.isAReferenceTypeSignature(new SignatureReader("")));

        // Test when reader's first character is '[' and subsequent character is each of 'B', 'C', 'D', 'F', 'I', 'J', 'S', 'V', 'Z'
        assertTrue(typeMaker.isAReferenceTypeSignature(new SignatureReader("[B")));

        // Test when reader's first character is 'L' and rest of signature is a valid class type signature
        assertTrue(typeMaker.isAReferenceTypeSignature(new SignatureReader("Ljava/lang/String;")));

        // Test when reader's first character is 'T'
        assertTrue(typeMaker.isAReferenceTypeSignature(new SignatureReader("T")));

        // Test when reader's first character is not 'L', 'T', or any of the primitive type characters
        assertFalse(typeMaker.isAReferenceTypeSignature(new SignatureReader("X")));
    }

    @Test
    public void testParseReferenceTypeSignature() {
        // ClassTypeSignature
        assertObjectType("Ljava/lang/String;", "java.lang.String", 0);

        // ArrayTypeSignature - for each primitive type
        assertObjectType("[I", "int", 1);
        assertObjectType("[J", "long", 1);
        assertObjectType("[Z", "boolean", 1);
        assertObjectType("[B", "byte", 1);
        assertObjectType("[C", "char", 1);
        assertObjectType("[D", "double", 1);
        assertObjectType("[F", "float", 1);
        assertObjectType("[S", "short", 1);

        // PrimitiveTypeSignatures
        assertPrimitiveType("I", "int", 0);
        assertPrimitiveType("J", "long", 0);
        assertPrimitiveType("Z", "boolean", 0);
        assertPrimitiveType("B", "byte", 0);
        assertPrimitiveType("C", "char", 0);
        assertPrimitiveType("D", "double", 0);
        assertPrimitiveType("F", "float", 0);
        assertPrimitiveType("S", "short", 0);

        // TypeVariableSignature
        assertGenericType("TT;", "T", 0);

        // Case where ';' is not found for a TypeVariableSignature
        SignatureReader reader = new SignatureReader("TIdentifier");
        Type result = typeMaker.parseReferenceTypeSignature(reader);
        assertNull(result);

        // Case where BaseType is 'V'
        reader = new SignatureReader("V");
        result = typeMaker.parseReferenceTypeSignature(reader);
        assertTrue(result.isPrimitiveType());
        assertEquals("void", ((PrimitiveType) result).getName());

        // Case where c is not any of the expected BaseType or ClassType
        reader = new SignatureReader("X");
        result = typeMaker.parseReferenceTypeSignature(reader);
        assertNull(result);

        // Case where SignatureReader has no more characters available
        reader = new SignatureReader("");
        result = typeMaker.parseReferenceTypeSignature(reader);
        assertNull(result);
    }

    private void assertObjectType(String signature, String expectedQualifiedName, int expectedDimension) {
        SignatureReader reader = new SignatureReader(signature);
        Type result = typeMaker.parseReferenceTypeSignature(reader);
        assertTrue(result.isObjectType());
        assertEquals(expectedQualifiedName, ((ObjectType) result).getQualifiedName());
        assertEquals(expectedDimension, result.getDimension());
    }

    private void assertPrimitiveType(String signature, String expectedName, int expectedDimension) {
        SignatureReader reader = new SignatureReader(signature);
        Type result = typeMaker.parseReferenceTypeSignature(reader);
        assertTrue(result.isPrimitiveType());
        assertEquals(expectedName, ((PrimitiveType) result).getName());
        assertEquals(expectedDimension, result.getDimension());
    }

    private void assertGenericType(String signature, String expectedName, int expectedDimension) {
        SignatureReader reader = new SignatureReader(signature);
        Type result = typeMaker.parseReferenceTypeSignature(reader);
        assertTrue(result.isGenericType());
        assertEquals(expectedName, ((GenericType) result).getName());
        assertEquals(expectedDimension, result.getDimension());
    }

    @Test
    public void testParseTypeParameter() {
        // Case: TypeParameter with no bounds
        SignatureReader reader = new SignatureReader("T:");
        TypeParameter result = typeMaker.parseTypeParameter(reader);
        assertEquals("T", result.getIdentifier());
        assertTrue(result instanceof TypeParameter);

        // Case: TypeParameter with a single ClassBound
        reader = new SignatureReader("T:Ljava/lang/String;");
        result = typeMaker.parseTypeParameter(reader);
        assertEquals("T", result.getIdentifier());
        assertTrue(result instanceof TypeParameterWithTypeBounds);
        assertEquals("java.lang.String", ((ObjectType) ((TypeParameterWithTypeBounds) result).getTypeBounds()).getQualifiedName());

        // Case: TypeParameter with multiple InterfaceBounds
        reader = new SignatureReader("T::Ljava/lang/Runnable;:Ljava/lang/Serializable;");
        result = typeMaker.parseTypeParameter(reader);
        assertEquals("T", result.getIdentifier());
        assertTrue(result instanceof TypeParameterWithTypeBounds);
        // Assuming getTypeBounds() returns a BaseType collection, and it can have multiple bounds
        assertEquals(2, ((TypeParameterWithTypeBounds) result).getTypeBounds().size());
        assertEquals("java.lang.Runnable", ((ObjectType) ((TypeParameterWithTypeBounds) result).getTypeBounds().getFirst()).getQualifiedName());
        assertEquals("java.lang.Serializable", ((ObjectType) ((TypeParameterWithTypeBounds) result).getTypeBounds().getLast()).getQualifiedName());
    }

    @Test
    public void testMatchCount() throws Exception {
        assertEquals(2, typeMaker.matchCount(StringConstants.JAVA_LANG_MATH, "round", 1, false));
        assertEquals(8, typeMaker.matchCount(StringConstants.JAVA_LANG_STRING, "valueOf", 1, false));
    }

    @Test
    public void testMatchCountWith1Arg() throws Exception {

        // Prepare some test data
        Map<String, TypeArgument> typeBindings = Collections.emptyMap();
        Map<String, BaseType> typeBounds = Collections.emptyMap();

        BaseExpression parameters = new StringConstantExpression("Hello World");

        // Call the method
        int count = typeMaker.matchCount(typeBindings, typeBounds, "java/io/PrintStream", "println", parameters, false);

        // Verify the result
        assertEquals(2, count);
    }

    @Test
    public void testMatchCountWithPrimitiveArg() throws Exception {

        // Prepare some test data
        Map<String, TypeArgument> typeBindings = Collections.emptyMap();
        Map<String, BaseType> typeBounds = Collections.emptyMap();

        BaseExpression parameters = new IntegerConstantExpression(PrimitiveType.TYPE_CHAR, 1);

        // Call the method
        int count = typeMaker.matchCount(typeBindings, typeBounds, "java/lang/String", "indexOf", parameters, false);

        // Verify the result
        assertEquals(1, count);
    }

    @Test
    public void testMatchCountWith3Args() throws Exception {

        // Prepare some test data
        Map<String, TypeArgument> typeBindings = Collections.emptyMap();
        Map<String, BaseType> typeBounds = Collections.emptyMap();

        String internalTypeName = "org/apache/commons/lang3/builder/ToStringStyle";
        String name = "appendDetail";
        Collection<Expression> arguments = new ArrayList<>();
        arguments.add(new NullExpression(ObjectType.TYPE_STRING_BUFFER));
        arguments.add(new NullExpression(ObjectType.TYPE_STRING));
        arguments.add(new NullExpression(ObjectType.TYPE_OBJECT));
        BaseExpression parameters = new Expressions(arguments);

        // Call the method
        int count = typeMaker.matchCount(typeBindings, typeBounds, internalTypeName, name, parameters, false);

        // Verify the result
        assertEquals(1, count);
    }

    @Test
    public void testSearchSuperParameterizedType() throws Exception {
        ObjectType hashMap = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/HashMap");
        ObjectType treeMap = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/TreeMap");
        ObjectType abstMap = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/AbstractMap");
        ObjectType set = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/Set");
        ObjectType unmodifiableEntrySet = typeMaker.makeFromInternalTypeName("org/apache/commons/collections4/map/UnmodifiableEntrySet");
        ObjectType mapEntry = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/Map$Entry");
        TypeArguments typeArguments = new TypeArguments(Arrays.asList(new GenericType("K"), new GenericType("V")));
        mapEntry = mapEntry.createType(typeArguments);
        unmodifiableEntrySet = unmodifiableEntrySet.createType(typeArguments);
        set = set.createType(mapEntry);
        assertEquals(abstMap, typeMaker.searchSuperParameterizedType(abstMap, hashMap));
        assertEquals(abstMap, typeMaker.searchSuperParameterizedType(abstMap, treeMap));
        assertEquals(treeMap, typeMaker.searchSuperParameterizedType(ObjectType.TYPE_UNDEFINED_OBJECT, treeMap));
        assertEquals(treeMap, typeMaker.searchSuperParameterizedType(ObjectType.TYPE_OBJECT, treeMap));
        assertEquals(ObjectType.TYPE_CLASS, typeMaker.searchSuperParameterizedType(ObjectType.TYPE_CLASS, ObjectType.TYPE_CLASS));
        assertNull(typeMaker.searchSuperParameterizedType((ObjectType) ObjectType.TYPE_CLASS.createType(1), ObjectType.TYPE_CLASS));
        assertNull(typeMaker.searchSuperParameterizedType(ObjectType.TYPE_CLASS, (ObjectType) ObjectType.TYPE_CLASS.createType(1)));
        assertEquals(set, typeMaker.searchSuperParameterizedType(set, unmodifiableEntrySet));
    }
    
    @Test
    public void testMakeFromDescriptorOrInternalTypeName() throws Exception {
        ObjectType hashMap = typeMaker.makeFromDescriptorOrInternalTypeName("[Ljava/util/HashMap;");
        ObjectType treeMap = typeMaker.makeFromDescriptorOrInternalTypeName("[Ljava/util/TreeMap;");
        ObjectType abstMap = typeMaker.makeFromDescriptorOrInternalTypeName("[Ljava/util/AbstractMap;");
        assertNull(typeMaker.searchSuperParameterizedType(abstMap, hashMap));
        assertNull(typeMaker.searchSuperParameterizedType(abstMap, treeMap));
        assertEquals(ObjectType.TYPE_PRIMITIVE_INT.createType(3), typeMaker.makeFromDescriptorOrInternalTypeName("[[[I"));
    }

    @Test
    public void testMakeFromDescriptor() throws Exception {
        assertEquals(ObjectType.TYPE_PRIMITIVE_INT, typeMaker.makeFromDescriptor("I"));
        assertEquals(typeMaker.makeFromDescriptorOrInternalTypeName("java/util/TreeMap"), typeMaker.makeFromDescriptor("Ljava/util/TreeMap;"));
    }

    @Test
    public void testCountDimension() {
        assertEquals(0, TypeMaker.countDimension("java.lang.String"));
        assertEquals(1, TypeMaker.countDimension("[java.lang.String"));
        assertEquals(3, TypeMaker.countDimension("[[[java.lang.String"));
        assertEquals(0, TypeMaker.countDimension("java[.lang.String"));
        assertEquals(0, TypeMaker.countDimension("java.lang.String[]"));
        assertEquals(0, TypeMaker.countDimension(""));
    }

    @Test
    public void testMakeFromSignatureOrInternalTypeName() {
        Type type;

        // Test with null
        try {
            typeMaker.makeFromSignatureOrInternalTypeName(null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ObjectTypeMaker.makeFromSignatureOrInternalTypeName(signatureOrInternalTypeName) : invalid signatureOrInternalTypeName", e.getMessage());
        }

        // Test with internal type name
        type = typeMaker.makeFromSignatureOrInternalTypeName("java/lang/String");
        assertTrue(type instanceof ObjectType);
        assertEquals("java.lang.String", ((ObjectType) type).getQualifiedName());

        // Test with signature starting with [
        type = typeMaker.makeFromSignatureOrInternalTypeName("[Ljava/lang/String;");
        assertTrue(type instanceof ObjectType);
        assertEquals("java.lang.String", ((ObjectType) type).getQualifiedName());
        assertEquals(1, ((ObjectType) type).getDimension());

        // Test with signature ending with ;
        type = typeMaker.makeFromSignatureOrInternalTypeName("Ljava/lang/String;");
        assertTrue(type instanceof ObjectType);
        assertEquals("java.lang.String", ((ObjectType) type).getQualifiedName());
    }


    @Test
    public void testCreateWithInternalTypeName() {
        assertNull(TypeMaker.create(null));
        // Test with standard class
        ObjectType objectType = typeMaker.create("java/lang/String");
        assertEquals("java.lang.String", objectType.getQualifiedName());
        assertTrue(objectType.isObjectType());

        // Test with inner class
        ObjectType innerObjectType = typeMaker.create("java/util/Map$Entry");
        assertEquals("java.util.Map.Entry", innerObjectType.getQualifiedName());
        assertTrue(innerObjectType.isObjectType());

        // Test with class name ending with $
        ObjectType dollarObjectType = typeMaker.create("my/package/TestClass$");
        assertEquals("my.package.TestClass$", dollarObjectType.getQualifiedName());
        assertTrue(dollarObjectType.isObjectType());

        // Test with inner name starting with digit
        ObjectType digitObjectType = typeMaker.create("my/package/TestClass$1InnerClass");
        assertNull(digitObjectType.getQualifiedName());
        assertEquals("InnerClass", digitObjectType.getName());
        assertTrue(digitObjectType.isObjectType());
    }

    @Test
    public void testIsRawTypeAssignable() {
        // Same type
        assertTrue(typeMaker.isRawTypeAssignable(ObjectType.TYPE_STRING, ObjectType.TYPE_STRING));

        // left is parent class, right is child class
        assertTrue(typeMaker.isRawTypeAssignable(ObjectType.TYPE_OBJECT, ObjectType.TYPE_STRING));
        assertTrue(typeMaker.isRawTypeAssignable(ObjectType.TYPE_NUMBER, ObjectType.TYPE_INTEGER));

        // left is child class, right is parent class
        assertFalse(typeMaker.isRawTypeAssignable(ObjectType.TYPE_STRING, ObjectType.TYPE_OBJECT));
        assertFalse(typeMaker.isRawTypeAssignable(ObjectType.TYPE_INTEGER, ObjectType.TYPE_NUMBER));

        // Unrelated classes
        assertFalse(typeMaker.isRawTypeAssignable(ObjectType.TYPE_STRING, ObjectType.TYPE_NUMBER));

        // left is undefined or object
        assertTrue(typeMaker.isRawTypeAssignable(ObjectType.TYPE_UNDEFINED_OBJECT, ObjectType.TYPE_STRING));
        assertTrue(typeMaker.isRawTypeAssignable(ObjectType.TYPE_OBJECT, ObjectType.TYPE_STRING));

        // left and right are equal
        assertTrue(typeMaker.isRawTypeAssignable(ObjectType.TYPE_STRING, ObjectType.TYPE_STRING));

        // left or right have dimensions
        ObjectType arrayStringType = typeMaker.makeFromDescriptor("[Ljava/lang/String;");
        assertFalse(typeMaker.isRawTypeAssignable(arrayStringType, ObjectType.TYPE_STRING));
        assertFalse(typeMaker.isRawTypeAssignable(ObjectType.TYPE_STRING, arrayStringType));

        // other special cases
        ObjectType left = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/util/StringMap");
        ObjectType right = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/util/SortedArrayStringMap");
        assertTrue(typeMaker.isRawTypeAssignable(left, right));
    }

    @Test
    public void testMakeFromInternalTypeNameException() {
        assertThrows(IllegalArgumentException.class, () -> typeMaker.makeFromInternalTypeName(null));
        assertThrows(IllegalArgumentException.class, () -> typeMaker.makeFromInternalTypeName("java/lang/String;"));
    }

    @Test
    public void testIsAssignable() {
        Map<String, TypeArgument> typeBindings = Collections.singletonMap("D", ObjectType.TYPE_DATE);
        Map<String, BaseType> typeBounds = Collections.singletonMap("D", ObjectType.TYPE_DATE);

        ObjectType classOfDate = ObjectType.TYPE_CLASS.createType(ObjectType.TYPE_DATE);

        WildcardExtendsTypeArgument wildcardTypeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_DATE);
        ObjectType classOfWildcardDate = ObjectType.TYPE_CLASS.createType(wildcardTypeArgument);

        GenericType genericTypeD = new GenericType("D");
        ObjectType classOfGenericTypeD = ObjectType.TYPE_CLASS.createType(genericTypeD);

        assertTrue(typeMaker.isAssignable(typeBindings, typeBounds, classOfDate, classOfGenericTypeD, classOfWildcardDate));
        assertFalse(typeMaker.isAssignable(ObjectType.TYPE_STRING, ObjectType.TYPE_CLASS_WILDCARD));
    }

    @Test
    public void testIsAssignable2() {
        // Prepare some test data
        Map<String, TypeArgument> typeBindings = new HashMap<>();
        typeBindings.put("X", typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/net/MailManager"));
        typeBindings.put("Y", WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT);

        Map<String, BaseType> typeBounds = new HashMap<>();
        typeBounds.put("X", typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/appender/AbstractManager"));

        ObjectType left = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/appender/ManagerFactory");
        ObjectType leftUnbound = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/appender/ManagerFactory");
        left = left.createType(new TypeArguments(Arrays.asList(
                                                    typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/net/MailManager"),
                                                    WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT)));
        leftUnbound = leftUnbound.createType(new TypeArguments(Arrays.asList(new GenericType("X"), new GenericType("Y"))));

        ObjectType right = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/net/SmtpManager$SMTPManagerFactory");

        // Call the method
        boolean result = typeMaker.isAssignable(typeBindings, typeBounds, left, leftUnbound, right);

        // Verify the result
        assertTrue(result);
    }

    @Test
    public void testIsAssignable3() {
        // Prepare some test data
        GenericType genericType = new GenericType("E");
        Map<String, BaseType> typeBounds = Collections.singletonMap("E", ObjectType.TYPE_ENUM.createType(genericType));

        ObjectType left = typeMaker.makeFromInternalTypeName("java/util/Map");
        BaseTypeArgument leftTypeArguments = new TypeArguments(Arrays.asList(ObjectType.TYPE_STRING, genericType));
        left = left.createType(leftTypeArguments);

        ObjectType right = typeMaker.makeFromInternalTypeName("java/util/LinkedHashMap");
        BaseTypeArgument rightTypeArguments = new TypeArguments(Arrays.asList(ObjectType.TYPE_STRING, genericType));
        right = right.createType(rightTypeArguments);

        // Verify the result
        assertTrue(typeMaker.isAssignable(typeBounds, left, right));
        assertTrue(typeMaker.isAssignable(Collections.emptyMap(), left, right));
    }

    @Test
    public void testIsAssignable4() {
        // Prepare some test data
        ObjectType left = typeMaker.makeFromInternalTypeName("java/util/Set");
        left = left.createType(ObjectType.TYPE_STRING);
        Map<String, BaseType> typeBounds = Collections.emptyMap();
        Map<String, TypeArgument> typeBindings = new HashMap<>();
        typeBindings.put("K", ObjectType.TYPE_STRING);
        typeBindings.put("V", left);

        ObjectType right = typeMaker.makeFromInternalTypeName("java/util/Collection");
        right = right.createType(new WildcardExtendsTypeArgument(ObjectType.TYPE_STRING));

        // Verify the result
        assertFalse(typeMaker.isAssignable(typeBindings, typeBounds, left, right));
    }

    @Test
    public void testMakeMethodTypes() throws Exception {
        MethodTypes methodTypes = typeMaker.makeMethodTypes("org/apache/logging/log4j/util/IndexedStringMap", "size", "()I");
        assertEquals(Const.ACC_ABSTRACT | Const.ACC_PUBLIC, methodTypes.getAccessFlags());
        assertEquals(PrimitiveType.TYPE_INT, methodTypes.getReturnedType());
        assertNull(methodTypes.getExceptionTypes());
        assertNull(methodTypes.getParameterTypes());
        assertNull(methodTypes.getTypeParameters());

        methodTypes = typeMaker.makeMethodTypes("java/util/List", "forEach", "(Ljava/util/function/Consumer;)V");
        assertEquals(0, methodTypes.getAccessFlags());
        assertEquals(PrimitiveType.TYPE_VOID, methodTypes.getReturnedType());
        assertNull(methodTypes.getExceptionTypes());
        assertNotNull(methodTypes.getParameterTypes());
        assertEquals("[ObjectType{java/util/function/Consumer<WildcardSuperTypeArgument{? super GenericType{E}}>}]",
                   methodTypes.getParameterTypes().toString());
        assertNull(methodTypes.getTypeParameters());

        methodTypes = typeMaker.makeMethodTypes("org/apache/logging/log4j/spi/ThreadContextStack", "addAll", "(Ljava/util/Collection;)Z");
        assertEquals(0, methodTypes.getAccessFlags());
        assertEquals(PrimitiveType.TYPE_BOOLEAN, methodTypes.getReturnedType());
        assertNull(methodTypes.getExceptionTypes());
        assertNotNull(methodTypes.getParameterTypes());
        assertEquals("[ObjectType{java/util/Collection<WildcardExtendsTypeArgument{? extends ObjectType{java/lang/String}}>}]",
                methodTypes.getParameterTypes().toString());
        assertNull(methodTypes.getTypeParameters());

        methodTypes = typeMaker.makeMethodTypes("java/util/LinkedList", "iterator", "()Ljava/util/Iterator;");
        assertEquals(0, methodTypes.getAccessFlags());
        assertNotNull(methodTypes.getReturnedType());
        assertEquals("ObjectType{java/util/Iterator<GenericType{E}>}", methodTypes.getReturnedType().toString());
        assertNull(methodTypes.getExceptionTypes());
        assertNull(methodTypes.getParameterTypes());
        assertNull(methodTypes.getTypeParameters());
        
        methodTypes = typeMaker.makeMethodTypes("org/apache/commons/lang3/ClassUtils", "iterator", "(Ljava/lang/Class;)Ljava/lang/Iterable;");
        assertEquals(0, methodTypes.getAccessFlags());
        assertNotNull(methodTypes.getReturnedType());
        assertEquals(ObjectType.TYPE_ITERABLE, methodTypes.getReturnedType());
        assertNull(methodTypes.getExceptionTypes());
        assertNotNull(methodTypes.getParameterTypes());
        assertEquals(Collections.singletonList(ObjectType.TYPE_CLASS), methodTypes.getParameterTypes());
        assertNull(methodTypes.getTypeParameters());

        methodTypes = typeMaker.makeMethodTypes("org/apache/logging/log4j/message/StructuredDataMessage", "appendMap", "(Ljava/lang/StringBuilder;)V");
        assertEquals(0, methodTypes.getAccessFlags());
        assertNotNull(methodTypes.getReturnedType());
        assertEquals(PrimitiveType.TYPE_VOID, methodTypes.getReturnedType());
        assertNull(methodTypes.getExceptionTypes());
        assertNotNull(methodTypes.getParameterTypes());
        assertEquals(Collections.singletonList(ObjectType.TYPE_STRING_BUILDER), methodTypes.getParameterTypes());
        assertNull(methodTypes.getTypeParameters());

        methodTypes = typeMaker.makeMethodTypes("java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");
        assertTrue(methodTypes.isVarArgs());
        assertEquals(Const.ACC_VARARGS | Const.ACC_STATIC | Const.ACC_PUBLIC, methodTypes.getAccessFlags());
        assertNotNull(methodTypes.getReturnedType());
        assertEquals(ObjectType.TYPE_STRING, methodTypes.getReturnedType());
        assertNull(methodTypes.getExceptionTypes());
        assertNotNull(methodTypes.getParameterTypes());
        assertEquals("[ObjectType{java/lang/String}, ObjectType{java/lang/Object, dimension=1}]", methodTypes.getParameterTypes().toString());
        assertNull(methodTypes.getTypeParameters());
    }

    @Test
    public void testHandlePolymorphicSignature() {
        // Arrange
        MethodTypes methodTypes = new MethodTypes();
        String typeName = "java/lang/invoke/MethodHandle";
        String name = "invokeExact"; // A method with @PolymorphicSignature in MethodHandle class

        // Act
        methodTypes.handlePolymorphicSignature(typeName, name);

        // Assert
        assertTrue(methodTypes.getReturnedType() instanceof GenericType);
        assertEquals("XXX", ((GenericType) methodTypes.getReturnedType()).getName());
    }

    @Test
    public void testHandlePolymorphicSignatureForVarHandle() {
        // Arrange
        MethodTypes methodTypes = new MethodTypes();
        String typeName = "java/lang/invoke/VarHandle";
        String name = "get"; // A method with @PolymorphicSignature in VarHandle class

        // Act
        methodTypes.handlePolymorphicSignature(typeName, name);

        // Assert
        assertTrue(methodTypes.getReturnedType() instanceof GenericType);
        assertEquals("XXX", ((GenericType) methodTypes.getReturnedType()).getName());
    }

    @Test
    public void testSetFieldType() {
        String internalTypeName = "java/lang/String";
        String fieldName = "hash";
        ObjectType type = ObjectType.TYPE_PRIMITIVE_INT;

        typeMaker.setFieldType(internalTypeName, fieldName, type);

        Type fieldtype = typeMaker.makeFieldType(internalTypeName, fieldName, "I");
        assertEquals(type, fieldtype);
    }

    @Test
    public void testSetMethodReturnedType() {
        String internalTypeName = "java/lang/String";
        String methodName = "toString";
        String descriptor = "()Ljava/lang/String;";
        Type type = ObjectType.TYPE_STRING;

        typeMaker.setMethodReturnedType(internalTypeName, methodName, descriptor, type);

        MethodTypes methodTypes = typeMaker.makeMethodTypes(internalTypeName, methodName, descriptor);
        Type returnType = methodTypes.getReturnedType();
        assertEquals(type, returnType);
    }

    @Test
    public void testGetThisType() {
        String internalTypeName = "java/lang/String";
        TypeMaker.TypeTypes typeTypes = typeMaker.makeTypeTypes(internalTypeName);
        ObjectType thisType = typeTypes.getThisType();

        assertNotNull(thisType);
    }

    @Test
    public void testMakeFieldType() throws Exception {
        testMakeFieldType(AbstractDualBidiMap.class, "java/io/ObjectInputStream", "STREAM_MAGIC", "S");
        testMakeFieldType(AbstractDualBidiMap.class, "org/apache/commons/collections4/list/NodeCachingLinkedList", "size", "I");
        testMakeFieldType(AbstractDualBidiMap.class, "org/apache/commons/collections4/bidimap/AbstractDualBidiMap$EntrySet", "parent", "Lorg/apache/commons/collections4/bidimap/AbstractDualBidiMap;");
        testMakeFieldType(AbstractFileFilter.class, "org/apache/commons/io/filefilter/AbstractFileFilter", "EMPTY_STRING_ARRAY", "[Ljava/lang/String;");
    }

    private void testMakeFieldType(Class<?> clazz, String internalTypeName, String name, String descriptor) throws Exception {
        File file = Paths.get(clazz.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipLoader loader = new ZipLoader(fis);
            TypeMaker typeMaker = new TypeMaker(loader);
            assertNotNull(typeMaker.makeFieldType(internalTypeName, name, descriptor));
        }
    }
}
