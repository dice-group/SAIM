package de.uni_leipzig.simba.saim.core.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.uni_leipzig.simba.saim.core.metric.Property.Origin;

/** A similarity measure.*/
public class Measure extends Node
{
	public Measure(String id) {super(id);}
	public static final Set<String> identifiers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			new String[] {"trigram","trigrams","jaccard","cosine","levenshtein","cosine","euclidean", "overlap"})));	

	@Override public Set<String> identifiers()	{return identifiers;}	
	@Override public byte getMaxChilds() {return 2;}

	static public final Set<Class<? extends Node>> validChildClasses =
			Collections.<Class<? extends Node>>singleton(Property.class);
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}	

	/**A fully connected measure contains exactly one source- and one target property.  */
	@Override public boolean acceptsChild(Node node)
	{
		synchronized(this)
		{
			synchronized (node)
			{
				return super.acceptsChild(node)&& // super.acceptsChild calls validParentOf(node), so node instanceof Property. 
						(getChilds().isEmpty()
								||((Property)getChilds().iterator().next()).origin!=((Property)node).origin);
			}
		}
	}

	@Override
	public Acceptance acceptsChildWithReason(Node node)
	{
		synchronized(this)
		{
			synchronized(node)
			{					
				Acceptance acceptance = super.acceptsChildWithReason(node);
				if(acceptance!=Acceptance.OK) {return acceptance;}
				if(!acceptsChild(node))
				{
					if((((Property)getChilds().iterator().next()).origin==Origin.SOURCE)) {return Acceptance.TARGET_PROPERTY_EXPECTED;}
					return Acceptance.SOURCE_PROPERTY_EXPECTED;
				}
				return Acceptance.OK;
			}
		}
	}
}