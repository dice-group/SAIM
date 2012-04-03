package de.uni_leipzig.simba.saim.core.metric;

/** A similarity measure.*/
public class Property extends Node
{
	public byte getMaxChilds() {return 0;}
	public enum Origin {SOURCE,TARGET};
	
	@Override public boolean validParentOf(Node node) {return false;}
	
	@Override public boolean acceptsChild(Node n)
	{
		return super.acceptsChild(n);
	}
}