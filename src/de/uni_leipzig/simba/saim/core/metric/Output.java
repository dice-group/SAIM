package de.uni_leipzig.simba.saim.core.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** The metric tree's root node specifiying the acceptance and review thresholds. It's single child is either a metric or an operator.*/
public class Output extends Node
{
	public Output() {super(null);}
	@Override public Set<String> identifiers()	{return new HashSet<>(Arrays.asList(new String[] {"output"}));}	
	@SuppressWarnings("unchecked")
	static public final Set<Class<? extends Node>> validChildClasses =
	Collections.unmodifiableSet(new HashSet<Class<? extends Node>>(Arrays.asList((Class<? extends Node>[])new Class[] {Measure.class,Operator.class})));		
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}	
	/**output just takes the value of a node and filters it with a threshold
	 * @return 1*/
	public byte getMaxChilds() { return 1; }
	/**	returns the Metric Expression for the metric tree, e.g. <pre>trigrams(y.dc:title,x.linkedct:condition_name).</pre>*/
	@Override public String toString()
	{
		return (getChilds().isEmpty()?"":getChilds().iterator().next().toString())+(param1!=null?"|"+param1:"")+(param2!=null?"|"+param2:"");
	}	
}