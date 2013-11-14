package de.uni_leipzig.simba.saim.cytoprocess;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

/**
 * @author rspeck
 */
public class PaintController {

	private static transient final Logger LOGGER = Logger.getLogger(PaintController.class);


	public void fitToView(final PaintTarget target, GraphProperties graphProperties) throws PaintException{
		target.startTag("fitToView");
		target.endTag("fitToView");
	}

	public void updateNode(final PaintTarget target, GraphProperties graphProperties) throws PaintException{
		int id = graphProperties.idsToUpdate.get(0);
		graphProperties.idsToUpdate.remove(0);

		target.startTag("updateNode");
		target.addAttribute("nodeID", id);
		updateNode(target,graphProperties,id);
		target.endTag("updateNode");
	}

	/**
	 * refreshNodePositions
	 */
	public void refreshNodePositions(final PaintTarget target, GraphProperties graphProperties) throws PaintException{
		target.startTag("refreshNodePositions");
//		mxGraph graph= graphProperties.getNetwork();
		
		for(ViewNode n : graphProperties.getModel().nodes) {
			target.startTag("node");
			target.addAttribute("nodeID", n.id);
			target.addAttribute("nodeX", Double.valueOf(n.x));
			target.addAttribute("nodeY", Double.valueOf(n.y));
			target.endTag("node");
		}
		target.endTag("refreshNodePositions");
	}

	public void addEdge(final PaintTarget target, GraphProperties graphProperties) throws PaintException{
		if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge...");
		int id = graphProperties.idsToUpdate.get(0);
		graphProperties.idsToUpdate.remove(0);
		
		final Edge edge = graphProperties.getModel().getEdge(id);
//		graphProperties
		if(edge != null){
			target.startTag("addEdge");
			target.addAttribute("edgeLabel", edge.name);
			target.addAttribute("edgeID",id);
			target.addAttribute("edgeShape", "todo: with label or not etc");
			target.addAttribute("edgeSourceID", edge.nodeA.id);
			target.addAttribute("edgeTargetID", edge.nodeB.id);
			target.endTag("addEdge");
		}else
			if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge failed! Edge not found!");

	}

	public void repaint(final PaintTarget target, GraphProperties graphProperties,Map<String,Set<Integer>> operationMap) throws PaintException{
		if(LOGGER.isDebugEnabled()) LOGGER.debug("repaintGraph...");

		final Color EDGE_COLOR        = Color.black;
		final Color NODE_BORDER_COLOR = Color.black;
		final Color NODE_FILL_COLOR   = Color.CYAN;
		final Color NODE_LABEL_COLOR  = Color.black;
		final int NODE_SIZE           = 120;//Math.round(Float.valueOf(String.valueOf(getNodeAppearance(VisualPropertyType.NODE_SIZE))));
		final Color EDGE_LABEL_COLOR  = Color.black;
		final Number NODE_LINE_WIDTH  = 1;

		final Font NODE_FONT_FACE = Font.decode(Font.DIALOG);
		final Font EDGE_FONT_FACE = Font.decode(Font.DIALOG);

		final Color DefaultBackgroundColor    = Color.white;
		final Color DefaultNodeSelectionColor = Color.GRAY;
		final Color DefaultEdgeSelectionColor = Color.gray;
		final float EDGE_LABEL_OPACITY        = 0.1f;
		final int   EDGE_LINE_WIDTH           = 1;

		// settings
		target.startTag("settings");
		target.addAttribute("title", graphProperties.getTitle());
		target.addAttribute("graphWidth", graphProperties.getWidth());
		target.addAttribute("graphHeight", graphProperties.getHeight());

		target.addAttribute("EDGE_COLOR",PaintController.getRGB(EDGE_COLOR));

		target.addAttribute("NODE_BORDER_COLOR",PaintController.getRGB(NODE_BORDER_COLOR));
		target.addAttribute("NODE_FILL_COLOR",PaintController.getRGB(NODE_FILL_COLOR));
		target.addAttribute("NODE_LABEL_COLOR",PaintController.getRGB(NODE_LABEL_COLOR));
		target.addAttribute("NODE_SIZE", NODE_SIZE);
		target.addAttribute("EDGE_LABEL_COLOR",PaintController.getRGB(EDGE_LABEL_COLOR));

		target.addAttribute("NODE_LINE_WIDTH", NODE_LINE_WIDTH.intValue());
		target.addAttribute("EDGE_LINE_WIDTH", EDGE_LINE_WIDTH);

		target.addAttribute("NODE_FONT_SIZE", NODE_FONT_FACE.getSize());
		target.addAttribute("NODE_FONT_NAME", NODE_FONT_FACE.getFontName());

		target.addAttribute("EDGE_FONT_SIZE", EDGE_FONT_FACE.getSize());
		target.addAttribute("EDGE_FONT_NAME", EDGE_FONT_FACE.getFontName());

		target.addAttribute("DefaultBackgroundColor",PaintController.getRGB(DefaultBackgroundColor));
		target.addAttribute("DefaultNodeSelectionColor",PaintController.getRGB(DefaultNodeSelectionColor));
		target.addAttribute("DefaultEdgeSelectionColor",PaintController.getRGB(DefaultEdgeSelectionColor));
		target.addAttribute("EDGE_LABEL_OPACITY",EDGE_LABEL_OPACITY);
		target.endTag("settings");
		// settings

		Set<Integer> paintedNodes = new HashSet<Integer>();
		// paint edge and there nodes
		HashSet<Edge> edges = graphProperties.getModel().edges;
		for(Edge e:edges) {

			paintedNodes.add(e.nodeA.id);
			paintedNodes.add(e.nodeB.id);

			target.startTag("edge");
			//
				target.addAttribute("edgeLabel", e.name);
				target.addAttribute("edgeID", e.id);
				target.addAttribute("edgeType", "todo: with label or not etc");

				target.startTag("sourceNode");
				addNode(target, graphProperties, e.nodeA.id);
				target.endTag("sourceNode");

				target.startTag("targetNode");
				addNode(target, graphProperties,e.nodeB.id);
				target.endTag("targetNode");
			//
			target.endTag("edge");
		}

		// paint single nodes
		for (ViewNode n : graphProperties.getModel().nodes) {
			if(!paintedNodes.contains(n.id)){

				target.startTag("node");
				addNode(target, graphProperties,n.id);
				target.endTag("node");
			}
		}

		// delete Node
		Set<Integer> deleteNodes = operationMap.get("deleteNodes");
		for(int id : deleteNodes){
			List<Integer> delEdges = graphProperties.getModel().getAdjacentEdges(id);
			boolean removed = graphProperties.removeNode(id);

			List<String> sEdges = new Vector<String>();
			for(int i = 0; i < delEdges.size(); sEdges.add(String.valueOf(delEdges.get(i++))));
			for(int i = 0; i < delEdges.size() && removed; graphProperties.removeEdge(delEdges.get(i++)));

			target.startTag("deleteNode");
			target.addAttribute("nodeID", id);
			target.addAttribute("removed", removed);
			if(removed && delEdges.size() > 0)
				target.addAttribute("edges", sEdges.toString());
			target.endTag("deleteNode");
			}
		deleteNodes.clear();

		// delete Edge
		Set<Integer> deleteEdges = operationMap.get("deleteEdges");
		for(int id : deleteEdges){

			target.startTag("deleteEdge");
			boolean removed = graphProperties.removeEdge(id);

			target.addAttribute("edgeID", id);
			target.addAttribute("edgeRemoved", removed);

			target.endTag("deleteEdge");
		}
		deleteEdges.clear();
	}


