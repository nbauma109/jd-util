package jd.core.links;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import jd.core.DecompilationResult;

public class DecompilationResultTest {

    private DecompilationResult result;

    @Before
    public void setUp() {
        result = new DecompilationResult();
    }

    @Test
    public void testSetAndGetDecompiledOutput() {
        String expectedOutput = "Decompiled output string";
        result.setDecompiledOutput(expectedOutput);
        assertEquals(expectedOutput, result.getDecompiledOutput());
    }

    @Test
    public void testSetAndGetDeclarations() {
        DeclarationData declarationData = new DeclarationData(1, 1, "Type1", "Name1", "Descriptor1");
        Map<String, DeclarationData> declarations = Collections.singletonMap("InternalTypeName", declarationData);
        result.setDeclarations(declarations);
        assertEquals(declarations, result.getDeclarations());
    }

    @Test
    public void testSetAndGetTypeDeclarations() {
        DeclarationData declarationData = new DeclarationData(1, 1, "Type1", "Name1", "Descriptor1");
        NavigableMap<Integer, DeclarationData> typeDeclarations = new TreeMap<>(Collections.singletonMap(1, declarationData));
        result.setTypeDeclarations(typeDeclarations);
        assertEquals(typeDeclarations, result.getTypeDeclarations());
    }

    @Test
    public void testSetAndGetReferences() {
        ReferenceData referenceData = new ReferenceData("TypeName1", "Name1", "Descriptor1", "Owner1");
        List<ReferenceData> references = Collections.singletonList(referenceData);
        result.setReferences(references);
        assertEquals(references, result.getReferences());
    }

    @Test
    public void testSetAndGetStringData() {
        StringData stringData = new StringData(1, "Text1", "Owner1");
        List<StringData> stringDataList = Collections.singletonList(stringData);
        result.setStrings(stringDataList);
        assertEquals(stringDataList, result.getStrings());
    }

    @Test
    public void testSetAndGetLineNumbers() {
        Map<Integer, Integer> lineNumbers = Collections.singletonMap(1, 100);
        result.setLineNumbers(lineNumbers);
        assertEquals(lineNumbers, result.getLineNumbers());
    }

    @Test
    public void testSetAndGetMaxLineNumber() {
        int maxLineNumber = 200;
        result.setMaxLineNumber(maxLineNumber);
        assertEquals(maxLineNumber, result.getMaxLineNumber());
    }

    @Test
    public void testSetAndGetHyperlinks() {
        HyperlinkData hyperlinkData = new HyperlinkData(1, 2);
        SortedMap<Integer, HyperlinkData> hyperlinks = new TreeMap<>(Collections.singletonMap(1, hyperlinkData));
        result.setHyperlinks(hyperlinks);
        assertEquals(hyperlinks, result.getHyperlinks());
    }

    @Test
    public void testAddString() {
        StringData stringData = new StringData(1, "test", "owner");
        result.addString(stringData);
        assertTrue(result.getStrings().contains(stringData));
    }

    @Test
    public void testAddDeclaration() {
        String internalTypeName = "InternalTypeName";
        DeclarationData declarationData = new DeclarationData(1, 1, "Type1", "Name1", "Descriptor1");
        result.addDeclaration(internalTypeName, declarationData);
        assertTrue(result.getDeclarations().containsKey(internalTypeName));
        assertEquals(declarationData, result.getDeclarations().get(internalTypeName));
    }

    @Test
    public void testAddTypeDeclaration() {
        int position = 1;
        DeclarationData declarationData = new DeclarationData(1, 1, "Type1", "Name1", "Descriptor1");
        result.addTypeDeclaration(position, declarationData);
        assertTrue(result.getTypeDeclarations().containsKey(position));
        assertEquals(declarationData, result.getTypeDeclarations().get(position));
    }

    @Test
    public void testAddHyperLink() {
        int position = 1;
        HyperlinkData hyperlinkData = new HyperlinkData(1, 2);
        result.addHyperLink(position, hyperlinkData);
        assertTrue(result.getHyperlinks().containsKey(position));
        assertEquals(hyperlinkData, result.getHyperlinks().get(position));
    }

    @Test
    public void testPutLineNumber() {
        int lineNumber = 1;
        int sourceLineNumber = 100;
        result.putLineNumber(lineNumber, sourceLineNumber);
        assertTrue(result.getLineNumbers().containsKey(lineNumber));
        assertEquals(Integer.valueOf(sourceLineNumber), result.getLineNumbers().get(lineNumber));
    }

    @Test
    public void testAddReference() {
        ReferenceData referenceData = new ReferenceData("TypeName1", "Name1", "Descriptor1", "Owner1");
        result.addReference(referenceData);
        assertTrue(result.getReferences().contains(referenceData));
    }
}
