package org.exparity.hamcrest;

import org.exparity.hamcrest.beans.*;
import org.exparity.hamcrest.beans.TheSameAs.PropertyType;
import org.hamcrest.*;

/**
 * Static factory for creating {@link Matcher} instances for testing properties
 * of objects whose properties follow the Java beans standards.
 * 
 * @author Stewart Bissett
 */
public abstract class BeanMatchers {

	/**
	 * Return an instance of a {@link Matcher} which will perform a deep
	 * comparison of the two objects by invoking all getter style properties.
	 * For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected))
	 * 
	 * // To test a simple object but ignore differences in a property
	 * Person expected = new Person("Jane", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected).excludeProperty("FirstName"));
	 * </pre>
	 * 
	 * @param object
	 *            the instance to match against
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of TheSameAs matcher
	 */
	public static <T> TheSameAs<T> theSameAs(final T object) {
		return TheSameAs.theSameAs(object);
	}

	/**
	 * Return an instance of a {@link Matcher} which will perform a deep
	 * comparison of the two objects by invoking all bean style properties where
	 * both a getter and setter exists. For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameBeanAs(expected))
	 * 
	 * // To test a simple object but ignore differences in a property
	 * Person expected = new Person("Jane", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameBeanAs(expected).excludeProperty("FirstName"));
	 * </pre>
	 * 
	 * @param object
	 *            the instance to match against
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of TheSameAs matcher
	 */
	public static <T> TheSameAs<T> theSameBeanAs(final T object) {
		return TheSameAs.theSameBeanAs(object);
	}

	/**
	 * Return an instance of a {@link Matcher} which will perform a deep
	 * comparison of the two objects. For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected))
	 * 
	 * // To test a simple object but ignore differences in a property
	 * Person expected = new Person("Jane", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected).excludeProperty("FirstName"));
	 * </pre>
	 * 
	 * @param object
	 *            the instance to match against
	 * @param propertyType
	 *            the type of properties to match
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of TheSameAs matcher
	 * @deprecated Use {@link #theSameAs(Object)} or
	 *             {@link #theSameBeanAs(Object)}
	 */
	public static <T> TheSameAs<T> theSameAs(final T object, final PropertyType propertyType) {
		return TheSameAs.theSameAs(object, propertyType);
	}

	/**
	 * Return an instance of a {@link Matcher} which will perform a deep
	 * comparison of the two objects by invoking all getter style properties.
	 * For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected, "Person"))
	 * 
	 * // To test a simple object but ignore differences in a property
	 * Person expected = new Person("Jane", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected,"Person").excludePath("Person.FirstName"));
	 * </pre>
	 * 
	 * @param object
	 *            the instance to match against
	 * @param name
	 *            the name to use for the base object for paths e.g Person would
	 *            prefix path i.e. Person.FirstName
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of TheSameAs matcher
	 */
	public static <T> TheSameAs<T> theSameAs(final T object, final String name) {
		return TheSameAs.theSameAs(object, name);
	}

	/**
	 * Return an instance of a {@link Matcher} which will perform a deep
	 * comparison of the two objects by invoking all bean style properties where
	 * both a getter and setter exists. For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameBeanAs(expected, "Person"))
	 * 
	 * // To test a simple object but ignore differences in a property
	 * Person expected = new Person("Jane", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameBeanAs(expected,"Person").excludePath("Person.FirstName"));
	 * </pre>
	 * 
	 * @param object
	 *            the instance to match against
	 * @param name
	 *            the name to use for the base object for paths e.g Person would
	 *            prefix path i.e. Person.FirstName
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of TheSameAs matcher
	 */
	public static <T> TheSameAs<T> theSameBeanAs(final T object, final String name) {
		return TheSameAs.theSameBeanAs(object, name);
	}

	/**
	 * Return an instance of a {@link Matcher} which will perform a deep
	 * comparison of the two objects. For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected, "Person"))
	 * 
	 * // To test a simple object but ignore differences in a property
	 * Person expected = new Person("Jane", "Doe");
	 * assertThat(new Person("John", "Doe"), theSameAs(expected,"Person").excludePath("Person.FirstName"));
	 * </pre>
	 * 
	 * @param object
	 *            the instance to match against
	 * @param name
	 *            the name to use for the base object for paths e.g Person would
	 *            prefix path i.e. Person.FirstName
	 * @param propertyType
	 *            the type of properties to match
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of TheSameAs matcher
	 * @deprecated Use {@link #theSameAs(Object, String)} or
	 *             {@link #theSameBeanAs(Object, String)}
	 */
	@Deprecated
	public static <T> TheSameAs<T> theSameAs(final T object, final String name, final PropertyType propertyType) {
		return TheSameAs.theSameAs(object, name, propertyType);
	}

	/**
	 * Return an instance of a {@link Matcher} which will test if an object has
	 * a named property with the given value. For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), hasProperty("FirstName, equalTo("John")))
	 * 
	 * </pre>
	 * 
	 * @param name
	 *            the name of the property
	 * @param matcher
	 *            the matcher to test the value of the property
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of a Matcher
	 */
	public static <T> Matcher<T> hasProperty(final String name, final Matcher<?> matcher) {
		return HasProperty.<T> hasProperty(name, matcher);
	}

	/**
	 * Return an instance of a {@link Matcher} which will test if an object has
	 * a named path with the given value. For Example</p>
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
	 * // To test a simple object
	 * Person expected = new Person("John", "Doe");
	 * assertThat(new Person("John", "Doe"), hasProperty("Person.FirstName, equalTo("John")))
	 * 
	 * </pre>
	 * 
	 * @param path
	 *            the name of the path
	 * @param matcher
	 *            the matcher to test the value of the path
	 * @param <T>
	 *            the type of the instance
	 * @return an instance of a Matcher
	 */
	public static <T> Matcher<T> hasPath(final String name, final Matcher<?> matcher) {
		return HasPath.<T> hasPath(name, matcher);
	}
}
