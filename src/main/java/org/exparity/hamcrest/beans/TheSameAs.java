
package org.exparity.hamcrest.beans;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.exparity.beans.Type;
import org.exparity.beans.core.ImmutableTypeProperty;
import org.exparity.beans.core.naming.CapitalizedNamingStrategy;
import org.exparity.hamcrest.beans.comparators.Excluded;
import org.exparity.hamcrest.beans.comparators.HamcrestComparator;
import org.exparity.hamcrest.beans.comparators.IsComparable;
import org.exparity.hamcrest.beans.comparators.IsEquals;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.exparity.beans.Type.type;

/**
 * Implementation of a {@link Matcher} for performing a deep comparison of two objects by testing getters which start with <em>get</em>, <em>is</em>, or <em>has</em> are the same
 * on each instances.
 * <p>
 * When comparing elements in a collection or an array the {@link Matcher} orders a copy of the collection. If there is a default comparator then one will be used, or alternatively
 * one will built using {@link org.apache.commons.lang.builder.CompareToBuilder#reflectionCompare(Object, Object)}
 * </p>
 * 
 * @author Stewart Bissett
 */
public class TheSameAs<T> extends TypeSafeDiagnosingMatcher<T> {

	/**
	 * Creates a matcher that matches the full object graph for the given instance against another instance
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * MyObject instance = new MyObject();
	 * dao.save(instance); // Save instance to persistent store
	 * assertThat(dao.getById(instance.getId()), theSameAs(instance);
	 * </pre>
	 * 
	 * @param object the instance to match against
	 */
	@Factory
	public static <T> TheSameAs<T> theSameAs(final T object) {
		return new TheSameAs<T>(object);
	}

	/**
	 * Creates a matcher that matches the full object graph for the given instance against another instance
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * MyObject instance = new MyObject();
	 * dao.save(instance); // Save instance to persistent store
	 * assertThat(dao.getById(instance.getId()), theSameAs(instance, "MyInstance");
	 * </pre>
	 * 
	 * @param object the instance to match against
	 * @param name the name given to the root entity
	 */
	@Factory
	public static <T> TheSameAs<T> theSameAs(final T object, final String name) {
		return new TheSameAs<T>(object, name);
	}

	private static final Logger LOG = LoggerFactory.getLogger(TheSameAs.class);

	/**
	 * Interface to be implemented by classes which can compare two property values to confirm if they're equivalent
	 */
	public interface PropertyComparator<T> {

		/**
		 * Return <code>true</code> if the actual value matches the expected value
		 */
		public boolean matches(final T lhs, final T rhs);
	}

	@SuppressWarnings("rawtypes")
	private static Comparator DEFAULT_COMPARATOR = new Comparator() {

		public int compare(final Object o1, final Object o2) {
			return CompareToBuilder.reflectionCompare(o1, o2);
		}
	};

	private final Map<String, PropertyComparator<?>> paths = new HashMap<>();
	private final Map<String, PropertyComparator<?>> properties = new HashMap<>();
	private final Map<Class<?>, PropertyComparator<?>> types = new HashMap<>();

	private final T object;
	private final String name;

	public TheSameAs(final T object) {
		this(object, object.getClass().getSimpleName());
	}

	public TheSameAs(final T object, final String name) {
		this.types.put(BigDecimal.class, new IsComparable<BigDecimal>());
		this.types.put(String.class, new IsEquals<String>());
		this.types.put(Integer.class, new IsEquals<Integer>());
		this.types.put(Long.class, new IsEquals<Long>());
		this.types.put(Double.class, new IsEquals<Double>());
		this.types.put(Float.class, new IsEquals<Float>());
		this.types.put(Character.class, new IsEquals<Character>());
		this.types.put(Date.class, new IsComparable<Date>());
		this.types.put(Class.class, new Excluded<Class<?>>());
		this.object = object;
		this.name = name;
	}

