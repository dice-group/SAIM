package de.uni_leipzig.simba.saim.core.metric;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Abstract superclass for the components of a metric.*/
public abstract class Node
{
	public byte getMaxChilds() {return 0;}
	public abstract boolean validParentOf(Node n);
	protected Set<Node> childs = new HashSet<>();
	
	public Set<Node> getChilds() {return Collections.unmodifiableSet(childs);}
	
	public Node addChild(Node n)
	{
		
		
		if(acceptsChild(n)) {childs.add(n);}
		return n;
	}
	
	public boolean acceptsChild(Node n) {return validParentOf(n)&&childs.size()<getMaxChilds();}
}