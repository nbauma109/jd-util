/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.expression.BaseExpression;
import org.jd.core.v1.model.javasyntax.expression.CastExpression;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.Expressions;
import org.jd.core.v1.model.javasyntax.expression.FieldReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.NewExpression;
import org.jd.core.v1.model.javasyntax.expression.NoExpression;
import org.jd.core.v1.model.javasyntax.expression.ObjectTypeReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Some compilers (ECJ) attribute a call to the line of its last argument or of its closing parenthesis: the line of the
 * call must then not be emitted before the lines of its arguments.
 */
public class ExpressionVisitorLineNumberTest {

    private static final ObjectType TYPE = ObjectType.TYPE_OBJECT;

    private static StatementVisitor newVisitor() {
        StatementVisitor visitor = new StatementVisitor(new ClassPathLoader(), "test/Test", MAJOR_1_5, JavaFragmentFactory.newImportsFragment());
        visitor.tokens = visitor.new Tokens();
        return visitor;
    }

    private static List<Integer> lineNumbers(StatementVisitor visitor) {
        List<Integer> lineNumbers = new ArrayList<>();

        for (Token token : visitor.tokens) {
            if (token instanceof LineNumberToken lineNumberToken) {
                lineNumbers.add(lineNumberToken.lineNumber());
            }
        }
        return lineNumbers;
    }

    private static Expression variable(int lineNumber) {
        return new LocalVariableReferenceExpression(lineNumber, TYPE, "v" + lineNumber);
    }

    private static BaseExpression parameters(Expression... parameters) {
        Expressions list = new Expressions();

        for (Expression parameter : parameters) {
            list.add(parameter);
        }
        return list;
    }

    private static MethodInvocationExpression call(int lineNumber, Expression receiver, BaseExpression parameters) {
        return new MethodInvocationExpression(lineNumber, TYPE, receiver, "test/Other", "m", "()V", parameters, null);
    }

    @Test
    public void staticCallIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        Expression receiver = new ObjectTypeReferenceExpression(ObjectType.TYPE_STRING);

        call(10, receiver, parameters(variable(3), variable(7))).accept(visitor);

