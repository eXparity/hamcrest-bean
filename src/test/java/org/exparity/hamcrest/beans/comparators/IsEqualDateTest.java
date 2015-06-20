/**
 * 
 */
package org.exparity.hamcrest.beans.comparators;

import static org.exparity.dates.en.FluentDateTime.JUN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;

/**
 * Unit test for {@link IsEqualDate}
 * 
 * @author Stewart Bissett
 */
public class IsEqualDateTest {

	@Test
	public void canCompareSameDate() {
		assertThat(new IsEqualDate().matches(JUN(19, 2015).at(1, 0, 0), JUN(19, 2015).at(1, 0, 0)), equalTo(true));
	}

	@Test
	public void canCompareSameDateDifferentTime() {
		assertThat(new IsEqualDate().matches(JUN(19, 2015).at(1, 0, 0), JUN(19, 2015).at(2, 0, 0)), equalTo(true));
	}

	@Test
	public void canCompareDifferentDate() {
		assertThat(new IsEqualDate().matches(JUN(20, 2015).at(1, 0, 0), JUN(19, 2015).at(1, 0, 0)), equalTo(false));
	}

}
