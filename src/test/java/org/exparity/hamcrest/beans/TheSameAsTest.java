package org.exparity.hamcrest.beans;

import static java.util.Collections.singletonMap;
import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.exparity.hamcrest.BeanMatchers.theSameAs;
import static org.exparity.stub.random.RandomBuilder.aRandomInstanceOf;
import static org.exparity.stub.random.RandomBuilder.aRandomString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.exparity.hamcrest.BeanMatchers;
import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;
import org.exparity.hamcrest.beans.comparators.HasPattern;
import org.exparity.hamcrest.beans.testutils.types.*;
import org.testng.annotations.Test;

/**
 * Unit Test for {@link TheSameAs}
 * 
 * @author Stewart Bissett
 */
public class TheSameAsTest {

	@Test
	public void canCompareBoolean() {
		assertThat(true, theSameAs(true));
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "\nExpected: the same as <false>\n.*but: Boolean is <true> instead of <false>")
	public void
			canCompareDifferentBooleans() {
		assertThat(true, theSameAs(false));
	}

	@Test
	public void canCompareInteger() {
		assertThat(1, theSameAs(1));
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "\nExpected: the same as <2>\n.*but: Integer is <1> instead of <2>")
	public void
			canCompareDifferentIntegers() {
		assertThat(1, theSameAs(2));
	}

	@Test
	public void canCompareLong() {
		assertThat(1L, theSameAs(1L));
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "\nExpected: the same as <2L>\n.*but: Long is <1L> instead of <2L>")
	public void
			canCompareDifferentLongs() {
		assertThat(1L, theSameAs(2L));
	}

