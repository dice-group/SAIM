package de.uni_leipzig.simba.saim.core.metric;

/** An operator like add,and,or,min,max. A function from [0,1]^2 to [0,1]. Logical operators convert input values to booleans via mandatory thresholds.*/
public class Operator extends Node
{
	@Override public byte getMaxChilds() {return 2;}
	@Override public boolean validParentOf(Node node) {return (node instanceof Operator || node instanceof Measure);}
}