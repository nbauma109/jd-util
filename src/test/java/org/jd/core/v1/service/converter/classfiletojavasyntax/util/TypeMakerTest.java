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
import java.util.Objects;
import java.util.PrimitiveIterator;

import static org.junit.Assert.assertThrows;

import junit.framework.TestCase;

@SuppressWarnings("all")
public class TypeMakerTest extends TestCase {
    protected TypeMaker typeMaker = new TypeMaker();

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
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass"); //$NON-NLS-1$

        assertEquals("org/jd/core/test/OuterClass", ot.getInternalName()); //$NON-NLS-1$
        assertEquals("org.jd.core.test.OuterClass", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("OuterClass", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testOuterClass$InnerClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$InnerClass"); //$NON-NLS-1$

        assertEquals("org/jd/core/test/OuterClass$InnerClass", ot.getInternalName()); //$NON-NLS-1$
        assertEquals("org.jd.core.test.OuterClass.InnerClass", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("InnerClass", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testOuterClass$StaticInnerClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$StaticInnerClass"); //$NON-NLS-1$

        assertEquals("org/jd/core/test/OuterClass$StaticInnerClass", ot.getInternalName()); //$NON-NLS-1$
        assertEquals("org.jd.core.test.OuterClass.StaticInnerClass", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("StaticInnerClass", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testOuterClass$1() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$1"); //$NON-NLS-1$

        assertEquals("org/jd/core/test/OuterClass$1", ot.getInternalName()); //$NON-NLS-1$
        assertNull(ot.getQualifiedName());
        assertNull(ot.getName());
    }

    @Test
    public void testOuterClass$1LocalClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$1LocalClass"); //$NON-NLS-1$

        assertEquals("org/jd/core/test/OuterClass$1LocalClass", ot.getInternalName()); //$NON-NLS-1$
        assertNull(ot.getQualifiedName());
        assertEquals("LocalClass", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testThread() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName(StringConstants.JAVA_LANG_THREAD);

        assertEquals(StringConstants.JAVA_LANG_THREAD, ot.getInternalName());
        assertEquals("java.lang.Thread", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("Thread", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testThreadState() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName("java/lang/Thread$State"); //$NON-NLS-1$

        assertEquals("java/lang/Thread$State", ot.getInternalName()); //$NON-NLS-1$
        assertEquals("java.lang.Thread.State", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("State", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testUnknownClass() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/unknown/Class"); //$NON-NLS-1$

        assertEquals("org/unknown/Class", ot.getInternalName()); //$NON-NLS-1$
        assertEquals("org.unknown.Class", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("Class", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testUnknownInnerClass() throws Exception {
        ObjectType ot = typeMaker.makeFromInternalTypeName("org/unknown/Class$InnerClass"); //$NON-NLS-1$

        assertEquals("org/unknown/Class$InnerClass", ot.getInternalName()); //$NON-NLS-1$
        assertEquals("org.unknown.Class.InnerClass", ot.getQualifiedName()); //$NON-NLS-1$
        assertEquals("InnerClass", ot.getName()); //$NON-NLS-1$
    }

    @Test
    public void testListIsAssignableFromArrayList() throws Exception {
        ObjectType parent = typeMaker.makeFromInternalTypeName("java/util/List"); //$NON-NLS-1$
        ObjectType child = typeMaker.makeFromInternalTypeName("java/util/ArrayList"); //$NON-NLS-1$

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
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName(StringConstants.JAVA_LANG_OBJECT);
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$SafeNumberComparator"); //$NON-NLS-1$

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testComparatorIsAssignableFromSafeNumberComparator() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName("java/util/Comparator"); //$NON-NLS-1$
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$SafeNumberComparator"); //$NON-NLS-1$

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testNumberComparatorIsAssignableFromSafeNumberComparator() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$NumberComparator"); //$NON-NLS-1$
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass$SafeNumberComparator"); //$NON-NLS-1$

        assertNotNull(parent);
        assertNotNull(child);
        assertTrue(typeMaker.isAssignable(parent, child));
    }

    @Test
    public void testOuterClassIsAssignableFromSimpleClass() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip"); //$NON-NLS-1$
        TypeMaker typeMaker = new TypeMaker(new ZipLoader(is));
        ObjectType parent = typeMaker.makeFromInternalTypeName("org/jd/core/test/OuterClass"); //$NON-NLS-1$
        ObjectType child = typeMaker.makeFromInternalTypeName("org/jd/core/test/SimpleClass"); //$NON-NLS-1$

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
        assertFalse(TypeMaker.isAClassTypeSignature(new SignatureReader("X"))); //$NON-NLS-1$

        // Case: Signature starts with 'L' but no class name
        assertTrue(TypeMaker.isAClassTypeSignature(new SignatureReader("L;"))); //$NON-NLS-1$

        // Case: Signature with a class name without TypeArguments
        assertTrue(TypeMaker.isAClassTypeSignature(new SignatureReader("Ljava/lang/String;"))); //$NON-NLS-1$

        // Case: Signature with a class name with TypeArguments
        assertTrue(TypeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;"))); //$NON-NLS-1$

        // Case: Signature with multiple class names separated by '.' without TypeArguments
        assertTrue(TypeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry;"))); //$NON-NLS-1$

        // Case: Signature with multiple class names separated by '.' with TypeArguments
        assertTrue(TypeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry<Ljava/lang/String;Ljava/lang/Integer;>;"))); //$NON-NLS-1$

        // Case: Class name starting with a digit
        assertTrue(TypeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.1Entry;"))); //$NON-NLS-1$

        // Case: Signature with class name but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/lang/String"))); //$NON-NLS-1$

        // Case: Signature with class name and TypeArguments but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer"))); //$NON-NLS-1$

        // Case: Signature with multiple class names separated by '.' and TypeArguments but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Entry.Map<Ljava/lang/String;Ljava/lang/Integer"))); //$NON-NLS-1$

        // Case: Class name starting with a digit but missing valid end marker
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Entry.1Map"))); //$NON-NLS-1$

        // Case: Signature with TypeArguments but missing closing '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;;"))); //$NON-NLS-1$

        // Case: Signature with multiple class names separated by '.' with TypeArguments but missing closing '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.isAClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry<Ljava/lang/String;Ljava/lang/Integer;;"))); //$NON-NLS-1$

        // Case: '+' as an extends wildcard followed by a reference type
        SignatureReader reader = new SignatureReader("Ljava/util/List<+Ljava/lang/Number;>;"); //$NON-NLS-1$
        assertTrue(TypeMaker.isAClassTypeSignature(reader));

        // Case: '-' as a super wildcard followed by a reference type
        reader = new SignatureReader("Ljava/util/List<-Ljava/lang/Number;>;"); //$NON-NLS-1$
        assertTrue(TypeMaker.isAClassTypeSignature(reader));

        // Case: '*' as an unbounded wildcard
        reader = new SignatureReader("Ljava/util/List<*>;"); //$NON-NLS-1$
        assertTrue(TypeMaker.isAClassTypeSignature(reader));

        // Case: Type argument starts with a valid reference type signature (ClassTypeSignature)
        reader = new SignatureReader("Ljava/util/List<Ljava/lang/String;>;"); //$NON-NLS-1$
        assertTrue(TypeMaker.isAClassTypeSignature(reader));

        // Case: Type argument starts with a valid reference type signature (ArrayTypeSignature)
        reader = new SignatureReader("Ljava/util/List<[I>;"); //$NON-NLS-1$
        assertTrue(TypeMaker.isAClassTypeSignature(reader));
    }

    @Test
    public void testParseClassTypeSignature() {
        // Case: Signature doesn't start with 'L'
        assertNull(typeMaker.parseClassTypeSignature(new SignatureReader("X"), 0)); //$NON-NLS-1$

        // Case: Signature starts with 'L' but no class name
        assertNotNull(typeMaker.parseClassTypeSignature(new SignatureReader("L;"), 0)); //$NON-NLS-1$

        // Case: Signature with a class name without TypeArguments
        ObjectType objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String;"), 0); //$NON-NLS-1$
        assertNotNull(objectType);
        assertEquals("java.lang.String", objectType.getQualifiedName()); //$NON-NLS-1$

        // Case: Signature with a class name with TypeArguments
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;"), 0); //$NON-NLS-1$
        assertNotNull(objectType);

        // Case: Signature with multiple class names separated by '.' without TypeArguments
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Entry.Map;"), 0); //$NON-NLS-1$
        assertNotNull(objectType);
        assertEquals("java.util.Entry.Map", objectType.getQualifiedName()); //$NON-NLS-1$

        // Case: Signature with multiple class names separated by '.' with TypeArguments
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Entry.Map<Ljava/lang/String;Ljava/lang/Integer;>;"), 0); //$NON-NLS-1$
        assertNotNull(objectType);

        // Case: Class name starting with a digit
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Entry.1Map;"), 0); //$NON-NLS-1$
        assertNotNull(objectType);
        assertNull(objectType.getQualifiedName());

        // Case: Dimension greater than 0
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String;"), 2); //$NON-NLS-1$
        assertNotNull(objectType);

        // Case: Dimension greater than 2
        objectType = typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String;"), 3); //$NON-NLS-1$
        assertNotNull(objectType);

        // Case: Signature ends unexpectedly after 'L'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/lang/String"), 0)); //$NON-NLS-1$

        // Case: Signature has TypeArgument but does not close it with '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;;"), 0)); //$NON-NLS-1$

        // Case: Signature ends unexpectedly after '.'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry"), 0)); //$NON-NLS-1$

        // Case: Signature has TypeArgument in ClassTypeSignatureSuffix but does not close it with '>'
        assertThrows(SignatureFormatException.class, () ->
                typeMaker.parseClassTypeSignature(new SignatureReader("Ljava/util/Map.Entry<Ljava/lang/String;Ljava/lang/Integer;;"), 0)); //$NON-NLS-1$

        // Case: '+' stands for an extends wildcard
        SignatureReader reader = new SignatureReader("Ljava/util/List<+Ljava/lang/Number;>;"); //$NON-NLS-1$
        Type result = typeMaker.parseClassTypeSignature(reader, 0);
        assertTrue(result.isObjectType());
        assertEquals("java.util.List", ((ObjectType) result).getQualifiedName()); //$NON-NLS-1$
        assertTrue(((ObjectType) result).getTypeArguments().isWildcardExtendsTypeArgument());
        TypeArgument typeArgument = ((ObjectType) result).getTypeArguments().getTypeArgumentFirst();
        assertTrue(typeArgument.isWildcardExtendsTypeArgument());
        assertEquals("java.lang.Number", ((ObjectType) ((WildcardExtendsTypeArgument) typeArgument).type()).getQualifiedName()); //$NON-NLS-1$

        // Case: '-' stands for a super wildcard
        reader = new SignatureReader("Ljava/util/List<-Ljava/lang/Number;>;"); //$NON-NLS-1$
        result = typeMaker.parseClassTypeSignature(reader, 0);
        assertTrue(result.isObjectType());
        assertEquals("java.util.List", ((ObjectType) result).getQualifiedName()); //$NON-NLS-1$
        assertTrue(((ObjectType) result).getTypeArguments().isWildcardSuperTypeArgument());
        typeArgument = ((ObjectType) result).getTypeArguments().getTypeArgumentFirst();
        assertTrue(typeArgument.isWildcardSuperTypeArgument());
        assertEquals("java.lang.Number", ((ObjectType) ((WildcardSuperTypeArgument) typeArgument).type()).getQualifiedName()); //$NON-NLS-1$

        // Case: '*' stands for an unbounded wildcard
        reader = new SignatureReader("Ljava/util/List<*>;"); //$NON-NLS-1$
        result = typeMaker.parseClassTypeSignature(reader, 0);
        assertTrue(result.isObjectType());
        assertEquals("java.util.List", ((ObjectType) result).getQualifiedName()); //$NON-NLS-1$
        assertTrue(((ObjectType) result).getTypeArguments().isWildcardTypeArgument());
        typeArgument = ((ObjectType) result).getTypeArguments().getTypeArgumentFirst();
        assertTrue(typeArgument.isWildcardTypeArgument());
    }

    @Test
    public void testIsAReferenceTypeSignature() {
        // Test when reader is empty
        assertFalse(TypeMaker.isAReferenceTypeSignature(new SignatureReader(""))); //$NON-NLS-1$

        // Test when reader's first character is '[' and subsequent character is each of 'B', 'C', 'D', 'F', 'I', 'J', 'S', 'V', 'Z'
        assertTrue(TypeMaker.isAReferenceTypeSignature(new SignatureReader("[B"))); //$NON-NLS-1$

        // Test when reader's first character is 'L' and rest of signature is a valid class type signature
        assertTrue(TypeMaker.isAReferenceTypeSignature(new SignatureReader("Ljava/lang/String;"))); //$NON-NLS-1$

        // Test when reader's first character is 'T'
        assertTrue(TypeMaker.isAReferenceTypeSignature(new SignatureReader("T"))); //$NON-NLS-1$

        // Test when reader's first character is not 'L', 'T', or any of the primitive type characters
        assertFalse(TypeMaker.isAReferenceTypeSignature(new SignatureReader("X"))); //$NON-NLS-1$
    }

    @Test
    public void testParseReferenceTypeSignature() {
        // ClassTypeSignature
        assertObjectType("Ljava/lang/String;", "java.lang.String", 0); //$NON-NLS-1$ //$NON-NLS-2$

        // ArrayTypeSignature - for each primitive type
        assertObjectType("[I", "int", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[J", "long", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[Z", "boolean", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[B", "byte", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[C", "char", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[D", "double", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[F", "float", 1); //$NON-NLS-1$ //$NON-NLS-2$
        assertObjectType("[S", "short", 1); //$NON-NLS-1$ //$NON-NLS-2$

        // PrimitiveTypeSignatures
        assertPrimitiveType("I", "int", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("J", "long", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("Z", "boolean", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("B", "byte", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("C", "char", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("D", "double", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("F", "float", 0); //$NON-NLS-1$ //$NON-NLS-2$
        assertPrimitiveType("S", "short", 0); //$NON-NLS-1$ //$NON-NLS-2$

        // TypeVariableSignature
        assertGenericType("TT;", "T", 0); //$NON-NLS-1$ //$NON-NLS-2$

        // Case where ';' is not found for a TypeVariableSignature
        SignatureReader reader = new SignatureReader("TIdentifier"); //$NON-NLS-1$
        Type result = typeMaker.parseReferenceTypeSignature(reader);
        assertNull(result);

        // Case where BaseType is 'V'
        reader = new SignatureReader("V"); //$NON-NLS-1$
        result = typeMaker.parseReferenceTypeSignature(reader);
        assertTrue(result.isPrimitiveType());
        assertEquals("void", ((PrimitiveType) result).getName()); //$NON-NLS-1$

        // Case where c is not any of the expected BaseType or ClassType
        reader = new SignatureReader("X"); //$NON-NLS-1$
        result = typeMaker.parseReferenceTypeSignature(reader);
        assertNull(result);

        // Case where SignatureReader has no more characters available
        reader = new SignatureReader(""); //$NON-NLS-1$
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
        SignatureReader reader = new SignatureReader("T:"); //$NON-NLS-1$
        TypeParameter result = typeMaker.parseTypeParameter(reader);
        assertEquals("T", result.getIdentifier()); //$NON-NLS-1$
        assertTrue(result instanceof TypeParameter);

        // Case: TypeParameter with a single ClassBound
        reader = new SignatureReader("T:Ljava/lang/String;"); //$NON-NLS-1$
        result = typeMaker.parseTypeParameter(reader);
        assertEquals("T", result.getIdentifier()); //$NON-NLS-1$
        assertTrue(result instanceof TypeParameterWithTypeBounds);
        assertEquals("java.lang.String", ((ObjectType) ((TypeParameterWithTypeBounds) result).getTypeBounds()).getQualifiedName()); //$NON-NLS-1$

        // Case: TypeParameter with multiple InterfaceBounds
        reader = new SignatureReader("T::Ljava/lang/Runnable;:Ljava/lang/Serializable;"); //$NON-NLS-1$
        result = typeMaker.parseTypeParameter(reader);
        assertEquals("T", result.getIdentifier()); //$NON-NLS-1$
        assertTrue(result instanceof TypeParameterWithTypeBounds);
        // Assuming getTypeBounds() returns a BaseType collection, and it can have multiple bounds
        assertEquals(2, ((TypeParameterWithTypeBounds) result).getTypeBounds().size());
        assertEquals("java.lang.Runnable", ((ObjectType) ((TypeParameterWithTypeBounds) result).getTypeBounds().getFirst()).getQualifiedName()); //$NON-NLS-1$
        assertEquals("java.lang.Serializable", ((ObjectType) ((TypeParameterWithTypeBounds) result).getTypeBounds().getLast()).getQualifiedName()); //$NON-NLS-1$
    }

    @Test
    public void testMatch() throws Exception {
        Map<String, TypeArgument> typeBindings = Collections.emptyMap();
        Map<String, BaseType> typeBounds = Collections.emptyMap();
        assertTrue(typeMaker.match(typeBindings, typeBounds, ObjectType.TYPE_OBJECT, new GenericType("T", 1))); //$NON-NLS-1$
    }

    @Test
    public void testMatchCount() throws Exception {
        assertEquals(2, typeMaker.matchCount(StringConstants.JAVA_LANG_MATH, "round", 1, false)); //$NON-NLS-1$
        assertEquals(8, typeMaker.matchCount(StringConstants.JAVA_LANG_STRING, "valueOf", 1, false)); //$NON-NLS-1$
        assertEquals(1, typeMaker.matchCount("java/util/concurrent/DelayQueue", "add", 1, false)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testMatchCountWith1Arg() throws Exception {

        // Prepare some test data
        Map<String, TypeArgument> typeBindings = Collections.emptyMap();
        Map<String, BaseType> typeBounds = Collections.emptyMap();

        BaseExpression parameters = new StringConstantExpression("Hello World"); //$NON-NLS-1$

        // Call the method
        int count = typeMaker.matchCount(typeBindings, typeBounds, "java/io/PrintStream", "println", parameters, false); //$NON-NLS-1$ //$NON-NLS-2$

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
        int count = typeMaker.matchCount(typeBindings, typeBounds, "java/lang/String", "indexOf", parameters, false); //$NON-NLS-1$ //$NON-NLS-2$

        // Verify the result
        assertEquals(1, count);
    }

    @Test
    public void testMatchCountWith3Args() throws Exception {

        // Prepare some test data
        Map<String, TypeArgument> typeBindings = Collections.emptyMap();
        Map<String, BaseType> typeBounds = Collections.emptyMap();

        String internalTypeName = "org/apache/commons/lang3/builder/ToStringStyle"; //$NON-NLS-1$
        String name = "appendDetail"; //$NON-NLS-1$
        Collection<Expression> arguments = new ArrayList<Expression>();
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
        ObjectType hashMap = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/HashMap"); //$NON-NLS-1$
        ObjectType treeMap = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/TreeMap"); //$NON-NLS-1$
        ObjectType abstMap = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/AbstractMap"); //$NON-NLS-1$
        ObjectType set = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/Set"); //$NON-NLS-1$
        ObjectType genericSet = set.createType(new GenericType("V")); //$NON-NLS-1$
        ObjectType unmodifiableEntrySet = typeMaker.makeFromInternalTypeName("org/apache/commons/collections4/map/UnmodifiableEntrySet"); //$NON-NLS-1$
        ObjectType mapEntry = typeMaker.makeFromDescriptorOrInternalTypeName("java/util/Map$Entry"); //$NON-NLS-1$
        ObjectType valueView = typeMaker.makeFromInternalTypeName("org/apache/commons/collections4/bidimap/TreeBidiMap$ValueView"); //$NON-NLS-1$
        TypeArguments typeArguments = new TypeArguments(Arrays.asList(new GenericType("K"), new GenericType("V"))); //$NON-NLS-1$ //$NON-NLS-2$
        mapEntry = mapEntry.createType(typeArguments);
        unmodifiableEntrySet = unmodifiableEntrySet.createType(typeArguments);
        ObjectType mapEntrySet = set.createType(mapEntry);
        assertEquals(abstMap, typeMaker.searchSuperParameterizedType(abstMap, hashMap));
        assertEquals(abstMap, typeMaker.searchSuperParameterizedType(abstMap, treeMap));
        assertEquals(treeMap, typeMaker.searchSuperParameterizedType(ObjectType.TYPE_UNDEFINED_OBJECT, treeMap));
        assertEquals(treeMap, typeMaker.searchSuperParameterizedType(ObjectType.TYPE_OBJECT, treeMap));
        assertEquals(ObjectType.TYPE_CLASS, typeMaker.searchSuperParameterizedType(ObjectType.TYPE_CLASS, ObjectType.TYPE_CLASS));
        assertNull(typeMaker.searchSuperParameterizedType((ObjectType) ObjectType.TYPE_CLASS.createType(1), ObjectType.TYPE_CLASS));
        assertNull(typeMaker.searchSuperParameterizedType(ObjectType.TYPE_CLASS, (ObjectType) ObjectType.TYPE_CLASS.createType(1)));
        assertEquals(mapEntrySet, typeMaker.searchSuperParameterizedType(set, unmodifiableEntrySet));
        assertEquals(set, typeMaker.searchSuperParameterizedType(genericSet, valueView));
    }

    @Test
    public void testMakeFromDescriptorOrInternalTypeName() throws Exception {
        ObjectType hashMap = typeMaker.makeFromDescriptorOrInternalTypeName("[Ljava/util/HashMap;"); //$NON-NLS-1$
        ObjectType treeMap = typeMaker.makeFromDescriptorOrInternalTypeName("[Ljava/util/TreeMap;"); //$NON-NLS-1$
        ObjectType abstMap = typeMaker.makeFromDescriptorOrInternalTypeName("[Ljava/util/AbstractMap;"); //$NON-NLS-1$
        assertNull(typeMaker.searchSuperParameterizedType(abstMap, hashMap));
        assertNull(typeMaker.searchSuperParameterizedType(abstMap, treeMap));
        assertEquals(ObjectType.TYPE_PRIMITIVE_INT.createType(3), typeMaker.makeFromDescriptorOrInternalTypeName("[[[I")); //$NON-NLS-1$
    }

    @Test
    public void testMakeFromDescriptor() throws Exception {
        assertEquals(ObjectType.TYPE_PRIMITIVE_INT, typeMaker.makeFromDescriptor("I")); //$NON-NLS-1$
        assertEquals(typeMaker.makeFromDescriptorOrInternalTypeName("java/util/TreeMap"), typeMaker.makeFromDescriptor("Ljava/util/TreeMap;")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testCountDimension() {
        assertEquals(0, TypeMaker.countDimension("java.lang.String")); //$NON-NLS-1$
        assertEquals(1, TypeMaker.countDimension("[java.lang.String")); //$NON-NLS-1$
        assertEquals(3, TypeMaker.countDimension("[[[java.lang.String")); //$NON-NLS-1$
        assertEquals(0, TypeMaker.countDimension("java[.lang.String")); //$NON-NLS-1$
        assertEquals(0, TypeMaker.countDimension("java.lang.String[]")); //$NON-NLS-1$
        assertEquals(0, TypeMaker.countDimension("")); //$NON-NLS-1$
    }

    @Test
    public void testMakeFromSignatureOrInternalTypeName() {
        Type type;

        // Test with null
        try {
            typeMaker.makeFromSignatureOrInternalTypeName(null);
            fail("Expected an IllegalArgumentException to be thrown"); //$NON-NLS-1$
        } catch (IllegalArgumentException e) {
            assertEquals("ObjectTypeMaker.makeFromSignatureOrInternalTypeName(signatureOrInternalTypeName) : invalid signatureOrInternalTypeName", e.getMessage()); //$NON-NLS-1$
        }

        // Test with internal type name
        type = typeMaker.makeFromSignatureOrInternalTypeName("java/lang/String"); //$NON-NLS-1$
        assertTrue(type instanceof ObjectType);
        assertEquals("java.lang.String", ((ObjectType) type).getQualifiedName()); //$NON-NLS-1$

        // Test with signature starting with [
        type = typeMaker.makeFromSignatureOrInternalTypeName("[Ljava/lang/String;"); //$NON-NLS-1$
        assertTrue(type instanceof ObjectType);
        assertEquals("java.lang.String", ((ObjectType) type).getQualifiedName()); //$NON-NLS-1$
        assertEquals(1, ((ObjectType) type).getDimension());

        // Test with signature ending with ;
        type = typeMaker.makeFromSignatureOrInternalTypeName("Ljava/lang/String;"); //$NON-NLS-1$
        assertTrue(type instanceof ObjectType);
        assertEquals("java.lang.String", ((ObjectType) type).getQualifiedName()); //$NON-NLS-1$
    }


    @Test
    public void testCreateWithInternalTypeName() {
        assertNull(TypeMaker.create(null));
        // Test with standard class
        ObjectType objectType = TypeMaker.create("java/lang/String"); //$NON-NLS-1$
        assertEquals("java.lang.String", objectType.getQualifiedName()); //$NON-NLS-1$
        assertTrue(objectType.isObjectType());

        // Test with inner class
        ObjectType innerObjectType = TypeMaker.create("java/util/Map$Entry"); //$NON-NLS-1$
        assertEquals("java.util.Map.Entry", innerObjectType.getQualifiedName()); //$NON-NLS-1$
        assertTrue(innerObjectType.isObjectType());

        // Test with class name ending with $
        ObjectType dollarObjectType = TypeMaker.create("my/package/TestClass$"); //$NON-NLS-1$
        assertEquals("my.package.TestClass$", dollarObjectType.getQualifiedName()); //$NON-NLS-1$
        assertTrue(dollarObjectType.isObjectType());

        // Test with inner name starting with digit
        ObjectType digitObjectType = TypeMaker.create("my/package/TestClass$1InnerClass"); //$NON-NLS-1$
        assertNull(digitObjectType.getQualifiedName());
        assertEquals("InnerClass", digitObjectType.getName()); //$NON-NLS-1$
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
        ObjectType arrayStringType = typeMaker.makeFromDescriptor("[Ljava/lang/String;"); //$NON-NLS-1$
        assertFalse(typeMaker.isRawTypeAssignable(arrayStringType, ObjectType.TYPE_STRING));
        assertFalse(typeMaker.isRawTypeAssignable(ObjectType.TYPE_STRING, arrayStringType));

        // other special cases
        ObjectType left = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/util/StringMap"); //$NON-NLS-1$
        ObjectType right = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/util/SortedArrayStringMap"); //$NON-NLS-1$
        assertTrue(typeMaker.isRawTypeAssignable(left, right));
    }

    @Test
    public void testMakeFromInternalTypeNameException() {
        assertThrows(IllegalArgumentException.class, () -> typeMaker.makeFromInternalTypeName(null));
        assertThrows(IllegalArgumentException.class, () -> typeMaker.makeFromInternalTypeName("java/lang/String;")); //$NON-NLS-1$
    }

    @Test
    public void testIsAssignable() {
        Map<String, TypeArgument> typeBindings = Collections.singletonMap("D", ObjectType.TYPE_DATE); //$NON-NLS-1$
        Map<String, BaseType> typeBounds = Collections.singletonMap("D", ObjectType.TYPE_DATE); //$NON-NLS-1$

        ObjectType classOfDate = ObjectType.TYPE_CLASS.createType(ObjectType.TYPE_DATE);

        WildcardExtendsTypeArgument wildcardTypeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_DATE);
        ObjectType classOfWildcardDate = ObjectType.TYPE_CLASS.createType(wildcardTypeArgument);

        GenericType genericTypeD = new GenericType("D"); //$NON-NLS-1$
        ObjectType classOfGenericTypeD = ObjectType.TYPE_CLASS.createType(genericTypeD);

        assertTrue(typeMaker.isAssignable(typeBindings, typeBounds, classOfDate, classOfGenericTypeD, classOfWildcardDate));
        assertFalse(typeMaker.isAssignable(ObjectType.TYPE_STRING, ObjectType.TYPE_CLASS_WILDCARD));
    }

    @Test
    public void testIsAssignable2() {
        // Prepare some test data
        Map<String, TypeArgument> typeBindings = new HashMap<String, TypeArgument>();
        typeBindings.put("X", typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/net/MailManager")); //$NON-NLS-1$ //$NON-NLS-2$
        typeBindings.put("Y", WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT); //$NON-NLS-1$

        Map<String, BaseType> typeBounds = new HashMap<String, BaseType>();
        typeBounds.put("X", typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/appender/AbstractManager")); //$NON-NLS-1$ //$NON-NLS-2$

        ObjectType left = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/appender/ManagerFactory"); //$NON-NLS-1$
        ObjectType leftUnbound = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/appender/ManagerFactory"); //$NON-NLS-1$
        left = left.createType(new TypeArguments(Arrays.asList(
                                                    typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/net/MailManager"), //$NON-NLS-1$
                                                    WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT)));
        leftUnbound = leftUnbound.createType(new TypeArguments(Arrays.asList(new GenericType("X"), new GenericType("Y")))); //$NON-NLS-1$ //$NON-NLS-2$

        ObjectType right = typeMaker.makeFromInternalTypeName("org/apache/logging/log4j/core/net/SmtpManager$SMTPManagerFactory"); //$NON-NLS-1$

        // Call the method
        boolean result = typeMaker.isAssignable(typeBindings, typeBounds, left, leftUnbound, right);

        // Verify the result
        assertTrue(result);
    }

    @Test
    public void testIsAssignable3() {
        // Prepare some test data
        GenericType genericType = new GenericType("E"); //$NON-NLS-1$
        Map<String, BaseType> typeBounds = Collections.singletonMap("E", ObjectType.TYPE_ENUM.createType(genericType)); //$NON-NLS-1$

        ObjectType left = typeMaker.makeFromInternalTypeName("java/util/Map"); //$NON-NLS-1$
        BaseTypeArgument leftTypeArguments = new TypeArguments(Arrays.asList(ObjectType.TYPE_STRING, genericType));
        left = left.createType(leftTypeArguments);

        ObjectType right = typeMaker.makeFromInternalTypeName("java/util/LinkedHashMap"); //$NON-NLS-1$
        BaseTypeArgument rightTypeArguments = new TypeArguments(Arrays.asList(ObjectType.TYPE_STRING, genericType));
        right = right.createType(rightTypeArguments);

        // Verify the result
        assertTrue(typeMaker.isAssignable(typeBounds, left, right));
        assertTrue(typeMaker.isAssignable(Collections.emptyMap(), left, right));
    }

    @Test
    public void testIsAssignable4() {
        // Prepare some test data
        ObjectType left = typeMaker.makeFromInternalTypeName("java/util/Set"); //$NON-NLS-1$
        left = left.createType(ObjectType.TYPE_STRING);
        Map<String, BaseType> typeBounds = Collections.emptyMap();
        Map<String, TypeArgument> typeBindings = new HashMap<String, TypeArgument>();
        typeBindings.put("K", ObjectType.TYPE_STRING); //$NON-NLS-1$
        typeBindings.put("V", left); //$NON-NLS-1$

        ObjectType right = typeMaker.makeFromInternalTypeName("java/util/Collection"); //$NON-NLS-1$
        right = right.createType(new WildcardExtendsTypeArgument(ObjectType.TYPE_STRING));

        // Verify the result
        assertFalse(typeMaker.isAssignable(typeBindings, typeBounds, left, right));
    }

    @Test
    public void testIsAssignable5() {
        // Prepare some test data
        ObjectType left = typeMaker.makeFromInternalTypeName("org/apache/commons/collections4/MapIterator"); //$NON-NLS-1$
        GenericType k = new GenericType("K"); //$NON-NLS-1$
        GenericType v = new GenericType("V"); //$NON-NLS-1$
        left = left.createType(new WildcardExtendsTypeArgument(k));
        Map<String, BaseType> typeBounds = Collections.emptyMap();
        Map<String, TypeArgument> typeBindings = new HashMap<String, TypeArgument>();
        typeBindings.put("V", v); //$NON-NLS-1$
        typeBindings.put("K", k); //$NON-NLS-1$

        ObjectType right = typeMaker.makeFromInternalTypeName("org/apache/commons/collections4/MapIterator"); //$NON-NLS-1$
        right = right.createType(new WildcardExtendsTypeArgument(v));

        // Verify the result
        assertFalse(typeMaker.isAssignable(typeBindings, typeBounds, left, right));
    }

    @Test
    public void testIsAssignable6() {
        // Prepare some test data
        ObjectType map = typeMaker.makeFromInternalTypeName("java/util/Map"); //$NON-NLS-1$
        map = map.createType(new TypeArguments(Arrays.asList(WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT, WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT)));
        Map<String, BaseType> typeBounds = Collections.emptyMap();
        Map<String, TypeArgument> typeBindings = Collections.singletonMap("T", new WildcardSuperTypeArgument(map)); //$NON-NLS-1$

        // Verify the result
        assertTrue(typeMaker.isAssignable(typeBindings, typeBounds, map, new GenericType("T"), ObjectType.TYPE_OBJECT)); //$NON-NLS-1$
    }

    @Test
    public void testIsTypeArgumentAssignableFrom() {
        // Prepare some test data
        GenericType k = new GenericType("K"); //$NON-NLS-1$
        GenericType v = new GenericType("V"); //$NON-NLS-1$
        Map<String, BaseType> typeBounds = Collections.emptyMap();
        Map<String, TypeArgument> typeBindings = Map.of("K", k, "V", v); //$NON-NLS-1$ //$NON-NLS-2$

        // Verify the result
        assertTrue(new WildcardExtendsTypeArgument(v).isTypeArgumentAssignableFrom(typeMaker, typeBindings, typeBounds, v));
    }

    @Test
    public void testMakeMethodTypes() throws Exception {
        assertMethodTypes("org/apache/logging/log4j/util/IndexedStringMap", "size", "()I", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                Const.ACC_ABSTRACT | Const.ACC_PUBLIC, PrimitiveType.TYPE_INT, null, null,
                false);

        assertMethodTypes("java/util/List", "forEach", "(Ljava/util/function/Consumer;)V", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                0, PrimitiveType.TYPE_VOID, null,
                Collections.singletonList(
                        new ObjectType("java/util/function/Consumer", "java.util.function.Consumer", "Consumer", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                new WildcardSuperTypeArgument(
                                        new GenericType("E") //$NON-NLS-1$
                                )
                        )
                ), false);

        assertMethodTypes("org/apache/logging/log4j/spi/ThreadContextStack", "addAll", "(Ljava/util/Collection;)Z", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                0, PrimitiveType.TYPE_BOOLEAN, null,
                Collections.singletonList(
                        new ObjectType("java/util/Collection", "java.util.Collection", "Collection", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                new WildcardExtendsTypeArgument(
                                        new ObjectType("java/lang/String", "java.lang.String", "String") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                )
                        )
                ), false);

        assertMethodTypes("java/util/LinkedList", "iterator", "()Ljava/util/Iterator;", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                0, new ObjectType("java/util/Iterator", "java.util.Iterator", "Iterator", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        new GenericType("E")), null, null, false); //$NON-NLS-1$

        assertMethodTypes("org/apache/commons/lang3/ClassUtils", "iterator", "(Ljava/lang/Class;)Ljava/lang/Iterable;", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                0, ObjectType.TYPE_ITERABLE, null,
                Collections.singletonList(ObjectType.TYPE_CLASS), false);

        assertMethodTypes("org/apache/logging/log4j/message/StructuredDataMessage", "appendMap", "(Ljava/lang/StringBuilder;)V", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                0, PrimitiveType.TYPE_VOID, null,
                Collections.singletonList(ObjectType.TYPE_STRING_BUILDER), false);

        assertMethodTypes("java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                Const.ACC_VARARGS | Const.ACC_STATIC | Const.ACC_PUBLIC, ObjectType.TYPE_STRING, null,
                Arrays.asList(
                        ObjectType.TYPE_STRING,
                        new ObjectType("java/lang/Object", "java.lang.Object", "Object", 1) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                ), true);
    }

    private void assertMethodTypes(String className, String methodName, String methodDescriptor,
                                   int expectedAccessFlags, Type expectedReturnType, Type[] expectedExceptionTypes,
                                   List<Type> expectedParameterTypes, boolean expectedIsVarArgs) {
        MethodTypes methodTypes = typeMaker.makeMethodTypes(className, methodName, methodDescriptor);

        assertEquals(expectedAccessFlags, methodTypes.getAccessFlags());
        assertEquals(expectedReturnType, methodTypes.getReturnedType());
        assertEquals(Objects.toString(expectedReturnType), Objects.toString(methodTypes.getReturnedType()));
        assertNull(methodTypes.getExceptionTypes());
        assertEquals(expectedParameterTypes, methodTypes.getParameterTypes());
        assertEquals(Objects.toString(expectedParameterTypes), Objects.toString(methodTypes.getParameterTypes()));
        assertEquals(expectedIsVarArgs, methodTypes.isVarArgs());
    }

    @Test
    public void testHandlePolymorphicSignature() {
        // Arrange
        MethodTypes methodTypes = new MethodTypes();
        String typeName = "java/lang/invoke/MethodHandle"; //$NON-NLS-1$
        String name = "invokeExact"; // A method with @PolymorphicSignature in MethodHandle class //$NON-NLS-1$

        // Act
        methodTypes.handlePolymorphicSignature(typeName, name);

        // Assert
        assertTrue(methodTypes.getReturnedType() instanceof GenericType);
        assertEquals("XXX", ((GenericType) methodTypes.getReturnedType()).getName()); //$NON-NLS-1$
    }

    @Test
    public void testHandlePolymorphicSignatureForVarHandle() {
        // Arrange
        MethodTypes methodTypes = new MethodTypes();
        String typeName = "java/lang/invoke/VarHandle"; //$NON-NLS-1$
        String name = "get"; // A method with @PolymorphicSignature in VarHandle class //$NON-NLS-1$

        // Act
        methodTypes.handlePolymorphicSignature(typeName, name);

        // Assert
        assertTrue(methodTypes.getReturnedType() instanceof GenericType);
        assertEquals("XXX", ((GenericType) methodTypes.getReturnedType()).getName()); //$NON-NLS-1$
    }

    @Test
    public void testSetFieldType() {
        String internalTypeName = "java/lang/String"; //$NON-NLS-1$
        String fieldName = "hash"; //$NON-NLS-1$
        ObjectType type = ObjectType.TYPE_PRIMITIVE_INT;

        typeMaker.setFieldType(internalTypeName, fieldName, type);

        Type fieldtype = typeMaker.makeFieldType(internalTypeName, fieldName, "I"); //$NON-NLS-1$
        assertEquals(type, fieldtype);
    }

    @Test
    public void testSetMethodReturnedType() {
        String internalTypeName = "java/lang/String"; //$NON-NLS-1$
        String methodName = "toString"; //$NON-NLS-1$
        String descriptor = "()Ljava/lang/String;"; //$NON-NLS-1$
        Type type = ObjectType.TYPE_STRING;

        typeMaker.setMethodReturnedType(internalTypeName, methodName, descriptor, type);

        MethodTypes methodTypes = typeMaker.makeMethodTypes(internalTypeName, methodName, descriptor);
        Type returnType = methodTypes.getReturnedType();
        assertEquals(type, returnType);
    }

    @Test
    public void testGetThisType() {
        String internalTypeName = "java/lang/String"; //$NON-NLS-1$
        TypeMaker.TypeTypes typeTypes = typeMaker.makeTypeTypes(internalTypeName);
        ObjectType thisType = typeTypes.getThisType();

        assertNotNull(thisType);
    }

    @Test
    public void testMakeFieldType() throws Exception {
        testMakeFieldType(AbstractDualBidiMap.class, "java/io/ObjectInputStream", "STREAM_MAGIC", "S"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        testMakeFieldType(AbstractDualBidiMap.class, "org/apache/commons/collections4/list/NodeCachingLinkedList", "size", "I"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        testMakeFieldType(AbstractDualBidiMap.class, "org/apache/commons/collections4/bidimap/AbstractDualBidiMap$EntrySet", "parent", "Lorg/apache/commons/collections4/bidimap/AbstractDualBidiMap;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        testMakeFieldType(AbstractFileFilter.class, "org/apache/commons/io/filefilter/AbstractFileFilter", "EMPTY_STRING_ARRAY", "[Ljava/lang/String;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
