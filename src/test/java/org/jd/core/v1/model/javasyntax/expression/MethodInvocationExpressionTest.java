package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.javasyntax.type.BaseType;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.model.javasyntax.type.TypeArgument;
import org.jd.core.v1.model.javasyntax.type.TypeArguments;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.MethodTypes;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

        // Set and get nonWildcardTypeArguments
        TypeArguments nonWildcardTypeArgument = new TypeArguments(Arrays.asList(ObjectType.TYPE_STRING));
        methodInvocationExpression.setNonWildcardTypeArguments(nonWildcardTypeArgument);
        assertEquals(nonWildcardTypeArgument, methodInvocationExpression.getNonWildcardTypeArguments());

        // Set and get parameters
        BaseExpression baseExpression = new StringConstantExpression("Params");
        methodInvocationExpression.setParameters(baseExpression);
        assertEquals(baseExpression, methodInvocationExpression.getParameters());

        // Check priority
        assertEquals(1, methodInvocationExpression.getPriority());

        // Set and get typeBounds
        Map<String, BaseType> typeBounds = new HashMap<>();
        typeBounds.put("key", type);
        methodInvocationExpression.setTypeBounds(typeBounds);
        assertEquals(typeBounds, methodInvocationExpression.getTypeBounds());

        // Set and get typeBindings
        Map<String, TypeArgument> typeBindings = new HashMap<>();
        typeBindings.put("key", ObjectType.TYPE_STRING);
        methodInvocationExpression.setTypeBindings(typeBindings);
        assertEquals(typeBindings, methodInvocationExpression.getTypeBindings());

        // Check isMethodInvocationExpression
        assertTrue(methodInvocationExpression.isMethodInvocationExpression());

        // Check toString
        assertEquals("MethodInvocationExpression{call " + expression + " . " + name + "(" + descriptor + ")}", methodInvocationExpression.toString());

        // Check accept
        TestVisitor testVisitor = new TestVisitor();
        methodInvocationExpression.accept(testVisitor);
        assertEquals(1, testVisitor.getMethodInvocationExpressionCount());
    }
}
