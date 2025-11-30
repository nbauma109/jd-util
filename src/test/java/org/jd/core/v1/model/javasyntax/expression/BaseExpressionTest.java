package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BaseExpressionTest implements BaseExpression {

	@Override
	public void accept(ExpressionVisitor visitor) {}

	@Test
	public void test() {
		assertFalse(this.isArrayExpression());
		assertFalse(this.isBinaryOperatorExpression());
		assertFalse(this.isBooleanExpression());
		assertFalse(this.isCastExpression());
		assertFalse(this.isConstructorInvocationExpression());
		assertFalse(this.isDoubleConstantExpression());
		assertFalse(this.isFieldReferenceExpression());
		assertFalse(this.isFloatConstantExpression());
		assertFalse(this.isIntegerConstantExpression());
		assertFalse(this.isLengthExpression());
		assertFalse(this.isLocalVariableReferenceExpression());
		assertFalse(this.isLongConstantExpression());
		assertFalse(this.isMethodInvocationExpression());
		assertFalse(this.isNew());
		assertFalse(this.isNewArray());
		assertFalse(this.isNewExpression());
		assertFalse(this.isNewInitializedArray());
		assertFalse(this.isNullExpression());
		assertFalse(this.isObjectTypeReferenceExpression());
		assertFalse(this.isPostOperatorExpression());
		assertFalse(this.isPreOperatorExpression());
		assertFalse(this.isStringConstantExpression());
		assertFalse(this.isSuperConstructorInvocationExpression());
		assertFalse(this.isSuperExpression());
		assertFalse(this.isTernaryOperatorExpression());
		assertFalse(this.isThisExpression());

		assertEquals(NoExpression.NO_EXPRESSION, this.getDimensionExpressionList());
		assertEquals(NoExpression.NO_EXPRESSION, this.getParameters());
		assertEquals(NoExpression.NO_EXPRESSION, this.getCondition());
		assertEquals(NoExpression.NO_EXPRESSION, this.getExpression());
		assertEquals(NoExpression.NO_EXPRESSION, this.getTrueExpression());
		assertEquals(NoExpression.NO_EXPRESSION, this.getFalseExpression());
		assertEquals(NoExpression.NO_EXPRESSION, this.getIndex());
		assertEquals(NoExpression.NO_EXPRESSION, this.getLeftExpression());
		assertEquals(NoExpression.NO_EXPRESSION, this.getRightExpression());

		assertEquals("", this.getDescriptor()); //$NON-NLS-1$
		assertEquals(0D, this.getDoubleValue(), 0.00001D);
		assertEquals(0F, this.getFloatValue(), 0.00001F);
		assertEquals(0, this.getIntegerValue());
		assertEquals("", this.getInternalTypeName()); //$NON-NLS-1$
		assertEquals(0L, this.getLongValue());
		assertEquals("", this.getName()); //$NON-NLS-1$
		assertEquals(ObjectType.TYPE_UNDEFINED_OBJECT, this.getObjectType());
		assertEquals("", this.getOperator()); //$NON-NLS-1$
		assertEquals("", this.getStringValue()); //$NON-NLS-1$
	}
}
