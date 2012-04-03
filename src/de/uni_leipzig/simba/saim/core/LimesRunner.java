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
	public static final int MAX_STEPS = 5;
	private String message;
	public static final String MESSAGE = "message";
	public static final String STEP = "step";
	public static final String FINISHED = "ready";
	HybridCache tC;
	HybridCache sC;
	
	public LimesRunner() {
		message = "Initializing Limes...";
		changes.firePropertyChange(MESSAGE, "", message);
//		changes.fireIndexedPropertyChange(STEP, 0, 0);
	}	
	
	public Mapping runConfig(Configuration config) {
		fire("Getting source cache...");
	//	System.out.println(config.getSource());
		sC = HybridCache.getData(config.getSource());
		fire("Getting target cache...");
	//	System.out.println(config.getTarget());
		tC = HybridCache.getData(config.getTarget());
		fire("Initialize Mapping...");
		Filter f = new LinearFilter();
		// call Mapper			
		SetConstraintsMapper sCM = SetConstraintsMapperFactory.getMapper("simple", 
				config.getSource(), config.getTarget(), 
				sC, tC, f, config.granularity);
		fire("Starting Mapping process...");
		Mapping actualMapping = sCM.getLinks(config.getMetricExpression(), config.getAcceptanceThreshold());
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
	  
	  public HybridCache getSourceCache() {
		  return sC;
	  }
	  
	  public HybridCache getTargetCache() {
		  return tC;
	  }
}
