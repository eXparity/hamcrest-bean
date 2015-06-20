
package org.exparity.hamcrest.beans.comparators;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;
import org.hamcrest.Matcher;

/**
 * Implementation of a PropertyComparator which uses a hamcrest Matcher to test each value
 * 
 * @author Stewart Bissett
 */
public class Matches<T> implements PropertyComparator<T> {

	private final Matcher<T> matcher;

	public Matches(final Matcher<T> matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean matches(final T lhs, final T rhs) {
		return matcher.matches(lhs) && matcher.matches(rhs);
	}

}
