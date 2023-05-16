package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.statement.AssertStatement;
import org.jd.core.v1.model.javasyntax.statement.BreakStatement;
import org.jd.core.v1.model.javasyntax.statement.CommentStatement;
import org.jd.core.v1.model.javasyntax.statement.ContinueStatement;
import org.jd.core.v1.model.javasyntax.statement.DoWhileStatement;
import org.jd.core.v1.model.javasyntax.statement.ExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.ForEachStatement;
import org.jd.core.v1.model.javasyntax.statement.ForStatement;
import org.jd.core.v1.model.javasyntax.statement.IfElseStatement;
import org.jd.core.v1.model.javasyntax.statement.IfStatement;
import org.jd.core.v1.model.javasyntax.statement.LabelStatement;
import org.jd.core.v1.model.javasyntax.statement.LambdaExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.LocalVariableDeclarationStatement;
import org.jd.core.v1.model.javasyntax.statement.NoStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnStatement;
import org.jd.core.v1.model.javasyntax.statement.StatementVisitor;
import org.jd.core.v1.model.javasyntax.statement.Statements;
import org.jd.core.v1.model.javasyntax.statement.SwitchStatement;
import org.jd.core.v1.model.javasyntax.statement.SwitchStatement.DefaultLabel;
import org.jd.core.v1.model.javasyntax.statement.SwitchStatement.ExpressionLabel;
import org.jd.core.v1.model.javasyntax.statement.SwitchStatement.LabelBlock;
import org.jd.core.v1.model.javasyntax.statement.SwitchStatement.MultiLabelsBlock;
import org.jd.core.v1.model.javasyntax.statement.SynchronizedStatement;
import org.jd.core.v1.model.javasyntax.statement.ThrowStatement;
import org.jd.core.v1.model.javasyntax.statement.TryStatement;
import org.jd.core.v1.model.javasyntax.statement.TryStatement.CatchClause;
import org.jd.core.v1.model.javasyntax.statement.TryStatement.Resource;
import org.jd.core.v1.model.javasyntax.statement.TypeDeclarationStatement;
import org.jd.core.v1.model.javasyntax.statement.WhileStatement;

public class TestVisitor implements StatementVisitor {
    private int tryStatementCount;
    private int assertStatementCount;
    private int breakStatementCount;
    private int commentStatementCount;
    private int continueStatementCount;
    private int doWhileStatementCount;
    private int expressionStatementCount;
    private int forEachStatementCount;
    private int forStatementCount;
    private int ifStatementCount;
    private int ifElseStatementCount;
    private int labelStatementCount;
    private int lambdaExpressionStatementCount;
    private int localVariableDeclarationStatementCount;
    private int noStatementCount;
    private int returnExpressionStatementCount;
    private int returnStatementCount;
    private int statementsCount;
    private int switchStatementCount;
    private int defaultLabelCount;
    private int expressionLabelCount;
    private int labelBlockCount;
    private int multiLabelsBlockCount;
    private int synchronizedStatementCount;
    private int throwStatementCount;
    private int resourceCount;
    private int catchClauseCount;
    private int typeDeclarationStatementCount;
    private int whileStatementCount;

    @Override
    public void visit(TryStatement statement) {
        tryStatementCount++;
    }

    @Override
    public void visit(AssertStatement statement) {
        assertStatementCount++;
    }

    @Override
    public void visit(BreakStatement statement) {
        breakStatementCount++;
    }

    @Override
    public void visit(CommentStatement statement) {
        commentStatementCount++;
    }

    @Override
    public void visit(ContinueStatement statement) {
        continueStatementCount++;
    }

    @Override
    public void visit(DoWhileStatement statement) {
        doWhileStatementCount++;
    }

    @Override
    public void visit(ExpressionStatement statement) {
        expressionStatementCount++;
    }

    @Override
    public void visit(ForEachStatement statement) {
        forEachStatementCount++;
    }

