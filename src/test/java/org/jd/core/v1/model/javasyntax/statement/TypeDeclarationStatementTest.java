package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeDeclarationStatementTest {

    @Test
    public void test() throws Exception {
        TypeDeclarationStatement typeDeclarationStatement = new TypeDeclarationStatement(null);
        TestVisitor visitor = new TestVisitor();
        typeDeclarationStatement.accept(visitor);
        assertEquals(1, visitor.getTypeDeclarationStatementCount());
    }
}
