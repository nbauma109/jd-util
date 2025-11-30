/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 *
 * Original source code location :
 * https://github.com/java-decompiler/jd-gui/blob/master/services/src/main/java/org/jd/gui/view/component/TypePage.java
 */
package jd.core.links;

public class HyperlinkReferenceData extends HyperlinkData {

	private final ReferenceData reference;

	public HyperlinkReferenceData(int startPosition, int length, ReferenceData reference) {
		super(startPosition, startPosition+length);
		this.reference = reference;
	}

	public ReferenceData getReference() {
		return reference;
	}

	@Override
	public String toString() {
		return reference.toString();
	}
}
