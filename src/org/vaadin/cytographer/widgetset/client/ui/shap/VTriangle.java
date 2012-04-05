package org.vaadin.cytographer.widgetset.client.ui.shap;


import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Path;

/**
 * 
 * @author rspeck
 *
 */
public class VTriangle extends Path {
	
	public VTriangle(int x, int y, int size) {
		super(x,y);
		moveRelativelyTo(-size,size);
		lineRelativelyTo(size*2, 0);
		lineRelativelyTo(-size, -size*2);
		
		close();
		this.setFillColor("#00EE76"); // green
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Path.class;
	}
}
