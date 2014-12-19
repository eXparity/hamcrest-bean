
package org.exparity.hamcrest.beans;

/**
 * Static factory for creating {@link Matcher} instances for performing a deep comparison of instances and comparing all methods on the instances which follow the Java bean naming
 * standard. For example
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
 * // Testing a simple object is the same as an expected instance
 * Person expected = new Person("John", "Doe");
 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected))
 * 
 * // Testing a simple object is the same except for a property
 * Person expected = new Person("Jane", "Doe");
 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected).excludeProperty("LastName"));
 * </pre>
 * 
 * @author Stewart Bissett
 */
public abstract class BeanMatchers {

	/**
	 * Static factory to return an instance of a {@link Matcher} which will perform a deep comparison of the two objects. For Example</p>
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
	 * // Testing a simple object is the same as an expected instance
	 * Person expected = new Person("John", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected))
	 * 
	 * // Testing a simple object is the same except for a property
	 * Person expected = new Person("Jane", "Doe");
	 * MatcherAssert.assertThat(new Person("John", "Doe"), BeanMatchers.theSameAs(expected).excludeProperty("LastName"));
	 * </pre>
	 */
	public static <T> TheSameAs<T> theSameAs(final T object) {
		return new TheSameAs<T>(object);
	}
}
