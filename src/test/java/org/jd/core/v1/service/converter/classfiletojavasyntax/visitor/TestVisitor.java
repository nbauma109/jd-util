package org.jd.core.v1.service.converter.classfiletojavasyntax.visitor;

import org.jd.core.v1.model.javasyntax.expression.AbstractNopExpressionVisitor;
import org.jd.core.v1.model.javasyntax.expression.ArrayExpression;
import org.jd.core.v1.model.javasyntax.expression.BinaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.BooleanExpression;
import org.jd.core.v1.model.javasyntax.expression.CastExpression;
import org.jd.core.v1.model.javasyntax.expression.ConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.ConstructorReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.DoubleConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.EnumConstantReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.Expressions;
import org.jd.core.v1.model.javasyntax.expression.FieldReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.FloatConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.InstanceOfExpression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.LambdaIdentifiersExpression;
import org.jd.core.v1.model.javasyntax.expression.LengthExpression;
import org.jd.core.v1.model.javasyntax.expression.LocalVariableReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.LongConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.MethodReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.NewArray;
import org.jd.core.v1.model.javasyntax.expression.NewExpression;
import org.jd.core.v1.model.javasyntax.expression.NewInitializedArray;
import org.jd.core.v1.model.javasyntax.expression.NoExpression;
import org.jd.core.v1.model.javasyntax.expression.NullExpression;
import org.jd.core.v1.model.javasyntax.expression.ObjectTypeReferenceExpression;
import org.jd.core.v1.model.javasyntax.expression.ParenthesesExpression;
import org.jd.core.v1.model.javasyntax.expression.PostOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.PreOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.QualifiedSuperExpression;
import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.expression.SuperConstructorInvocationExpression;
import org.jd.core.v1.model.javasyntax.expression.SuperExpression;
import org.jd.core.v1.model.javasyntax.expression.TernaryOperatorExpression;
import org.jd.core.v1.model.javasyntax.expression.ThisExpression;
import org.jd.core.v1.model.javasyntax.expression.TypeReferenceDotClassExpression;
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

public class TestVisitor extends AbstractNopExpressionVisitor implements StatementVisitor {

    // --- statement counters ---

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


    // --- expression counters ---

    private int arrayExpressionCount;
    private int binaryOperatorExpressionCount;
    private int booleanExpressionCount;
    private int castExpressionCount;
    private int commentExpressionCount;
    private int constructorInvocationExpressionCount;
    private int constructorReferenceExpressionCount;
    private int doubleConstantExpressionCount;
    private int enumConstantReferenceExpressionCount;
    private int expressionsCount;
    private int fieldReferenceExpressionCount;
    private int floatConstantExpressionCount;
    private int instanceOfExpressionCount;
    private int integerConstantExpressionCount;
    private int lambdaIdentifiersExpressionCount;
    private int lengthExpressionCount;
    private int localVariableReferenceExpressionCount;
    private int longConstantExpressionCount;
    private int methodInvocationExpressionCount;
    private int methodReferenceExpressionCount;
    private int newArrayCount;
    private int newExpressionCount;
    private int newInitializedArrayCount;
    private int noExpressionCount;
    private int nullExpressionCount;
    private int objectTypeReferenceExpressionCount;
    private int parenthesesExpressionCount;
    private int postOperatorExpressionCount;
    private int preOperatorExpressionCount;
    private int qualifiedSuperExpressionCount;
    private int stringConstantExpressionCount;
    private int superConstructorInvocationExpressionCount;
    private int superExpressionCount;
    private int ternaryOperatorExpressionCount;
    private int thisExpressionCount;
    private int typeReferenceDotClassExpressionCount;


    // --- statement visit methods ---

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


    // --- expression visit methods ---

    @Override
    public void visit(ArrayExpression expression) {
        super.visit(expression);
        arrayExpressionCount++;
    }

    @Override
    public void visit(BinaryOperatorExpression expression) {
        super.visit(expression);
        binaryOperatorExpressionCount++;
    }

    @Override
    public void visit(BooleanExpression expression) {
        super.visit(expression);
        booleanExpressionCount++;
    }

    @Override
    public void visit(CastExpression expression) {
        super.visit(expression);
        castExpressionCount++;
    }

    @Override
    public void visit(ConstructorInvocationExpression expression) {
        super.visit(expression);
        constructorInvocationExpressionCount++;
    }

