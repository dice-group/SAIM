package de.uni_leipzig.simba.saim.core.metric;

import java.util.Collections;
import java.util.Set;

/** A similarity measure.*/
public class Property extends Node
{
//	@Override public String identifier() {return origin==Origin.SOURCE?"x.rdfs.testproperty":"y.rdfs.testproperty";}	
	@Override public Set<String> identifiers()	{return Collections.<String>emptySet();}	
	public byte getMaxChilds() {return 0;}
	public enum Origin {SOURCE,TARGET};
	protected final String uri;
	protected final Origin origin;
	@Override public Set<Class<? extends Node>> validChildClasses() {return Collections.emptySet();}
	// properties are leaves on the tree 	
	@Override public boolean acceptsChild(Node n)	{return false;}
	
	public Property(String url, Origin origin)
	{
		super(url);
		this.uri=url;
		this.origin=origin;
	}

}