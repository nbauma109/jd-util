/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.javafragment;

import org.jd.core.v1.model.fragment.FlexibleFragment;
import org.jd.core.v1.model.javasyntax.JavaImport;
import org.jd.core.v1.util.DefaultList;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImportsFragment extends FlexibleFragment implements JavaFragment {

    private final Map<String, JavaImport> importMap = new HashMap<>();
    /** The imports which are kept whatever the number of lines left to the imports: a name that is written without its package depends on them */
    private final Set<String> pinned = new HashSet<>();

    public ImportsFragment(int weight) {
        super(0, -1, -1, weight, "Imports");
    }

    public void addImport(String internalName, String qualifiedName) {
        JavaImport imp = importMap.get(internalName);

        if (imp == null) {
            importMap.put(internalName, new JavaImport(internalName, qualifiedName));
        } else {
            imp.incCounter();
        }
    }

    public void addImport(JavaImport javaImport) {
        JavaImport imp = importMap.get(javaImport.getInternalName());

        if (imp == null) {
            importMap.put(javaImport.getInternalName(), javaImport);
        } else {
            imp.incCounter();
        }
    }

    public boolean incCounter(String internalName) {
        JavaImport imp = importMap.get(internalName);

        if (imp == null) {
            return false;
        }
        imp.incCounter();
        return true;
    }

    public boolean isEmpty() {
        return importMap.isEmpty();
    }

    public void initLineCounts() {
        maximalLineCount = initialLineCount = lineCount = importMap.size();
    }

    /** Keeps the import when the imports are cut to the lines they have: an annotation is written in front of a name that must stay as it is */
    public void keep(String internalName) {
        pinned.add(internalName);
    }

    public boolean contains(String internalName) {
        return importMap.containsKey(internalName);
    }

    @Override
    public int getLineCount() {
        if (lineCount == -1) {
            throw new IllegalStateException("Call initLineCounts() before");
        }
        return lineCount;
    }

    public Collection<JavaImport> getImports() {
        int lineCount = Math.max(getLineCount(), (int) importMap.keySet().stream().filter(pinned::contains).count());
        int size = importMap.size();

        if (lineCount >= size) {
            return importMap.values();
        }
        DefaultList<JavaImport> imports = new DefaultList<>(importMap.values());
        imports.sort(Comparator.comparing((JavaImport imp0rt) -> !pinned.contains(imp0rt.getInternalName())).thenComparing(Comparator.comparing(JavaImport::getCounter).reversed()));
        // Remove less used imports
        List<JavaImport> subList = imports.subList(lineCount, size);
        for (JavaImport imp0rt : subList) {
            importMap.remove(imp0rt.getInternalName());
        }
        subList.clear();
        return imports;
    }

    @Override
    public void accept(JavaFragmentVisitor visitor) {
        visitor.visit(this);
    }

}
