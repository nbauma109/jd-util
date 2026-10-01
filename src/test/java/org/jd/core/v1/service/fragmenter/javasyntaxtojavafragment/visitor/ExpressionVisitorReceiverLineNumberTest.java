/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.FieldReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util.JavaFragmentFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.bcel.Const.MAJOR_1_5;
import static org.junit.Assert.assertEquals;

/** A line which is known is never dropped: a receiver on an earlier line than its call keeps its own line number. */
public class ExpressionVisitorReceiverLineNumberTest {

    private static final ObjectType TYPE = ObjectType.TYPE_OBJECT;

    private static List<Integer> lineNumbers(Expression expression) {
        StatementVisitor visitor = new StatementVisitor(new ClassPathLoader(), "test/Test", MAJOR_1_5, JavaFragmentFactory.newImportsFragment());
        visitor.tokens = visitor.new Tokens();
        expression.accept(visitor);

        List<Integer> lineNumbers = new ArrayList<>();

        for (Token token : visitor.tokens) {
            if (token instanceof LineNumberToken lineNumberToken) {
                lineNumbers.add(lineNumberToken.lineNumber());
            }
        }
        return lineNumbers;
    }

    private static Expression field(int lineNumber) {
        return new FieldReferenceExpression(lineNumber, TYPE, new LocalVariableReferenceExpression(TYPE, "this"), "test/Other", "f", "Ljava/lang/Object;");
    }

    private static Expression call(int lineNumber, Expression receiver) {
        return new MethodInvocationExpression(lineNumber, TYPE, receiver, "test/Other", "m", "()V", null, null);
    }

    @Test
    public void receiverOnAnEarlierLineKeepsItsLineNumber() {
        assertEquals(List.of(2, 10), lineNumbers(call(10, field(2))).subList(0, 2));
    }

    @Test
    public void receiverOnTheSameLineIsPrecededByTheLineOfTheCall() {
        assertEquals(10, (int) lineNumbers(call(10, field(10))).get(0));
    }
}
