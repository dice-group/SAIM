package org.vaadin.cytographer.model;

import giny.model.Edge;
import giny.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import cytoscape.view.CyNetworkView;

public class GraphProperties {
	private static Logger logger = Logger.getLogger(GraphProperties.class);
	static{
		logger.setLevel(Level.OFF);
	}

	private final String title;

	private CyNetwork cyNetwork;
	private CyNetworkView cyNetworkView;

	private final List<Integer> edges, nodes;
	private final Map<String, List<Object>> nodeMetadata = new HashMap<>();
	public enum Shape {
		SOURCE,TARGET, METRIC, OPERATOR
	}
	private Map<Integer, Shape> shapes = new HashMap<Integer, Shape>();

	private final Set<String> selectedNodes = new HashSet<>();
	private final Set<String> selectedEdges = new HashSet<>();

	private final Map<String, Edge> edgeMap = new HashMap<String, Edge>();
	private final Map<Node, List<Edge>> nodeToEdgesMap = new HashMap<>();

	private int width, height, cytoscapeViewWidth, cytoscapeViewHeight;
	
	private int zoomFactor = 0;
	private double nodeSize = -1;

	private int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

	private boolean useFitting = false;
	private boolean textsVisible = false;
	private boolean styleOptimization = false;

	public GraphProperties(final CyNetwork network, final CyNetworkView finalView, final String p_title) {
		cyNetwork = network;
		cyNetworkView = finalView;
		title = p_title;
		edges = new ArrayList<Integer>(Arrays.asList(ArrayUtils.toObject(network.getEdgeIndicesArray())));
		nodes = new ArrayList<Integer>(Arrays.asList(ArrayUtils.toObject(network.getNodeIndicesArray())));
		measureDimensions();
		contructNodeToEdgesMap();
	}
	public void setNodeMetadata(String node, List<Object> data){
		this.nodeMetadata.put(node, data);	
	}
	public List<Object> getNodeMetadata(String node){
		List<Object> value =  nodeMetadata.get(node);
		if(value == null){
			return new ArrayList<>();
		}else return value;	
	}
	
	private void contructNodeToEdgesMap() {
		for (final Integer edgeIndex : edges) {
			final Edge e = cyNetwork.getEdge(edgeIndex);
			addEdgeIntoMap(e.getSource(), e);
			addEdgeIntoMap(e.getTarget(), e);
			edgeMap.put(e.getIdentifier(), e);
		}
	}

