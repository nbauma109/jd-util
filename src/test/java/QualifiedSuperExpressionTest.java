import org.jd.core.v1.model.javasyntax.expression.QualifiedSuperExpression;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Assert;
import org.junit.Test;

public class QualifiedSuperExpressionTest {

    @Test
    public void testQualifiedSuperExpression() {
        // Arrange
        ObjectType type = ObjectType.TYPE_OBJECT;
        int lineNumber = 10;
        TestVisitor visitor = new TestVisitor();

        // Act
        QualifiedSuperExpression expression = new QualifiedSuperExpression(lineNumber, type);
        expression.accept(visitor);

        // Assert
        Assert.assertEquals(lineNumber, expression.getLineNumber());
        Assert.assertEquals(type, expression.getType());
        Assert.assertTrue(expression.isSuperExpression());
        Assert.assertEquals(1, visitor.getQualifiedSuperExpressionCount());

        // Test toString method
        String expectedToString = "QualifiedSuperExpression{ObjectType{java/lang/Object}}";
        Assert.assertEquals(expectedToString, expression.toString());

        // Test copyTo method
        int newLineNumber = 20;
        QualifiedSuperExpression copiedExpression = (QualifiedSuperExpression) expression.copyTo(newLineNumber);
        Assert.assertEquals(newLineNumber, copiedExpression.getLineNumber());
        Assert.assertEquals(type, copiedExpression.getType());
    }
}
