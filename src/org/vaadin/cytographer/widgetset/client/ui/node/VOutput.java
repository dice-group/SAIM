package org.vaadin.cytographer.widgetset.client.ui.node;

import org.vaadin.cytographer.widgetset.client.ui.VContextMenu;
import org.vaadin.cytographer.widgetset.client.ui.VCytographer;
import org.vaadin.cytographer.widgetset.client.ui.VGraph;
import org.vaadin.cytographer.widgetset.client.ui.VNode;
import org.vaadin.cytographer.widgetset.client.ui.VVisualStyle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VCycle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VRectangle;
import org.vaadin.gwtgraphics.client.Shape;

public class VOutput extends VNode{ 

	public VOutput(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String id,final VVisualStyle style) {
		super(cytographer,graph,shape,name,id,style);			
	}	
	public static Shape getShape(int x, int y,int nodeSize){
		//return new VDiamond(x,y,nodeSize,"#00C0C0"); // teal 16 VGA
		return new VCycle(x,y,nodeSize,"#0000FF"); // blue 16 VGA
	}
	@Override
	public void initCommands(final VContextMenu menu) {
		super.initCommands(menu);
		commandMap.remove("Delete");
	}
}