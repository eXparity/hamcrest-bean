
package org.exparity.hamcrest.beans.testutils.types;

import java.util.List;

public class Branch {

	private boolean dead;
	private List<Leaf> leaves;

	public Branch(final boolean dead, final List<Leaf> leaves) {
		this.dead = dead;
		this.leaves = leaves;
	}

	public Branch() {}

	public long getNumOfLeaves() {
		return leaves.size();
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(final boolean dead) {
		this.dead = dead;
	}

	public List<Leaf> getLeaves() {
		return leaves;
	}

	public void setLeaves(final List<Leaf> leaves) {
		this.leaves = leaves;
	}
}