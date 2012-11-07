
package com.modularit.hamcrest.beans;

import static java.lang.Character.isUpperCase;
import static java.lang.reflect.Modifier.isStatic;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.substringAfterLast;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hamcrest.Description;
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

	/**
	 * Interface to be implemented by classes which can compare two property values to confirm if they're equivalent
	 */
	public interface PropertyComparator {

		/**
		 * Return <code>true</code> if both objects are equal
		 */
		public boolean isEquals(final Object lhs, final Object rhs);
	}

	@SuppressWarnings("rawtypes")
	private static Comparator DEFAULT_COMPARATOR = new Comparator() {

		public int compare(final Object o1, final Object o2) {
			return CompareToBuilder.reflectionCompare(o1, o2);
		}
	};

	private static final String[] PROPERTY_PREFIXES = {
			"get", "is", "has"
	};

	private final Map<String, PropertyComparator> paths = new HashMap<String, PropertyComparator>();
	private final Map<String, PropertyComparator> properties = new HashMap<String, PropertyComparator>();
	private final Map<Class<?>, PropertyComparator> types = new HashMap<Class<?>, PropertyComparator>();

	private final T object;

	public TheSameAs(final T object) {
		this.object = object;
	}

	public TheSameAs<T> withPathComparator(final String path, final PropertyComparator comparator) {
		this.paths.put(path, comparator);
		return this;
	}

	public TheSameAs<T> withPropertyComparator(final String path, final PropertyComparator comparator) {
		this.properties.put(path, comparator);
		return this;
	}

	public TheSameAs<T> withTypeComparator(final Class<?> type, final PropertyComparator comparator) {
		this.types.put(type, comparator);
		return this;
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
	private void compareObjects(final Object expected, final Object actual, final String path, final MismatchContext ctx) {

		if (expected == null) {
			if (actual != null) {
				ctx.addMismatch(expected, actual, path);
			}
			return;
		} else {
			if (actual == null) {
				ctx.addMismatch(expected, actual, path);
				return;
			}
		}

		if (ctx.hasComparedPair(expected, actual)) {
			return;
		} else {
			ctx.addComparedPair(expected, actual);
		}

		String pathNoIndexes = path.replaceAll("\\[\\w*\\]\\.", ".");

		for (Entry<String, PropertyComparator> entry : paths.entrySet()) {
			if (StringUtils.equalsIgnoreCase(entry.getKey(), pathNoIndexes)) {
				compareUsingPropertyCompartor(expected, actual, path, entry.getValue(), ctx);
				return;
			}
		}

		for (Entry<String, PropertyComparator> entry : properties.entrySet()) {
			if (equalsIgnoreCase(entry.getKey(), path) || equalsIgnoreCase(substringAfterLast(path, "."), entry.getKey())) {
				compareUsingPropertyCompartor(expected, actual, path, entry.getValue(), ctx);
				return;
			}
		}

		final Class<? extends Object> klass = expected.getClass();

		for (Entry<Class<?>, PropertyComparator> entry : types.entrySet()) {
			if (entry.getKey().equals(klass)) {
				compareUsingPropertyCompartor(expected, actual, path, entry.getValue(), ctx);
				return;
			}
		}

		if (klass.isArray()) {
			compareArrays((Object[]) expected, (Object[]) actual, path, ctx);
		} else if (klass.isAssignableFrom(String.class)) {
			compareStrings((String) expected, (String) actual, path, ctx);
		} else if (klass.getPackage().getName().startsWith("java.lang")) {
			compareLangTypes(expected, actual, path, ctx);
		} else if (Date.class.isAssignableFrom(klass)) {
			compareDates((Date) expected, (Date) actual, path, ctx);
		} else if (BigDecimal.class.isAssignableFrom(klass)) {
			compareBigDecimals((BigDecimal) expected, (BigDecimal) actual, path, ctx);
		} else if (List.class.isAssignableFrom(klass)) {
			compareLists((List) expected, (List) actual, path, ctx);
		} else if (Collection.class.isAssignableFrom(klass)) {
			compareCollections((Collection) expected, (Collection) actual, path, ctx);
		} else if (Map.class.isAssignableFrom(klass)) {
			compareMaps((Map) expected, (Map) actual, path, ctx);
		} else {
			for (PropertyMethod method : getPropertyMethodsFrom(klass)) {
				compareObjects(method.getPropertyValue(expected), method.getPropertyValue(actual), path + getDotIfRequired(path) + method.getPropertyName(), ctx);
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

	private List<PropertyMethod> getPropertyMethodsFrom(final Class<? extends Object> klass) {
		List<PropertyMethod> properties = new ArrayList<PropertyMethod>();
		for (Method method : klass.getMethods()) {
			if (isPotentialProperty(method)) {
				String methodName = method.getName();
				for (String prefix : PROPERTY_PREFIXES) {
					if (methodName.startsWith(prefix)) {
						String propertyName = StringUtils.substringAfter(methodName, prefix);
						if (hasSetter(klass, method, propertyName) && isUpperCase(propertyName.charAt(0))) {
							properties.add(new PropertyMethod(method, propertyName));
						}
						break;
					}
				}
			}
		}
		return properties;
	}

	private boolean hasSetter(final Class<? extends Object> klass, final Method method, final String propertyName) {
		try {
			return method.getDeclaringClass().getMethod("set" + StringUtils.capitalize(propertyName), method.getReturnType()) != null;
		} catch (SecurityException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	private boolean isPotentialProperty(final Method method) {
		return method.getParameterTypes().length == 0 && !isStatic(method.getModifiers());
	}

	private void compareLangTypes(final Object expected, final Object actual, final String path, final MismatchContext ctx) {
		if (!expected.equals(actual)) {
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
		if (expected.compareTo(actual) != 0) {
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
		if (!StringUtils.equals(expected, actual)) {
			ctx.addMismatch(expected, actual, path);
		}
	}

	private void compareUsingPropertyCompartor(final Object expected, final Object actual, final String path, final PropertyComparator comparator, final MismatchContext ctx) {
		if (!comparator.isEquals(expected, actual)) {
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

		private final int lhs, rhs;

		public Pair(final Object lhs, final Object rhs) {
			this.lhs = System.identityHashCode(lhs);
			this.rhs = System.identityHashCode(rhs);
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
				desc.appendText(SystemUtils.LINE_SEPARATOR);
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