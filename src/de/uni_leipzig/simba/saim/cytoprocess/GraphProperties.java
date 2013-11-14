package de.uni_leipzig.simba.saim.cytoprocess;

import java.util.Vector;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

import com.mxgraph.view.mxGraph;
/**
 * @author rspeck
 * @author Klaus Lyko
 */
public class GraphProperties {
	private static transient final Logger LOGGER = Logger.getLogger(GraphProperties.class);

	private mxGraph graph;
	private Graph gModel;
	private final String title;

	private int width, height;

	private Random rand = new Random();

	public List<Integer> idsToUpdate = new Vector<Integer>();

	public GraphProperties(Graph gModel, mxGraph graph, final String p_title) {
		if(LOGGER.isDebugEnabled()) LOGGER.debug("GraphProperties...");
		this.gModel = new Graph();
		this.graph = graph;
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
			System.out.println("Graphproperties.createEdge(): Created edge:"+e);
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
		LOGGER.debug("addNode to cytoscape...name"+name);
		System.out.println("Graphproperties.addNode:"+name+", x="+x+", y="+y+" nodeviewShape="+nodeViewShape+", rgb="+rgb);
		// search for a free node
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		
		try {
			Integer id = rand.nextInt(100);
			while(gModel.hasNode(id))
				id = rand.nextInt(100);
			ViewNode n = new ViewNode(name, x, y, nodeViewShape, rgb);
			n.id = id;
			gModel.addNode(n);
			Object v1 = graph.insertVertex(parent, id.toString(), n, x, y, 20,
					20, "label="+name+";color="+rgb+";color="+rgb+";shape="+nodeViewShape);
			System.out.println("Graphproperties.addNode:id="+id);
			return id;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			graph.getModel().endUpdate();
		}
	}
	
	public boolean removeNode(int id) {
		return gModel.nodes.remove(getNode(id));
	}
	
	public boolean removeEdge(int id) {
		gModel.edges.remove(id);
		return true;
//		graph.
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
	public mxGraph getNetwork() {
		return graph;
	}
	public Graph getModel(){
		return gModel;
	}

	public void setCyNetwork(mxGraph network) {
		this.graph = network;
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

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
