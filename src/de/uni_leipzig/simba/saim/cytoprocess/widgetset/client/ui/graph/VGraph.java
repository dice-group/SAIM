package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vaadin.contrib.processing.svg.gwt.client.ProcessingJs;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Command;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.VCytoprocess;
/**
 * 
 * @author rspeck
 *
 */
public class VGraph {

	private VVisualStyle vvisualStyle;
	private VCytoprocess vcytoprocess;
	/* id to edge */
	private Map<Integer, VEdge> edges = new HashMap<Integer, VEdge>();
	/* id to node */
	private Map<Integer, VNode> nodes = new HashMap<Integer, VNode>();
	public boolean LOG = false;

	/**
	 * 
	 * @param VCytoprocess
	 */
	public VGraph(VCytoprocess vcypro) {
		VConsole.log("VGraph constructor ...");
		vcytoprocess = vcypro;
		vvisualStyle = new VVisualStyle();
	}

	public void deleteNode(int id) {
		VNode node = nodes.get(id);
		nodes.remove(node);
		node.delete(vcytoprocess);
	}

	public void deleteEdge(int id) {
		VEdge edge = edges.get(id);
		edges.remove(edge);
		edge.delete(vcytoprocess);
	}

	public void updateEdgePositions() {
		for (VEdge edge : edges.values())
			edge.updatePosition();
	}

	public void updateNodePositions() {
		for (VNode node : nodes.values())
			node.updatePosition();
	}

	public void updatePositions() {
		updateEdgePositions();
		updateNodePositions();
	}

	public void moveNode(int id, int x, int y) {
		VNode node = nodes.get(id);
		node.move(x, y);
		updateEdgePositions();
	}

	public void nodeBringToFront(int id) {
		VNode node = nodes.get(id);
		node.bringToFront(vcytoprocess.getCanvas());
	}

	// parse
	/** update node values */
	public void parseUIDLtoAddNode(final UIDL child) {

	}

	/** update node values */
	public void parseUIDLtoUpdateNode(final UIDL child) {
		if (LOG)
			VConsole.log("parseUIDLtoUpdateNode ... ");

		final Iterator<Object> cytoprocessIter = child.getChildIterator();
		while (cytoprocessIter.hasNext()) {
			UIDL cytoChild = (UIDL) cytoprocessIter.next();
			if (cytoChild.getTag().equals("updateNode")) {

				int id = cytoChild.getIntAttribute("nodeID");
				updateNodeValues(cytoChild, nodes.get(id));
			}
		}
		if (LOG)
			VConsole.log("parseUIDLtoUpdateNode done. ");
		
		
		vcytoprocess.requestDone();
	}

	/** delete edge */
	public void parseUIDLtoDeleteEdge(final UIDL child) {
		if (LOG)
			VConsole.log("parseUIDLtoDeleteEdge ... ");

		final Iterator<Object> cytoprocessIter = child.getChildIterator();
		while (cytoprocessIter.hasNext()) {
			UIDL cytoChild = (UIDL) cytoprocessIter.next();
			if (cytoChild.getTag().equals("deleteEdge")) {

				int id = cytoChild.getIntAttribute("edgeID");
				boolean removed = cytoChild.getBooleanAttribute("edgeRemoved");
				if (removed)
					deleteEdge(id);
			}
		}
		if (LOG)
			VConsole.log("parseUIDLtoDeleteEdge done. ");
		
		
	}

	/** refresh node positions */
	public void parseUIDLtoRefreshNodePositions(final UIDL child) {
		if (LOG)
			VConsole.log("parseUIDLtoRefreshNodePositions ...");

		final Iterator<Object> cytoprocessIter = child.getChildIterator();
		while (cytoprocessIter.hasNext()) {
			UIDL cytoChild = (UIDL) cytoprocessIter.next();
			if (cytoChild.getTag().equals("refreshNodePositions")) {

				final Iterator<Object> refreshIter = cytoChild
						.getChildIterator();
				while (refreshIter.hasNext()) {
					UIDL node = (UIDL) refreshIter.next();
					if (node.getTag().equals("node")) {
						int id = node.getIntAttribute("nodeID");
						int x = node.getIntAttribute("nodeX");
						int y = node.getIntAttribute("nodeY");
						VNode n = nodes.get(id);

						if (LOG)
							VConsole.log("node id: " + id);
						if (LOG)
							VConsole.log("nodes : " + nodes.keySet().toString());

						if (n != null)
							n.updatePosition(x, y);
						else if (LOG)
							VConsole.log("ERROR: Node not found!");
					}
				}
			}
		}
		for (VEdge e : edges.values())
			e.updatePosition();

		vcytoprocess.fitShapes();

		if (LOG)
			VConsole.log("parseUIDLtoRefreshNodePositions done.");
		vcytoprocess.requestDone();
	}

