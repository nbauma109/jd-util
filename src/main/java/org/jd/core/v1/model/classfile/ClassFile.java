/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.classfile;

import org.jd.core.v1.service.deserializer.classfile.ClassCodeExtractor.MethodKey;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassFile {

    private final ClassNode classNode;
    private ClassFile outerClassFile;
    private List<ClassFile> innerClassFiles;

    /** Map key: methodName+desc → cleaned byte[] */
    private final Map<MethodKey, byte[]> cleanedCode = new HashMap<>();

    public ClassFile(ClassNode classNode) {
        this.classNode = classNode;
    }

    // --- Accessors to mirror old API, but now delegate to ASM ClassNode ---

    public ClassNode getClassNode() {
        return classNode;
    }

    public boolean isModule() {
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_MODULE) != 0;
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
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_ABSTRACT) != 0;
    }

    public final boolean isEnum() {
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_ENUM) != 0;
    }

    public final boolean isInterface() {
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_INTERFACE) != 0;
    }

    public final boolean isPublic() {
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_PUBLIC) != 0;
    }

    public final boolean isStatic() {
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_STATIC) != 0;
    }

    public int getMajorVersion() {
        return classNode.version & 0xFFFF;
    }

    public int getMinorVersion() {
        return classNode.version >>> 16;
    }

    public final boolean isClass() {
        return !isInterface() && !isEnum() && !isAnnotation();
    }

    public final int getAccessFlags() {
        return classNode.access;
    }

    public final void setAccessFlags(int accessFlags) {
        classNode.access = accessFlags;
    }

    public boolean isAnnotation() {
        return (classNode.access & org.objectweb.asm.Opcodes.ACC_ANNOTATION) != 0;
    }

    public List<MethodNode> getMethods() {
        return classNode.methods;
    }

    public List<org.objectweb.asm.tree.FieldNode> getFields() {
        return classNode.fields;
    }

    public List<String> getInterfaceTypeNames() {
        return classNode.interfaces;
    }

    public String getSuperTypeName() {
        return classNode.superName;
    }

    public String getInternalTypeName() {
        return classNode.name;
    }

    // --- Cleaned code storage (replaces BCEL Method.getCode().getCode()) ---

    public void setCleanedCode(String name, String desc, byte[] code) {
        cleanedCode.put(new MethodKey(name, desc), code == null ? new byte[0] : code);
    }

    public byte[] getCleanedCode(String name, String desc) {
        return cleanedCode.getOrDefault(new MethodKey(name, desc), new byte[0]);
    }

    public boolean isAInnerClass() {
        return outerClassFile != null;
    }

    @Override
    public String toString() {
        return "ClassFile{" + getInternalTypeName() + "}";
    }

}
