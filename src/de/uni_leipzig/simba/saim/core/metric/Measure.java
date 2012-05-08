package de.uni_leipzig.simba.saim.core.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** A similarity measure.*/
public class Measure extends Node
{
	public Measure(String id) {super(id);}
	
	public static final Set<String> identifiers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			new String[] {"trigrams", "trigram","jaccard","cosine","levenshtein","cosine","euclidean"})));	
	
	@Override public Set<String> identifiers()	{return identifiers;}	
	@Override public byte getMaxChilds() {return 2;}

	static public final Set<Class<? extends Node>> validChildClasses =
			Collections.<Class<? extends Node>>singleton(Property.class);
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}	

	/**A fully connected measure contains exactly one source- and one target property.  */
	@Override public boolean acceptsChild(Node node)
	{
		return super.acceptsChild(node)&& // super.acceptsChild calls validParentOf(node), so node instanceof Property. 
				(getChilds().isEmpty()
						||((Property)getChilds().iterator().next()).origin!=((Property)node).origin);
	}
}