    @Override
    public void visit(ConstructorReferenceExpression expression) {
        super.visit(expression);
        constructorReferenceExpressionCount++;
    }

    @Override
    public void visit(DoubleConstantExpression expression) {
        super.visit(expression);
        doubleConstantExpressionCount++;
    }

    @Override
    public void visit(EnumConstantReferenceExpression expression) {
        super.visit(expression);
        enumConstantReferenceExpressionCount++;
    }

    @Override
    public void visit(Expressions expression) {
        super.visit(expression);
        expressionsCount++;
    }

    @Override
    public void visit(FieldReferenceExpression expression) {
        super.visit(expression);
        fieldReferenceExpressionCount++;
    }

    @Override
    public void visit(FloatConstantExpression expression) {
        super.visit(expression);
        floatConstantExpressionCount++;
    }

    @Override
    public void visit(InstanceOfExpression expression) {
        super.visit(expression);
        instanceOfExpressionCount++;
    }

    @Override
    public void visit(IntegerConstantExpression expression) {
        super.visit(expression);
        integerConstantExpressionCount++;
    }

    @Override
    public void visit(LambdaIdentifiersExpression expression) {
        super.visit(expression);
        lambdaIdentifiersExpressionCount++;
    }

    @Override
    public void visit(LengthExpression expression) {
        super.visit(expression);
        lengthExpressionCount++;
    }

    @Override
    public void visit(LocalVariableReferenceExpression expression) {
        super.visit(expression);
        localVariableReferenceExpressionCount++;
    }

    @Override
    public void visit(LongConstantExpression expression) {
        super.visit(expression);
        longConstantExpressionCount++;
    }

    @Override
    public void visit(MethodInvocationExpression expression) {
        super.visit(expression);
        methodInvocationExpressionCount++;
    }

    @Override
    public void visit(MethodReferenceExpression expression) {
        super.visit(expression);
        methodReferenceExpressionCount++;
    }

    @Override
    public void visit(NewArray expression) {
        super.visit(expression);
        newArrayCount++;
    }

    @Override
    public void visit(NewExpression expression) {
        super.visit(expression);
        newExpressionCount++;
    }

    @Override
    public void visit(NewInitializedArray expression) {
        super.visit(expression);
        newInitializedArrayCount++;
    }

    @Override
    public void visit(NoExpression expression) {
        super.visit(expression);
        noExpressionCount++;
    }

    @Override
    public void visit(NullExpression expression) {
        super.visit(expression);
        nullExpressionCount++;
    }

    @Override
    public void visit(ObjectTypeReferenceExpression expression) {
        super.visit(expression);
        objectTypeReferenceExpressionCount++;
    }

    @Override
    public void visit(ParenthesesExpression expression) {
        super.visit(expression);
        parenthesesExpressionCount++;
    }

    @Override
    public void visit(PostOperatorExpression expression) {
        super.visit(expression);
        postOperatorExpressionCount++;
    }

    @Override
    public void visit(PreOperatorExpression expression) {
        super.visit(expression);
        preOperatorExpressionCount++;
    }

    @Override
    public void visit(QualifiedSuperExpression expression) {
        super.visit(expression);
        qualifiedSuperExpressionCount++;
    }

    @Override
    public void visit(StringConstantExpression expression) {
        super.visit(expression);
        stringConstantExpressionCount++;
    }

    @Override
    public void visit(SuperConstructorInvocationExpression expression) {
        super.visit(expression);
        superConstructorInvocationExpressionCount++;
    }

    @Override
    public void visit(SuperExpression expression) {
        super.visit(expression);
        superExpressionCount++;
    }

    @Override
    public void visit(TernaryOperatorExpression expression) {
        super.visit(expression);
        ternaryOperatorExpressionCount++;
    }

    @Override
    public void visit(ThisExpression expression) {
        super.visit(expression);
        thisExpressionCount++;
    }

    @Override
    public void visit(TypeReferenceDotClassExpression expression) {
        super.visit(expression);
        typeReferenceDotClassExpressionCount++;
    }


    // --- getters & setters ---

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

    public int getArrayExpressionCount() {
        return arrayExpressionCount;
    }

    public void setArrayExpressionCount(int arrayExpressionCount) {
        this.arrayExpressionCount = arrayExpressionCount;
    }

    public int getBinaryOperatorExpressionCount() {
        return binaryOperatorExpressionCount;
    }

