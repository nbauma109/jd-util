package org.jd.core.v1.model.javasyntax.declaration;

import org.junit.Test;
import org.jd.core.v1.service.converter.classfiletojavasyntax.visitor.TestDeclarationVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModuleDeclarationTest {

    @Test
    public void testGetVersion() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertEquals("1.0", moduleDeclaration.getVersion());
    }

    @Test
    public void testGetRequires() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertNotNull(moduleDeclaration.getRequires());
        assertEquals(2, moduleDeclaration.getRequires().size());
    }

    @Test
    public void testGetExports() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertNotNull(moduleDeclaration.getExports());
        assertEquals(3, moduleDeclaration.getExports().size());
    }

    @Test
    public void testGetOpens() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertNotNull(moduleDeclaration.getOpens());
        assertEquals(4, moduleDeclaration.getOpens().size());
    }

    @Test
    public void testGetUses() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertNotNull(moduleDeclaration.getUses());
        assertEquals(2, moduleDeclaration.getUses().size());
    }

    @Test
    public void testGetProvides() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertNotNull(moduleDeclaration.getProvides());
        assertEquals(3, moduleDeclaration.getProvides().size());
    }

    @Test
    public void testAccept() {
        TestDeclarationVisitor visitor = new TestDeclarationVisitor();
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        moduleDeclaration.accept(visitor);

        assertEquals(1, visitor.getModuleDeclarationCount());
    }

    @Test
    public void testToString() {
        ModuleDeclaration moduleDeclaration = createModuleDeclaration();
        assertEquals("ModuleDeclaration{internalName}", moduleDeclaration.toString());

        ModuleDeclaration.ModuleInfo moduleInfo = new ModuleDeclaration.ModuleInfo("module1", 0, "1.0");
        assertEquals("ModuleInfo{name=module1, flags=0, version=1.0}", moduleInfo.toString());

        ModuleDeclaration.PackageInfo packageInfo = new ModuleDeclaration.PackageInfo("package1", 0, List.of("module1", "module2"));
        assertEquals("PackageInfo{internalName=package1, flags=0, moduleInfoNames=[module1, module2]}", packageInfo.toString());

        ModuleDeclaration.ServiceInfo serviceInfo = new ModuleDeclaration.ServiceInfo("interface1", List.of("impl1", "impl2"));
        assertEquals("ServiceInfo{interfaceTypeName=interface1, implementationTypeNames=[impl1, impl2]}", serviceInfo.toString());
    }

    private static ModuleDeclaration createModuleDeclaration() {
        List<ModuleDeclaration.ModuleInfo> requires = new ArrayList<>();
        requires.add(new ModuleDeclaration.ModuleInfo("module1", 0, "1.0"));
        requires.add(new ModuleDeclaration.ModuleInfo("module2", 1, null));

        List<ModuleDeclaration.PackageInfo> exports = new ArrayList<>();
        exports.add(new ModuleDeclaration.PackageInfo("package1", 0, null));
        exports.add(new ModuleDeclaration.PackageInfo("package2", 1, List.of("module1")));
        exports.add(new ModuleDeclaration.PackageInfo("package3", 2, List.of("module1", "module2")));

        List<ModuleDeclaration.PackageInfo> opens = new ArrayList<>();
        opens.add(new ModuleDeclaration.PackageInfo("package4", 0, null));
        opens.add(new ModuleDeclaration.PackageInfo("package5", 1, List.of("module1")));
        opens.add(new ModuleDeclaration.PackageInfo("package6", 2, List.of("module1", "module2")));
        opens.add(new ModuleDeclaration.PackageInfo("package7", 3, List.of()));

        List<String> uses = new ArrayList<>();
        uses.add("service1");
        uses.add("service2");

        List<ModuleDeclaration.ServiceInfo> provides = new ArrayList<>();
        provides.add(new ModuleDeclaration.ServiceInfo("interface1", List.of("impl1")));
        provides.add(new ModuleDeclaration.ServiceInfo("interface2", List.of("impl2", "impl3")));
        provides.add(new ModuleDeclaration.ServiceInfo("interface3", null));

        return new ModuleDeclaration(0, "internalName", "name", "1.0", requires, exports, opens, uses, provides);
    }
}
