/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.javasyntax.type;

import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.util.DefaultList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class TypeArguments extends DefaultList<TypeArgument> implements BaseTypeArgument {

    private static final long serialVersionUID = 1L;

    public TypeArguments() {}

    public TypeArguments(int capacity) {
        super(capacity);
    }

    public TypeArguments(Collection<TypeArgument> list) {
        super(list);
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public boolean isTypeArgumentAssignableFrom(TypeMaker typeMaker, Map<String, TypeArgument> typeBindings, Map<String, BaseType> typeBounds, BaseTypeArgument typeArgument) {
        if (typeArgument instanceof TypeArguments ata) {
            if (size() != ata.size()) {
                return false;
            }
            Iterator<TypeArgument> iterator2 = ata.iterator();

            for (TypeArgument element : this) {
                if (!element.isTypeArgumentAssignableFrom(typeMaker, typeBindings, typeBounds, iterator2.next())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeArgumentList() {
        return true;
    }

    @Override
    public TypeArgument getTypeArgumentFirst() {
        return getFirst();
    }

    @Override
    public DefaultList<TypeArgument> getTypeArgumentList() {
        return this;
    }

    @Override
    public int typeArgumentSize() {
        return size();
    }

    @Override
    public void accept(TypeArgumentVisitor visitor) {
        visitor.visit(this);
    }
}
