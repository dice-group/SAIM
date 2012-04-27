package de.uni_leipzig.simba.saim.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;
import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.metric.MetricParser;
import de.uni_leipzig.simba.saim.core.metric.Output;

/**Class holds all configuration settings for a linking process. */
public class Configuration
{
	static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	public Output metric = null; 
	private static Configuration instance = new Configuration();	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this ); 
	public static final String SETTING_CONFIG = "setting from xml";
	protected String id = null;
	protected String name;

	protected double acceptanceThreshold=0.5d;
	protected double verificationThreshold=0.4d;
	protected int granularity=2;
	public String sameAsRelation = "owl:sameAs";
	private ConfigReader cR = new ConfigReader();

	protected KBInfo source = null;
	protected KBInfo target = null;
	protected String metricExpression;
	public boolean isLocal  = false;

	public PropertyMapping propertyMapping = new PropertyMapping(); 

	public String getMetricExpression() {return metricExpression;}
	public void setMetricExpression(String metricExpression) {	this.metricExpression = metricExpression;}
	public double getAcceptanceThreshold() {return acceptanceThreshold;}
	public void setAcceptanceThreshold(double acceptanceThreshold) {this.acceptanceThreshold = acceptanceThreshold;}

	private Configuration() {}
	public void store() {}

	/** Implements Singleton pattern.*/
	public static Configuration getInstance() {return instance;}

	public void setSourceEndpoint(KBInfo source) {	this.source = source;
		if(source.var == null)
			source.var = "?src";
	}
	public void setTargetEndpoint(KBInfo target) {	this.target = target;
		if(target.var == null);
			target.var = "?dest";
	}
	public KBInfo getSource() {	return source;}
	public KBInfo getTarget() {	return target;}

	public void setFromConfigReader(ConfigReader cR)
	{
		this.cR = cR;
		source = cR.sourceInfo;
		target = cR.targetInfo;
		metricExpression = cR.metricExpression;
		metric = MetricParser.parse(metricExpression,cR.sourceInfo.var.replace("?",""));
		
		logger.info("Successfully parsed metric from config reader: "+metric);
		acceptanceThreshold = cR.acceptanceThreshold;
		verificationThreshold = cR.verificationThreshold;
		granularity = cR.granularity;
		if(source.type.equalsIgnoreCase("CSV") || target.type.equalsIgnoreCase("CSV"))
			this.isLocal = true;
		changes.firePropertyChange(SETTING_CONFIG, null, this);
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{ 
		changes.addPropertyChangeListener(l); 
	} 

	public void removePropertyChangeListener(PropertyChangeListener l) 
	{ 
		changes.removePropertyChangeListener(l); 
	}

	//	/**Set default namespaces in both source and target KBInfo  */
	//	public void setDefaultNameSpaces() {
	//		source.prefixes = getDefaultNameSpaces();
	//		target.prefixes = getDefaultNameSpaces();
	//	}
	//
	//	/**
	//	 * Function returns HashMap of label and uri of often used namespaces.
	//	 * @return HashMap<label,uri>
	//	 */
	//	public HashMap<String, String> getDefaultNameSpaces()
	//	{
	//		HashMap<String, String> defs = new HashMap<String, String>();
	//		//		  defs.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	//		//		  defs.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	//		//		  defs.put("foaf", "http://xmlns.com/foaf/0.1/");
	//		//		  defs.put("owl", "http://www.w3.org/2002/07/owl#");
	//		//		  defs.put("diseasome", "http://www4.wiwiss.fu-berlin.de/diseasome/resource/diseasome/");
	//		//		  defs.put("dbpedia", "http://dbpedia.org/ontology/");
	//		//		  defs.put("dbpedia-p", "http://dbpedia.org/property/");
	//		//		  defs.put("dc", "http://purl.org/dc/terms/");
	//		//		  defs.put("sider", "http://www4.wiwiss.fu-berlin.de/sider/resource/sider/");
	//		//		  defs.put("drugbank", "http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/");
	//		//		  defs.put("dailymed", "http://www4.wiwiss.fu-berlin.de/dailymed/resource/dailymed/");
	//		Map<String, String> map = PrefixHelper.getPrefixes();	
	//		for(Entry<String, String> e : map.entrySet())
	//			defs.put(e.getKey(), e.getValue());
	//		return defs;
	//	}

	protected void fillKBElement(Element element, KBInfo kb)
	{		
		element.getChild("ID").setText(kb.id);
		element.getChild("ENDPOINT").setText(kb.endpoint);
		element.getChild("PAGESIZE").setText(String.valueOf(kb.pageSize));
		for(String restriction: kb.restrictions)
		{			
			Element restrictionElement = new Element("RESTRICTION");
			element.addContent(restrictionElement);
			restrictionElement.setText(restriction);
		}				
	}

	public void saveToXML(String filename)
	{
		try{
			Document document = null;
			document = new SAXBuilder().build(getClass().getClassLoader().getResourceAsStream("template.xml"));			
			Element sourceElement = (Element) XPath.selectSingleNode(document,"//SOURCE");
			Element targetElement = (Element) XPath.selectSingleNode(document,"//TARGET");
			fillKBElement(sourceElement,source);
			fillKBElement(targetElement,target);
			Element acceptanceElement = (Element) XPath.selectSingleNode(document,"//ACCEPTANCE");
			acceptanceElement.getChild("FILE").setText(source.endpoint+'-'+target.endpoint+"-accept");
			Element reviewElement = (Element) XPath.selectSingleNode(document,"//REVIEW");
			reviewElement.getChild("FILE").setText(source.endpoint+'-'+target.endpoint+"-review");

			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(document,new FileOutputStream("linkspec.xml"));

			//getElementById("/LIMES/SOURCE/VAR")
		}
		catch (Exception e){throw new RuntimeException(e);}
	}	

	public String toString() {
		return source.toString()+"\n<br>\n"+target.toString()+"\n<br>\n"+metricExpression+"\n<br>\n"+acceptanceThreshold;  
	}

	/**
	 * Method adds a property match, and the properties to the according KBInfos.
	 * @param sourceProp source property without heading variable of the KBInfo! Eg. <i>rdf:label</i> not <i>x.rdf:label</i>!
	 * @param targetProp target property without heading variable of the KBInfo! Eg. <i>rdf:label</i> not <i>y.rdf:label</i>!
	 */
	public void addPropertiesMatch(String sourceProp, String targetProp) {
		String s_abr=PrefixHelper.abbreviate(sourceProp);
		String t_abr=PrefixHelper.abbreviate(targetProp);
		logger.info("Adding Property Match: "+s_abr+" - "+t_abr);
		if(!source.properties.contains(s_abr)) {
			source.properties.add(s_abr);
			source.prefixes.put(PrefixHelper.getBase(s_abr), PrefixHelper.getURI(PrefixHelper.getBase(s_abr)));
			source.functions.put(s_abr, "lowercase");	
		}
		//		System.out.println("Adding source property: "+s_abr+"::::"+PrefixHelper.getPrefixFromURI(s_abr)+" -- "+PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
		if(!target.properties.contains(t_abr)) {
			target.properties.add(t_abr);
			target.prefixes.put(PrefixHelper.getBase(t_abr), PrefixHelper.getURI(PrefixHelper.getBase(t_abr)));
			target.functions.put(s_abr, "lowercase");
		}
		//		System.out.println("Adding target property: "+t_abr+"::::"+PrefixHelper.getPrefixFromURI(t_abr)+" -- "+PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(t_abr)));
		this.propertyMapping.addStringPropertyMatch(s_abr, t_abr);

	}

	public ConfigReader getLimesConfiReader() {
		cR = new ConfigReader();
		if(source.var == null)
			source.var="?src";
		if(target.var==null)
			target.var="?dest";
		cR.sourceInfo = getSource();
		cR.targetInfo = getTarget();
		if(metricExpression == null) {			
			String defMetric = "trigram("+source.var+"."+source.properties.get(0)+","+target.var+"."+target.properties.get(0)+")";
			defMetric = defMetric.replaceAll("\\?", "");
			System.out.println("No metricExpression set ... using default: "+defMetric);
			metricExpression = defMetric;
		}
		cR.prefixes.putAll(getSource().prefixes);
		cR.prefixes.putAll(getTarget().prefixes);
		cR.prefixes.put(PrefixHelper.getPrefix(sameAsRelation), PrefixHelper.getURI(PrefixHelper.getPrefix(sameAsRelation)));
		cR.verificationRelation = sameAsRelation;
		cR.acceptanceRelation = sameAsRelation;
		cR.metricExpression = metricExpression;
		cR.acceptanceThreshold = acceptanceThreshold;
		cR.verificationThreshold  = verificationThreshold;
		cR.granularity = granularity;		 
		//	cR.
		return cR;
	}
	
	/**
	 * Tests whether or not the given property is part of either source or target endpoint. Expects property to begin with endpoint variable, if
	 * not we will just look in both endpoints.
	 * @param prop
	 * @return
	 */
	public boolean isPropertyDefined(String prop) {
		if(prop.indexOf(".") == -1) {
			return source.properties.contains(prop) || target.properties.contains(prop);
		}else {
			String var = prop.substring(0, prop.indexOf("."));
			if(source.var.contains(var))
				return source.properties.contains(prop.substring(prop.indexOf(".")+1));
			if(target.var.contains(var))
				return target.properties.contains(prop.substring(prop.indexOf(".")+1));
			return false;
		}
	}
}