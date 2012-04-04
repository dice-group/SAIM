package de.uni_leipzig.simba.saim.core.metric;

/** A similarity measure.*/
public class Property extends Node
{
	public byte getMaxChilds() {return 0;}
	public enum Origin {SOURCE,TARGET};
	protected final String uri;
	protected final Origin origin;
	
	// properties are leaves on the tree 
	@Override public boolean validParentOf(Node node) {return false;}	
	@Override public boolean acceptsChild(Node n)	{return false;}
	
	public Property(String url, Origin origin)
	{
		this.uri=url;
		this.origin=origin;
	}

}