/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 *
 * Original source code location :
 * https://github.com/java-decompiler/jd-gui/blob/master/services/src/main/java/org/jd/gui/view/component/TypePage.java
 */
package jd.core.links;

import org.jd.core.v1.util.StringConstants;

public class ReferenceData {

    private String typeName;
    /**
     * Field or method name or null for type
     */
    private final String name;
    /**
     * Field or method descriptor or null for type
     */
    private final String descriptor;
    /**
     * Internal type name containing reference or null for "import" statement.
     * Used to high light items matching with URI like "file://dir1/dir2/file?highlightPattern=hello&highlightFlags=drtcmfs&highlightScope=type".
     */
    private final String owner;
    /**
     * "Enabled" flag for link of reference
     */
    private boolean enabled;

    public ReferenceData(String typeName, String name, String descriptor, String owner) {
        this.setTypeName(typeName);
        this.name = name;
        this.descriptor = descriptor;
        this.owner = owner;
    }

    public boolean isAType() { return getName() == null; }
    public boolean isAField() { return getDescriptor() != null && getDescriptor().charAt(0) != '('; }
    public boolean isAMethod() { return getDescriptor() != null && getDescriptor().charAt(0) == '('; }
    public boolean isAConstructor() { return StringConstants.INSTANCE_CONSTRUCTOR.equals(getName()); }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ReferenceData [typeName=" + typeName + ", name=" + name + ", descriptor=" + descriptor + ", owner=" + owner + ", enabled=" + enabled + "]";
    }
}