	/**
	 * Exclude a property path from the comparison. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("Jane", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected).excludePath("Person.LastName"));
	 * </pre>
	 * 
	 * @param property the path to exclude from the comparison e.g Person.LastName
	 * @return the current matcher
	 */
	public TheSameAs<T> excludePath(final String path) {
		this.paths.put(path.toLowerCase(), new Excluded<Object>());
		return this;
	}

	/**
	 * Exclude a property from the comparison. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("Jane", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected).excludeProperty("LastName"));
	 * </pre>
	 * 
	 * @param property the property to exclude from the comparison e.g LastName
	 * @return the current matcher
	 */
	public TheSameAs<T> excludeProperty(final String property) {
		this.properties.put(property.toLowerCase(), new Excluded<Object>());
		return this;
	}

	/**
	 * Exclude a type from the comparison. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("Jane", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected).excludeType(String.class));
	 * </pre>
	 * 
	 * @param type the type to exclude from the comparison e.g String.class
	 * @return the current matcher
	 */
	public TheSameAs<T> excludeType(final Class<?> type) {
		this.types.put(type, new Excluded<Object>());
		return this;
	}

	/**
	 * Override the PropertyComparator used for a path. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("john", "doe"), BeanMatchers.theSameAs(expected).comparePath("Person.LastName", new IsEqualsIgnoreCase());
	 * </pre>
	 * 
	 * @param property the property to exclude from the comparison e.g LastName
	 * @return the current matcher
	 */
	public TheSameAs<T> comparePath(final String path, final PropertyComparator<?> comparator) {
		this.paths.put(path.toLowerCase(), comparator);
		return this;
	}

	/**
	 * Override the PropertyComparator used for a property. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("john", "doe"), BeanMatchers.theSameAs(expected).comparePath("Person.LastName", new IsEqualsIgnoreCase());
	 * </pre>
	 * 
	 * @param path the path to set the comparator for
	 * @param comparator the comparator to use
	 * @return the current matcher
	 */
	public TheSameAs<T> compareProperty(final String path, final PropertyComparator<?> comparator) {
		this.properties.put(path.toLowerCase(), comparator);
		return this;
	}

	/**
	 * Override the PropertyComparator used for a type. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("john", "doe"), BeanMatchers.theSameAs(expected).compareType(String.class, new IsEqualsIgnoreCase());
	 * </pre>
	 * 
	 * @param type the type to set the comparator for
	 * @param comparator the comparator to use
	 * @return the current matcher
	 */
	public <P> TheSameAs<T> compareType(final Class<P> type, final PropertyComparator<P> comparator) {
		this.types.put(type, comparator);
		return this;
	}

	/**
	 * Override the PropertyComparator used for a path to use a hamcrest Matcher. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Deer"), BeanMatchers.theSameAs(expected).comparePath("Person.LastName", Matchers.startsWith("D")));
	 * </pre>
	 * 
	 * @param property the property to exclude from the comparison e.g LastName
	 * @return the current matcher
	 */
	public <P> TheSameAs<T> comparePath(final String path, final Matcher<P> matcher) {
		this.paths.put(path.toLowerCase(), new HamcrestComparator<P>(matcher));
		return this;
	}

	/**
	 * Override the PropertyComparator used for a property to use a hamcrest matcher. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Deer"), BeanMatchers.theSameAs(expected).comparePath("Person.LastName", Matchers.startsWith("D")));
	 * </pre>
	 * 
	 * @param path the path to set the comparator for
	 * @param matcher the matcher to use
	 * @return the current matcher
	 */
	public <P> TheSameAs<T> compareProperty(final String path, final Matcher<P> matcher) {
		this.properties.put(path.toLowerCase(), new HamcrestComparator<P>(matcher));
		return this;
	}

