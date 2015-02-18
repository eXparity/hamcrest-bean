package org.exparity.hamcrest.beans.testutils.types;


public class OuterClass {

	public static class InnerClass {

		private final String name;

		public InnerClass(final String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	private final OuterClass.InnerClass inner;

	public OuterClass(final String name) {
		this.inner = new InnerClass(name);
	}

	public String getName() {
		return inner.name;
	}

}