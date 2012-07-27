package org.vaadin.cytographer.widgetset.client.ui;

import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.impl.util.SVGUtil;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.terminal.gwt.client.UIDL;
//import com.vaadin.terminal.gwt.client.VConsole;

public class VEdge  extends Group implements ClickHandler {

	private final VGraph graph;
	// two nodes
	private final VNode node1, node2;
	// edge text 
	private Shape textShape;
	private final String name;
	private String originalStrokeColor;
	private final VCytographer cytographer;
	
	private Line line = null;
	private Line arrow1 = new Line(0,0,0,0);
	private Line arrow2 = new Line(0,0,0,0);
	
	public Line getLine(){
		return line;
	}
	public Line getArrow1(){
		return arrow1;
	}
	public Line getArrow2(){
		return arrow2;
	}
	public void makeArrow(VNode node2, VNode node1){
	
		final int arrowSize = 5;		
		// middle
		final float toX = (( node1.getX() + node2.getX())/2);
		final float toY = (( node1.getY() + node2.getY())/2);
		// d
		final int dx = (int) (toX - node1.getX());
		final int dy = (int) (toY - node1.getY());		
		
		double hdx = (arrowSize*dx)/Math.sqrt(dx*dx+dy*dy);
		double hdy = (arrowSize*dy)/Math.sqrt(dx*dx+dy*dy);		
		
		arrow1.setX1((int)(toX));               arrow1.setY1((int)(toY));		
		arrow1.setX2((int)(toX-hdx+hdy));       arrow1.setY2((int)(toY-hdy-hdx));		
		arrow2.setX1((int)(toX));               arrow2.setY1((int)(toY));		
		arrow2.setX2((int)(toX-hdx-hdy));       arrow2.setY2((int)(toY-hdy+hdx));	
	}

	public VEdge(final VCytographer cytographer, final VGraph graph, final VNode node1, final VNode node2, final Shape text,final String name) {
		
		line = new Line((int) node1.getX(), (int) node1.getY(), (int) node2.getX(), (int) node2.getY());
		add(line);		
		makeArrow(node1,node2);
		add(arrow1);
		add(arrow2);
		
		this.cytographer = cytographer;
		this.graph = graph;
		this.node1 = node1;
		this.node2 = node2;
		this.textShape = text;
		this.name = name;
		addClickHandler(this);
	}

	public void refreshEdgeData(final UIDL child, final VVisualStyle style) {
		// set edge text
		((Text) textShape).setFontSize(style.getEdgeFontSize());
		((Text) textShape).setFontFamily(style.getFontFamily());
		textShape.setStrokeOpacity(0);
		textShape.setFillOpacity(1);
		textShape.setFillColor(style.getEdgeLabelColor());

		// set edge 
		line.setStrokeColor(style.getEdgeColor());
		arrow1.setStrokeColor(style.getEdgeColor());
		arrow2.setStrokeColor(style.getEdgeColor());
		
		line.setStrokeWidth(style.getEdgeLineWidth());
		arrow1.setStrokeWidth(style.getEdgeLineWidth());
		arrow2.setStrokeWidth(style.getEdgeLineWidth());
		
		setStrokeDashArray(style.getEdgeDashArray());

		// edge specific style attributes
		if (child != null){
			if (child.hasAttribute("_ec")) {
				line.setStrokeColor(child.getStringAttribute("_ec"));
				arrow1.setStrokeColor(child.getStringAttribute("_ec"));
				arrow2.setStrokeColor(child.getStringAttribute("_ec"));
				
				setOriginalStrokeColor(line.getStrokeColor());
			}
			if (child.hasAttribute("_elw")) {
				line.setStrokeWidth(child.getIntAttribute("_elw"));
				arrow1.setStrokeWidth(child.getIntAttribute("_elw"));
				arrow2.setStrokeWidth(child.getIntAttribute("_elw"));
			}
			if (child.hasAttribute("_eda")) 
				setStrokeDashArray(child.getStringAttribute("_eda"));
		}
	}

	public void setStrokeDashArray(final String dasharray) {
		SVGUtil.setAttributeNS(getElement(), "stroke-dasharray", dasharray);
	}
	@Override
	public void onClick(final ClickEvent event) {
		graph.setEdgeSelected((VEdge) event.getSource(), !graph.getSelectedEdges().contains(event.getSource()));
		cytographer.nodeOrEdgeSelectionChanged();
	}
	// static method to make an edge
	public static VEdge createAnEdge(final UIDL child, final VCytographer cytographer, final VGraph graph, final String name, final VNode node1, final VNode node2, final VVisualStyle style) {
	
		final Text text = new Text((int) (node1.getX() + node2.getX()) / 2, (int) (node1.getY() + node2.getY()) / 2, name);
		text.setFontSize(style.getEdgeFontSize());
		text.setFontFamily(style.getFontFamily());
		text.setStrokeOpacity(0);
		text.setFillOpacity(style.getFillOpacity());
		text.setFillColor(style.getEdgeLabelColor());
		
		final VEdge edge = new VEdge(cytographer, graph, node1, node2, text, name);
		edge.getLine().setStrokeColor(style.getEdgeColor());
		edge.getArrow1().setStrokeColor(style.getEdgeColor());
		edge.getArrow2().setStrokeColor(style.getEdgeColor());
		
		edge.getLine().setStrokeWidth(style.getEdgeLineWidth());
		edge.getArrow1().setStrokeWidth(style.getEdgeLineWidth());
		edge.getArrow2().setStrokeWidth(style.getEdgeLineWidth());
		edge.setStrokeDashArray(style.getEdgeDashArray());

		// edge specific style attributes
		if (child != null && child.hasAttribute("_ec")) {
			edge.getLine().setStrokeColor(child.getStringAttribute("_ec"));
			edge.getArrow1().setStrokeColor(child.getStringAttribute("_ec"));
			edge.getArrow2().setStrokeColor(child.getStringAttribute("_ec"));
			edge.setOriginalStrokeColor(edge.getLine().getStrokeColor());
		}
		if (child != null && child.hasAttribute("_elw")) {
			edge.getLine().setStrokeWidth(child.getIntAttribute("_elw"));
			edge.getArrow1().setStrokeWidth(child.getIntAttribute("_elw"));
			edge.getArrow2().setStrokeWidth(child.getIntAttribute("_elw"));
		}
		if (child != null && child.hasAttribute("_eda")) {
			edge.setStrokeDashArray(child.getStringAttribute("_eda"));
		}
		return edge;
	}
		
	//getter setter
	public VNode getFirstNode() {
		return node1;
	}

	public VNode getSecondNode() {
		return node2;
	}

	public Shape getText() {
		return textShape;
	}

	public void setText(final Shape text) {
		textShape = text;
	}


	public void setOriginalStrokeColor(final String originalStrokeColor) {
		this.originalStrokeColor = originalStrokeColor;
	}

	public String getOriginalStrokeColor() {
		return originalStrokeColor;
	}

	@Override
	public String toString() {
		return textShape.getTitle();
	}

	public String getName() {
		return name;
	}
}
