package org.exparity.hamcrest.beans.testutils.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassContainingNestedClasses {

	private List<OuterClass> nested = new ArrayList<>();

	public ClassContainingNestedClasses(final OuterClass... nested) {
		this.nested.addAll(Arrays.asList(nested));
	}

	public List<OuterClass> getNested() {
		return nested;
	}

}