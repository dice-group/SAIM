package de.uni_leipzig.simba.saim.cytoprocess;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.vaadin.contrib.component.svg.processing.Processing;
import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.VCytoprocess;

import com.mxgraph.view.mxGraph;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
//import cytoscape.CyMain;
//import cytoscape.CyNetwork;
//import cytoscape.Cytoscape;
//import cytoscape.CytoscapeInit;
//import cytoscape.init.CyInitParams;
//import cytoscape.layout.CyLayoutAlgorithm;
//import cytoscape.view.CyNetworkView;
/**
 * @author rspeck
 */
@ClientWidget(VCytoprocess.class)
public class Cytoprocess extends Processing {

	private static final long serialVersionUID = 4075833580638275769L;
	protected final Logger LOGGER = Logger.getLogger(Cytoprocess.class);

	public enum GraphOperation {
		REPAINT,
		ADD_EDGE,
		REFRESH_NODE_POSTIONS,
		UPDATE_NODE,
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

	protected final Map<String,Set<Integer>> operationMap = new HashMap<String,Set<Integer>>();


	/**
	 */
	public Cytoprocess(int width, int height){
		String name = "Cytoprocess";
//		CytoscapeInit.loadStaticProperties("mode", CytoscapeInit.getyInitParams().TEXT);
		String[] args = new String[1];
		args[0]= "-H";
//		try {
//			CyMain cyMain = new CyMain(args);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		cyMain.
//		Cytoscape.createNewSession();
//		System.out.println("CytoInits:\n"+CytoscapeInit.getCyInitParams());
//		CyNetwork network = Cytoscape.createNetwork(name, false);^
		mxGraph graph = new mxGraph();


		graphProperties = new GraphProperties(graph, name);
		graphProperties.setWidth(width);
		graphProperties.setHeight(height);

		operationMap.put("deleteNodes" , new HashSet<Integer>());
		operationMap.put("deleteEdges" , new HashSet<Integer>());

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

			paintController.repaint(target, graphProperties,operationMap);
			break;

		case ADD_EDGE:
			if(LOGGER.isDebugEnabled())LOGGER.debug("ADD_EDGE ...");

			paintController.addEdge(target, graphProperties);
			break;

		case REFRESH_NODE_POSTIONS:
			if(LOGGER.isDebugEnabled())LOGGER.debug("REFRESH_NODE_POSTIONS ...");

			paintController.refreshNodePositions(target, graphProperties);
			break;

		case UPDATE_NODE:
			if(LOGGER.isDebugEnabled())LOGGER.debug("UPDATE_NODE ...");

			paintController.updateNode(target, graphProperties);
			break;
		}

		target.endTag("cytoprocess");
	}

	// Handles client requests
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
		if (variables.containsKey("requestDone"))
			currentGraphOperation = GraphOperation.NOTHING;
	}

	public void deleteEdge(int id){
		operationMap.get("deleteEdges").add(id);
	}
	public void deleteNode(int id){
		operationMap.get("deleteNodes").add(id);
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
	public void applyLayoutAlgorithm(final CyLayoutAlgorithm loAlgorithm) {
		applyLayoutAlgorithm(loAlgorithm,false);
	}

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
