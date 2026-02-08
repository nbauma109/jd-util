package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.declaration.ArrayVariableInitializer;
import org.jd.core.v1.model.javasyntax.declaration.ExpressionVariableInitializer;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.jd.core.v1.model.javasyntax.expression.NoExpression.NO_EXPRESSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


public class ArrayVariableInitializerTest {
    private static final int LINE_NUMBER = 1;
    private static final Type TEST_TYPE = ObjectType.TYPE_STRING;

    @Test
    public void testArrayVariableInitializer() {
        Expression expression1 = new IntegerConstantExpression(1, 42);
        Expression expression2 = new StringConstantExpression(2, "Hello");

        ExpressionVariableInitializer variableInitializer1 = new ExpressionVariableInitializer(expression1);
        ExpressionVariableInitializer variableInitializer2 = new ExpressionVariableInitializer(expression2);

        ArrayVariableInitializer arrayInitializer1 = new ArrayVariableInitializer(TEST_TYPE);
        arrayInitializer1.add(variableInitializer1);
        arrayInitializer1.add(variableInitializer2);

        ArrayVariableInitializer arrayInitializer2 = new ArrayVariableInitializer(TEST_TYPE);
        arrayInitializer2.add(variableInitializer1);
        arrayInitializer2.add(variableInitializer2);

        assertEquals(2, arrayInitializer1.size());
        assertEquals(variableInitializer1, arrayInitializer1.get(0));
        assertEquals(variableInitializer2, arrayInitializer1.get(1));
        assertEquals(TEST_TYPE, arrayInitializer1.getType());
        assertEquals(1, arrayInitializer1.getLineNumber());

        assertEquals(arrayInitializer1, arrayInitializer2); // Same elements
        assertEquals(arrayInitializer2, arrayInitializer1); // Reversed order
        assertNotEquals(arrayInitializer1, null);

        TestDeclarationVisitor visitor = new TestDeclarationVisitor();
        arrayInitializer1.accept(visitor);
        assertEquals(1, visitor.getArrayVariableInitializerCount());

        int hashCode1 = arrayInitializer1.hashCode();
        int hashCode2 = arrayInitializer2.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(arrayInitializer1.isExpressionVariableInitializer());
        assertEquals(NO_EXPRESSION, arrayInitializer1.getExpression());
    }

    @Test
    public void testAcceptWithTestVisitor() {
        TestVisitor visitor = new TestVisitor();

        ArrayVariableInitializer arrayInitializer = new ArrayVariableInitializer(TEST_TYPE);
        NewInitializedArray newInitializedArray = new NewInitializedArray(LINE_NUMBER, TEST_TYPE, arrayInitializer);

        newInitializedArray.accept(visitor);

        assertEquals(1, visitor.getNewInitializedArrayCount());
    }
}
