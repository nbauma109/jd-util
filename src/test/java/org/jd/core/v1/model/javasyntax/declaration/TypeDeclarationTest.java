package org.jd.core.v1.model.javasyntax.declaration;

import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration.ModuleInfo;
import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration.PackageInfo;
import org.jd.core.v1.model.javasyntax.declaration.ModuleDeclaration.ServiceInfo;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeclarationTest {

	@Test
	public void test() throws Exception {
		ModuleInfo moduleInfo = new ModuleInfo(null, 0, null);
		PackageInfo packageInfo = new PackageInfo(null, 0, null);
		ServiceInfo serviceInfo = new ServiceInfo(null, null);
		List<ModuleInfo> requires = Collections.singletonList(moduleInfo);
		List<PackageInfo> exports = Collections.singletonList(packageInfo);
		List<PackageInfo> opens = Collections.singletonList(packageInfo);
		List<String> uses = Collections.emptyList();
		List<ServiceInfo> provides = Collections.singletonList(serviceInfo);
		ModuleDeclaration moduleDeclaration = new ModuleDeclaration(0, null, null, null, requires, exports, opens, uses, provides);
		assertNull(moduleDeclaration.getAnnotationReferences());
		assertEquals(0, moduleDeclaration.getFlags());
		assertNull(moduleDeclaration.getName());
		assertNull(moduleDeclaration.getInternalTypeName());
		assertNull(moduleDeclaration.getBodyDeclaration());
		assertEquals("ModuleInfo{name=null, flags=0}", moduleInfo.toString()); //$NON-NLS-1$
		assertEquals("PackageInfo{internalName=null, flags=0}", packageInfo.toString()); //$NON-NLS-1$
		assertEquals("ServiceInfo{interfaceTypeName=null}", serviceInfo.toString()); //$NON-NLS-1$
	}
}
