package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui;

import org.vaadin.contrib.processing.svg.gwt.client.ui.VProcessingSVG;

import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;

/**
 * 
 * Extended Vaadin ProcessingSVG component.
 * 
 * @author rspeck
 *
 */
public class VProcessingSVGextended extends VProcessingSVG{
	
	public static final double ZOOM_DOWN = 0.90;
	public static final double ZOOM_UP = 1.10;

	public int scaleCount = 0;
	private float scaleFactor = 2.0f; 
	private boolean scaledUp = false;
	/**
	 * 
	 */
	public VProcessingSVGextended(){
		super();
	}
	
	public void zoom(final double factor) {
		
		int x = mouseX;
		int y = mouseY;
		
		if (!doClear) {
			for (VectorObject v : objects) {				
				if (v instanceof Shape) {
					Shape s = (Shape) v;
					s.setX(round(((float) ((s.getX() - x) * factor) + x)));
					s.setY(round(((float) ((s.getY() - y) * factor) + y)));
				}
				if(v instanceof Line){
					Line l = (Line) v;
					l.setX1(round(((float) ((l.getX1() - x) * factor) + x)));
					l.setY1(round(((float) ((l.getY1() - y) * factor) + y)));
					l.setX2(round(((float) ((l.getX2() - x) * factor) + x)));
					l.setY2(round(((float) ((l.getY2() - y) * factor) + y)));
				}
			}
		}
	}
	
	/**
	 * Moves all Shape objects
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y){
		for (VectorObject o : objects){			
			if(o instanceof Shape){
				Shape s = (Shape) o;
				s.setX(s.getX()-x);
				s.setY(s.getY()-y);
			}
			if(o instanceof Line){
				Line l = (Line) o;
				l.setX1(l.getX1()-x);
				l.setY1(l.getY1()-y);
				l.setX2(l.getX2()-x);
				l.setY2(l.getY2()-y);
			}
		}		
	}
	
	/**
	 * Draw a diamond.
	 *
	 * @param x
	 *            X-coordinate of the center of the diamond
	 * @param y
	 *            Y-coordinate of the center of the diamond
	 * @param w
	 *           Width of the diamond
	 * @param h
	 *           height of the diamond
	 * @return Path
	 */
	public Path diamond(float x, float y,float w, float h){
		w = round(w/2);
		h = round(h/2);
		
		if(scaleCount > 0)
			for(int i = 0 ; i < scaleCount; i++){
				x = (x * (scaleFactor) + transX);
				y = (y * (scaleFactor) + transY);
				w = (int)(w * (scaleFactor) + transX);
				h = (int)(h * (scaleFactor) + transY);
			}
		
		if(scaleCount < 0)
			for(int i = scaleCount ; i < 0; i++){
				x = (x * (1/scaleFactor) + transX);
				y = (y * (1/scaleFactor) + transY);
				w = (int)(w * (1/scaleFactor) + transX);
				h = (int)(h * (1/scaleFactor) + transY);
			}

		
		style(beginPath());
		path.setX((int)x);
		path.setY((int)y);		
		path.moveRelativelyTo(-(int)w,  0);
		
		path.lineRelativelyTo( (int)w,  (int)h);
		path.lineRelativelyTo( (int)w, -(int)h);
		path.lineRelativelyTo(-(int)w, -(int)h);
		path.lineRelativelyTo(-(int)w,  (int)h);
		
		return endShape(true);
	}
	
	/**
	 * Draw a hexagon.
	 *
	 * @param x
	 *            X-coordinate of the center of the hexagon
	 * @param y
	 *            Y-coordinate of the center of the hexagon
	 * @param size
	 *           radius of the hexagon
	 * @return Path
	 */
	public Path hexagon(float x, float y,float size){

		if(scaleCount > 0)
			for(int i = 0 ; i < scaleCount; i++){
				x = (x * (scaleFactor) + transX);
				y = (y * (scaleFactor) + transY);
				size = (int)(size * (scaleFactor) + transX);
			}
		
		if(scaleCount < 0)
			for(int i = scaleCount ; i < 0; i++){
				x = (x * (1/scaleFactor) + transX);
				y = (y * (1/scaleFactor) + transY);
				size = (int)(size * (1/scaleFactor) + transX);
			}
		
		style(beginPath());
		path.setX((int)x);
		path.setY((int)y);	
		size = round(size);
		int v = round(size * (((float)Math.sqrt(3)) / 2.0f));
		path.moveRelativelyTo((int)size,0);		
		
		path.lineRelativelyTo(-(int)(size/2), v);		
		path.lineRelativelyTo(-(int) size   , 0);		
		path.lineRelativelyTo(-(int)(size/2),-v);		
		path.lineRelativelyTo( (int)(size/2),-v);		
		path.lineRelativelyTo( (int) size   , 0);
		path.lineRelativelyTo( (int)(size/2), v);
		
		return endShape(true);
	}
	/**
	 * Draw a circle.
	 *
	 * @param x
	 *            X-coordinate
	 * @param y
	 *            Y-coordinate
	 * @param radius
	 *            Radius
	 * @return Circle
	 */
	public Circle circle(float x, float y, float radius) {
		Circle s = super.circle(x, y, radius);
		if(scaleCount > 0)
			for(int i = 0 ; i < scaleCount; i++){
				x = (x * (scaleFactor) + transX);
				y = (y * (scaleFactor) + transY);
				radius = (int)(radius * (scaleFactor) );
			}
		
		if(scaleCount < 0)
			for(int i = scaleCount ; i < 0; i++){
				x = (x * (1/scaleFactor) + transX);
				y = (y * (1/scaleFactor) + transY);
				radius = (int)(radius * (1/scaleFactor));
			}
		
		s.setX((int)x);
		s.setY((int)y);
		s.setRadius((int)radius);
		return s;
	}
	
