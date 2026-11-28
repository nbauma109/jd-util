/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.deserializer.classfile;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.Method;
import org.apache.commons.lang3.Validate;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.util.DefaultList;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static org.apache.bcel.Const.ACC_SYNTHETIC;

import jd.core.process.analyzer.instruction.bytecode.util.ByteCodeUtil;

public final class ClassFileDeserializer {

    public ClassFile loadClassFile(Loader loader, String internalTypeName) throws IOException {
        Validate.isTrue(loader.canLoad(internalTypeName), "Class %s could not be loaded", internalTypeName);

        byte[] data = loader.load(internalTypeName);

        Validate.notNull(data, "Class %s could not be loaded", internalTypeName);

        try (DataInputStream reader = new DataInputStream(new ByteArrayInputStream(data))) {

            // Load main type
            ClassParser classParser = new ClassParser(reader, internalTypeName);
            ClassFile classFile = new ClassFile(classParser.parse());
            loadInnerClasses(loader, internalTypeName, classFile);
            for (Method method : classFile.getMethods()) {
                Code methodCode = method.getCode();
                if (methodCode == null) {
                    continue;
                }
                ByteCodeUtil.cleanUpByteCode(methodCode.getCode());
            }
            return classFile;
        }
    }

    private void loadInnerClasses(Loader loader, String internalTypeName, ClassFile classFile) throws IOException {
        InnerClasses innerClasses = classFile.getAttribute(Const.ATTR_INNER_CLASSES);
        if (innerClasses == null) {
            return;
        }

        // Resolve inner classes in the same multi-release directory, with fallback to the base entry.
        int versionedEnd = 0;
        if (internalTypeName.startsWith("META-INF/versions/")) {
            versionedEnd = internalTypeName.indexOf('/', "META-INF/versions/".length()) + 1;
        }
        String versionPrefix = internalTypeName.substring(0, versionedEnd);
        String baseInternalTypeName = internalTypeName.substring(versionedEnd);
        DefaultList<ClassFile> innerClassFiles = new DefaultList<>();
        for (InnerClass innerClass : innerClasses.getInnerClasses()) {
            ClassFile loaded = loadInnerClass(loader, classFile, innerClass, baseInternalTypeName, versionPrefix);
            if (loaded != null) {
                innerClassFiles.add(loaded);
            }
        }
        if (!innerClassFiles.isEmpty()) {
            classFile.setInnerClassFiles(innerClassFiles);
        }
    }

    private ClassFile loadInnerClass(Loader loader, ClassFile classFile, InnerClass innerClass,
            String baseInternalTypeName, String versionPrefix) throws IOException {
        ConstantPool constantPool = classFile.getConstantPool();
        String innerTypeName = constantPool.getConstantString(innerClass.getInnerClassIndex(), Const.CONSTANT_Class);
        String outerTypeName = null;
        if (innerClass.getOuterClassIndex() != 0) {
            outerTypeName = constantPool.getConstantString(innerClass.getOuterClassIndex(), Const.CONSTANT_Class);
        }
        String innerTypePrefix = baseInternalTypeName + '$';
        if (baseInternalTypeName.equals(innerTypeName)
                || (!baseInternalTypeName.equals(outerTypeName) && !innerTypeName.startsWith(innerTypePrefix))) {
            return null;
        }
        String innerLookupKey = innerTypeName;
        if (!versionPrefix.isEmpty() && loader.canLoad(versionPrefix + innerTypeName)) {
            innerLookupKey = versionPrefix + innerTypeName;
        }
        if (!loader.canLoad(innerLookupKey)) {
            // Missing inner classes cannot be represented by null-backed class files.
            return null;
        }
        ClassFile innerClassFile = loadClassFile(loader, innerLookupKey);
        int flags = innerClass.getInnerAccessFlags();
        int length = innerTypeName.indexOf('$') + 1;
        if (innerTypeName.startsWith(innerTypePrefix)) {
            length = baseInternalTypeName.length() + 1;
        }
        if (Character.isDigit(innerTypeName.charAt(length))) {
            flags |= ACC_SYNTHETIC;
        }
        innerClassFile.setAccessFlags(flags);
        innerClassFile.setOuterClassFile(classFile);
        return innerClassFile;
    }
}
