package de.uni_leipzig.simba.saim.core;

import java.io.Serializable;

public class InstanceMatch  implements Serializable{
	public static final int SEED = 23;
	String uri1, uri2;
	double value = new Double(0);
	boolean selected = false;
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public InstanceMatch(String uri1, String uri2) {
		this.uri1 = uri1;
		this.uri2 = uri2;
	}
	public InstanceMatch(String uri1, String uri2, double value) {
		this(uri1, uri2);
		this.value = value;
	}
	public String getUri1() {
		return uri1;
	}
	public void setUri1(String uri1) {
		this.uri1 = uri1;
	}
	public String getUri2() {
		return uri2;
	}
	public void setUri2(String uri2) {
		this.uri2 = uri2;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		  int result = SEED;
		  result = getFieldHash(result, uri1);
		  result = getFieldHash(result, uri2);
		  return result;
	}
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof InstanceMatch)) {
			return false;
		}
		InstanceMatch oi = (InstanceMatch) o;		
		return (oi.getUri1().equals(uri1) && oi.getUri2().equals(uri2));
		    
	}
	
	private int getFieldHash(int seed, Object o) {
		return seed + o.hashCode();
	}
	
	@Override
	public String toString() {
		return uri1 + " - " + uri2 +": "+value+"? "+selected;
	}
	
}