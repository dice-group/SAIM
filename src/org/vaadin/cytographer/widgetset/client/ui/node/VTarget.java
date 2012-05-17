package org.vaadin.cytographer.widgetset.client.ui.node;

import org.vaadin.cytographer.widgetset.client.ui.VCytographer;
import org.vaadin.cytographer.widgetset.client.ui.VGraph;
import org.vaadin.cytographer.widgetset.client.ui.VNode;
import org.vaadin.cytographer.widgetset.client.ui.VVisualStyle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VCycle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VRectangle;
import org.vaadin.gwtgraphics.client.Shape;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.vaadin.terminal.gwt.client.VConsole;

public class VTarget extends VNode{ 

	public VTarget(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String id,final VVisualStyle style) {
		super(cytographer,graph,shape,name,id,style);			
	}
	public static Shape getShape(int x, int y,int nodeSize){
		return new VRectangle(x, y, nodeSize,"#FF0000"); //red 16 VGA
	}	
}