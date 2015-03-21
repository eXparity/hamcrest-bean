package org.exparity.hamcrest.beans.testutils.types;

/**
 * @author Stewart Bissett
 */
public class SimpleTypeWithSimpleType {

	private SimpleType value;

	public SimpleTypeWithSimpleType(final SimpleType value) {
		this.value = value;
	}

	public SimpleTypeWithSimpleType() {
		// TODO Auto-generated constructor stub
	}

	public SimpleType getValue() {
		return value;
	}

	public void setValue(final SimpleType value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SimpleTypeWithSimpleType)) {
			return false;
		}
		SimpleTypeWithSimpleType rhs = (SimpleTypeWithSimpleType) obj;
		return rhs.value.equals(value);
	}
}
