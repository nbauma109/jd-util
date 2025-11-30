package org.jd.core.v1.model.javasyntax.reference;

public class TestReferenceVisitor implements ReferenceVisitor {

	private int visitAnnotationElementValueCount;
	private int visitAnnotationReferenceCount;
	private int visitAnnotationReferencesCount;
	private int visitElementValueArrayInitializerElementValueCount;
	private int visitElementValuesCount;
	private int visitElementValuePairCount;
	private int visitElementValuePairsCount;
	private int visitExpressionElementValueCount;

	@Override
	public void visit(AnnotationElementValue reference) {
		visitAnnotationElementValueCount++;
	}

	@Override
	public void visit(AnnotationReference reference) {
		visitAnnotationReferenceCount++;
	}

	@Override
	public void visit(AnnotationReferences<? extends AnnotationReference> references) {
		visitAnnotationReferencesCount++;
	}

	@Override
	public void visit(ElementValueArrayInitializerElementValue reference) {
		visitElementValueArrayInitializerElementValueCount++;
	}

	@Override
	public void visit(ElementValues references) {
		visitElementValuesCount++;
	}

	@Override
	public void visit(ElementValuePair reference) {
		visitElementValuePairCount++;
	}

	@Override
	public void visit(ElementValuePairs references) {
		visitElementValuePairsCount++;
	}

	@Override
	public void visit(ExpressionElementValue reference) {
		visitExpressionElementValueCount++;
	}

	// Getter methods to check the counts
	public int getVisitAnnotationElementValueCount() {
		return visitAnnotationElementValueCount;
	}

	public int getVisitAnnotationReferenceCount() {
		return visitAnnotationReferenceCount;
	}

	public int getVisitAnnotationReferencesCount() {
		return visitAnnotationReferencesCount;
	}

	public int getVisitElementValueArrayInitializerElementValueCount() {
		return visitElementValueArrayInitializerElementValueCount;
	}

	public int getVisitElementValuesCount() {
		return visitElementValuesCount;
	}

	public int getVisitElementValuePairCount() {
		return visitElementValuePairCount;
	}

	public int getVisitElementValuePairsCount() {
		return visitElementValuePairsCount;
	}

	public int getVisitExpressionElementValueCount() {
		return visitExpressionElementValueCount;
	}
}
