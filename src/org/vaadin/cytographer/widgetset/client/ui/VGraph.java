package org.vaadin.cytographer.widgetset.client.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vaadin.cytographer.widgetset.client.ui.node.VOperator;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VGraph extends VectorObject {

	private Map<String, VEdge> edges = new HashMap<String, VEdge>();
	private Map<String, VNode> nodes = new HashMap<String, VNode>();
	private Map<VNode, Set<VEdge>> shapeToEdgesMap = new HashMap<VNode, Set<VEdge>>();
	
	private final Set<VNode> paintedShapes = new HashSet<VNode>();
	private final Set<VNode> selectedShapes = new HashSet<VNode>();
	private final Set<VEdge> selectedEdges = new HashSet<VEdge>();

	private VNode movedShape = null;
	private final VVisualStyle vVisualStyle;
	private final VFocusDrawingArea vFocusDrawingArea;
	private final VCytographer vCytographer;

	private final int gwidth;
	private final int gheight;

	public VGraph(final VCytographer cg, final VVisualStyle s, final VFocusDrawingArea c, final int w, final int h) {
		
		vCytographer = cg;
		vVisualStyle = s;
		vFocusDrawingArea = c;
		gwidth = w;
		gheight = h;
	}

	public void repaintGraph(final UIDL uidl) {
		parseGraphFromUIDL(uidl, vVisualStyle);
		paintGraph();
	}

	public void refreshGraphFromUIDL(final UIDL uidl) {
		
		VConsole.log("VGraph.refreshGraphFromUIDL() ...");
		
		for (int i = 0; i < uidl.getChildCount(); i++) {
			final UIDL child = uidl.getChildUIDL(i);
			final String name = child.getStringAttribute("name");
			if (name == null || name.isEmpty()) {
				continue;
			}
			final String node1id = child.getStringAttribute("node1");
			final String node2id = child.getStringAttribute("node2");

			
			final VNode node1 = nodes.get(node1id);
			VNode node2 = null;
			if(node2id != null)
				node2 = nodes.get(node2id);

			node1.refreshNodeData(child, vVisualStyle);
			
			if(node2 != null)
				node2.refreshNodeData(child, vVisualStyle);

			final VEdge edge = edges.get(name);
			if(edge != null)
				edge.refreshEdgeData(child, vVisualStyle);
		}
		paintGraph();
	}

	public void parseGraphFromUIDL(final UIDL uidl, final VVisualStyle style) {
		
		VConsole.log("VGraph.parseGraphFromUIDL() ...");
		
		edges = new HashMap<String, VEdge>();
		nodes = new HashMap<String, VNode>();
		shapeToEdgesMap = new HashMap<VNode, Set<VEdge>>();

		for (int i = 0; i < uidl.getChildCount(); i++) {
			final UIDL child = uidl.getChildUIDL(i);
			
			final String name = child.getStringAttribute("name");
			if (name == null || name.isEmpty()) 
				continue;
			
			final String node1id = child.getStringAttribute("node1");
			final String node2id = child.getStringAttribute("node2");
			
			final String node1Name  = child.getStringAttribute("node1name");
			final String node2Name  = child.getStringAttribute("node2name");
			
			VNode node1 = nodes.get(node1id);
			VNode node2 = null;
			if(node2id!=null)
				node2 = nodes.get(node2id);

			final String shape1 = child.getStringAttribute("shape1");
			final String shape2 = child.getStringAttribute("shape2");

			if (node1 == null && node1id != null) {
				node1 = VNode.create(child, vCytographer, this, node1Name,node1id, true, style,shape1);
				updateEdges(node1, false);
				nodes.put(node1id, node1);
			}
			if (node2 == null && node2id != null) {
				node2 = VNode.create(child, vCytographer, this, node2Name,node2id, false, style,shape2);
				updateEdges(node2, false);
				nodes.put(node2id, node2);
			}
			if (node1 != null && node2 != null) {
				final VEdge edge = VEdge.createAnEdge(child, vCytographer, this, name, node1, node2, style);
				createEdgeConnections(edge);
				edges.put(name, edge);
			}		
			
			String smeta1 = child.getStringAttribute("meta1");			
			if(smeta1!=null){
				String[] meta1 = smeta1.substring(1, child.getStringAttribute("meta1").length()-1).split(",");
				for(int ii=0;ii<meta1.length;ii++)
					meta1[ii]=meta1[ii].trim();
				
				if(node1 != null && shape1.equals("OPERATOR")){
					VOperator vp = (VOperator)node1;
					vp.updateValues(meta1[0],meta1[1]);
				}
			}
			
			String smeta2 = child.getStringAttribute("meta2");
			if(smeta2!=null){
				String[] meta2 = smeta2.substring(1, child.getStringAttribute("meta2").length()-1).split(",");
				for(int ii=0;ii<meta2.length;ii++)
					meta2[ii]=meta2[ii].trim();	

				if( node2 != null && shape2.equals("OPERATOR")){
					VOperator vp = (VOperator)node2;
					vp.updateValues(meta2[0],meta2[1]);				
				}
			}
		}
		paintGraph();
	}

	public void paintGraph(final Shape... updatedShapes) {
		
		VConsole.log("VGraph.paintGraph() ...");
		
		if (updatedShapes == null || updatedShapes.length == 0) {
			vFocusDrawingArea.clear();
			paintedShapes.clear();
			final Rectangle bg = new Rectangle(0, 0, gwidth * 2, gheight);
			bg.setFillColor(vVisualStyle.getBgColor());
			bg.setStrokeColor(vVisualStyle.getBgColor());
			vFocusDrawingArea.add(bg);
			for (final Map.Entry<String, VEdge> entry : edges.entrySet()) {
				final VEdge edge = entry.getValue();
				vFocusDrawingArea.add(edge);
				if (vVisualStyle.isTextsVisible()) {
					vFocusDrawingArea.add(edge.getText());
				}
			}
			for (final Map.Entry<String, VEdge> entry : edges.entrySet()) {
				final VEdge edge = entry.getValue();
				final VNode n1 = edge.getSecondNode();
				if (!paintedShapes.contains(n1) && isInPaintedArea(n1)) {
					vFocusDrawingArea.add(n1);
					paintedShapes.add(n1);
				}
				final VNode n2 = edge.getFirstNode();
				if (!paintedShapes.contains(n2) && isInPaintedArea(n1)) {
					vFocusDrawingArea.add(n2);
					paintedShapes.add(n2);
				}
			}
			// paint nodes that doesn't have edges
			for (final Map.Entry<String, VNode> entry : nodes.entrySet()) {
				final VNode n = entry.getValue();
				if (!paintedShapes.contains(n) && isInPaintedArea(n)) {
					vFocusDrawingArea.add(n);
					paintedShapes.add(n);
				}
			}
		} else {
			for (final Shape s : updatedShapes) {
				vFocusDrawingArea.remove(s);
				vFocusDrawingArea.add(s);
			}
		}
	}

	public void updateGraphProperties(final VVisualStyle style) {
		for (final VNode n : getNodes().values()) {
			n.setTextVisible(style.isTextsVisible());
			n.setRadius(style.getNodeSize());
			updateEdges(n, true);
		}
	}
	public void moveGraph(final float x, final float y) {
		
		VConsole.log("VGraph.moveGraph() ...");
		
		for (final VNode vnode : getPaintedShapes()) {
			vnode.moveNode(vnode.getX()-x,vnode.getY()- y);
			updateEdges(vnode, false);
		}
		paintGraph();
	}

	public void updateEdges(final VNode node, final boolean repaint) {
		final Set<VEdge> edgs = shapeToEdgesMap.get(node);

		if (edgs == null || edgs.isEmpty()) {
			// single nodes
			if (paintedShapes.contains(node)) 
				vFocusDrawingArea.remove(node);
			vFocusDrawingArea.add(node);
			paintedShapes.add(node);
			return;
		}
		for (final VEdge e : edgs) {
			// update edge positions
			if (e.getFirstNode().equals(node)) {
				e.setX1((int) node.getX());
				e.setY1((int) node.getY());
				e.getText().setX((int) (node.getX() + e.getSecondNode().getX()) / 2);
				e.getText().setY((int) (node.getY() + e.getSecondNode().getY()) / 2);
			} else {
				e.setX2((int) node.getX());
				e.setY2((int) node.getY());
				e.getText().setX((int) (e.getFirstNode().getX() + node.getX()) / 2);
				e.getText().setY((int) (e.getFirstNode().getY() + node.getY()) / 2);
			}
			if (repaint) 
				updateEdgeIntoCanvas(e, node, false);
		}
		if (movedShape != null) {
			vFocusDrawingArea.remove(movedShape);
			vFocusDrawingArea.add(movedShape);
		}
	}

	public void updateEdgeIntoCanvas(final VEdge e, final VNode node, final boolean bothNodes) {
		
		vFocusDrawingArea.remove(e);
		vFocusDrawingArea.remove(e.getText());
		vFocusDrawingArea.add(e);
		if (vVisualStyle.isTextsVisible()) 
			vFocusDrawingArea.add(e.getText());

		if (!bothNodes) {
			if (e.getFirstNode().equals(node)) {
				vFocusDrawingArea.remove(e.getSecondNode());
				vFocusDrawingArea.add(e.getSecondNode());
			} else {
				vFocusDrawingArea.remove(e.getFirstNode());
				vFocusDrawingArea.add(e.getFirstNode());
			}
		} else {
			vFocusDrawingArea.remove(e.getSecondNode());
			vFocusDrawingArea.remove(e.getFirstNode());
			vFocusDrawingArea.add(e.getSecondNode());
			vFocusDrawingArea.add(e.getFirstNode());
		}
	}

	public Map<String, VNode> getNodes() {
		return nodes;
	}

	private boolean isInPaintedArea(final VNode n1) {
		// TODO
		return true;
	}

	public Set<VNode> getSelectedShapes() {
		return selectedShapes;
	}

	public Set<VEdge> getSelectedEdges() {
		return selectedEdges;
	}

	public Set<VNode> getPaintedShapes() {
		return paintedShapes;
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Group.class;
	}

	public void setEdgeSelected(final VEdge edge, final boolean selected) {
		if (selected) {
			edge.setStrokeColor(vVisualStyle.getEdgeSelectionColor());
			getSelectedEdges().add(edge);
		} else {
			edge.setStrokeColor(edge.getOrginalStrokeColor());
			getSelectedEdges().remove(edge);
		}
	}

	public void setNodeSelected(final VNode node, final boolean selected) {
		if (selected) {
			VConsole.log("VGraph.setNodeSelected() add ...");
			getSelectedShapes().add(node);
		} else {
			VConsole.log("VGraph.setNodeSelected() remove ...");
			getSelectedShapes().remove(node);
		}
	}

	public void setMovedShape(final VNode vNode) {
		movedShape = vNode;
	}

	public VNode getMovedShape() {
		return movedShape;
	}

	public void addNode(final VNode node) {
		vFocusDrawingArea.add(node);
		nodes.put(node.getID().toString(), node);
		paintedShapes.add(node);
	}

	public void addEdge(final VEdge edge) {
		vFocusDrawingArea.add(edge);
		createEdgeConnections(edge);
		edges.put(edge.getName(), edge);
		updateEdgeIntoCanvas(edge, null, true);
	}

	
	private void createEdgeConnections(final VEdge vedge,final VNode vnode){
		Set<VEdge> e = shapeToEdgesMap.get(vnode);
		if (e == null) {
			e = new HashSet<VEdge>();
			shapeToEdgesMap.put(vnode, e);
		}  
		e.add(vedge);
	}
	
	private void createEdgeConnections(final VEdge edge) {
		createEdgeConnections(edge,edge.getFirstNode());
		createEdgeConnections(edge,edge.getSecondNode());
	}
	

	public void removeNode(final VNode node) {
		
		VConsole.log("VGraph.removeNode");	
		
		vFocusDrawingArea.remove(node);
		paintedShapes.remove(node);
		if (nodes.remove(node.getID().toString()) == null) 
			VConsole.log("node not found" + node.getName());		
		selectedShapes.remove(node);

		final Set<VEdge> edgs = shapeToEdgesMap.get(node);
		for (final VEdge edge : edgs) {
			if (edges.remove(edge.getName()) == null) {
				VConsole.log("edge not found " + edge.toString());
			}
			vFocusDrawingArea.remove(edge);
			selectedEdges.remove(edge);
		}
		if (shapeToEdgesMap.remove(node) == null) 
			VConsole.log("edgeset not found" + node.getName());		
	}

	public void removeEdge(final VEdge edge) {
		vFocusDrawingArea.remove(edge);
		edges.remove(edge.getName());
		selectedEdges.remove(edge);
		removeEdgeFromMap(edge, edge.getFirstNode());
		removeEdgeFromMap(edge, edge.getSecondNode());
	}

	private void removeEdgeFromMap(final VEdge edge, final VNode node) {
		if (node != null) {
			final Set<VEdge> vedges = shapeToEdgesMap.get(node);
			if (vedges != null) 
				vedges.remove(edge);
		}
	}
}
