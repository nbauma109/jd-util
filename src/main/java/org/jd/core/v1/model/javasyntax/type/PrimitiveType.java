/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;

import java.util.Map;

public class PrimitiveType implements Type {
    public static final int FLAG_BOOLEAN = 1;
    public static final int FLAG_CHAR    = 2;
    public static final int FLAG_FLOAT   = 4;
    public static final int FLAG_DOUBLE  = 8;
    public static final int FLAG_BYTE    = 16;
    public static final int FLAG_SHORT   = 32;
    public static final int FLAG_INT     = 64;
    public static final int FLAG_LONG    = 128;
    public static final int FLAG_VOID    = 256;

    /** Type, type = ..., ... = type */
    public static final PrimitiveType TYPE_BOOLEAN                = new PrimitiveType("boolean",                PrimitiveType.FLAG_BOOLEAN,                                         PrimitiveType.FLAG_BOOLEAN,                                         PrimitiveType.FLAG_BOOLEAN); //$NON-NLS-1$
    public static final PrimitiveType TYPE_BYTE                   = new PrimitiveType("byte",                   PrimitiveType.FLAG_BYTE,                                            PrimitiveType.FLAG_BYTE,                                            PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_INT|PrimitiveType.FLAG_SHORT); //$NON-NLS-1$
    public static final PrimitiveType TYPE_CHAR                   = new PrimitiveType("char",                   PrimitiveType.FLAG_CHAR,                                            PrimitiveType.FLAG_CHAR,                                            PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    public static final PrimitiveType TYPE_DOUBLE                 = new PrimitiveType("double",                 PrimitiveType.FLAG_DOUBLE,                                          PrimitiveType.FLAG_DOUBLE,                                          PrimitiveType.FLAG_DOUBLE); //$NON-NLS-1$
    public static final PrimitiveType TYPE_FLOAT                  = new PrimitiveType("float",                  PrimitiveType.FLAG_FLOAT,                                           PrimitiveType.FLAG_FLOAT,                                           PrimitiveType.FLAG_FLOAT); //$NON-NLS-1$
    public static final PrimitiveType TYPE_INT                    = new PrimitiveType("int",                    PrimitiveType.FLAG_INT,                                             PrimitiveType.FLAG_INT|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT,              PrimitiveType.FLAG_INT); //$NON-NLS-1$
    public static final PrimitiveType TYPE_LONG                   = new PrimitiveType("long",                   PrimitiveType.FLAG_LONG,                                            PrimitiveType.FLAG_LONG,                                            PrimitiveType.FLAG_LONG); //$NON-NLS-1$
    public static final PrimitiveType TYPE_SHORT                  = new PrimitiveType("short",                  PrimitiveType.FLAG_SHORT,                                           PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_BYTE,                                 PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    public static final PrimitiveType TYPE_VOID                   = new PrimitiveType("void",                   PrimitiveType.FLAG_VOID,                                            PrimitiveType.FLAG_VOID,                                            PrimitiveType.FLAG_VOID); //$NON-NLS-1$

    /** 32768 .. 65535 */
    public static final PrimitiveType MAYBE_CHAR_TYPE             = new PrimitiveType("maybe_char",             PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_INT,                                   PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_INT,                                   PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** 128 .. 32767 */
    public static final PrimitiveType MAYBE_SHORT_TYPE            = new PrimitiveType("maybe_short",            PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,                        PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,                        PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** 2 .. 127 */
    public static final PrimitiveType MAYBE_BYTE_TYPE             = new PrimitiveType("maybe_byte",             PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,              PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,              PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** 0 .. 1 */
    public static final PrimitiveType MAYBE_BOOLEAN_TYPE          = new PrimitiveType("maybe_boolean",          PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT, PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT, PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_CHAR|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** -128 .. -1 */
    public static final PrimitiveType MAYBE_NEGATIVE_BYTE_TYPE    = new PrimitiveType("maybe_negative_byte",    PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,                        PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,                        PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** -32768 .. -129 */
    public static final PrimitiveType MAYBE_NEGATIVE_SHORT_TYPE   = new PrimitiveType("maybe_negative_short",   PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,                                  PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,                                  PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** Otherwise. */
    public static final PrimitiveType MAYBE_INT_TYPE              = new PrimitiveType("maybe_int",              PrimitiveType.FLAG_INT,                                             PrimitiveType.FLAG_INT,                                             PrimitiveType.FLAG_INT); //$NON-NLS-1$
    /** Boolean or negative. */
    public static final PrimitiveType MAYBE_NEGATIVE_BOOLEAN_TYPE = new PrimitiveType("maybe_negative_boolean", PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,           PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT,           PrimitiveType.FLAG_BOOLEAN|PrimitiveType.FLAG_BYTE|PrimitiveType.FLAG_SHORT|PrimitiveType.FLAG_INT); //$NON-NLS-1$

    protected static final PrimitiveType[] descriptorToType = new PrimitiveType['Z' - 'B' + 1];

    static {
        PrimitiveType.descriptorToType[0]         = PrimitiveType.TYPE_BYTE;
        PrimitiveType.descriptorToType['C' - 'B'] = PrimitiveType.TYPE_CHAR;
        PrimitiveType.descriptorToType['D' - 'B'] = PrimitiveType.TYPE_DOUBLE;
        PrimitiveType.descriptorToType['F' - 'B'] = PrimitiveType.TYPE_FLOAT;
        PrimitiveType.descriptorToType['I' - 'B'] = PrimitiveType.TYPE_INT;
        PrimitiveType.descriptorToType['J' - 'B'] = PrimitiveType.TYPE_LONG;
        PrimitiveType.descriptorToType['S' - 'B'] = PrimitiveType.TYPE_SHORT;
        PrimitiveType.descriptorToType['V' - 'B'] = PrimitiveType.TYPE_VOID;
        PrimitiveType.descriptorToType['Z' - 'B'] = PrimitiveType.TYPE_BOOLEAN;
    }

    private final String name;
    private final int flags;
    private final int leftFlags;
    private final int rightFlags;
    private final String descriptor;

    protected PrimitiveType(String name, int flags, int leftFlags, int rightFlags) {
        this.name = name;
        this.flags = flags;
        this.leftFlags = leftFlags;
        this.rightFlags = rightFlags;

        StringBuilder sb = new StringBuilder();

        if ((flags & PrimitiveType.FLAG_DOUBLE) != 0) {
            sb.append('D');
        } else if ((flags & PrimitiveType.FLAG_FLOAT) != 0) {
            sb.append('F');
        } else if ((flags & PrimitiveType.FLAG_LONG) != 0) {
            sb.append('J');
        } else if ((flags & PrimitiveType.FLAG_BOOLEAN) != 0) {
            sb.append('Z');
        } else if ((flags & PrimitiveType.FLAG_BYTE) != 0) {
            sb.append('B');
        } else if ((flags & PrimitiveType.FLAG_CHAR) != 0) {
            sb.append('C');
        } else if ((flags & PrimitiveType.FLAG_SHORT) != 0) {
            sb.append('S');
        } else {
            sb.append('I');
        }

        this.descriptor = sb.toString();
    }

    public static PrimitiveType getPrimitiveType(char primitiveDescriptor) {
        return PrimitiveType.descriptorToType[primitiveDescriptor - 'B'];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public int getDimension() {
        return 0;
    }

    public int getFlags() {
        return flags;
    }

    public int getLeftFlags() {
        return leftFlags;
    }

    public int getRightFlags() {
        return rightFlags;
    }

    @Override
    public Type createType(int dimension) {
        if (dimension < 0) {
            throw new IllegalArgumentException("PrimitiveType.createType(dim) : create type with negative dimension"); //$NON-NLS-1$
        }
        if (dimension == 0) {
            return this;
        }
        return new ObjectType(descriptor, dimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrimitiveType that = (PrimitiveType) o;

        return flags == that.flags;
    }

    @Override
    public int hashCode() {
        return 750_039_781 + flags;
    }

    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(TypeArgumentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isTypeArgumentAssignableFrom(TypeMaker typeMaker, Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, BaseTypeArgument typeArgument) {
        return equals(typeArgument);
    }

    @Override
    public boolean isPrimitiveType() {
        return true;
    }

    @Override
    public boolean isPrimitiveTypeArgument() {
        return true;
    }

    @Override
    public String toString() {
        return "PrimitiveType{primitive=" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public int getJavaPrimitiveFlags() {
        if ((flags & PrimitiveType.FLAG_BOOLEAN) != 0) {
            return PrimitiveType.FLAG_BOOLEAN;
        }
        if ((flags & PrimitiveType.FLAG_INT) != 0) {
            return PrimitiveType.FLAG_INT;
        }
        if ((flags & PrimitiveType.FLAG_CHAR) != 0) {
            return PrimitiveType.FLAG_CHAR;
        }
        if ((flags & PrimitiveType.FLAG_SHORT) != 0) {
            return PrimitiveType.FLAG_SHORT;
        }
        if ((flags & PrimitiveType.FLAG_BYTE) != 0) {
            return PrimitiveType.FLAG_BYTE;
        }

        return flags;
    }
}
