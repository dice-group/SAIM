package de.uni_leipzig.simba.saim.cytoprocess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Graph {
	public HashSet<ViewNode> nodes = new HashSet<ViewNode>();
	public HashSet<Edge> edges = new HashSet<Edge>();
	
	public List<Integer> getAdjacentEdges(int nodeId) {
		 ArrayList<Integer> ar = new ArrayList<Integer>();
		 for(Edge e: edges) {
			 if(e.nodeA.id == nodeId || e.nodeB.id == nodeId)
				 ar.add(e.id);
		 }
		return ar;
	}
	
	public boolean addNode(ViewNode n) {
		return nodes.add(n);
	}
	public ViewNode getNode(int id) {
		for(ViewNode n : nodes) {
			if(n.id == id)
				return n;
		}
		return null;
	}
	public boolean hasNode(int id) {
		for(ViewNode n : nodes) {
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
//		if(!hasNode(nodeA) || !hasNode(nodeB)) return null;
		Random r = new Random();
		int id = r.nextInt(100);
		while(hasEdge(id))
			id=r.nextInt(100);
		Edge e = new Edge(id, getNode(nodeA), getNode(nodeB));
		edges.add(e);
//		System.out.println("Created edge "+e);
		return e;
	}
	
	public Edge getEdge(int id) {
		for(Edge e: edges) {
			if(e.id == id)
				return e;
		}
			return null;
	}
	
	public boolean hasEdge(int id) {
		for(Edge e: edges) {
			if(e.id == id)
				return true;
		}
		return false;
	}
}


