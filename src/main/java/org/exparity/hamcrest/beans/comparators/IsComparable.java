
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a string property regardless of case
 * 
 * @author Stewart Bissett
 */
public class IsComparable<T extends Comparable<T>> implements PropertyComparator<T> {

	public boolean matches(final T lhs, final T rhs) {
		return lhs == null ? rhs == null : lhs.getClass().isInstance(rhs) && lhs.compareTo(rhs) == 0;
	}

}
