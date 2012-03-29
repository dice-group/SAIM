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
		c.setMetricExpression("trigram(x.title,y.title)");
		c.saveToXML("test.xml");
	}
}