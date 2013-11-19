package de.uni_leipzig.simba.saim.cytoprocess;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import prefuse.data.Table;
import prefuse.data.Tree;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.VisualItem;

/**
 * @author rspeck
 * @author Klaus Lyko
 */

public class GraphProperties {
	private static transient final Logger LOGGER = Logger.getLogger(GraphProperties.class);

	private Graph gModel;
	private final String title;
	private int rootNode = 0;
	private int width=600;
	private int height=600;
	

	public List<Integer> idsToUpdate = new Vector<Integer>();

	public GraphProperties(Graph gModel, final String p_title) {
		if(LOGGER.isDebugEnabled()) LOGGER.debug("GraphProperties...");
		this.gModel = new Graph();
		title = p_title;
	}
//	public void applyLayoutAlgorithm(final CyLayoutAlgorithm loAlgorithm) {
//		cyNetworkView.applyLayout(loAlgorithm);
//
//		// fit to view
//		int margin = 0;
//		Object o = Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().get(VisualPropertyType.NODE_SIZE);
//		if(o instanceof Double)
//			margin = ((Double)o).intValue()/2;
//		if(o instanceof Integer)
//			margin = ((Integer)o)/2;
//
//		int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
//
//		int[] ids = getCyNetwork().getNodeIndicesArray();
//		for(int id : ids){
//
//			int x = Double.valueOf(getCyNetworkView().getNodeView(id).getXPosition()).intValue();
//			int y = Double.valueOf(getCyNetworkView().getNodeView(id).getYPosition()).intValue();
//
//			if (x > maxX)	maxX = x;
//			if (x < minX) 	minX = x;
//			if (y > maxY) 	maxY = y;
//			if (y < minY) 	minY = y;
//		}
//		int viewWidth  = maxX - minX;
//		int viewHeight = maxY - minY;
//
//		for(int id : ids){
//			if(viewWidth > 0){
//				float d = Double.valueOf(getCyNetworkView().getNodeView(id).getXPosition()).intValue() - minX;
//				if(d == 0)
//					d = 0.1f;
//				getCyNetworkView().getNodeView(id).setXPosition(( d / viewWidth  * (width - 2 * margin)) + margin);
//			}
//
//			if(viewHeight > 0){
//				float d = Double.valueOf(getCyNetworkView().getNodeView(id).getYPosition()).intValue() - minY;
//				if(d == 0)
//					d = 0.1f;
//				getCyNetworkView().getNodeView(id).setYPosition(( d / viewHeight * (height - 2 * margin)) + margin);
//			}
//		}
//	}

	/**
	 * Adds an edge.
	 * @param
	 * @return
	 */
	public Integer addEdge(int nodeAid, int nodeBid, String name) {
		if(nodeAid != nodeBid){
			if(!gModel.hasNode(nodeAid)) {
				System.out.println("1stNode for edge doesn't exist yet");
				gModel.addNode(new ViewNode(nodeAid));
			}
			if(!gModel.hasNode(nodeBid)) {
				System.out.println("2ndNode for edge doesn't exist yet");
				gModel.addNode(new ViewNode(nodeBid));
			}
			Edge e = gModel.createEdge(nodeAid, nodeBid);
			e.name = name;
//			System.out.println("Graphproperties.createEdge(): Created edge:"+e);
				return e.id;//gModel.getEdgeId(e);

		}else {
			if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge failed: nodeAid == nodeBid");
			System.out.println("addEdge failed: nodeAid == nodeBid");
		}
		return null;
	}

	/**
	 * Adds a node. Node with same label are allowed.
	 * @param name node label
	 * @param x position
	 * @param y	position
	 * @param shape node type
	 * @return node id
	 */
	public Integer addNode(final String name, final int x, final int y, int nodeViewShape,String rgb) {
		LOGGER.debug("addNode...name="+name);
//		System.out.println("Graphproperties.addNode:"+name+", x="+x+", y="+y+" nodeviewShape="+nodeViewShape+", rgb="+rgb);
		// search for a free node
//		graph.getModel().beginUpdate();
		Integer id = 0;//rand.nextInt(100);
		while(gModel.hasNode(id))
			id++;
		ViewNode n = new ViewNode(name, x, y, nodeViewShape, rgb);
		n.id = id;
		gModel.addNode(n);
		return id;
	}
	
