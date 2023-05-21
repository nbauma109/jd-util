package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.Expression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ForEachStatementTest {

    @Test
    public void testForEachStatement() {
        // Arrange
        ObjectType type = ObjectType.TYPE_ITERABLE;
        String name = "element";
        Expression expression = new LocalVariableReferenceExpression(type, name);
        BaseStatement statements = new ExpressionStatement(expression);

        // Act
        ForEachStatement forEachStatement = new ForEachStatement(type, name, expression, statements);
        forEachStatement.setFinal(true);

        // Assert
        assertEquals(type, forEachStatement.getType());
        assertEquals(name, forEachStatement.getName());
        assertEquals(expression, forEachStatement.getExpression());
        assertEquals(statements, forEachStatement.getStatements());
        assertTrue(forEachStatement.isFinal());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        forEachStatement.accept(visitor);

        // Assert
        assertEquals(1, visitor.getForEachStatementCount());
    }
}
