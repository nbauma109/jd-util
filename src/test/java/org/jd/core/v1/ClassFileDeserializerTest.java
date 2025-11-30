/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.AnnotationElementValue;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.SimpleElementValue;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;
import org.jd.core.v1.util.StringConstants;
import org.jd.core.v1.util.ZipLoader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class ClassFileDeserializerTest extends TestCase {
	@Test
	public void testMissingClass() throws Exception {
		class NoOpLoader implements Loader {
			@Override
			public boolean canLoad(String internalName) {
				return false;
			}

			@Override
			public byte[] load(String internalName) throws IOException {
				fail("Loader cannot load anything"); //$NON-NLS-1$
				return null;
			}
		}

		ClassFileDeserializer deserializer = new ClassFileDeserializer();
		try {
			deserializer.loadClassFile(new NoOpLoader(), "DoesNotExist"); //$NON-NLS-1$
			fail("Expected exception"); //$NON-NLS-1$
		}
		// Expecting exception because class cannot be loaded
		catch (IllegalArgumentException expected) {
			assertEquals("Class DoesNotExist could not be loaded", expected.getMessage()); //$NON-NLS-1$
		}
	}

	@Test
	public void testAnnotatedClass() throws Exception {
		try (InputStream is = this.getClass().getResourceAsStream("/zip/data-java-jdk-1.7.0.zip")) { //$NON-NLS-1$
			ZipLoader loader = new ZipLoader(is);
			ClassFileDeserializer deserializer = new ClassFileDeserializer();

			ClassFile classFile = deserializer.loadClassFile(loader, "org/jd/core/test/AnnotatedClass"); //$NON-NLS-1$

			// Check class
			assertNotNull(classFile);
			assertEquals("org/jd/core/test/AnnotatedClass", classFile.getInternalTypeName()); //$NON-NLS-1$
			assertEquals("java/util/ArrayList", classFile.getSuperTypeName()); //$NON-NLS-1$

			Annotations invAttr = classFile.getAttribute(Const.ATTR_RUNTIME_INVISIBLE_ANNOTATIONS);
			assertNotNull(invAttr.getAnnotationEntries());
			assertEquals(2, invAttr.getNumAnnotations());
			assertNotNull(invAttr.getAnnotationEntries()[0].getElementValuePairs());
			assertEquals(1, invAttr.getAnnotationEntries()[0].getNumElementValuePairs());

			AnnotationElementValue annotationValue = (AnnotationElementValue) invAttr.getAnnotationEntries()[1].getElementValuePairs()[0].getValue();
			assertEquals("Lorg/jd/core/test/annotation/Name;", annotationValue.getAnnotationEntry().getAnnotationType()); //$NON-NLS-1$
			assertNotNull(annotationValue.getAnnotationEntry().getElementValuePairs());
			assertEquals(3, annotationValue.getAnnotationEntry().getNumElementValuePairs());
			assertEquals("salutation", annotationValue.getAnnotationEntry().getElementValuePairs()[0].getNameString()); //$NON-NLS-1$

			SimpleElementValue ev = (SimpleElementValue) annotationValue.getAnnotationEntry().getElementValuePairs()[1].getValue();
			assertEquals("Donald", ev.toString()); //$NON-NLS-1$

			// Check fields
			assertNotNull(classFile.getFields());
			assertEquals(10, classFile.getFields().length);

			// Check 1st field
			Field field = classFile.getFields()[1];
			assertEquals("b1", field.getName()); //$NON-NLS-1$
			assertEquals("B", field.getSignature()); //$NON-NLS-1$

			assertNotNull(field.getAnnotationEntries());
			assertEquals(1, field.getAnnotationEntries().length);
			assertNotNull(field.getAnnotationEntries()[0].getElementValuePairs());
			assertEquals(1, field.getAnnotationEntries()[0].getElementValuePairs().length);
			assertEquals("b", field.getAnnotationEntries()[0].getElementValuePairs()[0].getNameString()); //$NON-NLS-1$

			ev = (SimpleElementValue) field.getAnnotationEntries()[0].getElementValuePairs()[0].getValue();
			assertEquals(-15, ev.getValueByte());

			// Check 8th field
			field = classFile.getFields()[8];
			assertEquals("str2", field.getName()); //$NON-NLS-1$
			assertEquals("Ljava/lang/String;", field.getSignature()); //$NON-NLS-1$

			assertNotNull(field.getAnnotationEntries());
			assertEquals(1, field.getAnnotationEntries().length);
			assertNotNull(field.getAnnotationEntries()[0].getElementValuePairs());
			assertEquals(1, field.getAnnotationEntries()[0].getElementValuePairs().length);
			assertEquals("str", field.getAnnotationEntries()[0].getElementValuePairs()[0].getNameString()); //$NON-NLS-1$

			ev = (SimpleElementValue) field.getAnnotationEntries()[0].getElementValuePairs()[0].getValue();
			assertEquals("str \u0083 \u0909 \u1109", ev.toString()); //$NON-NLS-1$

			// Check getters
			assertNotNull(classFile.getMethods());
			assertEquals(3, classFile.getMethods().length);

			// Check constructor
			assertEquals(StringConstants.INSTANCE_CONSTRUCTOR, classFile.getMethods()[0].getName());
			assertEquals("()V", classFile.getMethods()[0].getSignature()); //$NON-NLS-1$
			assertNotNull(classFile.getMethods()[0].getCode());
		}
	}
}
