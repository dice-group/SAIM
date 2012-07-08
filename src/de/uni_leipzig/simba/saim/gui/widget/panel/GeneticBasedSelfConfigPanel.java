package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.HashMap;

import org.jgap.InvalidConfigurationException;


import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.selfconfig.BasicGeneticSelfConfigurator;
import de.uni_leipzig.simba.genetics.selfconfig.GeneticSelfConfigurator;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.form.SelfConfigGeneticBasedBean;
import de.uni_leipzig.simba.saim.gui.widget.form.SelfConfigGeneticBasedForm;
/**
 * Panel displayed after selecting genetic based self configuration.
 * Shows form to configure the the genetic based learning approach, and starts 
 * learning process. Extends PerformPanel to stop learning action on close().
 * @author Lyko
 *
 */
public class GeneticBasedSelfConfigPanel extends PerformPanel {
	private static final long serialVersionUID = -6889241029277079008L;
	private final Messages messages;
	private Layout mainLayout;
	private Configuration config;
	//to show progress
	final ProgressIndicator indicator = new ProgressIndicator();
	final Panel stepPanel = new Panel();
	Panel resultPanel;
	Metric learnedMetric;
	Mapping learnedMapping = new Mapping();
	// configuration
	SelfConfigGeneticBasedBean bean = new SelfConfigGeneticBasedBean();
	SelfConfigGeneticBasedForm form;
	//perform
	Thread thread;
	Button start;
	
	public GeneticBasedSelfConfigPanel(final Messages messages) {
		this.messages = messages;
	}

	
	/**
	 * Method initializes the view. Called on attachment.
	 */
	private void init() {
		mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		Label descriptor = new Label("Configure and run a selfconfiguration based on Genetic Programming."); //$NON-NLS-1$
		mainLayout.addComponent(descriptor);
		Refresher refresher = new Refresher();
		SelfConfigRefreshListener listener = new SelfConfigRefreshListener();
		refresher.addListener(listener);
		addComponent(refresher);

		indicator.setCaption("Current action"); 
		mainLayout.addComponent(indicator);
		indicator.setImmediate(true);
		indicator.setVisible(false);
		
	
		stepPanel.setCaption(""); 
		mainLayout.addComponent(stepPanel);
		stepPanel.setVisible(false);
		if(!config.propertyMapping.wasSet()) {
			Panel info = new Panel("No property mapping was set. So we use a default.");
			config.propertyMapping.setDefault(config.getSource(), config.getTarget());
			mainLayout.addComponent(info);
		}
		
		mainLayout.addComponent(form = new SelfConfigGeneticBasedForm(bean, messages));
		start = new Button("Start learning");
		start.addListener(new ClickListener() {
			private static final long serialVersionUID = 5899998766641774597L;
			@Override
			public void buttonClick(ClickEvent event) {
				mainLayout.removeComponent(start);
				indicator.setVisible(true);
				stepPanel.setVisible(true);
				performSelfConfiguration();
			}
		});
		mainLayout.addComponent(start);
		
		
		resultPanel = new Panel();
		mainLayout.addComponent(resultPanel);
		// Buttons
		VerticalLayout resultLayout = new VerticalLayout();
		resultPanel.setContent(resultLayout);	
	}
	
	/**
	 * Runs the algorithm. Steps: 
	 * (1)get Caches
	 * (2)get bean settings
	 * (3)learn metric
	 */
	protected void performSelfConfiguration() {
		mainLayout.addComponent(indicator);
		mainLayout.addComponent(stepPanel);
		thread = new Thread() {
			public void run() {
				float steps = 5f;
				indicator.setValue(new Float(1f/steps));
				indicator.requestRepaint();
				stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.sourcecache")); //$NON-NLS-1$
				HybridCache sourceCache = HybridCache.getData(config.getSource());
				indicator.setValue(new Float(2f/steps));
				indicator.requestRepaint();
				stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.targetcache")); //$NON-NLS-1$
				HybridCache targetCache = HybridCache.getData(config.getTarget());
				indicator.setValue(new Float(3f/steps));
				stepPanel.setCaption("Initializing Learning algorithm...");
				indicator.requestRepaint();
				HashMap<String, Object> params = bean.getConfiguartorParams(config, sourceCache, targetCache);
				indicator.setValue(new Float(4f/steps));
				stepPanel.setCaption("Learning..."); 
				indicator.requestRepaint();
				GeneticSelfConfigurator learner = new BasicGeneticSelfConfigurator();
				try {
					learnedMetric = learner.learn(params);
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
					indicator.setValue(1f);
					stepPanel.setCaption("An Error occured: "+e.getMessage());
					indicator.requestRepaint();
				}
				indicator.setValue(1f);
				stepPanel.setCaption("Ready! Setting the learned Link Specification: "+learnedMetric);
				indicator.requestRepaint();
				config.setMetricExpression(learnedMetric.expression);
				config.setAcceptanceThreshold(learnedMetric.threshold);
				
				learnedMapping = learner.getMapping();			
				onFinish(sourceCache, targetCache);
			}
		};
		thread.start();	
	}

	/**
	 * Method displays button to show the learned Mapping.
	 */
	private void onFinish(Cache sC, Cache tC) {
		Button showMapping = new Button("Show Mapping");
		showMapping.addListener(new ShowPseudoMappingClickListener(sC, tC, learnedMapping, messages, getApplication().getMainWindow()));
		if(learnedMapping!= null && learnedMapping.size()>0)
			resultPanel.addComponent(showMapping);
	}
	
	/**To enable refreshing while multithreading*/
	public class SelfConfigRefreshListener implements RefreshListener  {
		boolean running = true; 
		private static final long serialVersionUID = -8765221895426102605L;		    
		@Override 
		public void refresh(final Refresher source)	{
			if(!running) {
				removeComponent(source);
				source.setEnabled(false);
			}
		}
	}

	@Override
	public void attach() {
		this.config = ((SAIMApplication)getApplication()).getConfig();
		init();
	}
	
	@Override
	public void onClose() {
		if(thread != null)
			if(thread.isAlive())
				thread.stop();
		((SAIMApplication) getApplication()).refresh();
	}

	@Override
	public void start() {
		//nothing to do here so far...
	}
	
	class ShowPseudoMappingClickListener implements ClickListener {
		private static final long serialVersionUID = -5605524411526653096L;
		Mapping data;
		Cache sC;
		Cache tC;
		Messages messages;
		Window parent;
		
		public ShowPseudoMappingClickListener(Cache sC, Cache tC, Mapping data, Messages messages, Window parent) {
			this.sC = sC;
			this.tC = tC;
			this.data = data;
			this.messages = messages;
			this.parent = parent;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			Window sub = new Window("Pseudo Results");
			InstanceMappingTable table = new InstanceMappingTable(getApplication(), config, data, sC, tC, false, messages);
			ResultPanel res = new ResultPanel(table, messages);
			sub.setSizeUndefined();
			sub.setContent(res);
			sub.setWidth("90%");
//			sub.setPositionX(parent.);
			parent.addWindow(sub);
		}
		
	}
}