        assertEquals(List.of(3, 7), lineNumbers(visitor));
    }

    @Test
    public void staticCallKeepsItsLineWhenTheArgumentsAreClose() {
        StatementVisitor visitor = newVisitor();
        Expression receiver = new ObjectTypeReferenceExpression(ObjectType.TYPE_STRING);

        call(10, receiver, parameters(variable(9), variable(10))).accept(visitor);

        assertEquals(10, (int) lineNumbers(visitor).get(0));
    }

    @Test
    public void callOnAFieldIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        Expression receiver = new FieldReferenceExpression(2, TYPE, new LocalVariableReferenceExpression(TYPE, "this"), "test/Other", "f", "Ljava/lang/Object;");

        call(10, receiver, parameters(variable(5))).accept(visitor);

        // The receiver is on an earlier line than the call, so are the arguments
        assertEquals(List.of(2, 5), lineNumbers(visitor));
    }

    @Test
    public void callOnAnotherCallIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        Expression receiver = call(1, variable(1), null);

        call(10, receiver, parameters(variable(4))).accept(visitor);

        List<Integer> lineNumbers = lineNumbers(visitor);

        assertEquals(1, (int) lineNumbers.get(0));
        assertEquals(4, (int) lineNumbers.get(lineNumbers.size() - 1));
        assertFalse(lineNumbers.contains(10));
    }

    @Test
    public void callWithoutArgumentsKeepsItsLine() {
        StatementVisitor visitor = newVisitor();

        call(10, NoExpression.NO_EXPRESSION, null).accept(visitor);

        assertEquals(List.of(10), lineNumbers(visitor));
    }

    @Test
    public void constantsDoNotDeferTheCall() {
        StatementVisitor visitor = newVisitor();

        call(10, NoExpression.NO_EXPRESSION, parameters(new StringConstantExpression(2, "a"))).accept(visitor);

        assertEquals(10, (int) lineNumbers(visitor).get(0));
    }

    @Test
    public void resourceStartsAtTheLineOfTheFirstCallOfItsChain() {
        StatementVisitor visitor = newVisitor();
        Expression chain = call(10, call(7, variable(5), null), null);

        visitor.visit(new TryStatement.Resource(TYPE, "r", chain));

        assertEquals(5, (int) lineNumbers(visitor).get(0));
    }

    @Test
    public void callWithASingleMultilineArgumentIsDeferred() {
        StatementVisitor visitor = newVisitor();
        // The only argument is a chain of calls on lines 3 to 7
        Expression chain = call(7, call(3, variable(3), null), null);

        call(10, NoExpression.NO_EXPRESSION, parameters(chain)).accept(visitor);

        List<Integer> lineNumbers = lineNumbers(visitor);

        assertEquals(3, (int) lineNumbers.get(0));
        assertFalse(lineNumbers.contains(10));
    }

    @Test
    public void resourceCallWithEarlierArgumentsIsNotAnchoredOnItsOwnLine() {
        StatementVisitor visitor = newVisitor();

        visitor.visit(new TryStatement.Resource(TYPE, "r", call(10, NoExpression.NO_EXPRESSION, parameters(variable(3), variable(7)))));

        assertEquals(List.of(3, 7), lineNumbers(visitor));
    }

    @Test
    public void callWithAMultilineArgumentInsideAnotherExpressionIsDeferred() {
        StatementVisitor visitor = newVisitor();
        // The only argument is a cast (not a call) wrapping a chain of calls on lines 3 to 7
        Expression cast = new CastExpression(7, TYPE, call(7, call(3, variable(3), null), null));

        call(10, NoExpression.NO_EXPRESSION, parameters(cast)).accept(visitor);

        List<Integer> lineNumbers = lineNumbers(visitor);

        // The line of the outer call is not emitted before the lines of the argument
        assertEquals(7, (int) lineNumbers.get(0));
        assertFalse(lineNumbers.contains(10));
    }

    @Test
    public void callWithACallArgumentWhoseOwnArgumentIsEarlierIsDeferred() {
        StatementVisitor visitor = newVisitor();
        // The only argument is a call on line 7, whose own argument is on line 3
        Expression argument = call(7, NoExpression.NO_EXPRESSION, parameters(variable(3)));

        call(10, NoExpression.NO_EXPRESSION, parameters(argument)).accept(visitor);

        assertFalse(lineNumbers(visitor).contains(10));
    }

    @Test
    public void callOnAFieldWithoutLineIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        // The receiver has no line of its own
        Expression receiver = new FieldReferenceExpression(0, TYPE, new LocalVariableReferenceExpression(TYPE, "this"), "test/Other", "f", "Ljava/lang/Object;");

        call(10, receiver, parameters(variable(3), variable(7))).accept(visitor);

        assertEquals(3, (int) lineNumbers(visitor).get(0));
    }

    @Test
    public void callOnAFieldOnTheSameLineIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        // The receiver and the call are on the late line
        Expression receiver = new FieldReferenceExpression(10, TYPE, new LocalVariableReferenceExpression(TYPE, "this"), "test/Other", "f", "Ljava/lang/Object;");

        call(10, receiver, parameters(variable(3), variable(7))).accept(visitor);

        assertEquals(List.of(3, 7), lineNumbers(visitor));
    }

    @Test
    public void callIsDeferredWhenALaterArgumentAddsALineToTheOneOfTheStatement() {
        StatementVisitor visitor = newVisitor();
        // The statement has already reached line 3, the first argument is on that line, the second one is on line 7
        visitor.tokens.addLineNumberToken(3);

        call(10, NoExpression.NO_EXPRESSION, parameters(variable(3), variable(7))).accept(visitor);

        List<Integer> lineNumbers = lineNumbers(visitor);

        assertTrue(lineNumbers.contains(7));
        assertFalse(lineNumbers.contains(10));
    }

    @Test
    public void objectCreationIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        NewExpression creation = new NewExpression(10, TYPE, "()V", false, false);
        creation.setParameters(parameters(variable(3), variable(7)));

        creation.accept(visitor);

        assertEquals(List.of(3, 7), lineNumbers(visitor));
    }

    @Test
    public void nestedDeferredReceiversKeepTheLineNumbersSuppressed() {
        StatementVisitor visitor = newVisitor();
        // service.make(a, b).field.outer(c, d): both calls and the field are on the late line 10
        Expression inner = call(10, variable(10), parameters(variable(3), variable(4)));
        Expression field = new FieldReferenceExpression(10, TYPE, inner, "test/Other", "f", "Ljava/lang/Object;");

        call(10, field, parameters(variable(5), variable(7))).accept(visitor);

        assertFalse(lineNumbers(visitor).contains(10));
    }

    @Test
    public void staticCallOnTheSameLineIsDeferredToItsArguments() {
        StatementVisitor visitor = newVisitor();
        // The type and the call are on the late line
        Expression receiver = new ObjectTypeReferenceExpression(10, ObjectType.TYPE_STRING);

        call(10, receiver, parameters(variable(3), variable(7))).accept(visitor);

        assertEquals(List.of(3, 7), lineNumbers(visitor));
    }

    @Test
    public void staticCallKeepsItsLineAfterItsReceiverWhenNotDeferred() {
        StatementVisitor visitor = newVisitor();
        Expression receiver = new ObjectTypeReferenceExpression(9, ObjectType.TYPE_STRING);

        call(10, receiver, null).accept(visitor);

        assertEquals(List.of(9, 10), lineNumbers(visitor));
    }
}
