/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.AbstractJavaSyntaxVisitor;
import org.jd.core.v1.model.javasyntax.declaration.LocalVariableDeclarator;
import org.jd.core.v1.model.javasyntax.expression.ArrayExpression;
import org.jd.core.v1.model.javasyntax.expression.BinaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.CastExpression;
import org.jd.core.v1.model.javasyntax.expression.ConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.EnumConstantReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.FieldReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.InstanceOfExpression;
import org.jd.core.v1.model.javasyntax.expression.LambdaIdentifiersExpression;
import org.jd.core.v1.model.javasyntax.expression.LengthExpression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.SwitchExpression;
import org.jd.core.v1.model.javasyntax.expression.SuperConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.QualifiedSuperExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.NewArray;
import org.jd.core.v1.model.javasyntax.expression.NewExpression;
import org.jd.core.v1.model.javasyntax.expression.NewInitializedArray;
import org.jd.core.v1.model.javasyntax.expression.ObjectTypeReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.ParenthesesExpression;
import org.jd.core.v1.model.javasyntax.expression.PostOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.PreOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.SuperExpression;
import org.jd.core.v1.model.javasyntax.expression.TernaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.ThisExpression;
import org.jd.core.v1.model.javasyntax.expression.TypeReferenceDotClassExpression;

import java.util.Set;
import java.util.TreeSet;

/**
 * Collects all the known line numbers of the expressions it visits (except the constants, which never own a line),
 * whatever their nesting: the receivers and the
 * parameters of the calls, the bodies of lambdas... ({@link SearchFirstKnownLineNumberVisitor} stops at the first
 * known line number and only looks at a part of the children).
 */
public class SearchKnownLineNumbersVisitor extends AbstractJavaSyntaxVisitor {
    private final Set<Integer> lineNumbers = new TreeSet<>();

    public void init() {
        lineNumbers.clear();
    }

    public Set<Integer> getLineNumbers() {
        return lineNumbers;
    }

    private void add(int lineNumber) {
        if (lineNumber > 0) {
            lineNumbers.add(lineNumber);
        }
    }

    @Override
    public void visit(MethodInvocationExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(ArrayExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(BinaryOperatorExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(CastExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(ConstructorInvocationExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(ConstructorReferenceExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(EnumConstantReferenceExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(FieldReferenceExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(InstanceOfExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(LambdaIdentifiersExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(LengthExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(LocalVariableReferenceExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(MethodReferenceExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(NewArray expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(NewExpression expression) {
        add(expression.getLineNumber());
        if (expression.getQualifier() != null) {
            // The qualifier is not part of the default traversal
            expression.getQualifier().accept(this);
        }
        super.visit(expression);
    }

    @Override
    public void visit(NewInitializedArray expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(ObjectTypeReferenceExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(ParenthesesExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(PostOperatorExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(PreOperatorExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(SuperExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(TernaryOperatorExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(ThisExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(TypeReferenceDotClassExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(QualifiedSuperExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(SuperConstructorInvocationExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(SwitchExpression expression) {
        add(expression.getLineNumber());
        super.visit(expression);
    }

    @Override
    public void visit(LocalVariableDeclarator declarator) {
        // The line of a declarator without initializer is written on its own
        add(declarator.getLineNumber());
        super.visit(declarator);
    }
}
