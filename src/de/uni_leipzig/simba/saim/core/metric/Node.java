package de.uni_leipzig.simba.saim.core.metric;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Abstract superclass for the components of a metric.*/
public abstract class Node
{
	public byte getMaxChilds() {return 0;}
	public abstract boolean validParentOf(Node node);
	protected Node parent = null;
	protected Set<Node> childs = new HashSet<>();

	public Set<Node> getChilds() {return Collections.unmodifiableSet(childs);}

	public boolean addChild(Node node)
	{		
		if(!acceptsChild(node)) {return false;}		
			childs.add(node);
			return true;
	}

	public boolean acceptsChild(Node n) {return validParentOf(n)&&childs.size()<getMaxChilds();}

	/** Returns true if the subtree having this node as root node is complete.*/
	public boolean isComplete()
	{
		if(childs.size()<getMaxChilds()) {return false;}
		for(Node child : childs) {if(!child.isComplete()) return false;}
		return true;
	}
}