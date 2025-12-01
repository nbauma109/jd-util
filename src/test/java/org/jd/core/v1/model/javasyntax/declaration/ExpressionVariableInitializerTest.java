package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ExpressionVariableInitializerTest {

    @Test
    public void testExpressionVariableInitializer() {
        // Arrange
        int lineNumber = 10;
        Expression expression = new IntegerConstantExpression(lineNumber, 20);

        // Act
        ExpressionVariableInitializer initializer = new ExpressionVariableInitializer(expression);

        // Assert
        assertEquals(expression, initializer.getExpression());
        assertEquals(lineNumber, initializer.getLineNumber());
        assertTrue(initializer.isExpressionVariableInitializer());

        // Act & Assert
        Expression newExpression = new IntegerConstantExpression(lineNumber, 30);
        initializer.setExpression(newExpression);
        assertEquals(newExpression, initializer.getExpression());
        initializer.setExpression(expression);

        // Act & Assert for equals and hashCode
        ExpressionVariableInitializer initializer2 = new ExpressionVariableInitializer(expression);
        Object o = initializer;
        assertEquals(o, initializer);
        assertNotEquals(initializer, null);
        assertNotEquals(initializer, new Object());
        assertEquals(initializer, initializer2);
        assertEquals(initializer.hashCode(), initializer2.hashCode());
    }

    @Test
    public void testVisitor() {
        // Arrange
        int lineNumber = 10;
        Expression expression = new IntegerConstantExpression(lineNumber, 20);
        ExpressionVariableInitializer initializer = new ExpressionVariableInitializer(expression);
        TestDeclarationVisitor visitor = new TestDeclarationVisitor();

        // Act
        initializer.accept(visitor);

        // Assert
        assertEquals(1, visitor.getExpressionVariableInitializerCount());
    }
}