    @Override
    public void visit(ForStatement statement) {
        forStatementCount++;
    }

    @Override
    public void visit(IfStatement statement) {
        ifStatementCount++;
    }

    @Override
    public void visit(IfElseStatement statement) {
        ifElseStatementCount++;
    }

    @Override
    public void visit(LabelStatement statement) {
        labelStatementCount++;
    }

    @Override
    public void visit(LambdaExpressionStatement statement) {
        lambdaExpressionStatementCount++;
    }

    @Override
    public void visit(LocalVariableDeclarationStatement statement) {
        localVariableDeclarationStatementCount++;
    }

    @Override
    public void visit(NoStatement statement) {
        noStatementCount++;
    }

    @Override
    public void visit(ReturnExpressionStatement statement) {
        returnExpressionStatementCount++;
    }

    @Override
    public void visit(ReturnStatement statement) {
        returnStatementCount++;
    }

    @Override
    public void visit(Statements statement) {
        statementsCount++;
    }

    @Override
    public void visit(SwitchStatement statement) {
        switchStatementCount++;
    }

    @Override
    public void visit(DefaultLabel statement) {
        defaultLabelCount++;
    }

    @Override
    public void visit(ExpressionLabel statement) {
        expressionLabelCount++;
    }

    @Override
    public void visit(LabelBlock statement) {
        labelBlockCount++;
    }

    @Override
    public void visit(MultiLabelsBlock statement) {
        multiLabelsBlockCount++;
    }

    @Override
    public void visit(SynchronizedStatement statement) {
        synchronizedStatementCount++;
    }

    @Override
    public void visit(ThrowStatement statement) {
        throwStatementCount++;
    }

    @Override
    public void visit(Resource statement) {
        resourceCount++;
    }

    @Override
    public void visit(CatchClause statement) {
        catchClauseCount++;
    }

    @Override
    public void visit(TypeDeclarationStatement statement) {
        typeDeclarationStatementCount++;
    }

    @Override
    public void visit(WhileStatement statement) {
        whileStatementCount++;
    }

    public int getTryStatementCount() {
        return tryStatementCount;
    }

    public void setTryStatementCount(int tryStatementCount) {
        this.tryStatementCount = tryStatementCount;
    }

    public int getAssertStatementCount() {
        return assertStatementCount;
    }

    public void setAssertStatementCount(int assertStatementCount) {
        this.assertStatementCount = assertStatementCount;
    }

    public int getBreakStatementCount() {
        return breakStatementCount;
    }

    public void setBreakStatementCount(int breakStatementCount) {
        this.breakStatementCount = breakStatementCount;
    }

    public int getCommentStatementCount() {
        return commentStatementCount;
    }

    public void setCommentStatementCount(int commentStatementCount) {
        this.commentStatementCount = commentStatementCount;
    }

    public int getContinueStatementCount() {
        return continueStatementCount;
    }

    public void setContinueStatementCount(int continueStatementCount) {
        this.continueStatementCount = continueStatementCount;
    }

    public int getDoWhileStatementCount() {
        return doWhileStatementCount;
    }

    public void setDoWhileStatementCount(int doWhileStatementCount) {
        this.doWhileStatementCount = doWhileStatementCount;
    }

    public int getExpressionStatementCount() {
        return expressionStatementCount;
    }

    public void setExpressionStatementCount(int expressionStatementCount) {
        this.expressionStatementCount = expressionStatementCount;
    }

    public int getForEachStatementCount() {
        return forEachStatementCount;
    }

    public void setForEachStatementCount(int forEachStatementCount) {
        this.forEachStatementCount = forEachStatementCount;
    }

    public int getForStatementCount() {
        return forStatementCount;
    }

    public void setForStatementCount(int forStatementCount) {
        this.forStatementCount = forStatementCount;
    }

    public int getIfStatementCount() {
        return ifStatementCount;
    }

    public void setIfStatementCount(int ifStatementCount) {
        this.ifStatementCount = ifStatementCount;
    }

