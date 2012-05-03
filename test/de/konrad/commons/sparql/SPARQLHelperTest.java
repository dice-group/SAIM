/**
 * Copyright (C) 2011, SAIM team at the MOLE research
 * group at AKSW / University of Leipzig
 *
 * This file is part of SAIM (Semi-Automatic Instance Matcher).
 *
 * SAIM is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * SAIM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.konrad.commons.sparql;

import static de.konrad.commons.sparql.SPARQLHelper.querySelect;
import static de.konrad.commons.sparql.SPARQLHelper.resultSetToList;
import static de.konrad.commons.sparql.SPARQLHelper.rootClasses;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.DefaultEndpointLoader;
import de.uni_leipzig.simba.saim.gui.validator.EndpointURLValidator;

/** @author Konrad Höffner */
public class SPARQLHelperTest
{
	//static String[] testObjects = {"Comune di Marcedusa@en","556^^http://www.w3.org/2001/XMLSchema#integer","http://www4.wiwiss.fu-berlin.de/flickrwrappr/photos/Marcedusa"}; 

	@Test
	public void testSubClassesOf()
	{
		System.out.println(SPARQLHelper.subclassesOf("http://dbpedia.org/ontology/Building", SPARQLHelper.DBPEDIA_ENDPOINT, null));
	}

	@Test
	public void testRootClasses()
	{
		//		assertTrue(rootClasses(TestingDefaults.sparqlEndpoint, null).equals(Collections.singletonList(OWL.Thing.toString())));
		//		assertTrue(rootClasses(TestingDefaults.sparqlEndpoint, null).equals(Collections.singletonList(OWL.Thing.toString())));
		//		assertTrue(rootClasses("http://linkedgeodata.org/sparql", "http://linkedgeodata.org").contains("http://linkedgeodata.org/ontology/Way"));
		Map<String,KBInfo> endpoints = DefaultEndpointLoader.getDefaultEndpoints();
		EndpointURLValidator validator = new EndpointURLValidator();
		for(KBInfo kb: endpoints.values())
		{
			String endpoint = kb.endpoint;
			if(validator.isValid(endpoint))
			{
				try{
					assertTrue("could not get root classes for endpoint "+kb.endpoint,!rootClasses(kb.endpoint,kb.graph).isEmpty());
				} catch (Exception e)
				{
					throw new RuntimeException("error getting root classes for endpoint "+kb.endpoint,e);
				}
			}
		}
		//		KBInfo drugbank = endpoints.get("lgd.aksw - Drugbank");
		//		assertTrue(drugbank!=null);
		//		assertTrue(drugbank.endpoint,validator.isValid(drugbank.endpoint));
		//		KBInfo sider = endpoints.get("lgd.aksw - Sider");
		//		assertTrue(sider!=null);
		//		assertTrue(sider.endpoint,validator.isValid(sider.endpoint));
		//		System.out.println(rootClasses(drugbank.endpoint, drugbank.graph));
		//		System.out.println(rootClasses(sider.endpoint, sider.graph));
		//		assertTrue(rootClasses("http://www4.wiwiss.fu-berlin.de/diseasome/sparql",null).contains("http://www4.wiwiss.fu-berlin.de/diseasome/resource/diseasome/genes"));
	}

	@Test
	public void testResultSetToList()
	{
		assertTrue(resultSetToList(querySelect(
				"select ?s where {?s <http://dbpedia.org/ontology/birthPlace> <http://dbpedia.org/resource/Leipzig> }",
				SPARQLHelper.DBPEDIA_ENDPOINT, null)).contains("http://dbpedia.org/resource/Friedrich_Nietzsche"));
	}

	@Test
	public void testQuerySelect()
	{
		assertTrue((querySelect(
				"select ?o where {<http://dbpedia.org/resource/Leipzig> <http://dbpedia.org/property/vorwahl> ?o.} limit 1",
				SPARQLHelper.DBPEDIA_ENDPOINT, null).next().getLiteral("o").getInt())==341);
	}

	@Test
	public void testProperties()
	{
		fail("not implemented yet");
	}

	//	@Test
	//	public void testDataType()
	//	{
	//		String[] dataTypes = {"","http://www.w3.org/2001/XMLSchema#integer",""};			
	//		for(int i=0;i<testObjects.length;i++)
	//		{
	//		assertTrue(SPARQLHelper.dataType(testObjects[i]).equals(dataTypes[i]));
	//		}
	//	}
	//
	//	/** Test method for {@link de.konrad.commons.sparql.SPARQLHelper#languageTag(java.lang.String)}.*/
	//	@Test
	//	public void testLanguageTag()
	//	{
	//		String[] languageTags = {"en","",""};
	//		for(int i=0;i<testObjects.length;i++)
	//		{
	//		assertTrue(SPARQLHelper.languageTag(testObjects[i]).equals(languageTags[i]));
	//		}
	//	}
	//
	//	/** Test method for {@link de.konrad.commons.sparql.SPARQLHelper#lexicalForm(java.lang.String)}. */
	//	@Test
	//	public void testLexicalForm()
	//	{
	//		String[] lexicalForms = {"Comune di Marcedusa","556","http://www4.wiwiss.fu-berlin.de/flickrwrappr/photos/Marcedusa"};
	//		for(int i=0;i<testObjects.length;i++)
	//		{
	//		assertTrue(SPARQLHelper.lexicalForm(testObjects[i]).equals(lexicalForms[i]));
	//		}
	//	}
	//	
	//	@Test
	//	public void getRandomSample() throws Exception
	//	{
	//		Instance[] instances = SPARQLHelper.getRandomSample(Knowledgebases.DBPEDIA_SETTLEMENT,100,0);
	//		System.out.println(Arrays.toString(instances));
	//	}
	//
	//	static final Map<String,String> rdfsPrefix = SPARQLHelper.textToMap("rdfs	http://www.w3.org/2000/01/rdf-schema#");
	//	
	//	@Test
	//	public void testTextToMap()
	//	{
	//		assertTrue(rdfsPrefix.containsKey("rdfs")&&rdfsPrefix.get("rdfs").equals("http://www.w3.org/2000/01/rdf-schema#"));
	//	}
	//
	//	@Test
	//	public void testGetDefaultPrefixes()
	//	{
	//		Map<String,String> defaultPrefixes = SPARQLHelper.getDefaultPrefixes();
	//		assertTrue(defaultPrefixes.containsKey("rdfs")&&defaultPrefixes.get("rdfs").equals("http://www.w3.org/2000/01/rdf-schema#"));
	//	}
	//
	//	@Test
	//	public void testFormatPrefixes()
	//	{
	//		assertTrue(PrefixHelper.formatPrefixes(rdfsPrefix).equals("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"));
	//	}
}
