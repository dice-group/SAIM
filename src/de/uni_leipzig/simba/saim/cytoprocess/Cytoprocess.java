package de.uni_leipzig.simba.saim.cytoprocess;

import java.util.Map;

import org.apache.log4j.Logger;
import org.vaadin.contrib.component.svg.processing.Processing;

import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.VCytoprocess;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.layout.CyLayoutAlgorithm;
import cytoscape.view.CyNetworkView;
/**
 * 
 * @author rspeck
 *
 */
@ClientWidget(VCytoprocess.class)
public class Cytoprocess extends Processing {

	private static final long serialVersionUID = 4075833580638275769L;
	protected final Logger LOGGER = Logger.getLogger(Cytoprocess.class);
	
	public enum GraphOperation {
		REPAINT, 
		ADD_EDGE,
		DELETE_NODE,
		REFRESH_NODE_POSTIONS,
		DELETE_EDGE,
		UPDATE_NODE, 
	//	UPDATE_EDGE,
		FIT_TO_VIEW, // is used as operation on client side
		ZOOMIN,
		ZOOMOUT,
		SCALEIN,
		SCALEOUT,		
		NOTHING
	}
	protected GraphOperation currentGraphOperation = GraphOperation.NOTHING;
	protected final GraphProperties graphProperties;
	protected final PaintController paintController = new PaintController();
	
	/**
	 * 
	 */
	public Cytoprocess(int width, int height){
		String name = "Cytoprocess";
		
		Cytoscape.createNewSession();
		CyNetwork network = Cytoscape.createNetwork(name, false);			
		CyNetworkView finalView = Cytoscape.createNetworkView(network);		
		
		graphProperties = new GraphProperties(network, finalView, name);
		graphProperties.setWidth(width);
		graphProperties.setHeight(height);
		
	}

	@Override
	public void paintContent(final PaintTarget target) throws PaintException {
		super.paintContent(target);
		if(LOGGER.isDebugEnabled())LOGGER.debug("request to paintContent ...");
		
		target.startTag("cytoprocess");		
		target.addAttribute("operation", currentGraphOperation.toString());
		switch (currentGraphOperation) {
		
		case REPAINT:
			if(LOGGER.isDebugEnabled())LOGGER.debug("REPAINT ...");
			
			paintController.repaint(target, graphProperties);
			break;
			
		case ADD_EDGE:
			if(LOGGER.isDebugEnabled())LOGGER.debug("ADD_EDGE ...");
			
			paintController.addEdge(target, graphProperties);
			break;
		
		case DELETE_NODE:
			if(LOGGER.isDebugEnabled())LOGGER.debug("DELETE_NODE ...");
			
			paintController.deleteNode(target, graphProperties);
			break;
		
		case REFRESH_NODE_POSTIONS:
			if(LOGGER.isDebugEnabled())LOGGER.debug("REFRESH_NODE_POSTIONS ...");
			
			paintController.refreshNodePositions(target, graphProperties);
			break;
		
		case DELETE_EDGE:
			if(LOGGER.isDebugEnabled())LOGGER.debug("DELETE_EDGE ...");
			
			paintController.deleteEdge(target, graphProperties);
			break;
			
		case UPDATE_NODE:
			if(LOGGER.isDebugEnabled())LOGGER.debug("UPDATE_NODE ...");
			
			paintController.updateNode(target, graphProperties);
			break;	
		}
			
		target.endTag("cytoprocess");
	}

