package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpressionsTest {

    @Test
    public void testExpressions() {
        // Test default constructor
        Expressions expressions = new Expressions();
        assertEquals(0, expressions.size());

        // Test constructor with capacity
        Expressions expressionsWithCapacity = new Expressions(5);
        assertEquals(0, expressionsWithCapacity.size());

        // Test constructor with a collection
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new CommentExpression("Test1"));
        expressionList.add(new CommentExpression("Test2"));
        
        Expressions expressionsFromCollection = new Expressions(expressionList);
        assertEquals(2, expressionsFromCollection.size());
        assertEquals("Test1", ((CommentExpression) expressionsFromCollection.get(0)).text());
        assertEquals("Test2", ((CommentExpression) expressionsFromCollection.get(1)).text());

        // Test constructor with expression parameters
        Expression expression1 = new CommentExpression("Test3");
        Expression expression2 = new CommentExpression("Test4");
        
        Expressions expressionsFromExpressions = new Expressions(expression1, expression2);
        assertEquals(2, expressionsFromExpressions.size());
        assertEquals("Test3", ((CommentExpression) expressionsFromExpressions.get(0)).text());
        assertEquals("Test4", ((CommentExpression) expressionsFromExpressions.get(1)).text());

        // Test IllegalArgumentException on single element collection
        List<Expression> singleElementList = Collections.singletonList(new CommentExpression("Test5"));
        
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new Expressions(singleElementList));
        assertEquals("Use 'Expression' or sub class instead", exception1.getMessage());

        // Test IllegalArgumentException on single element parameters
        Expression singleExpression = new CommentExpression("Test6");
        
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new Expressions(singleExpression));
        assertEquals("Use 'Expression' or sub class instead", exception2.getMessage());

        // Test the accept method with a simple visitor
        TestVisitor visitor = new TestVisitor();
        expressions.accept(visitor);
        assertEquals(1, visitor.getExpressionsCount());
    }
}