	@Test
	public void canCompareDouble() {
		assertThat(1.23401, theSameAs(1.23401));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentDoubles() {
		assertThat(1.23401, theSameAs(1.23402));
	}

	@Test
	public void canCompareFloat() {
		assertThat(1.23401f, theSameAs(1.23401f));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentFloats() {
		assertThat(1.23401f, theSameAs(1.23402f));
	}

	@Test
	public void canCompareString() {
		assertThat("abc", theSameAs("abc"));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentStrings() {
		assertThat("abc", theSameAs("ABC"));
	}

	@Test
	public void canCompareListsOfNonComparableObjects() {
		ObjectWithAllTypes tree = new ObjectWithAllTypes(), otherTree = new ObjectWithAllTypes();
		tree.addObject(Arrays.asList(new SimpleTypeWithList(false, Arrays.asList(new SimpleType("A"), new SimpleType(
				"B")))));
		otherTree.addObject(Arrays.asList(new SimpleTypeWithList(false, Arrays.asList(
				new SimpleType("B"),
					new SimpleType("A")))));
		assertThat(tree, theSameAs(otherTree));
	}

	@Test
	public void canCompareArrays() {
		assertThat(new String[] { "abc", "xyz" }, theSameAs(new String[] { "abc", "xyz" }));
	}

	@Test
	public void canCompareArrayOfBytes() {
		assertThat(new byte[] { 0x01, 0x02 }, theSameAs(new byte[] { 0x01, 0x02 }));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentArraySizes() {
		assertThat(new String[] { "abc", "xyz" }, theSameAs(new String[] { "abc" }));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentStringArrays() {
		assertThat(new String[] { "abc", "xyz" }, theSameAs(new String[] { "def", "xyz" }));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentByteArrays() {
		assertThat(new byte[] { 0x01, 0x02 }, theSameAs(new byte[] { 0x02, 0x01 }));
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void canCompareArraysContainingNullExpected() {
		assertThat(new String[] { "A", "B", null }, theSameAs(new String[] { "C", "D", null }));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareArraysContainingNullVsPresent() {
		assertThat(new String[] { "A", "B", null }, theSameAs(new String[] { "C", "D", "E" }));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareArraysContainingPresentVsNull() {
		assertThat(new String[] { "A", "B", "C" }, theSameAs(new String[] { "E", "F", null }));
	}

	@Test
	public void canCompareObjectArrays() {
		assertThat(
				new SimpleType[] { new SimpleType("A"), new SimpleType("B") },
					theSameAs(new SimpleType[] { new SimpleType("A"), new SimpleType("B") }));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentObjectArrays() {
		assertThat(
				new SimpleType[] { new SimpleType("A"), new SimpleType("B") },
					theSameAs(new SimpleType[] { new SimpleType("A"), new SimpleType("C") }));
	}

	@Test
	public void canCompareMaps() {
		Map<String, ObjectWithAllTypes> referenceMapOfTrees = singletonMap("Oak", new ObjectWithAllTypes());
		Map<String, ObjectWithAllTypes> sampleMapOfTrees = singletonMap("Oak", new ObjectWithAllTypes());
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentMapSizes() {
		Map<String, ObjectWithAllTypes> referenceMapOfTrees = singletonMap("Oak", new ObjectWithAllTypes());
		Map<String, ObjectWithAllTypes> sampleMapOfTrees = new HashMap<String, ObjectWithAllTypes>();
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentMapKeys() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		Map<String, ObjectWithAllTypes> referenceMapOfTrees = singletonMap(reference.getStringValue(), reference);
		Map<String, ObjectWithAllTypes> sampleMapOfTrees = singletonMap("Birch", reference);
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentMapEntries() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setIntValue(reference.getIntValue() + 1);
		Map<String, ObjectWithAllTypes> referenceMapOfTrees = singletonMap(reference.getStringValue(), reference);
		Map<String, ObjectWithAllTypes> sampleMapOfTrees = singletonMap(sample.getStringValue(), sample);
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test
	public void canConfirmNoProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentBooleanProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setBooleanValue(false);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentStringProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setStringValue("Ddssd");
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentDateProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setDateValue(addDays(reference.getDateValue(), 100));
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentIntegerProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setIntValue(reference.getIntValue() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentDoubleProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setDoubleValue(reference.getDoubleValue() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentFloatProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setFloatValue(reference.getFloatValue() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentLongProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setLongValue(reference.getLongValue() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentBigDecimalProperties() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.setDecimalValue(reference.getDecimalValue().add(new BigDecimal(1.1)));
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentPropertyListSizes() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		sample.addObject(Arrays.asList(new SimpleTypeWithList(true, Arrays.asList(new SimpleType()))));
		assertThat(sample, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentPropertyListItems() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		reference.addObject(Arrays.asList(new SimpleTypeWithList(false, Arrays.asList(new SimpleType()))));
		sample.addObject(Arrays.asList(new SimpleTypeWithList(true, Arrays.asList(new SimpleType()))));
		assertThat(sample, theSameAs(reference));
	}

	@Test
	public void canExcludeType() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes(), sample = new ObjectWithAllTypes();
		reference.addObject(Arrays.asList(new SimpleTypeWithList(false, Arrays.asList(new SimpleType()))));
		sample.addObject(Arrays.asList(new SimpleTypeWithList(true, Arrays.asList(new SimpleType()))));
		assertThat(sample, theSameAs(reference).excludeType(SimpleTypeWithList.class));
	}

	@Test
	public void canExcludePath() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		reference.setIntValue(1);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Oak");
		sample.setIntValue(2);
		assertThat(sample, theSameAs(reference).excludePath("ObjectWithAllTypes.IntValue"));
	}

	@Test
	public void canExcludePathNamedType() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		reference.setIntValue(1);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Oak");
		sample.setIntValue(2);
		assertThat(sample, theSameAs(reference, "type").excludePath("type.IntValue"));
	}

	@Test
	public void canExcludeProperty() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		reference.setIntValue(1);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Oak");
		sample.setIntValue(2);
		assertThat(sample, theSameAs(reference).excludeProperty("IntValue"));
	}

	@Test
	public void canOverridePathComparator() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference).comparePath("ObjectWithAllTypes.StringValue", new HasPattern("O.*")));
	}

	@Test
	public void canOverridePathComparatorWithNamedType() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference, "base").comparePath("base.StringValue", new HasPattern("O.*")));
	}

	@Test
	public void canOverridePropertyComparator() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference).compareProperty("StringValue", new HasPattern("O.*")));
	}

	@Test
	public void canOverrideTypeComparatorLangType() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference).compareType(String.class, new HasPattern("O.*")));
	}