	//
	//
	// private
	//
	//
	private void updateNode(final PaintTarget target, GraphProperties graphProperties,int id) throws PaintException{

		if(graphProperties.getNode(id).labeling.containsKey("label1")){
			target.addAttribute("label1",(String)graphProperties.getNode(id).labeling.get("label1"));
			target.addAttribute("value1",(Double)graphProperties.getNode(id).labeling.get("value1"));
		}

		if(graphProperties.getNode(id).labeling.containsKey("label2")){
			target.addAttribute("label2",(String)graphProperties.getNode(id).labeling.get("label2"));
			target.addAttribute("value2",(Double)graphProperties.getNode(id).labeling.get("value2"));
		}
	}

	private void addNode(final PaintTarget target,GraphProperties graphProperties, int nodeid) throws PaintException{

		final ViewNode node = graphProperties.getNode(nodeid);
		target.addAttribute("nodeID", nodeid);
		target.addAttribute("nodeLabel",node.name);
		target.addAttribute("nodeColor",node.rgb);
		target.addAttribute("nodeX", new Double(node.x));
		target.addAttribute("nodeY", new Double(node.y));
		target.addAttribute("nodeShape",node.nodeViewShape);

		//if(LOGGER.isDebugEnabled()) LOGGER.debug("shape: "+ graphProperties.getCyNetworkView().getNodeView(nodeid).getShape());

		updateNode(target,graphProperties,nodeid);
	}

//	private Object getNodeAppearance (VisualPropertyType vpt){
//		return Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().get(vpt);
//	}
//
//	private Object getEdgeAppearance (VisualPropertyType vpt){
//		return Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().get(vpt);
//	}

	//
	//
	// static
	//
	//

	/**
	 * @param rgb e.g.: "rgb(255,255,255)"
	 * @return returns a Color object respective to the rgb parameter or Color.WHITE if something wrong
	 */
	public static Color getColor(final String rgb) {
		try{
			String tmprgb=rgb.substring(rgb.lastIndexOf("(")+1, rgb.lastIndexOf(")"));
			String colors[] =  tmprgb.split(",");
		if(colors.length == 3)
			return new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		}catch(Exception e){
			return Color.WHITE;
		}
		return Color.WHITE;
	}

	public static String getRGB(final Color bc) {
		return "rgb(" + bc.getRed() + "," + bc.getGreen() + "," + bc.getBlue() + ")";
	}
}
