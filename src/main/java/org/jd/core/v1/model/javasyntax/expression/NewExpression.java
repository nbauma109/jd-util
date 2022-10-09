/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.declaration.BodyDeclaration;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;

public class NewExpression extends AbstractLineNumberExpression {
    protected ObjectType type;
    protected String descriptor;
    protected BaseExpression parameters;
    private final BodyDeclaration bodyDeclaration;
    private boolean diamondPossible;
    private Expression qualifier;
    private final boolean varArgs;

    public NewExpression(int lineNumber, ObjectType type, String descriptor, boolean varArgs, boolean diamondPossible) {
        this(lineNumber, type, descriptor, null, varArgs, diamondPossible);
    }

    public NewExpression(int lineNumber, ObjectType type, String descriptor, BodyDeclaration bodyDeclaration, boolean varArgs, boolean diamondPossible) {
        super(lineNumber);
        this.type = type;
        this.descriptor = descriptor;
        this.bodyDeclaration = bodyDeclaration;
        this.diamondPossible = diamondPossible;
        this.varArgs = varArgs;
    }

    public boolean isVarArgs() {
        return varArgs;
    }
    
    @Override
    public ObjectType getObjectType() {
        return type;
    }

    public void setObjectType(ObjectType type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public BaseExpression getParameters() {
        return parameters;
    }

    public void setParameters(BaseExpression parameters) {
        this.parameters = parameters;
    }

    public BodyDeclaration getBodyDeclaration() {
        return bodyDeclaration;
    }
    
    public boolean isDiamondPossible() {
        return diamondPossible;
    }
    
    public void setDiamondPossible(boolean diamondPossible) {
        this.diamondPossible = diamondPossible;
    }

    public Expression getQualifier() {
        return qualifier;
    }

    public void setQualifier(Expression qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public boolean isNewExpression() { return true; }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NewExpression{new " + type + "}";
    }

    @Override
    public Expression copyTo(int lineNumber) {
        return new NewExpression(lineNumber, type, descriptor, bodyDeclaration, varArgs, diamondPossible);
    }
}
