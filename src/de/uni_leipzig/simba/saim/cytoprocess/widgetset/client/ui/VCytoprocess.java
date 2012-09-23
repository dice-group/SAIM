package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui;

import java.util.Iterator;

import org.vaadin.contrib.processing.svg.gwt.client.ProcessingJs;

import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph.VGraph;
import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph.VVisualStyle;
/**
 * 
 * @author rspeck
 *
 */
public class VCytoprocess extends VProcessingSVGextended {

	public VGraph vgraph;
	
	private ApplicationConnection applicationConnection;
	private String uidl_id;

	// node size
	private float radius = 10f;
	
	// mouse pressed coordinates to move to new ones 
	public int moveX = 0,moveY = 0 ;
	
	// make edge
	private boolean isLinkingTo = false;
	
	// edge source
	private int likedNodeid = Integer.MAX_VALUE;

	public boolean LOG = false;
	
	private VVisualStyle vvisualStyle;
	
	private Object selectedObject = null;
	/**	 
	 * 
	*/
	public VCytoprocess() {
		if(LOG)VConsole.log("VCytoprocess ...");
		size(500, 500);
		vgraph = new VGraph(this);
	}
	
	/** This method will be called only once during initializing after updateFromUIDL(...) was called
	 * 
	 * */
	@Override
	public void setup() {
		if(LOG)VConsole.log("VCytoprocess setup ...");
		
		doClear = false;
		frameRate(1);
		
		if(vvisualStyle == null){
			
			strokeWeight(3);			
			background(100);
			fill(0, 121, 184);
			stroke(255);
		}
		else
			setVvisualStyle(vvisualStyle);
	}
	
	public void setVvisualStyle(VVisualStyle vvisualStyle){
		if(LOG)VConsole.log("VCytoprocess setVvisualStyle");

		/* this sets the defaults from cytoscape */
		this.vvisualStyle = vvisualStyle;
		// title
		//setTitle(vvisualStyle.title);
		// graphWidth  graphHeight
		size(vvisualStyle.width, vvisualStyle.height);
		//NODE_BORDER_COLOR
		setStroke(vvisualStyle.NODE_BORDER_COLOR);
		//NODE_FILL_COLOR
		setFill(vvisualStyle.NODE_FILL_COLOR);
		//NODE_LABEL_COLOR
		
		//NODE_SIZE
		radius = vvisualStyle.nodeSize;
		//EDGE_LABEL_COLOR
		
		//NODE_LINE_WIDTH
		strokeWeight(Integer.valueOf(vvisualStyle.EDGE_LINE_WIDTH));
		//EDGE_FONT_SIZE
		
		//NODE_FONT_SIZE
		//NODE_FONT_NAME
		textFont(vvisualStyle.NODE_FONT_NAME, Integer.valueOf(vvisualStyle.NODE_FONT_SIZE));
		//DefaultBackgroundColor
		background(vvisualStyle.DefaultBackgroundColor);
		//DefaultNodeSelectionColor
		
		//DefaultEdgeSelectionColor
		
		//EDGE_LABEL_OPACITY		
			
		//curStrokeOpacity = 1.0;
	}
	
	/** This method will be called on every frame.*/
	@Override
	public void draw() {

	}
	
	/**	 */
	@Override
	public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {	
		super.updateFromUIDL(uidl, client);
		
		uidl_id = uidl.getId();
		applicationConnection = client;

		final Iterator<Object> uidlIter = uidl.getChildIterator();
		while (uidlIter.hasNext()) {
			UIDL child = (UIDL) uidlIter.next();
			
			if (child.getTag().equals("cytoprocess")) {

				if(child.getStringAttribute("operation").equals("REPAINT")){
					vgraph.parseUIDL(child);
				}
				
				if(child.getStringAttribute("operation").equals("ADD_EDGE"))
					vgraph.parseUIDLtoEdge(child);
				
				if(child.getStringAttribute("operation").equals("DELETE_NODE"))
					vgraph.parseUIDLtoDeleteNode(child);
				
				if(child.getStringAttribute("operation").equals("REFRESH_NODE_POSTIONS"))
					vgraph.parseUIDLtoRefreshNodePositions(child);
				
				if(child.getStringAttribute("operation").equals("DELETE_EDGE"))
					vgraph.parseUIDLtoDeleteEdge(child);
				
				if(child.getStringAttribute("operation").equals("UPDATE_NODE"))
					vgraph.parseUIDLtoUpdateNode(child);
				
				if(child.getStringAttribute("operation").equals("FIT_TO_VIEW"))
					fitShapes();
				
				if(child.getStringAttribute("operation").equals("ZOOMIN"))
					zoomIn();
				
				if(child.getStringAttribute("operation").equals("ZOOMOUT"))
					zoomOut();
				
				if(child.getStringAttribute("operation").equals("SCALEIN"))
					scaleIn();
				
				if(child.getStringAttribute("operation").equals("SCALEOUT"))
					scaleOut();
			}
		}		
	}

