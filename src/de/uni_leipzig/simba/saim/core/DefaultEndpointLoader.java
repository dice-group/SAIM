package de.uni_leipzig.simba.saim.core;

import java.util.LinkedList;
import java.util.List;

import de.uni_leipzig.simba.io.KBInfo;

public class DefaultEndpointLoader
{
	static public KBInfo createKBInfo(String endpoint, String graph, int pageSize, String id)
	{
		KBInfo kb = new KBInfo();
		kb.endpoint=endpoint;
		kb.graph=graph;
		kb.pageSize=pageSize;
		kb.id=id;
		return kb;
	}
	
	public static List<KBInfo> getDefaultEndpoints() {
		LinkedList<KBInfo> defaults = new LinkedList<>();
		defaults.add(createKBInfo("http://dbpedia.org/sparql","http://dbpedia.org",10000,"dbpedia"));	
		defaults.add(createKBInfo("http://www4.wiwiss.fu-berlin.de/diseasome/sparql",null,1000,"diseasome"));	
		defaults.add(createKBInfo("http://live.dbpedia.org/sparql","http://dbpedia.org",1000,"dbpedia"));
		return defaults;
	}
}