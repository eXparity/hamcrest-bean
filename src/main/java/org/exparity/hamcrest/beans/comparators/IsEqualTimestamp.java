/**
 * 
 */
package org.exparity.hamcrest.beans.comparators;

import java.util.Date;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of {@link PropertyComparator} which compares two dates to see
 * if the date and time are equal down to the millisecond
 * 
 * @author Stewart Bissett
 */
public class IsEqualTimestamp implements PropertyComparator<Date> {

	@Override
	public boolean matches(Date lhs, Date rhs) {
		return lhs == null ? rhs == null : rhs != null && lhs.getTime() == rhs.getTime();
	}
}