	/**
	 * Override the PropertyComparator used for a type to use a hamcrest Matcher. For example</p>
	 * 
	 * <pre>
	 * class Person [
	 *   private String firstName, lastName;
	 *   public Person(final String firstName, final String lastName) {
	 *     this.firstname = firstName;
	 *     this.lastName = lastName;
	 *    }
	 *    public String getFirstName() { return firstName;};
	 *    public String getLastName() { return lastName;};
	 * }
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected).compareType(String.class, Matchers.startsWith("J")));
	 * </pre>
	 * 
	 * @param type the type to set the comparator for
	 * @param matcher the matcher to use
	 * @return the current matcher
	 */
	public <P> TheSameAs<T> compareType(final Class<P> type, final Matcher<P> matcher) {
		this.types.put(type, new HamcrestComparator<P>(matcher));
		return this;
	}

	@Override
	protected boolean matchesSafely(final T item, final Description mismatchDesc) {
		MismatchContext context = new MismatchContext(mismatchDesc);
		compareObjects(object, item, name, context);
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

		String pathNoIndexes = path.replaceAll("\\[\\w*\\]\\.", ".").toLowerCase();
		String propertyName = StringUtils.contains(path, ".") ? substringAfterLast(pathNoIndexes, ".") : pathNoIndexes;

		if (expected != null && actual != null) {
			if (ctx.hasComparedPair(expected, actual)) {
				LOG.trace("Already compared [{}] vs [{}]", expected, actual);
				return;
			} else {
				ctx.addComparedPair(expected, actual);
			}
		} else if (expected == null && actual == null) {
			return;
		}

		LOG.trace("Check override for path [{}]", pathNoIndexes);
		PropertyComparator pathComparator = paths.get(pathNoIndexes);
		if (pathComparator != null) {
			compareUsingPropertyComparator(expected, actual, path, pathComparator, ctx);
			return;
		}

		LOG.trace("Check override for property [{}]", propertyName);
		PropertyComparator propertyComparator = getPropertyComparator(propertyName);
		if (propertyComparator != null) {
			compareUsingPropertyComparator(expected, actual, path, propertyComparator, ctx);
			return;
		}

		final Class<? extends Object> klass = expected != null ? expected.getClass() : actual.getClass();
		LOG.trace("Check override for type [{}]", klass);
		for (Entry<Class<?>, PropertyComparator<?>> entry : types.entrySet()) {
			if (entry.getKey().isAssignableFrom(klass)) {
				compareUsingPropertyComparator(expected, actual, path, entry.getValue(), ctx);
				return;
			}
		}

		final Type type = type(klass, new CapitalizedNamingStrategy());
		if (type.isArray()) {
			compareArrays(expected, actual, path, ctx);
		} else if (type.packageName().startsWith("java.lang")) {
			compareLangTypes(expected, actual, path, ctx);
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

	private PropertyComparator<?> getPropertyComparator(final String propertyName) {
		return properties.get(propertyName);
	}

	private void compareArrays(final Object expected, final Object actual, final String path, final MismatchContext ctx) {
		LOG.debug("Compare path [{}] as array", path);
		try {
			int expectedLength = Array.getLength(expected), actualLength = Array.getLength(actual);
			if (expectedLength != actualLength) {
				ctx.addMismatch(expectedLength, actualLength, path + getDotIfRequired(path) + "size");
			} else {
				for (int i = 0; i < expectedLength; ++i) {
					if (!Array.get(expected, i).equals(Array.get(actual, i))) {
						ctx.addMismatch(expected, actual, path + getDotIfRequired(path));
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error comparing path '" + path + "'. Error '" + e.getMessage() + "'", e);
		}
	}

	private void compareLangTypes(final Object expected, final Object actual, final String path, final MismatchContext ctx) {
		LOG.debug("Compare path [{}] as lang type", path);
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
		LOG.debug("Compare path [{}] as map", path);
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

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	private void compareLists(final List expected, final List actual, final String path, final MismatchContext ctx) {
		LOG.debug("Compare path [{}] as list", path);
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

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	private void compareUsingPropertyComparator(final Object lhs, final Object rhs, final String path, final PropertyComparator comparator, final MismatchContext ctx) {
		LOG.debug("Compare path [{}] using [{}]", path, comparator.getClass().getSimpleName());
		try {
			if (!comparator.matches(lhs, rhs)) {
				ctx.addMismatch(lhs, rhs, path);
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