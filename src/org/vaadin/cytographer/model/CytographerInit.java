package org.vaadin.cytographer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cytoscape.init.CyInitParams;

public final class CytographerInit implements CyInitParams {
	
	public Properties getVizProps() {
		return new Properties();
	}

	public String getSessionFile() {
		return null;
	}

	public Properties getProps() {
		return new Properties();
	}

	public List getPlugins() {
		return new ArrayList();
	}

	public List getNodeAttributeFiles() {
		return new ArrayList();
	}

	public int getMode() {
		return CyInitParams.TEXT;
	}

	public List getGraphFiles() {
		return new ArrayList();
	}

	public List getExpressionFiles() {
		return new ArrayList();
	}

	public List getEdgeAttributeFiles() {
		return new ArrayList();
	}

	public String[] getArgs() {
		return new String[] {};
	}
}