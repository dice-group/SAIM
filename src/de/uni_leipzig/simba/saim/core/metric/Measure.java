package de.uni_leipzig.simba.saim.core.metric;

/** A similarity measure.*/
public class Measure extends Node
{
	public byte getMaxChilds() {return 2;}
	@Override public boolean validParentOf(Node node) {return (node instanceof Property);}
}