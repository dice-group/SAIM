/**
 * Copyright (C) 2011, SAIM team at the MOLE research
 * group at AKSW / University of Leipzig
 * This file is part of SAIM (Semi-Automatic Instance Matcher).
 * SAIM is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * SAIM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.konrad.commons.sparql;

import static org.junit.Assert.*;
import org.junit.Test;
import de.uni_leipzig.simba.saim.core.Configuration;
/** @author Konrad Höffner */
public class PrefixHelperTest
{
	@Test
	public void testAddPrefix()
	{
		PrefixHelper.addPrefix("elephants","http://elephants.org/");
		assertTrue(PrefixHelper.getPrefix("http://elephants.org/").equals("elephants"));
	}

	@Test
	public void testGeneratePrefix()
	{
		PrefixHelper.generatePrefix("http://zebras.org/ontology/NormalZebra");
		assertTrue(PrefixHelper.getPrefix("http://zebras.org/ontology/")!=null);
		assertTrue(PrefixHelper.getPrefix("http://lamas.org/ontology/")==null);
	}


	@Test
	public void testGetPrefix()
	{
		assertTrue(PrefixHelper.getPrefix("http://dbpedia.org/ontology/").equals("dbo"));
	}

	@Test
	public void testGetURI()
	{
		assertTrue(PrefixHelper.getURI("dbo").equals("http://dbpedia.org/ontology/"));
		assertTrue(PrefixHelper.getURI("dbpediaowl").equals("http://dbpedia.org/ontology/"));
	}

	@Test
	public void testAbbreviate()
	{
		assertTrue(PrefixHelper.abbreviate("http://dbpedia.org/ontology/Settlement").equals("dbo:Settlement"));
	}

	@Test
	public void testExpand()
	{
		assertTrue(PrefixHelper.expand("dbo:Settlement").equals("http://dbpedia.org/ontology/Settlement"));
	}

	@Test
	public void testSuffix()
	{
		assertTrue(PrefixHelper.getSuffix("http://dbpedia.org/ontology/Settlement").equals("Settlement"));
	}

	@Test
	public void testOWL() {
		Configuration config = new Configuration();
		String sameAsRelation = config.sameAsRelation;
		String base = PrefixHelper.getBase(sameAsRelation);
		if(base.endsWith(":")) {
			base = base.substring(0,base.length()-1);
		}
		assertNotNull(PrefixHelper.getURI(base));

//		PrefixHelper.getPrefix(PrefixHelper.getBase(sameAsRelation)), PrefixHelper.getURI(PrefixHelper.getBase(sameAsRelation))
	}
}
