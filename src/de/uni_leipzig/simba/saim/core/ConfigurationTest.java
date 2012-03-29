package de.uni_leipzig.simba.saim.core;

import org.junit.Test;

public class ConfigurationTest
{
	@Test
	public void testSaveToXML()
	{
		Configuration c = Configuration.getInstance();
		c.setSourceEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Drugbank"));
		c.setTargetEndpoint(DefaultEndpointLoader.getDefaultEndpoints().get("lgd.aksw - Sider"));
		c.setMetricExpression("trigram(x.title,y.title)");
		c.saveToXML("test.xml");
	}
}