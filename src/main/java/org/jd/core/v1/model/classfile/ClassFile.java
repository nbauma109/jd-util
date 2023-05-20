/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.classfile;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.bcel.Const.ACC_MODULE;
import static org.apache.bcel.Const.CONSTANT_Class;

public class ClassFile {

    private final JavaClass javaClass;
    private ClassFile outerClassFile;
    private List<ClassFile> innerClassFiles;

    public ClassFile(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    public boolean isModule() {
        return (javaClass.getAccessFlags() & ACC_MODULE) != 0;
    }

    public ClassFile getOuterClassFile() {
        return outerClassFile;
    }

    public void setOuterClassFile(ClassFile outerClassFile) {
        this.outerClassFile = outerClassFile;
    }

    public List<ClassFile> getInnerClassFiles() {
        return innerClassFiles;
    }

    public void setInnerClassFiles(List<ClassFile> innerClassFiles) {
        this.innerClassFiles = innerClassFiles;
    }

    public final boolean isAbstract() {
        return javaClass.isAbstract();
    }

    public final boolean isEnum() {
        return javaClass.isEnum();
    }

    public final boolean isInterface() {
        return javaClass.isInterface();
    }

    public final boolean isPublic() {
        return javaClass.isPublic();
    }

    public final boolean isStatic() {
        return javaClass.isStatic();
    }

    public int getMajorVersion() {
        return javaClass.getMajor();
    }

    public int getMinorVersion() {
        return javaClass.getMinor();
    }

    public final boolean isClass() {
        return javaClass.isClass();
    }

    public final int getAccessFlags() {
        return javaClass.getAccessFlags();
    }

    public final void setAccessFlags(int accessFlags) {
        javaClass.setAccessFlags(accessFlags);
    }

    public boolean isAnnotation() {
        return javaClass.isAnnotation();
    }

    public Method[] getMethods() {
        return javaClass.getMethods();
    }

    public Field[] getFields() {
        return javaClass.getFields();
    }

    public String[] getInterfaceTypeNames() {
        int[] interfaceIndices = javaClass.getInterfaceIndices();
        String[] interfaceNames = new String[interfaceIndices.length];
        for (int i = 0; i < interfaceNames.length; i++) {
            interfaceNames[i] = getConstantPool().getConstantString(interfaceIndices[i], CONSTANT_Class);
        }
        return interfaceNames;
    }

    public String getSuperTypeName() {
        return getConstantPool().getConstantString(getSuperclassNameIndex(), CONSTANT_Class);
    }

    public int getSuperclassNameIndex() {
        return javaClass.getSuperclassNameIndex();
    }

    public String getInternalTypeName() {
        return Utility.packageToPath(javaClass.getClassName());
    }

    public ConstantPool getConstantPool() {
        return javaClass.getConstantPool();
    }
    
    public Attribute[] getAttributes() {
        return javaClass.getAttributes();
    }

    public AnnotationEntry[] getAnnotationEntries() {
        return javaClass.getAnnotationEntries();
    }

    @SuppressWarnings("unchecked")
    public <T extends Attribute> T getAttribute(byte tag) {
        return (T) Stream.of(javaClass.getAttributes()).filter(a -> a.getTag() == tag).findAny().orElse(null);
    }

    public boolean isAInnerClass() {
        return outerClassFile != null;
    }

    @Override
    public String toString() {
        return "ClassFile{" + getInternalTypeName() + "}";
    }
}
