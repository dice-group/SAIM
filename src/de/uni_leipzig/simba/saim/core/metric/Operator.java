package de.uni_leipzig.simba.saim.core.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** An operator like add,and,or,min,max. A function from [0,1]^2 to [0,1]. Logical operators convert input values to booleans via mandatory thresholds.*/
public class Operator extends Node
{
	public Operator(String id) {super(id);}
	public static final Set<String> identifiers =
			Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new String[] {"min","max","add"})));
	@Override public Set<String> identifiers()	{return identifiers;}	
	@Override public byte getMaxChilds() {return 2;}
	@SuppressWarnings("unchecked")
	static public final Set<Class<? extends Node>> validChildClasses =
	Collections.unmodifiableSet(new HashSet<Class<? extends Node>>(Arrays.asList((Class<? extends Node>[])new Class[] {Measure.class,Operator.class})));		
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}	
}