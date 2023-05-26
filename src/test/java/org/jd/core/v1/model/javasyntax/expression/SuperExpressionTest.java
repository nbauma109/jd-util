package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Assert;
import org.junit.Test;

public class SuperExpressionTest {

    @Test
    public void testSuperExpression() {
        // Arrange
        int lineNumber = 10;
        ObjectType type = ObjectType.TYPE_STRING_BUILDER;

        // Act
        SuperExpression expression = new SuperExpression(lineNumber, type);

        // Assert
        Assert.assertEquals(lineNumber, expression.getLineNumber());
        Assert.assertEquals(type, expression.getType());
        Assert.assertTrue(expression.isSuperExpression());

        // Test accept method
        TestVisitor visitor = new TestVisitor();
        expression.accept(visitor);
        Assert.assertEquals(1, visitor.getSuperExpressionCount());

        // Test toString method
        String expectedToString = "SuperExpression{" + type + "}";
        Assert.assertEquals(expectedToString, expression.toString());

        // Test copyTo method
        int newLineNumber = 20;
        SuperExpression copiedExpression = (SuperExpression) expression.copyTo(newLineNumber);
        Assert.assertEquals(newLineNumber, copiedExpression.getLineNumber());
        Assert.assertEquals(type, copiedExpression.getType());
    }
}
