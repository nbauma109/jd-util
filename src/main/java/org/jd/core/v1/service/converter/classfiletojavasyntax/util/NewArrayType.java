/*
 * Copyright (c) 2008, 2025 Emmanuel Dupuy and other contributors.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.converter.classfiletojavasyntax.util;
/**
 * Operand values for the NEWARRAY instruction (see JVM Spec §newarray).
 * ASM does not expose these, so we provide them here
 * as a drop-in replacement for BCEL's Const.T_*.
 */
public final class NewArrayType {
  private NewArrayType() {}

  public static final int T_BOOLEAN = 4;
  public static final int T_CHAR    = 5;
  public static final int T_FLOAT   = 6;
  public static final int T_DOUBLE  = 7;
  public static final int T_BYTE    = 8;
  public static final int T_SHORT   = 9;
  public static final int T_INT     = 10;
  public static final int T_LONG    = 11;
}
