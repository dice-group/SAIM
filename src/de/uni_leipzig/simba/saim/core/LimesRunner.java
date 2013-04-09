package de.uni_leipzig.simba.saim.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.Filter;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;

public class LimesRunner implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -7235467719847804816L;

	private PropertyChangeSupport changes = new PropertyChangeSupport( this );

	private int step = 0;
	public static final int MAX_STEPS = 5;
	private String message;
	public static final String MESSAGE = "message";
	public static final String STEP = "step";
	public static final String FINISHED = "ready";
	public static final String ERROR = "error";
	HybridCache tC;
	HybridCache sC;

	public LimesRunner() {
		message = "Initializing Limes...";
		changes.firePropertyChange(MESSAGE, "", message);
	}

	public Mapping runConfig(Configuration config) throws CachingException {
		fire("Getting source cache...");
		try {
			sC = HybridCache.getData(config.getSource());
		} catch(Exception e) {
			throw new CachingException(config.source, e);
		}
		fire("Getting target cache...");
		try {
			tC = HybridCache.getData(config.getTarget());
		} catch(Exception e) {
			throw new CachingException(config.getTarget(), e);
		}
		fire("Initialize Mapping...");
		Filter f = new LinearFilter();
		// call Mapper
		SetConstraintsMapper sCM = SetConstraintsMapperFactory.getMapper("simple",
				config.getSource(), config.getTarget(),
				sC, tC, f, config.granularity);
		fire("Starting Mapping process...");
		Mapping actualMapping;
		try{
			actualMapping = sCM.getLinks(config.getMetricExpression(), config.getAcceptanceThreshold());
			fire("Mapping performed.");
		}catch(Exception e) {
			changes.firePropertyChange(ERROR, null, e.getMessage());
			actualMapping = new Mapping();
		}
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
	  
	  /**
	   * TODO best practice???
	   * @author Lyko
	   *
	   */
	 public class CachingException extends Exception {
		  KBInfo info;
		  Exception root;
		  public CachingException(KBInfo info, Exception rootCause) {
			  this.info = info;
			  this.root = rootCause;
		  }
		  
		  public String toString() {
			  return "Exception caching data of "+info.endpoint+" Rootcause:"+root;
		  }
	  }
}
