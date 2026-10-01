/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.expression.ArrayExpression;
import org.jd.core.v1.model.javasyntax.expression.BinaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.CastExpression;
import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.TypeReferenceDotClassExpression;
import org.jd.core.v1.model.javasyntax.expression.ObjectTypeReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.NewInitializedArray;
import org.jd.core.v1.model.javasyntax.expression.NewExpression;
import org.jd.core.v1.model.javasyntax.expression.NewArray;
import org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.LambdaIdentifiersExpression;
import org.jd.core.v1.model.javasyntax.expression.EnumConstantReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.ConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.Expressions;
import org.jd.core.v1.model.javasyntax.expression.FieldReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.InstanceOfExpression;
import org.jd.core.v1.model.javasyntax.expression.LengthExpression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.NoExpression;
import org.jd.core.v1.model.javasyntax.expression.ParenthesesExpression;
import org.jd.core.v1.model.javasyntax.expression.PostOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.PreOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.QualifiedSuperExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.SuperConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.SuperExpression;
import org.jd.core.v1.model.javasyntax.expression.SwitchExpression;
import org.jd.core.v1.model.javasyntax.expression.TernaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.ThisExpression;
import org.jd.core.v1.model.javasyntax.statement.Statements;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchKnownLineNumbersVisitorTest {

    private static final ObjectType TYPE = ObjectType.TYPE_OBJECT;

    private static Expression variable(int lineNumber) {
        return new LocalVariableReferenceExpression(lineNumber, TYPE, "v");
    }

    private static List<Integer> lineNumbers(Expression expression) {
        SearchKnownLineNumbersVisitor visitor = new SearchKnownLineNumbersVisitor();
        visitor.init();
        expression.accept(visitor);
        return List.copyOf(visitor.getLineNumbers());
    }

    @Test
    public void collectsTheLinesOfTheParametersOfACall() {
        Expressions parameters = new Expressions();
        parameters.add(variable(3));
        parameters.add(variable(7));

        assertEquals(List.of(3, 5, 7, 10), lineNumbers(new MethodInvocationExpression(10, TYPE, variable(5), "t/T", "m", "()V", parameters, null)));
    }

    @Test
    public void collectsTheLinesNestedInOperators() {
        Expression ternary = new TernaryOperatorExpression(1, TYPE, variable(2),
                new CastExpression(3, TYPE, new ParenthesesExpression(4, new BinaryOperatorExpression(5, TYPE, variable(6), "+", variable(7)))),
                new ArrayExpression(8, new LengthExpression(9, variable(10)), variable(11)));

        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), lineNumbers(ternary));
    }

    @Test
    public void collectsTheLinesNestedInUnaryOperatorsAndTypeTests() {
        Expression expression = new BinaryOperatorExpression(1, TYPE,
                new PreOperatorExpression(2, "!", new InstanceOfExpression(3, variable(4), TYPE)),
                "&&",
                new PostOperatorExpression(5, new FieldReferenceExpression(6, TYPE, new ThisExpression(7, TYPE), "t/T", "f", "I"), "++"));

        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), lineNumbers(expression));
    }

    @Test
    public void collectsTheLineOfSuper() {
        assertEquals(List.of(4), lineNumbers(new SuperExpression(4, TYPE)));
    }

    @Test
    public void ignoresConstantsAndUnknownLines() {
        assertEquals(List.of(), lineNumbers(new StringConstantExpression(3, "a")));
        assertEquals(List.of(), lineNumbers(new MethodInvocationExpression(0, TYPE, NoExpression.NO_EXPRESSION, "t/T", "m", "()V", null, null)));
    }

    @Test
    public void initForgetsThePreviousLines() {
        SearchKnownLineNumbersVisitor visitor = new SearchKnownLineNumbersVisitor();

        variable(3).accept(visitor);
        visitor.init();
        variable(5).accept(visitor);

        assertEquals(List.of(5), List.copyOf(visitor.getLineNumbers()));
    }

    @Test
    public void collectsTheLinesOfSuperAndSwitchExpressions() {
        Expressions parameters = new Expressions();
        parameters.add(variable(3));

        assertEquals(List.of(3, 4), lineNumbers(new SuperConstructorInvocationExpression(4, TYPE, "()V", parameters, false)));
        assertEquals(List.of(5), lineNumbers(new QualifiedSuperExpression(5, TYPE)));
        // A selector which is a constant has no line of its own: the line of the switch is the only one
        assertEquals(List.of(6), lineNumbers(new SwitchExpression(6, new StringConstantExpression(2, "a"), new ArrayList<>(), TYPE)));
    }

    @Test
    public void collectsTheLinesOfConstructorsAndReferences() {
        Expressions parameters = new Expressions();
        parameters.add(variable(3));

        assertEquals(List.of(3, 4), lineNumbers(new ConstructorInvocationExpression(4, TYPE, "()V", parameters, false)));
        assertEquals(List.of(5), lineNumbers(new ConstructorReferenceExpression(5, TYPE, TYPE, "()V")));
        assertEquals(List.of(6), lineNumbers(new EnumConstantReferenceExpression(6, TYPE, "A")));
        assertEquals(List.of(7), lineNumbers(new LambdaIdentifiersExpression(7, TYPE, TYPE, List.of("x"), new Statements())));
        assertEquals(List.of(8, 9), lineNumbers(new MethodReferenceExpression(9, TYPE, variable(8), "t/T", "m", "()V")));
        assertEquals(List.of(3, 10), lineNumbers(new NewArray(10, TYPE, parameters)));
        assertEquals(List.of(11), lineNumbers(new NewExpression(11, TYPE, "()V", false, false)));
        assertEquals(List.of(12), lineNumbers(new NewInitializedArray(12, TYPE, null)));
        assertEquals(List.of(13), lineNumbers(new ObjectTypeReferenceExpression(13, TYPE)));
        assertEquals(List.of(14), lineNumbers(new TypeReferenceDotClassExpression(14, TYPE)));
    }
}
