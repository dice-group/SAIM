package de.uni_leipzig.simba.saim.core.metric;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeTest
{
	@Test
	public void test()
	{
		Measure m = new Measure();
		Property p = new Property();
		Property q = new Property();
		Property r = new Property();
		
		assertTrue(m.validParentOf(p));
		assertTrue(m.acceptsChild(p));
		assertFalse(p.validParentOf(m));
		assertFalse(p.acceptsChild(m));
		
		m.addChild(p);
		m.addChild(q);
		
		assertTrue(m.validParentOf(r));
		assertFalse(m.acceptsChild(r));
		

	}

}
