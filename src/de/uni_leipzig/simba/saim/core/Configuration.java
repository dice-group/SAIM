package de.uni_leipzig.simba.saim.core;

import de.uni_leipzig.simba.io.KBInfo;

/**Class holds all configuration settings for a linking process. */
public class Configuration {	
	protected String id = null;
	protected String name;
	
	protected KBInfo source = null;	
	protected KBInfo target = null;
	
	public Configuration() {
		
	}
	
	public void store() {
		
	}
}
