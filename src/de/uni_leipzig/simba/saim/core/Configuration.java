package de.uni_leipzig.simba.saim.core;

import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;

/**Class holds all configuration settings for a linking process. */
public class Configuration {	
	private static Configuration instance = null;
	
	protected String id = null;
	protected String name;
	
	protected KBInfo source = null;	

	protected KBInfo target = null;

	private ConfigReader cR;

	private Configuration() {
		
	}
	
	public void store() {
		
	}
	
	/**
	 * Implements Singleton pattern.
	 * @return
	 */
	public static Configuration getInstance() {
		if(instance == null) {
			instance = new Configuration();
		}
		return instance;
	}
	
	public void setSourceEndpoint(KBInfo source) {
		this.source = source;
	}
	public void setTargetEndpoint(KBInfo target) {
		this.target = target;
	}
	public KBInfo getSource() {
		return source;
	}
	public KBInfo getTarget() {
		return target;
	}
	
	public void setConfigReader(ConfigReader cR) {
		this.cR = cR;
	}
	
	public ConfigReader getConfigReader() {
		return cR;
	}
}
