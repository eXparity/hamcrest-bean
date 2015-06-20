/**
 * 
 */
package org.exparity.hamcrest.beans.comparators;

import java.util.Date;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of {@link PropertyComparator} which compares two dates to see
 * if the date and time are the same excluding milliseconds
 * 
 * @author Stewart Bissett
 */
public class IsEqualDateTime implements PropertyComparator<Date> {

	@Override
	public boolean matches(Date lhs, Date rhs) {
		return lhs == null ? rhs == null : rhs != null && lhs.getTime() / 1000 == rhs.getTime() / 1000;
	}
}
