package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.declaration.AbstractNopDeclarationVisitor;
import org.jd.core.v1.model.javasyntax.declaration.AnnotationDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ArrayVariableInitializer;
import org.jd.core.v1.model.javasyntax.declaration.BodyDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ClassDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ConstructorDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.EnumDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.EnumDeclaration.Constant;
import org.jd.core.v1.model.javasyntax.declaration.ExpressionVariableInitializer;
import org.jd.core.v1.model.javasyntax.declaration.FieldDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.FieldDeclarator;
import org.jd.core.v1.model.javasyntax.declaration.FieldDeclarators;
import org.jd.core.v1.model.javasyntax.declaration.FormalParameter;
import org.jd.core.v1.model.javasyntax.declaration.FormalParameters;
import org.jd.core.v1.model.javasyntax.declaration.InterfaceDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclarator;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclarators;
import org.jd.core.v1.model.javasyntax.declaration.MemberDeclarations;
import org.jd.core.v1.model.javasyntax.declaration.MethodDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.StaticInitializerDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.TypeDeclarations;

public class TestDeclarationVisitor extends AbstractNopDeclarationVisitor {

    private int annotationDeclarationCount;
    private int arrayVariableInitializerCount;
    private int bodyDeclarationCount;
    private int classDeclarationCount;
    private int constructorDeclarationCount;
    private int enumDeclarationCount;
    private int constantCount;
    private int expressionVariableInitializerCount;
    private int fieldDeclarationCount;
    private int fieldDeclaratorCount;
    private int fieldDeclaratorsCount;
    private int formalParameterCount;
    private int formalParametersCount;
    private int interfaceDeclarationCount;
    private int localVariableDeclarationCount;
    private int localVariableDeclaratorCount;
    private int localVariableDeclaratorsCount;
    private int methodDeclarationCount;
    private int memberDeclarationsCount;
    private int moduleDeclarationCount;
    private int staticInitializerDeclarationCount;
    private int typeDeclarationsCount;


    public void visit(AnnotationDeclaration declaration) {
        super.visit(declaration);
        annotationDeclarationCount++;
    }

    public void visit(ArrayVariableInitializer declaration) {
        super.visit(declaration);
        arrayVariableInitializerCount++;
    }

    public void visit(BodyDeclaration declaration) {
        super.visit(declaration);
        bodyDeclarationCount++;
    }

    public void visit(ClassDeclaration declaration) {
        super.visit(declaration);
        classDeclarationCount++;
    }

    public void visit(ConstructorDeclaration declaration) {
        super.visit(declaration);
        constructorDeclarationCount++;
    }

    public void visit(EnumDeclaration declaration) {
        super.visit(declaration);
        enumDeclarationCount++;
    }

    public void visit(Constant declaration) {
        super.visit(declaration);
        constantCount++;
    }

    public void visit(ExpressionVariableInitializer declaration) {
        super.visit(declaration);
        expressionVariableInitializerCount++;
    }

    public void visit(FieldDeclaration declaration) {
        super.visit(declaration);
        fieldDeclarationCount++;
    }

    public void visit(FieldDeclarator declaration) {
        super.visit(declaration);
        fieldDeclaratorCount++;
    }

    public void visit(FieldDeclarators declarations) {
        super.visit(declarations);
        fieldDeclaratorsCount++;
    }

    public void visit(FormalParameter declaration) {
        super.visit(declaration);
        formalParameterCount++;
    }

    public void visit(FormalParameters declarations) {
        super.visit(declarations);
        formalParametersCount++;
    }

    public void visit(InterfaceDeclaration declaration) {
        super.visit(declaration);
        interfaceDeclarationCount++;
    }

    public void visit(LocalVariableDeclaration declaration) {
        super.visit(declaration);
        localVariableDeclarationCount++;
    }

    public void visit(LocalVariableDeclarator declarator) {
        super.visit(declarator);
        localVariableDeclaratorCount++;
    }

    public void visit(LocalVariableDeclarators declarators) {
        super.visit(declarators);
        localVariableDeclaratorsCount++;
    }

    public void visit(MethodDeclaration declaration) {
        super.visit(declaration);
        methodDeclarationCount++;
    }

    public void visit(MemberDeclarations declarations) {
        super.visit(declarations);
        memberDeclarationsCount++;
    }

    public void visit(ModuleDeclaration declaration) {
        super.visit(declaration);
        moduleDeclarationCount++;
    }

    public void visit(StaticInitializerDeclaration declaration) {
        super.visit(declaration);
        staticInitializerDeclarationCount++;
    }

    public void visit(TypeDeclarations declarations) {
        super.visit(declarations);
        typeDeclarationsCount++;
    }

    // --- getters & setters ---

    public int getAnnotationDeclarationCount() {
        return annotationDeclarationCount;
    }

    public void setAnnotationDeclarationCount(int annotationDeclarationCount) {
        this.annotationDeclarationCount = annotationDeclarationCount;
    }

