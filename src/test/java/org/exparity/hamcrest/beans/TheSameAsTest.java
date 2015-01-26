
package org.exparity.hamcrest.beans;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.exparity.hamcrest.beans.TheSameAs.PropertyComparator;
import org.exparity.hamcrest.beans.comparators.HasPattern;
import org.exparity.hamcrest.beans.testutils.types.Branch;
import org.exparity.hamcrest.beans.testutils.types.Leaf;
import org.exparity.hamcrest.beans.testutils.types.Tree;
import org.junit.Test;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.exparity.hamcrest.BeanMatchers.theSameAs;
import static org.hamcrest.MatcherAssert.assertThat;

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

	@Test(expected = AssertionError.class)
	public void canDetectBooleanDifferences() {
		assertThat(true, theSameAs(false));
	}

	@Test
	public void canCompareInteger() {
		assertThat(1, theSameAs(1));
	}

	@Test(expected = AssertionError.class)
	public void canDetectIntegerDifferences() {
		assertThat(1, theSameAs(2));
	}

	@Test
	public void canCompareLong() {
		assertThat(1L, theSameAs(1L));
	}

	@Test(expected = AssertionError.class)
	public void canDetectLongDifferences() {
		assertThat(1L, theSameAs(2L));
	}

	@Test
	public void canCompareDouble() {
		assertThat(1.23401, theSameAs(1.23401));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDoubleDifferences() {
		assertThat(1.23401, theSameAs(1.23402));
	}

	@Test
	public void canCompareFloat() {
		assertThat(1.23401f, theSameAs(1.23401f));
	}

	@Test(expected = AssertionError.class)
	public void canDetectFloatDifferences() {
		assertThat(1.23401f, theSameAs(1.23402f));
	}

	@Test
	public void canCompareString() {
		assertThat("abc", theSameAs("abc"));
	}

	@Test(expected = AssertionError.class)
	public void canDetectStringDifferences() {
		assertThat("abc", theSameAs("ABC"));
	}

	@Test
	public void canCompareListsOfNonComparableObjects() {
		Tree tree = new Tree(), otherTree = new Tree();
		tree.addBranches(Arrays.asList(new Branch(false, Arrays.asList(new Leaf()))));
		otherTree.addBranches(Arrays.asList(new Branch(false, Arrays.asList(new Leaf()))));
		assertThat(tree, theSameAs(otherTree));
	}

	@Test
	public void canCompareArrays() {
		assertThat(new String[] {
				"abc", "xyz"
		}, theSameAs(new String[] {
				"abc", "xyz"
		}));
	}

	@Test
	public void canCompareArrayOfBytes() {
		assertThat(new byte[] {
				0x01, 0x02
		}, theSameAs(new byte[] {
				0x01, 0x02
		}));
	}

	@Test(expected = AssertionError.class)
	public void canDetectArraySizeDifferences() {
		assertThat(new String[] {
				"abc", "xyz"
		}, theSameAs(new String[] {
				"abc"
		}));
	}

	@Test(expected = AssertionError.class)
	public void canDetectContentDifferencesInStringArrays() {
		assertThat(new String[] {
				"abc", "xyz"
		}, theSameAs(new String[] {
				"def", "xyz"
		}));
	}

	@Test(expected = AssertionError.class)
	public void canDetectContentDifferencesInByteArrays() {
		assertThat(new byte[] {
				0x01, 0x02
		}, theSameAs(new byte[] {
				0x02, 0x01
		}));
	}

	@Test
	public void canCompareMaps() {
		Map<String, Tree> referenceMapOfTrees = singletonMap("Oak", new Tree());
		Map<String, Tree> sampleMapOfTrees = singletonMap("Oak", new Tree());
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDifferentMapSizes() {
		Map<String, Tree> referenceMapOfTrees = singletonMap("Oak", new Tree());
		Map<String, Tree> sampleMapOfTrees = new HashMap<String, Tree>();
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDifferentMapKeys() {
		Tree reference = new Tree();
		Map<String, Tree> referenceMapOfTrees = singletonMap(reference.getName(), reference);
		Map<String, Tree> sampleMapOfTrees = singletonMap("Birch", reference);
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDifferentMapEntries() {
		Tree reference = new Tree();
		Tree sample = new Tree();
		sample.setAge(reference.getAge() + 1);
		Map<String, Tree> referenceMapOfTrees = singletonMap(reference.getName(), reference);
		Map<String, Tree> sampleMapOfTrees = singletonMap(sample.getName(), sample);
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test
	public void canConfirmNoPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectBooleanPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setDeciduous(false);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectStringPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setName("Ddssd");
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDatePropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setGerminationDate(addDays(reference.getGerminationDate(), 100));
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectIntegerPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setAge(reference.getAge() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDoublePropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setWeight(reference.getWeight() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectFloatPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setGirth(reference.getGirth() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectLongPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setNumOfBranches(reference.getNumOfBranches() + 1);
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectBigDecimalPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setHeight(reference.getHeight().add(new BigDecimal(1.1)));
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDifferentPropertyListSizes() {
		Tree reference = new Tree(), sample = new Tree();
		sample.addBranches(Arrays.asList(new Branch(true, Arrays.asList(new Leaf()))));
		assertThat(sample, theSameAs(reference));
	}

	@Test(expected = AssertionError.class)
	public void canDetectDifferentPropertyListItems() {
		Tree reference = new Tree(), sample = new Tree();
		reference.addBranches(Arrays.asList(new Branch(false, Arrays.asList(new Leaf()))));
		sample.addBranches(Arrays.asList(new Branch(true, Arrays.asList(new Leaf()))));
		assertThat(sample, theSameAs(reference));
	}

	@Test
	public void canExcludeType() {
		Tree reference = new Tree(), sample = new Tree();
		reference.addBranches(Arrays.asList(new Branch(false, Arrays.asList(new Leaf()))));
		sample.addBranches(Arrays.asList(new Branch(true, Arrays.asList(new Leaf()))));
		assertThat(sample, theSameAs(reference).excludeType(Branch.class));
	}

	@Test
	public void canExcludePath() {
		Tree reference = new Tree();
		reference.setName("Oak");
		reference.setAge(1);
		Tree sample = new Tree();
		sample.setName("Oak");
		sample.setAge(2);
		assertThat(sample, theSameAs(reference).excludePath("Tree.Age"));
	}

	@Test
	public void canExcludeProperty() {
		Tree reference = new Tree();
		reference.setName("Oak");
		reference.setAge(1);
		Tree sample = new Tree();
		sample.setName("Oak");
		sample.setAge(2);
		assertThat(sample, theSameAs(reference).excludeProperty("Age"));
	}

	@Test
	public void canOverridePathComparator() {
		Tree reference = new Tree();
		reference.setName("Oak");
		Tree sample = new Tree();
		sample.setName("Olive");
		assertThat(sample, theSameAs(reference).comparePath("Tree.Name", new HasPattern("O.*")));
	}

	@Test
	public void canOverridePropertyComparator() {
		Tree reference = new Tree();
		reference.setName("Oak");
		Tree sample = new Tree();
		sample.setName("Olive");
		assertThat(sample, theSameAs(reference).compareProperty("Name", new HasPattern("O.*")));
	}

	@Test
	public void canOverrideTypeComparatorLangType() {
		Tree reference = new Tree();
		reference.setName("Oak");
		Tree sample = new Tree();
		sample.setName("Olive");
		assertThat(sample, theSameAs(reference).compareType(String.class, new HasPattern("O.*")));
	}

	@Test
	public void canOverrideTypeComparator() {
		Tree reference = new Tree();
		reference.setMainBranch(new Branch(true, Arrays.asList(new Leaf())));
		Tree sample = new Tree();
		sample.setMainBranch(new Branch(false, Arrays.asList(new Leaf())));
		assertThat(sample, theSameAs(reference).compareType(Branch.class, new PropertyComparator() {

			@Override
			public boolean matches(final Object lhs, final Object rhs) {
				return true;
			}
		}));
	}

}
