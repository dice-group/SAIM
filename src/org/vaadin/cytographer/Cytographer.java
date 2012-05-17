package org.vaadin.cytographer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.Window.Notification;

import org.vaadin.cytographer.widgetset.client.ui.VCytographer;

import cytoscape.CyNetwork;
import cytoscape.view.CyNetworkView;
import de.uni_leipzig.simba.saim.core.metric.Node;
import de.uni_leipzig.simba.saim.core.metric.Output;

/**
 * Server side component for the VCytographer widget.
 */
@ClientWidget(VCytographer.class)
public class Cytographer extends AbstractComponent {
	private static final long serialVersionUID = 8483008141219579936L;
	
	public enum GraphOperation {
		REPAINT, SET_NODE_SIZE, SET_VISUAL_STYLE, SET_TEXT_VISIBILITY, SET_OPTIMIZED_STYLES, SET_ZOOM, REFRESH //,UPDATE_NODE
	}
	private GraphOperation currentOperation = GraphOperation.REPAINT;
		
	private final GraphProperties graphProperties;
	private final PaintController paintController = new PaintController();
	private Window mainWindow;
	/**
	 * Gets the metrics output node / root node
	 */
	public Node getMetric(){
		for(Node n : graphProperties.getNodeMap().values()){
			if(n instanceof Output)
				return n;
		}return null;
	}
	/**
	 * 
	 */
	public Cytographer(final CyNetwork network, final CyNetworkView finalView, final String title, final int width, final int height,Window mainWindow) {

		graphProperties = new GraphProperties(network, finalView, title);
		graphProperties.setWidth(width);
		graphProperties.setHeight(height);
		this.mainWindow = mainWindow;
		graphProperties.setMainWindow(mainWindow);		
	}

	public int  addNode(String name, int x, int y, GraphProperties.Shape shape){
		return graphProperties.addANewNode(name, x, y,shape);
	}
	public void createAnEdge(int nodeAid, int nodeBid, String attribute) {
		graphProperties.createAnEdge(nodeAid, nodeBid, attribute);
	}

