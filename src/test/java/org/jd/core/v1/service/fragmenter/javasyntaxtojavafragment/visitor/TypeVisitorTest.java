package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util.JavaFragmentFactory;
import org.junit.Test;

import static org.apache.bcel.Const.MAJOR_1_5;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor.TypeVisitor.UNKNOWN_LINE_NUMBER;

public class TypeVisitorTest {

    @Test
    public void testShouldUseQualifiedTypeName() {
        TypeVisitor samePackageVisitor = new TypeVisitor(
                new ClassPathLoader(),
                "java/util/HashMap",
                MAJOR_1_5,
                JavaFragmentFactory.newImportsFragment()
        );
        samePackageVisitor.currentType = samePackageVisitor.typeMaker.makeFromInternalTypeName("java/util/HashMap");
        assertTrue(samePackageVisitor.shouldUseQualifiedTypeName("java/util/Map$Entry", "Entry"));

        TypeVisitor javaLangVisitor = new TypeVisitor(
                new ClassPathLoader(),
                "java/lang/StringBuilder",
                MAJOR_1_5,
                JavaFragmentFactory.newImportsFragment()
        );
        javaLangVisitor.currentType = javaLangVisitor.typeMaker.makeFromInternalTypeName("java/lang/StringBuilder");
        assertTrue(javaLangVisitor.shouldUseQualifiedTypeName("java/lang/Thread$State", "State"));

        TypeVisitor importedTypeVisitor = new TypeVisitor(
                new ClassPathLoader(),
                "java/util/HashMap",
                MAJOR_1_5,
                JavaFragmentFactory.newImportsFragment()
        );
        importedTypeVisitor.currentType = importedTypeVisitor.typeMaker.makeFromInternalTypeName("java/util/HashMap");
        assertTrue(importedTypeVisitor.shouldUseQualifiedTypeName("org/w3c/dom/Node", "Node"));
        assertFalse(importedTypeVisitor.shouldUseQualifiedTypeName("org/w3c/dom/Document", "Document"));
    }

    private static TypeVisitor newVisitor(boolean realignLineNumbers) {
        TypeVisitor visitor = new TypeVisitor(new ClassPathLoader(), "java/util/HashMap", MAJOR_1_5, JavaFragmentFactory.newImportsFragment());
        visitor.setRealignLineNumbers(realignLineNumbers);
        return visitor;
    }

    @Test
    public void testLineNumbersNeverDecreaseOverTheCompilationUnitWhenNotRealigning() {
        TypeVisitor visitor = newVisitor(false);

        TypeVisitor.Tokens first = visitor.new Tokens();
        first.addLineNumberToken(1201);
        TypeVisitor.Tokens second = visitor.new Tokens();
        second.addLineNumberToken(12);

        assertEquals(1, first.size());
        assertEquals(1201, first.getCurrentLineNumber());
        // Out of order: dropped
        assertEquals(0, second.size());
        assertEquals(UNKNOWN_LINE_NUMBER, second.getCurrentLineNumber());
    }

    @Test
    public void testLineNumbersOnlyNeverDecreaseInsideAStatementWhenRealigning() {
        TypeVisitor visitor = newVisitor(true);

        TypeVisitor.Tokens first = visitor.new Tokens();
        first.addLineNumberToken(1201);
        first.addLineNumberToken(12);
        TypeVisitor.Tokens second = visitor.new Tokens();
        second.addLineNumberToken(12);
        second.addLineNumberToken(13);

        // Inside a statement: still in order
        assertEquals(1, first.size());
        // The layouter takes care of the statements which are out of order
        assertEquals(2, second.size());
        assertEquals(13, second.getCurrentLineNumber());
    }
}