	/**
	 * delete node and edges
	 */
	public void parseUIDLtoDeleteNode(final UIDL child) {
		if (LOG)
			VConsole.log("parseUIDLtoDeleteNode ...");

		final Iterator<Object> cytoprocessIter = child.getChildIterator();
		while (cytoprocessIter.hasNext()) {
			UIDL cytoChild = (UIDL) cytoprocessIter.next();
			if (cytoChild.getTag().equals("deleteNode")) {
				int nodeID = cytoChild.getIntAttribute("nodeID");
				boolean removed = cytoChild.getBooleanAttribute("removed");
				if (removed) {

					deleteNode(nodeID);

					if (cytoChild.hasAttribute("edges")) {
						String[] edges = cytoChild.getStringAttribute("edges")
								.replaceAll("\\[", "").replaceAll("\\]", "")
								.split(",");
						for (String edge : edges)
							deleteEdge(Integer.valueOf(edge.trim()));
					}
				}
			}
		}
		if (LOG)
			VConsole.log("parseUIDLtoDeleteNode done.");
		
	}

	/** adds an edge */
	public void parseUIDLtoEdge(final UIDL child) {
		if (LOG)
			VConsole.log("parseUIDLtoEdge ...");

		// for all cytoprocess tags
		final Iterator<Object> cytoprocessIter = child.getChildIterator();
		while (cytoprocessIter.hasNext()) {
			UIDL cytoChild = (UIDL) cytoprocessIter.next();
			if (cytoChild.getTag().equals("addEdge")) {

				int edgeSourceID = cytoChild.getIntAttribute("edgeSourceID");
				int edgeTargetID = cytoChild.getIntAttribute("edgeTargetID");
				VNode source = nodes.get(edgeSourceID);
				VNode target = nodes.get(edgeTargetID);

				createVEdge(cytoChild, source, target);

				source.bringToFront(vcytoprocess.getCanvas());
				target.bringToFront(vcytoprocess.getCanvas());
			}
		}
		if (LOG)
			VConsole.log("parseUIDLtoEdge done.");
		vcytoprocess.requestDone();
	}

	/** parse whole graph */
	public void parseUIDL(final UIDL child) {
		if (LOG)
			VConsole.log("parseUIDL ...");

		// for all cytoprocess tags
		final Iterator<Object> cytoprocessIter = child.getChildIterator();
		while (cytoprocessIter.hasNext()) {
			UIDL cytoChild = (UIDL) cytoprocessIter.next();

			if (cytoChild.getTag().equals("settings")) {
				vvisualStyle.parseUIDL(cytoChild);
				vcytoprocess.setVvisualStyle(vvisualStyle);
			}

			if (cytoChild.getTag().equals("edge")) {
				if (LOG)
					VConsole.log("parseUIDL edge ...");

				UIDL sourceNodeUIDL = cytoChild.getChildByTagName("sourceNode");
				VNode sourceNode = createVNode(sourceNodeUIDL);

				UIDL targetNodeUIDL = cytoChild.getChildByTagName("targetNode");
				VNode targetNode = createVNode(targetNodeUIDL);

				createVEdge(cytoChild, sourceNode, targetNode);

				sourceNode.bringToFront(vcytoprocess.getCanvas());
				targetNode.bringToFront(vcytoprocess.getCanvas());
			}

			if (cytoChild.getTag().equals("node")) {
				if (LOG)
					VConsole.log("parseUIDL node ...");
				createVNode(cytoChild);
			}
						
		}
		
		this.parseUIDLtoDeleteNode(child);
		this.parseUIDLtoDeleteEdge(child);
		if (LOG)
			VConsole.log("parseUIDL done.");
		vcytoprocess.requestDone();
	}

