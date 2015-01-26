
package org.exparity.hamcrest.beans;

import org.exparity.beans.Graph;
import org.exparity.beans.core.BeanProperty;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author Stewart Bissett
 */
public class HasProperty<T> extends TypeSafeDiagnosingMatcher<T> {

	/**
	 * Creates a matcher that matches if the object being tested has a property anywhere in it's graph and it matches the Matcher
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * MyObject instance = new MyObject();
	 * assertThat(instance, hasProperty("Name", Matchers.equalTo("Jane"))
	 * </pre>
	 * 
	 * @param property the property to match
	 * @param matcher the matcher to match with
	 */
	@Factory
	public static <T> Matcher<T> hasProperty(final String property, final Matcher<?> matcher) {
		return new HasProperty<T>(property, matcher);
	}

	private final String property;
	private final Matcher<?> matcher;

	public HasProperty(final String property, final Matcher<?> matcher) {
		this.property = property;
		this.matcher = matcher;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("has property '").appendText(property).appendText("' which matches ").appendDescriptionOf(matcher);
	}

	@Override
	protected boolean matchesSafely(final T item, final Description mismatchDescription) {
		BeanProperty beanProperty = Graph.graph(item).propertyNamed(property);
		if (beanProperty != null) {
			if (matcher.matches(beanProperty.getValue())) {
				return true;
			} else {
				mismatchDescription.appendText("has property '").appendText(property).appendText("' which matches ");
				matcher.describeTo(mismatchDescription);
				return false;
			}
		} else {
			mismatchDescription.appendText("does not have property '").appendText(property).appendText("' which matches ");
			return false;
		}
	}

}
