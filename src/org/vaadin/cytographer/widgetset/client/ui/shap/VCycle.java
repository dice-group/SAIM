package org.vaadin.cytographer.widgetset.client.ui.shap;
import org.vaadin.gwtgraphics.client.shape.Circle;

/**
 * 
 * @author rspeck
 *
 */
public class VCycle extends Circle {

	public VCycle(int x, int y, int radius, String color) {
		super(x,y,radius);
		this.setFillColor(color);
	}
}
