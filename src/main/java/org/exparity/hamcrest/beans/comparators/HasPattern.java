
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a string property against a regular expression
 * 
 * @author Stewart Bissett
 */
public class HasPattern implements PropertyComparator {

	private final String regex;

	public HasPattern(final String regex) {
		this.regex = regex;
	}

	public boolean matches(final Object lhs, final Object rhs) {
		return lhs == null ? rhs == null : rhs instanceof String && ((String) rhs).matches(regex);
	}

}
