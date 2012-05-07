package org.vaadin.cytographer.widgetset.client.ui.node;

import org.vaadin.cytographer.widgetset.client.ui.VCytographer;
import org.vaadin.cytographer.widgetset.client.ui.VGraph;
import org.vaadin.cytographer.widgetset.client.ui.VNode;
import org.vaadin.cytographer.widgetset.client.ui.VVisualStyle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VCycle;
import org.vaadin.gwtgraphics.client.Shape;

public class VSource extends VNode{ 

	public VSource(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String id,final VVisualStyle style) {
		super(cytographer,graph,shape,name,id,style);		
	}
	public static Shape getShape(int x, int y,int nodeSize){
		return new VCycle(x, y,nodeSize,"#00C000"); //green 16 VGA
	}	
}