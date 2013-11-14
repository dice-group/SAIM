package de.uni_leipzig.simba.saim.cytoprocess;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Graph {
	HashSet<Node> nodes = new HashSet<Node>();
	List<Edge> edges = new LinkedList<Edge>();
	
	public boolean addNode(Node n) {
		return nodes.add(n);
	}
	public Node getNode(int id) {
		for(Node n : nodes) {
			if(n.id == id)
				return n;
		}
		return null;
	}
	public boolean hasNode(int id) {
		for(Node n : nodes) {
			if(n.id == id)
				return true;
		}
		return false;
	}
	
	/**
	 * Attempts to create an Edge;
	 * @param nodeA
	 * @param nodeB
	 * @return Edge iff Nodes exists and no Edge between them already.
	 */
	public Edge createEdge(int nodeA, int nodeB) {
		if(!hasNode(nodeA) || !hasNode(nodeB)) return null;
		Integer i=getEdgeId(nodeA, nodeB);
		if(i!=null)
			return edges.get(i);
		Edge e = new Edge(getNode(nodeA), getNode(nodeB));
		edges.add(e);
		return e;
	}
	
	public Integer getEdgeId(Edge e) {
		return getEdgeId(e.nodeA,e.nodeB);
	}
	
	public Integer getEdgeId(Node nodeA, Node nodeB) {
		return getEdgeId(nodeA.id, nodeB.id);
	}
	public Integer getEdgeId(int nodeA, int nodeB) {
		if(!hasNode(nodeA) || !hasNode(nodeB)) return null;
		for(int i = 0; i<edges.size(); i++) {
			Edge e = edges.get(i);
			if(e.nodeA.id == nodeA && e.nodeA.id == nodeB) {
				return i;
			}
		}
		return null;
	}
	public Edge getEdge(int i) {
		if(edges.size()>i)
			return edges.get(i);
		else
			return null;
	}
}


