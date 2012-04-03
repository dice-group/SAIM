package org.vaadin.cytographer.widgetset.client.ui.shap;

import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Path;
/**
 * 
 * @author rspeck
 *
 */
public class VRectangle extends Path {

	public VRectangle(int x, int y, int size) {
		super(x,y);
		moveRelativelyTo(-size,size);
		lineRelativelyTo(size*2,0);
		lineRelativelyTo(0, -size*2);
		lineRelativelyTo(-size*2, 0);
		close();
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Path.class;
	}
}
