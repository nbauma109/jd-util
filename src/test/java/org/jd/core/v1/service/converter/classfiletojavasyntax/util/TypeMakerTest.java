/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.jd.core.v1.loader.ClassPathLoader;
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
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.SignatureReader;
import org.jd.core.v1.util.StringConstants;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        assertEquals(2, typeMaker.matchCount("java/lang/Math", "round", 1, false));
    }
}
