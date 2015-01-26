
package org.exparity.hamcrest.beans;

import org.exparity.hamcrest.beans.testutils.types.Tree;
import org.junit.Test;
import static org.exparity.hamcrest.BeanMatchers.hasProperty;
import static org.exparity.stub.random.RandomBuilder.aRandomInstanceOf;
import static org.exparity.stub.random.RandomBuilder.aRandomString;
import static org.exparity.stub.random.RandomBuilder.path;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit test for HasProperty
 * 
 * @author Stewart Bissett
 */
public class HasPropertyTest {

	@Test
	public void canTestPathWithValue() {
		String expectedValue = aRandomString();
		Tree tree = aRandomInstanceOf(Tree.class, path("Tree.Name", expectedValue));
		assertThat(tree, hasProperty("Name", equalTo(expectedValue)));
	}

	@Test(expected = AssertionError.class)
	public void canTestPathWithWrongValue() {
		String expectedValue = aRandomString(), wrongValue = expectedValue + expectedValue;
		Tree tree = aRandomInstanceOf(Tree.class, path("Tree.Name", expectedValue));
		assertThat(tree, hasProperty("Name", equalTo(wrongValue)));
	}

}
