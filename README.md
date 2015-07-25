Hamcrest Bean  [![Build Status](https://travis-ci.org/eXparity/hamcrest-bean.svg?branch=master)](https://travis-ci.org/eXparity/hamcrest-bean) [![Coverage Status](https://coveralls.io/repos/eXparity/hamcrest-bean/badge.png?branch=master)](https://coveralls.io/r/eXparity/hamcrest-bean?branch=master)
=============

A Java library which provides a hamcrest matcher for matching objects and graphs which follow the Java beans standard.

Licensed under [BSD License][].

What is Hamcrest Bean?
-----------------
Hamcrest Bean is an extension library for the [Java Hamcrest][] matcher library which provides Matcher implementations for performing deep object graph matching of java beans

Downloads
---------
You can obtain Hamcrest Bean binaries from [maven central][]. To include your project in:

A maven project

    <dependency>
        <groupId>org.exparity</groupId>
        <artifactId>hamcrest-bean</artifactId>
        <version>1.0.11</version>
    </dependency>

A project which uses ivy for dependency management

    <dependency org="org.exparity" name="hamcrest-bean" rev="1.0.11"/>
            
Binaries
--------
Hamcrest Bean has a single binary, hamcrest-bean.jar, which contains all the date matchers. Sources and JavaDoc jars are available.

Usage
-------------

The matchers are exposed as static methods on the BeanMatchers class. For Example

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    MatcherAssert.assertThat(save, BeanMatchers.theSameAs(object));

or after static importing

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object));

The matcher allows paths, properties, and types to be excluded from the match if they're expected to be different. For example to exclude a path 

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).excludePath("MyObject.Id"));

or to exclude a property;

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).excludeProperty("Id"));

or to exclude a path;

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).excludeType(String.class));

The matcher allows paths, properties, and types to use alternate matchers if you want finer control over the match. For example to override the match for a path

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).comparePath("MyObject.Name", new IsEqualIgnoreCase()));

or to override the match for a property;

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).compareProperty("Name", new IsEqualIgnoreCase()));

or to override the match for a path;

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).compareType(String.class, new IsEqualIgnoreCase()));

The matcher also supports using other [Java Hamcrest][] matchers to act as comparators.For example.

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).comparePath("MyObject.Name", Matchers.startsWith("S")));

or to override the match for a property;

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).compareProperty("Name", Matchers.startsWith("S")));

or to override the match for a path;

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object).compareType(String.class, Matchers.notNullValue()));

The matcher exposes a fluent interface so multiple exclusions or overrides can be provider. For example

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameAs(object)
                         .compareType(String.class, new IsEqualIgnoreCase())
                         .excludeProperty("Id")
                         .excludePath("MyObject.Name"));

If you only want to include bean properties, those which have both a getter and setter pair, in the comparison then use the theSameBeanAs matcher. For example 

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    MatcherAssert.assertThat(save, BeanMatchers.theSameBeanAs(object));

or after static importing

    MyObject object = new MyObject();
    MyObject saved = dao.save(object);
    assertThat(save, theSameBeanAs(object));

 
The library includes several built in overrides for the comparison

* __IsComparable__ - Test if the objects are comparable
* __IsEqual__ - Test if the objects are equals
* __IsEqualIgnoreCase__ - Test if two string objects are equal regardless of case
* __HasPattern__ - Test if the strings match the pattern
* __Excluded__ - Exclude a property, type, or method from the comparison
* __IsEqualDate__ - Test it two date objects are equal date but not time or millisecond portion
* __IsEqualDateTime__ - Test it two date objects are equal date and time but no millisecond portion
* __IsEqualTimestamp__ - Test it two date objects are equal timestamps
* __Matches__ - Test if a property, type, or method are equal using a hamcrest matcher

The Javadocs include examples on all methods so you can look there for examples for specific methods.

Source
------
The source is structured along the lines of the maven standard folder structure for a jar project.

  * Core classes [src/main/java]
  * Unit tests [src/test/java]

The source includes a pom.xml for building with Maven 

Release Notes
-------------
Changes 1.0.10 -> 1.0.11
  * Fix Issue #6 and support comparing nulls in arrays
Changes 1.0.9 -> 1.0.10
  * Add IsEqual date comparators and add BeanComparators static factory
Changes 1.0.8 -> 1.0.9
  * Add theSameBeanAs matchers
Changes 1.0.4 -> 1.0.8
  * Fix issue 4 comparing null nested objects
Changes 1.0.3 -> 1.0.4
  * Strongly type PropertyComparator


Acknowledgements
----------------
Developers:
  * Stewart Bissett
  * Simon Birt

Thanks to the developers at [Java Hamcrest][]. Without their hardwork and core libraries there'd be nothing to be extend and we be stuck with old school, non-declarative, non-reusable, assertions.

[BSD License]: http://opensource.org/licenses/BSD-3-Clause
[Maven central]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22hamcrest-bean%22
[Java Hamcrest]: http://github.com/hamcrest/JavaHamcrest
