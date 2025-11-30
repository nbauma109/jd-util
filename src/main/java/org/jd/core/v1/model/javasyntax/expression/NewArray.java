/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.type.Type;

public class NewArray extends AbstractLineNumberTypeExpression {
	private BaseExpression dimensionExpressionList;

	public NewArray(int lineNumber, Type type, BaseExpression dimensionExpressionList) {
		super(lineNumber, type);
		this.dimensionExpressionList = dimensionExpressionList;
	}

	public boolean isEmptyNewArray() {
		return dimensionExpressionList instanceof IntegerConstantExpression ice && ice.getIntegerValue() == 0;
	}

	@Override
	public BaseExpression getDimensionExpressionList() {
		return dimensionExpressionList;
	}

	public void setDimensionExpressionList(BaseExpression dimensionExpressionList) {
		this.dimensionExpressionList = dimensionExpressionList;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean isNewArray() { return true; }

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "NewArray{" + this.getType() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Expression copyTo(int lineNumber) {
		return new NewArray(lineNumber, this.getType(), dimensionExpressionList);
	}
}
