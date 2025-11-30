package org.jd.core.v1.model.javasyntax.statement;

import org.jd.core.v1.model.javasyntax.expression.StringConstantExpression;
import org.jd.core.v1.model.javasyntax.statement.TryStatement.CatchClause;
import org.jd.core.v1.model.javasyntax.statement.TryStatement.Resource;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.jd.core.v1.util.DefaultList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TryStatementTest {

	@Test
	public void testResource() {
		StringConstantExpression expression = new StringConstantExpression("test"); //$NON-NLS-1$
		ObjectType type = ObjectType.TYPE_STRING;
		TryStatement.Resource resource = new TryStatement.Resource(type, "resource1", expression); //$NON-NLS-1$

		assertEquals(type, resource.getType());
		assertEquals("resource1", resource.getName()); //$NON-NLS-1$
		assertEquals(expression, resource.getExpression());

		StringConstantExpression newExpression = new StringConstantExpression("newTest"); //$NON-NLS-1$
		resource.setExpression(newExpression);
		assertEquals(newExpression, resource.getExpression());
	}

	@Test
	public void testCatchClause() {
		Statements statements = new Statements();
		ObjectType type = ObjectType.TYPE_EXCEPTION;
		TryStatement.CatchClause catchClause = new TryStatement.CatchClause(0, type, "exception", statements); //$NON-NLS-1$

		assertEquals(0, catchClause.getLineNumber());
		assertEquals(type, catchClause.getType());
		assertEquals("exception", catchClause.getName()); //$NON-NLS-1$
		assertEquals(statements, catchClause.getStatements());

		ObjectType otherType = ObjectType.TYPE_RUNTIME_EXCEPTION;
		catchClause.addType(otherType);
		assertEquals(1, catchClause.getOtherTypes().size());
		assertEquals(otherType, catchClause.getOtherTypes().get(0));

		assertFalse(catchClause.isFinal());
		catchClause.setFinal(true);
		assertTrue(catchClause.isFinal());
	}

	@Test
	public void testTryStatement() {
		Statements tryStatements = new Statements();
		Statements finallyStatements = new Statements();
		DefaultList<TryStatement.CatchClause> catchClauses = new DefaultList<CatchClause>();
		TryStatement tryStatement = new TryStatement(tryStatements, catchClauses, finallyStatements);

		assertEquals(tryStatements, tryStatement.getTryStatements());
		assertEquals(catchClauses, tryStatement.getCatchClauses());
		assertEquals(finallyStatements, tryStatement.getFinallyStatements());

		assertTrue(tryStatement.isTryStatement());

		DefaultList<TryStatement.Resource> resources = new DefaultList<Resource>();
		StringConstantExpression expression = new StringConstantExpression("test"); //$NON-NLS-1$
		ObjectType type = ObjectType.TYPE_STRING;
		TryStatement.Resource resource = new TryStatement.Resource(type, "resource1", expression); //$NON-NLS-1$
		resources.add(resource);

		TryStatement resourceTryStatement = new TryStatement(resources, tryStatements, catchClauses, finallyStatements);
		assertEquals(resources, resourceTryStatement.getResources());
	}

	@Test
	public void testAcceptMethods() {
		// Create TypeMaker instance
		TypeMaker typeMaker = new TypeMaker();

		// Create resources
		DefaultList<TryStatement.Resource> resources = new DefaultList<Resource>();
		StringConstantExpression expression = new StringConstantExpression("test"); //$NON-NLS-1$
		resources.add(new TryStatement.Resource(typeMaker.makeFromInternalTypeName("ResourceType"), "resourceName", expression)); //$NON-NLS-1$ //$NON-NLS-2$

		// Create catch clauses
		DefaultList<TryStatement.CatchClause> catchClauses = new DefaultList<CatchClause>();
		catchClauses.add(new TryStatement.CatchClause(1, typeMaker.makeFromInternalTypeName("CatchClauseType"), "catchClauseName", new Statements())); //$NON-NLS-1$ //$NON-NLS-2$

		// Create try and finally statements
		Statements tryStatements = new Statements();
		Statements finallyStatements = new Statements();

		// Create TryStatement
		TryStatement tryStatement = new TryStatement(resources, tryStatements, catchClauses, finallyStatements);

		// Create TestVisitor and accept it
		TestVisitor visitor = new TestVisitor();
		tryStatement.accept(visitor);

		// Have each resource and catch clause accept the visitor
		for (TryStatement.Resource resource : resources) {
			resource.accept(visitor);
		}

		for (TryStatement.CatchClause catchClause : catchClauses) {
			catchClause.accept(visitor);
		}

		// Check the counts
		assertEquals(1, visitor.getTryStatementCount());
		assertEquals(1, visitor.getResourceCount());
		assertEquals(1, visitor.getCatchClauseCount());
	}

}
