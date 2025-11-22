/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Signature;
import org.apache.commons.lang3.Validate;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.model.javasyntax.expression.BaseExpression;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.LambdaIdentifiersExpression;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.BaseTypeArgument;
import org.jd.core.v1.model.javasyntax.type.BaseTypeParameter;
import org.jd.core.v1.model.javasyntax.type.GenericType;
import org.jd.core.v1.model.javasyntax.type.InnerObjectType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeArgument;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
import org.jd.core.v1.model.javasyntax.type.TypeParameter;
import org.jd.core.v1.model.javasyntax.type.TypeParameterWithTypeBounds;
import org.jd.core.v1.model.javasyntax.type.TypeParameters;
import org.jd.core.v1.model.javasyntax.type.UnmodifiableTypes;
import org.jd.core.v1.model.javasyntax.type.WildcardExtendsTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardSuperTypeArgument;
import org.jd.core.v1.model.javasyntax.type.WildcardTypeArgument;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.BindTypesToTypesVisitor;
import org.jd.core.v1.util.StringConstants;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.bcel.Const.CONSTANT_Class;
import static org.apache.bcel.Const.CONSTANT_Double;
import static org.apache.bcel.Const.CONSTANT_Dynamic;
import static org.apache.bcel.Const.CONSTANT_Fieldref;
import static org.apache.bcel.Const.CONSTANT_Float;
import static org.apache.bcel.Const.CONSTANT_Integer;
import static org.apache.bcel.Const.CONSTANT_InterfaceMethodref;
import static org.apache.bcel.Const.CONSTANT_InvokeDynamic;
import static org.apache.bcel.Const.CONSTANT_Long;
import static org.apache.bcel.Const.CONSTANT_MethodHandle;
import static org.apache.bcel.Const.CONSTANT_MethodType;
import static org.apache.bcel.Const.CONSTANT_Methodref;
import static org.apache.bcel.Const.CONSTANT_Module;
import static org.apache.bcel.Const.CONSTANT_NameAndType;
import static org.apache.bcel.Const.CONSTANT_Package;
import static org.apache.bcel.Const.CONSTANT_String;
import static org.apache.bcel.Const.CONSTANT_Utf8;
import static org.jd.core.v1.model.javasyntax.type.ObjectType.TYPE_OBJECT;
import static org.jd.core.v1.model.javasyntax.type.ObjectType.TYPE_UNDEFINED_OBJECT;

import jd.core.CoreConstants;

/**
 * https://jcp.org/aboutJava/communityprocess/maintenance/jsr924/JVMS-SE5.0-Ch4-ClassFile.pdf
 *
 * https://docs.oracle.com/javase/tutorial/extra/generics/methods.html
 *
 * http://www.angelikalanger.com/GenericsFAQ/JavaGenericsFAQ.html
 */
public class TypeMaker {
    private static final Map<String, ObjectType> INTERNALNAME_TO_OBJECTPRIMITIVETYPE = new HashMap<>();

