package de.uni_leipzig.simba.saim.cytoprocess;

import giny.model.Edge;
import giny.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import cytoscape.layout.CyLayoutAlgorithm;
import cytoscape.view.CyNetworkView;
/**
 * 
 * @author rspeck
 *
 */
public class GraphProperties {
	private static final Logger LOGGER = Logger.getLogger(GraphProperties.class);

	private CyNetwork cyNetwork;

	private CyNetworkView cyNetworkView;

	private final String title;

	private int width, height;

	private Random rand = new Random();

	public List<Integer> idsToUpdate = new ArrayList<Integer>();
	
	public GraphProperties(final CyNetwork network, final CyNetworkView finalView, final String p_title) {
		
		if(LOGGER.isDebugEnabled()) LOGGER.debug("GraphProperties...");
		cyNetwork = network;
		cyNetworkView = finalView;
		title = p_title;
	}
	public void applyLayoutAlgorithm(final CyLayoutAlgorithm loAlgorithm) {
		cyNetworkView.applyLayout(loAlgorithm);
	}

	/**
	 * Adds an edge.
	 * 
	 * @param 
	 * @return
	 */
	public Integer addEdge(int nodeAid, int nodeBid, String name) {
		if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge to cytoscape...");
		
		if(nodeAid != nodeBid){
			final CyNode node1 = Cytoscape.getCyNode(String.valueOf(nodeAid), false);
			final CyNode node2 = Cytoscape.getCyNode(String.valueOf(nodeBid), false);

			if (node1 != null && node2 != null) {
				// check if edge exists
				if(cyNetwork.getEdgeCount(nodeAid, nodeBid, true) == 0 && cyNetwork.getEdgeCount(nodeBid, nodeAid, true) == 0){	
			
					CyEdge edge  = null;
					String tmpname = "";
					do{
						tmpname = "#" + rand.nextInt(999999999) + "_edge";
						edge =  Cytoscape.getCyEdge(node1, node2, Semantics.INTERACTION, tmpname,false) ;
					}while(edge != null);				
					edge = Cytoscape.getCyEdge(node1, node2, Semantics.INTERACTION, tmpname, true);
					// edge.isDirected()  => true
					int id = edge.getRootGraphIndex();
					
					edge.setIdentifier(String.valueOf(id));
					cyNetwork.addEdge(edge);
					cyNetworkView.addEdgeView(id);
					Cytoscape.getEdgeAttributes().setAttribute(String.valueOf(id), "label", name);
	
					if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge created with id: " + id + " nodes are: " + nodeAid + "," + nodeBid);
					
					
					return edge.getRootGraphIndex();
				}else
					if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge failed: there is an edge from nodeAid to nodeBid");
			}else
				if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge failed: nodeAid or nodeBid are N/A");
		}else
			if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge failed: nodeAid == nodeBid");

		return null;
	}

	/**
	 * Adds a node. Node with same label are allowed.
	 * 
	 * 
	 * @param name node label
	 * @param x position	
	 * @param y	position
	 * @param shape node type
	 * @return node id
	 */
	public int addNode(final String name, final int x, final int y, int nodeViewShape,String rgb) {
		if(LOGGER.isDebugEnabled()) LOGGER.debug("addNode to cytoscape...");
		
		// search for a free node	
		CyNode node = null;
		String tmpname = "";
		do{
			tmpname = "#" + rand.nextInt(999999999)+"_node";
			node = Cytoscape.getCyNode(tmpname) ;
		}while(node != null);

		node = Cytoscape.getCyNode(tmpname, true);	
		int id = node.getRootGraphIndex();
		node.setIdentifier(String.valueOf(id)); 		

		cyNetwork.addNode(node);		
		cyNetworkView.addNodeView(id).setXPosition(x);
		cyNetworkView.addNodeView(id).setYPosition(y);
		//cyNetworkView.addNodeView(id).setShape(nodeViewShape);
		
		Cytoscape.getNodeAttributes().setAttribute (String.valueOf(id), "label", name);
		Cytoscape.getNodeAttributes().setAttribute (String.valueOf(id), "color", rgb);
		Cytoscape.getNodeAttributes().setAttribute (String.valueOf(id), "shape", nodeViewShape);
		
//		setNodeDoubleProperty(int node_index, int property, double value) 
//		cyNetworkView.setNodeIntProperty(arg0, arg1, arg2)
		
		return id;
	}

	public Node getNode(int id){
		return getCyNetwork().getNode(id);
	}
	public Edge getEdge(int id){
		return getCyNetwork().getEdge(id);
	}
	public CyNode getCyNode(int id){
		return (CyNode) getCyNetwork().getNode(id);
	}
	public CyEdge getCyEdge(int id){
		return (CyEdge) getCyNetwork().getEdge(id);
	}
	
	
	
	// getter setter
	public CyNetwork getCyNetwork() {
		return cyNetwork;
	}

	public CyNetworkView getCyNetworkView() {
		return cyNetworkView;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}


	public int getWidth() {
		return width;
	}

	public void setCyNetwork(CyNetwork cyNetwork) {
		this.cyNetwork = cyNetwork;
	}

	public void setCyNetworkView(CyNetworkView cyNetworkView) {
		this.cyNetworkView = cyNetworkView;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
