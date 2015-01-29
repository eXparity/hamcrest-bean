
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a string property against a regular expression
 * 
 * @author Stewart Bissett
 */
public class HasPattern implements PropertyComparator<String> {

	private final String regex;

	public HasPattern(final String regex) {
		this.regex = regex;
	}

	public boolean matches(final String lhs, final String rhs) {
		return lhs == null ? rhs == null : rhs.matches(regex);
	}

}
