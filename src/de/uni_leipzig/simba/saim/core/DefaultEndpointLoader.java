package de.uni_leipzig.simba.saim.core;

import java.util.HashMap;

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
	
	/**
	 * Returns named default endpoints.
	 * @return
	 */
	public static HashMap<String, KBInfo> getDefaultEndpoints() {
		HashMap<String, KBInfo> defaults = new HashMap<>();
//		defaults.put("DBPedia - default graph", createKBInfo("http://dbpedia.org/sparql","http://dbpedia.org",10000,"dbpedia"));	
		defaults.put("DBPedia live - default graph", createKBInfo("http://live.dbpedia.org/sparql","http://dbpedia.org",1000,"dbpedia"));
		defaults.put("lgd.aksw - Diseasome", createKBInfo("http://lgd.aksw.org:5678/sparql", "http://www.instancematching.org/oaei/di/diseasome/", 1000, "diseasome"));
		defaults.put("lgd.aksw - Sider", createKBInfo("http://lgd.aksw.org:5678/sparql", "http://www.instancematching.org/oaei/di/sider/", 1000, "sider"));
		defaults.put("lgd.aksw - Drugbank", createKBInfo("http://lgd.aksw.org:5678/sparql", "http://www.instancematching.org/oaei/di/drugbank/", 1000, "drugbank"));
		defaults.put("Dailymed", createKBInfo("http://www4.wiwiss.fu-berlin.de/dailymed/sparql", "", 1000, "dailymed"));
		defaults.put("Drugbank", createKBInfo("http://www4.wiwiss.fu-berlin.de/drugbank/sparql", "", 1000, "drugbank"));
		defaults.put("Sider", createKBInfo("http://www4.wiwiss.fu-berlin.de/sider/sparql", "", 1000, "sider"));
		
		defaults.put("Wiktionary", createKBInfo("http://wiktionary.dbpedia.org/sparql", "", 1000, "wiktionary"));
		defaults.put("WordNet 3.0 (VU Amsterdam)", createKBInfo("http://api.talis.com/stores/wordnet/services/sparql", "", 1000, "wordnet"));
		defaults.put("WordNet (RKBExplorer)", createKBInfo("http://wordnet.rkbexplorer.com/sparql/", "", 1000, "wordnet"));
		return defaults;
	}
}