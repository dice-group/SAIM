package de.uni_leipzig.simba.saim.core;

import java.util.LinkedList;
import java.util.List;

public class DefaultEndpointLoader {
	
	public static List<String> getDefaultEndpoints() {
		LinkedList<String> defaults = new LinkedList<String>();
		defaults.add("http://dbpedia.org/sparql");	
		return defaults;
	}
}
