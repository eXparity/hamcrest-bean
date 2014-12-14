package org.exparity.hamcrest.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.exparity.hamcrest.beans.BeanMatchers.theSameAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Unit Test for {@link ObjectMatchers}
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanMatcherTest {

	@Test
	public void canCompareBoolean() {
		assertThat(true, theSameAs(true));
	}

	@Test
	public void canDetectBooleanDifferences() {
		assertThat(true, not(theSameAs(false)));
	}

	@Test
	public void canCompareInteger() {
		assertThat(1, theSameAs(1));
	}

	@Test
	public void canDetectIntegerDifferences() {
		assertThat(1, not(theSameAs(2)));
	}

	@Test
	public void canCompareLong() {
		assertThat(1L, theSameAs(1L));
	}

	@Test
	public void canDetectLongDifferences() {
		assertThat(1L, not(theSameAs(2L)));
	}

	@Test
	public void canCompareDouble() {
		assertThat(1.23401, theSameAs(1.23401));
	}

	@Test
	public void canDetectDoubleDifferences() {
		assertThat(1.23401, not(theSameAs(1.23402)));
	}

	@Test
	public void canCompareFloat() {
		assertThat(1.23401f, theSameAs(1.23401f));
	}

	@Test
	public void canDetectFloatDifferences() {
		assertThat(1.23401f, not(theSameAs(1.23402f)));
	}

	@Test
	public void canCompareString() {
		assertThat("abc", theSameAs("abc"));
	}

	@Test
	public void canDetectStringDifferences() {
		assertThat("abc", not(theSameAs("ABC")));
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

	@Test
	public void canDetectArraySizeDifferences() {
		assertThat(new String[] {
				"abc", "xyz"
		}, not(theSameAs(new String[] {
				"abc"
		})));
	}

	@Test
	public void canDetectContentDifferencesInStringArrays() {
		assertThat(new String[] {
				"abc", "xyz"
		}, not(theSameAs(new String[] {
				"def", "xyz"
		})));
	}

	@Test
	public void canDetectContentDifferencesInByteArrays() {
		assertThat(new byte[] {
				0x01, 0x02
		}, not(theSameAs(new byte[] {
				0x02, 0x01
		})));
	}

	@Test
	public void canCompareMaps() {
		Map<String, Tree> referenceMapOfTrees = singletonMap("Oak", new Tree());
		Map<String, Tree> sampleMapOfTrees = singletonMap("Oak", new Tree());
		assertThat(sampleMapOfTrees, theSameAs(referenceMapOfTrees));
	}

	@Test
	public void canDetectDifferentMapSizes() {
		Map<String, Tree> referenceMapOfTrees = singletonMap("Oak", new Tree());
		Map<String, Tree> sampleMapOfTrees = new HashMap<String, Tree>();
		assertThat(sampleMapOfTrees, not(theSameAs(referenceMapOfTrees)));
	}

	@Test
	public void canDetectDifferentMapKeys() {
		Tree reference = new Tree();
		Map<String, Tree> referenceMapOfTrees = singletonMap(reference.getName(), reference);
		Map<String, Tree> sampleMapOfTrees = singletonMap("Birch", reference);
		assertThat(sampleMapOfTrees, not(theSameAs(referenceMapOfTrees)));
	}

	@Test
	public void canDetectDifferentMapEntries() {
		Tree reference = new Tree();
		Tree sample = new Tree();
		sample.setAge(reference.getAge() + 1);
		Map<String, Tree> referenceMapOfTrees = singletonMap(reference.getName(), reference);
		Map<String, Tree> sampleMapOfTrees = singletonMap(sample.getName(), sample);
		assertThat(sampleMapOfTrees, not(theSameAs(referenceMapOfTrees)));
	}

	@Test
	public void canConfirmNoPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		assertThat(sample, theSameAs(reference));
	}

	@Test
	public void canDetectBooleanPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setDeciduous(false);
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectStringPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setName("Ddssd");
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectDatePropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setGerminationDate(addDays(reference.getGerminationDate(), 100));
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectIntegerPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setAge(reference.getAge() + 1);
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectDoublePropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setWeight(reference.getWeight() + 1);
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectFloatPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setGirth(reference.getGirth() + 1);
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectLongPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setNumOfBranches(reference.getNumOfBranches() + 1);
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectBigDecimalPropertyDifferences() {
		Tree reference = new Tree(), sample = new Tree();
		sample.setHeight(reference.getHeight().add(new BigDecimal(1.1)));
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectDifferentPropertyListSizes() {
		Tree reference = new Tree(), sample = new Tree();
		sample.addBranches(Arrays.asList(new Branch(true, 0)));
		assertThat(sample, not(theSameAs(reference)));
	}

	@Test
	public void canDetectDifferentPropertyListItems() {
		Tree reference = new Tree(), sample = new Tree();
		reference.addBranches(Arrays.asList(new Branch(false, 1234)));
		sample.addBranches(Arrays.asList(new Branch(true, 0)));
		assertThat(sample, not(theSameAs(reference)));
	}

	public static class Branch {

		private boolean dead;
		private long numOfLeaves;

		public Branch(final boolean dead, final long numOfLeaves) {
			this.dead = dead;
			this.numOfLeaves = numOfLeaves;
		}

		public long getNumOfLeaves() {
			return numOfLeaves;
		}

		public void setNumOfLeaves(final long numOfLeaves) {
			this.numOfLeaves = numOfLeaves;
		}

		public boolean isDead() {
			return dead;
		}

		public void setDead(final boolean dead) {
			this.dead = dead;
		}
	}

	public static class Tree {

		private List<Branch> branches = new ArrayList<Branch>();
		private String name = "Oak";
		private int age = 1;
		private long numOfBranches = 20000L;
		private double weight = 4.56;
		private BigDecimal height = new BigDecimal(10.98);
		private float girth = 2.34f;
		private Date germinationDate = new Date();
		private boolean deciduous = true;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(final int age) {
			this.age = age;
		}

		public long getNumOfBranches() {
			return numOfBranches;
		}

		public void setNumOfBranches(final long numOfBranches) {
			this.numOfBranches = numOfBranches;
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(final double weight) {
			this.weight = weight;
		}

		public BigDecimal getHeight() {
			return height;
		}

		public void setHeight(final BigDecimal height) {
			this.height = height;
		}

		public float getGirth() {
			return girth;
		}

		public void setGirth(final float girth) {
			this.girth = girth;
		}

		public Date getGerminationDate() {
			return germinationDate;
		}

		public void setGerminationDate(final Date germinationDate) {
			this.germinationDate = germinationDate;
		}

		public boolean isDeciduous() {
			return deciduous;
		}

		public void setDeciduous(final boolean deciduous) {
			this.deciduous = deciduous;
		}

		public List<Branch> getBranches() {
			return branches;
		}

		public void setBranches(final List<Branch> branches) {
			this.branches = branches;
		}

		public void addBranches(final List<Branch> branches) {
			this.branches.addAll(branches);
		}

		@Override
		public String toString() {
			return "Tree [" + name + "]";
		}
	}

}
