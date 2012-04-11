package de.uni_leipzig.simba.saim.core.metric;

import java.util.Arrays;

import de.uni_leipzig.simba.saim.core.metric.Property.Origin;

public class MetricParser
{
	protected static Node parsePart(String s, String sourceVar) throws MetricFormatException
	{
//		if(!s.contains("(")) {return output;}
		String[] tokens = s.split("[(),]");		
		String id = tokens[0].trim();
		if(tokens.length==1) return new Property(id,id.startsWith(sourceVar)?Origin.SOURCE:Origin.TARGET);
		Node node = Node.createNode(id);
		for(String argument: Arrays.copyOfRange(tokens,1,tokens.length))
		{
			if(!node.addChild(parsePart(argument,sourceVar))) {throw new MetricFormatException("Could not add child \""+argument+'"');}
		}				
		return node;
	}

	/** Parses the string and returns the Output root node of the metric tree represented by the argument in a metric expression.
	 * @param s the metric expression as required by LIMES, e.g. <code>trigrams(x.rdfs.label,y.rdfs.label)</code>,
	 * see <a href="http://aksw.org/Projects/LIMES/files?get=limes_manual.pdf">the LIMES manual.</a>
	 * @param sourceVar the source variable without question mark, e.g. "x"
	 * @return the Output root node of the metric tree represented by the argument in a metric expression.
	 * @throws MetricFormatException  if the string does not contain a parsable metric. */
	public static Output parse(String s,String sourceVar) throws MetricFormatException
	{
		if(s.isEmpty()) throw new MetricFormatException();
		Output output = new Output();
		output.addChild(parsePart(s,sourceVar));
		return output;
	}
}