	@Test
	public void canOverrideTypeComparator() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setObject(new SimpleTypeWithList(true, Arrays.asList(new SimpleType())));
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setObject(new SimpleTypeWithList(false, Arrays.asList(new SimpleType())));
		assertThat(
				sample,
					theSameAs(reference).compareType(
							SimpleTypeWithList.class,
								new PropertyComparator<SimpleTypeWithList>() {

									@Override
									public boolean matches(final SimpleTypeWithList lhs, final SimpleTypeWithList rhs) {
										return true;
									}
								}));
	}

	@Test
	public void canOverridePathComparatorWithMatcher() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference).comparePath("ObjectWithAllTypes.StringValue", startsWith("O")));
	}

	@Test
	public void canOverridePathComparatorWithMatcherWithNamedType() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference, "base").comparePath("base.StringValue", startsWith("O")));
	}

	@Test
	public void canOverridePropertyComparatorWithMatcher() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference).compareProperty("StringValue", startsWith("O")));
	}

	@Test
	public void canOverrideTypeComparatorWithMatcher() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue("Oak");
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue("Olive");
		assertThat(sample, theSameAs(reference).compareType(String.class, startsWith("O")));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareNullVsExisting() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(null);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(aRandomString());
		assertThat(sample, theSameAs(reference));
	}

	@Test
	public void canExcludeNullPropertyVsExisting() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(null);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(aRandomString());
		assertThat(sample, theSameAs(reference).excludeProperty("StringValue"));
	}

	@Test
	public void canExcludeNullPathVsExisting() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(null);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(aRandomString());
		assertThat(sample, theSameAs(reference).excludePath("ObjectWithAllTypes.StringValue"));
	}

	@Test
	public void canExcludeNullTypeVsExisting() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(null);
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(aRandomString());
		assertThat(sample, theSameAs(reference).excludeType(String.class));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareExistingVsNull() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(aRandomString());
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(null);
		assertThat(sample, theSameAs(reference));
	}

	@Test
	public void canExcludeExistingPropertyVsNull() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(aRandomString());
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(null);
		assertThat(sample, theSameAs(reference).excludeProperty("StringValue"));
	}

	@Test
	public void canExcludeExistingPathVsNull() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(aRandomString());
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(null);
		assertThat(sample, theSameAs(reference).excludePath("ObjectWithAllTypes.StringValue"));
	}

	@Test
	public void canExcludeExistingTypeVsNull() {
		ObjectWithAllTypes reference = new ObjectWithAllTypes();
		reference.setStringValue(aRandomString());
		ObjectWithAllTypes sample = new ObjectWithAllTypes();
		sample.setStringValue(null);
		assertThat(sample, theSameAs(reference).excludeType(String.class));
	}

	@Test
	public void canMatchListsOfTypesWithInnerClasses() {
		ClassContainingNestedClasses reference = new ClassContainingNestedClasses(new OuterClass("A"), new OuterClass(
				"B"));
		ClassContainingNestedClasses actual = new ClassContainingNestedClasses(new OuterClass("A"), new OuterClass("B"));
		assertThat(actual, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canMismatchListsOfTypesWithInnerClasses() {
		ClassContainingNestedClasses reference = new ClassContainingNestedClasses(new OuterClass("A"), new OuterClass(
				"B"));
		ClassContainingNestedClasses actual = new ClassContainingNestedClasses(new OuterClass("C"), new OuterClass("D"));
		assertThat(actual, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canMatchNonBeanProperties() {
		NotBean reference = new NotBean(aRandomString(10), aRandomString(10));
		NotBean actual = new NotBean(reference.getStringA(), reference.getStringB() + aRandomString(10));
		assertThat(actual, theSameAs(reference));
	}

	@Test
	public void canIgnoreNonBeanProperties() {
		NotBean reference = new NotBean(aRandomString(10), aRandomString(10));
		NotBean actual = new NotBean(reference.getStringA(), reference.getStringB() + aRandomString(10));
		assertThat(actual, BeanMatchers.theSameBeanAs(reference));
	}

	@Test
	public void canIgnoreNonBeanPropertiesWithNamedType() {
		NotBean reference = new NotBean(aRandomString(10), aRandomString(10));
		NotBean actual = new NotBean(reference.getStringA(), reference.getStringB() + aRandomString(10));
		assertThat(actual, BeanMatchers.theSameBeanAs(reference, "bean"));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canTestNestedNullObjects() {
		SimpleTypeWithSimpleType reference = aRandomInstanceOf(SimpleTypeWithSimpleType.class);
		SimpleTypeWithSimpleType actual = new SimpleTypeWithSimpleType();
		assertThat(actual, theSameAs(reference));
	}

	@Test
	public void canCompareSameEnumValues() {
		final SimpleEnum reference = SimpleEnum.VALUE_A;
		final SimpleEnum actual = SimpleEnum.VALUE_A;
		assertThat(actual, theSameAs(reference));
	}

	@Test(expectedExceptions = AssertionError.class)
	public void canCompareDifferentEnumValues() {
		final SimpleEnum reference = SimpleEnum.VALUE_A;
		final SimpleEnum actual = SimpleEnum.VALUE_B;
		assertThat(actual, theSameAs(reference));
	}

}
