/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.util;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.javasyntax.type.Type;

import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_BOOLEAN;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_BYTE;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_CHAR;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_DOUBLE;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_FLOAT;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_INT;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_LONG;
import static org.jd.core.v1.service.converter.classfiletojavasyntax.util.NewArrayType.T_SHORT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_BOOLEAN;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_BYTE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_CHAR;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_DOUBLE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_FLOAT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_INT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_LONG;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_SHORT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.FLAG_VOID;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_BOOLEAN_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_BYTE_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_CHAR_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_INT_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_NEGATIVE_BOOLEAN_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_NEGATIVE_BYTE_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_NEGATIVE_SHORT_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.MAYBE_SHORT_TYPE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_BOOLEAN;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_BYTE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_CHAR;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_DOUBLE;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_FLOAT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_INT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_LONG;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_SHORT;
import static org.jd.core.v1.model.javasyntax.type.PrimitiveType.TYPE_VOID;

public final class PrimitiveTypeUtil {

    private PrimitiveTypeUtil() {
        super();
    }

    public static Type getPrimitiveTypeFromDescriptor(String descriptor) {
        int dimension = 0;

        while (descriptor.charAt(dimension) == '[') {
            dimension++;
        }

        if (dimension == 0) {
            return PrimitiveType.getPrimitiveType(descriptor.charAt(dimension));
        }
        return new ObjectType(descriptor.substring(dimension), dimension);
    }

    public static PrimitiveType getPrimitiveTypeFromValue(int value) {
        if (value >= 0) {
            if (value <= 1) {
                return MAYBE_BOOLEAN_TYPE;
            }
            if (value <= Byte.MAX_VALUE) {
                return MAYBE_BYTE_TYPE;
            }
            if (value <= Short.MAX_VALUE) {
                return MAYBE_SHORT_TYPE;
            }
            if (value <= Character.MAX_VALUE) {
                return MAYBE_CHAR_TYPE;
            }
        } else {
            if (value >= Byte.MIN_VALUE) {
                return MAYBE_NEGATIVE_BYTE_TYPE;
            }
            if (value >= Short.MIN_VALUE) {
                return MAYBE_NEGATIVE_SHORT_TYPE;
            }
        }
        return MAYBE_INT_TYPE;
    }

    public static PrimitiveType getCommonPrimitiveType(PrimitiveType pt1, PrimitiveType pt2) {
        return getPrimitiveTypeFromFlags(pt1.getFlags() & pt2.getFlags());
    }

    public static PrimitiveType getPrimitiveTypeFromFlags(int flags) {
        return switch (flags) {
            case FLAG_BOOLEAN -> TYPE_BOOLEAN;
            case FLAG_CHAR    -> TYPE_CHAR;
            case FLAG_FLOAT   -> TYPE_FLOAT;
            case FLAG_DOUBLE  -> TYPE_DOUBLE;
            case FLAG_BYTE    -> TYPE_BYTE;
            case FLAG_SHORT   -> TYPE_SHORT;
            case FLAG_INT     -> TYPE_INT;
            case FLAG_LONG    -> TYPE_LONG;
            case FLAG_VOID    -> TYPE_VOID;
            default           -> switch (flags) {
            case FLAG_CHAR|FLAG_INT                                   -> MAYBE_CHAR_TYPE;
            case FLAG_CHAR|FLAG_SHORT|FLAG_INT                        -> MAYBE_SHORT_TYPE;
            case FLAG_BYTE|FLAG_CHAR|FLAG_SHORT|FLAG_INT              -> MAYBE_BYTE_TYPE;
            case FLAG_BOOLEAN|FLAG_BYTE|FLAG_CHAR|FLAG_SHORT|FLAG_INT -> MAYBE_BOOLEAN_TYPE;
            case FLAG_BYTE|FLAG_SHORT|FLAG_INT                        -> MAYBE_NEGATIVE_BYTE_TYPE;
            case FLAG_SHORT|FLAG_INT                                  -> MAYBE_NEGATIVE_SHORT_TYPE;
            case FLAG_BOOLEAN|FLAG_BYTE|FLAG_SHORT|FLAG_INT           -> MAYBE_NEGATIVE_BOOLEAN_TYPE;
            default                                                   -> null;
            };
        };
    }

    public static Type getPrimitiveTypeFromTag(int tag) {
        return switch (tag) {
            case T_BOOLEAN -> TYPE_BOOLEAN;
            case T_CHAR    -> TYPE_CHAR;
            case T_FLOAT   -> TYPE_FLOAT;
            case T_DOUBLE  -> TYPE_DOUBLE;
            case T_BYTE    -> TYPE_BYTE;
            case T_SHORT   -> TYPE_SHORT;
            case T_INT     -> TYPE_INT;
            case T_LONG    -> TYPE_LONG;
            default        -> throw new IllegalStateException();
        };
    }
}
