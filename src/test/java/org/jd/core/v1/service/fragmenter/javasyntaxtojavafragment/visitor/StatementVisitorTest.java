/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor;

import org.jd.core.v1.loader.NopLoader;
import org.jd.core.v1.model.fragment.Fragment;
import org.jd.core.v1.model.javafragment.ImportsFragment;
import org.jd.core.v1.model.javafragment.StartSingleStatementBlockFragment;
import org.jd.core.v1.model.javafragment.StartStatementsBlockFragment;
import org.jd.core.v1.model.javafragment.TokensFragment;
import org.jd.core.v1.model.javasyntax.expression.BooleanExpression;
import org.jd.core.v1.model.javasyntax.expression.IntegerConstantExpression;
import org.jd.core.v1.model.javasyntax.statement.BaseStatement;
import org.jd.core.v1.model.javasyntax.statement.IfStatement;
import org.jd.core.v1.model.javasyntax.statement.NoStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnExpressionStatement;
import org.jd.core.v1.model.javasyntax.statement.ReturnStatement;
import org.jd.core.v1.model.javasyntax.statement.Statements;
import org.jd.core.v1.model.javasyntax.statement.WhileStatement;
import org.jd.core.v1.model.javasyntax.type.PrimitiveType;
import org.jd.core.v1.model.token.BooleanConstantToken;
import org.jd.core.v1.model.token.CharacterConstantToken;
import org.jd.core.v1.model.token.NumericConstantToken;
import org.jd.core.v1.model.token.TextToken;
import org.jd.core.v1.model.token.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class StatementVisitorTest {
    @Test
    public void testLoopWithoutGeneratedBody() {
        List<Fragment> fragments = visit(new WhileStatement(BooleanExpression.TRUE, NoStatement.NO_STATEMENT));
        assertEquals(1, fragments.size());
        List<Token> tokens = ((TokensFragment) fragments.get(0)).getTokens();
        assertSame(TextToken.SEMICOLON, tokens.get(tokens.size() - 1));
    }

    @Test
    public void testEmptyIfBody() {
        List<Fragment> fragments = visit(new IfStatement(BooleanExpression.TRUE, new Statements()));
        assertEquals(2, fragments.size());
        assertSame(TokensFragment.SEMICOLON, fragments.get(1));
    }

    @Test
    public void testSingleStatementBlocks() {
        assertBlock(new WhileStatement(BooleanExpression.TRUE, ReturnStatement.RETURN), StartSingleStatementBlockFragment.class);
        assertBlock(new IfStatement(BooleanExpression.TRUE, ReturnStatement.RETURN), StartSingleStatementBlockFragment.class);
    }

    @Test
    public void testMultipleStatementBlocks() {
        Statements body = new Statements(ReturnStatement.RETURN, ReturnStatement.RETURN);
        assertBlock(new WhileStatement(BooleanExpression.TRUE, body), StartStatementsBlockFragment.class);
        assertBlock(new IfStatement(BooleanExpression.TRUE, body), StartStatementsBlockFragment.class);
    }

    @Test
    public void testIntegerConstantsRetainTheirPrimitiveTypes() {
        assertEquals(new BooleanConstantToken(false), literalToken(new IntegerConstantExpression(PrimitiveType.TYPE_BOOLEAN, 0)));
        assertEquals(new BooleanConstantToken(true), literalToken(new IntegerConstantExpression(PrimitiveType.TYPE_BOOLEAN, 1)));
        assertEquals(new NumericConstantToken("42"), literalToken(new IntegerConstantExpression(PrimitiveType.TYPE_INT, 42)));

        Token character = literalToken(new IntegerConstantExpression(PrimitiveType.TYPE_CHAR, 'A'));
        assertTrue(character instanceof CharacterConstantToken);
        assertEquals("A", ((CharacterConstantToken) character).getCharacter());
    }

    private static Token literalToken(IntegerConstantExpression expression) {
        List<Fragment> fragments = visit(new ReturnExpressionStatement(expression));
        return ((TokensFragment) fragments.get(0)).getTokens().get(3);
    }

    private static void assertBlock(BaseStatement statement, Class<? extends Fragment> expected) {
        List<Fragment> fragments = visit(statement);
        assertEquals(expected, fragments.get(1).getClass());
    }

    private static List<Fragment> visit(BaseStatement statement) {
        StatementVisitor visitor = new StatementVisitor(new NopLoader(), "example/Test", 61, new ImportsFragment(0));
        statement.accept(visitor);
        return visitor.getFragments();
    }
}