    public int getIfElseStatementCount() {
        return ifElseStatementCount;
    }

    public void setIfElseStatementCount(int ifElseStatementCount) {
        this.ifElseStatementCount = ifElseStatementCount;
    }

    public int getLabelStatementCount() {
        return labelStatementCount;
    }

    public void setLabelStatementCount(int labelStatementCount) {
        this.labelStatementCount = labelStatementCount;
    }

    public int getLambdaExpressionStatementCount() {
        return lambdaExpressionStatementCount;
    }

    public void setLambdaExpressionStatementCount(int lambdaExpressionStatementCount) {
        this.lambdaExpressionStatementCount = lambdaExpressionStatementCount;
    }

    public int getLocalVariableDeclarationStatementCount() {
        return localVariableDeclarationStatementCount;
    }

    public void setLocalVariableDeclarationStatementCount(int localVariableDeclarationStatementCount) {
        this.localVariableDeclarationStatementCount = localVariableDeclarationStatementCount;
    }

    public int getNoStatementCount() {
        return noStatementCount;
    }

    public void setNoStatementCount(int noStatementCount) {
        this.noStatementCount = noStatementCount;
    }

    public int getReturnExpressionStatementCount() {
        return returnExpressionStatementCount;
    }

    public void setReturnExpressionStatementCount(int returnExpressionStatementCount) {
        this.returnExpressionStatementCount = returnExpressionStatementCount;
    }

    public int getReturnStatementCount() {
        return returnStatementCount;
    }

    public void setReturnStatementCount(int returnStatementCount) {
        this.returnStatementCount = returnStatementCount;
    }

    public int getStatementsCount() {
        return statementsCount;
    }

    public void setStatementsCount(int statementsCount) {
        this.statementsCount = statementsCount;
    }

    public int getSwitchStatementCount() {
        return switchStatementCount;
    }

    public void setSwitchStatementCount(int switchStatementCount) {
        this.switchStatementCount = switchStatementCount;
    }

    public int getDefaultLabelCount() {
        return defaultLabelCount;
    }

    public void setDefaultLabelCount(int defaultLabelCount) {
        this.defaultLabelCount = defaultLabelCount;
    }

    public int getExpressionLabelCount() {
        return expressionLabelCount;
    }

    public void setExpressionLabelCount(int expressionLabelCount) {
        this.expressionLabelCount = expressionLabelCount;
    }

    public int getLabelBlockCount() {
        return labelBlockCount;
    }

    public void setLabelBlockCount(int labelBlockCount) {
        this.labelBlockCount = labelBlockCount;
    }

    public int getMultiLabelsBlockCount() {
        return multiLabelsBlockCount;
    }

    public void setMultiLabelsBlockCount(int multiLabelsBlockCount) {
        this.multiLabelsBlockCount = multiLabelsBlockCount;
    }

    public int getSynchronizedStatementCount() {
        return synchronizedStatementCount;
    }

    public void setSynchronizedStatementCount(int synchronizedStatementCount) {
        this.synchronizedStatementCount = synchronizedStatementCount;
    }

    public int getThrowStatementCount() {
        return throwStatementCount;
    }

    public void setThrowStatementCount(int throwStatementCount) {
        this.throwStatementCount = throwStatementCount;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getCatchClauseCount() {
        return catchClauseCount;
    }

    public void setCatchClauseCount(int catchClauseCount) {
        this.catchClauseCount = catchClauseCount;
    }

    public int getTypeDeclarationStatementCount() {
        return typeDeclarationStatementCount;
    }

    public void setTypeDeclarationStatementCount(int typeDeclarationStatementCount) {
        this.typeDeclarationStatementCount = typeDeclarationStatementCount;
    }

    public int getWhileStatementCount() {
        return whileStatementCount;
    }

    public void setWhileStatementCount(int whileStatementCount) {
        this.whileStatementCount = whileStatementCount;
    }
}
