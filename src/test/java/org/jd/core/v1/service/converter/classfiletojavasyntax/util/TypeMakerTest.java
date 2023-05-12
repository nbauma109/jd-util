/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
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
    }


    @Test
    public void testIsAReferenceTypeSignature() {
        // Test when reader is empty
        assertFalse(typeMaker.isAReferenceTypeSignature(new SignatureReader("")));

        // Test when reader's first character is '[' and subsequent character is each of 'B', 'C', 'D', 'F', 'I', 'J', 'S', 'V', 'Z'
        assertTrue(typeMaker.isAReferenceTypeSignature(new SignatureReader("[B")));
        // Repeat for each character

        // Test when reader's first character is 'L' and rest of signature is a valid class type signature
        assertTrue(typeMaker.isAReferenceTypeSignature(new SignatureReader("Ljava/lang/String;")));

        // Test when reader's first character is 'T'
        assertTrue(typeMaker.isAReferenceTypeSignature(new SignatureReader("T")));

        // Test when reader's first character is not 'L', 'T', or any of the primitive type characters
        assertFalse(typeMaker.isAReferenceTypeSignature(new SignatureReader("X")));
    }

}
