
package org.exparity.hamcrest.beans.testutils.types;

/**
 * @author Stewart Bissett
 */
public class NotBean {

	private String stringA;
	private final String stringB;

	public NotBean(final String stringA, final String stringB) {
		this.stringA = stringA;
		this.stringB = stringB;
	}

	public String getStringA() {
		return stringA;
	}

	public void setStringA(final String stringA) {
		this.stringA = stringA;
	}

	public String getStringB() {
		return stringB;
	}

}
