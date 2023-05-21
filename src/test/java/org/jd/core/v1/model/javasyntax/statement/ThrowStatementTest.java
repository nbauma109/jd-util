package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.NewExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThrowStatementTest {

    @Test
    public void testThrowStatement() {
        // Test constructor
        TypeMaker typeMaker = new TypeMaker();
        ObjectType type = typeMaker.makeFromInternalTypeName("java/lang/RuntimeException");
        NewExpression newExpression = new NewExpression(1, type, "(Ljava/lang/String;)V", false, false);
        newExpression.setParameters(new StringConstantExpression("Error"));
        ThrowStatement throwStatement = new ThrowStatement(newExpression);

        assertTrue(throwStatement.getExpression() instanceof NewExpression);

        // Test setExpression method
        NewExpression newNewExpression = new NewExpression(2, type, "(Ljava/lang/String;)V", false, false);
        newNewExpression.setParameters(new StringConstantExpression("New Error"));
        throwStatement.setExpression(newNewExpression);
        assertTrue(throwStatement.getExpression() instanceof NewExpression);

        // Test toString method
        assertEquals("ThrowStatement{throw NewExpression{new ObjectType{java/lang/RuntimeException}}}", throwStatement.toString());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        throwStatement.accept(visitor);
        assertEquals(1, visitor.getThrowStatementCount());
    }
}
