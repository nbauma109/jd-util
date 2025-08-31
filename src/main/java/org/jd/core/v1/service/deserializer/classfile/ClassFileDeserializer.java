/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.deserializer.classfile;

import jd.core.process.analyzer.instruction.bytecode.util.ByteCodeUtil;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public final class ClassFileDeserializer {

    public static ClassFile loadClassFile(Loader loader, String internalTypeName) throws IOException {
        if (loader == null || !loader.canLoad(internalTypeName)) {
            throw new IllegalArgumentException("Class " + internalTypeName + " could not be loaded");
        }

        final byte[] data = loader.load(internalTypeName);
        if (data == null) {
            throw new IllegalArgumentException("Class " + internalTypeName + " could not be loaded");
        }

        // 1) Extract all Code attributes (maxStack, maxLocals, raw code[]) directly from bytes.
        final Map<ClassCodeExtractor.MethodKey, ClassCodeExtractor.Code> codeMap =
                ClassCodeExtractor.extractCode(data);

        // 2) Build ASM tree (we still want all the structural info).
        final ClassReader cr = new ClassReader(data);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_FRAMES);

        // 3) Wrap in our model.
        final ClassFile cf = new ClassFile(cn);

        // 4) Clean and store per-method code[] when present.
        for (MethodNode mn : cn.methods) {
            final ClassCodeExtractor.MethodKey key =
                    new ClassCodeExtractor.MethodKey(mn.name, mn.desc);

            final ClassCodeExtractor.Code code = codeMap.get(key);
            if (code == null) {
                continue; // abstract/native or no Code attribute
            }

            // Work on a copy, then clean.
            final byte[] cleaned = Arrays.copyOf(code.code, code.code.length);
            ByteCodeUtil.cleanUpByteCode(cleaned);

            // Save cleaned bytes so the rest of your pipeline can use them.
            cf.setCleanedCode(mn.name, mn.desc, cleaned);
        }

        return cf;
    }
}