	// private
	private void addEdge(VEdge vedge) {
		edges.put(vedge.id, vedge);

		EdgeHandler eh = new EdgeHandler(vcytoprocess, vedge.id);
		vedge.line.addMouseDownHandler(eh);
		vedge.line.addDoubleClickHandler(eh);
		
		vedge.label.addMouseDownHandler(eh);
		vedge.label.addDoubleClickHandler(eh);
	}

	private void addNode(VNode vnode) {
		nodes.put(vnode.id, vnode);

		NodeHandler nh = new NodeHandler(vcytoprocess, vnode.id);
		vnode.shape.addMouseUpHandler(nh);
		vnode.shape.addMouseMoveHandler(nh);
		vnode.shape.addMouseDownHandler(nh);
		vnode.shape.addClickHandler(nh);
		vnode.shape.addDoubleClickHandler(nh);
		
		vnode.label.addMouseUpHandler(nh);
		vnode.label.addMouseMoveHandler(nh);
		vnode.label.addMouseDownHandler(nh);
		vnode.label.addClickHandler(nh);
		vnode.label.addDoubleClickHandler(nh);
	}

	private VEdge createVEdge(final UIDL nodeChild, VNode src, VNode tar) {

		Integer id = nodeChild.getIntAttribute("edgeID");
		VEdge vedge = edges.get(id);
		if (vedge == null) {
			vedge = VEdge.createVEdge(vvisualStyle, vcytoprocess, src, tar, id,
					nodeChild.getStringAttribute("edgeLabel"),
					nodeChild.getStringAttribute("edgeShape"), true);
			addEdge(vedge);
		} else {
			if (LOG)
				VConsole.log("vedge already exists.");
			updateEdgeValues(nodeChild, vedge);
		}

		return vedge;
	}

	private VNode createVNode(final UIDL nodeChild) {
		if (LOG)
			VConsole.log("createVNode");

		if (!nodes.containsKey(nodeChild.getIntAttribute("nodeID"))) {

			if (LOG) {
				VConsole.log("create new VNode");
				VConsole.log("" + nodeChild.getIntAttribute("nodeX"));
				VConsole.log("" + nodeChild.getIntAttribute("nodeY"));
				VConsole.log("" + nodeChild.getIntAttribute("nodeID"));
				VConsole.log("" + nodeChild.getStringAttribute("nodeLabel"));
				VConsole.log("" + nodeChild.getIntAttribute("nodeShape"));
				VConsole.log("" + nodeChild.getStringAttribute("nodeColor"));
			}

			VNode node = VNode.createVNode(vvisualStyle, vcytoprocess,
					nodeChild.getIntAttribute("nodeX"),
					nodeChild.getIntAttribute("nodeY"),
					nodeChild.getIntAttribute("nodeID"),
					nodeChild.getStringAttribute("nodeLabel"),
					nodeChild.getIntAttribute("nodeShape"),
					nodeChild.getStringAttribute("nodeColor"));

			updateNodeValues(nodeChild, node);

			addNode(node);
			return node;
		} else {
			if (LOG)
				VConsole.log("node already exists.");
			VNode node = nodes.get(nodeChild.getIntAttribute("nodeID"));
			updateNodeValues(nodeChild, node);
			return node;
		}
	}

	private void updateEdgeValues(final UIDL nodeChild, VEdge edge) {
		if (LOG)
			VConsole.log("updateEdgeValues ...");
		edge.label.setText(nodeChild.getStringAttribute("edgeLabel"));
		if (LOG)
			VConsole.log("updateEdgeValues done.");
	}

	private void updateNodeValues(final UIDL nodeChild, VNode node) {
		if (LOG)
			VConsole.log("updateNodeValues ...");

		if (nodeChild.hasAttribute("label1")
				&& nodeChild.hasAttribute("value1"))
			node.addValue1(vcytoprocess,
					nodeChild.getStringAttribute("label1"),
					nodeChild.getDoubleAttribute("value1"));

		if (nodeChild.hasAttribute("label2")
				&& nodeChild.hasAttribute("value2"))
			node.addValue2(vcytoprocess,
					nodeChild.getStringAttribute("label2"),
					nodeChild.getDoubleAttribute("value2"));

		node.updatePosition();

		if (LOG)
			VConsole.log("updateNodeValues done.");
	}
}

