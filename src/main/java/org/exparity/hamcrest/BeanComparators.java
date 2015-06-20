package org.exparity.hamcrest;

import java.util.Date;

import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;
import org.exparity.hamcrest.beans.comparators.Excluded;
import org.exparity.hamcrest.beans.comparators.Matches;
import org.exparity.hamcrest.beans.comparators.HasPattern;
import org.exparity.hamcrest.beans.comparators.IsComparable;
import org.exparity.hamcrest.beans.comparators.IsEqual;
import org.exparity.hamcrest.beans.comparators.IsEqualDate;
import org.exparity.hamcrest.beans.comparators.IsEqualDateTime;
import org.exparity.hamcrest.beans.comparators.IsEqualIgnoreCase;
import org.exparity.hamcrest.beans.comparators.IsEqualTimestamp;
import org.hamcrest.Matcher;

/**
 * Static repository of {@link PropertyComparator} for use with
 * {@link BeanMatchers}
 * 
 * @author Stewart Bissett
 */
public abstract class BeanComparators {

	/**
	 * Exclude the property, type, or path from comparison
	 */
	public static <T> PropertyComparator<T> exclude(final Class<T> type) {
		return new Excluded<T>();
	}

	/**
	 * Match the property, type, or path with a hamcrest matcher
	 */
	public static <T> PropertyComparator<T> matches(final Matcher<T> matcher) {
		return new Matches<T>(matcher);
	}

	/**
	 * Match the String property, type, or path against a regular expression
	 */
	public static PropertyComparator<String> hasPattern(final String pattern) {
		return new HasPattern(pattern);
	}

	/**
	 * Match the property, type, or path using their comparison method
	 */
	public static <T extends Comparable<T>> PropertyComparator<T> isComparable(final Class<T> type) {
		return new IsComparable<T>();
	}

	/**
	 * Match the property, type, or path using their equals method
	 */
	public static <T> PropertyComparator<T> isEqual(final Class<T> type) {
		return new IsEqual<T>();
	}

	/**
	 * Match the String property, type, or path using their equalsIgnoreCase
	 * method
	 */
	public static PropertyComparator<String> isEqualIgnoreCase() {
		return new IsEqualIgnoreCase();
	}

	/**
	 * Match the Date property, type, or path using by the date and time portion
	 * with no millisecond
	 */
	public static PropertyComparator<Date> isEqualDateTime() {
		return new IsEqualDateTime();
	}

	/**
	 * Match the Date property, type, or path using by the date and no time
	 * portion
	 */
	public static PropertyComparator<Date> isEqualDate() {
		return new IsEqualDate();
	}

	/**
	 * Match the Date property, type, or path by comparing as dates down to the millisecond
	 */
	public static PropertyComparator<Date> isEqualTimestamp() {
		return new IsEqualTimestamp();
	}

}
