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
        assertFalse(isArrayExpression());
        assertFalse(isBinaryOperatorExpression());
        assertFalse(isBooleanExpression());
        assertFalse(isCastExpression());
        assertFalse(isConstructorInvocationExpression());
        assertFalse(isDoubleConstantExpression());
        assertFalse(isFieldReferenceExpression());
        assertFalse(isFloatConstantExpression());
        assertFalse(isIntegerConstantExpression());
        assertFalse(isLengthExpression());
        assertFalse(isLocalVariableReferenceExpression());
        assertFalse(isLongConstantExpression());
        assertFalse(isMethodInvocationExpression());
        assertFalse(isNew());
        assertFalse(isNewArray());
        assertFalse(isNewExpression());
        assertFalse(isNewInitializedArray());
        assertFalse(isNullExpression());
        assertFalse(isObjectTypeReferenceExpression());
        assertFalse(isPostOperatorExpression());
        assertFalse(isPreOperatorExpression());
        assertFalse(isStringConstantExpression());
        assertFalse(isSuperConstructorInvocationExpression());
        assertFalse(isSuperExpression());
        assertFalse(isTernaryOperatorExpression());
        assertFalse(isThisExpression());

        assertEquals(NoExpression.NO_EXPRESSION, getDimensionExpressionList());
        assertEquals(NoExpression.NO_EXPRESSION, getParameters());
        assertEquals(NoExpression.NO_EXPRESSION, getCondition());
        assertEquals(NoExpression.NO_EXPRESSION, getExpression());
        assertEquals(NoExpression.NO_EXPRESSION, getTrueExpression());
        assertEquals(NoExpression.NO_EXPRESSION, getFalseExpression());
        assertEquals(NoExpression.NO_EXPRESSION, getIndex());
        assertEquals(NoExpression.NO_EXPRESSION, getLeftExpression());
        assertEquals(NoExpression.NO_EXPRESSION, getRightExpression());

        assertEquals("", getDescriptor());
        assertEquals(0D, getDoubleValue(), 0.00001D);
        assertEquals(0F, getFloatValue(), 0.00001F);
        assertEquals(0, getIntegerValue());
        assertEquals("", getInternalTypeName());
        assertEquals(0L, getLongValue());
        assertEquals("", getName());
        assertEquals(ObjectType.TYPE_UNDEFINED_OBJECT, getObjectType());
        assertEquals("", getOperator());
        assertEquals("", getStringValue());
    }
}
