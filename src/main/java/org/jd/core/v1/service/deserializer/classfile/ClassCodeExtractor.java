package org.jd.core.v1.service.deserializer.classfile;

import org.jd.core.v1.service.converter.classfiletojavasyntax.util.ClassFormatException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ClassCodeExtractor {

    private ClassCodeExtractor() {
    }

    public static final class MethodKey {
        public final String name;
        public final String descriptor;

        public MethodKey(String name, String descriptor) {
            this.name = name;
            this.descriptor = descriptor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodKey other = (MethodKey) o;
            return Objects.equals(name, other.name) &&
                   Objects.equals(descriptor, other.descriptor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, descriptor);
        }
    }

    public static final class Code {
        public final int maxStack;
        public final int maxLocals;
        public final byte[] code;

        public Code(int maxStack, int maxLocals, byte[] code) {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
            this.code = code;
        }
    }

    public static Map<MethodKey, Code> extractCode(byte[] classBytes) {
        Cursor c = new Cursor(classBytes);

        if (c.u4() != 0xCAFEBABE)
            throw new ClassFormatException("Not a classfile");
        c.u2(); // minor
        c.u2(); // major

        int cpCount = c.u2();
        Object[] cp = new Object[cpCount];
        for (int i = 1; i < cpCount; i++) {
            int tag = c.u1();
            switch (tag) {
                case 1:
                    cp[i] = c.utf8(c.u2());
                    break; // Utf8
                case 3:
                case 4:
                    c.skip(4);
                    break; // int/float
                case 5:
                case 6:
                    c.skip(8);
                    i++;
                    break; // long/double (2 slots)
                case 7:
                case 8:
                    c.skip(2);
                    break; // class/string
                case 9:
                case 10:
                case 11:
                case 12:
                    c.skip(4);
                    break; // refs & name/type
                case 15:
                    c.skip(3);
                    break; // MethodHandle
                case 16:
                    c.skip(2);
                    break; // MethodType
                case 17:
                case 18:
                    c.skip(4);
                    break; // Dynamic/InvokeDynamic
                case 19:
                case 20:
                    c.skip(2);
                    break; // Module/Package
                default:
                    throw new IllegalArgumentException("Unknown CP tag: " + tag);
            }
        }

        c.u2(); // access_flags
        c.u2(); // this_class
        c.u2(); // super_class

        int interfacesCount = c.u2();
        c.skip(2 * interfacesCount);

        int fieldsCount = c.u2();
        for (int i = 0; i < fieldsCount; i++)
            skipMember(c);

        int methodsCount = c.u2();
        Map<MethodKey, Code> out = new LinkedHashMap<>(methodsCount * 2);
        for (int i = 0; i < methodsCount; i++) {
            c.u2(); // access
            String name = utf8(cp, c.u2());
            String desc = utf8(cp, c.u2());
            int attrCount = c.u2();

            Code code = null;
            for (int a = 0; a < attrCount; a++) {
                String attrName = utf8(cp, c.u2());
                int attrLen = c.u4();
                if ("Code".equals(attrName)) {
                    int maxStack = c.u2();
                    int maxLocals = c.u2();
                    int codeLen = c.u4();
                    byte[] codeBytes = c.bytes(codeLen);
                    int exCount = c.u2();
                    c.skip(8 * exCount); // exception_table
                    int caCount = c.u2();
                    for (int ca = 0; ca < caCount; ca++) {
                        c.u2();
                        int l = c.u4();
                        c.skip(l);
                    }
                    code = new Code(maxStack, maxLocals, codeBytes);
                } else {
                    c.skip(attrLen);
                }
            }
            out.put(new MethodKey(name, desc), code); // may be null
        }

        int classAttrCount = c.u2();
        for (int i = 0; i < classAttrCount; i++) {
            c.u2();
            int l = c.u4();
            c.skip(l);
        }

        return out;
    }

    private static void skipMember(Cursor c) {
        c.u2();
        c.u2();
        c.u2(); // access, name_index, descriptor_index
        int ac = c.u2();
        for (int i = 0; i < ac; i++) {
            c.u2();
            int l = c.u4();
            c.skip(l);
        }
    }

    private static String utf8(Object[] cp, int index) {
        Object v = cp[index];
        if (v instanceof String s)
            return s;
        throw new IllegalArgumentException("CP[" + index + "] not Utf8");
    }

    private static final class Cursor {
        final byte[] b;
        int p;

        Cursor(byte[] b) {
            this.b = b;
        }

        int u1() {
            return b[p++] & 0xFF;
        }

        int u2() {
            int v = ((b[p] & 0xFF) << 8) | (b[p + 1] & 0xFF);
            p += 2;
            return v;
        }

        int u4() {
            int v = ((b[p] & 0xFF) << 24) | ((b[p + 1] & 0xFF) << 16) | ((b[p + 2] & 0xFF) << 8) | (b[p + 3] & 0xFF);
            p += 4;
            return v;
        }

        void skip(int len) {
            p += len;
        }

        String utf8(int len) {
            String s = new String(b, p, len, StandardCharsets.UTF_8);
            p += len;
            return s;
        }

        byte[] bytes(int len) {
            byte[] out = Arrays.copyOfRange(b, p, p + len);
            p += len;
            return out;
        }
    }
}
