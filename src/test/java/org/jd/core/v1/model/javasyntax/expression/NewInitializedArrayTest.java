package org.jd.core.v1.model.javasyntax.expression;

import org.jd.core.v1.model.javasyntax.declaration.ArrayVariableInitializer;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.model.javasyntax.type.Type;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NewInitializedArrayTest {
    private static final int LINE_NUMBER = 1;
    private static final Type TEST_TYPE = ObjectType.TYPE_STRING;

    @Test
    public void testNewInitializedArray() {
        ArrayVariableInitializer arrayInitializer = new ArrayVariableInitializer(NewInitializedArrayTest.TEST_TYPE);
        NewInitializedArray newInitializedArray = new NewInitializedArray(NewInitializedArrayTest.LINE_NUMBER, NewInitializedArrayTest.TEST_TYPE, arrayInitializer);

        assertSame(arrayInitializer, newInitializedArray.getArrayInitializer());
        assertEquals(0, newInitializedArray.getPriority());
        assertTrue(newInitializedArray.isNewInitializedArray());
        assertTrue(newInitializedArray.isNew());

        String expectedString = "NewInitializedArray{new " + NewInitializedArrayTest.TEST_TYPE + " [" + arrayInitializer + "]}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals(expectedString, newInitializedArray.toString());

        int newLineNumber = 2;
        Expression copiedNewInitializedArray = newInitializedArray.copyTo(newLineNumber);

        assertTrue(copiedNewInitializedArray instanceof NewInitializedArray);
        assertEquals(newLineNumber, copiedNewInitializedArray.getLineNumber());
        assertEquals(NewInitializedArrayTest.TEST_TYPE, ((NewInitializedArray) copiedNewInitializedArray).getType());
        assertSame(arrayInitializer, ((NewInitializedArray) copiedNewInitializedArray).getArrayInitializer());
    }

    @Test
    public void testAcceptWithTestVisitor() {
        TestVisitor visitor = new TestVisitor();

        ArrayVariableInitializer arrayInitializer = new ArrayVariableInitializer(NewInitializedArrayTest.TEST_TYPE);
        NewInitializedArray newInitializedArray = new NewInitializedArray(NewInitializedArrayTest.LINE_NUMBER, NewInitializedArrayTest.TEST_TYPE, arrayInitializer);

        newInitializedArray.accept(visitor);

        assertEquals(1, visitor.getNewInitializedArrayCount());
    }
}