    public void setBinaryOperatorExpressionCount(int binaryOperatorExpressionCount) {
        this.binaryOperatorExpressionCount = binaryOperatorExpressionCount;
    }

    public int getBooleanExpressionCount() {
        return booleanExpressionCount;
    }

    public void setBooleanExpressionCount(int booleanExpressionCount) {
        this.booleanExpressionCount = booleanExpressionCount;
    }

    public int getCastExpressionCount() {
        return castExpressionCount;
    }

    public void setCastExpressionCount(int castExpressionCount) {
        this.castExpressionCount = castExpressionCount;
    }

    public int getCommentExpressionCount() {
        return commentExpressionCount;
    }

    public void setCommentExpressionCount(int commentExpressionCount) {
        this.commentExpressionCount = commentExpressionCount;
    }

    public int getConstructorInvocationExpressionCount() {
        return constructorInvocationExpressionCount;
    }

    public void setConstructorInvocationExpressionCount(int constructorInvocationExpressionCount) {
        this.constructorInvocationExpressionCount = constructorInvocationExpressionCount;
    }

    public int getConstructorReferenceExpressionCount() {
        return constructorReferenceExpressionCount;
    }

    public void setConstructorReferenceExpressionCount(int constructorReferenceExpressionCount) {
        this.constructorReferenceExpressionCount = constructorReferenceExpressionCount;
    }

    public int getDoubleConstantExpressionCount() {
        return doubleConstantExpressionCount;
    }

    public void setDoubleConstantExpressionCount(int doubleConstantExpressionCount) {
        this.doubleConstantExpressionCount = doubleConstantExpressionCount;
    }

    public int getEnumConstantReferenceExpressionCount() {
        return enumConstantReferenceExpressionCount;
    }

    public void setEnumConstantReferenceExpressionCount(int enumConstantReferenceExpressionCount) {
        this.enumConstantReferenceExpressionCount = enumConstantReferenceExpressionCount;
    }

    public int getExpressionsCount() {
        return expressionsCount;
    }

    public void setExpressionsCount(int expressionsCount) {
        this.expressionsCount = expressionsCount;
    }

    public int getFieldReferenceExpressionCount() {
        return fieldReferenceExpressionCount;
    }

    public void setFieldReferenceExpressionCount(int fieldReferenceExpressionCount) {
        this.fieldReferenceExpressionCount = fieldReferenceExpressionCount;
    }

    public int getFloatConstantExpressionCount() {
        return floatConstantExpressionCount;
    }

    public void setFloatConstantExpressionCount(int floatConstantExpressionCount) {
        this.floatConstantExpressionCount = floatConstantExpressionCount;
    }

    public int getInstanceOfExpressionCount() {
        return instanceOfExpressionCount;
    }

    public void setInstanceOfExpressionCount(int instanceOfExpressionCount) {
        this.instanceOfExpressionCount = instanceOfExpressionCount;
    }

    public int getIntegerConstantExpressionCount() {
        return integerConstantExpressionCount;
    }

    public void setIntegerConstantExpressionCount(int integerConstantExpressionCount) {
        this.integerConstantExpressionCount = integerConstantExpressionCount;
    }

    public int getLambdaIdentifiersExpressionCount() {
        return lambdaIdentifiersExpressionCount;
    }

    public void setLambdaIdentifiersExpressionCount(int lambdaIdentifiersExpressionCount) {
        this.lambdaIdentifiersExpressionCount = lambdaIdentifiersExpressionCount;
    }

    public int getLengthExpressionCount() {
        return lengthExpressionCount;
    }

    public void setLengthExpressionCount(int lengthExpressionCount) {
        this.lengthExpressionCount = lengthExpressionCount;
    }

    public int getLocalVariableReferenceExpressionCount() {
        return localVariableReferenceExpressionCount;
    }

    public void setLocalVariableReferenceExpressionCount(int localVariableReferenceExpressionCount) {
        this.localVariableReferenceExpressionCount = localVariableReferenceExpressionCount;
    }

    public int getLongConstantExpressionCount() {
        return longConstantExpressionCount;
    }

    public void setLongConstantExpressionCount(int longConstantExpressionCount) {
        this.longConstantExpressionCount = longConstantExpressionCount;
    }

    public int getMethodInvocationExpressionCount() {
        return methodInvocationExpressionCount;
    }

