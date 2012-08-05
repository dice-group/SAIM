package org.vaadin.cytographer;

import giny.model.Edge;
import giny.model.Node;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import cytoscape.Cytoscape;
import cytoscape.visual.EdgeAppearance;
import cytoscape.visual.LineStyle;
import cytoscape.visual.NodeAppearance;
import cytoscape.visual.VisualPropertyType;

public class PaintController {
	
	// margin for fit to view (depends on node size)
	//private final int margin = 0;

	// colors
	private Map<Integer,Map<String,String>> colormaps = new HashMap<Integer,Map<String,String>>();
	private Map<String,String> colormap = new HashMap<String,String>();
	private String[] keys  = new String[]{"metric","operator","output","source","target"};
	private final String resource = "de/uni_leipzig/simba/saim/colors/default.properties";
	//
	private Set<Integer> paintedNodes = new HashSet<Integer>();	
	
	public void initDefaults(){
		InputStream in=getClass().getClassLoader().getResourceAsStream(resource);
	
		Properties properties =null;
		if(in != null){		
			properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(Integer pattern = 1; pattern < 5;pattern++){
				Map<String,String> cm = new HashMap<String,String>();
				for(String key: keys)			
					cm.put(key, properties.get(new String(key+pattern)).toString());
				
				colormaps.put(pattern, cm);
			}
		}
		// default colors
		setNodeColors(1);
		if(properties != null){
			
			final int NODESIZE = Integer.parseInt(properties.getProperty("nodesize"));
			//margin = (NODESIZE)/2;			
			final double EDGE_LABEL_OPACITY = Double.parseDouble(properties.getProperty("edge_label_opacity"));
			final int EDGE_LINE_WIDTH = Integer.parseInt(properties.getProperty("edge_line_width"));
			final Color BACKGROUND_COLOR = getColor(properties.getProperty("background_color"));
			final Color EDGE_COLOR = getColor(properties.getProperty("edge_color"));
			final Color EDGE_SELECTION_COLOR = getColor(properties.getProperty("edge_selection_color"));
			
			Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().setDefaultBackgroundColor(BACKGROUND_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_LINE_WIDTH,EDGE_LINE_WIDTH);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_COLOR,EDGE_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_LABEL_OPACITY,EDGE_LABEL_OPACITY);
			Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.NODE_SIZE, NODESIZE);
			Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().setDefaultEdgeSelectionColor(EDGE_SELECTION_COLOR);
		}
	}
	
	public void setNodeColors(int pattern){
		colormap = colormaps.get(pattern);
	}
	
	/**
	 */
	private Object getNodeAppearance (VisualPropertyType vpt){
		return Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().get(vpt);
	}
	
	/**
	 */
	private Object getEdgeAppearance (VisualPropertyType vpt){
		return Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().get(vpt);
	}

	/**
	 */
	public void repaintGraph(final PaintTarget paintTarget, final GraphProperties graphProperties) throws PaintException {
		
		if(colormap.size() != 0)
			for(Entry<String, String> e : colormap.entrySet())
				paintTarget.addAttribute(e.getKey(), e.getValue());
		
		paintTarget.addAttribute("title", graphProperties.getTitle());
		paintTarget.addAttribute("gwidth", graphProperties.getWidth());
		paintTarget.addAttribute("gheight", graphProperties.getHeight());
		paintTarget.addAttribute("texts", graphProperties.isTextsVisible());
		
		final Color ec   = (Color) getEdgeAppearance(VisualPropertyType.EDGE_COLOR);
		final Number elw = (Number) getEdgeAppearance(VisualPropertyType.EDGE_LINE_WIDTH);
		
		final Color nbc  = (Color) getNodeAppearance(VisualPropertyType.NODE_BORDER_COLOR);
		final Color nfc  = (Color) getNodeAppearance(VisualPropertyType.NODE_FILL_COLOR);
		final Color nlc  = (Color) getNodeAppearance(VisualPropertyType.NODE_LABEL_COLOR);
		final Color elc  = (Color) getNodeAppearance(VisualPropertyType.EDGE_LABEL_COLOR);
		final Number nbw = (Number) getNodeAppearance(VisualPropertyType.NODE_LINE_WIDTH);
		final Number efs = (Number) getNodeAppearance(VisualPropertyType.EDGE_FONT_SIZE);
		final Number nfs = (Number) getNodeAppearance(VisualPropertyType.NODE_FONT_SIZE);		
		final String dashArray = getDashArray((LineStyle) getEdgeAppearance(VisualPropertyType.EDGE_LINE_STYLE));

		Number ns = null;	
		if (graphProperties.getNodeSize() > 0) 
			ns = graphProperties.getNodeSize();	
		else
			ns = (Number) getNodeAppearance(VisualPropertyType.NODE_SIZE);

		final Color bc  = Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().getDefaultBackgroundColor();
		final Color nsc = Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().getDefaultNodeSelectionColor();
		final Color esc = Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().getDefaultEdgeSelectionColor();
		final double efo = (Double) Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().get(VisualPropertyType.EDGE_LABEL_OPACITY);
				
		paintTarget.addAttribute("ec", getRGB(ec));
		paintTarget.addAttribute("elw", elw.intValue());
		paintTarget.addAttribute("nbc", getRGB(nbc));
		paintTarget.addAttribute("nfc", getRGB(nfc));
		paintTarget.addAttribute("nlc", getRGB(nlc));
		paintTarget.addAttribute("elc", getRGB(elc));
		paintTarget.addAttribute("nbw", nbw.intValue());
		paintTarget.addAttribute("efs", efs.intValue());
		paintTarget.addAttribute("nfs", nfs.intValue());
		paintTarget.addAttribute("eda", dashArray);
		paintTarget.addAttribute("ns", ns.intValue());		
		paintTarget.addAttribute("bc", getRGB(bc));
		paintTarget.addAttribute("nsc", getRGB(nsc));
		paintTarget.addAttribute("esc", getRGB(esc));
		paintTarget.addAttribute("efo", efo);		
				
		int cytoscapeViewWidth=0, cytoscapeViewHeight=0, minX=0, minY=0;
		if (graphProperties.isUseFitting()) {

			minX = graphProperties.getMinX();
			minY = graphProperties.getMinY();
			
			cytoscapeViewWidth  = graphProperties.getMaxX() - minX;
			cytoscapeViewHeight = graphProperties.getMaxY() - minY;			
		}
		
		paintedNodes = new HashSet<Integer>();
		for (final int ei : graphProperties.getEdges()) {
			final Edge e = graphProperties.getCyNetwork().getEdge(ei);
			final Node node1 = e.getSource();
			final Node node2 = e.getTarget();
			paintedNodes.add(node1.getRootGraphIndex());
			paintedNodes.add(node2.getRootGraphIndex());

			paintTarget.startTag("e");
			paintTarget.addAttribute("name", e.getIdentifier());
			paintTarget.addAttribute("node1", node1.getIdentifier());
			paintTarget.addAttribute("node2", node2.getIdentifier());
			
			paintTarget.addAttribute("node1name", graphProperties.getNodeNames().get(Integer.parseInt(node1.getIdentifier())));
			paintTarget.addAttribute("node2name", graphProperties.getNodeNames().get(Integer.parseInt(node2.getIdentifier())));
			
			paintTarget.addAttribute("meta1", graphProperties.getNodeMetadata(node1.getIdentifier()).toString());
			paintTarget.addAttribute("meta2", graphProperties.getNodeMetadata(node2.getIdentifier()).toString());

			paintTarget.addAttribute("shape1", graphProperties.getShapes(node1.getIdentifier()).toString());
			paintTarget.addAttribute("shape2", graphProperties.getShapes(node2.getIdentifier()).toString());
						
			final double xx1 = graphProperties.getCyNetworkView().getNodeView(node1).getXPosition();
			final double yy1 = graphProperties.getCyNetworkView().getNodeView(node1).getYPosition();
			final double xx2 = graphProperties.getCyNetworkView().getNodeView(node2).getXPosition();
			final double yy2 = graphProperties.getCyNetworkView().getNodeView(node2).getYPosition();

			int x1 = (int) xx1;
			int y1 = (int) yy1;
			int x2 = (int) xx2;
			int y2 = (int) yy2;

//			if (graphProperties.isUseFitting()) {
//				x1 = margin + (int) ((xx1 - graphProperties.getMinX()) / graphProperties.getCytoscapeViewWidth() * (graphProperties.getWidth() - 2 * margin));
//				y1 = margin + (int) ((yy1 - graphProperties.getMinY()) / graphProperties.getCytoscapeViewHeight() * (graphProperties.getHeight() - 2 * margin));
//				x2 = margin + (int) ((xx2 - graphProperties.getMinX()) / graphProperties.getCytoscapeViewWidth() * (graphProperties.getWidth() - 2 * margin));
//				y2 = margin + (int) ((yy2 - graphProperties.getMinY()) / graphProperties.getCytoscapeViewHeight() * (graphProperties.getHeight() - 2 * margin));
//			}
			if (graphProperties.isUseFitting()) {
				
				x1 = (int) ((xx1 - minX) / cytoscapeViewWidth * (graphProperties.getWidth()));
				y1 = (int) ((yy1 - minY) / cytoscapeViewHeight * (graphProperties.getHeight()));
				x2 = (int) ((xx2 - minX) / cytoscapeViewWidth * (graphProperties.getWidth()));
				y2 = (int) ((yy2 - minY) / cytoscapeViewHeight * (graphProperties.getHeight()));
			}

			paintTarget.addAttribute("node1x", x1);
			paintTarget.addAttribute("node1y", y1);
			paintTarget.addAttribute("node2x", x2);
			paintTarget.addAttribute("node2y", y2);

			if (!graphProperties.isStyleOptimization()) {
				final EdgeAppearance ea = Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().calculateEdgeAppearance(e, graphProperties.getCyNetwork());
				final NodeAppearance n1a = Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().calculateNodeAppearance(node1, graphProperties.getCyNetwork());
				final NodeAppearance n2a = Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().calculateNodeAppearance(node2, graphProperties.getCyNetwork());

				final LineStyle _ls = (LineStyle) ea.get(VisualPropertyType.EDGE_LINE_STYLE);
				final String _dashArray = getDashArray(_ls);

				paintTarget.addAttribute("_ec", getRGB((Color) ea.get(VisualPropertyType.EDGE_COLOR)));
				paintTarget.addAttribute("_elw", ((Number) ea.get(VisualPropertyType.EDGE_LINE_WIDTH)).intValue());

				paintTarget.addAttribute("_n1bc", getRGB((Color) n1a.get(VisualPropertyType.NODE_BORDER_COLOR)));
				paintTarget.addAttribute("_n1fc", getRGB((Color) n1a.get(VisualPropertyType.NODE_FILL_COLOR)));
				paintTarget.addAttribute("_n1bw", ((Number) n1a.get(VisualPropertyType.NODE_LINE_WIDTH)).intValue());
				if (graphProperties.getNodeSize() > 0) {
					paintTarget.addAttribute("_n1s", ns.intValue());
				} else {
					paintTarget.addAttribute("_n1s", ((Number) n1a.get(VisualPropertyType.NODE_SIZE)).intValue());
				}

				paintTarget.addAttribute("_n2bc", getRGB((Color) n2a.get(VisualPropertyType.NODE_BORDER_COLOR)));
				paintTarget.addAttribute("_n2fc", getRGB((Color) n2a.get(VisualPropertyType.NODE_FILL_COLOR)));
				paintTarget.addAttribute("_n2bw", ((Number) n2a.get(VisualPropertyType.NODE_LINE_WIDTH)).intValue());
				paintTarget.addAttribute("_n2s", ((Number) n2a.get(VisualPropertyType.NODE_SIZE)).intValue());
				paintTarget.addAttribute("_eda", _dashArray);
			}
			paintTarget.endTag("e");
		}
		// paint also single nodes
		for (final int nodeIndex : graphProperties.getNodes()) {
			final Node node1 = graphProperties.getCyNetwork().getNode(nodeIndex);
			if (!paintedNodes.contains(node1.getRootGraphIndex())) {
				paintTarget.startTag("e");
				paintTarget.addAttribute("name", "tmp");
				paintTarget.addAttribute("node1", node1.getIdentifier());
				paintTarget.addAttribute("node1name", graphProperties.getNodeNames().get(Integer.parseInt(node1.getIdentifier())));
				paintTarget.addAttribute("meta1", graphProperties.getNodeMetadata(node1.getIdentifier()).toString());
							
				paintTarget.addAttribute("shape1", graphProperties.getShapes(node1.getIdentifier()).toString());
				
				final double xx1 = graphProperties.getCyNetworkView().getNodeView(node1).getXPosition();
				final double yy1 = graphProperties.getCyNetworkView().getNodeView(node1).getYPosition();
				final int x1 = (int) xx1;
				final int y1 = (int) yy1;
				paintTarget.addAttribute("node1x", x1);
				paintTarget.addAttribute("node1y", y1);
				if (!graphProperties.isStyleOptimization()) {
					final NodeAppearance n1a = Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().calculateNodeAppearance(node1, graphProperties.getCyNetwork());

					paintTarget.addAttribute("_n1bc", getRGB((Color) n1a.get(VisualPropertyType.NODE_BORDER_COLOR)));
					paintTarget.addAttribute("_n1fc", getRGB((Color) n1a.get(VisualPropertyType.NODE_FILL_COLOR)));
					paintTarget.addAttribute("_n1bw", ((Number) n1a.get(VisualPropertyType.NODE_LINE_WIDTH)).intValue());
					if (graphProperties.getNodeSize() > 0) {
						paintTarget.addAttribute("_n1s", ns.intValue());
					} else {
						paintTarget.addAttribute("_n1s", ((Number) n1a.get(VisualPropertyType.NODE_SIZE)).intValue());
					}
				}
				paintTarget.endTag("e");
			}
		}
	}

	private String getDashArray(final LineStyle ls) {
		String dashArray;
		switch (ls) {
		case DASH_DOT:
			dashArray = "4 1";
			break;
		case LONG_DASH:
			dashArray = "6 6";
			break;
		case EQUAL_DASH:
			dashArray = "4 4";
			break;
		case DOT:
			dashArray = "1 1";
			break;
		default:
			dashArray = " ";
		}
		return dashArray;
	}

	public void paintNodeSize(final PaintTarget target, final GraphProperties graphProperties) throws PaintException {
		target.addAttribute("ns", (int) graphProperties.getNodeSize());
	}

	public void paintTextVisibility(final PaintTarget target, final GraphProperties graphProperties) throws PaintException {
		target.addAttribute("texts", graphProperties.isTextsVisible());
	}

	/**
	 * @param rgb e.g.: "rgb(255,255,255)"
	 * @return returns a Color object respective to the rgb parameter or Color.WHITE if something wrong
	 */
	private Color getColor(final String rgb) {
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
	
	private String getRGB(final Color bc) {
		return "rgb(" + bc.getRed() + "," + bc.getGreen() + "," + bc.getBlue() + ")";
	}

	public void setZoom(final PaintTarget target, final GraphProperties graphProperties) throws PaintException {
		target.addAttribute("zoom", graphProperties.getZoomFactor());
	}
}
