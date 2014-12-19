package org.exparity.hamcrest.beans.testutils.types;

public class Branch {

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