    public void setMethodInvocationExpressionCount(int methodInvocationExpressionCount) {
        this.methodInvocationExpressionCount = methodInvocationExpressionCount;
    }

    public int getMethodReferenceExpressionCount() {
        return methodReferenceExpressionCount;
    }

    public void setMethodReferenceExpressionCount(int methodReferenceExpressionCount) {
        this.methodReferenceExpressionCount = methodReferenceExpressionCount;
    }

    public int getNewArrayCount() {
        return newArrayCount;
    }

    public void setNewArrayCount(int newArrayCount) {
        this.newArrayCount = newArrayCount;
    }

    public int getNewExpressionCount() {
        return newExpressionCount;
    }

    public void setNewExpressionCount(int newExpressionCount) {
        this.newExpressionCount = newExpressionCount;
    }

    public int getNewInitializedArrayCount() {
        return newInitializedArrayCount;
    }

    public void setNewInitializedArrayCount(int newInitializedArrayCount) {
        this.newInitializedArrayCount = newInitializedArrayCount;
    }

    public int getNoExpressionCount() {
        return noExpressionCount;
    }

    public void setNoExpressionCount(int noExpressionCount) {
        this.noExpressionCount = noExpressionCount;
    }

    public int getNullExpressionCount() {
        return nullExpressionCount;
    }

    public void setNullExpressionCount(int nullExpressionCount) {
        this.nullExpressionCount = nullExpressionCount;
    }

    public int getObjectTypeReferenceExpressionCount() {
        return objectTypeReferenceExpressionCount;
    }

    public void setObjectTypeReferenceExpressionCount(int objectTypeReferenceExpressionCount) {
        this.objectTypeReferenceExpressionCount = objectTypeReferenceExpressionCount;
    }

    public int getParenthesesExpressionCount() {
        return parenthesesExpressionCount;
    }

    public void setParenthesesExpressionCount(int parenthesesExpressionCount) {
        this.parenthesesExpressionCount = parenthesesExpressionCount;
    }

    public int getPostOperatorExpressionCount() {
        return postOperatorExpressionCount;
    }

    public void setPostOperatorExpressionCount(int postOperatorExpressionCount) {
        this.postOperatorExpressionCount = postOperatorExpressionCount;
    }

    public int getPreOperatorExpressionCount() {
        return preOperatorExpressionCount;
    }

    public void setPreOperatorExpressionCount(int preOperatorExpressionCount) {
        this.preOperatorExpressionCount = preOperatorExpressionCount;
    }

    public int getQualifiedSuperExpressionCount() {
        return qualifiedSuperExpressionCount;
    }

    public void setQualifiedSuperExpressionCount(int qualifiedSuperExpressionCount) {
        this.qualifiedSuperExpressionCount = qualifiedSuperExpressionCount;
    }

    public int getStringConstantExpressionCount() {
        return stringConstantExpressionCount;
    }

    public void setStringConstantExpressionCount(int stringConstantExpressionCount) {
        this.stringConstantExpressionCount = stringConstantExpressionCount;
    }

    public int getSuperConstructorInvocationExpressionCount() {
        return superConstructorInvocationExpressionCount;
    }

    public void setSuperConstructorInvocationExpressionCount(int superConstructorInvocationExpressionCount) {
        this.superConstructorInvocationExpressionCount = superConstructorInvocationExpressionCount;
    }

    public int getSuperExpressionCount() {
        return superExpressionCount;
    }

    public void setSuperExpressionCount(int superExpressionCount) {
        this.superExpressionCount = superExpressionCount;
    }

    public int getTernaryOperatorExpressionCount() {
        return ternaryOperatorExpressionCount;
    }

    public void setTernaryOperatorExpressionCount(int ternaryOperatorExpressionCount) {
        this.ternaryOperatorExpressionCount = ternaryOperatorExpressionCount;
    }

    public int getThisExpressionCount() {
        return thisExpressionCount;
    }

    public void setThisExpressionCount(int thisExpressionCount) {
        this.thisExpressionCount = thisExpressionCount;
    }

    public int getTypeReferenceDotClassExpressionCount() {
        return typeReferenceDotClassExpressionCount;
    }

    public void setTypeReferenceDotClassExpressionCount(int typeReferenceDotClassExpressionCount) {
        this.typeReferenceDotClassExpressionCount = typeReferenceDotClassExpressionCount;
    }
}
