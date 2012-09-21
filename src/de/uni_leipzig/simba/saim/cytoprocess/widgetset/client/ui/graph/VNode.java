package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.vaadin.terminal.gwt.client.VConsole;

import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.VCytoprocess;
/**
 * 
 * @author rspeck
 *
 */
public class VNode {

	// giny.view.NodeView shape types
	public static final int DIAMOND = 1;
	public static final int ELLIPSE = 2;
	public static final int HEXAGON = 3;
	// public static final int OCTAGON = 4;
	// public static final int PARALELLOGRAM = 5;
	public static final int RECTANGLE = 6;
	// public static final int ROUNDED_RECTANGLE = 7;
	// public static final int TRIANGLE = 0;

	public Shape shape;
	public int id;
	public Text label;

	// attributs
	public Text label1 = null;
	public Double value1 = null;
	public Text label2 = null;
	public Double value2 = null;

	/**
	 */
	public VNode(int id, Shape shape, Text label) {
		this.id = id;
		this.label = label;
		this.shape = shape;
	}

	/** deletes objects from canvas */
	public void delete(VCytoprocess vcypro) {
		vcypro.remove(shape);
		vcypro.remove(label);
		if (label1 != null)
			vcypro.remove(label1);
		if (label2 != null)
			vcypro.remove(label2);
	}

	public void addValue1(VCytoprocess vcypro, String name, Double value) {
		VConsole.log("VNode: addValue1");

		if (label1 == null) {

			if (!name.trim().isEmpty()) {
				label1 = vcypro.text(name + ": " + value, shape.getX(),
						shape.getY() + label.getTextHeight());
				
				setStyle(label,label1);
				value1 = value;
			}
		} else if (!name.trim().isEmpty())
			label1.setText(name + ": " + value);

		bringToFront(vcypro.getCanvas());
	}

	public void addValue2(VCytoprocess vcypro, String name, Double value) {
		VConsole.log("VNode: addValue2");

		if (label2 == null) {

			if (!name.trim().isEmpty()) {
				label2 = vcypro.text(name + ": " + value, shape.getX(),
						shape.getY()
								+ label.getTextHeight()
								+ ((label1 != null) ? label1.getTextHeight()
										: 0));
				setStyle(label,label2);
				value2 = value;
			}
		} else if (!name.trim().isEmpty())
			label2.setText(name + ": " + value);

		bringToFront(vcypro.getCanvas());
	}

	public static VNode createVNode(VVisualStyle vs, VCytoprocess vcytoprocess,
			int x, int y, int id, String label, int nodeViewShape, String rgb) {

		// set color
		String tmp = vcytoprocess.getFillStyle();
		if (!rgb.trim().isEmpty())
			vcytoprocess.setFill(rgb);
		vcytoprocess.strokeWeight(Integer.valueOf(vs.NODE_LINE_WIDTH));
		vcytoprocess.setStroke(vs.NODE_BORDER_COLOR);
		// draw node
		Shape shape = null;
		switch (nodeViewShape) {
		case RECTANGLE:
			shape = (Shape) vcytoprocess.rectangle(x, y, vs.nodeSize, vs.nodeSize/2);
			break;
		case DIAMOND:
			shape = (Shape) vcytoprocess.diamond(x, y, vs.nodeSize, vs.nodeSize);
			break;
		case HEXAGON:
			shape = (Shape) vcytoprocess.hexagon(x, y, vs.nodeSize / 2f);
			break;
		case ELLIPSE:
		default:
			shape = (Shape) vcytoprocess.circle(x, y, vs.nodeSize / 2f);
		}

		// reset color
		if (!rgb.trim().isEmpty())
			vcytoprocess.setFill(tmp);

		// draw label
		vcytoprocess.noStroke();
		vcytoprocess.noFill();
		
		Text text =  vcytoprocess.text(label, 0, 0);
		setStyle(text,vs);
		
		VNode vnode = new VNode(id, shape,text);
		vnode.updatePosition();
		
		vcytoprocess.fill();
		vcytoprocess.stroke();

		return vnode;
	}

	private static void setStyle(Text text,VVisualStyle vs){
		text.setStrokeColor(vs.NODE_LABEL_COLOR);
		text.setFontSize(Integer.valueOf(vs.NODE_FONT_SIZE));
		text.setFontFamily(vs.NODE_FONT_NAME);
	}
	private void setStyle(Text from,Text to){
		to.setStrokeColor(from.getStrokeColor());
		to.setFontSize(from.getFontSize());
		to.setFontFamily(from.getFontFamily());
	}
	public void move(int x, int y) {
		shape.setX(shape.getX() - x);
		shape.setY(shape.getY() - y);

		label.setX(label.getX() - x);
		label.setY(label.getY() - y);

		if (label1 != null) {
			label1.setX(label1.getX() - x);
			label1.setY(label1.getY() - y);
		}

		if (label2 != null) {
			label2.setX(label2.getX() - x);
			label2.setY(label2.getY() - y);
		}
	}

	public void bringToFront(DrawingArea da) {
		da.bringToFront(shape);
		da.bringToFront(label);
		if (label1 != null)
			da.bringToFront(label1);
		if (label2 != null)
			da.bringToFront(label2);
	}

	public void updatePosition(int x, int y) {
		shape.setX(x);
		shape.setY(y);
		updatePosition();
	}

	public void updatePosition() {
		
		boolean l1 = (label1 != null && label1.getText().trim().length()>0)? true:false;
		boolean l2 = (label2 != null && label2.getText().trim().length()>0)? true:false;
			
		if(l1 && l2){
			label.setX(  getX() - label.getTextWidth() / 2);
			label1.setX( getX() - label1.getTextWidth()/ 2);			
			label2.setX( getX() - label2.getTextWidth()/ 2);
			
			label.setY( getY() - label1.getTextHeight()/2);
			label1.setY(getY() + label1.getTextHeight()/2);				
			label2.setY(getY() + label1.getTextHeight()/2  + label1.getTextHeight());
		}
		if(l1 && !l2){
			label.setX(  getX() - label.getTextWidth()  / 2);
			label1.setX( getX() - label1.getTextWidth() / 2);			
						
			label.setY( getY() - label.getTextHeight() /2);
			label1.setY(getY() + label1.getTextHeight()/2);
		}
		if(!l1 && l2){
			label.setX(  getX() - label.getTextWidth() / 2);
			label2.setX( getX() - label2.getTextWidth()/ 2);			
						
			label.setY( getY() - label.getTextHeight() /2);
			label2.setY(getY() + label2.getTextHeight()/2);
		}
		if(!l1 && !l2){
			label.setX(getX() - label.getTextWidth() /2);
			label.setY(getY() + label.getTextHeight()/2);
		}
	}

	// getter setter
	public int getX() {
		return shape.getX();
	}

	public int getY() {
		return shape.getY();
	}
}