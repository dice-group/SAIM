package de.uni_leipzig.simba.saim.core;

import java.io.Serializable;

import com.vaadin.ui.Label;

import de.konrad.commons.sparql.PrefixHelper;

public class InstanceMatch  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1683632585316114100L;
	public static final int SEED = 23;
	String uri1, uri2;
	double value = new Double(0);
	boolean selected = false;
	String originalUri1, originalUri2;
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public InstanceMatch(String uri1, String uri2) {
		setUri1(uri1);
		setUri2(uri2);
	}
	public InstanceMatch(String uri1, String uri2, double value) {
		this(uri1, uri2);
		this.value = value;
	}
	public String getUri1() {
		return uri1;
	}
	public void setUri1(String uri1) {
		// avoid <, >, "
		originalUri1=uri1;
		this.uri1 = (uri1.replaceAll("[<>\"]", ""));
	}
	public String getUri2() {
		return uri2;
	}
	public void setUri2(String uri2) {
		originalUri2=uri2;
		this.uri2 = (uri2.replaceAll("[<>\"]", ""));
	}
	public double getValue() {
		return value;
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
	
	public static Label getLinkLabelToUri(String uri) {
		return new Label("<a href='"+uri.replaceAll("[<>\"]", "")+"' target='_blank'>"+PrefixHelper.abbreviate(uri)+"</a>", Label.CONTENT_XHTML);
	}
	public static Label getLinkLabelToUri(String uri, String caption) {
		return new Label("<a href='"+uri.replaceAll("[<>\"]", "")+"' target='_blank'>"+caption+"</a>", Label.CONTENT_XHTML);
	}
	public String getOriginalUri1() {
		return originalUri1;
	}
	public String getOriginalUri2() {
		return originalUri2;
	}
}