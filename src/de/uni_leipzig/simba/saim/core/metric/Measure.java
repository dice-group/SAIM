package de.uni_leipzig.simba.saim.core.metric;

/** A similarity measure.*/
public class Measure extends Node
{
	@Override public String getIdentifier() {return "trigrams";}
	@Override public byte getMaxChilds() {return 2;}
	@Override public boolean validParentOf(Node node) {return (node instanceof Property);}
		
	/**{@inheritDoc} 
	 * A fully connected measure contains exactly one source- and one target property.  */
	@Override public boolean acceptsChild(Node node)
	{
		return super.acceptsChild(node)&& // super.acceptsChild calls validParentOf(node), so node instanceof Property. 
				(getChilds().isEmpty()
						||((Property)getChilds().iterator().next()).origin!=((Property)node).origin);
	}
}