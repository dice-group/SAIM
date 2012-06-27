/* 
 * Copyright 2011 Johannes Tuikkala <johannes@vaadin.com>
 *                           LICENCED UNDER
 *                  GNU LESSER GENERAL PUBLIC LICENSE
 *                     Version 3, 29 June 2007
 */
package org.vaadin.cytographer.widgetset.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.cytographer.widgetset.client.ui.node.*;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Command;
import com.vaadin.terminal.gwt.client.UIDL;

public class VNode extends Group 
implements ContextListener, MouseDownHandler, MouseUpHandler, MouseMoveHandler, ClickHandler{

	protected final VCytographer cytographer;
	protected Map<String, Command> commandMap;

	private final VGraph graph;
	private float x, y;

	private Shape shape;
	protected final Text textShape;
	private final String name;
	private Integer id = null;
	
	public Integer getID(){
		return id;
	}

	private boolean textsVisible = true;
	private String originalFillColor;

	public VNode(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String nodeID, final VVisualStyle style) {
		super();
		this.cytographer = cytographer;
		this.graph = graph;
		VNode.setStyleToShape(shape, style);
		this.shape = shape;
		this.name = name;
		this.id = Integer.parseInt(nodeID);		
		textShape = new Text(shape.getX()-name.length()/2,shape.getY()-name.length()/2, name);
		textShape.setStrokeOpacity(0);

		setX(shape.getX());
		setY(shape.getY());

		add(shape);
		add(textShape);		
		addClickHandler(this);
		addMouseDownHandler(this);
		addMouseUpHandler(this);
		addMouseMoveHandler(this);

		VNode.setStyleToVNode(this,style);		
	}
	public static VNode create(
			final UIDL child, final VCytographer cytographer, final VGraph graph, 
			 final String nodeName, final String nodeID,final boolean firstNode, final VVisualStyle style,String shape) {

		// make node 
		int x,y;
		if (firstNode) {
			x = child.getIntAttribute("node1x");
			y = child.getIntAttribute("node1y");
		} else {
			x = child.getIntAttribute("node2x");
			y = child.getIntAttribute("node2y");
		}	
		
		if(shape.equals("METRIC"))
			return new VMetric(cytographer, graph, VMetric.getShape(x,y, style.getNodeSize(),style.getMetric()), nodeName,nodeID, style);
		else if(shape.equals("OPERATOR"))
			return new VOperator(cytographer, graph, VOperator.getShape(x,y, style.getNodeSize(),style.getOperator()), nodeName,nodeID, style);
		else if(shape.equals("SOURCE"))
			return new VSource(cytographer, graph, VSource.getShape(x,y, style.getNodeSize(),style.getSource()), nodeName,nodeID, style);
		else if(shape.equals("TARGET"))
			return new VTarget(cytographer, graph, VTarget.getShape(x,y, style.getNodeSize(),style.getTarget()), nodeName,nodeID, style);
		else if(shape.equals("OUTPUT"))
			return new VOutput(cytographer, graph, VOutput.getShape(x,y, style.getNodeSize(),style.getOutput()), nodeName,nodeID, style);

		else  throw new IllegalStateException("Shape creation failed since shape not found,use:SOURCE,TARGET,METRIC, OUTPUT or OPERATOR.");
	}

	protected static void setStyleToVNode(VNode vNode,final VVisualStyle style){
		
		vNode.setLabelColor(style.getNodeLabelColor());
		vNode.setFontSize(style.getNodeFontSize());
		vNode.setFontFamily(style.getFontFamily());
		vNode.setTextVisible(style.isTextsVisible());
	}
	protected static void setStyleToShape(Shape shape,final VVisualStyle style){
		//shape.setFillColor(style.getNodeFillColor());
		shape.setStrokeColor(style.getNodeBorderColor());
		shape.setStrokeWidth(style.getNodeBorderWidth());
	}

	public void refreshNodeData(final UIDL child, final VVisualStyle style) {
		VNode.setStyleToShape(shape, style);
		VNode.setStyleToVNode(this,style);
	}
	@Override
	protected Class<? extends VectorObject> getType() {
		return Group.class;
	}

	public void setView(final Shape view) {
		shape = view;
	}

	public Shape getView() {
		return shape;
	}

	public String getName() {
		return name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setFillColor(final String color) {
		shape.setFillColor(color);
	}

	public void setLabelColor(final String color) {
		textShape.setFillColor(color);
		textShape.setFillOpacity(1);
	}

	public void setX(final float x) {
		this.x = x;
		shape.setX((int) x);
		// center text
		textShape.setX((int) (x-textShape.getTextWidth()/2));
	}

	public void setY(final float y) {
		this.y = y;
		shape.setY((int) y);
		// center text
		textShape.setY((int) (y+textShape.getTextHeight()/2));
	}

	public void setFontSize(final int nodeFontSize) {
		textShape.setFontSize(nodeFontSize);
	}

	public void setFontFamily(final String family) {
		textShape.setFontFamily(family);
	}

	public void setTextVisible(final boolean visible) {
		if (!visible && textsVisible) 
			remove(textShape);
		else if (visible && !textsVisible)
			add(textShape);
	
		textsVisible = visible;
	}

	public String getOriginalFillColor() {
		return originalFillColor;
	}

	public void setOriginalFillColor(final String originalFillColor) {
		this.originalFillColor = originalFillColor;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setRadius(final int nodeSize) {
		try {
			((Circle) shape).setRadius(nodeSize);
		} catch (final Exception e) {
			shape.setPixelSize(nodeSize, nodeSize);
		}
	}

	public void moveNode(final float x, final float y) {
		setX(x);
		setY(y);
		graph.updateEdges(this, true);

	}

	@Override
	public void onClick(final ClickEvent event) {
		if (cytographer.isOnLink()){
			cytographer.constructLinkTo(this);
		}
		 else {
			graph.setNodeSelected((VNode) event.getSource(), !graph.getSelectedShapes().contains(event.getSource()));
			cytographer.nodeOrEdgeSelectionChanged();
		}
	}
	@Override
	public void onMouseDown(final MouseDownEvent event) {
		
		if (event.getNativeEvent().getButton() == NativeEvent.BUTTON_RIGHT) {
			final VContextMenu menu = new VContextMenu(VNode.this);
			menu.showMenu(event.getClientX(), event.getClientY());
			cytographer.setCurrentMenu(menu);
		} else 
			graph.setMovedShape(this);
		event.stopPropagation();
	}
	@Override
	public void onMouseMove(final MouseMoveEvent event) {
	}

	@Override
	public void onMouseUp(final MouseUpEvent event) {
		graph.setMovedShape(null);
		// update position of node
		cytographer.onNodeMouseUp(new String[]{getID().toString(), String.valueOf(getX()), String.valueOf(getY())});
	}
	@Override
	public void initCommands(final VContextMenu menu) {
		commandMap = new HashMap<String, Command>();
		commandMap.put("Link to",  menu.new ContextMenuCommand() {
			@Override public void execute() {
				super.execute();
				cytographer.startLinkingFrom(VNode.this);
			}
		});		
		commandMap.put("Delete", menu.new ContextMenuCommand() {
			@Override public void execute() {
				super.execute();
				cytographer.deleteNode(VNode.this, true);
			}
		});
		commandMap.put("Close", menu.new ContextMenuCommand(){			
			@Override public void execute(){
				super.execute();
				cytographer.removeMenu();
			}
		});
	}

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
}