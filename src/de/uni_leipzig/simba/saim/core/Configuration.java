package de.uni_leipzig.simba.saim.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;

/**Class holds all configuration settings for a linking process. */
public class Configuration {	
	private static Configuration instance = new Configuration();	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this ); 
	public static final String SETTING_CONFIG = "setting from xml";
	protected String id = null;
	protected String name;
	
	protected KBInfo source = null;
	protected KBInfo target = null;
	protected String metricExpression;
	protected double acceptanceThreshold;
	protected double verificationThreshold;
	protected int granularity;

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
	
	public void setFromConfigReader(ConfigReader cR) {
		this.cR = cR;
		source = cR.sourceInfo;
		target = cR.targetInfo;
		metricExpression = cR.metricExpression;
		acceptanceThreshold = cR.acceptanceThreshold;
		verificationThreshold = cR.verificationThreshold;
		granularity = cR.granularity;		
		changes.firePropertyChange(SETTING_CONFIG, null, this);
	}
	
	public void addPropertyChangeListener( PropertyChangeListener l ) 
	  { 
	    changes.addPropertyChangeListener( l ); 
	  } 
	 
	  public void removePropertyChangeListener( PropertyChangeListener l ) 
	  { 
	    changes.removePropertyChangeListener( l ); 
	  }
}