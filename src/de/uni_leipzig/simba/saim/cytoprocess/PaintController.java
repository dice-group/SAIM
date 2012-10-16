package de.uni_leipzig.simba.saim.cytoprocess;

import giny.model.Edge;
import giny.model.Node;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import cytoscape.Cytoscape;
import cytoscape.visual.VisualPropertyType;
/**
 * 
 * @author rspeck
 *
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
		int[] ids = graphProperties.getCyNetwork().getNodeIndicesArray();
		for(int id : ids){
			target.startTag("node");
			target.addAttribute("nodeID", id);
			target.addAttribute("nodeX", Double.valueOf(graphProperties.getCyNetworkView().getNodeView(id).getXPosition()).intValue());
			target.addAttribute("nodeY", Double.valueOf(graphProperties.getCyNetworkView().getNodeView(id).getYPosition()).intValue());
			target.endTag("node");
		}
		target.endTag("refreshNodePositions");
	}
	
	public void addEdge(final PaintTarget target, GraphProperties graphProperties) throws PaintException{
		if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge...");
		int id = graphProperties.idsToUpdate.get(0);
		graphProperties.idsToUpdate.remove(0);
		
		final Edge edge = graphProperties.getCyNetwork().getEdge(id);
		if(edge != null){
			target.startTag("addEdge");
			target.addAttribute("edgeLabel", Cytoscape.getEdgeAttributes().getStringAttribute(String.valueOf(id), "label"));
			target.addAttribute("edgeID",id);
			target.addAttribute("edgeShape", "todo: with label or not etc");
			target.addAttribute("edgeSourceID", edge.getSource().getIdentifier());
			target.addAttribute("edgeTargetID", edge.getTarget().getIdentifier());
			target.endTag("addEdge");
		}else
			if(LOGGER.isDebugEnabled()) LOGGER.debug("addEdge failed! Edge not found!");
		
	}

	public void repaint(final PaintTarget target, GraphProperties graphProperties,Map<String,Set<Integer>> operationMap) throws PaintException{
		if(LOGGER.isDebugEnabled()) LOGGER.debug("repaintGraph...");
		
		final Color EDGE_COLOR        = (Color) getEdgeAppearance(VisualPropertyType.EDGE_COLOR);
		final Color NODE_BORDER_COLOR = (Color) getNodeAppearance(VisualPropertyType.NODE_BORDER_COLOR);
		final Color NODE_FILL_COLOR   = (Color) getNodeAppearance(VisualPropertyType.NODE_FILL_COLOR);
		final Color NODE_LABEL_COLOR  = (Color) getNodeAppearance(VisualPropertyType.NODE_LABEL_COLOR);
		final int NODE_SIZE           =  Math.round(Float.valueOf(String.valueOf(getNodeAppearance(VisualPropertyType.NODE_SIZE))));
		final Color EDGE_LABEL_COLOR  = (Color) getEdgeAppearance(VisualPropertyType.EDGE_LABEL_COLOR);
		final Number NODE_LINE_WIDTH  = (Number) getNodeAppearance(VisualPropertyType.NODE_LINE_WIDTH);
		
	
		final Font NODE_FONT_FACE = (Font)getNodeAppearance(VisualPropertyType.NODE_FONT_FACE);
		final Font EDGE_FONT_FACE = (Font)getEdgeAppearance(VisualPropertyType.EDGE_FONT_FACE);
				
		final Color DefaultBackgroundColor    = Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().getDefaultBackgroundColor();
		final Color DefaultNodeSelectionColor = Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().getDefaultNodeSelectionColor();
		final Color DefaultEdgeSelectionColor = Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().getDefaultEdgeSelectionColor();
		final float EDGE_LABEL_OPACITY        = Float.valueOf(String.valueOf(getEdgeAppearance(VisualPropertyType.EDGE_LABEL_OPACITY)));
		final int   EDGE_LINE_WIDTH           = Double.valueOf(String.valueOf(getEdgeAppearance(VisualPropertyType.EDGE_LINE_WIDTH))).intValue();
		
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
		for (final int edgeid : graphProperties.getCyNetwork().getEdgeIndicesArray()) {	

			final Edge edge = graphProperties.getCyNetwork().getEdge(edgeid);
			final Node edgeSource = edge.getSource();
			final Node edgeTarget = edge.getTarget();

			paintedNodes.add(edgeSource.getRootGraphIndex());
			paintedNodes.add(edgeTarget.getRootGraphIndex());

			target.startTag("edge");
			//		
				target.addAttribute("edgeLabel", Cytoscape.getEdgeAttributes().getStringAttribute(String.valueOf(edge.getIdentifier()), "label"));
				target.addAttribute("edgeID", edge.getIdentifier());
				target.addAttribute("edgeType", "todo: with label or not etc");
			
				target.startTag("sourceNode");
				addNode(target, graphProperties,Integer.parseInt(edgeSource.getIdentifier()));
				target.endTag("sourceNode");
			
				target.startTag("targetNode");
				addNode(target, graphProperties,Integer.parseInt(edgeTarget.getIdentifier()));
				target.endTag("targetNode");
			//
			target.endTag("edge");
		}

		// paint single nodes
		for (final int nodeid : graphProperties.getCyNetwork().getNodeIndicesArray()) {
			if(!paintedNodes.contains(nodeid)){
				
				target.startTag("node");
				addNode(target, graphProperties,nodeid);
				target.endTag("node");
			}
		}
		
		// delete Node
		Set<Integer> deleteNodes = operationMap.get("deleteNodes");
		for(int id : deleteNodes){		
			int[] edges = graphProperties.getCyNetwork().getAdjacentEdgeIndicesArray(id, true, true, true);		
			boolean removed = graphProperties.getCyNetwork().removeNode(id, true);
			
			List<String> sEdges = new ArrayList<String>();
			for(int i = 0; i < edges.length; sEdges.add(String.valueOf(edges[i++])));
			for(int i = 0; i < edges.length && removed; graphProperties.getCyNetwork().removeEdge(edges[i++], true));
				
			target.startTag("deleteNode");
			target.addAttribute("nodeID", id);
			target.addAttribute("removed", removed);
			if(removed && edges.length > 0)
				target.addAttribute("edges", sEdges.toString());
			target.endTag("deleteNode");
			}
		deleteNodes.clear();
		
		// delete Edge
		Set<Integer> deleteEdges = operationMap.get("deleteEdges");
		for(int id : deleteEdges){		
		
			target.startTag("deleteEdge");
			boolean removed = graphProperties.getCyNetwork().removeEdge(id, true);
			
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
		
		if(Cytoscape.getNodeAttributes().hasAttribute(id+"", "label1")){
			target.addAttribute("label1",Cytoscape.getNodeAttributes().getStringAttribute(id+"", "label1"));
			target.addAttribute("value1",Cytoscape.getNodeAttributes().getDoubleAttribute(id+"", "value1"));
		}
		
		if(Cytoscape.getNodeAttributes().hasAttribute(id+"", "label2")){
			target.addAttribute("label2",Cytoscape.getNodeAttributes().getStringAttribute(id+"", "label2"));
			target.addAttribute("value2",Cytoscape.getNodeAttributes().getDoubleAttribute(id+"", "value2"));
		}
	}
	
	private void addNode(final PaintTarget target,GraphProperties graphProperties, int nodeid) throws PaintException{
		
		final Node node = graphProperties.getCyNetwork().getNode(nodeid);		
		target.addAttribute("nodeID", nodeid);
		target.addAttribute("nodeLabel",Cytoscape.getNodeAttributes().getStringAttribute (String.valueOf(nodeid), "label"));
		target.addAttribute("nodeColor",Cytoscape.getNodeAttributes().getStringAttribute (String.valueOf(nodeid), "color"));
		target.addAttribute("nodeX", Double.valueOf(graphProperties.getCyNetworkView().getNodeView(node).getXPosition()).intValue());
		target.addAttribute("nodeY", Double.valueOf(graphProperties.getCyNetworkView().getNodeView(node).getYPosition()).intValue());
		target.addAttribute("nodeShape",Cytoscape.getNodeAttributes().getIntegerAttribute (String.valueOf(nodeid), "shape"));
		
		//if(LOGGER.isDebugEnabled()) LOGGER.debug("shape: "+ graphProperties.getCyNetworkView().getNodeView(nodeid).getShape());
		
		updateNode(target,graphProperties,nodeid);
	}

	private Object getNodeAppearance (VisualPropertyType vpt){
		return Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().get(vpt);
	}

	private Object getEdgeAppearance (VisualPropertyType vpt){
		return Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().get(vpt);
	}
	
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
