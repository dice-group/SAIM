package de.uni_leipzig.simba.saim.core.metric;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Abstract superclass for the components of a metric.*/
public abstract class Node
{
	/** the number of "colors" already assigned to nodes (some of which may not exist anymore) to prevent cycles.*/
	protected static int colors = 0;
	/** the "color" of the node which is used for cycle prevention. Not related to any "real" color of a graphical representation of the node.*/
	protected int color;
	public byte getMaxChilds() {return 0;}
	/** returns true if the type of the parent and the child are compatible (e.g. a measure may only have properties as childs). */
	public abstract boolean validParentOf(Node node);
	protected Node parent = null;
	protected Set<Node> childs = new HashSet<>();

	public Set<Node> getChilds() {return Collections.unmodifiableSet(childs);}

	/**	adds the child node to the node and returns true if it was successfull. */
	public boolean addChild(Node child)
	{		
		if(!acceptsChild(child)) {return false;}		
		childs.add(child);
		child.parent=this;
		child.pushDownColor(this.color);
		return true;
	}

	public void removeChild(Node child)
	{
		childs.remove(child);
		child.parent=null;
		child.pushDownColor(colors++);
	}

	public boolean acceptsChild(Node n) {return validParentOf(n)&&n.parent==null&&childs.size()<getMaxChilds()&&this.color!=n.color;}

	/** Returns true if the subtree having this node as root node is complete.*/
	public boolean isComplete()
	{
		if(childs.size()<getMaxChilds()) {return false;}
		for(Node child : childs) {if(!child.isComplete()) return false;}
		return true;
	}

	public Node()
	{
		color = colors++;
	}

	/** recursively sets the color for the node decendents */
	protected void pushDownColor(int color)
	{
		this.color = color;
		for(Node child : childs) {child.pushDownColor(color);}
	}	
}