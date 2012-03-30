package de.uni_leipzig.simba.saim.core;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

public class ConfigurationTest
{
	@Test
	public void testSaveToXML()
	{
		Configuration c = new Configuration();
		c.setSourceEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Drugbank"));
		c.setTargetEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Sider"));
		c.source.restrictions.add("testsourcerestriction1");
		c.source.restrictions.add("testsourcerestriction2");
		c.target.restrictions.add("testtargetrestriction1");
		c.setMetricExpression("trigram(x.title,y.title)");
		c.saveToXML("test.xml");
	}
}