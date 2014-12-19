
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of a {@link PropertyComparator} which matches a properties regardless
 * 
 * @author Stewart Bissett
 */
public class Ignored implements PropertyComparator {

	public boolean matches(final Object lhs, final Object rhs) {
		return true;
	}
}
