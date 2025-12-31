/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.declaration;

public class BodyDeclaration implements Declaration {
    private final String internalTypeName;
    protected BaseMemberDeclaration memberDeclarations;
    private boolean anonymous;

    public BodyDeclaration(String internalTypeName, BaseMemberDeclaration memberDeclarations) {
        this.internalTypeName = internalTypeName;
        this.memberDeclarations = memberDeclarations;
    }

    public String getInternalTypeName() {
        return internalTypeName;
    }

    public BaseMemberDeclaration getMemberDeclarations() {
        return memberDeclarations;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    @Override
    public void accept(DeclarationVisitor visitor) {
        visitor.visit(this);
    }

    public boolean isEmpty() {
        return memberDeclarations == null || memberDeclarations.size() == 0;
    }
}
