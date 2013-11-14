package de.uni_leipzig.simba.saim.cytoprocess;

public class Node {
	public int id;
	public String name;
	public int x=0;
	public int y=0;
	public int nodeViewShape;
	public String rgb;
	
	public Node(final String name, final int x, final int y, int nodeViewShape,String rgb) {
		this.name=name;
		this.x=x;
		this.y=y;
		this.nodeViewShape = nodeViewShape;
		this.rgb=rgb;
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
			Node that = (Node) o;
			if(that.id == id)
				return true;						
		}
		return false;
	}
}
