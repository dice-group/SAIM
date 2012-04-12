package de.uni_leipzig.simba.saim.core;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni_leipzig.simba.io.ConfigReader;

public class ConfigurationTest
{
	@Test
	public void testToConfigReader() {
		Configuration c = Configuration.getInstance();
		c.setSourceEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("Dailymed"));
		c.setTargetEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Sider"));
		c.source.restrictions.add("testsourcerestriction1");
		c.target.restrictions.add("testtargetrestriction1");
		c.addPropertiesMatch("rdfs:label", "rdf:type");
		ConfigReader cR = c.getLimesConfiReader();
		System.out.println(cR.metricExpression);
		assertNotNull(cR.metricExpression);
		assertTrue(cR.metricExpression.length()>0);
		assertTrue(cR.metricExpression.indexOf("?")<0);
		c=null;
	}
	@Test
	public void testSaveToXML()
	{
		Configuration c = Configuration.getInstance();
		c.setSourceEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Drugbank"));
		c.setTargetEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Sider"));
		c.source.restrictions.add("testsourcerestriction1");
		c.source.restrictions.add("testsourcerestriction2");
		c.target.restrictions.add("testtargetrestriction1");
		c.setMetricExpression("trigram(x.title,y.title)");
		c.saveToXML("test.xml");
		c=null;
	}
	@Test
	public void testisPropertyDefined() {
		Configuration c = Configuration.getInstance();
		c.setSourceEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Drugbank"));
		c.setTargetEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Sider"));
		c.addPropertiesMatch("rdfs:label", "rdfs:FalseLable");
		assertTrue(c.isPropertyDefined("rdfs:label"));
		assertTrue(c.isPropertyDefined("src.rdfs:label"));
		assertTrue(c.isPropertyDefined("dest.rdfs:FalseLable"));
		assertFalse(c.isPropertyDefined("src.rdfs:FalseLable"));
		c = null;
	}
}