package org.vaadin.cytographer.widgetset.client.ui.node;

import org.vaadin.cytographer.widgetset.client.ui.VCytographer;
import org.vaadin.cytographer.widgetset.client.ui.VGraph;
import org.vaadin.cytographer.widgetset.client.ui.VNode;
import org.vaadin.cytographer.widgetset.client.ui.VVisualStyle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VRectangle;
import org.vaadin.gwtgraphics.client.Shape;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;


public class VSource extends VNode  implements DoubleClickHandler{

	public VSource(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String id,final VVisualStyle style) {
		super(cytographer,graph,shape,name,id,style);		
		addDoubleClickHandler(this);
	}
	public static Shape getShape(int x, int y,int nodeSize,String color){
		return new VRectangle(x, y,nodeSize,color); 
	}	
	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		cytographer.doubleClick(new String[]{getID().toString(), getX()+"", getY()+"", "", "","Source"});		
	}
}