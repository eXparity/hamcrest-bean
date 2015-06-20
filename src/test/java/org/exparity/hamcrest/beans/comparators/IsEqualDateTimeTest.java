/**
 * 
 */
package org.exparity.hamcrest.beans.comparators;

import static org.exparity.stub.random.RandomBuilder.aRandomDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;

import org.testng.annotations.Test;

/**
 * @author Stewart Bissett
 */
public class IsEqualDateTimeTest {

	@Test
	public void canCompareDateTime() {
		Date date = aRandomDate(), other = new Date(date.getTime());
		assertThat(new IsEqualDateTime().matches(date, other), equalTo(true));
	}

	@Test
	public void canCompareDateTimeDifferentMillisecond() {
		Date date = new Date(1434251682001L), other = new Date(1434251682999L);
		assertThat(new IsEqualDateTime().matches(date, other), equalTo(true));
	}

	@Test
	public void canCompareDateTimeDifferentSecond() {
		Date date = aRandomDate(), other = new Date(date.getTime() + 1000);
		assertThat(new IsEqualDateTime().matches(date, other), equalTo(false));
	}

}
