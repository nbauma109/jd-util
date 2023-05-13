package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ObjectTypeTest {

    private TypeMaker typeMaker;

    @Before
    public void setUp() {
        typeMaker = new TypeMaker(new ClassPathLoader());
    }

    @Test
    public void testObjectTypeCreation() {
        assertEquals("java.lang.Boolean", ObjectType.TYPE_BOOLEAN.getQualifiedName());
        assertEquals("Boolean", ObjectType.TYPE_BOOLEAN.getName());
        assertEquals("java/lang/Boolean", ObjectType.TYPE_BOOLEAN.getInternalName());
        assertEquals("Ljava/lang/Boolean;", ObjectType.TYPE_BOOLEAN.getDescriptor());
        assertEquals(0, ObjectType.TYPE_BOOLEAN.getDimension());
        assertTrue(ObjectType.TYPE_BOOLEAN.isObjectTypeArgument());
        assertTrue(ObjectType.TYPE_BOOLEAN.isObjectType());
    }

    @Test
    public void testObjectTypeCreationWithDimension() {
        ObjectType typeWithDim = (ObjectType) ObjectType.TYPE_BOOLEAN.createType(2);
        assertEquals("java.lang.Boolean", typeWithDim.getQualifiedName());
        assertEquals("Boolean", typeWithDim.getName());
        assertEquals("java/lang/Boolean", typeWithDim.getInternalName());
        assertEquals("[[Ljava/lang/Boolean;", typeWithDim.getDescriptor());
        assertEquals(2, typeWithDim.getDimension());
        assertTrue(typeWithDim.isObjectTypeArgument());
        assertTrue(typeWithDim.isObjectType());
    }

    @Test
    public void testEqualsAndHashcode() {
        assertEquals(ObjectType.TYPE_BOOLEAN, ObjectType.TYPE_BOOLEAN);
        assertNotEquals(ObjectType.TYPE_BOOLEAN, ObjectType.TYPE_BOOLEAN.createType(2));
        assertNotEquals(ObjectType.TYPE_BOOLEAN, ObjectType.TYPE_BYTE);
        assertNotEquals(ObjectType.TYPE_BOOLEAN, null);
        assertNotEquals(ObjectType.TYPE_BOOLEAN, new Object());

        assertEquals(ObjectType.TYPE_BOOLEAN.hashCode(), ObjectType.TYPE_BOOLEAN.hashCode());
        assertNotEquals(ObjectType.TYPE_BOOLEAN.hashCode(), ObjectType.TYPE_BOOLEAN.createType(2).hashCode());
        assertNotEquals(ObjectType.TYPE_BOOLEAN.hashCode(), ObjectType.TYPE_BYTE.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTypeWithNegativeDimension() {
        ObjectType.TYPE_BOOLEAN.createType(-1);
    }

    @Test
    public void testCreateTypeWithBaseTypeArgument() {
        assertEquals(ObjectType.TYPE_CLASS_WILDCARD, ObjectType.TYPE_CLASS_WILDCARD.createType(WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT));
    }

    @Test
    public void testObjectTypeWithPrimitiveDescriptor() {
        ObjectType typeWithPrimitiveDesc = new ObjectType("I", 2);
        assertEquals("int", typeWithPrimitiveDesc.getQualifiedName());
        assertEquals("int", typeWithPrimitiveDesc.getName());
        assertEquals("I", typeWithPrimitiveDesc.getInternalName());
        assertEquals("[[I", typeWithPrimitiveDesc.getDescriptor());
        assertEquals(2, typeWithPrimitiveDesc.getDimension());
        assertTrue(typeWithPrimitiveDesc.isObjectTypeArgument());
        assertTrue(typeWithPrimitiveDesc.isObjectType());
    }

    @Test
    public void testToString() {
        assertEquals("ObjectType{java/lang/Boolean}", ObjectType.TYPE_BOOLEAN.toString());
        assertEquals("ObjectType{java/lang/Class<Wildcard{?}>, dimension=1}", ObjectType.TYPE_CLASS_WILDCARD.createType(1).toString());
    }

    @Test
    public void testUndefinedObjectTypeToString() {
        assertEquals("UndefinedObjectType", ObjectType.TYPE_UNDEFINED_OBJECT.toString());
    }

    @Test
    public void testConstructors() {
        ObjectType objectType1 = new ObjectType("I", 1);
        assertEquals("I", objectType1.getInternalName());
        assertEquals(1, objectType1.getDimension());

        ObjectType objectType3 = new ObjectType("java/lang/String", "java.lang.String", "String", 2);
        assertEquals("java/lang/String", objectType3.getInternalName());
        assertEquals(2, objectType3.getDimension());
    }

    @Test
    public void testCreateDescriptor() {
        ObjectType objectType1 = new ObjectType("I", 1);
        assertEquals("[I", objectType1.getDescriptor());

        ObjectType objectType2 = new ObjectType("I", 3);
        assertEquals("[[[I", objectType2.getDescriptor());
    }

    @Test
    public void testCreateType() {
        ObjectType objectType1 = (ObjectType) ObjectType.TYPE_BOOLEAN.createType(0);
        assertEquals(ObjectType.TYPE_BOOLEAN, objectType1);

        ObjectType objectType2 = (ObjectType) ObjectType.TYPE_BOOLEAN.createType(1);
        assertNotEquals(ObjectType.TYPE_BOOLEAN, objectType2);
    }

    @Test
    public void testRawEquals() {
        assertTrue(ObjectType.TYPE_BOOLEAN.rawEquals(ObjectType.TYPE_BOOLEAN));
        assertFalse(ObjectType.TYPE_BOOLEAN.rawEquals(ObjectType.TYPE_BYTE));
        assertFalse(ObjectType.TYPE_BOOLEAN.rawEquals(null));
        assertFalse(ObjectType.TYPE_BOOLEAN.rawEquals(PrimitiveType.TYPE_BOOLEAN));
        assertFalse(ObjectType.TYPE_BOOLEAN.rawEquals(ObjectType.TYPE_BOOLEAN.createType(1)));
        assertTrue(ObjectType.TYPE_CLASS_WILDCARD.rawEquals(ObjectType.TYPE_CLASS.createType(ObjectType.TYPE_OBJECT)));
    }

    @Test
    public void testIsTypeArgumentAssignableFrom() {
        assertTrue(ObjectType.TYPE_BOOLEAN.isTypeArgumentAssignableFrom(null, null, null, ObjectType.TYPE_BOOLEAN));
        assertFalse(ObjectType.TYPE_BOOLEAN.isTypeArgumentAssignableFrom(null, null, null, ObjectType.TYPE_BYTE));
        Map<String, TypeArgument> typeBindings = Collections.singletonMap("E", ObjectType.TYPE_ENUM);
        Map<String, BaseType> typeBounds = Collections.singletonMap("E", new ObjectType("java/lang/Enum", "java.lang.Enum", "Enum", new GenericType("E"), 0));
        BaseTypeArgument typeArgument = new GenericType("E");
        boolean result = ObjectType.TYPE_ENUM.isTypeArgumentAssignableFrom(typeMaker, typeBindings, typeBounds, typeArgument);
        assertTrue(result);
    }

    @Test
    public void testIsTypeArgumentAssignableFromWithTypeMakerAndBindingsAndBoundsExtendsWildcard() {
        Map<String, TypeArgument> typeBindings = Collections.singletonMap("D", ObjectType.TYPE_DATE);
        Map<String, BaseType> typeBounds = Collections.singletonMap("D", ObjectType.TYPE_DATE);
        BaseTypeArgument typeArgument = new WildcardExtendsTypeArgument(ObjectType.TYPE_DATE);
        boolean result = ObjectType.TYPE_DATE.isTypeArgumentAssignableFrom(typeMaker, typeBindings, typeBounds, typeArgument);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullInternalName() {
        new ObjectType(null, "java.lang.Integer", "Integer");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInternalNameEndingWithSemicolon() {
        new ObjectType("Ljava/lang/Integer;", "java.lang.Integer", "Integer");
    }

    @Test
    public void testCreateTypeWithPrimitiveTypeAndZeroDimension() {
        ObjectType objectType = new ObjectType("I", 1);
        BaseType baseType = objectType.createType(0);
        assertTrue(baseType.isPrimitiveType());
        assertEquals(PrimitiveType.TYPE_INT, baseType);
    }

    @Test
    public void testCreateTypeWithPrimitiveTypeAndDimension() {
        ObjectType objectType = new ObjectType("I", 1);
        BaseType baseType = objectType.createType(1);
        assertTrue(baseType.isObjectType());
        assertEquals("I", baseType.getInternalName());
    }

    @Test
    public void testEqualsForJavaLangClassWithWildcardTypeArguments() {
        ObjectType objectType1 = new ObjectType("java/lang/Class", "java.lang.Class", "Class", WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT, 0);
        ObjectType objectType2 = new ObjectType("java/lang/Class", "java.lang.Class", "Class", WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT, 0);
        ObjectType objectType3 = new ObjectType("java/lang/Class", "java.lang.Class", "Class", Collections.emptySet(), 0);
        ObjectType objectType4 = new ObjectType("java/lang/Class", "java.lang.Class", "Class", Collections.emptySet(), 0);
        ObjectType objectType5 = new ObjectType("java/lang/Class", "java.lang.Class", "Class", (TypeArgument) null, 0);

        assertEquals(objectType1, objectType2);
        assertEquals(objectType1, objectType3);
        assertEquals(objectType3, objectType4);
        assertEquals(objectType5, objectType1);
        assertEquals(objectType1, objectType5);
        assertEquals(objectType3, objectType5);
        assertEquals(objectType5, objectType5);
        assertNotEquals(objectType1, objectType5.createType(ObjectType.TYPE_OBJECT));
        assertNotEquals(objectType5.createType(ObjectType.TYPE_OBJECT), objectType1);
    }


    @Test
    public void testGetInnerTypeNames() {
        ObjectType objectType = new ObjectType("java/lang/Class", "java.lang.Class", "Class", Collections.singleton("InnerType"), 0);
        assertEquals(Collections.singleton("InnerType"), objectType.getInnerTypeNames());
    }
}
