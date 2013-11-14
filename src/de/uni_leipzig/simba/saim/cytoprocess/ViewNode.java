package de.uni_leipzig.simba.saim.cytoprocess;

import java.util.HashMap;

public class ViewNode {
	public static final int TRIANGLE = 0;
	public static final int DIAMOND = 1;
	public static final int ELLIPSE = 2;
	public static final int HEXAGON = 3;
	public static final int OCTAGON = 4;
	public static final int PARALELLOGRAM = 5;
	public static final int RECTANGLE = 6;
	public static final int ROUNDED_RECTANGLE = 7;
	public static final int VEE = 8;
	
	public int id;
	public String name;
	public int x=0;
	public int y=0;
	public int nodeViewShape;
	public String rgb;
	public HashMap<String, Object> labeling = new HashMap<String, Object>();
	public ViewNode(final String name, final int x, final int y, int nodeViewShape,String rgb) {
		this.name=name;
		this.x=x;
		this.y=y;
		this.nodeViewShape = nodeViewShape;
		this.rgb=rgb;
	}
	public ViewNode(int id) {
		this.id =id;
	}
	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public boolean equals(Object o) {
		if ( o == null )
		   return false;
		if ( o == this )
			return true;
		else {
			ViewNode that = (ViewNode) o;
			if(that.id == id)
				return true;						
		}
		return false;
	}
	@Override
	public String toString() {
		return "ViewNode(id="+id+"):"+name;
	}
}
