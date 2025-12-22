/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.util.DefaultList;

import static org.apache.bcel.Const.ACC_PUBLIC;

public class TypeDeclarations extends DefaultList<MemberDeclaration> implements BaseTypeDeclaration {
    private static final long serialVersionUID = 1L;

    @Override
    public void accept(DeclarationVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getInternalTypeName() {
        if (isEmpty()) {
            return "";
        }

        for (MemberDeclaration member : this) {
            if (member instanceof BaseTypeDeclaration btd && (btd.getFlags() & ACC_PUBLIC) != 0) {
                String internalName = btd.getInternalTypeName();
                if (internalName != null && !internalName.isEmpty()) {
                    return internalName;
                }
            }
        }

        return "";
    }

    @Override
    public int getFlags() {
        throw new UnsupportedOperationException();
    }

}
