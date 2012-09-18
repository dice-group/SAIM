package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph;

import com.vaadin.terminal.gwt.client.UIDL;

public class VVisualStyle {

	public int width = 0, height = 0;
	public String title = "";
	public int nodeSize = 50;
	
	public String NODE_BORDER_COLOR, NODE_FILL_COLOR, NODE_LABEL_COLOR,
			EDGE_LABEL_COLOR,EDGE_COLOR, NODE_LINE_WIDTH, NODE_FONT_SIZE, NODE_FONT_NAME,EDGE_FONT_NAME,EDGE_FONT_SIZE,
			DefaultBackgroundColor, DefaultNodeSelectionColor,
			DefaultEdgeSelectionColor, EDGE_LABEL_OPACITY;
	
	public int EDGE_LINE_WIDTH;

	// parse settings
	public void parseUIDL(final UIDL child) {

		width = child.getIntAttribute("graphWidth");
		height = child.getIntAttribute("graphHeight");
		title = child.getStringAttribute("title");
		nodeSize = child.getIntAttribute("NODE_SIZE");

		NODE_BORDER_COLOR         = child.getStringAttribute("NODE_BORDER_COLOR");
		NODE_FILL_COLOR           = child.getStringAttribute("NODE_FILL_COLOR");
		NODE_LABEL_COLOR          = child.getStringAttribute("NODE_LABEL_COLOR");
		EDGE_LABEL_COLOR          = child.getStringAttribute("EDGE_LABEL_COLOR");
		EDGE_COLOR                = child.getStringAttribute("EDGE_COLOR");
		NODE_LINE_WIDTH           = child.getStringAttribute("NODE_LINE_WIDTH");
		NODE_FONT_SIZE            = child.getStringAttribute("NODE_FONT_SIZE");
		NODE_FONT_NAME            = child.getStringAttribute("NODE_FONT_NAME");
		EDGE_FONT_SIZE            = child.getStringAttribute("EDGE_FONT_SIZE");
		EDGE_FONT_NAME            = child.getStringAttribute("EDGE_FONT_NAME");
		EDGE_LINE_WIDTH           = child.getIntAttribute("EDGE_LINE_WIDTH");
		DefaultBackgroundColor    = child.getStringAttribute("DefaultBackgroundColor");
		DefaultNodeSelectionColor = child.getStringAttribute("DefaultNodeSelectionColor");
		DefaultEdgeSelectionColor = child.getStringAttribute("DefaultEdgeSelectionColor");
		EDGE_LABEL_OPACITY        = child.getStringAttribute("EDGE_LABEL_OPACITY");
		
	}
}
