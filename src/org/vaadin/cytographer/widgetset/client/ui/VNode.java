/* 
 * Copyright 2011 Johannes Tuikkala <johannes@vaadin.com>
 *                           LICENCED UNDER
 *                  GNU LESSER GENERAL PUBLIC LICENSE
 *                     Version 3, 29 June 2007
 */
package org.vaadin.cytographer.widgetset.client.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.vaadin.cytographer.widgetset.client.ui.shap.VCycle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VDiamond;
import org.vaadin.cytographer.widgetset.client.ui.shap.VRectangle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VTriangle;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
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
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VNode extends Group implements ContextListener, MouseDownHandler, MouseUpHandler, MouseMoveHandler, ClickHandler {

	private final VCytographer cytographer;
	private Map<String, Command> commandMap;

	private final VGraph graph;
	private float x, y;

	private Shape shape;
	private final Text textShape;
	private final String name;

	private boolean textsVisible = true;
	private String originalFillColor;

	public VNode(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final VVisualStyle style) {
		super();
		this.cytographer = cytographer;
		this.graph = graph;
		this.shape = shape;
		this.name = name;
				
		textShape = new Text(shape.getX()-name.length()/2,shape.getY()-name.length()/2, name.substring(2));
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

	public static void setStyleToVNode(VNode vNode,final VVisualStyle style){
		
		vNode.setLabelColor(style.getNodeLabelColor());
		vNode.setFontSize(style.getNodeFontSize());
		vNode.setFontFamily(style.getFontFamily());
		vNode.setTextVisible(style.isTextsVisible());
	}
	public static void setStyleToShape(Shape shape,final VVisualStyle style){
		//shape.setFillColor(style.getNodeFillColor());
		shape.setStrokeColor(style.getNodeBorderColor());
		shape.setStrokeWidth(style.getNodeBorderWidth());
	}

	public static VNode createANode(
			final UIDL child, final VCytographer cytographer, final VGraph graph, 
			final String nodeName, final boolean firstNode, final VVisualStyle style) {

		// make node 
		int x,y;
		if (firstNode) {
			x = child.getIntAttribute("node1x");
			y = child.getIntAttribute("node1y");
		} else {
			x = child.getIntAttribute("node2x");
			y = child.getIntAttribute("node2y");
		}	
		Shape shape = VNode.getShape(x,y,style,nodeName.charAt(0));

		// create a new node
		final VNode node = new VNode(cytographer, graph, shape, nodeName,style);

		// node specific styles
//		if (child.hasAttribute("_n1bc")) {
//			shape.setStrokeColor(child.getStringAttribute("_n1bc"));
//		}
//		if (child.hasAttribute("_n1fc")) {
//			shape.setFillColor(child.getStringAttribute("_n1fc"));
//			node.setOriginalFillColor(shape.getFillColor());
//		}
//		if (child.hasAttribute("_n1bw")) {
//			shape.setStrokeWidth(child.getIntAttribute("_n1bw"));
//		}
//		if(shape instanceof Circle  )
//			if (child.hasAttribute("_n1s")) 
//				((Circle)shape).setRadius(child.getIntAttribute("_n1s") / 2);

		return node;
	}
	
	/** create mouse click random node	 */
	public static VNode createANode(final float x, final float y, final VCytographer cytographer, final VGraph graph, final VVisualStyle style) {

		final VNode node = new VNode(
				cytographer, 
				graph,
				VNode.getShape((int)x,(int)y, style, 'c'), 
				"tmp" + new Random().nextInt(1000000),
				style
				);
		
		node.setOriginalFillColor(style.getNodeFillColor());

		return node;
	}
	/** get the nodes shape */
	public static Shape getShape(int x , int y, final VVisualStyle style,char kind){
		
		Shape shape = null;
		switch(kind){
		case 't' : shape = new VTriangle(x,y,style.getNodeSize());break;
		case 'r' : shape = new VRectangle(x,y,style.getNodeSize());break;
		case 'd' : shape = new VDiamond(x,y,style.getNodeSize());break;
		default:
		case 'c' : shape = new VCycle(x, y, style.getNodeSize());break;
		}
		//style
		VNode.setStyleToShape(shape, style);

		return shape;
	}
	public void refreshNodeData(final UIDL child, final VVisualStyle style) {

		VNode.setStyleToShape(shape, style);
		VNode.setStyleToVNode(this,style);

		// node specific styles
//		if (child.hasAttribute("_n1bc")) {
//			shape.setStrokeColor(child.getStringAttribute("_n1bc"));
//		}
//		if (child.hasAttribute("_n1fc")) {
//			shape.setFillColor(child.getStringAttribute("_n1fc"));
//			setOriginalFillColor(shape.getFillColor());
//		}
//		if (child.hasAttribute("_n1bw")) {
//			shape.setStrokeWidth(child.getIntAttribute("_n1bw"));
//		}
//		if(shape instanceof Circle  )
//			if (child.hasAttribute("_n1s")) {
//				((Circle) shape).setRadius(child.getIntAttribute("_n1s") / 2);
//			}
	}
	@Override
	protected Class<? extends VectorObject> getType() {
		return Group.class;
	}

	public void setView(final Shape view) {
		this.shape = view;
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
		if (!visible && textsVisible) {
			remove(textShape);
		} else if (visible && !textsVisible) {
			add(textShape);
		}
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
		if (cytographer.isOnLink()) {
			cytographer.constructLinkTo(this);
		} else {
			graph.setNodeSelected((VNode) event.getSource(), !graph.getSelectedShapes().contains(event.getSource()));
			cytographer.nodeOrEdgeSelectionChanged();
		}
	}

	@Override
	public void onMouseMove(final MouseMoveEvent event) {
	}

	@Override
	public void onMouseUp(final MouseUpEvent event) {
		graph.setMovedShape(null);
	}

	@Override
	public void onMouseDown(final MouseDownEvent event) {
		if (event.getNativeEvent().getButton() == NativeEvent.BUTTON_RIGHT) {
			VConsole.log("rightClick");
			final VContextMenu menu = new VContextMenu(VNode.this);
			menu.showMenu(event.getClientX(), event.getClientY());
			cytographer.setCurrentMenu(menu);
		} else {
			graph.setMovedShape(this);
		}
		event.stopPropagation();
	}

	@Override
	public void initCommands(final VContextMenu menu) {
		commandMap = new HashMap<String, Command>();
		//		final Command editCommand = menu.new ContextMenuCommand() {
		//			@Override
		//			public void execute() {
		//				super.execute();
		//				cytographer.editNode(VNode.this);
		//			}
		//		};
		final Command linkCommand = menu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
				cytographer.startLinkingFrom(VNode.this);
			}
		};
		final Command delCommand = menu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
				cytographer.deleteNode(VNode.this, true);
			}
		};

		commandMap.put("Link to", linkCommand);
		commandMap.put("Delete", delCommand);
	}

	@Override
	public Command[] getCommands() {
		return commandMap.values().toArray(new Command[2]);
	}

	@Override
	public String getCommandName(final Command command) {
		for (final Map.Entry<String, Command> entry : commandMap.entrySet()) {
			if (entry.getValue().equals(command)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
