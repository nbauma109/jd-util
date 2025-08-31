/*
 * Copyright (c) 2008, 2025 Emmanuel Dupuy and other contributors.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.converter.classfiletojavasyntax.util;
/**
 * JVM constant pool tag numbers (see JVM Spec §4.4).
 * ASM does not expose these, so we provide them here
 * as a drop-in replacement for BCEL's Const.CONSTANT_*.
 */
public final class ConstantPoolTags {
  private ConstantPoolTags() {}

  public static final int CONSTANT_Utf8               = 1;
  public static final int CONSTANT_Integer            = 3;
  public static final int CONSTANT_Float              = 4;
  public static final int CONSTANT_Long               = 5;
  public static final int CONSTANT_Double             = 6;
  public static final int CONSTANT_Class              = 7;
  public static final int CONSTANT_String             = 8;
  public static final int CONSTANT_Fieldref           = 9;
  public static final int CONSTANT_Methodref          = 10;
  public static final int CONSTANT_InterfaceMethodref = 11;
  public static final int CONSTANT_NameAndType        = 12;
  public static final int CONSTANT_MethodHandle       = 15;
  public static final int CONSTANT_MethodType         = 16;
  public static final int CONSTANT_Dynamic            = 17; // Java 11+
  public static final int CONSTANT_InvokeDynamic      = 18;
  public static final int CONSTANT_Module             = 19; // Java 9+
  public static final int CONSTANT_Package            = 20; // Java 9+
}
