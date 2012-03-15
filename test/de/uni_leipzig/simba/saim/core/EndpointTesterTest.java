package de.uni_leipzig.simba.saim.core;

import static org.junit.Assert.*;
import org.junit.Test;
import de.uni_leipzig.simba.saim.core.EndpointTester.EndpointStatus;

public class EndpointTesterTest
{
	public static final String[] endpoints = {"http://doesntexist.org/sparqlsparql","http://dbpedia.org/sparql"};
	public static final EndpointStatus[] endpointStatusse = {EndpointStatus.HTTP_ERROR,EndpointStatus.OK};
	
	
	@Test
	public void test()
	{
		for(int i=0;i<endpoints.length;i++)
		{
			EndpointStatus status = EndpointTester.testSPARQLEndpointTimeOut(endpoints[i]);
			assertTrue(endpoints[i]+"->"+status.toString(),status==endpointStatusse[i]);
		}
	}

}
