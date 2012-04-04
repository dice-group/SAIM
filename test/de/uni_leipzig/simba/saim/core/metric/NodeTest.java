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
		Node[] nodes = {m,p,q,r};
		
		for(int i=0;i<nodes.length;i++)
			for(int j=i+1;j<nodes.length;j++)
			{assertTrue(nodes[i].color!=nodes[j].color);}
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
		assertFalse(m.isComplete());
		m.addChild(r);
		assertTrue(m.isComplete());
		
		Operator o1 = new Operator();
		Operator o2 = new Operator();
		Operator o3 = new Operator();
		assertTrue(o1.addChild(o2));
		assertTrue(o2.addChild(o3));
		// would be a cycle
		assertFalse(o3.addChild(o1));
		o2.removeChild(o3);
		assertTrue(o3.addChild(o1));
		assertFalse(o1.isComplete());
	}
}