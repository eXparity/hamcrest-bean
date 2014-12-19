
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a string property regardless of case
 * 
 * @author Stewart Bissett
 */
public class IsComparable implements PropertyComparator {

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public boolean matches(final Object lhs, final Object rhs) {
		return lhs instanceof Comparable && rhs instanceof Comparable && ((Comparable) lhs).compareTo(rhs) == 0;
	}

}
