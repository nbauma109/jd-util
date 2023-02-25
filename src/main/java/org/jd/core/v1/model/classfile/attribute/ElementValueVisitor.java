/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.classfile.attribute;

import org.apache.bcel.classfile.AnnotationElementValue;
import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ClassElementValue;
import org.apache.bcel.classfile.EnumElementValue;
import org.apache.bcel.classfile.SimpleElementValue;

public interface ElementValueVisitor {
    void visit(SimpleElementValue elementValue);
    void visit(ClassElementValue elementValue);
    void visit(AnnotationElementValue elementValue);
    void visit(EnumElementValue elementValue);
    void visit(ArrayElementValue elementValue);
}
