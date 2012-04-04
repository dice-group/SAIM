package de.uni_leipzig.simba.saim.core.metric;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.uni_leipzig.simba.saim.core.metric.Property.Origin;

public class NodeTest
{
	@Test
	public void test()
	{
		Measure m = new Measure();
		Property p = new Property("rdf:type",Origin.SOURCE);
		Property q = new Property("rdf:type",Origin.SOURCE);
		Property r = new Property("rdf:type",Origin.TARGET);
		//Property s = new Property("rdf:type",Origin.SOURCE);
		
		assertTrue(m.validParentOf(p));
		assertTrue(m.acceptsChild(p));
		m.addChild(p);
		assertFalse(p.validParentOf(m));
		assertFalse(p.acceptsChild(m));

		assertTrue(m.validParentOf(q));
		assertFalse(m.acceptsChild(q));

		assertTrue(m.validParentOf(r));
		assertTrue(m.acceptsChild(r));

		m.addChild(r);	

	}

}