	@Override
	public void changeVariables(final Object source, final Map<String, Object> variables) {
		super.changeVariables(source, variables);
		if(LOGGER.isDebugEnabled()) LOGGER.debug("changeVariables...");
		
		// edgeCreated
		if (variables.containsKey("linkTo")) {
			if(LOGGER.isDebugEnabled()) LOGGER.debug("changeVariables edgeCreated...");
			
			final String[] edgeCreated = (String[]) variables.get("linkTo");
			Integer id = addEdge(Integer.valueOf(edgeCreated[0]),Integer.valueOf(edgeCreated[1]),edgeCreated[2]);
			if(id != null){
				graphProperties.idsToUpdate.clear();
				graphProperties.idsToUpdate.add(id);
				currentGraphOperation = GraphOperation.ADD_EDGE;
				requestRepaint();
			}
		}
		
		// deleteNode
		if (variables.containsKey("deleteNode")) {
			if(LOGGER.isDebugEnabled()) LOGGER.debug("changeVariables deleteNode...");
			
			deleteNode(Integer.valueOf(variables.get("deleteNode").toString()));
		}
		
		// deleteEdge
		if (variables.containsKey("deleteEdge")) {
			if(LOGGER.isDebugEnabled()) LOGGER.debug("changeVariables deleteEdge...");
			
			deleteEdge(Integer.valueOf(variables.get("deleteEdge").toString()));
		}
		
		// nodeDoubleClick
		if (variables.containsKey("nodeDoubleClick")) {
			if(LOGGER.isDebugEnabled()) LOGGER.debug("changeVariables nodeDoubleClick...");
			
			final String[] vars = (String[]) variables.get("nodeDoubleClick");
			
			int id = Integer.valueOf(vars[0]);
			int  x = Integer.valueOf(vars[1]);
			int  y = Integer.valueOf(vars[2]);
			
			graphProperties.idsToUpdate.add(id);
			nodeDoubleClick(id,x,y);
		}
		
		// edgeDoubleClick
		if (variables.containsKey("edgeDoubleClick")) {
			if(LOGGER.isDebugEnabled()) LOGGER.debug("changeVariables edgeDoubleClick...");
			
//			final String[] vars = (String[]) variables.get("edgeDoubleClick");
			
//			int id = Integer.valueOf(vars[0]);
//			int  x = Integer.valueOf(vars[1]);
//			int  y = Integer.valueOf(vars[2]);
			
			//TODO edgeDoubleClick maybe?
			//graphProperties.idsToUpdate.add(id);
			//edgeDoubleClick(id+"",x,y);
		}
	}

	public void deleteEdge(int id){
		graphProperties.idsToUpdate.clear();
		graphProperties.idsToUpdate.add(id);
		currentGraphOperation = GraphOperation.DELETE_EDGE;
		requestRepaint();
	}
	public void deleteNode(int id){
		graphProperties.idsToUpdate.clear();
		graphProperties.idsToUpdate.add(id);
		currentGraphOperation = GraphOperation.DELETE_NODE;
		requestRepaint();
	}
	
	/** delegate to GraphProperties.addNode(...) **/
	public int addNode(String name, int x, int y, int nodeViewShape, String rgb){		
		return addNode(name, x, y, nodeViewShape, rgb, false);
	}
	/** delegate to GraphProperties.addNode(...) **/
	public int addNode(String name, int x, int y, int nodeViewShape, String rgb, boolean update){
		
		int id = graphProperties.addNode(name, x, y, nodeViewShape, rgb);
		return id;
	}	

	/** delegate to GraphProperties.addEdge(...) **/
	public Integer addEdge(int nodeAid, int nodeBid, String attribute) {
		return graphProperties.addEdge(nodeAid, nodeBid, attribute);
	}
	
	public void makeGraphOperation(GraphOperation go) {
		currentGraphOperation = go;
		requestRepaint();
	}
	

	public void repaintGraph() {
		currentGraphOperation = GraphOperation.REPAINT;
		requestRepaint();
	}
	
//	public void cleanGraph(){
//		// TODO
//	}
	
	public void applyLayoutAlgorithm(final CyLayoutAlgorithm loAlgorithm,boolean update) {
		graphProperties.applyLayoutAlgorithm(loAlgorithm);
		if(update){
			currentGraphOperation = GraphOperation.REFRESH_NODE_POSTIONS;
			requestRepaint();
		}
	}
	
	/** Override this to handle node double clicks */
	public void nodeDoubleClick(int nodeid, int x, int y){
		currentGraphOperation = GraphOperation.UPDATE_NODE;
		requestRepaint();
	}
//	/** Override this*/
//	public void edgeDoubleClick(String edgeid, int x, int y){
//		currentGraphOperation = GraphOperation.UPDATE_EDGE;
//		requestRepaint();
//	}

	public void fitToView(){
		currentGraphOperation = GraphOperation.FIT_TO_VIEW;
		requestRepaint();
	}
	
	public void zoomIn(boolean zoomIn){
		if(zoomIn)
			currentGraphOperation = GraphOperation.ZOOMIN;
		else
			currentGraphOperation = GraphOperation.ZOOMOUT;
			requestRepaint();
	}
	
	public void scaleIn(boolean scaleIn){
		if(scaleIn)
			currentGraphOperation = GraphOperation.SCALEIN;
		else
			currentGraphOperation = GraphOperation.SCALEOUT;
			requestRepaint();
	}
	
	public void updateNode(){
		currentGraphOperation = GraphOperation.UPDATE_NODE;
		requestRepaint();
	}
}