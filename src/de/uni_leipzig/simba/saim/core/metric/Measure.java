package de.uni_leipzig.simba.saim.core.metric;

/** A similarity measure.*/
public class Measure extends Node
{
	@Override public boolean validParentOf(Node node) {return (node instanceof Property);}
}