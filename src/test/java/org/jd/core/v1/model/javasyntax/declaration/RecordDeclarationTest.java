package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.apache.bcel.Const.ACC_PUBLIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordDeclarationTest {

    private RecordDeclaration recordDeclaration;
    private TestDeclarationVisitor visitor;

    @Before
    public void setUp() {
        RecordDeclaration.RecordComponent component =
                new RecordDeclaration.RecordComponent(
                        null,
                        ObjectType.TYPE_STRING,
                        "name"
                );

        recordDeclaration =
                new RecordDeclaration(
                        null,
                        ACC_PUBLIC,
                        "org/jd/core/v1/service/test/RecordTest",
                        "RecordTest",
                        null,
                        Collections.singletonList(component),
                        null,
                        null
                );

        visitor = new TestDeclarationVisitor();
    }

    @Test
    public void testRecordDeclaration() {
        // Act
        recordDeclaration.accept(visitor);

        // Assert
        assertEquals(1, visitor.getRecordDeclarationCount());
        assertTrue(recordDeclaration.isClassDeclaration());
        assertEquals(
                ObjectType.TYPE_RECORD,
                recordDeclaration.getSuperType()
        );
        assertEquals(1, recordDeclaration.getComponents().size());
        assertNull(recordDeclaration.getTypeParameters());
        assertNull(recordDeclaration.getInterfaces());
        assertEquals("name", recordDeclaration.getComponents().get(0).getName());
        assertEquals(ObjectType.TYPE_STRING, recordDeclaration.getComponents().get(0).getType());
        assertNull(recordDeclaration.getComponents().get(0).getAnnotationReferences());
    }

    @Test
    public void testToString() {
        // Act & Assert
        assertEquals(
                "RecordDeclaration{org/jd/core/v1/service/test/RecordTest}",
                recordDeclaration.toString()
        );
    }
}
