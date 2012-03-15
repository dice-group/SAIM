package de.uni_leipzig.simba.saim.gui.validator;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class EndpointURLValidatorTest
{
	protected Set<String> goodEndpoints = new HashSet<String>(Arrays.asList(new String[]{"http://dbpedia.org/sparql","http://linkedgeodata.org/sparql"}));
	protected Set<String> badEndpoints = new HashSet<String>(Arrays.asList(new String[]{"123","htp://bla.org/sparql","http:/bla.org/sparql"}));
	Set<String> endpoints = new HashSet<String>(goodEndpoints);
	{
		endpoints.addAll(badEndpoints);		
		
	}
	
	@Test
	public void testIsValid()
	{	
		EndpointURLValidator validator = new EndpointURLValidator();
		for(String endpoint: endpoints)
		{
			System.out.println("Testing endpoint "+endpoint);
			assertTrue(validator.isValid(endpoint)==goodEndpoints.contains(endpoint));
		}
	}

}