/**
 * 
 * @author rspeck
 * 
 */
class EdgeHandler implements ContextListener, MouseDownHandler,
		DoubleClickHandler {

	protected Map<String, Command> commandMap;
	protected VCytoprocess vccytoprocess;
	private int vedgeid;

	public EdgeHandler(VCytoprocess vccytoprocess, int vedgeid) {
		this.vccytoprocess = vccytoprocess;
		this.vedgeid = vedgeid;
	}

	// DoubleClickHandler
	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		vccytoprocess.edgeDoubleClick(vedgeid, event.getClientX(),
				event.getClientY());
	}

	// MouseDownHandler
	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			final VContextMenu menu = new VContextMenu(this);
			menu.showMenu(event.getClientX(), event.getClientY());
		}
	}

	// ContextListener
	@Override
	public Command[] getCommands() {
		return commandMap.values().toArray(new Command[2]);
	}

	@Override
	public String getCommandName(final Command command) {
		for (final Map.Entry<String, Command> entry : commandMap.entrySet())
			if (entry.getValue().equals(command))
				return entry.getKey();
		return null;
	}

	@Override
	public void initCommands(VContextMenu contextMenu) {
		commandMap = new HashMap<String, Command>();
		commandMap.put("Delete", contextMenu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
				vccytoprocess.deleteEdge(vedgeid);
			}
		});
		commandMap.put("Close", contextMenu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
			}
		});
	}
}

/**
 * 
 * @author rspeck
 * 
 */
class NodeHandler implements ContextListener, MouseDownHandler, MouseUpHandler,
		ClickHandler, MouseMoveHandler, DoubleClickHandler {

	protected Map<String, Command> commandMap;
	protected VCytoprocess vccytoprocess;
	private int vnodeid;
	private int moveX = 0, moveY = 0;

	public NodeHandler(VCytoprocess vccytoprocess, int vnodeid) {
		this.vccytoprocess = vccytoprocess;
		this.vnodeid = vnodeid;
	}

	// DoubleClickHandler
	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		vccytoprocess.nodeDoubleClick(vnodeid, event.getClientX(),
				event.getClientY());
	}

	// MouseMoveHandler
	@Override
	public void onMouseMove(MouseMoveEvent event) {

		if ( 
				vccytoprocess.getSelectedObject() == this &&
				vccytoprocess.isMousePressed() && 
				vccytoprocess.mouseButton == ProcessingJs.LEFT
				) {

			int x = event.getClientX();
			int y = event.getClientY();
			vccytoprocess.vgraph.moveNode(vnodeid, moveX - x, moveY - y);

			moveX = x;
			moveY = y;

			event.stopPropagation();
		}
	}

	// MouseUpHandler
	@Override
	public void onMouseUp(MouseUpEvent event) {
		moveX = 0;
		moveY = 0;
		vccytoprocess.setSelectedObject(null);
	}

	// MouseDownHandler
	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			final VContextMenu menu = new VContextMenu(this);
			menu.showMenu(event.getClientX(), event.getClientY());
		}
		if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
			moveX = event.getClientX();
			moveY = event.getClientY();
			vccytoprocess.setSelectedObject(this);
			vccytoprocess.vgraph.nodeBringToFront(vnodeid);
		}
	}

	// ClickHandler
	@Override
	public void onClick(ClickEvent event) {
		if (vccytoprocess.isLinkingTo())
			vccytoprocess.linkTo(vnodeid);
	}

	// ContextListener
	@Override
	public Command[] getCommands() {
		return commandMap.values().toArray(new Command[3]);
	}

	@Override
	public String getCommandName(final Command command) {
		for (final Map.Entry<String, Command> entry : commandMap.entrySet())
			if (entry.getValue().equals(command))
				return entry.getKey();
		return null;
	}

	@Override
	public void initCommands(VContextMenu contextMenu) {
		commandMap = new HashMap<String, Command>();
		commandMap.put("Link to", contextMenu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
				vccytoprocess.setlinkNode(vnodeid);
			}
		});
		commandMap.put("Delete", contextMenu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
				vccytoprocess.deleteNode(vnodeid);
			}
		});
		commandMap.put("Close", contextMenu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
			}
		});
	}

}