	public boolean removeNode(int id) {
		return gModel.nodes.remove(getNode(id));
	}
	
	public boolean removeEdge(int id) {
		gModel.edges.remove(id);
		return true;
	}

	public ViewNode getNode(int id){
		return gModel.getNode(id);
	}
	public Edge getEdge(int id) {
		return gModel.getEdge(id);
	}
	
	public Edge getEdge(ViewNode n1, ViewNode n2) {
		for(Edge e : gModel.edges) {
			if(e.nodeA.id == n1.id && e.nodeB.id ==n2.id)
				return e;
		}
		return null;
	}

//	// getter setter
//	public mxGraph getNetwork() {
//		return graph;
//	}
	public Graph getModel(){
		return gModel;
	}

//	public void setCyNetwork(mxGraph network) {
//		this.graph = network;
//	}


	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}


	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public prefuse.data.Tree getPrefuseData(int root) {
		Table nodes = new Table();
		nodes.addColumn("nodeId", int.class);
		nodes.addColumn("name", String.class);
		for(ViewNode n : this.getModel().nodes) {
			int row = nodes.addRow();
			nodes.set(row, "nodeId", n.id);
			nodes.set(row, "name", n.name);
//			System.out.println("added None "+n.id+", name="+n.name);
		}
		Table edges = new Table();
		edges.addColumn("nodeAid", int.class);
		edges.addColumn("nodeBid", int.class);
		for(Edge e : this.getModel().edges) {
			int row = edges.addRow();
			edges.set(row, "nodeAid", e.nodeA.id);
			edges.set(row, "nodeBid", e.nodeB.id);
		}
		Tree tree = new Tree(nodes, edges, "nodeId", "nodeAid", "nodeBid"); 
//		System.out.println(tree.getRoot());
		return tree;
	}
	
	public void updateTreeLayout() {
//		if(gModel.nodes.size()<=1)
//			return;
	
		Tree tree = getPrefuseData(this.rootNode);
		Dimension inner = new Dimension(width, height);
		Dimension outer = new Dimension(width, height);
		DefaultTreeView treeview = new DefaultTreeView(tree, "name", inner);
		
//		JPanel jf = new JPanel();
//		jf.setSize(outer);
//		jf.setMinimumSize(outer);
//		jf.setMaximumSize(outer);
//		jf.setPreferredSize(outer);
//		jf.add(treeview);
//		jf.paintImmediately(0, 0, jf.getWidth(),  jf.getHeight());
//		jf.addNotify();
//		System.out.println("JPANEL isDisplayable()?"+jf.isDisplayable());
//		System.out.println("Treeview isDisplayable()?"+treeview.isDisplayable());
//		BufferedImage bi = new BufferedImage(600,600,BufferedImage.TYPE_INT_RGB);
		try {
//			jf.paint(bi.getGraphics());
	    	TupleSet set = treeview.getVisualization().getGroup("tree.nodes");
	    	Iterator it = set.tuples();
	    	while(it.hasNext()) {
	    		VisualItem item = (VisualItem) it.next();
	    		Integer nodeID = (Integer) item.get("nodeId");
	    		if(nodeID != null) {
	    			ViewNode vn = gModel.getNode(nodeID);
	    			vn.x = (int) item.getEndX();
	    			vn.y = (int) item.getEndY();
	    		}	    		
	    	}
		} catch(NullPointerException e) {
			System.out.println("NullPoniter by creating BufferedImage bi...");
			System.out.println("GraphicsEnvironment.isHeadless() "+GraphicsEnvironment.isHeadless() );
		}
    }
}
