
package org.exparity.hamcrest.beans;

import org.exparity.hamcrest.beans.testutils.types.Tree;
import org.junit.Test;
import static org.exparity.hamcrest.BeanMatchers.hasPath;
import static org.exparity.stub.random.RandomBuilder.aRandomInstanceOf;
import static org.exparity.stub.random.RandomBuilder.aRandomString;
import static org.exparity.stub.random.RandomBuilder.path;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit test for HasPath
 * 
 * @author Stewart Bissett
 */
public class HasPathTest {

	@Test
	public void canTestPathWithValue() {
		String expectedValue = aRandomString();
		Tree tree = aRandomInstanceOf(Tree.class, path("Tree.Name", expectedValue));
		assertThat(tree, hasPath("Tree.Name", equalTo(expectedValue)));
	}

	@Test(expected = AssertionError.class)
	public void canTestPathWithWrongValue() {
		String expectedValue = aRandomString(), wrongValue = expectedValue + expectedValue;
		Tree tree = aRandomInstanceOf(Tree.class, path("Tree.Name", expectedValue));
		assertThat(tree, hasPath("Tree.Name", equalTo(wrongValue)));
	}

}
