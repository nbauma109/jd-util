/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.declaration;

public interface BaseTypeDeclaration extends BaseMemberDeclaration {
    /**
     * @return internal name on a single type, else first public top-level type
     */
    String getInternalTypeName();

    /**
     * @return modifier flags on a single type, else 0
     */
    int getFlags();
}
