
package org.exparity.hamcrest.beans.testutils.types;

/**
 * @author Stewart Bissett
 */
public class SimpleType {

	private String value;

	public SimpleType(final String value) {
		this.value = value;
	}

	public SimpleType() {
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SimpleType)) {
			return false;
		}
		SimpleType rhs = (SimpleType) obj;
		return rhs.value.equals(value);
	}
}
