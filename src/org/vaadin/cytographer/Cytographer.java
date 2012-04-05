package org.vaadin.cytographer;

import java.util.Map;

import org.vaadin.cytographer.ctrl.PaintController;
import org.vaadin.cytographer.model.GraphProperties;

import com.vaadin.data.Container;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;
import org.vaadin.cytographer.widgetset.client.ui.VCytographer;

import cytoscape.CyNetwork;
import cytoscape.view.CyNetworkView;

/**
 * Server side component for the VCytographer widget.
 */
@ClientWidget(VCytographer.class)
public class Cytographer extends AbstractComponent {
	private static final long serialVersionUID = 8483008141219579936L;

	public enum Shape {
		CYCLE, RECTANGLE,TRIANGLE,DIAMOND
	}
	
	public enum GraphOperation {
		REPAINT, SET_NODE_SIZE, SET_VISUAL_STYLE, SET_TEXT_VISIBILITY, SET_OPTIMIZED_STYLES, UPDATE_NODE, SET_ZOOM, REFRESH
	}

	private GraphOperation currentOperation = GraphOperation.REPAINT;

	private final GraphProperties graphProperties;
	private final PaintController paintController = new PaintController();

	private String updatedNode;
	/**
	 * 
	 */
	public Cytographer(final CyNetwork network, final CyNetworkView finalView, final String title, final int width, final int height) {
		
		graphProperties = new GraphProperties(network, finalView, title);
		graphProperties.setWidth(width);
		graphProperties.setHeight(height);
	}

	public void addNode(String id, int x, int y, Shape shape){
		switch(shape){
		case CYCLE: id = "c." + id; break;
		case RECTANGLE : id = "r."+id;break;
		case DIAMOND: id = "d."+id;break;
		default:
		case TRIANGLE : id = "t."+id;break;
		}
		graphProperties.addANewNode(id, x, y);
	}
	public void createAnEdge(final String[] ids){
		graphProperties.createAnEdge(ids);
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
		case SET_VISUAL_STYLE:
			paintController.paintVisualStyle(target, graphProperties);
			break;
		case SET_TEXT_VISIBILITY:
			paintController.paintTextVisibility(target, graphProperties);
			break;
		case SET_OPTIMIZED_STYLES:
			paintController.paintOptimizedStyles(target, graphProperties);
			break;
		case UPDATE_NODE:
			paintController.updateNode(target, graphProperties, updatedNode);
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
		graphProperties.setFitting(false);
	}

	/**
	 * Receive and handle events and other variable changes from the client.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void changeVariables(final Object source, final Map<String, Object> variables) {
		
		super.changeVariables(source, variables);
		
		if (variables.containsKey("selectedEdges")) {
			graphProperties.clearSelectedEdges();
			final String[] selectedEdges = (String[]) variables.get("selectedEdges");
			for (final String edge : selectedEdges) 
				graphProperties.addSelectedEdge(edge);

			System.out.printf("Selected %d edges\n", graphProperties.getSelectedEdges().size());
		}
		if (variables.containsKey("selectedNodes")) {
			graphProperties.clearSelectedNodes();
			final String[] strs = (String[]) variables.get("selectedNodes");
			for (final String str : strs) {
				graphProperties.addSelectedNode(str);
			}
			System.out.printf("Selected %d nodes\n", graphProperties.getSelectedNodes().size());
		}
		if (variables.containsKey("zoomFactor")) {
			graphProperties.setZoomFactor((Integer) variables.get("zoomFactor"));
		}
		if (variables.containsKey("createdANode")) {
			final Object[] nodeData = (Object[]) variables.get("createdANode");
			graphProperties.addANewNode((String) nodeData[0], (Integer) nodeData[1], (Integer) nodeData[2]);
		}
		if (variables.containsKey("removedNode")) {
			graphProperties.removeNode((String) variables.get("removedNode"));
		}
		if (variables.containsKey("edgeCreated")) {
			graphProperties.createAnEdge((String[]) variables.get("edgeCreated"));
		}
		if (variables.containsKey("removedEdge")) {
			graphProperties.removeEdge((String) variables.get("removedEdge"));
		}
	}


	/**
	 * Change texts visibilities
	 * 
	 * @param b
	 */
	public void setTextVisible(final boolean b) {
		currentOperation = GraphOperation.SET_TEXT_VISIBILITY;
		graphProperties.setTextVisible(b);
		requestRepaint();
	}

	/**
	 * Optimize styles to minimize client-server traffic
	 * 
	 * @param b
	 */
	public void setStyleOptimization(final boolean b) {
		currentOperation = GraphOperation.SET_OPTIMIZED_STYLES;
		graphProperties.setStyleOptimization(b);
		requestRepaint();
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

	public void setOptimizedStyles(final boolean b) {
		graphProperties.setStyleOptimization(b);
	}

	public Container getNodeAttributeContainerForSelectedNodes() {
		return graphProperties.getNodeAttributeContainerForSelectedNodes();
	}

	public void fitToView() {
		graphProperties.measureDimensions();
		graphProperties.setFitting(true);
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
