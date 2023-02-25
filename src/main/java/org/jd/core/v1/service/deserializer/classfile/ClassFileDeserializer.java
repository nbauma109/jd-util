/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.deserializer.classfile;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.util.DefaultList;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static org.apache.bcel.Const.ACC_SYNTHETIC;

public final class ClassFileDeserializer {

    public ClassFile loadClassFile(Loader loader, String internalTypeName) throws IOException {
        ClassFile classFile = innerLoadClassFile(loader, internalTypeName);

        if (classFile == null) {
            throw new IllegalArgumentException("Class '" + internalTypeName + "' could not be loaded");
        }
        return classFile;
    }

    private ClassFile innerLoadClassFile(Loader loader, String internalTypeName) throws IOException {
        if (!loader.canLoad(internalTypeName)) {
            return null;
        }

        byte[] data = loader.load(internalTypeName);

        if (data == null) {
            return null;
        }

        try (DataInputStream reader = new DataInputStream(new ByteArrayInputStream(data))) {

            // Load main type
            ClassParser classParser = new ClassParser(reader, internalTypeName);
            ClassFile classFile = new ClassFile(classParser.parse());
            InnerClasses innerClasses = classFile.getAttribute(Const.ATTR_INNER_CLASSES);

            // Load inner types
            if (innerClasses != null) {
                DefaultList<ClassFile> innerClassFiles = new DefaultList<>();
                String innerTypePrefix = internalTypeName + '$';

                for (InnerClass ic : innerClasses.getInnerClasses()) {
                    ConstantPool cp = classFile.getConstantPool();
                    String innerTypeName = cp.getConstantString(ic.getInnerClassIndex(), Const.CONSTANT_Class);
                    String outerTypeName = ic.getOuterClassIndex() == 0 ? null : cp.getConstantString(ic.getOuterClassIndex(), Const.CONSTANT_Class);

                    if (!internalTypeName.equals(innerTypeName) && (internalTypeName.equals(outerTypeName) || innerTypeName.startsWith(innerTypePrefix))) {
                        ClassFile innerClassFile = innerLoadClassFile(loader, innerTypeName);
                        int flags = ic.getInnerAccessFlags();
                        int length;

                        if (innerTypeName.startsWith(innerTypePrefix)) {
                            length = internalTypeName.length() + 1;
                        } else {
                            length = innerTypeName.indexOf('$') + 1;
                        }

                        if (Character.isDigit(innerTypeName.charAt(length))) {
                            flags |= ACC_SYNTHETIC;
                        }

                        if (innerClassFile == null) {
                            // Inner class not found. Create an empty one.
                            innerClassFile = new ClassFile(null);
                        }

                        innerClassFile.setOuterClassFile(classFile);
                        innerClassFile.setAccessFlags(flags);
                        innerClassFiles.add(innerClassFile);
                    }
                }

                if (!innerClassFiles.isEmpty()) {
                    classFile.setInnerClassFiles(innerClassFiles);
                }
            }
            return classFile;
        }
    }
}
