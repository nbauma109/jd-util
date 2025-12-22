/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax;

import java.util.Objects;

public final class JavaImport {
    private final String qualifiedName;
    private final String internalName;
    private final boolean isStatic;
    private final boolean isOnDemand;
    private int counter = 1;

    public JavaImport(final String qualifiedName, final boolean isStatic, final boolean isOnDemand) {
        this.qualifiedName = Objects.requireNonNull(qualifiedName, "qualifiedName");
        this.internalName = qualifiedName.replace('.', '/');
        this.isStatic = isStatic;
        this.isOnDemand = isOnDemand;
    }

    public JavaImport(String internalName, String qualifiedName) {
        this.qualifiedName = Objects.requireNonNull(qualifiedName, "qualifiedName");
        this.internalName = internalName;
        this.isStatic = false;
        this.isOnDemand = false;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isOnDemand() {
        return isOnDemand;
    }

    public int getCounter() {
        return counter;
    }

    public void incCounter() {
        counter++;
    }

    public String getInternalName() {
        return internalName;
    }

    public String toSourceForm() {
        final StringBuilder sb = new StringBuilder();
        sb.append("import ");
        if (isStatic) {
            sb.append("static ");
        }
        sb.append(qualifiedName);
        if (isOnDemand) {
            sb.append(".*");
        }
        sb.append(";");
        return sb.toString();
    }
}