	@Override
	public void paintContent(final PaintTarget target) throws PaintException {
		super.paintContent(target);	
		target.addAttribute("operation", currentOperation.toString());
		switch (currentOperation) {
		case REPAINT:
			paintController.repaintGraph(target, graphProperties);
			break;
		case SET_NODE_SIZE:
			paintController.paintNodeSize(target, graphProperties);
			break;
			
		case SET_TEXT_VISIBILITY:
			paintController.paintTextVisibility(target, graphProperties);
			break;

		case SET_ZOOM:
			paintController.setZoom(target, graphProperties);
			break;
		case REFRESH:
			paintController.repaintGraph(target, graphProperties);
			break;
		default:
			;
		}
		currentOperation = GraphOperation.REPAINT;
		graphProperties.setUseFitting(false);
	}
	/**
	 * Receive and handle events and other variable changes from the client.
	 */
	@Override
	public void changeVariables(final Object source, final Map<String, Object> variables) {
		super.changeVariables(source, variables);
		
		if (variables.containsKey("selectedEdges")) {
			graphProperties.clearSelectedEdges();
			final String[] selectedEdges = (String[]) variables.get("selectedEdges");
			for (final String edge : selectedEdges) 
				graphProperties.addSelectedEdge(edge);
		}
		if (variables.containsKey("selectedNodes")) {
			graphProperties.clearSelectedNodes();
			final String[] strs = (String[]) variables.get("selectedNodes");
			for (final String str : strs) 
				graphProperties.addSelectedNode(str);
		}
		if (variables.containsKey("zoomFactor")) {
			graphProperties.setZoomFactor((Integer) variables.get("zoomFactor"));
		}
		if (variables.containsKey("createdANode")) {
			final Object[] nodeData = (Object[]) variables.get("createdANode");
			graphProperties.addANewNode((String) nodeData[0], (Integer) nodeData[1], (Integer) nodeData[2],graphProperties.getShapes((String) nodeData[0]));
		}
		if (variables.containsKey("removedNode")) {
			graphProperties.removeNode((String) variables.get("removedNode"));
			repaintGraph();
		}
		if (variables.containsKey("edgeCreated")) {
			String[] args = (String[]) variables.get("edgeCreated");
			graphProperties.createAnEdge(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
			repaintGraph();
		}
		if (variables.containsKey("removedEdge")) {
			graphProperties.removeEdge((String) variables.get("removedEdge"));
		}
		if (variables.containsKey("doubleClick")) {
			final String[] args = (String[]) variables.get("doubleClick");
			
			Window mywindow = new Window("");
			
			final TextField t = new TextField("option",args[3]);
			final TextField tt = new TextField("option",args[4]);
			mywindow.addComponent(t);
			mywindow.addComponent(tt);
			t.addValidator(new DoubleValidator("A Threshold must be a value between 1 and 0.") {
				private static final long serialVersionUID = -5585916227598767457L;
				@Override
			    protected boolean isValidString(String value) {
			        try {
			            double d = Double.parseDouble(value);
			            return d>=0 && d <=1;
			        } catch (Exception e) {
			            return false;
			        }
			    }
			});
			tt.addValidator(new DoubleValidator("A Threshold must be a value between 1 and 0.") {
				private static final long serialVersionUID = 2933399368578994985L;
				@Override
			    protected boolean isValidString(String value) {
			        try {
			            double d = Double.parseDouble(value);
			            return d>=0 && d <=1;
			        } catch (Exception e) {
			            return false;
			        }
			    }
			});
			t.setMaxLength(4);
			tt.setMaxLength(4);
			t.setImmediate(true);
			tt.setImmediate(true);
			mywindow.addListener(new CloseListener(){
				private static final long serialVersionUID = -165177940359643613L;
				@Override public void windowClose(CloseEvent e) {
					if(t.isValid() && tt.isValid()){
						List<Object> value = new ArrayList<>();
						value.add(t.getValue());
						value.add(tt.getValue());
						
						graphProperties.setNodeMetadata( args[0], value);
						repaintGraph();
					}else{
						mainWindow.showNotification("A Threshold must be a value between 1 and 0.", Notification.TYPE_WARNING_MESSAGE);
					}
				}
			});
			mywindow.setResizable(false);
			mywindow.setModal(true); 
			mywindow.setHeight("180px");
			mywindow.setWidth("200px");			 
			mywindow.setPositionX(Math.round(Float.valueOf(args[1])));
			mywindow.setPositionY(Math.round(Float.valueOf(args[2])));	

			mainWindow.addWindow(mywindow);
			
		}
		if (variables.containsKey("onNodeMouseUp")) {
			final String[] args = (String[]) variables.get("onNodeMouseUp");
			
			String nodeID = args[0];
			String x = args[1];
			String y = args[2];
			
			graphProperties.getCyNetworkView().getNodeView(new Integer(nodeID)).setXPosition(Double.parseDouble(x));
			graphProperties.getCyNetworkView().getNodeView(new Integer(nodeID)).setYPosition(Double.parseDouble(y));
		}
	}

	/**
	 * Change texts visibilities
	 * 
	 * @param b
	 */
	public void setTextVisible(final boolean b) {
		currentOperation = GraphOperation.SET_TEXT_VISIBILITY;
		graphProperties.setTextsVisible(b);
		requestRepaint();
	}
	
	public void setNodeMetadata(String nodeID, List<Object> value){
		graphProperties.setNodeMetadata( nodeID, value);
		repaintGraph();
	}

	/**
	 * Change node size
	 * 
	 * @param nodeSize
	 * @param repaint
	 */
	public void setNodeSize(final double nodeSize, final boolean repaint) {
		graphProperties.setNodeSize(nodeSize);
		if (repaint) {
			currentOperation = GraphOperation.SET_NODE_SIZE;
			requestRepaint();
		}
	}

	public void repaintGraph() {
		currentOperation = GraphOperation.REPAINT;
		graphProperties.setZoomFactor(0);
		requestRepaint();
	}


	public Container getNodeAttributeContainerForSelectedNodes() {
		return graphProperties.getNodeAttributeContainerForSelectedNodes();
	}

	public void fitToView() {
		graphProperties.measureDimensions();
		graphProperties.setUseFitting(true);
		graphProperties.setZoomFactor(0);
		currentOperation = GraphOperation.REPAINT;
		requestRepaint();
	}

	public boolean isTextsVisible() {
		return graphProperties.isTextsVisible();
	}

	public void zoomIn() {
		graphProperties.setZoomFactor(graphProperties.getZoomFactor() + 1);
		currentOperation = GraphOperation.SET_ZOOM;
		requestRepaint();
	}

	public void zoomOut() {
		graphProperties.setZoomFactor(graphProperties.getZoomFactor() - 1);
		currentOperation = GraphOperation.SET_ZOOM;
		requestRepaint();
	}

	public void refresh() {
		currentOperation = GraphOperation.REFRESH;
		requestRepaint();
	}
}