	@Override
	public void mousePressed() {
		if(LOG)VConsole.log("mousePressed ...");
		moveX = mouseX;
		moveY = mouseY;
		if(getSelectedObject() == null)
			setSelectedObject(this);
	}
	
	@Override
	public void mouseReleased(){
		if(LOG)VConsole.log("mouseReleased ...");
		moveX = 0;
		moveY = 0;
		setSelectedObject(null);
	}
	
	@Override
	public void mouseMoved() {

		if(getSelectedObject() == this && mousePressed &&  mouseButton == ProcessingJs.LEFT){
			if(LOG)VConsole.log("mouse moved and mouse pressed ...");
			
			int x = mouseX;
			int y = mouseY;
			move(moveX - x, moveY - y);
			moveX = x;
			moveY = y;
		}	
	}

	@Override
	public void onMouseWheel(MouseWheelEvent mwv) {
		super.onMouseWheel(mwv);

		if ( mwv.getDeltaY() > 0) {

			if(keyPressed && key == 'z')
				scaleOut();
			else
				zoomOut();
		} 
		else if (mwv.getDeltaY() < 0) {
			
			if(keyPressed && key == 'z')
				scaleIn();
			else
				zoomIn();
		}
	}
	
	public void scaleIn(){
		scaleUp();
		vgraph.updateEdgePositions();
	}
	
	public void scaleOut(){
		scaleDown();
		vgraph.updateEdgePositions();
	}
	
	public void zoomIn(){
		zoom(ZOOM_UP);
		vgraph.updatePositions();
	}
	public void zoomOut(){
		zoom(ZOOM_DOWN);
		vgraph.updatePositions();
	}

	@Override
	public void fitShapes(){
		super.fitShapes((int)radius/2);
		vgraph.updatePositions();
	}
	
	/**
	 * Override this. Called on key typing.
	 */
	public void keyTyped() {
		switch(key){		
		case 'f' :
			fitShapes();
			break;
		}
	}
	
	/**
	 * sends the client to delete the node
	 * 
	 * @param nodeid
	 */
	public void deleteNode(final int nodeid) {
		
		applicationConnection.updateVariable(
				uidl_id,
				"deleteNode", 
				nodeid,
				true
				);
	}
	
	/**
	 * sends the client to delete the edge
	 * 
	 * @param nodeid
	 */
	public void deleteEdge(final int edgeid) {
		
		applicationConnection.updateVariable(
				uidl_id,
				"deleteEdge", 
				edgeid,
				true
				);
	}
	
	/**
	 * sets a tmp node named likedNode and and a flag isLinkingTo to true. 
	 * Used to make an edge on client side.
	 * 
	 * @param nodeid
	 */
	public void setlinkNode(int nodeid){
		likedNodeid = nodeid;
		isLinkingTo = true;
	}
	
	/**
	 * True if setLinkNode was called before
	 * @return
	 */
	public boolean isLinkingTo(){
		return isLinkingTo;
	}
	
	/**
	 * send the client to create an edge with the given id as target and likedNode as source
	 * @param nodeid
	 */
	public void linkTo(final int nodeid) {		
		
		if(isLinkingTo && nodeid != likedNodeid ){
			applicationConnection.updateVariable(
					uidl_id,
					"linkTo", 
					new String[] {nodeid+"",likedNodeid+"", new String("newEdge")},
					true
					);
			
			isLinkingTo = false;
			likedNodeid = Integer.MAX_VALUE;
		}
	}
	
	public void nodeDoubleClick(final int nodeid,int x, int y) {
		
		applicationConnection.updateVariable(
				uidl_id,
				"nodeDoubleClick", 
				new String[] {nodeid+"", x+"",y+""},
				true
				);
	}
	
	public void edgeDoubleClick(final int edgeid,int x, int y) {
		
		applicationConnection.updateVariable(
				uidl_id,
				"edgeDoubleClick", 
				new String[] {edgeid+"", x+"",y+""},
				true
				);
	}
	
	public Object getSelectedObject(){
		return selectedObject;
	}
	
	public void setSelectedObject(Object selectedObject){
		this.selectedObject = selectedObject;
	}
	public void requestDone() {
		
		applicationConnection.updateVariable(
				uidl_id,
				"requestDone", 
				"",
				true
				);
	}
	
}
