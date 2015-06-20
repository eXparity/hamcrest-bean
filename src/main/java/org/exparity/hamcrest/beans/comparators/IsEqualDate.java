/**
 * 
 */
package org.exparity.hamcrest.beans.comparators;

import java.util.Calendar;
import java.util.Date;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;

/**
 * Implementation of {@link PropertyComparator} which compares two dates to see
 * if the date and time are the same excluding milliseconds
 * 
 * @author Stewart Bissett
 */
public class IsEqualDate implements PropertyComparator<Date> {

	@Override
	public boolean matches(Date lhs, Date rhs) {
		return lhs == null ? rhs == null : rhs != null && toDate(lhs).equals(toDate(rhs));
	}

	private Date toDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return calendar.getTime();
	}
}
