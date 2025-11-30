package jd.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import jd.core.links.DeclarationData;
import jd.core.links.HyperlinkData;
import jd.core.links.ReferenceData;
import jd.core.links.StringData;

public class DecompilationResult {

    private String decompiledOutput;
    private Map<String, DeclarationData> declarations = new HashMap<String, DeclarationData>();
    private NavigableMap<Integer, DeclarationData> typeDeclarations = new TreeMap<Integer, DeclarationData>();
    private List<ReferenceData> references = new ArrayList<ReferenceData>();
    private List<StringData> strings = new ArrayList<StringData>();
    private SortedMap<Integer, HyperlinkData> hyperlinks = new TreeMap<Integer, HyperlinkData>();
    private Map<Integer, Integer> lineNumbers = new TreeMap<Integer, Integer>();
    private int maxLineNumber;

    public String getDecompiledOutput() {
        return decompiledOutput;
    }

    public void setDecompiledOutput(String decompiledOutput) {
        this.decompiledOutput = decompiledOutput;
    }

    public Map<String, DeclarationData> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(Map<String, DeclarationData> declarations) {
        this.declarations = declarations;
    }

    public NavigableMap<Integer, DeclarationData> getTypeDeclarations() {
        return typeDeclarations;
    }

    public void setTypeDeclarations(NavigableMap<Integer, DeclarationData> typeDeclarations) {
        this.typeDeclarations = typeDeclarations;
    }

    public List<ReferenceData> getReferences() {
        return references;
    }

    public void setReferences(List<ReferenceData> references) {
        this.references = references;
    }

    public List<StringData> getStrings() {
        return strings;
    }

    public void setStrings(List<StringData> strings) {
        this.strings = strings;
    }

    public Map<Integer, Integer> getLineNumbers() {
        return lineNumbers;
    }

    public void setLineNumbers(Map<Integer, Integer> lineNumbers) {
        this.lineNumbers = lineNumbers;
    }

    public int getMaxLineNumber() {
        return maxLineNumber;
    }

    public void setMaxLineNumber(int maxLineNumber) {
        this.maxLineNumber = maxLineNumber;
    }

    public void addString(StringData stringData) {
        strings.add(stringData);
    }

    public void addDeclaration(String internalTypeName, DeclarationData declarationData) {
        declarations.put(internalTypeName, declarationData);
    }

    public void addTypeDeclaration(int position, DeclarationData declarationData) {
        typeDeclarations.put(position, declarationData);
    }

    public void addHyperLink(int position, HyperlinkData hyperlinkData) {
        hyperlinks.put(position, hyperlinkData);
    }

    public void putLineNumber(int lineNumber, int sourceLineNumber) {
        lineNumbers.put(lineNumber, sourceLineNumber);
    }

    public void addReference(ReferenceData reference) {
        references.add(reference);
    }

    public SortedMap<Integer, HyperlinkData> getHyperlinks() {
        return hyperlinks;
    }

    public void setHyperlinks(SortedMap<Integer, HyperlinkData> hyperlinks) {
        this.hyperlinks = hyperlinks;
    }
}
