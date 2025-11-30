/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 *
 * Original source code location :
 * https://github.com/java-decompiler/jd-gui/blob/master/services/src/main/java/org/jd/gui/view/component/HyperlinkPage.java
 */
package jd.core.links;

public class HyperlinkData {

	private final int startPosition;
	private final int endPosition;
	private boolean enabled;

	public HyperlinkData(int startPosition, int endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
