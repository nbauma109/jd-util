/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser;

import org.jd.core.v1.model.javasyntax.JavaImport;
import org.jd.core.v1.model.javasyntax.declaration.BaseTypeDeclaration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record JavaParseResult(String packageName,
                              List<JavaImport> imports,
                              BaseTypeDeclaration typeDeclaration) {

  public JavaParseResult {
    packageName = packageName == null ? "" : packageName;
    imports = Collections.unmodifiableList(Objects.requireNonNull(imports, "imports"));
    Objects.requireNonNull(typeDeclaration, "typeDeclaration");
  }
}

