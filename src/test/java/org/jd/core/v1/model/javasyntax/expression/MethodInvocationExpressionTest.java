package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.MethodTypes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MethodInvocationExpressionTest {

    private TypeMaker typeMaker;

    @Before
    public void setUp() {
        this.typeMaker = new TypeMaker(new ClassPathLoader());
    }

    @Test
    public void testMethodInvocationExpression() {
        String internalTypeName = "java/lang/String";
        String name = "testMethod";
        String descriptor = "(Ljava/lang/String;)V";
        Expression expression = new StringConstantExpression("Expression");

        Type type = typeMaker.makeFromInternalTypeName(internalTypeName);
        MethodTypes methodTypes = typeMaker.makeMethodTypes(internalTypeName, name, descriptor);

        MethodInvocationExpression methodInvocationExpression = new MethodInvocationExpression(type, expression, internalTypeName, name, descriptor, methodTypes);

        // Assertions
        assertEquals(expression, methodInvocationExpression.getExpression());
        assertEquals(internalTypeName, methodInvocationExpression.getInternalTypeName());
        assertEquals(name, methodInvocationExpression.getName());
        assertEquals(descriptor, methodInvocationExpression.getDescriptor());
        assertNull(methodInvocationExpression.getExceptionTypes());
        assertFalse(methodInvocationExpression.isVarArgs());
    }
}
