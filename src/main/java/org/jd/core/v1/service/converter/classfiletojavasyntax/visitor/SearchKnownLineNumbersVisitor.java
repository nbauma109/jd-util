/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;

import java.util.Set;
import java.util.TreeSet;

/**
 * Collects all the known line numbers of the expressions and statements it visits, whatever their nesting (the
 * search for the first known line number stops at the first expression which has one).
 */
public class SearchKnownLineNumbersVisitor extends SearchFirstKnownLineNumberVisitor {
    private final Set<Integer> lineNumbers = new TreeSet<>();

    @Override
    public void init() {
        super.init();
        lineNumbers.clear();
    }

    public Set<Integer> getLineNumbers() {
        return lineNumbers;
    }

    @Override
    protected boolean setLineNumberIfValid(int candidateLineNumber) {
        if (candidateLineNumber > 0) {
            lineNumbers.add(candidateLineNumber);
        }
        // Never stop: look at the children too
        return false;
    }

    @Override
    public void visit(MethodInvocationExpression expression) {
        // The search for the first line only looks at the receiver: look at the parameters too
        setLineNumberIfValid(expression.getLineNumber());
        if (expression.getExpression() != null) {
            expression.getExpression().accept(this);
        }
        safeAccept(expression.getParameters());
    }
}
