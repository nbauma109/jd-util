package jd.core.links;

import org.jd.core.v1.util.StringConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeclarationDataTest {

	@Test
	public void testDeclarationData() {
		// Arrange
		int startPosition = 10;
		int length = 5;
		String typeName = "TestType"; //$NON-NLS-1$
		String fieldName = "TestField"; //$NON-NLS-1$
		String fieldDescriptor = "I"; //$NON-NLS-1$
		String methodName = "TestMethod"; //$NON-NLS-1$
		String methodDescriptor = "(I)V"; //$NON-NLS-1$
		String constructorName = StringConstants.INSTANCE_CONSTRUCTOR;
		String constructorDescriptor = "(Ljava/lang/String;)V"; //$NON-NLS-1$

		// Act
		DeclarationData fieldType = new DeclarationData(startPosition, length, typeName, fieldName, fieldDescriptor);
		DeclarationData methodType = new DeclarationData(startPosition, length, typeName, methodName, methodDescriptor);
		DeclarationData constructorType = new DeclarationData(startPosition, length, typeName, constructorName, constructorDescriptor);
		DeclarationData typeType = new DeclarationData(startPosition, length, typeName, null, null);

		// Assert
		assertTrue(fieldType.isAField());
		assertFalse(fieldType.isAMethod());
		assertFalse(fieldType.isAConstructor());
		assertFalse(fieldType.isAType());

		assertFalse(methodType.isAField());
		assertTrue(methodType.isAMethod());
		assertFalse(methodType.isAConstructor());
		assertFalse(methodType.isAType());

		assertFalse(constructorType.isAField());
		assertTrue(constructorType.isAMethod());
		assertTrue(constructorType.isAConstructor());
		assertFalse(constructorType.isAType());

		assertFalse(typeType.isAField());
		assertFalse(typeType.isAMethod());
		assertFalse(typeType.isAConstructor());
		assertTrue(typeType.isAType());

		assertEquals(startPosition, fieldType.getStartPosition());
		assertEquals(startPosition + length, fieldType.getEndPosition());
		assertEquals(typeName, fieldType.getTypeName());
		assertEquals(fieldName, fieldType.getName());

		String expectedString = "DeclarationData [typeName=" + typeName + ", name=" + fieldName + ", descriptor=" + fieldDescriptor + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals(expectedString, fieldType.toString());
	}
}
