package de.uni_leipzig.simba.saim.core.metric;

/** A similarity measure.*/
public class Property extends Node
{
	@Override public boolean validParentOf(Node n) {return false;}
	
	@Override public boolean accepts(Node n)
	{
		return super.accepts(n);
	}
}