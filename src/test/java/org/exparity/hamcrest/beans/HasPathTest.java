
package org.exparity.hamcrest.beans;

import static org.exparity.hamcrest.BeanMatchers.hasPath;
import static org.exparity.stub.random.RandomBuilder.aRandomInstanceOf;
import static org.exparity.stub.random.RandomBuilder.aRandomString;
import static org.exparity.stub.random.RandomBuilder.path;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.exparity.hamcrest.beans.testutils.types.ObjectWithAllTypes;
import org.testng.annotations.Test;

/**
 * Unit test for HasPath
 * 
 * @author Stewart Bissett
 */
public class HasPathTest {

	@Test
	public void canTestPathWithValue() {
		String expectedValue = aRandomString();
		ObjectWithAllTypes tree = aRandomInstanceOf(ObjectWithAllTypes.class, path("ObjectWithAllTypes.StringValue", expectedValue));
		assertThat(tree, hasPath("ObjectWithAllTypes.StringValue", equalTo(expectedValue)));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canTestPathWithWrongValue() {
		String expectedValue = aRandomString(), wrongValue = expectedValue + expectedValue;
		ObjectWithAllTypes tree = aRandomInstanceOf(ObjectWithAllTypes.class, path("ObjectWithAllTypes.StringValue", expectedValue));
		assertThat(tree, hasPath("ObjectWithAllTypes.StringValue", equalTo(wrongValue)));
	}

}
