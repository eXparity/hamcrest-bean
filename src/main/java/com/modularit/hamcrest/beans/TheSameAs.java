
package com.modularit.hamcrest.beans;

import static java.lang.reflect.Modifier.isStatic;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Implementation of a {@link Matcher} for performing a deep comparison of two objects by testing getters which start with <em>get</em>, <em>is</em>, or <em>has</em> are the same
 * on each instances.
 * <p>
 * When comparing elements in a collection or an array the {@link Matcher} orders a copy of the collection. If there is a default comparator then one will be used, or alternatively
 * one will built using {@link org.apache.commons.lang.builder.CompareToBuilder#reflectionCompare(Object, Object)}
 * </p>
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class TheSameAs<T> extends TypeSafeDiagnosingMatcher<T> {

	@SuppressWarnings("rawtypes")
	private static Comparator DEFAULT_COMPARATOR = new Comparator() {

		public int compare(final Object o1, final Object o2) {
			return CompareToBuilder.reflectionCompare(o1, o2);
		}
	};

	private static final String[] PROPERTY_PREFIXES = {
			"get", "is", "has"
	};

	private final T object;

	public TheSameAs(final T object) {
		this.object = object;
	}

	@Override
	protected boolean matchesSafely(final T item, final Description mismatchDesc) {
		MismatchContext context = new MismatchContext(mismatchDesc);
		compareObjects(object, item, "", context);
		return context.areSame();
	}

	public void describeTo(final Description description) {
		description.appendText("the same as ").appendValue(object);
	}

	@SuppressWarnings("rawtypes")
	private void compareObjects(final Object expected, final Object actual, final String prefix, final MismatchContext ctx) {

		if (expected == null) {
			Matcher<Object> m = nullValue();
			if (!m.matches(actual)) {
				ctx.addMismatch(expected, actual, prefix);
			}
			return;
		} else {
			Matcher<Object> m = notNullValue();
			if (!m.matches(actual)) {
				ctx.addMismatch(expected, actual, prefix);
				return;
			}
		}

		if (ctx.hasComparedPair(expected, actual)) {
			return;
		} else {
			ctx.addComparedPair(expected, actual);
		}

		final Class<? extends Object> klass = expected.getClass();
		if (klass.isArray()) {
			compareArrays((Object[]) expected, (Object[]) actual, prefix, ctx);
		} else if (klass.isAssignableFrom(String.class)) {
			compareStrings((String) expected, (String) actual, prefix, ctx);
		} else if (klass.getPackage().getName().startsWith("java.lang")) {
			compareLangTypes(expected, actual, prefix, prefix, ctx);
		} else if (Date.class.isAssignableFrom(klass)) {
			compareDates((Date) expected, (Date) actual, prefix, ctx);
		} else if (BigDecimal.class.isAssignableFrom(klass)) {
			compareBigDecimals((BigDecimal) expected, (BigDecimal) actual, prefix, ctx);
		} else if (List.class.isAssignableFrom(klass)) {
			compareLists((List) expected, (List) actual, prefix, ctx);
		} else if (Collection.class.isAssignableFrom(klass)) {
			compareCollections((Collection) expected, (Collection) actual, prefix, ctx);
		} else if (Map.class.isAssignableFrom(klass)) {
			compareMaps((Map) expected, (Map) actual, prefix, ctx);
		} else {
			for (PropertyMethod method : getPropertyMethodsFrom(klass)) {
				compareObjects(method.getPropertyValue(expected), method.getPropertyValue(actual), prefix + getDotIfRequired(prefix) + method.getPropertyName(), ctx);
			}
		}
	}

	private void compareArrays(final Object[] expected, final Object[] actual, final String path, final MismatchContext ctx) {
		if (expected.length != actual.length) {
			ctx.addMismatch(expected.length, actual.length, path + getDotIfRequired(path) + "size");
		} else {
			compareLists(Arrays.asList(expected), Arrays.asList(actual), path, ctx);
		}
	}

	public List<PropertyMethod> getPropertyMethodsFrom(final Class<? extends Object> klass) {
		List<PropertyMethod> properties = new ArrayList<PropertyMethod>();
		for (Method method : klass.getMethods()) {
			if (isPotentialProperty(method)) {
				String methodName = method.getName();
				for (String prefix : PROPERTY_PREFIXES) {
					if (methodName.startsWith(prefix)) {
						String propertyName = StringUtils.substringAfter(methodName, prefix);
						if (Character.isUpperCase(propertyName.charAt(0))) {
							properties.add(new PropertyMethod(method, propertyName));
						}
						break;
					}
				}
			}
		}
		return properties;
	}

	private boolean isPotentialProperty(final Method method) {
		return method.getParameterTypes().length == 0 && !isStatic(method.getModifiers());
	}

	private void compareLangTypes(final Object expected, final Object actual, final String prefix, final String path, final MismatchContext ctx) {
		Matcher<Object> m = equalTo(expected);
		if (!m.matches(actual)) {
			ctx.addMismatch(expected, actual, path);
		}
	}

	@SuppressWarnings("rawtypes")
	private void compareMaps(final Map expected, final Map actual, final String path, final MismatchContext ctx) {
		if (expected.size() != actual.size()) {
			ctx.addMismatch(expected.size(), actual.size(), path + getDotIfRequired(path) + "size");
		} else {
			for (Object key : expected.keySet()) {
				Object expectedValue = expected.get(key), actualValue = actual.get(key);
				if (actualValue == null) {
					ctx.addMismatch(expectedValue, null, path + "[" + key + "]");
				} else {
					compareObjects(expectedValue, actualValue, path + "[" + key + "]", ctx);
				}
			}
		}
	}

	private String getDotIfRequired(final String path) {
		return StringUtils.isNotBlank(path) ? "." : "";
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	private void compareCollections(final Collection expected, final Collection actual, final String path, final MismatchContext ctx) {
		compareLists(new ArrayList(expected), new ArrayList(actual), path, ctx);
	}

	private void compareBigDecimals(final BigDecimal expected, final BigDecimal actual, final String path, final MismatchContext ctx) {
		Matcher<?> m = comparesEqualTo(expected);
		if (!m.matches(actual)) {
			ctx.addMismatch(expected, actual, path);
		}
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	private void compareLists(final List expected, final List actual, final String path, final MismatchContext ctx) {
		if (expected.size() != actual.size()) {
			ctx.addMismatch(expected.size(), actual.size(), path + getDotIfRequired(path) + "size");
		} else {
			List expectedList = new ArrayList(expected), actualList = new ArrayList(actual);
			Collections.sort(expectedList, DEFAULT_COMPARATOR);
			Collections.sort(actualList, DEFAULT_COMPARATOR);
			int ctr = 0;
			for (Iterator i = expectedList.iterator(), j = actualList.iterator(); i.hasNext();) {
				compareObjects(i.next(), j.next(), path + "[" + (ctr++) + "]", ctx);
			}
		}
	}

	private void compareDates(final Date expected, final Date actual, final String path, final MismatchContext ctx) {
		if (expected.getTime() != actual.getTime()) {
			ctx.addMismatch(expected, actual, path);
		}
	}

	private void compareStrings(final String expected, final String actual, final String path, final MismatchContext ctx) {
		Matcher<String> m = Matchers.equalTo(expected);
		if (!m.matches(actual)) {
			ctx.addMismatch(expected, actual, path);
		}
	}

	/**
	 * Encapsulation of a java {@link Method} object
	 */
	private static class PropertyMethod {

		private final Method method;
		private final String propertyName;

		public PropertyMethod(final Method method, final String propertyName) {
			this.method = method;
			this.propertyName = StringUtils.uncapitalize(propertyName);
		}

		public Object getPropertyValue(final Object obj) {
			try {
				return method.invoke(obj);
			} catch (Exception e) {
				throw new RuntimeException("Error matching " + method + " on " + obj.getClass(), e);
			}
		}

		public String getPropertyName() {
			return propertyName;
		}
	}

	private static class Pair {

		private final Object lhs, rhs;

		public Pair(final Object lhs, final Object rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Pair)) {
				return false;
			}
			Pair rhs = (Pair) o;
			return new EqualsBuilder().append(this.lhs, rhs.lhs).append(this.rhs, rhs.rhs).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(35, 67).append(lhs).append(rhs).toHashCode();
		}
	}

	private static class MismatchContext {

		private final Set<Pair> compared = new HashSet<Pair>();
		private final Description desc;
		private boolean same = true;

		public MismatchContext(final Description desc) {
			this.desc = desc;
		}

		public boolean areSame() {
			return same;
		}

		public void addComparedPair(final Object lhs, final Object rhs) {
			compared.add(new Pair(lhs, rhs));
		}

		public void addMismatch(final Object expected, final Object actual, final String path) {
			if (!isFirstMismatch()) {
				desc.appendText(", ");
			}
			desc.appendText(path).appendText(" is ").appendValue(actual).appendText(" instead of ").appendValue(expected);
			same = false;
		}

		private boolean isFirstMismatch() {
			return same;
		}

		public boolean hasComparedPair(final Object lhs, final Object rhs) {
			return compared.contains(new Pair(lhs, rhs));
		}
	}

}