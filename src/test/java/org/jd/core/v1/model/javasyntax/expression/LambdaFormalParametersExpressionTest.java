package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.declaration.BaseFormalParameter;
import org.jd.core.v1.model.javasyntax.declaration.FormalParameter;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnExpressionStatement;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LambdaFormalParametersExpressionTest {

    @Test
    public void testLambdaFormalParametersExpression() {
        // Arrange
        Type type = ObjectType.TYPE_OBJECT;
        Expression returnExpression = new IntegerConstantExpression(1);
        BaseStatement statement = new ReturnExpressionStatement(returnExpression);
        BaseFormalParameter baseFormalParameter = new FormalParameter(ObjectType.TYPE_INTEGER, "x"); //$NON-NLS-1$
        int lineNumber = 10;

        // Act
        LambdaFormalParametersExpression lambdaFormalParametersExpression = new LambdaFormalParametersExpression(lineNumber, type, baseFormalParameter, statement);

        // Assert
        assertEquals(lineNumber, lambdaFormalParametersExpression.getLineNumber());
        assertEquals(type, lambdaFormalParametersExpression.getType());
        assertEquals(baseFormalParameter, lambdaFormalParametersExpression.getFormalParameters());
        assertEquals(statement, lambdaFormalParametersExpression.getStatements());

        // Act & Assert
        LambdaFormalParametersExpression copy = (LambdaFormalParametersExpression) lambdaFormalParametersExpression.copyTo(lineNumber + 1);
        assertEquals(lambdaFormalParametersExpression.getType(), copy.getType());
        assertEquals(lambdaFormalParametersExpression.getFormalParameters(), copy.getFormalParameters());
        assertEquals(lambdaFormalParametersExpression.getStatements(), copy.getStatements());
        assertNotEquals(lambdaFormalParametersExpression.getLineNumber(), copy.getLineNumber());

        // Test accept method
        TestVisitor visitor = new TestVisitor();
        lambdaFormalParametersExpression.accept(visitor);
        assertEquals(1, visitor.getLambdaFormalParametersExpressionCount());

        // Test toString method
        String expectedToString = "LambdaFormalParametersExpression{" + baseFormalParameter + " -> " + statement + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals(expectedToString, lambdaFormalParametersExpression.toString());
    }
}
