package org.vaadin.cytographer.widgetset.client.ui.shap;


import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Path;

/**
 * 
 * @author rspeck
 *
 */
public class VHexagon extends Path {
	
	public VHexagon(int x, int y, int size,String color) {
		super(x,y);
		
		/*
		A1=(L,0)
		A2=(L/2, L*sqrt(3)/2)
		A3=(-L/2, L*sqrt(3)/2)
		A4=(-L,0)
		A5=(-L/2, -L*sqrt(3)/2)
		A6=(L/2, -L*sqrt(3)/2)
		*/
		
		int v = (int)(size * (((float)Math.sqrt(3)) / 2));
		
		moveRelativelyTo(size,0);		
		lineRelativelyTo(-size/2,v);		
		lineRelativelyTo(-size,0);		
		lineRelativelyTo(-size/2,-v);		
		lineRelativelyTo(size/2,-v);		
		lineRelativelyTo(size,0);	
		close();
		//this.setRotation(90);
		setFillColor(color); 
	}
	
	@Override
	protected Class<? extends VectorObject> getType() {
		return Path.class;
	}
}