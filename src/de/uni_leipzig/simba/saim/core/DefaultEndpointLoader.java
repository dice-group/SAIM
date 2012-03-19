package de.uni_leipzig.simba.saim.core;

import java.util.LinkedList;
import java.util.List;

public class DefaultEndpointLoader {
	
	public static List<String> getDefaultEndpoints() {
		LinkedList<String> defaults = new LinkedList<String>();
		defaults.add("http://dbpedia.org/sparql");	
		defaults.add("http://www4.wiwiss.fu-berlin.de/diseasome/sparql");
		return defaults;
	}
}
