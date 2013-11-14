package de.uni_leipzig.simba.saim.cytoprocess;

public class Edge {
	public ViewNode nodeA;
	public ViewNode nodeB;
	public int id;
	public String name;
	public String label;
	public Edge(int id, ViewNode nodeA, ViewNode nodeB) {
		this.id = id;
		this.nodeA=nodeA;
		this.nodeB=nodeB;
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o == null )
		   return false;
		if ( o == this )
			return true;
		else {
			Edge that = (Edge) o;
			if(that.id ==this.id) return true;
			if(that.nodeA.equals(nodeA) && that.nodeB.equals(nodeB))
				return true;						
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return nodeA.hashCode() + nodeB.hashCode();
	}
	@Override
	public String toString() {
		return "Edge(id="+id+"):"+nodeA+" - "+nodeB;
	}
}