
package org.exparity.hamcrest.beans.testutils.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.exparity.dates.en.FluentDate.AUG;

public class ObjectWithAllTypes {

	private List<SimpleTypeWithList> listOfObjects = new ArrayList<SimpleTypeWithList>();
	private SimpleTypeWithList object = new SimpleTypeWithList(false, Arrays.asList(new SimpleType()));
	private String stringValue = "Oak";
	private int intValue = 1;
	private long longValue = 20000L;
	private double doubleValue = 4.56;
	private BigDecimal decimalValue = new BigDecimal(10.98);
	private float floatValue = 2.34f;
	private Date dateValue = AUG(9, 1975);
	private boolean booleanValue = true;

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(final String name) {
		this.stringValue = name;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(final int age) {
		this.intValue = age;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(final long numOfBranches) {
		this.longValue = numOfBranches;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(final double weight) {
		this.doubleValue = weight;
	}

	public BigDecimal getDecimalValue() {
		return decimalValue;
	}

	public void setDecimalValue(final BigDecimal height) {
		this.decimalValue = height;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(final float girth) {
		this.floatValue = girth;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(final Date germinationDate) {
		this.dateValue = germinationDate;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(final boolean deciduous) {
		this.booleanValue = deciduous;
	}

	public List<SimpleTypeWithList> getObjects() {
		return listOfObjects;
	}

	public void setObjects(final List<SimpleTypeWithList> branches) {
		this.listOfObjects = branches;
	}

	public void addObject(final List<SimpleTypeWithList> branches) {
		this.listOfObjects.addAll(branches);
	}

	public void setObject(final SimpleTypeWithList mainBranch) {
		this.object = mainBranch;
	}

	public SimpleTypeWithList getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "ObjectWithAllTypes [" + stringValue + "]";
	}
}