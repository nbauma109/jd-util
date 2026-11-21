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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Where the body of a method or constructor sits in a Java source file.
 *
 * @param typeInternalName  internal name of the declaring type (for example {@code p/Outer$Inner})
 * @param methodName        the method name, or {@code <init>} for constructors
 * @param parameterTypes    erased simple names of the parameter types ({@code String}, {@code int[]}...)
 * @param bodyStart         offset in the source of the opening brace of the body
 * @param bodyEnd           offset in the source just after the closing brace of the body
 * @param occurrence        0 for the first method with this signature in the source, 1 for the next one... (local classes of
 *                          different methods may share a name, and so do their methods)
 */
public record MethodBodyRange(String typeInternalName,
                              String methodName,
                              List<String> parameterTypes,
                              int bodyStart,
                              int bodyEnd,
                              int occurrence) {

  public MethodBodyRange(String typeInternalName, String methodName, List<String> parameterTypes, int bodyStart, int bodyEnd) {
    this(typeInternalName, methodName, parameterTypes, bodyStart, bodyEnd, 0);
  }

  public MethodBodyRange {
    parameterTypes = Collections.unmodifiableList(new ArrayList<>(parameterTypes));
  }

  /**
   * A key that identifies the method regardless of how the types are written in the source
   * (qualified or imported, with or without type arguments).
   */
  public String key() {
    String key = typeInternalName + '#' + methodName + '(' + String.join(",", parameterTypes) + ')';
    return occurrence == 0 ? key : key + '@' + occurrence;
  }

  /** The same ranges, numbering the ones that share a signature in the order of the source. */
  static List<MethodBodyRange> withOccurrences(List<MethodBodyRange> ranges) {
    Map<String, Integer> seen = new HashMap<>();
    List<MethodBodyRange> numbered = new ArrayList<>();
    for (MethodBodyRange range : ranges) {
      int occurrence = seen.merge(range.key(), 1, Integer::sum) - 1;
      numbered.add(new MethodBodyRange(range.typeInternalName, range.methodName, range.parameterTypes, range.bodyStart, range.bodyEnd, occurrence));
    }
    return Collections.unmodifiableList(numbered);
  }

  /** The offset where the character ending at {@code lastIndex} starts: a unicode escape (\\u007b, \\uu007b) spans several characters. */
  static int startOfEscape(String source, int lastIndex) {
    int hexStart = lastIndex - 3;
    if (hexStart < 2 || lastIndex >= source.length()) {
      return lastIndex;
    }
    for (int i = hexStart; i <= lastIndex; i++) {
      if (Character.digit(source.charAt(i), 16) < 0) {
        return lastIndex;
      }
    }
    int u = hexStart - 1;
    if (source.charAt(u) != 'u') {
      return lastIndex;
    }
    while (u > 0 && source.charAt(u - 1) == 'u') {
      u--;
    }
    int backslash = u - 1;
    if (backslash < 0 || source.charAt(backslash) != '\\') {
      return lastIndex;
    }
    int backslashes = 0;
    for (int i = backslash; i >= 0 && source.charAt(i) == '\\'; i--) {
      backslashes++;
    }
    return backslashes % 2 == 1 ? backslash : lastIndex;
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
    int cut = Math.max(name.lastIndexOf('.'), name.lastIndexOf('/'));
    StringBuilder sb = new StringBuilder(cut >= 0 ? name.substring(cut + 1) : name);
    for (int i = 0; i < type.getDimension(); i++) {
      sb.append("[]");
    }
    return sb.toString();
  }
}
