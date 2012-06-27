package org.vaadin.cytographer.widgetset.client.ui.node;

import org.vaadin.cytographer.widgetset.client.ui.VContextMenu;
import org.vaadin.cytographer.widgetset.client.ui.VCytographer;
import org.vaadin.cytographer.widgetset.client.ui.VGraph;
import org.vaadin.cytographer.widgetset.client.ui.VNode;
import org.vaadin.cytographer.widgetset.client.ui.VVisualStyle;
import org.vaadin.cytographer.widgetset.client.ui.shap.VCycle;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.vaadin.terminal.gwt.client.VConsole;

public class VOutput extends VNode implements DoubleClickHandler{ 
	
	protected final Text textShape1;
	protected final Text textShape2;
	
	public VOutput(final VCytographer cytographer, final VGraph graph, final Shape shape, final String name,final String id,final VVisualStyle style) {
		super(cytographer,graph,shape,name,id,style);	
		addDoubleClickHandler(this);
		
		textShape1 = new Text(0,0, "");
		textShape1.setStrokeOpacity(0);
		textShape2 = new Text(0,0, "");
		textShape2.setStrokeOpacity(0);
		VOutput.setStyle(style, textShape1);
		VOutput.setStyle(style, textShape2);
		
		add(textShape1);
		add(textShape2);
		moveNode(shape.getX(),shape.getY());
	}	
	public static void setStyle(final VVisualStyle style,final Text shape){
		shape.setFillColor("#000000");
		shape.setFillOpacity(1);
		shape.setFontSize(style.getNodeFontSize());
		shape.setFontFamily(style.getFontFamily());		
	}
	public void updateValues(String value1,String value2){
		VConsole.log("updateValues");
		textShape1.setText(value1);
		textShape2.setText(value2);
	}
	public static Shape getShape(int x, int y,int nodeSize,String color){
		return new VCycle(x,y,nodeSize,color); 
	}
	@Override
	public void initCommands(final VContextMenu menu) {
		super.initCommands(menu);
		commandMap.remove("Delete");
	}
	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		cytographer.doubleClick(new String[]{getID().toString(), getX()+"", getY()+"", getValue1(), getValue2(),"Output"});	
	}
	public String getValue1(){
		return textShape1.getText();
	}
	public String getValue2(){
		return textShape2.getText();
	}
	@Override
	public void moveNode(final float x, final float y) {

		textShape1.setX((int)(x-textShape1.getTextWidth()/2));
		textShape1.setY((int)y+textShape.getFontSize()*2);
				
		textShape2.setX((int)(x-textShape2.getTextWidth()/2));
		textShape2.setY((int)y+textShape.getFontSize()*2+textShape1.getFontSize());
		super.moveNode(x, y);
	}
}