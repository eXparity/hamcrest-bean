
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a string property regardless of case
 * 
 * @author Stewart Bissett
 */
public class IsEqualsIgnoreCase implements PropertyComparator {

	public boolean matches(final Object lhs, final Object rhs) {
		return lhs instanceof String && rhs instanceof String && ((String) lhs).equalsIgnoreCase((String) rhs);
	}

}