    static {
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_BOOLEAN.getInternalName(), ObjectType.TYPE_PRIMITIVE_BOOLEAN);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_BYTE.getInternalName(),    ObjectType.TYPE_PRIMITIVE_BYTE);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_CHAR.getInternalName(),    ObjectType.TYPE_PRIMITIVE_CHAR);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_DOUBLE.getInternalName(),  ObjectType.TYPE_PRIMITIVE_DOUBLE);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_FLOAT.getInternalName(),   ObjectType.TYPE_PRIMITIVE_FLOAT);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_INT.getInternalName(),     ObjectType.TYPE_PRIMITIVE_INT);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_LONG.getInternalName(),    ObjectType.TYPE_PRIMITIVE_LONG);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_SHORT.getInternalName(),   ObjectType.TYPE_PRIMITIVE_SHORT);
        INTERNALNAME_TO_OBJECTPRIMITIVETYPE.put(ObjectType.TYPE_PRIMITIVE_VOID.getInternalName(),    ObjectType.TYPE_PRIMITIVE_VOID);
    }

    /*
     * Internal type names for which we have already loaded fields and methods
     */
    private static final Map<String, Boolean> loaded = Collections.synchronizedMap(new HashMap<>());

    private static final Map<String, Type> signatureToType = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, Type> internalTypeNameFieldNameToType = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, ObjectType> descriptorToObjectType = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, ObjectType> internalTypeNameToObjectType = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, TypeTypes> internalTypeNameToTypeTypes = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, Set<BaseType>> internalTypeNameMethodNameParameterCountToDeclaredParameterTypes = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, Set<BaseType>> internalTypeNameMethodNameParameterCountToParameterTypes = Collections.synchronizedMap(new HashMap<>(1024));
    private static final Map<String, MethodTypes> internalTypeNameMethodNameDescriptorToMethodTypes = Collections.synchronizedMap(new HashMap<>(1024));
    private final Map<String, MethodTypes> signatureToMethodTypes = new HashMap<>(1024);

    private final Map<Long, Boolean> assignableRawTypes = new HashMap<>(1024);
    private final Map<Long, ObjectType> superParameterizedObjectTypes = new HashMap<>(1024);

    private static final Map<String, String[]> hierarchy = Collections.synchronizedMap(new HashMap<>(1024));
    private static final ClassPathLoader classPathLoader = new ClassPathLoader();
    private final Loader loader;

    public TypeMaker() {
        this(classPathLoader);
    }

    public TypeMaker(Loader loader) {
        this.loader = loader;

        signatureToType.put("B", PrimitiveType.TYPE_BYTE);
        signatureToType.put("C", PrimitiveType.TYPE_CHAR);
        signatureToType.put("D", PrimitiveType.TYPE_DOUBLE);
        signatureToType.put("F", PrimitiveType.TYPE_FLOAT);
        signatureToType.put("I", PrimitiveType.TYPE_INT);
        signatureToType.put("J", PrimitiveType.TYPE_LONG);
        signatureToType.put("S", PrimitiveType.TYPE_SHORT);
        signatureToType.put("V", PrimitiveType.TYPE_VOID);
        signatureToType.put("Z", PrimitiveType.TYPE_BOOLEAN);
        signatureToType.put("Ljava/lang/Class;", ObjectType.TYPE_CLASS);
        signatureToType.put("Ljava/lang/Exception;", ObjectType.TYPE_EXCEPTION);
        signatureToType.put(StringConstants.INTERNAL_OBJECT_SIGNATURE, TYPE_OBJECT);
        signatureToType.put("Ljava/lang/Throwable;", ObjectType.TYPE_THROWABLE);
        signatureToType.put("Ljava/lang/String;", ObjectType.TYPE_STRING);
        signatureToType.put("Ljava/lang/System;", ObjectType.TYPE_SYSTEM);

        descriptorToObjectType.put("Ljava/lang/Class;", ObjectType.TYPE_CLASS);
        descriptorToObjectType.put("Ljava/lang/Exception;", ObjectType.TYPE_EXCEPTION);
        descriptorToObjectType.put(StringConstants.INTERNAL_OBJECT_SIGNATURE, TYPE_OBJECT);
        descriptorToObjectType.put("Ljava/lang/Throwable;", ObjectType.TYPE_THROWABLE);
        descriptorToObjectType.put("Ljava/lang/String;", ObjectType.TYPE_STRING);
        descriptorToObjectType.put("Ljava/lang/System;", ObjectType.TYPE_SYSTEM);

        loadType(StringConstants.JAVA_LANG_BOOLEAN);
        loadType(StringConstants.JAVA_LANG_BYTE);
        loadType(StringConstants.JAVA_LANG_CHARACTER);
        loadType(StringConstants.JAVA_LANG_CLASS);
        loadType(StringConstants.JAVA_LANG_DOUBLE);
        loadType(StringConstants.JAVA_LANG_ENUM);
        loadType(StringConstants.JAVA_LANG_EXCEPTION);
        loadType(StringConstants.JAVA_LANG_FLOAT);
        loadType(StringConstants.JAVA_LANG_INTEGER);
        loadType(StringConstants.JAVA_LANG_ITERABLE);
        loadType(StringConstants.JAVA_LANG_LONG);
        loadType(StringConstants.JAVA_LANG_MATH);
        loadType(StringConstants.JAVA_LANG_NUMBER);
        loadType(StringConstants.JAVA_LANG_OBJECT);
        loadType(StringConstants.JAVA_LANG_RUNTIME_EXCEPTION);
        loadType(StringConstants.JAVA_LANG_SHORT);
        loadType(StringConstants.JAVA_LANG_STRING);
        loadType(StringConstants.JAVA_LANG_STRING_BUFFER);
        loadType(StringConstants.JAVA_LANG_STRING_BUILDER);
        loadType(StringConstants.JAVA_LANG_SYSTEM);
        loadType(StringConstants.JAVA_LANG_THREAD);
        loadType(StringConstants.JAVA_LANG_THROWABLE);
        loadType(StringConstants.JAVA_UTIL_DATE);

    }

    /**
     * Rules:
     *  ClassSignature: TypeParameters? SuperclassSignature SuperInterfaceSignature*
     *  SuperclassSignature: ClassTypeSignature
     *  SuperInterfaceSignature: ClassTypeSignature
     */
    public synchronized TypeTypes parseClassFileSignature(ClassFile classFile) {
        TypeTypes typeTypes = new TypeTypes();
        String internalTypeName = classFile.getInternalTypeName();

        typeTypes.setThisType(makeFromInternalTypeName(internalTypeName));

        Signature attributeSignature = classFile.getAttribute(Const.ATTR_SIGNATURE);

        if (attributeSignature == null) {
            String superTypeName = classFile.getSuperTypeName();
            String[] interfaceTypeNames = classFile.getInterfaceTypeNames();

            if (superTypeName != null  && ! StringConstants.JAVA_LANG_OBJECT.equals(superTypeName)) {
                typeTypes.setSuperType(makeFromInternalTypeName(superTypeName));
            }

            if (interfaceTypeNames != null) {
                int length = interfaceTypeNames.length;

                if (length == 1) {
                    typeTypes.setInterfaces(makeFromInternalTypeName(interfaceTypeNames[0]));
                } else {
                    UnmodifiableTypes list = new UnmodifiableTypes(length);
                    for (String interfaceTypeName : interfaceTypeNames) {
                        list.add(makeFromInternalTypeName(interfaceTypeName));
                    }
                    typeTypes.setInterfaces(list);
                }
            }
        } else {
            // Parse 'signature' attribute
            SignatureReader signatureReader = new SignatureReader(attributeSignature.getSignature());

            typeTypes.setTypeParameters(parseTypeParameters(signatureReader));
            typeTypes.setSuperType(parseClassTypeSignature(signatureReader, 0));

            Type firstInterface = parseClassTypeSignature(signatureReader, 0);

            if (firstInterface != null) {
                Type nextInterface = parseClassTypeSignature(signatureReader, 0);

                if (nextInterface == null) {
                    typeTypes.setInterfaces(firstInterface);
                } else {
                    UnmodifiableTypes list = new UnmodifiableTypes(classFile.getInterfaceTypeNames().length);

                    list.add(firstInterface);

                    do {
                        list.add(nextInterface);
                        nextInterface = parseClassTypeSignature(signatureReader, 0);
                    } while (nextInterface != null);

                    typeTypes.setInterfaces(list);
                }
            }
        }

        internalTypeNameToTypeTypes.put(internalTypeName, typeTypes);

        return typeTypes;
    }

    public synchronized MethodTypes parseMethodSignature(ClassFile classFile, Method method) {
        String key = classFile.getInternalTypeName() + ':' + method.getName() + method.getSignature();
        return parseMethodSignature(classFile.getInternalTypeName(), method, key);
    }

    private MethodTypes parseMethodSignature(String internalTypeName, Method method, String key) {
        Signature attributeSignature = getSignature(method);
        String[] exceptionTypeNames = getExceptionTypeNames(method);
        MethodTypes methodTypes;
        if (attributeSignature == null) {
            methodTypes = parseMethodSignature(internalTypeName, method.getName(), method.getSignature(), exceptionTypeNames);
        } else {
            methodTypes = parseMethodSignature(internalTypeName, method.getName(), method.getSignature(), attributeSignature.getSignature(), exceptionTypeNames);
        }

        internalTypeNameMethodNameDescriptorToMethodTypes.put(key, methodTypes);

        return methodTypes;
    }

    private static String[] getExceptionTypeNames(Method method) {
        if (method != null) {
            ExceptionTable attributeExceptions = method.getExceptionTable();

            if (attributeExceptions != null) {
                int[] exceptionIndexTable = attributeExceptions.getExceptionIndexTable();
                String[] exceptionNames = new String[exceptionIndexTable.length];
                for (int i = 0; i < exceptionNames.length; i++) {
                    exceptionNames[i] = attributeExceptions.getConstantPool().getConstantString(exceptionIndexTable[i], CONSTANT_Class);
                }
                return exceptionNames;
            }
        }

        return null;
    }

    public synchronized Type parseFieldSignature(ClassFile classFile, Field field) {
        String key = classFile.getInternalTypeName() + ':' + field.getName();
        Signature attributeSignature = getSignature(field);
        String signature = attributeSignature == null ? field.getSignature() : attributeSignature.getSignature();
        Type type = makeFromSignature(signature);

        putInternalTypeNameFieldNameToType(key, type);

        return type;
    }

    private static Signature getSignature(FieldOrMethod fm) {
        return (Signature) Stream.of(fm.getAttributes()).filter(Signature.class::isInstance).findAny().orElse(null);
    }

    public synchronized Type makeFromSignature(String signature) {
        return signatureToType.computeIfAbsent(signature, this::parseReferenceTypeSignature);
    }

    public static synchronized int countDimension(String descriptor) {
        int count = 0;

        for (int i=0, len=descriptor.length(); i<len && descriptor.charAt(i)=='['; i++) {
            count++;
        }

        return count;
    }

    private MethodTypes parseMethodSignature(String internalTypeName, String methodName, String descriptor, String signature, String[] exceptionTypeNames) {
        if (signature == null) {
            return parseMethodSignature(internalTypeName, methodName, descriptor, exceptionTypeNames);
        }
        // Signature does not contain synthetic parameters like outer type name, for example.
        MethodTypes mtDescriptor = parseMethodSignature(internalTypeName, methodName, descriptor, exceptionTypeNames);
        MethodTypes mtSignature  = parseMethodSignature(internalTypeName, methodName, signature, exceptionTypeNames);

        if (mtDescriptor.getParameterTypes() != null) {
            if (mtSignature.getParameterTypes() == null) {
                MethodTypes mt = new MethodTypes();

                mt.setTypeParameters(mtSignature.getTypeParameters());
                mt.setParameterTypes(mtDescriptor.getParameterTypes());
                mt.setReturnedType(mtSignature.getReturnedType());
                mt.setExceptionTypes(mtSignature.getExceptionTypes());

                return mt;
            }
            if (mtDescriptor.getParameterTypes().size() != mtSignature.getParameterTypes().size()) {
                UnmodifiableTypes parameterTypes = new UnmodifiableTypes(mtDescriptor.getParameterTypes().getList().subList(
                    0, mtDescriptor.getParameterTypes().size() - mtSignature.getParameterTypes().size()));
                parameterTypes.addAll(mtSignature.getParameterTypes().getList());

                MethodTypes mt = new MethodTypes();

                mt.setTypeParameters(mtSignature.getTypeParameters());
                mt.setParameterTypes(parameterTypes);
                mt.setReturnedType(mtSignature.getReturnedType());
                mt.setExceptionTypes(mtSignature.getExceptionTypes());

                return mt;
            }
        }
        return mtSignature;
    }

    /**
     * Rules:
     *  MethodTypeSignature: TypeParameters? '(' ReferenceTypeSignature* ')' ReturnType ThrowsSignature*
     *  ReturnType: TypeSignature | VoidDescriptor
     *  ThrowsSignature: '^' ClassTypeSignature | '^' TypeVariableSignature
     */
    private MethodTypes parseMethodSignature(String internalTypeName, String methodName, String signature, String[] exceptionTypeNames) {
        String cacheKey = internalTypeName + '.' + methodName + signature;
        boolean containsThrowsSignature = signature.indexOf('^') != -1;

        if (!containsThrowsSignature && exceptionTypeNames != null) {
            StringBuilder sb = new StringBuilder(cacheKey);

            for (String exceptionTypeName : exceptionTypeNames) {
                sb.append("^L").append(exceptionTypeName).append(';');
            }

            cacheKey = sb.toString();
        }

        MethodTypes methodTypes = signatureToMethodTypes.get(cacheKey);

        if (methodTypes == null) {
            SignatureReader reader = new SignatureReader(signature);

            // Type parameters
            methodTypes = new MethodTypes();
            methodTypes.setTypeParameters(parseTypeParameters(reader));

            // Parameters
            ensureReadCharacter(reader, '(');

            Type firstParameterType = parseReferenceTypeSignature(reader);

            if (firstParameterType == null) {
                methodTypes.setParameterTypes(null);
            } else {
                Type nextParameterType = parseReferenceTypeSignature(reader);
                UnmodifiableTypes types = new UnmodifiableTypes();

                types.add(firstParameterType);

                while (nextParameterType != null) {
                    types.add(nextParameterType);
                    nextParameterType = parseReferenceTypeSignature(reader);
                }

                methodTypes.setParameterTypes(types);
            }

            ensureReadCharacter(reader, ')');

            // Result
            methodTypes.setReturnedType(parseReferenceTypeSignature(reader));

            // Exceptions
            Type firstException = parseExceptionSignature(reader);

            if (firstException == null) {
                // Signature does not contain exceptions
                if (exceptionTypeNames != null) {
                    if (exceptionTypeNames.length == 1) {
                        methodTypes.setExceptionTypes(makeFromInternalTypeName(exceptionTypeNames[0]));
                    } else {
                        UnmodifiableTypes list = new UnmodifiableTypes(exceptionTypeNames.length);

                        for (String exceptionTypeName : exceptionTypeNames) {
                            list.add(makeFromInternalTypeName(exceptionTypeName));
                        }

                        methodTypes.setExceptionTypes(list);
                    }
                }
            } else {
                Type nextException = parseExceptionSignature(reader);

                if (nextException == null) {
                    methodTypes.setExceptionTypes(firstException);
                } else {
                    UnmodifiableTypes list = new UnmodifiableTypes();

                    list.add(firstException);

                    do {
                        list.add(nextException);
                        nextException = parseExceptionSignature(reader);
                    } while (nextException != null);

                    methodTypes.setExceptionTypes(list);
                }
            }

            signatureToMethodTypes.put(cacheKey, methodTypes);
        }

        return methodTypes;
    }

    /** Rules: TypeParameters: '<' TypeParameter+ '>' */
    private BaseTypeParameter parseTypeParameters(SignatureReader reader) {
        if (reader.nextEqualsTo('<')) {
            // Skip '<'
            reader.index++;

            TypeParameter firstTypeParameter = parseTypeParameter(reader);

            requireNonNull(reader, firstTypeParameter);

            TypeParameter nextTypeParameter = parseTypeParameter(reader);
            BaseTypeParameter typeParameters;

            if (nextTypeParameter == null) {
                typeParameters = firstTypeParameter;
            } else {
                TypeParameters list = new TypeParameters();
                list.add(firstTypeParameter);

                do {
                    list.add(nextTypeParameter);
                    nextTypeParameter = parseTypeParameter(reader);
                } while (nextTypeParameter != null);

                typeParameters = list;
            }

            ensureReadCharacter(reader, '>');

            return typeParameters;
        }
        return null;
    }

    /**
     * Rules:
     *  TypeParameter: Identifier ClassBound InterfaceBound*
     *  ClassBound: ':' FieldTypeSignature?
     *  InterfaceBound: ':' FieldTypeSignature
     */
    TypeParameter parseTypeParameter(SignatureReader reader) {
        int fistIndex = reader.index;

        // Search ':'
        if (reader.search(':')) {
            String identifier = reader.substring(fistIndex);

            // Parser bounds
            Type firstBound = null;
            UnmodifiableTypes types = null;

            Type bound;
            while (reader.nextEqualsTo(':')) {
                // Skip ':'
                reader.index++;

                bound = parseReferenceTypeSignature(reader);

                if (bound != null && !StringConstants.INTERNAL_OBJECT_SIGNATURE.equals(bound.getDescriptor())) {
                    if (firstBound == null) {
                        firstBound = bound;
                    } else {
                        if (types == null) {
                            types = new UnmodifiableTypes();
                            types.add(firstBound);
                        }
                        types.add(bound);
                    }
                }
            }

            if (firstBound == null) {
                return new TypeParameter(identifier);
            }
            if (types == null) {
                return new TypeParameterWithTypeBounds(identifier, firstBound);
            }
            return new TypeParameterWithTypeBounds(identifier, types);
        }
        return null;
    }

    /**
     * Rules:
     *  ThrowsSignature: '^' ClassTypeSignature | '^' TypeVariableSignature
     */
    private Type parseExceptionSignature(SignatureReader reader) {
        if (reader.nextEqualsTo('^')) {
            // Skip '^'
            reader.index++;

            return parseReferenceTypeSignature(reader);
        }
        return null;
    }

    /**
     * Rules:
     *  ClassTypeSignature: 'L' PackageSpecifier* SimpleClassTypeSignature ClassTypeSignatureSuffix* ';'
     *  SimpleClassTypeSignature: Identifier TypeArguments?
     *  ClassTypeSignatureSuffix: '.' SimpleClassTypeSignature
     */
    ObjectType parseClassTypeSignature(SignatureReader reader, int dimension) {
        if (reader.nextEqualsTo('L')) {
            // Skip 'L'. Parse 'PackageSpecifier* SimpleClassTypeSignature'
            int index = ++reader.index;
            char endMarker = ensureNonZeroEndMarker(reader);

            String internalTypeName = reader.substring(index);
            ObjectType to = makeFromInternalTypeName(internalTypeName);

            if (endMarker == '<') {
                // Skip '<'
                reader.index++;
                to = to.createType(parseTypeArguments(reader));
                ensureReadCharacter(reader, '>');
            }

            String name;
            String qualifiedName;
            // Parse 'ClassTypeSignatureSuffix*'
            while (reader.nextEqualsTo('.')) {
                // Skip '.'
                index = ++reader.index;
                endMarker = ensureNonZeroEndMarker(reader);

                name = reader.substring(index);
                internalTypeName = String.join("$", internalTypeName, name);
                if (Character.isDigit(name.charAt(0))) {
                    name = extractLocalClassName(name);
                    qualifiedName = null;
                } else {
                    qualifiedName = to.getQualifiedName() + '.' + name;
                }

                if (endMarker == '<') {
                    // Skip '<'
                    reader.index++;

                    BaseTypeArgument typeArguments = parseTypeArguments(reader);
                    ensureReadCharacter(reader, '>');

                    to = new InnerObjectType(internalTypeName, qualifiedName, name, typeArguments, to);
                } else {
                    to = new InnerObjectType(internalTypeName, qualifiedName, name, to);
                }
            }

            // Skip ';'
            reader.index++;

            return dimension==0 ? to : (ObjectType)to.createType(dimension);
        }
        return null;
    }

    private static char ensureNonZeroEndMarker(SignatureReader reader) {
        char endMarker = reader.searchEndMarker();
        if (endMarker == 0) {
            throw new SignatureFormatException(reader.signature);
        }
        return endMarker;
    }

    private static void ensureReadCharacter(SignatureReader reader, char closingCharacter) {
        if (reader.read() != closingCharacter) {
            throw new SignatureFormatException(reader.signature);
        }
    }

    /** Rules: TypeArguments: '<' TypeArgument+ '>' */
    private BaseTypeArgument parseTypeArguments(SignatureReader reader) {
        TypeArgument firstTypeArgument = parseTypeArgument(reader);

        requireNonNull(reader, firstTypeArgument);

        TypeArgument nextTypeArgument = parseTypeArgument(reader);

        if (nextTypeArgument == null) {
            return firstTypeArgument;
        }
        TypeArguments typeArguments = new TypeArguments();
        typeArguments.add(firstTypeArgument);

        do {
            typeArguments.add(nextTypeArgument);
            nextTypeArgument = parseTypeArgument(reader);
        } while (nextTypeArgument != null);

        return typeArguments;
    }

    private static void requireNonNull(SignatureReader reader, Object o) {
        if (o == null) {
            throw new SignatureFormatException(reader.signature);
        }
    }

    private Type parseReferenceTypeSignature(String signature) {
        return parseReferenceTypeSignature(new SignatureReader(signature));
    }
    
    /**
     * Rules:
     *  ReferenceTypeSignature: ClassTypeSignature | ArrayTypeSignature | TypeVariableSignature
     *  SimpleClassTypeSignature: Identifier TypeArguments?
     *  ArrayTypeSignature: '[' TypeSignature
     *  TypeSignature: '[' FieldTypeSignature | '[' BaseType
     *  BaseType: 'B' | 'C' | 'D' | 'F' | 'I' | 'J' | 'S' | 'Z'
     *  TypeVariableSignature: 'T' Identifier ';'
     */
    Type parseReferenceTypeSignature(SignatureReader reader) {
        if (reader.available()) {
            int dimension = 0;
            char c = reader.read();

            while (c == '[') {
                dimension++;
                c = reader.read();
            }

            switch (c) {
                case 'B':
                    return dimension == 0 ? PrimitiveType.TYPE_BYTE : PrimitiveType.TYPE_BYTE.createType(dimension);
                case 'C':
                    return dimension == 0 ? PrimitiveType.TYPE_CHAR : PrimitiveType.TYPE_CHAR.createType(dimension);
                case 'D':
                    return dimension == 0 ? PrimitiveType.TYPE_DOUBLE : PrimitiveType.TYPE_DOUBLE.createType(dimension);
                case 'F':
                    return dimension == 0 ? PrimitiveType.TYPE_FLOAT : PrimitiveType.TYPE_FLOAT.createType(dimension);
                case 'I':
                    return dimension == 0 ? PrimitiveType.TYPE_INT : PrimitiveType.TYPE_INT.createType(dimension);
                case 'J':
                    return dimension == 0 ? PrimitiveType.TYPE_LONG : PrimitiveType.TYPE_LONG.createType(dimension);
                case 'L':
                    // Unread 'L'
                    reader.index--;
                    return parseClassTypeSignature(reader, dimension);
                case 'S':
                    return dimension == 0 ? PrimitiveType.TYPE_SHORT : PrimitiveType.TYPE_SHORT.createType(dimension);
                case 'T':
                    int index = reader.index;

                    if (!reader.search(';')) {
                        return null;
                    }

                    String identifier = reader.substring(index);

                    // Skip ';'
                    reader.index++;

                    return new GenericType(identifier, dimension);
                case 'V':
                    Validate.isTrue(dimension == 0);
                    return PrimitiveType.TYPE_VOID;
                case 'Z':
                    return dimension == 0 ? PrimitiveType.TYPE_BOOLEAN : PrimitiveType.TYPE_BOOLEAN.createType(dimension);
                default:
                    // Unread 'c'
                    reader.index--;
                    return null;
            }
        }
        return null;
    }

    /**
     * Rules:
     *  TypeArgument: WildcardIndicator? FieldTypeSignature | '*'
     *  WildcardIndicator: '+' | '-'
     */
    private TypeArgument parseTypeArgument(SignatureReader reader) {
        switch (reader.read()) {
            case '+':
                return new WildcardExtendsTypeArgument(parseReferenceTypeSignature(reader));
            case '-':
                return new WildcardSuperTypeArgument(parseReferenceTypeSignature(reader));
            case '*':
                return WildcardTypeArgument.WILDCARD_TYPE_ARGUMENT;
            default:
                // Unread 'c'
                reader.index--;
                return parseReferenceTypeSignature(reader);
        }
    }

    static boolean isAReferenceTypeSignature(SignatureReader reader) {
        if (reader.available()) {
            // Skip dimension
            char c = reader.read();

            while (c == '[') {
                c = reader.read();
            }

            switch (c) {
                case 'B', 'C', 'D', 'F', 'I', 'J', 'S', 'V', 'Z':
                return true;
                case 'L':
                    // Unread 'L'
                    reader.index--;
                    return isAClassTypeSignature(reader);
            case 'T':
                    reader.searchEndMarker();
                    return true;
            default:
                    // Unread 'c'
                    reader.index--;
                    return false;
            }
        }
        return false;
    }

    static boolean isAClassTypeSignature(SignatureReader reader) {
        if (reader.nextEqualsTo('L')) {
            // Skip 'L'
            reader.index++;

            // Parse 'PackageSpecifier* SimpleClassTypeSignature'
            char endMarker = ensureNonZeroEndMarker(reader);

            if (endMarker == '<') {
                // Skip '<'
                reader.index++;
                isATypeArguments(reader);
                ensureReadCharacter(reader, '>');
            }

            // Parse 'ClassTypeSignatureSuffix*'
            while (reader.nextEqualsTo('.')) {
                // Skip '.'
                reader.index++;

                endMarker = ensureNonZeroEndMarker(reader);

                if (endMarker == '<') {
                    // Skip '<'
                    reader.index++;
                    isATypeArguments(reader);
                    ensureReadCharacter(reader, '>');
                }
            }

            // Skip ';'
            reader.index++;

            return true;
        }
        return false;
    }

    private static boolean isATypeArguments(SignatureReader reader) {
        if (!isATypeArgument(reader)) {
            throw new SignatureFormatException(reader.signature);
        }

        while (isATypeArgument(reader)) {
            // read all type arguments
        }

        return true;
    }

    private static boolean isATypeArgument(SignatureReader reader) {
        switch (reader.read()) {
            case '+', '-':
                return isAReferenceTypeSignature(reader);
            case '*':
                return true;
            default:
                // Unread 'c'
                reader.index--;
                return isAReferenceTypeSignature(reader);
        }
    }

    private static String extractLocalClassName(String name) {
        if (Character.isDigit(name.charAt(0))) {
            int i = 0;
            int len = name.length();

            while (i < len && Character.isDigit(name.charAt(i))) {
                i++;
            }

            return i == len ? null : name.substring(i);
        }

        return name;
    }

    public synchronized ObjectType makeFromDescriptorOrInternalTypeName(String descriptorOrInternalTypeName) {
        return descriptorOrInternalTypeName.charAt(0) == '[' ? makeFromDescriptor(descriptorOrInternalTypeName) : makeFromInternalTypeName(descriptorOrInternalTypeName);
    }

    public synchronized Type makeFromSignatureOrInternalTypeName(String signatureOrInternalTypeName) {
        if (signatureOrInternalTypeName == null) {
            throw new IllegalArgumentException("ObjectTypeMaker.makeFromSignatureOrInternalTypeName(signatureOrInternalTypeName) : invalid signatureOrInternalTypeName");
        }
        if (signatureOrInternalTypeName.charAt(0) == '[' || signatureOrInternalTypeName.endsWith(";")) {
            return makeFromSignature(signatureOrInternalTypeName);
        }
        return makeFromInternalTypeName(signatureOrInternalTypeName);
    }

    public synchronized ObjectType makeFromDescriptor(String descriptor) {
        ObjectType to = descriptorToObjectType.get(descriptor);

        if (to == null) {
            if (descriptor.charAt(0) == '[') {
                int dimension = 1;

                while (descriptor.charAt(dimension) == '[') {
                    dimension++;
                }

                to = (ObjectType)makeFromDescriptorWithoutBracket(descriptor.substring(dimension)).createType(dimension);
            } else {
                to = makeFromDescriptorWithoutBracket(descriptor);
            }

            descriptorToObjectType.put(descriptor, to);
        }

        return to;
    }

    private ObjectType makeFromDescriptorWithoutBracket(String descriptor) {
        ObjectType to = INTERNALNAME_TO_OBJECTPRIMITIVETYPE.get(descriptor);

        if (to == null) {
            to = makeFromInternalTypeName(descriptor.substring(1, descriptor.length()-1));
        }

        return to;
    }

    public synchronized ObjectType makeFromInternalTypeName(String internalTypeName) {
        if (internalTypeName == null || internalTypeName.endsWith(";")) {
            throw new IllegalArgumentException("ObjectTypeMaker.makeFromInternalTypeName(internalTypeName) : invalid internalTypeName");
        }

        ObjectType to = loadType(internalTypeName);

        if (to == null) {
            // File not found with the system class loader -> Create type from 'internalTypeName'
            to = create(internalTypeName);
        }

        return to;
    }

    public static synchronized ObjectType create(String internalTypeName) {

        if (internalTypeName == null) {
            return null;
        }

        int lastSlash = internalTypeName.lastIndexOf('/');
        int lastDollar = internalTypeName.lastIndexOf('$');
        ObjectType to;

        if (lastSlash < lastDollar) {
            String outerTypeName = internalTypeName.substring(0, lastDollar);
            ObjectType outerSot = create(outerTypeName);
            String innerName = internalTypeName.substring(outerTypeName.length() + 1);

            if (innerName.isEmpty()) {
                String qualifiedName = internalTypeName.replace('/', '.');
                String name = qualifiedName.substring(lastSlash + 1);
                to = new ObjectType(internalTypeName, qualifiedName, name);
            } else if (Character.isDigit(innerName.charAt(0))) {
                to = new InnerObjectType(internalTypeName, null, extractLocalClassName(innerName), outerSot);
            } else {
                String qualifiedName = outerSot.getQualifiedName() + '.' + innerName;
                to = new InnerObjectType(internalTypeName, qualifiedName, innerName, outerSot);
            }
        } else {
            String qualifiedName = internalTypeName.replace('/', '.');
            String name = qualifiedName.substring(lastSlash + 1);
            to = new ObjectType(internalTypeName, qualifiedName, name);
        }

        internalTypeNameToObjectType.put(internalTypeName, to);

        return to;
    }

    public synchronized ObjectType searchSuperParameterizedType(ObjectType superObjectType, ObjectType objectType) {
        if (superObjectType == TYPE_UNDEFINED_OBJECT || superObjectType.equals(TYPE_OBJECT) || superObjectType.equals(objectType)) {
            return objectType;
        }
        if (superObjectType.getDimension() > 0 || objectType.getDimension() > 0) {
            return null;
        }
        String superInternalTypeName = superObjectType.getInternalName();
        long superHashCode = superInternalTypeName.hashCode() * 31L;
        return searchSuperParameterizedType(superHashCode, superInternalTypeName, objectType);
    }

    public synchronized boolean isAssignable(Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, ObjectType left, Type leftUnbound, ObjectType right) {
        if (left == TYPE_UNDEFINED_OBJECT || right == TYPE_UNDEFINED_OBJECT || left.equals(TYPE_OBJECT) || left.equals(right)) {
            return true;
        }
        int leftDim = left.getDimension();
        int rightDim = right.getDimension();
        if (leftDim <= 0 && rightDim <= 0) {
            String leftInternalTypeName = left.getInternalName();
            long leftHashCode = leftInternalTypeName.hashCode() * 31L;
            ObjectType to = searchSuperParameterizedType(leftHashCode, leftInternalTypeName, right);

            if (to != null && leftInternalTypeName.equals(to.getInternalName())) {
                return left.getTypeArguments() == null || to.getTypeArguments() == null
                        || left.getTypeArguments().isTypeArgumentAssignableFrom(this, typeBindings, typeBounds, to.getTypeArguments())
                        || (leftUnbound instanceof ObjectType lt
                                && isAssignable(typeBindings, typeBounds, lt, right));
            }
        }
        if (leftDim == rightDim && leftDim > 0 && rightDim > 0) {
            Type leftType = left.createType(leftDim - 1);
            Type rightType = right.createType(rightDim - 1);
            if (leftType instanceof ObjectType lt && rightType instanceof ObjectType rt) {
                return isAssignable(typeBindings, typeBounds, lt, rt);
            }
        }
        if (leftUnbound instanceof GenericType gt) {
            TypeArgument typeArgument = typeBindings.get(gt.getName());
            if (typeArgument instanceof WildcardSuperTypeArgument wsta && wsta.type().equals(left)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isAssignable(Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, ObjectType left, ObjectType right) {
        return isAssignable(typeBindings, typeBounds, left, null, right);
    }

    public synchronized boolean isAssignable(Map<String, BaseType> typeBounds, ObjectType left, ObjectType right) {
        return isAssignable(Collections.emptyMap(), typeBounds, left, right);
    }
    
    public synchronized boolean isAssignable(ObjectType left, ObjectType right) {
        return isAssignable(Collections.emptyMap(), left, right);
    }
    
    public synchronized ObjectType searchSuperParameterizedType(long leftHashCode, String leftInternalTypeName, ObjectType right) {
        if (right.equals(TYPE_OBJECT)) {
            return null;
        }

        Long key = leftHashCode + right.hashCode();

        if (superParameterizedObjectTypes.containsKey(key)) {
            return superParameterizedObjectTypes.get(key);
        }

        String rightInternalTypeName = right.getInternalName();

        if (leftInternalTypeName.equals(rightInternalTypeName)) {
            superParameterizedObjectTypes.put(key, right);
            return right;
        }

        TypeTypes rightTypeTypes = makeTypeTypes(rightInternalTypeName);

        if (rightTypeTypes != null) {
            BindTypesToTypesVisitor bindTypesToTypesVisitor = new BindTypesToTypesVisitor();
            Map<String, TypeArgument> bindings;

            if (rightTypeTypes.getTypeParameters() == null || right.getTypeArguments() == null) {
                bindings = Collections.emptyMap();
            } else {
                bindings = new HashMap<>();

                if (rightTypeTypes.getTypeParameters().isList() && right.getTypeArguments().isTypeArgumentList()) {
                    Iterator<TypeParameter> iteratorTypeParameter = rightTypeTypes.getTypeParameters().iterator();
                    Iterator<TypeArgument> iteratorTypeArgument = right.getTypeArguments().getTypeArgumentList().iterator();

                    while (iteratorTypeParameter.hasNext()) {
                        bindings.put(iteratorTypeParameter.next().getIdentifier(), iteratorTypeArgument.next());
                    }
                } else {
                    bindings.put(rightTypeTypes.getTypeParameters().getFirst().getIdentifier(), right.getTypeArguments().getTypeArgumentFirst());
                }
            }

            bindTypesToTypesVisitor.setBindings(bindings);

            if (rightTypeTypes.getSuperType() != null) {
                bindTypesToTypesVisitor.init();
                rightTypeTypes.getSuperType().accept(bindTypesToTypesVisitor);
                ObjectType to = (ObjectType) bindTypesToTypesVisitor.getType();
                to = searchSuperParameterizedType(leftHashCode, leftInternalTypeName, to);

                if (to != null) {
                    superParameterizedObjectTypes.put(key, to);
                    return to;
                }
            }
            if (rightTypeTypes.getInterfaces() != null) {
                ObjectType to;
                for (Type interfaze : rightTypeTypes.getInterfaces()) {
                    bindTypesToTypesVisitor.init();
                    interfaze.accept(bindTypesToTypesVisitor);
                    to = (ObjectType) bindTypesToTypesVisitor.getType();
                    to = searchSuperParameterizedType(leftHashCode, leftInternalTypeName, to);

                    if (to != null) {
                        superParameterizedObjectTypes.put(key, to);
                        return to;
                    }
                }
            }
        }

        superParameterizedObjectTypes.put(key, null);
        return null;
    }

    public synchronized boolean isRawTypeAssignable(ObjectType left, ObjectType right) {
        if (left == TYPE_UNDEFINED_OBJECT || left.equals(TYPE_OBJECT) || left.equals(right)) {
            return true;
        }
        if (left.getDimension() > 0 || right.getDimension() > 0) {
            return false;
        }
        String leftInternalName = left.getInternalName();
        String rightInternalName = right.getInternalName();
        return leftInternalName.equals(rightInternalName) || isRawTypeAssignable(leftInternalName.hashCode() * 31L, leftInternalName, rightInternalName);
    }

    private boolean isRawTypeAssignable(long leftHashCode, String leftInternalName, String rightInternalName) {
        if (StringConstants.JAVA_LANG_OBJECT.equals(rightInternalName)) {
            return false;
        }

        Long key = leftHashCode + rightInternalName.hashCode();

        if (assignableRawTypes.containsKey(key)) {
            return assignableRawTypes.get(key);
        }

        String[] superClassAndInterfaceNames = hierarchy.get(rightInternalName);

        if (superClassAndInterfaceNames == null) {
            loadType(rightInternalName);
            superClassAndInterfaceNames = hierarchy.get(rightInternalName);
        }

        if (superClassAndInterfaceNames != null) {
            for (String name : superClassAndInterfaceNames) {
                if (leftInternalName.equals(name)) {
                    assignableRawTypes.put(key, Boolean.TRUE);
                    return true;
                }
            }

            for (String name : superClassAndInterfaceNames) {
                if (isRawTypeAssignable(leftHashCode, leftInternalName, name)) {
                    assignableRawTypes.put(key, Boolean.TRUE);
                    return true;
                }
            }
        }

        assignableRawTypes.put(key, Boolean.FALSE);
        return false;
    }

    public synchronized TypeTypes makeTypeTypes(String internalTypeName) {
        if (internalTypeNameToTypeTypes.containsKey(internalTypeName)) {
            return internalTypeNameToTypeTypes.get(internalTypeName);
        }

        TypeTypes typeTypes = null;

        try {
            if (loader.canLoad(internalTypeName)) {
                typeTypes = makeTypeTypes(internalTypeName, loader.load(internalTypeName));
            } else if (classPathLoader.canLoad(internalTypeName)) {
                typeTypes = makeTypeTypes(internalTypeName, classPathLoader.load(internalTypeName));
            }
            internalTypeNameToTypeTypes.put(internalTypeName, typeTypes);
        } catch (Exception e) {
            assert ExceptionUtil.printStackTrace(e);
        }

        return typeTypes;
    }

    private TypeTypes makeTypeTypes(String internalTypeName, byte[] data) throws IOException {
        if (data == null) {
            return null;
        }

        try (DataInputStream reader = new DataInputStream(new ByteArrayInputStream(data))) {
            Object[] constants = loadClassFile(internalTypeName, reader);

            // Skip fields
            skipMembers(reader);

            // Skip methods
            skipMembers(reader);

            // Load attributes
            String signature = null;
            int count = reader.readUnsignedShort();

            int attributeNameIndex;
            int attributeLength;
            for (int j=0; j<count; j++) {
                attributeNameIndex = reader.readUnsignedShort();
                attributeLength = reader.readInt();

                if (StringConstants.SIGNATURE_ATTRIBUTE_NAME.equals(constants[attributeNameIndex])) {
                    signature = (String)constants[reader.readUnsignedShort()];
                    break;
                }
                reader.skipBytes(attributeLength);
            }

            String[] superClassAndInterfaceNames = hierarchy.get(internalTypeName);
            TypeTypes typeTypes = new TypeTypes();

            typeTypes.setThisType(makeFromInternalTypeName(internalTypeName));

            if (signature == null) {
                String superTypeName = superClassAndInterfaceNames[0];

                typeTypes.setSuperType(superTypeName == null ? null : makeFromInternalTypeName(superTypeName));

                switch (superClassAndInterfaceNames.length) {
                    case 0, 1:
                        break;
                    case 2:
                        typeTypes.setInterfaces(makeFromInternalTypeName(superClassAndInterfaceNames[1]));
                        break;
                    default:
                        int length = superClassAndInterfaceNames.length;
                        UnmodifiableTypes list = new UnmodifiableTypes(length-1);
                        for (int i=1; i<length; i++) {
                            list.add(makeFromInternalTypeName(superClassAndInterfaceNames[i]));
                        }
                        typeTypes.setInterfaces(list);
                        break;
                }
            } else {
                // Parse 'signature' attribute
                SignatureReader signatureReader = new SignatureReader(signature);

                typeTypes.setTypeParameters(parseTypeParameters(signatureReader));
                typeTypes.setSuperType(parseClassTypeSignature(signatureReader, 0));

                Type firstInterface = parseClassTypeSignature(signatureReader, 0);

                if (firstInterface != null) {
                    Type nextInterface = parseClassTypeSignature(signatureReader, 0);

                    if (nextInterface == null) {
                        typeTypes.setInterfaces(firstInterface);
                    } else {
                        int length = superClassAndInterfaceNames.length;
                        UnmodifiableTypes list = new UnmodifiableTypes(length-1);

                        list.add(firstInterface);

                        do {
                            list.add(nextInterface);
                            nextInterface = parseClassTypeSignature(signatureReader, 0);
                        } while (nextInterface != null);

                        typeTypes.setInterfaces(list);
                    }
                }
            }

            return typeTypes;
        }
    }

    public synchronized void setFieldType(String internalTypeName, String fieldName, Type type) {
        String key = internalTypeName + ':' + fieldName;
        putInternalTypeNameFieldNameToType(key, type);
    }

    public synchronized Type makeFieldType(String internalTypeName, String fieldName, String descriptor) {
        Type type = loadFieldType(internalTypeName, fieldName, descriptor);

        if (type == null) {
            String key = internalTypeName + ':' + fieldName;
            type = makeFromSignature(descriptor);
            putInternalTypeNameFieldNameToType(key, type);
        }

        return type;
    }

    private Type loadFieldType(String internalTypeName, String fieldName, String descriptor) {
        String key = internalTypeName + ':' + fieldName;
        Type type = internalTypeNameFieldNameToType.get(key);

        // Load fields
        if (type == null && loaded.computeIfAbsent(internalTypeName, this::loadFieldsAndMethods)) {
            type = internalTypeNameFieldNameToType.get(key);

            if (type == null) {
                TypeTypes typeTypes = makeTypeTypes(internalTypeName);

                if (typeTypes != null) {
                    if (typeTypes.getSuperType() != null) {
                        type = loadFieldType(typeTypes.getSuperType(), fieldName, descriptor);
                    }

                    if (type == null && typeTypes.getInterfaces() != null) {
                        if (typeTypes.getInterfaces().isList()) {
                            for (Type interfaze : typeTypes.getInterfaces()) {
                                type = loadFieldType((ObjectType) interfaze, fieldName, descriptor);
                                if (type != null) {
                                    break;
                                }
                            }
                        } else {
                            type = loadFieldType((ObjectType) typeTypes.getInterfaces().getFirst(), fieldName, descriptor);
                        }
                    }
                }
            }

            if (type != null) {
                putInternalTypeNameFieldNameToType(key, type);
            }
        }

        return type;
    }

    private Type loadFieldType(ObjectType objectType, String fieldName, String descriptor) {
        String internalTypeName = objectType.getInternalName();
        BaseTypeArgument typeArguments = objectType.getTypeArguments();
        Type type = loadFieldType(internalTypeName, fieldName, descriptor);

        if (type != null && typeArguments != null) {
            TypeTypes typeTypes = makeTypeTypes(internalTypeName);

            if (typeTypes != null && typeTypes.getTypeParameters() != null) {
                BindTypesToTypesVisitor bindTypesToTypesVisitor = new BindTypesToTypesVisitor();
                Map<String, TypeArgument> bindings = new HashMap<>();

                if (typeTypes.getTypeParameters().isList() && typeArguments.isTypeArgumentList()) {
                    Iterator<TypeParameter> iteratorTypeParameter = typeTypes.getTypeParameters().iterator();
                    Iterator<TypeArgument> iteratorTypeArgument = typeArguments.getTypeArgumentList().iterator();

                    while (iteratorTypeParameter.hasNext()) {
                        bindings.put(iteratorTypeParameter.next().getIdentifier(), iteratorTypeArgument.next());
                    }
                } else {
                    bindings.put(typeTypes.getTypeParameters().getFirst().getIdentifier(), typeArguments.getTypeArgumentFirst());
                }

                bindTypesToTypesVisitor.setBindings(bindings);

                bindTypesToTypesVisitor.init();
                type.accept(bindTypesToTypesVisitor);
                type = (Type) bindTypesToTypesVisitor.getType();
            }
        }

        return type;
    }

    public synchronized void setMethodReturnedType(String internalTypeName, String methodName, String descriptor, Type type) {
        makeMethodTypes(internalTypeName, methodName, descriptor).setReturnedType(type);
    }

    public synchronized MethodTypes makeMethodTypes(String internalTypeName, String methodName, String descriptor) {
        MethodTypes methodTypes = loadMethodTypes(internalTypeName, methodName, descriptor);

        if (methodTypes == null) {
            String key = internalTypeName + ':' + methodName + descriptor;
            methodTypes = parseMethodSignature(internalTypeName, methodName, descriptor, null);
            internalTypeNameMethodNameDescriptorToMethodTypes.put(key, methodTypes);
        }

        return methodTypes;
    }

    private MethodTypes loadMethodTypes(String internalTypeName, String methodName, String descriptor) {
        String key = internalTypeName + ':' + methodName + descriptor;
        MethodTypes methodTypes = internalTypeNameMethodNameDescriptorToMethodTypes.get(key);

        // Load method
        if (methodTypes == null && loaded.computeIfAbsent(internalTypeName, this::loadFieldsAndMethods)) {
            methodTypes = internalTypeNameMethodNameDescriptorToMethodTypes.get(key);

            if (methodTypes == null) {
                TypeTypes typeTypes = makeTypeTypes(internalTypeName);

                if (typeTypes != null) {
                    if (typeTypes.getSuperType() != null) {
                        methodTypes = loadMethodTypes(typeTypes.getSuperType(), methodName, descriptor);
                    }

                    if (methodTypes == null && typeTypes.getInterfaces() != null) {
                        if (typeTypes.getInterfaces().isList()) {
                            for (Type interfaze : typeTypes.getInterfaces()) {
                                methodTypes = loadMethodTypes((ObjectType) interfaze, methodName, descriptor);
                                if (methodTypes != null) {
                                    break;
                                }
                            }
                        } else {
                            methodTypes = loadMethodTypes((ObjectType) typeTypes.getInterfaces().getFirst(), methodName, descriptor);
                        }
                    }
                }
            }

            if (methodTypes != null) {
                internalTypeNameMethodNameDescriptorToMethodTypes.put(key, methodTypes);
            }
        }

        return methodTypes;
    }

    private MethodTypes loadMethodTypes(ObjectType objectType, String methodName, String descriptor) {
        String internalTypeName = objectType.getInternalName();
        BaseTypeArgument typeArguments = objectType.getTypeArguments();
        MethodTypes methodTypes = loadMethodTypes(internalTypeName, methodName, descriptor);

        if (methodTypes != null && typeArguments != null) {
            TypeTypes typeTypes = makeTypeTypes(internalTypeName);

            if (typeTypes != null && typeTypes.getTypeParameters() != null) {
                BindTypesToTypesVisitor bindTypesToTypesVisitor = new BindTypesToTypesVisitor();
                Map<String, TypeArgument> bindings = new HashMap<>();
                MethodTypes newMethodTypes = new MethodTypes();

                if (typeTypes.getTypeParameters().isList() && typeArguments.isTypeArgumentList()) {
                    Iterator<TypeParameter> iteratorTypeParameter = typeTypes.getTypeParameters().iterator();
                    Iterator<TypeArgument> iteratorTypeArgument = typeArguments.getTypeArgumentList().iterator();

                    while (iteratorTypeParameter.hasNext()) {
                        bindings.put(iteratorTypeParameter.next().getIdentifier(), iteratorTypeArgument.next());
                    }
                } else {
                    bindings.put(typeTypes.getTypeParameters().getFirst().getIdentifier(), typeArguments.getTypeArgumentFirst());
                }

                bindTypesToTypesVisitor.setBindings(bindings);

                if (methodTypes.getParameterTypes() == null) {
                    newMethodTypes.setParameterTypes(null);
                } else {
                    bindTypesToTypesVisitor.init();
                    methodTypes.getParameterTypes().accept(bindTypesToTypesVisitor);
                    BaseType baseType = bindTypesToTypesVisitor.getType();

                    if (baseType.isList() && baseType.isTypes()) {
                        baseType = new UnmodifiableTypes(baseType.getList());
                    }

                    newMethodTypes.setParameterTypes(baseType);
                }

                bindTypesToTypesVisitor.init();
                methodTypes.getReturnedType().accept(bindTypesToTypesVisitor);
                newMethodTypes.setReturnedType((Type)bindTypesToTypesVisitor.getType());

                newMethodTypes.setTypeParameters(null);
                newMethodTypes.setExceptionTypes(methodTypes.getExceptionTypes());

                methodTypes = newMethodTypes;
            }
        }

        return methodTypes;
    }

    private ObjectType loadType(String internalTypeName) {
        ObjectType to = internalTypeNameToObjectType.get(internalTypeName);

        if (to == null) {
            try {
                if (loader.canLoad(internalTypeName)) {
                    to = loadType(internalTypeName, loader.load(internalTypeName));
                } else if (classPathLoader.canLoad(internalTypeName)) {
                    to = loadType(internalTypeName, classPathLoader.load(internalTypeName));
                }
                internalTypeNameToObjectType.put(internalTypeName, Optional.ofNullable(to).orElse(TYPE_UNDEFINED_OBJECT));
            } catch (Exception e) {
                assert ExceptionUtil.printStackTrace(e);
            }
        }

        return to;
    }

    private ObjectType loadType(String internalTypeName, byte[] data) throws IOException {

        try (DataInputStream reader = new DataInputStream(new ByteArrayInputStream(data))) {
            Object[] constants = loadClassFile(internalTypeName, reader);

            // Skip fields
            skipMembers(reader);

            // Skip methods
            skipMembers(reader);

            String outerTypeName = null;
            ObjectType outerObjectType = null;

            // Load attributes
            int count = reader.readUnsignedShort();
            int attributeNameIndex;
            int attributeLength;
            Set<String> innerTypeNames = new HashSet<>();
            for (int i = 0; i < count; i++) {
                attributeNameIndex = reader.readUnsignedShort();
                attributeLength = reader.readInt();

                if ("InnerClasses".equals(constants[attributeNameIndex])) {
                    int innerClassesCount = reader.readUnsignedShort();

                    int innerTypeIndex;
                    int outerTypeIndex;
                    Integer cc;
                    String innerTypeName;
                    for(int j=0; j < innerClassesCount; j++) {
                        innerTypeIndex = reader.readUnsignedShort();
                        outerTypeIndex = reader.readUnsignedShort();

                        // Skip 'innerNameIndex' & innerAccessFlags'
                        reader.skipBytes(2 * 2);

                        cc = (Integer)constants[innerTypeIndex];
                        innerTypeName = (String)constants[cc];
                        innerTypeNames.add(innerTypeName);
                        if (innerTypeName.equals(internalTypeName)) {
                            if (outerTypeIndex == 0) {
                                // Synthetic inner class -> Search outer class
                                int lastDollar = internalTypeName.lastIndexOf('$');

                                if (lastDollar != -1) {
                                    outerTypeName = internalTypeName.substring(0, lastDollar);
                                    outerObjectType = loadType(outerTypeName);
                                }
                            } else {
                                // Return 'outerTypeName'
                                cc = (Integer)constants[outerTypeIndex];
                                outerTypeName = (String)constants[cc];
                                outerObjectType = loadType(outerTypeName);
                            }
                            break;
                        }
                    }

                    break;
                }
                reader.skipBytes(attributeLength);
            }

            if (outerObjectType == null) {
                int lastSlash = internalTypeName.lastIndexOf('/');
                String qualifiedName = internalTypeName.replace('/', '.');
                String name = qualifiedName.substring(lastSlash + 1);

                return new ObjectType(internalTypeName, qualifiedName, name, innerTypeNames);
            }
            int index;

            if (outerTypeName != null && internalTypeName.length() > outerTypeName.length() + 1) {
                index = outerTypeName.length();
            } else {
                index = internalTypeName.lastIndexOf('$');
            }

            String innerName = internalTypeName.substring(index + 1);

            if (Character.isDigit(innerName.charAt(0))) {
                return new InnerObjectType(internalTypeName, null, extractLocalClassName(innerName), innerTypeNames, outerObjectType);
            }
            String qualifiedName = outerObjectType.getQualifiedName() + '.' + innerName;
            return new InnerObjectType(internalTypeName, qualifiedName, innerName, innerTypeNames, outerObjectType);
        }
    }

    public synchronized boolean loadFieldsAndMethods(String internalTypeName) {
        try {
            if (loader.canLoad(internalTypeName)) {
                loadFieldsAndMethods(internalTypeName, loader.load(internalTypeName));
                return true;
            }
            if (classPathLoader.canLoad(internalTypeName)) {
                loadFieldsAndMethods(internalTypeName, classPathLoader.load(internalTypeName));
                return true;
            }
        } catch (Exception e) {
            assert ExceptionUtil.printStackTrace(e);
        }

        return false;
    }

    private void loadFieldsAndMethods(String internalTypeName, byte[] data) throws IOException {
        if (data != null) {
            try (DataInputStream reader = new DataInputStream(new ByteArrayInputStream(data))) {
                Object[] constants = loadClassFile(internalTypeName, reader);

                // Load fields
                int count = reader.readUnsignedShort();
                for (int i = 0; i < count; i++) {
                    // skip 'accessFlags'
                    reader.skipBytes(2);

                    int nameIndex = reader.readUnsignedShort();
                    int descriptorIndex = reader.readUnsignedShort();

                    // Load attributes
                    String signature = null;
                    int attributeCount = reader.readUnsignedShort();

                    int attributeNameIndex;
                    int attributeLength;
                    for (int j=0; j<attributeCount; j++) {
                        attributeNameIndex = reader.readUnsignedShort();
                        attributeLength = reader.readInt();

                        if (StringConstants.SIGNATURE_ATTRIBUTE_NAME.equals(constants[attributeNameIndex])) {
                            signature = (String)constants[reader.readUnsignedShort()];
                        } else {
                            reader.skipBytes(attributeLength);
                        }
                    }

                    String name = (String)constants[nameIndex];
                    String descriptor = (String)constants[descriptorIndex];
                    String key = internalTypeName + ':' + name;

                    if (signature == null) {
                        putInternalTypeNameFieldNameToType(key, makeFromSignature(descriptor));
                    } else {
                        putInternalTypeNameFieldNameToType(key, makeFromSignature(signature));
                    }
                }

                // Load methods
                count = reader.readUnsignedShort();
                int accessFlags;
                int nameIndex;
                int descriptorIndex;
                String signature;
                String[] exceptionTypeNames;
                int attributeCount;
                String name;
                String descriptor;
                String key;
                MethodTypes methodTypes;
                int parameterCount;
                int attributeNameIndex;
                int attributeLength;
                for (int i = 0; i < count; i++) {
                    accessFlags = reader.readUnsignedShort();
                    nameIndex = reader.readUnsignedShort();
                    descriptorIndex = reader.readUnsignedShort();

                    // Load attributes
                    signature = null;
                    exceptionTypeNames = null;
                    attributeCount = reader.readUnsignedShort();

                    for (int j=0; j<attributeCount; j++) {
                        attributeNameIndex = reader.readUnsignedShort();
                        attributeLength = reader.readInt();
                        name = (String)constants[attributeNameIndex];

                        switch (name) {
                            case StringConstants.SIGNATURE_ATTRIBUTE_NAME:
                                signature = (String)constants[reader.readUnsignedShort()];
                                break;
                            case "Exceptions":
                                int exceptionCount = reader.readUnsignedShort();
                                if (exceptionCount > 0) {
                                    exceptionTypeNames = new String[exceptionCount];

                                    int exceptionClassIndex;
                                    Integer cc;
                                    for (int k=0; k<exceptionCount; k++) {
                                        exceptionClassIndex = reader.readUnsignedShort();
                                        cc = (Integer)constants[exceptionClassIndex];
                                        exceptionTypeNames[k] = (String)constants[cc];
                                    }
                                }
                                break;
                            default:
                                reader.skipBytes(attributeLength);
                                break;
                        }
                    }

                    name = (String)constants[nameIndex];
                    descriptor = (String)constants[descriptorIndex];
                    key = internalTypeName + ':' + name + descriptor;
                    if (signature == null) {
                        methodTypes = parseMethodSignature(internalTypeName, name, descriptor, exceptionTypeNames);
                    } else {
                        methodTypes = parseMethodSignature(internalTypeName, name, descriptor, signature, exceptionTypeNames);
                    }
                    methodTypes.setAccessFlags(accessFlags);
                    if ((accessFlags & Const.ACC_SYNTHETIC) != 0) {
                        continue;
                    }
                    internalTypeNameMethodNameDescriptorToMethodTypes.put(key, methodTypes);

                    parameterCount = methodTypes.getParameterTypes() == null ? 0 : methodTypes.getParameterTypes().size();
                    key = internalTypeName + ':' + name + ':' + parameterCount;

                    if (parameterCount > 0) {
                        internalTypeNameMethodNameParameterCountToDeclaredParameterTypes.computeIfAbsent(key, k -> new HashSet<>()).add(methodTypes.getParameterTypes());
                    } else {
                        internalTypeNameMethodNameParameterCountToDeclaredParameterTypes.computeIfAbsent(key, k -> Collections.emptySet());
                    }
                }
            }
        }
    }

    private static void putInternalTypeNameFieldNameToType(String key, Type value) {
        internalTypeNameFieldNameToType.put(key, value);
    }

    private static Object[] loadClassFile(String internalTypeName, DataInput reader) throws IOException {
        int magic = reader.readInt();

        if (magic != CoreConstants.JAVA_MAGIC_NUMBER) {
            throw new ClassFormatException("Invalid CLASS file");
        }

        // Skip 'minorVersion', 'majorVersion'
        reader.skipBytes(2 * 2);

        Object[] constants = loadConstants(reader);

        // Skip 'accessFlags' & 'thisClassIndex'
        reader.skipBytes(2 * 2);

        // Load super class name
        int superClassIndex = reader.readUnsignedShort();
        String superClassName;

        if (superClassIndex == 0) {
            superClassName = null;
        } else {
            Integer cc = (Integer)constants[superClassIndex];
            superClassName = (String)constants[cc];
        }

        // Load interface names
        int count = reader.readUnsignedShort();
        String[] superClassAndInterfaceNames = new String[count + 1];

        superClassAndInterfaceNames[0] = superClassName;

        int interfaceIndex;
        Integer cc;
        for (int i = 1; i <= count; i++) {
            interfaceIndex = reader.readUnsignedShort();
            cc = (Integer)constants[interfaceIndex];
            superClassAndInterfaceNames[i] = (String)constants[cc];
        }

        hierarchy.put(internalTypeName, superClassAndInterfaceNames);

        return constants;
    }

    private static void skipMembers(DataInput reader) throws IOException {
        int count = reader.readUnsignedShort();
        for (int i = 0; i < count; i++) {
            // skip 'accessFlags', 'nameIndex', 'descriptorIndex'
            reader.skipBytes(3 * 2);
            skipAttributes(reader);
        }
    }

    private static Object[] loadConstants(DataInput reader) throws IOException {
        int count = reader.readUnsignedShort();

        if (count == 0) {
            throw new ClassFormatException("Zero-length constant pool");
        }

        Object[] constants = new Object[count];

        int tag;
        for (int i=1; i<count; i++) {
            tag = reader.readByte();
            switch (tag) {
                case CONSTANT_Utf8:
                    constants[i] = reader.readUTF();
                    break;
                case CONSTANT_Class:
                    constants[i] = Integer.valueOf(reader.readUnsignedShort());
                    break;
                case CONSTANT_String, CONSTANT_MethodType, CONSTANT_Module, CONSTANT_Package:
                    reader.skipBytes(2);
                    break;
                case CONSTANT_MethodHandle:
                    reader.skipBytes(3);
                    break;
                case CONSTANT_Integer,
                     CONSTANT_Float,
                     CONSTANT_Fieldref,
                     CONSTANT_Methodref,
                     CONSTANT_InterfaceMethodref,
                     CONSTANT_NameAndType,
                     CONSTANT_Dynamic,
                     CONSTANT_InvokeDynamic:
                    reader.skipBytes(4);
                    break;
                case CONSTANT_Long, CONSTANT_Double:
                    reader.skipBytes(8);
                    i++;
                    break;
                default:
                    throw new ClassFormatException("Invalid constant pool entry");
            }
        }

        return constants;
    }

    private static void skipAttributes(DataInput reader) throws IOException {
        int count = reader.readUnsignedShort();

        int attributeLength;
        for (int i = 0; i < count; i++) {
            // skip 'attributeNameIndex'
            reader.skipBytes(2);

            attributeLength = reader.readInt();

            reader.skipBytes(attributeLength);
        }
    }

    static class SignatureReader {
        private final String signature;
        private final char[] array;
        private final int length;
        int index;

        public SignatureReader(String signature) {
            this(signature, 0);
        }

        public SignatureReader(String signature, int index) {
            this.signature = signature;
            this.array = signature.toCharArray();
            this.length = array.length;
            this.index = index;
        }

        public synchronized char read() {
            return array[index++];
        }

        public synchronized boolean nextEqualsTo(char c) {
            return index < length && array[index] == c;
        }

        public synchronized boolean search(char c) {

            for (int i=index; i<array.length; i++) {
                if (array[i] == c) {
                    index = i;
                    return true;
                }
            }

            return false;
        }

        public synchronized char searchEndMarker() {

            char c;
            while (index < array.length) {
                c = array[index];

                if (c == ';' || c == '<' || c == '.') {
                    return c;
                }

                index++;
            }

            return 0;
        }

        public synchronized boolean available() {
            return index < length;
        }

        public synchronized String substring(int beginIndex) {
            return new String(array, beginIndex, index-beginIndex);
        }

        @Override
        public synchronized String toString() {
            return "SignatureReader{index=" + index + ", nextChars=" + new String(array, index, length-index) + "}";
        }
    }

    public synchronized int matchCount(String internalTypeName, String name, int parameterCount, boolean constructor) {
        String suffixKey = ":" + name + ':' + parameterCount;
        return getSetOfParameterTypes(internalTypeName, suffixKey, constructor).size();
    }

    public synchronized int matchCount(Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, String internalTypeName, String name, BaseExpression parameters, boolean constructor) {
        int parameterCount = parameters.size();

        String suffixKey = ":" + name + ':' + parameterCount;
        Set<BaseType> setOfParameterTypes = getSetOfParameterTypes(internalTypeName, suffixKey, constructor);

        if (parameterCount == 0 || setOfParameterTypes.size() <= 1 || parameters instanceof LambdaIdentifiersExpression) {
            return setOfParameterTypes.size();
        }
        int counter = 0;

        for (BaseType parameterTypes : setOfParameterTypes) {
            if (match(typeBindings, typeBounds, parameterTypes, parameters)) {
                counter++;
            }
        }

        return counter;
    }

    @SuppressWarnings("all")
    private Set<BaseType> getSetOfParameterTypes(String internalTypeName, String suffixKey, boolean constructor) {
        String key = internalTypeName + suffixKey;
        Set<BaseType> setOfParameterTypes = internalTypeNameMethodNameParameterCountToParameterTypes.get(key);

        if (setOfParameterTypes == null) {
            setOfParameterTypes = new HashSet<>();

            if (!constructor) {
                TypeTypes typeTypes = makeTypeTypes(internalTypeName);

                if (typeTypes != null) {
                    if (typeTypes.getSuperType() != null) {
                        setOfParameterTypes.addAll(getSetOfParameterTypes(typeTypes.getSuperType().getInternalName(), suffixKey, constructor));
                    }
                    if (typeTypes.getInterfaces() != null) {
                        for (Type interfaceType : typeTypes.getInterfaces()) {
                            setOfParameterTypes.addAll(getSetOfParameterTypes(interfaceType.getInternalName(), suffixKey, constructor));
                        }
                    }
                }
            }

            Set<BaseType> declaredParameterTypes = internalTypeNameMethodNameParameterCountToDeclaredParameterTypes.get(key);

            if (declaredParameterTypes == null && loaded.computeIfAbsent(internalTypeName, this::loadFieldsAndMethods)) {
                declaredParameterTypes = internalTypeNameMethodNameParameterCountToDeclaredParameterTypes.get(key);
            }
            if (declaredParameterTypes != null) {
                setOfParameterTypes.addAll(declaredParameterTypes);
            }

            internalTypeNameMethodNameParameterCountToParameterTypes.put(key, setOfParameterTypes);
        }

        return setOfParameterTypes;
    }

    private boolean match(Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, BaseType parameterTypes, BaseExpression parameters) {
        if (parameterTypes.size() != parameters.size()) {
            return false;
        }

        switch (parameterTypes.size()) {
            case 0:
                return true;
            case 1:
                return match(typeBindings, typeBounds, parameterTypes.getFirst(), parameters.getFirst().getType());
            default:
                Iterator<Type> iteratorType = parameterTypes.getList().iterator();
                Iterator<Expression> iteratorExpression = parameters.getList().iterator();

                while (iteratorType.hasNext()) {
                    if (!match(typeBindings, typeBounds, iteratorType.next(), iteratorExpression.next().getType())) {
                        return false;
                    }
                }

                return true;
        }
    }

    boolean match(Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, Type leftType, Type rightType) {
        if (ObjectType.TYPE_OBJECT.equals(leftType) || leftType.equals(rightType)) {
            return true;
        }

        if (leftType.isPrimitiveType() && rightType.isPrimitiveType()) {
            int flags = ((PrimitiveType)leftType).getFlags() | ((PrimitiveType)rightType).getFlags();
            return flags != 0;
        }

        if (rightType instanceof ObjectType toRight) {
            if (leftType  instanceof ObjectType toLeft) {
                return isAssignable(typeBindings, typeBounds, toLeft, toRight);
            }
    
            if (leftType instanceof GenericType gt) {
                BaseType boundType = typeBounds.get(gt.getName());
                if (boundType instanceof ObjectType to && isAssignable(to, toRight)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class TypeTypes {

        private ObjectType thisType;
        private BaseTypeParameter typeParameters;
        private ObjectType superType;
        private BaseType interfaces;

        public BaseTypeParameter getTypeParameters() {
            return typeParameters;
        }

        private void setTypeParameters(BaseTypeParameter typeParameters) {
            this.typeParameters = typeParameters;
        }

        public ObjectType getThisType() {
            return thisType;
        }

        private void setThisType(ObjectType thisType) {
            this.thisType = thisType;
        }

        public BaseType getInterfaces() {
            return interfaces;
        }

        private void setInterfaces(BaseType interfaces) {
            this.interfaces = interfaces;
        }


        public ObjectType getSuperType() {
            return superType;
        }

        private void setSuperType(ObjectType superType) {
            this.superType = superType;
        }
    }

    public static class MethodTypes {

        private BaseTypeParameter typeParameters;
        private BaseType parameterTypes;
        private Type returnedType;
        private BaseType exceptionTypes;
        private int accessFlags;

        public boolean isVarArgs() {
            return (accessFlags & Const.ACC_VARARGS) != 0;
        }
        
        public int getAccessFlags() {
            return accessFlags;
        }

        public void setAccessFlags(int accessFlags) {
            this.accessFlags = accessFlags;
        }

        public BaseType getParameterTypes() {
            return parameterTypes;
        }

        void setParameterTypes(BaseType parameterTypes) {
            this.parameterTypes = parameterTypes;
        }

        public Type getReturnedType() {
            return returnedType;
        }

        void setReturnedType(Type returnedType) {
            this.returnedType = returnedType;
        }

        public BaseType getExceptionTypes() {
            return exceptionTypes;
        }

        void setExceptionTypes(BaseType exceptionTypes) {
            this.exceptionTypes = exceptionTypes;
        }

        public BaseTypeParameter getTypeParameters() {
            return typeParameters;
        }

        private void setTypeParameters(BaseTypeParameter typeParameters) {
            this.typeParameters = typeParameters;
        }

        public void handlePolymorphicSignature(String typeName, String name) {
            if ("java/lang/invoke/MethodHandle".equals(typeName) || "java/lang/invoke/VarHandle".equals(typeName)) {
                try {
                    String fullyQualifiedName = typeName.replace('/', '.');
                    Class<?> clazz = Class.forName(fullyQualifiedName);
                    for (java.lang.reflect.Method method : clazz.getMethods()) {
                        if (method.getName().equals(name)) {
                            for (java.lang.annotation.Annotation annotation : method.getAnnotations()) {
                                if (annotation.annotationType().getSimpleName().contains("PolymorphicSignature")) {
                                    setReturnedType(new GenericType("XXX"));
                                }
                            }
                        }
                    }
                } catch (SecurityException | ClassNotFoundException e) {
                    assert ExceptionUtil.printStackTrace(e);
                }
            }
        }
    }
}
