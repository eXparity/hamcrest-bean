
package org.exparity.hamcrest.beans;

import org.exparity.beans.BeanProperty;
import org.exparity.beans.Graph;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author Stewart Bissett
 */
public class HasPath<T> extends TypeSafeDiagnosingMatcher<T> {

	/**
	 * Creates a matcher that matches if the object being tested has a property at the given path and it matches the Matcher
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * MyObject instance = new MyObject();
	 * assertThat(instance, hasPath("MyObject.Person.Name", Matchers.equalTo("Jane"))
	 * </pre>
	 * 
	 * @param path the path to match
	 */
	@Factory
	public static <T> Matcher<T> hasPath(final String path, final Matcher<T> matcher) {
		return new HasPath<T>(path, matcher);
	}

	private final String path;
	private final Matcher<T> matcher;

	public HasPath(final String path, final Matcher<T> matcher) {
		this.path = path;
		this.matcher = matcher;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("has path '").appendText(path).appendText("' which matches ").appendDescriptionOf(matcher);
	}

	@Override
	protected boolean matchesSafely(final T item, final Description mismatchDescription) {
		BeanProperty property = Graph.graph(item).propertyNamed(path);
		if (property != null) {
			if (matcher.matches(property.getValue())) {
				return true;
			} else {
				mismatchDescription.appendText("has path '").appendText(path).appendText("' which matches ");
				matcher.describeTo(mismatchDescription);
				return false;
			}
		} else {
			mismatchDescription.appendText("does not have path '").appendText(path).appendText("' which matches ");
			return false;
		}
	}

}
