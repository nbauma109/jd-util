package org.jd.core.v1.model.javasyntax.expression;

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
		this.typeMaker = new TypeMaker();
	}

	@Test
	public void testMethodInvocationExpression() {
		String internalTypeName = "java/lang/String"; //$NON-NLS-1$
		String name = "testMethod"; //$NON-NLS-1$
		String descriptor = "(Ljava/lang/String;)V"; //$NON-NLS-1$
		Expression expression = new StringConstantExpression("Expression"); //$NON-NLS-1$

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
		BaseExpression baseExpression = new StringConstantExpression("Params"); //$NON-NLS-1$
		methodInvocationExpression.setParameters(baseExpression);
		assertEquals(baseExpression, methodInvocationExpression.getParameters());

		// Check priority
		assertEquals(1, methodInvocationExpression.getPriority());

		// Set and get typeBounds
		Map<String, BaseType> typeBounds = new HashMap<String, BaseType>();
		typeBounds.put("key", type); //$NON-NLS-1$
		methodInvocationExpression.setTypeBounds(typeBounds);
		assertEquals(typeBounds, methodInvocationExpression.getTypeBounds());

		// Set and get typeBindings
		Map<String, TypeArgument> typeBindings = new HashMap<String, TypeArgument>();
		typeBindings.put("key", ObjectType.TYPE_STRING); //$NON-NLS-1$
		methodInvocationExpression.setTypeBindings(typeBindings);
		assertEquals(typeBindings, methodInvocationExpression.getTypeBindings());

		// Check isMethodInvocationExpression
		assertTrue(methodInvocationExpression.isMethodInvocationExpression());

		// Check toString
		assertEquals("MethodInvocationExpression{call " + expression + " . " + name + "(" + descriptor + ")}", methodInvocationExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		// Check accept
		TestVisitor testVisitor = new TestVisitor();
		methodInvocationExpression.accept(testVisitor);
		assertEquals(1, testVisitor.getMethodInvocationExpressionCount());

		// Check copyTo
		Expression copy = methodInvocationExpression.copyTo(42);
		assertEquals(42, copy.getLineNumber());
		assertEquals(methodInvocationExpression.getName(), copy.getName());
		assertEquals(methodInvocationExpression.getInternalTypeName(), copy.getInternalTypeName());
		assertEquals(methodInvocationExpression.getDescriptor(), copy.getDescriptor());
		assertEquals(methodInvocationExpression.getExpression(), copy.getExpression());
	}

	@Test
	public void testMethodReferenceExpression() {
		String internalTypeName = "java/lang/String"; //$NON-NLS-1$
		String name = "testMethod"; //$NON-NLS-1$
		String descriptor = "(Ljava/lang/String;)V"; //$NON-NLS-1$
		Expression expression = new StringConstantExpression("Expression"); //$NON-NLS-1$

		Type type = typeMaker.makeFromInternalTypeName(internalTypeName);

		MethodReferenceExpression methodReferenceExpression = new MethodReferenceExpression(type, expression, internalTypeName, name, descriptor);

		// Check toString
		assertEquals("MethodReferenceExpression{call " + expression + " :: " + name + "(" + descriptor + ")}", methodReferenceExpression.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		// Check accept
		TestVisitor testVisitor = new TestVisitor();
		methodReferenceExpression.accept(testVisitor);
		assertEquals(1, testVisitor.getMethodReferenceExpressionCount());

		// Check copyTo
		Expression copy = methodReferenceExpression.copyTo(42);
		assertEquals(42, copy.getLineNumber());
		assertEquals(methodReferenceExpression.getName(), copy.getName());
		assertEquals(methodReferenceExpression.getInternalTypeName(), copy.getInternalTypeName());
		assertEquals(methodReferenceExpression.getDescriptor(), copy.getDescriptor());
		assertEquals(methodReferenceExpression.getExpression(), copy.getExpression());
	}
}
