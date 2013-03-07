package de.uni_leipzig.simba.saim.util;

import java.util.HashMap;
import java.util.Map.Entry;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.evaluation.PRFComputer;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.learning.oracle.mappingreader.CSVMappingReader;
import de.uni_leipzig.simba.query.ModelRegistry;
import de.uni_leipzig.simba.query.QueryModuleFactory;

public class LinkQualityComputer {
	
	Mapping m;
	Mapping reference;
	
	public LinkQualityComputer(String mappingFile, String referenceFile) {
		m = getMapping(mappingFile);
		reference = getMapping(referenceFile);
	}
	
	public double computeFScore() {
		PRFComputer p = new PRFComputer();
		return p.computeFScore(m, reference);
	}
	
	public double computePrecision() {
		PRFComputer p = new PRFComputer();
		return p.computePrecision(m, reference);
	}
	
	public double computeRecall() {
		PRFComputer p = new PRFComputer();
		return p.computeRecall(m, reference);
	}
	
	private static Mapping getMapping(String file) {
		CSVMappingReader r = new CSVMappingReader();
		if(getSeparator(file) != null) {
			r.setSeparator(getSeparator(file));
			r.setCleanIRI(true);
			return r.getMapping(file);
		} else {
			// try SPARQL
			KBInfo kb = new KBInfo();
			kb.id = "foo";
			kb.endpoint = file;
			
			QueryModuleFactory.getQueryModule("ttl", kb);
			Model model = ModelRegistry.getInstance().getMap().get(kb.endpoint);
			String query = "PREFIX owl: <"+PrefixHelper.getURI("owl")+">"+'\n'+
					" SELECT ?s ?t WHERE {?s owl:sameAs ?t}";
			ResultSet rs = SPARQLHelper.querySelect(query, file, null, model);
			Mapping m = new Mapping();
			while(rs.hasNext())
			{
				QuerySolution qs = rs.nextSolution();
				m.add(""+qs.get("?s"), ""+qs.get("?t"), 1d);
			}
			return m;
		}
		
		
		
	}
	
	private static String getSeparator(String fileName) {
		String last = fileName.substring(fileName.lastIndexOf(".")+1);
		if(last.equalsIgnoreCase("csv"))
			return ",";
		if(last.equalsIgnoreCase("tsv"))
			return "\t";
		if(last.equalsIgnoreCase("ttl") || last.equalsIgnoreCase("nt"))
			return null;
		return ";";
	}
	
	public static void main(String args []) {
		String ttl = "person11.nt_person12.nt.ttl";
		String tsv = "person11.nt_person12.nt.tsv";
		
		String mapFile2 = System.getProperty("user.home")+"/"+ttl;
		String mapFile = System.getProperty("user.home")+"/"+tsv;
		Mapping m1 = getMapping(mapFile);
		Mapping m2 = getMapping(mapFile2);
		
		for(Entry<String, HashMap<String, Double>> uri1: m1.map.entrySet())
			for(Entry<String, Double> e2 : uri1.getValue().entrySet()) {
				if(!m2.contains(uri1.getKey(), e2.getKey()))
					System.out.println("m2 without..."+uri1.getKey()+"  "+ e2.getKey());
			}
		
		String refFile = "C:\\Users\\Lyko\\workspace\\LIMES\\resources\\Persons1\\dataset11_dataset12_goldstandard_person.xml.csv";
		
		
		LinkQualityComputer lc = new LinkQualityComputer(mapFile, refFile);
		LinkQualityComputer lc2 = new LinkQualityComputer(mapFile, refFile);
		System.out.println("f="+lc.computeFScore()+" p="+lc.computePrecision()+" r="+lc.computeRecall());
		System.out.println("f="+lc2.computeFScore()+" p="+lc2.computePrecision()+" r="+lc2.computeRecall());
	}
}

