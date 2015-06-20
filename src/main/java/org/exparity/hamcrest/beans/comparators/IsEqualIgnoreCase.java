
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a string property regardless of case
 * 
 * @author Stewart Bissett
 */
public class IsEqualIgnoreCase implements PropertyComparator<String> {

	public boolean matches(final String lhs, final String rhs) {
		return lhs == null ? rhs == null : lhs.equalsIgnoreCase(rhs);
	}

}
