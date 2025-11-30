package org.jd.core.v1.model.javasyntax.declaration;

import org.junit.Test;
import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration.ModuleInfo;
import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration.PackageInfo;
import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration.ServiceInfo;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModuleDeclarationTest {

    @Test
    public void testGetVersion() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertEquals("1.0", moduleDeclaration.getVersion()); //$NON-NLS-1$
    }

    @Test
    public void testGetRequires() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertNotNull(moduleDeclaration.getRequires());
        assertEquals(2, moduleDeclaration.getRequires().size());
    }

    @Test
    public void testGetExports() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertNotNull(moduleDeclaration.getExports());
        assertEquals(3, moduleDeclaration.getExports().size());
    }

    @Test
    public void testGetOpens() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertNotNull(moduleDeclaration.getOpens());
        assertEquals(4, moduleDeclaration.getOpens().size());
    }

    @Test
    public void testGetUses() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertNotNull(moduleDeclaration.getUses());
        assertEquals(2, moduleDeclaration.getUses().size());
    }

    @Test
    public void testGetProvides() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertNotNull(moduleDeclaration.getProvides());
        assertEquals(3, moduleDeclaration.getProvides().size());
    }

    @Test
    public void testAccept() {
        TestDeclarationVisitor visitor = new TestDeclarationVisitor();
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        moduleDeclaration.accept(visitor);

        assertEquals(1, visitor.getModuleDeclarationCount());
    }

    @Test
    public void testToString() {
        ModuleDeclaration moduleDeclaration = ModuleDeclarationTest.createModuleDeclaration();
        assertEquals("ModuleDeclaration{internalName}", moduleDeclaration.toString()); //$NON-NLS-1$

        ModuleDeclaration.ModuleInfo moduleInfo = new ModuleDeclaration.ModuleInfo("module1", 0, "1.0"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("ModuleInfo{name=module1, flags=0, version=1.0}", moduleInfo.toString()); //$NON-NLS-1$

        ModuleDeclaration.PackageInfo packageInfo = new ModuleDeclaration.PackageInfo("package1", 0, List.of("module1", "module2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("PackageInfo{internalName=package1, flags=0, moduleInfoNames=[module1, module2]}", packageInfo.toString()); //$NON-NLS-1$

        ModuleDeclaration.ServiceInfo serviceInfo = new ModuleDeclaration.ServiceInfo("interface1", List.of("impl1", "impl2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("ServiceInfo{interfaceTypeName=interface1, implementationTypeNames=[impl1, impl2]}", serviceInfo.toString()); //$NON-NLS-1$
    }

    private static ModuleDeclaration createModuleDeclaration() {
        List<ModuleDeclaration.ModuleInfo> requires = new ArrayList<ModuleInfo>();
        requires.add(new ModuleDeclaration.ModuleInfo("module1", 0, "1.0")); //$NON-NLS-1$ //$NON-NLS-2$
        requires.add(new ModuleDeclaration.ModuleInfo("module2", 1, null)); //$NON-NLS-1$

        List<ModuleDeclaration.PackageInfo> exports = new ArrayList<PackageInfo>();
        exports.add(new ModuleDeclaration.PackageInfo("package1", 0, null)); //$NON-NLS-1$
        exports.add(new ModuleDeclaration.PackageInfo("package2", 1, List.of("module1"))); //$NON-NLS-1$ //$NON-NLS-2$
        exports.add(new ModuleDeclaration.PackageInfo("package3", 2, List.of("module1", "module2"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        List<ModuleDeclaration.PackageInfo> opens = new ArrayList<PackageInfo>();
        opens.add(new ModuleDeclaration.PackageInfo("package4", 0, null)); //$NON-NLS-1$
        opens.add(new ModuleDeclaration.PackageInfo("package5", 1, List.of("module1"))); //$NON-NLS-1$ //$NON-NLS-2$
        opens.add(new ModuleDeclaration.PackageInfo("package6", 2, List.of("module1", "module2"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        opens.add(new ModuleDeclaration.PackageInfo("package7", 3, List.of())); //$NON-NLS-1$

        List<String> uses = new ArrayList<String>();
        uses.add("service1"); //$NON-NLS-1$
        uses.add("service2"); //$NON-NLS-1$

        List<ModuleDeclaration.ServiceInfo> provides = new ArrayList<ServiceInfo>();
        provides.add(new ModuleDeclaration.ServiceInfo("interface1", List.of("impl1"))); //$NON-NLS-1$ //$NON-NLS-2$
        provides.add(new ModuleDeclaration.ServiceInfo("interface2", List.of("impl2", "impl3"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        provides.add(new ModuleDeclaration.ServiceInfo("interface3", null)); //$NON-NLS-1$

        return new ModuleDeclaration(0, "internalName", "name", "1.0", requires, exports, opens, uses, provides); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
