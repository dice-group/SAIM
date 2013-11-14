package de.uni_leipzig.simba.saim.cytoprocess;

public class Edge {
	Node nodeA;
	Node nodeB;
	public String name;
	public String label;
	public Edge(Node nodeA, Node nodeB) {
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
			if(that.nodeA.equals(nodeA) && that.nodeB.equals(nodeB))
				return true;						
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return nodeA.hashCode() + nodeB.hashCode();
	}
}