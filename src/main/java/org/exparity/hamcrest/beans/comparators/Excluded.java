
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a properties regardless
 * 
 * @author Stewart Bissett
 */
public class Excluded<T> implements PropertyComparator<T> {

	public boolean matches(final T lhs, final T rhs) {
		return true;
	}
}
