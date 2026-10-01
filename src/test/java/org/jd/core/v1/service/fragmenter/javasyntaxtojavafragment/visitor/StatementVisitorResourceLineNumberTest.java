/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.statement.TryStatement;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.token.LineNumberToken;
import org.jd.core.v1.model.token.Token;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util.JavaFragmentFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.bcel.Const.MAJOR_1_5;
import static org.junit.Assert.assertEquals;

/** The line of a chain of calls is the line of its last call: a resource starts at the line of the first one. */
public class StatementVisitorResourceLineNumberTest {

    private static final ObjectType TYPE = ObjectType.TYPE_OBJECT;

    private static List<Integer> lineNumbersOfResource(Expression expression) {
        StatementVisitor visitor = new StatementVisitor(new ClassPathLoader(), "test/Test", MAJOR_1_5, JavaFragmentFactory.newImportsFragment());
        visitor.tokens = visitor.new Tokens();
        visitor.visit(new TryStatement.Resource(TYPE, "r", expression));

        List<Integer> lineNumbers = new ArrayList<>();

        for (Token token : visitor.tokens) {
            if (token instanceof LineNumberToken lineNumberToken) {
                lineNumbers.add(lineNumberToken.lineNumber());
            }
        }
        return lineNumbers;
    }

    private static Expression variable(int lineNumber) {
        return new LocalVariableReferenceExpression(lineNumber, TYPE, "v");
    }

    private static Expression call(int lineNumber, Expression receiver) {
        return new MethodInvocationExpression(lineNumber, TYPE, receiver, "test/Other", "m", "()V", null, null);
    }

    @Test
    public void startsAtTheLineOfTheFirstCallOfTheChain() {
        assertEquals(5, (int) lineNumbersOfResource(call(10, call(7, variable(5)))).get(0));
    }

    @Test
    public void unknownLinesOfTheReceiversAreIgnored() {
        assertEquals(10, (int) lineNumbersOfResource(call(10, call(0, variable(0)))).get(0));
    }

    @Test
    public void receiversOnALaterLineDoNotMoveTheStart() {
        assertEquals(10, (int) lineNumbersOfResource(call(10, call(12, variable(15)))).get(0));
    }

    @Test
    public void startsAtTheLineOfTheCallWhenItHasNoKnownLineButItsReceiverHas() {
        assertEquals(4, (int) lineNumbersOfResource(call(0, variable(4))).get(0));
    }
}
