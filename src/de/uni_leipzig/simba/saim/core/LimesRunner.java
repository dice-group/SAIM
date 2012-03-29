package de.uni_leipzig.simba.saim.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.Filter;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;

public class LimesRunner implements Serializable {
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this ); 
	 
	private int step = 0;
	private int maxStep = 5;
	private String message;
	public static final String MESSAGE = "message";
	public static final String STEP = "step";
	public static final String FINISHED = "ready";
		
	public LimesRunner() {
		message = "Initializing Limes...";
		changes.firePropertyChange(MESSAGE, "", message);
//		changes.fireIndexedPropertyChange(STEP, 0, 0);
	}	
	
	public Mapping runConfig(Configuration config) {
		ConfigReader cR = new ConfigReader();
		
		fire("Getting source cache...");
		HybridCache sC = HybridCache.getData(config.getSource());
		fire("Getting target cache...");
		HybridCache tC = HybridCache.getData(config.getTarget());
		fire("Starting Matching process...");
		Filter f = new LinearFilter();
		// call Mapper			
		SetConstraintsMapper sCM = SetConstraintsMapperFactory.getMapper("simple", 
				config.getSource(), config.getTarget(), 
				sC, tC, f, config.granularity);
		
		Mapping actualMapping = sCM.getLinks(config.metricExpression, config.acceptanceThreshold);
		fire("Mapping performed.");
		return actualMapping;		
	}
	
	public void addPropertyChangeListener( PropertyChangeListener l ) 
	  { 
	    changes.addPropertyChangeListener( l ); 
	  } 
	 
	  public void removePropertyChangeListener( PropertyChangeListener l ) 
	  { 
	    changes.removePropertyChangeListener( l ); 
	  }
	  
	  private void fire(String message) {
		  this.message = message;
		  changes.firePropertyChange(MESSAGE, "", message);
		  changes.firePropertyChange(STEP, step, ++step);
	  }
}
