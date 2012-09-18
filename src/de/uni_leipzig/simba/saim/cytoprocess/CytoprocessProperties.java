package de.uni_leipzig.simba.saim.cytoprocess;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cytoscape.Cytoscape;
import cytoscape.visual.VisualPropertyType;
/**
 * 
 * @author rspeck
 *
 */
public class CytoprocessProperties {

	public static final String resource = "de/uni_leipzig/simba/saim/cytoprocess/cytoprocess.properties";

	public static void defaults(){
		InputStream in=CytoprocessProperties.class.getClassLoader().getResourceAsStream(resource);

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
			
		}

		if(properties != null){

			final int NODESIZE = Integer.parseInt(properties.getProperty("nodesize"));
			//margin = (NODESIZE)/2;			
			final double EDGE_LABEL_OPACITY = Double.parseDouble(properties.getProperty("edge_label_opacity"));
			final int EDGE_LINE_WIDTH = Integer.parseInt(properties.getProperty("edge_line_width"));
			final int NODE_LINE_WIDTH = Integer.parseInt(properties.getProperty("node_line_width"));
			final Color BACKGROUND_COLOR = getColor(properties.getProperty("background_color"));
			final Color NODE_LINE_COLOR = getColor(properties.getProperty("node_line_color"));
			final Color EDGE_COLOR = getColor(properties.getProperty("edge_color"));
			final Color EDGE_LABEL_COLOR = getColor(properties.getProperty("edge_label_color"));
			final Color EDGE_SELECTION_COLOR = getColor(properties.getProperty("edge_selection_color"));
			final int EDGE_FONT_SIZE = Integer.parseInt(properties.getProperty("edge_font_size"));
			final int NODE_FONT_SIZE = Integer.parseInt(properties.getProperty("node_font_size"));
			final String NODE_FONT = (properties.getProperty("node_font"));
			final String EDGE_FONT = (properties.getProperty("edge_font"));
			
			Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().setDefaultBackgroundColor(BACKGROUND_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_LINE_WIDTH,EDGE_LINE_WIDTH);
			Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.NODE_LINE_WIDTH, NODE_LINE_WIDTH);
			Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.NODE_BORDER_COLOR, NODE_LINE_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_COLOR,EDGE_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_LABEL_OPACITY,EDGE_LABEL_OPACITY);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_LABEL_COLOR,EDGE_LABEL_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.NODE_SIZE, NODESIZE);
			Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().setDefaultEdgeSelectionColor(EDGE_SELECTION_COLOR);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_FONT_SIZE,EDGE_FONT_SIZE);
			Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(
					VisualPropertyType.EDGE_FONT_FACE,
					new Font(EDGE_FONT, Font.PLAIN, EDGE_FONT_SIZE));
			
			Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.NODE_FONT_SIZE,NODE_FONT_SIZE);			
			Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(
					VisualPropertyType.NODE_FONT_FACE,
					new Font(NODE_FONT, Font.PLAIN, NODE_FONT_SIZE));
			
						
		}
	}
	
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
