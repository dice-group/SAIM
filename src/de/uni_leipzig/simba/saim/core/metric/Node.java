package de.uni_leipzig.simba.saim.core.metric;

import java.util.HashSet;
import java.util.Set;

/** Abstract superclass for the components of a metric.*/
public abstract class Node
{
	public final byte maxChilds = 0;
	public abstract boolean validParentOf(Node n);
	public Set<Node> childs = new HashSet<>();
	
	public boolean accepts(Node n) {return validParentOf(n)&&childs.size()<maxChilds;}
}