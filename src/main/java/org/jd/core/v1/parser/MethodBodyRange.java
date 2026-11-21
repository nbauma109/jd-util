/*
 * Copyright (c) 2026 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser;

import org.jd.core.v1.model.javasyntax.type.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Where the body of a method or constructor sits in a Java source file.
 *
 * @param typeInternalName  internal name of the declaring type (for example {@code p/Outer$Inner})
 * @param methodName        the method name, or {@code <init>} for constructors
 * @param parameterTypes    erased simple names of the parameter types ({@code String}, {@code int[]}...)
 * @param bodyStart         offset in the source of the opening brace of the body
 * @param bodyEnd           offset in the source just after the closing brace of the body
 */
public record MethodBodyRange(String typeInternalName,
                              String methodName,
                              List<String> parameterTypes,
                              int bodyStart,
                              int bodyEnd) {

  public MethodBodyRange {
    parameterTypes = Collections.unmodifiableList(new ArrayList<>(parameterTypes));
  }

  /**
   * A key that identifies the method regardless of how the types are written in the source
   * (qualified or imported, with or without type arguments).
   */
  public String key() {
    return typeInternalName + '#' + methodName + '(' + String.join(",", parameterTypes) + ')';
  }

  /** Offsets in {@code source} of the first character of each line (lines end with LF, CRLF or a lone CR). */
  static int[] lineStartOffsets(String source) {
    List<Integer> starts = new ArrayList<>();
    starts.add(0);
    for (int i = 0; i < source.length(); i++) {
      char c = source.charAt(i);
      if (c == '\n' || (c == '\r' && (i + 1 >= source.length() || source.charAt(i + 1) != '\n'))) {
        starts.add(i + 1);
      }
    }
    return starts.stream().mapToInt(Integer::intValue).toArray();
  }

  static String erasedSimpleName(Type type) {
    String name = type.getName();
    int cut = Math.max(name.lastIndexOf('.'), Math.max(name.lastIndexOf('/'), name.lastIndexOf('$')));
    StringBuilder sb = new StringBuilder(cut >= 0 ? name.substring(cut + 1) : name);
    for (int i = 0; i < type.getDimension(); i++) {
      sb.append("[]");
    }
    return sb.toString();
  }
}
