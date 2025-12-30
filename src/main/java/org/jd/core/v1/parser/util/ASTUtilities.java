/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser.util;

import org.jd.core.v1.model.javasyntax.declaration.BaseTypeDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.MemberDeclaration;
import org.jd.core.v1.model.javasyntax.declaration.TypeDeclarations;
import org.jd.core.v1.model.javasyntax.expression.BaseExpression;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.Expressions;
import org.jd.core.v1.model.javasyntax.expression.NoExpression;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.NoStatement;
import org.jd.core.v1.model.javasyntax.statement.Statement;
import org.jd.core.v1.model.javasyntax.statement.Statements;
import org.jd.core.v1.parser.ParseException;

import java.util.List;

public class ASTUtilities {

    public ASTUtilities() {
    }

    public static BaseStatement toBaseStatement(List<Statement> list) {
        if (list == null || list.isEmpty()) {
            return NoStatement.NO_STATEMENT;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return new Statements(list);
    }

    public static void appendBaseStatement(List<Statement> out, BaseStatement bs) {
        if (bs == null) {
            return;
        }
        if (bs instanceof NoStatement) {
            return;
        }
        if (bs instanceof Statements ss) {
            out.addAll(ss);
            return;
        }
        if (bs instanceof Statement s) {
            out.add(s);
        }
    }

    public static BaseExpression toBaseExpression(List<Expression> list) {
        if (list == null || list.isEmpty()) {
            return NoExpression.NO_EXPRESSION;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return new Expressions(list);
    }

    public static BaseTypeDeclaration toBaseTypeDeclaration(List<MemberDeclaration> topLevelTypes)
            throws ParseException {
        if (topLevelTypes == null || topLevelTypes.isEmpty()) {
            throw new ParseException("No top-level type declaration found");
        }
        if (topLevelTypes.size() == 1) {
            return (BaseTypeDeclaration) topLevelTypes.get(0);
        }

        TypeDeclarations decls = new TypeDeclarations();
        decls.addAll(topLevelTypes);
        return decls;
    }
}
