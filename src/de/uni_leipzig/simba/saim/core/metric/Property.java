package de.uni_leipzig.simba.saim.core.metric;

import java.util.Collections;
import java.util.Set;
import lombok.Getter;
/** A similarity measure.*/
public class Property extends Node
{
	//	@Override public String identifier() {return origin==Origin.SOURCE?"x.rdfs.testproperty":"y.rdfs.testproperty";}
	@Override public Set<String> identifiers()	{return Collections.<String>emptySet();}
	public byte getMaxChilds() {return 0;}
	public enum Origin {SOURCE,TARGET};
	//protected final String id;
	@Getter protected final Origin origin;
	@Override public Set<Class<? extends Node>> validChildClasses() {return Collections.emptySet();}
	// properties are leaves on the tree
	@Override public boolean acceptsChild(Node n)	{return false;}

	/**@param id Example: "x.dc:title" the variable without question mark followed by a period followed by the prefix, a colon and the suffix.
	 * @param origin either Origin.SOURCE (property belongs to the source data source)
	 * or Origin.TARGET (property belongs to the target data source)*/
	public Property(String id, Origin origin)
	{
		super(id);
		String regex = "\\w+\\.\\w+:?\\w+";
		if(!id.matches(regex)) throw new MetricFormatException("id \""+id+"\" does not confirm to the regex "+regex);
		this.origin=origin;
	}
}
