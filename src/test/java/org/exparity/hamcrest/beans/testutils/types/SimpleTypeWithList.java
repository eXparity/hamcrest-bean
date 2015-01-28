
package org.exparity.hamcrest.beans.testutils.types;

import java.util.List;

public class SimpleTypeWithList {

	private boolean dead;
	private List<SimpleType> leaves;

	public SimpleTypeWithList(final boolean dead, final List<SimpleType> leaves) {
		this.dead = dead;
		this.leaves = leaves;
	}

	public SimpleTypeWithList() {}

	public long getNumOfLeaves() {
		return leaves.size();
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(final boolean dead) {
		this.dead = dead;
	}

	public List<SimpleType> getLeaves() {
		return leaves;
	}

	public void setLeaves(final List<SimpleType> leaves) {
		this.leaves = leaves;
	}
}