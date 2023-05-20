package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Assert;
import org.junit.Test;

public class LocalVariableDeclaratorTest {

    @Test
    public void testNameGetterAndSetter() {
        // Arrange
        String expectedName = "myVariable";
        LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(expectedName);

        // Act
        localVariableDeclarator.setName("newVariable");
        String actualName = localVariableDeclarator.getName();

        // Assert
        Assert.assertEquals("newVariable", actualName);
    }

    @Test
    public void testDimensionGetterAndSetter() {
        // Arrange
        String name = "myVariable";
        LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(name);

        // Act
        localVariableDeclarator.setDimension(5);
        int actualDimension = localVariableDeclarator.getDimension();

        // Assert
        Assert.assertEquals(5, actualDimension);
    }

    @Test
    public void testVariableInitializerGetter() {
        // Arrange
        String name = "myVariable";
        Expression expression = new IntegerConstantExpression(1, 42);
        ExpressionVariableInitializer variableInitializer = new ExpressionVariableInitializer(expression);
        LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(1, name, variableInitializer);

        // Act
        VariableInitializer actualVariableInitializer = localVariableDeclarator.getVariableInitializer();

        // Assert
        Assert.assertEquals(variableInitializer, actualVariableInitializer);
    }

    @Test
    public void testToStringMethod() {
        // Arrange
        String name = "myVariable";
        int dimension = 3;
        Expression expression = new StringConstantExpression(2, "Hello");
        ExpressionVariableInitializer variableInitializer = new ExpressionVariableInitializer(expression);
        LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(1, name, variableInitializer);
        localVariableDeclarator.setDimension(dimension);

        // Act
        String actualString = localVariableDeclarator.toString();

        // Assert
        Assert.assertEquals("LocalVariableDeclarator{name=myVariable, dimension3, variableInitializer=" + variableInitializer + "}", actualString);
    }

    @Test
    public void testGetLineNumber() {
        // Arrange
        int lineNumber = 5;
        String name = "myVariable";
        LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(lineNumber, name, null);

        // Act
        int actualLineNumber = localVariableDeclarator.getLineNumber();

        // Assert
        Assert.assertEquals(lineNumber, actualLineNumber);
    }

    @Test
    public void testAccept() {
        // Arrange
        String name = "myVariable";
        LocalVariableDeclarator localVariableDeclarator = new LocalVariableDeclarator(name);
        TestDeclarationVisitor testDeclarationVisitor = new TestDeclarationVisitor();

        // Act
        localVariableDeclarator.accept(testDeclarationVisitor);

        // Assert
        Assert.assertEquals(1, testDeclarationVisitor.getLocalVariableDeclaratorCount());
    }
}
