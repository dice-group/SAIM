package org.vaadin.cytographer.widgetset.client.ui.node;

import org.vaadin.cytographer.widgetset.client.ui.VCytographer;
import org.vaadin.cytographer.widgetset.client.ui.VGraph;
import org.vaadin.cytographer.widgetset.client.ui.VNode;
import org.vaadin.cytographer.widgetset.client.ui.VVisualStyle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VDiamond;
import org.vaadin.gwtgraphics.client.Shape;

public class VMetric extends VNode{ 

	public VMetric(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String id,final VVisualStyle style) {
		super(cytographer,graph,shape,name,id,style);			
	}	
	public static Shape getShape(int x, int y,int nodeSize){
		return new VDiamond(x,y,nodeSize,"#dddd99"); 
	}
}