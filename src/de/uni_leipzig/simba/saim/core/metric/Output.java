package de.uni_leipzig.simba.saim.core.metric;

/** The metric tree's root node specifiying the acceptance and review thresholds. It's single child is either a metric or an operator.*/
public class Output extends Node
{
	public byte getMaxChilds() {return 1;}
	
	@Override public boolean validParentOf(Node node) {return node instanceof Measure || node instanceof Operator;}	
}