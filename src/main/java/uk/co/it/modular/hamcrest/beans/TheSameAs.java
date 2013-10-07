
package uk.co.it.modular.hamcrest.beans;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.it.modular.beans.ImmutableTypeProperty;
import uk.co.it.modular.beans.Type;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static uk.co.it.modular.beans.Type.type;

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

	private static final Logger LOG = LoggerFactory.getLogger(TheSameAs.class);

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

	private final Map<String, PropertyComparator> paths = new HashMap<String, PropertyComparator>();
	private final Map<String, PropertyComparator> properties = new HashMap<String, PropertyComparator>();
	private final Map<Class<?>, PropertyComparator> types = new HashMap<Class<?>, PropertyComparator>();
	private final Set<String> excludedPaths = new HashSet<String>();
	private final Set<String> excludedProperties = new HashSet<String>();

	private final T object;

	public TheSameAs(final T object) {
		this.object = object;
	}

	public TheSameAs<T> excludePath(final String path) {
		this.excludedPaths.add(path);
		return this;
	}

	public TheSameAs<T> excludeProperty(final String property) {
		this.excludedProperties.add(property);
		return this;
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

		LOG.trace("Compare [{}] vs [{}] at [{}]", new Object[] {
				expected, actual, path
		});

		String pathNoIndexes = path.replaceAll("\\[\\w*\\]\\.", ".");
		if (excludedPaths.contains(pathNoIndexes)) {
			LOG.trace("Ignore path [{}]", pathNoIndexes);
			return;
		}

		String propertyName = StringUtils.contains(path, ".") ? substringAfterLast(pathNoIndexes, ".") : pathNoIndexes;
		if (excludedProperties.contains(propertyName)) {
			LOG.trace("Ignore property [{}]", propertyName);
			return;
		}

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
			LOG.trace("Already compared [{}] vs [{}]", expected, actual);
			return;
		} else {
			ctx.addComparedPair(expected, actual);
		}

		LOG.trace("Check override for path [{}]", pathNoIndexes);
		for (Entry<String, PropertyComparator> entry : paths.entrySet()) {
			if (StringUtils.equalsIgnoreCase(entry.getKey(), pathNoIndexes)) {
				compareUsingPropertyCompartor(expected, actual, path, entry.getValue(), ctx);
				return;
			}
		}

		LOG.trace("Check override for property [{}]", propertyName);
		PropertyComparator comparator = getPropertyComparator(propertyName);
		if (comparator != null) {
			compareUsingPropertyCompartor(expected, actual, path, comparator, ctx);
			return;
		}

		final Class<? extends Object> klass = expected.getClass();
		LOG.trace("Check override for type [{}]", klass);
		for (Entry<Class<?>, PropertyComparator> entry : types.entrySet()) {
			if (entry.getKey().equals(klass)) {
				compareUsingPropertyCompartor(expected, actual, path, entry.getValue(), ctx);
				return;
			}
		}

		final Type type = type(klass);
		if (type.isArray()) {
			compareArrays((Object[]) expected, (Object[]) actual, path, ctx);
		} else if (type.is(String.class)) {
			compareStrings((String) expected, (String) actual, path, ctx);
		} else if (type.packageName().startsWith("java.lang")) {
			compareLangTypes(expected, actual, path, ctx);
		} else if (type.is(Date.class)) {
			compareDates((Date) expected, (Date) actual, path, ctx);
		} else if (type.is(BigDecimal.class)) {
			compareBigDecimals((BigDecimal) expected, (BigDecimal) actual, path, ctx);
		} else if (type.is(List.class)) {
			compareLists((List) expected, (List) actual, path, ctx);
		} else if (type.is(Collection.class)) {
			compareCollections((Collection) expected, (Collection) actual, path, ctx);
		} else if (type.is(Map.class)) {
			compareMaps((Map) expected, (Map) actual, path, ctx);
		} else {
			for (ImmutableTypeProperty property : type.accessorList()) {
				compareObjects(property.getValue(expected), property.getValue(actual), path + getDotIfRequired(path) + property.getName(), ctx);
			}
		}
	}

	private PropertyComparator getPropertyComparator(final String propertyName) {
		for (Entry<String, PropertyComparator> entry : properties.entrySet()) {
			if (equalsIgnoreCase(propertyName, entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	private void compareArrays(final Object[] expected, final Object[] actual, final String path, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] as array", path);
		try {
			if (expected.length != actual.length) {
				ctx.addMismatch(expected.length, actual.length, path + getDotIfRequired(path) + "size");
			} else {
				compareLists(Arrays.asList(expected), Arrays.asList(actual), path, ctx);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	private void compareLangTypes(final Object expected, final Object actual, final String path, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] as lang type", path);
		try {
			if (!expected.equals(actual)) {
				ctx.addMismatch(expected, actual, path);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void compareMaps(final Map expected, final Map actual, final String path, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] as map", path);
		try {
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
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
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
		LOG.trace("Compare path [{}] as decimal", path);
		try {
			if (expected.compareTo(actual) != 0) {
				ctx.addMismatch(expected, actual, path);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	private void compareLists(final List expected, final List actual, final String path, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] as list", path);
		try {
			if (expected.isEmpty() && actual.isEmpty()) {
				return;
			} else if (expected.size() != actual.size()) {
				ctx.addMismatch(expected.size(), actual.size(), path + getDotIfRequired(path) + "size");
			} else {
				List expectedList = new ArrayList(expected), actualList = new ArrayList(actual);
				if (expectedList.get(0) instanceof Comparable) {
					Collections.sort(expectedList);
					Collections.sort(actualList);
				} else {
					Collections.sort(expectedList, DEFAULT_COMPARATOR);
					Collections.sort(actualList, DEFAULT_COMPARATOR);
				}
				int ctr = 0;
				for (Iterator i = expectedList.iterator(), j = actualList.iterator(); i.hasNext();) {
					compareObjects(i.next(), j.next(), path + "[" + (ctr++) + "]", ctx);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	private void compareDates(final Date expected, final Date actual, final String path, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] as date", path);
		try {
			if (expected.getTime() != actual.getTime()) {
				ctx.addMismatch(expected, actual, path);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	private void compareStrings(final String expected, final String actual, final String path, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] as string", path);
		try {
			if (!StringUtils.equals(expected, actual)) {
				ctx.addMismatch(expected, actual, path);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	private void compareUsingPropertyCompartor(final Object expected, final Object actual, final String path, final PropertyComparator comparator, final MismatchContext ctx) {
		LOG.trace("Compare path [{}] with comparator [{}]", path, comparator.getClass().getSimpleName());
		try {
			if (!comparator.isEquals(expected, actual)) {
				ctx.addMismatch(expected, actual, path);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
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
			return same == true;
		}

		public boolean hasComparedPair(final Object lhs, final Object rhs) {
			return compared.contains(new Pair(lhs, rhs));
		}
	}
}