    public int getArrayVariableInitializerCount() {
        return arrayVariableInitializerCount;
    }

    public void setArrayVariableInitializerCount(int arrayVariableInitializerCount) {
        this.arrayVariableInitializerCount = arrayVariableInitializerCount;
    }

    public int getBodyDeclarationCount() {
        return bodyDeclarationCount;
    }

    public void setBodyDeclarationCount(int bodyDeclarationCount) {
        this.bodyDeclarationCount = bodyDeclarationCount;
    }

    public int getClassDeclarationCount() {
        return classDeclarationCount;
    }

    public void setClassDeclarationCount(int classDeclarationCount) {
        this.classDeclarationCount = classDeclarationCount;
    }

    public int getConstructorDeclarationCount() {
        return constructorDeclarationCount;
    }

    public void setConstructorDeclarationCount(int constructorDeclarationCount) {
        this.constructorDeclarationCount = constructorDeclarationCount;
    }

    public int getEnumDeclarationCount() {
        return enumDeclarationCount;
    }

    public void setEnumDeclarationCount(int enumDeclarationCount) {
        this.enumDeclarationCount = enumDeclarationCount;
    }

    public int getConstantCount() {
        return constantCount;
    }

    public void setConstantCount(int constantCount) {
        this.constantCount = constantCount;
    }

    public int getExpressionVariableInitializerCount() {
        return expressionVariableInitializerCount;
    }

    public void setExpressionVariableInitializerCount(int expressionVariableInitializerCount) {
        this.expressionVariableInitializerCount = expressionVariableInitializerCount;
    }

    public int getFieldDeclarationCount() {
        return fieldDeclarationCount;
    }

    public void setFieldDeclarationCount(int fieldDeclarationCount) {
        this.fieldDeclarationCount = fieldDeclarationCount;
    }

    public int getFieldDeclaratorCount() {
        return fieldDeclaratorCount;
    }

    public void setFieldDeclaratorCount(int fieldDeclaratorCount) {
        this.fieldDeclaratorCount = fieldDeclaratorCount;
    }

    public int getFieldDeclaratorsCount() {
        return fieldDeclaratorsCount;
    }

    public void setFieldDeclaratorsCount(int fieldDeclaratorsCount) {
        this.fieldDeclaratorsCount = fieldDeclaratorsCount;
    }

    public int getFormalParameterCount() {
        return formalParameterCount;
    }

    public void setFormalParameterCount(int formalParameterCount) {
        this.formalParameterCount = formalParameterCount;
    }

    public int getFormalParametersCount() {
        return formalParametersCount;
    }

    public void setFormalParametersCount(int formalParametersCount) {
        this.formalParametersCount = formalParametersCount;
    }

    public int getInterfaceDeclarationCount() {
        return interfaceDeclarationCount;
    }

    public void setInterfaceDeclarationCount(int interfaceDeclarationCount) {
        this.interfaceDeclarationCount = interfaceDeclarationCount;
    }

    public int getLocalVariableDeclarationCount() {
        return localVariableDeclarationCount;
    }

    public void setLocalVariableDeclarationCount(int localVariableDeclarationCount) {
        this.localVariableDeclarationCount = localVariableDeclarationCount;
    }

    public int getLocalVariableDeclaratorCount() {
        return localVariableDeclaratorCount;
    }

    public void setLocalVariableDeclaratorCount(int localVariableDeclaratorCount) {
        this.localVariableDeclaratorCount = localVariableDeclaratorCount;
    }

    public int getLocalVariableDeclaratorsCount() {
        return localVariableDeclaratorsCount;
    }

    public void setLocalVariableDeclaratorsCount(int localVariableDeclaratorsCount) {
        this.localVariableDeclaratorsCount = localVariableDeclaratorsCount;
    }

    public int getMethodDeclarationCount() {
        return methodDeclarationCount;
    }

    public void setMethodDeclarationCount(int methodDeclarationCount) {
        this.methodDeclarationCount = methodDeclarationCount;
    }

    public int getMemberDeclarationsCount() {
        return memberDeclarationsCount;
    }

    public void setMemberDeclarationsCount(int memberDeclarationsCount) {
        this.memberDeclarationsCount = memberDeclarationsCount;
    }

    public int getModuleDeclarationCount() {
        return moduleDeclarationCount;
    }

    public void setModuleDeclarationCount(int moduleDeclarationCount) {
        this.moduleDeclarationCount = moduleDeclarationCount;
    }

    public int getStaticInitializerDeclarationCount() {
        return staticInitializerDeclarationCount;
    }

    public void setStaticInitializerDeclarationCount(int staticInitializerDeclarationCount) {
        this.staticInitializerDeclarationCount = staticInitializerDeclarationCount;
    }

    public int getTypeDeclarationsCount() {
        return typeDeclarationsCount;
    }

    public void setTypeDeclarationsCount(int typeDeclarationsCount) {
        this.typeDeclarationsCount = typeDeclarationsCount;
    }
}
