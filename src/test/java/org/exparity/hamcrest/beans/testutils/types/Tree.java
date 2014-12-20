
package org.exparity.hamcrest.beans.testutils.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.exparity.dates.en.FluentDate.AUG;

public class Tree {

	private List<Branch> branches = new ArrayList<Branch>();
	private Branch mainBranch = new Branch(false, 100);
	private String name = "Oak";
	private int age = 1;
	private long numOfBranches = 20000L;
	private double weight = 4.56;
	private BigDecimal height = new BigDecimal(10.98);
	private float girth = 2.34f;
	private Date germinationDate = AUG(9, 1975);
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

	public void setMainBranch(final Branch mainBranch) {
		this.mainBranch = mainBranch;
	}

	public Branch getMainBranch() {
		return mainBranch;
	}

	@Override
	public String toString() {
		return "Tree [" + name + "]";
	}
}