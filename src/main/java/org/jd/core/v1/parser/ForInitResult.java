/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser;

import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclaration;
import org.jd.core.v1.model.javasyntax.expression.BaseExpression;

public record ForInitResult(LocalVariableDeclaration declaration, BaseExpression init) {
}
