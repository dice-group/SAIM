package org.vaadin.cytographer.widgetset.client.ui;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Circle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VCytographer extends Composite implements 
Paintable, ClickHandler,MouseDownHandler, MouseUpHandler, 
MouseMoveHandler, MouseWheelHandler, KeyDownHandler, KeyUpHandler {
	
	private static final double ZOOM_DOWN = 0.90;
	private static final double ZOOM_UP = 1.10;
	private static final long serialVersionUID = 5554800884802605342L;
	public static final String CLASSNAME = "v-vaadingraph";

	protected String paintableId;
	protected ApplicationConnection applicationConnection;

	private int graphWidth = 500;
	private int graphHeight = 500;

	private final VGraph graph;
	private final FocusPanel panel;
	private final VFocusDrawingArea canvas;
	private final VVisualStyle style = new VVisualStyle();
	private final VSelectionBox selectionBox = new VSelectionBox();

	private Line linkLine;
	private VNode linkNode;
	private VContextMenu currentMenu;

	private float startY;
	private float startX;
	private int zoomFactor = 0;
	private float angle = 0;

	private boolean onMove = false;
	private boolean onLink = false;

	private Set<Integer> currentKeyModifiers;
	private float centerX = graphWidth / 2f;
	private float centerY = graphHeight / 2f;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VCytographer() {
		
		panel = new FocusPanel();
		panel.setSize(graphWidth + "px", graphHeight + "px");
		panel.addKeyDownHandler(this);
		panel.addKeyUpHandler(this);
		
		canvas = new VFocusDrawingArea(this, graphWidth, graphHeight);
		canvas.addKeyDownHandler(this);
		canvas.addKeyUpHandler(this);
		canvas.addMouseMoveHandler(this);
		canvas.addMouseUpHandler(this);
		canvas.addMouseDownHandler(this);
		canvas.addClickHandler(this);
		canvas.addMouseWheelHandler(this);
		
		graph = new VGraph(this, style, canvas, graphWidth, graphHeight);
		panel.add(canvas);
		canvas.add(graph);
		initWidget(panel);
		setStyleName(CLASSNAME);
	}
	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {	
				
		if (client.updateComponent(this, uidl, true)) 
			return;
		
		applicationConnection = client;
		paintableId = uidl.getId();
	
		currentKeyModifiers = new HashSet<Integer>();
		final String operation = uidl.getStringAttribute("operation");

		if ("REPAINT".equals(operation)) 
			repaint(uidl);
		else if ("SET_NODE_SIZE".equals(operation)) {
			style.setNodeSize(uidl.getIntAttribute("ns") / 2);
			graph.updateGraphProperties(style);
//			paintGraph();
		} else if ("SET_VISUAL_STYLE".equals(operation)) {
			graph.updateGraphProperties(style);
//			paintGraph();
		} else if ("SET_TEXT_VISIBILITY".equals(operation)) {
			style.setTextsVisible(uidl.getBooleanAttribute("texts"));
			graph.updateGraphProperties(style);
//			paintGraph();
		} 
//		else if ("SET_OPTIMIZED_STYLES".equals(operation))
//			graph.paintGraph();
//		else if ("UPDATE_NODE".equals(operation)) {
//			graph.updateNode(uidl, uidl.getStringAttribute("node"));
//		}
		else if ("SET_ZOOM".equals(operation)) 
			setZoom(uidl.getIntAttribute("zoom"));
		else if ("REFRESH".equals(operation)) 
			refresh(uidl);
		else 
			repaint(uidl);		
	}

	private void repaint(final UIDL uidl) {
		graphWidth = uidl.getIntAttribute("gwidth");
		graphHeight = uidl.getIntAttribute("gheight");
		centerX = graphWidth / 2f;
		centerY = graphHeight / 2f;
		zoomFactor = 0;
		style.parseGeneralStyleAttributesFromUidl(uidl);
		initializeCanvas();
		graph.repaintGraph(uidl);
		graph.moveGraph(0, 0);
	}

	private void refresh(final UIDL uidl) {
		style.parseGeneralStyleAttributesFromUidl(uidl);
		graph.refreshGraphFromUIDL(uidl);
	}

	private void initializeCanvas() {
		panel.setSize(50 + 1 + graphWidth + "px", 25 + graphHeight + "px");
		canvas.setWidth(graphWidth);
		canvas.setHeight(graphHeight);
		canvas.getElement().getStyle().setPropertyPx("width", graphWidth);
		canvas.getElement().getStyle().setPropertyPx("height", graphHeight);
	}

	@Override
	public void onMouseDown(final MouseDownEvent event) {
		extractSelection();
		removeSelectionBox();
		
		if (currentKeyModifiers.contains(KeyCodes.KEY_CTRL)) {
			selectionBox.setSelectionBoxStartX(event.getX());
			selectionBox.setSelectionBoxStartY(event.getY());
			selectionBox.setSelectionBoxVisible(true);
		} else if (event.getSource() instanceof VNode) {
			onMove = false;
		} else if (event.getSource() instanceof DrawingArea) {
			onMove = true;
			startX = event.getX();
			startY = event.getY();
		} 
	}

	@Override
	public void onMouseMove(final MouseMoveEvent event) {
		final int currentX = event.getX();
		final int currentY = event.getY();

		if (selectionBox.isSelectionBoxVisible()) {
			selectionBox.drawSelectionBox(canvas, currentX, currentY);
		} else if (graph.getMovedShape() != null) {
			graph.getMovedShape().moveNode(currentX, currentY);
		} else if (onLink) {
			if (linkLine != null) 
				canvas.remove(linkLine);
			
			linkLine = getLinkLine(currentX, currentY);
			canvas.add(linkLine);
		} else if (onMove && event.getSource().equals(canvas)) {
			graph.moveGraph(startX - currentX, startY - currentY);
			startX = currentX;
			startY = currentY;
		}
	}

	@Override
	public void onMouseUp(final MouseUpEvent event) {
		extractSelection();
		removeSelectionBox();
		graph.setMovedShape(null);
		onMove = false;
		startX = 0;
		startY = 0;
	}

	private void extractSelection() {
		if (selectionBox.isSelectionBoxVisible()){
			final int x = selectionBox.getSelectionBoxStartX();
			final int y = selectionBox.getSelectionBoxStartY();
			selectNodesAndEdgesInTheBox(x, y, x + selectionBox.getWidth(), y + selectionBox.getHeight());
		}
	}

	private void selectNodesAndEdgesInTheBox(final int startX, final int startY, final int endX, final int endY) {
		for (final VNode node : graph.getSelectedShapes()) 
			graph.setNodeSelected(node, false);
		
		for (final VEdge edge : graph.getSelectedEdges()) 
			graph.setEdgeSelected(edge, false);
		
		for (final VNode node : graph.getPaintedShapes()) 
			if (isInArea(node.getX(), node.getY(), startX, startY, endX, endY)) 
				graph.setNodeSelected(node, true);
			
		for (final VEdge edge : graph.getSelectedEdges()) 
			if (graph.getSelectedShapes().contains(edge.getFirstNode()) && graph.getSelectedShapes().contains(edge.getSecondNode())) 
				graph.setEdgeSelected(edge, true);
		
		nodeOrEdgeSelectionChanged();
	}

	private boolean isInArea(final float targetX, final float targetY, final int x1, final int y1, final int x2, final int y2) {
		return targetX >= x1 && targetX <= x2 && targetY >= y1 && targetY <= y2;
	}

	private void removeSelectionBox() {
		if (selectionBox.isSelectionBoxVisible()) {
			canvas.remove(selectionBox);
			selectionBox.setSelectionBoxVisible(false);
			selectionBox.setSelectionBoxRightHandSide(true);
		}
	}

	private Line getLinkLine(final int x, final int y) {
		final Line linkLine = new Line((int) startX, (int) startY, x, y);
		linkLine.setStrokeColor(style.getEdgeColor());
		linkLine.setStrokeOpacity(0.35);
		return linkLine;
	}

	private void removeLinkLine() {
		if (linkLine != null) {
			canvas.remove(linkLine);
			linkLine = null;
		}
		onLink = false;
	}

	@Override
	public void onClick(final ClickEvent event) {
		removeLinkLine();
		onMove = false;
		removeMenu();
		extractSelection();
		removeSelectionBox();
	}

	public void nodeOrEdgeSelectionChanged() {
		final String[] edges = new String[graph.getSelectedEdges().size()];
		final String[] nodes = new String[graph.getSelectedShapes().size()];
		int i = 0;
		for (final VEdge vedge : graph.getSelectedEdges()) {
			edges[i] = vedge.getName();
			++i;
		}
		i = 0;
		for (final VNode vnode : graph.getSelectedShapes()) {
			nodes[i] = vnode.toString();
			++i;
		}
		
		applicationConnection.updateVariable(paintableId, "selectedEdges", edges, false);		
		applicationConnection.updateVariable(paintableId, "selectedNodes", nodes, true);		
	}

	public void doubleClick(String[] values) {
		applicationConnection.updateVariable(paintableId, "doubleClick", values, true);		
	}

	public void onNodeMouseUp(String[] values){		
		applicationConnection.updateVariable(paintableId, "onNodeMouseUp", values, true);				
	}
	@Override
	public void onMouseWheel(final MouseWheelEvent event) {
		final int delta = event.getDeltaY();
		if (currentKeyModifiers.contains(KeyCodes.KEY_ALT)) {
			if (delta < 0) 
				rotate(0.05);
			else if (delta > 0) 
				rotate(-0.05);
		} else {
			if (delta < 0) 
				zoom(ZOOM_UP);
			 else if (delta > 0) 
				zoom(ZOOM_DOWN);
		}
	}

	private void rotate(final double delta) {
		for (final VNode n : graph.getPaintedShapes()) {
			final float x = (float) ((n.getX() - centerX) * Math.cos(delta) - (n.getY() - centerY) * Math.sin(delta)) + centerX;
			final float y = (float) ((n.getX() - centerX) * Math.sin(delta) + (n.getY() - centerY) * Math.cos(delta)) + centerY;
			n.moveNode(x,y);
			graph.updateEdges(n, false);
		}
		angle += delta;
	}

	private void setZoom(final int newZoomFactor) {
		if (newZoomFactor > zoomFactor) 
			for (int i = 0; i < newZoomFactor - zoomFactor; i++) 
				zoom(ZOOM_UP);			
		else
			for (int i = 0; i < zoomFactor - newZoomFactor; i++) 
				zoom(ZOOM_DOWN);
	}

	private void zoom(final double factor) {
		if (factor < 1)
			zoomFactor--;
		 else 
			zoomFactor++;
		
		for (final VNode n : graph.getPaintedShapes()) {
			
			n.moveNode(((float) ((n.getX() - centerX) * factor) + centerX),((float) ((n.getY() - centerY) * factor) + centerY));
			
			if (n.getView() instanceof Circle) {
				/*
				 * if (delta > 1) { ((Circle) n.getView()).setRadius((((Circle)
				 * n.getView()).getRadius() + 1)); } else { ((Circle)
				 * n.getView()).setRadius((((Circle) n.getView()).getRadius() -
				 * 1)); }
				 */
			}
			graph.updateEdges(n, false);
		}
		applicationConnection.updateVariable(paintableId, "zoomFactor", zoomFactor, true);
	}

	@Override
	public void onKeyDown(final KeyDownEvent event) {
		currentKeyModifiers.add(event.getNativeKeyCode());
	}

	@Override
	public void onKeyUp(final KeyUpEvent event) {
		if (currentKeyModifiers.contains(KeyCodes.KEY_CTRL))
			removeSelectionBox();
		currentKeyModifiers.clear();
	}

	public void removeMenu() {
		if (currentMenu != null) {
			currentMenu.hide();
			currentMenu = null;
		}
	}

	public void setCurrentMenu(final VContextMenu currentMenu) {
		this.currentMenu = currentMenu;
	}

	public VContextMenu getCurrentMenu() {
		return currentMenu;
	}

	public void startLinkingFrom(final VNode node) {
		onLink = true;
		linkNode = node;
		startX = node.getX();
		startY = node.getY();
	}

	public void constructLinkTo(final VNode node2) {		
		final String name = linkNode.getID() + "_to_" + node2.getID() + "_" + new Random().nextInt(1000);
		applicationConnection.updateVariable(paintableId, "edgeCreated", new String[] { linkNode.getID().toString(), node2.getID().toString(), name }, true);
	}

	public boolean isOnLink() {
		return onLink;
	}

	public void deleteSelectedItems() {
		for (final VEdge edge : graph.getSelectedEdges())    deleteEdge(edge, true);
		for (final VNode node : graph.getSelectedShapes())    deleteNode(node, true);
	}

	public void deleteNode(final VNode node, final boolean immediate) {
		applicationConnection.updateVariable(paintableId, "removedNode", node.getID().toString(), immediate);
		graph.removeNode(node);
	}

	private void deleteEdge(final VEdge edge, final boolean immediate) {
		applicationConnection.updateVariable(paintableId, "removedEdge", edge.getName(), immediate);
		graph.removeEdge(edge);
	}
}