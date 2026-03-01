package org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.visitor;

import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.service.fragmenter.javasyntaxtojavafragment.util.JavaFragmentFactory;
import org.junit.Test;

import static org.apache.bcel.Const.MAJOR_1_5;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