	/**
	 * Draw a rectangle.
	 *
	 * @param x
	 *            X-coordinate of the center of the rectangle
	 * @param y
	 *            Y-coordinate of the center of the rectangle
	 * @param w
	 *           Width of the rectangle
	 * @param h
	 *           height of the rectangle
	 * @return Path
	 */
	public Path rectangle(float x, float y,float w, float h){

		if(scaleCount > 0)
			for(int i = 0 ; i < scaleCount; i++){
				x = (x * (scaleFactor) + transX);
				y = (y * (scaleFactor) + transY);
				w = (int)(w * (scaleFactor) + transX);
				h = (int)(h * (scaleFactor) + transY);
			}
		
		if(scaleCount < 0)
			for(int i = scaleCount ; i < 0; i++){
				x = (x * (1/scaleFactor) + transX);
				y = (y * (1/scaleFactor) + transY);
				w = (int)(w * (1/scaleFactor) + transX);
				h = (int)(h * (1/scaleFactor) + transY);
			}
			
		style(beginPath());
		path.setX((int)round(x));
		path.setY((int)round(y));	
		path.moveRelativelyTo(-round(w/2),round(h/2));
		
		path.lineRelativelyTo(round(w),0);
		path.lineRelativelyTo(0, -round(h));
		path.lineRelativelyTo(-round(w), 0);
		path.lineRelativelyTo(0, round(h));
		
		return endShape(true);
	}
	
	/**
	 * Removes an object.
	 * @param VectorObject
	 */
	public void remove(VectorObject o){
		objects.remove(o);
		getCanvas().remove(o);
	}
	/**
	 * Fits all Shape objects to the canvas size
	 */
	public void fitShapes(){
		fitShapes(0);
	}
	/**
	 * Fits all Shape objects to the canvas size
	 * 
	 */
	public void fitShapes(final int margin){
		
		int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

		for (VectorObject o : objects){
			if(o instanceof Shape){
				int x = ((Shape)o).getX();
				int y = ((Shape)o).getY();

				if (x > maxX)	maxX = x;
				if (x < minX) 	minX = x;
				if (y > maxY) 	maxY = y;
				if (y < minY) 	minY = y;
			}
		}

		int viewWidth  = maxX - minX;
		int viewHeight = maxY - minY;	
		
		for (VectorObject o : objects){
			
			if(o instanceof Shape){
				Shape s = (Shape)o;
						
				if(viewWidth > 0){
					float d = s.getX() - minX;
					if(d == 0)
						d = 0.1f;
					s.setX(round( d / viewWidth  * (width - 2 * margin)) + margin);
				}
				
				if(viewHeight > 0){
					float d = s.getY() - minY;
					if(d == 0)
						d = 0.1f;
					s.setY(round( d / viewHeight * (height - 2 * margin)) + margin);
				}
			}
		}
	}
	
	public void scaleDown(){
		if(scaledUp){
			scaleXFactor = 1f;
			scaleYFactor = 1f;
			scaledUp = false;
		}
		scale((1.0f/scaleFactor),  (1.0f/scaleFactor));
		scaleCount--;
	}

	public void scaleUp(){
		if(!scaledUp){
			scaleXFactor = 1f;
			scaleYFactor = 1f;
			scaledUp = true;
		}
		scale(scaleFactor, scaleFactor);
		scaleCount++;
	}

	public void scaleNull(){
		
		if(scaleCount > 0){
			scaleDown();
			scaleNull();
		}
		
		if(scaleCount < 0){
			scaleUp();
			scaleNull();
		}
		
		if(scaleCount == 0) {
			scaleXFactor = 1f;
			scaleYFactor = 1f;
		}
	}
	
	/**
	 * Set background color (RGB).
	 *
	 * @param color
	 *            RGB color rgb(" + color + "," + color + "," + color + ")
	 */
	public void background(String rgb) {
		curBackground = rgb;
		if (curBackground != null) {
			getCanvas().getElement().getStyle().setProperty("background",
					curBackground);
		}
	}
	/**
	 * Set stroke color (RGB)
	 *
	 * @param c
	 *              RGB color rgb(" + color + "," + color + "," + color + ")
	 */
	public void setStroke(String rgb) {
		doStroke = true;
		strokeStyle = rgb;
	}
	/**
	 * Set the fill color (RGB)
	 *
	 * @param c
	 *              RGB color rgb(" + color + "," + color + "," + color + ")
	 */
	public void setFill(String rgb) {
		doFill = true;
		fillStyle = rgb;
	}
	public String getFillStyle() {
		return fillStyle;
	}
	// privates
	private void style(Shape shape){
		if (doStroke) {
			shape.setStrokeColor(strokeStyle);
			shape.setStrokeOpacity(curStrokeOpacity);
		}
		if (doFill) {
			shape.setFillColor(fillStyle);
			shape.setFillOpacity(curFillOpacity);
		} else {
			shape.setFillOpacity(0.0f);
		}
	}
}
