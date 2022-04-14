package jd.core.links;

import org.jd.core.v1.util.StringConstants;

public class DeclarationData {

    private final int startPosition;
    private final int endPosition;
    private final String typeName;
    /**
     * Field or method name or null for type
     */
    private final String name;
    private final String descriptor;

    public DeclarationData(int startPosition, int length, String typeName, String name, String descriptor) {
        this.startPosition = startPosition;
        this.endPosition = startPosition + length;
        this.typeName = typeName;
        this.name = name;
        this.descriptor = descriptor;
    }

    public boolean isAType() { return getName() == null; }
    public boolean isAField() { return descriptor != null && descriptor.charAt(0) != '('; }
    public boolean isAMethod() { return descriptor != null && descriptor.charAt(0) == '('; }
    public boolean isAConstructor() { return StringConstants.INSTANCE_CONSTRUCTOR.equals(getName()); }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DeclarationData [typeName=" + typeName + ", name=" + name + ", descriptor=" + descriptor + "]";
    }
}
