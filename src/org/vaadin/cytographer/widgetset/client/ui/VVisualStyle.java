package org.vaadin.cytographer.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.UIDL;

public class VVisualStyle {

	private String bgColor;
	private String edgeColor;
	private String nodeBorderColor;
	private String nodeFillColor;
	private String nodeSelectionColor;
	private String edgeSelectionColor;
	private String nodeLabelColor;
	private String edgeLabelColor;
	private String fontFamily = "Times New Roman Regular";
	private String edgeDashArray;

	private int edgeLineWidth;
	private int nodeBorderWidth;
	private int nodeSize;
	private int nodeFontSize;
	private int edgeFontSize;
	private double fillOpacity; //edge

	private boolean textsVisible = true;

	public void parseGeneralStyleAttributesFromUidl(final UIDL uidl) {
	
		textsVisible = uidl.getBooleanAttribute("texts");
		bgColor = uidl.getStringAttribute("bc");
		edgeColor = uidl.getStringAttribute("ec");
		nodeBorderColor = uidl.getStringAttribute("nbc");
		nodeFillColor = uidl.getStringAttribute("nfc");
		edgeLineWidth = uidl.getIntAttribute("elw");
		nodeBorderWidth = uidl.getIntAttribute("nbw");
		nodeSize = uidl.getIntAttribute("ns") / 2;
		nodeSelectionColor = uidl.getStringAttribute("nsc");
		edgeSelectionColor = uidl.getStringAttribute("esc");
		nodeLabelColor = uidl.getStringAttribute("nlc");
		edgeLabelColor = uidl.getStringAttribute("elc");
		nodeFontSize = uidl.getIntAttribute("nfs");
		edgeFontSize = uidl.getIntAttribute("efs");
		edgeDashArray = uidl.getStringAttribute("eda");
		fillOpacity = uidl.getDoubleAttribute("efo");
	}

	public String getBgColor() {
		return bgColor;
	}
	public double getFillOpacity(){
		return fillOpacity;
	}

	public void setBgColor(final String bgColor) {
		this.bgColor = bgColor;
	}

	public String getEdgeColor() {
		return edgeColor;
	}

	public void setEdgeColor(final String edgeColor) {
		this.edgeColor = edgeColor;
	}

	public String getNodeBorderColor() {
		return nodeBorderColor;
	}

	public void setNodeBorderColor(final String nodeBorderColor) {
		this.nodeBorderColor = nodeBorderColor;
	}

	public String getNodeFillColor() {
		return nodeFillColor;
	}

	public void setNodeFillColor(final String nodeFillColor) {
		this.nodeFillColor = nodeFillColor;
	}

	public String getNodeSelectionColor() {
		return nodeSelectionColor;
	}

	public void setNodeSelectionColor(final String nodeSelectionColor) {
		this.nodeSelectionColor = nodeSelectionColor;
	}

	public String getEdgeSelectionColor() {
		return edgeSelectionColor;
	}

	public void setEdgeSelectionColor(final String edgeSelectionColor) {
		this.edgeSelectionColor = edgeSelectionColor;
	}

	public String getNodeLabelColor() {
		return nodeLabelColor;
	}

	public void setNodeLabelColor(final String nodeLabelColor) {
		this.nodeLabelColor = nodeLabelColor;
	}

	public String getEdgeLabelColor() {
		return edgeLabelColor;
	}

	public void setEdgeLabelColor(final String edgeLabelColor) {
		this.edgeLabelColor = edgeLabelColor;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(final String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public int getEdgeLineWidth() {
		return edgeLineWidth;
	}

	public void setEdgeLineWidth(final int edgeLineWidth) {
		this.edgeLineWidth = edgeLineWidth;
	}

	public int getNodeBorderWidth() {
		return nodeBorderWidth;
	}

	public void setNodeBorderWidth(final int nodeBorderWidth) {
		this.nodeBorderWidth = nodeBorderWidth;
	}

	public int getNodeSize() {
		return nodeSize;
	}

	public void setNodeSize(final int nodeSize) {
		this.nodeSize = nodeSize;
	}

	public int getNodeFontSize() {
		return nodeFontSize;
	}

	public void setNodeFontSize(final int nodeFontSize) {
		this.nodeFontSize = nodeFontSize;
	}

	public int getEdgeFontSize() {
		return edgeFontSize;
	}

	public void setEdgeFontSize(final int edgeFontSize) {
		this.edgeFontSize = edgeFontSize;
	}

	public boolean isTextsVisible() {
		return textsVisible;
	}

	public void setTextsVisible(final boolean textsVisible) {
		this.textsVisible = textsVisible;
	}

	public void setEdgeDashArray(final String edgeDashArray) {
		this.edgeDashArray = edgeDashArray;
	}

	public String getEdgeDashArray() {
		return edgeDashArray;
	}
}