	private void addEdgeIntoMap(final Node node, final Edge e) {
		if(logger.isDebugEnabled())
			logger.debug("addEdgeIntoMap:" + node.getIdentifier() + " " + e.getIdentifier());
		
		List<Edge> edges = nodeToEdgesMap.get(node);
		if (edges == null) {
			edges = new ArrayList<Edge>();
			nodeToEdgesMap.put(node, edges);
		}
		edges.add(e);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public double getNodeSize() {
		return nodeSize;
	}

	public void setNodeSize(final double nodeSize) {
		this.nodeSize = nodeSize;
	}

	public boolean isStyleOptimization() {
		return styleOptimization;
	}

	public void setStyleOptimization(final boolean styleOptimization) {
		this.styleOptimization = styleOptimization;
	}

	public String getTitle() {
		return title;
	}

	public CyNetwork getNetwork() {
		return cyNetwork;
	}

	public List<Integer> getEdges() {
		return edges;
	}

	public List<Integer> getNodes() {
		return nodes;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getCytoscapeViewWidth() {
		return cytoscapeViewWidth;
	}

	public void setCytoscapeViewWidth(final int cytoscapeViewWidth) {
		this.cytoscapeViewWidth = cytoscapeViewWidth;
	}

	public int getCytoscapeViewHeight() {
		return cytoscapeViewHeight;
	}

	public void setCytoscapeViewHeight(final int cytoscapeViewHeight) {
		this.cytoscapeViewHeight = cytoscapeViewHeight;
	}

	public boolean isUseFitting() {
		return useFitting;
	}

	public void setFitting(final boolean b) {
		useFitting = b;
	}

	public boolean isTextsVisible() {
		return textsVisible;
	}

	public void setTextVisible(final boolean b) {
		textsVisible = b;
	}

	public void setNetwork(final CyNetwork network) {
		this.cyNetwork = network;
	}

	public void setFinalView(final CyNetworkView finalView) {
		this.cyNetworkView = finalView;
	}

	public void measureDimensions() {
		for (final int ei : edges) {
			final int x1 = (int) cyNetworkView.getNodeView(cyNetwork.getEdge(ei).getSource()).getXPosition();
			final int y1 = (int) cyNetworkView.getNodeView(cyNetwork.getEdge(ei).getSource()).getYPosition();
			
			final int x2 = (int) cyNetworkView.getNodeView(cyNetwork.getEdge(ei).getTarget()).getXPosition();
			final int y2 = (int) cyNetworkView.getNodeView(cyNetwork.getEdge(ei).getTarget()).getYPosition();
			
			if (x1 > maxX)	maxX = x1;
			if (x1 < minX) 	minX = x1;
			if (y1 > maxY) 	maxY = y1;
			if (y1 < minY) 	minY = y1;
			
			if (x2 > maxX) 	maxX = x2;
			if (x2 < minX) 	minX = x2;
			if (y2 > maxY) 	maxY = y2;
			if (y2 < minY)  minY = y2;
		}
		cytoscapeViewWidth = maxX - minX;
		cytoscapeViewHeight = maxY - minY;
	}

	public int getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(final int zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public CyNetworkView getFinalView() {
		return cyNetworkView;
	}

	public Set<String> getSelectedNodes() {
		return selectedNodes;
	}

	public Set<String> getSelectedEdges() {
		return selectedEdges;
	}

	public void addSelectedNode(final String n) {
		selectedNodes.add(n);
	}

	public void addSelectedEdge(final String e) {
		selectedEdges.add(e);
	}

	public void clearSelectedNodes() {
		selectedNodes.clear();
	}

	public void clearSelectedEdges() {
		selectedEdges.clear();
	}

	public Container getNodeAttributeContainerForSelectedNodes() {
		final IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("index", Integer.class, null);
		container.addContainerProperty("identifier", String.class, null);

		for (final Integer nodeIndex : nodes) {
			final Node n = cyNetwork.getNode(nodeIndex);
			for (final String str : selectedNodes) {
				if (str.equals(n.getIdentifier())) {
					final Item i = container.addItem(n);
					i.getItemProperty("index").setValue(nodeIndex);
					i.getItemProperty("identifier").setValue(str);
					break;
				}
			}
		}
		return container;
	}

	public void addANewNode(final String id, final int x, final int y, Shape shape) {
		
		CyNode node = cyNetwork.addNode(Cytoscape.getCyNode(id, true));			
		cyNetworkView.addNodeView(node.getRootGraphIndex()).setXPosition(x);
		cyNetworkView.addNodeView(node.getRootGraphIndex()).setYPosition(y);
		nodes.add(node.getRootGraphIndex());
		
		shapes.put(node.getRootGraphIndex(), shape);
	}
	
	public  Shape getShapes(final String id){
		return shapes.get(Cytoscape.getCyNode(id, false).getRootGraphIndex());
	}

	public void removeNode(final String id) {
		final CyNode node = Cytoscape.getCyNode(id, false);
		if (node != null) {
			final List<Edge> edgs = nodeToEdgesMap.remove(node);
			if (edgs != null) {
				for (final Edge e : edgs) {
					cyNetwork.removeEdge(e.getRootGraphIndex(), true);
					edges.remove(Integer.valueOf(e.getRootGraphIndex()));
					edgeMap.remove(e.getIdentifier());
					selectedEdges.remove(e.getIdentifier());
				}
			}
			cyNetworkView.removeNodeView(node);
			cyNetwork.removeNode(node.getRootGraphIndex(), true);
			selectedNodes.remove(node.getIdentifier());
			nodes.remove(Integer.valueOf(node.getRootGraphIndex()));
		} else {
			throw new IllegalStateException("Node not found " + id);
		}
	}

	public void createAnEdge(String nodeA, String nodeB, String attribute) {
		final CyNode node1 = Cytoscape.getCyNode(nodeA, false);
		final CyNode node2 = Cytoscape.getCyNode(nodeB, false);
		if (node1 != null && node2 != null) {
			final CyEdge edge = Cytoscape.getCyEdge(node1, node2, Semantics.INTERACTION, attribute, true);
			edge.setIdentifier(attribute);
			cyNetwork.addEdge(edge);
			cyNetworkView.addEdgeView(edge.getRootGraphIndex());
			edges.add(edge.getRootGraphIndex());
			edgeMap.put(attribute, edge);
			addEdgeIntoMap(node1, edge);
			addEdgeIntoMap(node2, edge);
		} else {
			throw new IllegalStateException("Edge creation failed since node not found");
		}
	}

	public void removeEdge(final String id) {
		final Edge edge = edgeMap.remove(id);
		edges.remove(Integer.valueOf(edge.getRootGraphIndex()));
		selectedEdges.remove(id);
		
		removeEdgeFromTheMap(edge, edge.getSource());
		removeEdgeFromTheMap(edge, edge.getTarget());
		cyNetworkView.removeEdgeView(edge.getRootGraphIndex());
		cyNetwork.removeEdge(edge.getRootGraphIndex(), true);
	}

	private void removeEdgeFromTheMap(final Edge edge, final Node node) {
		if (node != null) {
			final List<Edge> edgs = nodeToEdgesMap.get(node);
			if (edgs != null) 
				edgs.remove(edge);
		}
	}
}