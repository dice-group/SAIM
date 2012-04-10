package de.uni_leipzig.simba.saim.core.metric;

/** The metric tree's root node specifiying the acceptance and review thresholds. It's single child is either a metric or an operator.*/
public class Output extends Node
{
	@Override public String getIdentifier() {return "output";}
	public byte getMaxChilds() {return 1;}
	
	@Override public boolean validParentOf(Node node) {return node instanceof Measure || node instanceof Operator;}
	
	/**	returns the Metric Expression for the metric tree, e.g. <pre>trigrams(y.dc:title,x.linkedct:condition_name).</pre>*/
	@Override public String toString()
	{
		return getChilds().isEmpty()?"":getChilds().iterator().next().toString();
	}	
}