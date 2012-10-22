package de.uni_leipzig.simba.saim.gui.widget.panel.selfconfiguration;

import java.util.HashMap;

import org.jgap.InvalidConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.selfconfig.BasicGeneticSelfConfigurator;
import de.uni_leipzig.simba.genetics.selfconfig.GeneticSelfConfigurator;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.form.SelfConfigGeneticBasedBean;
import de.uni_leipzig.simba.saim.gui.widget.form.SelfConfigGeneticBasedForm;

/**
 * Panel displayed after selecting genetic based self configuration.
 * Shows form to configure the the genetic based learning approach, and starts 
 * learning process. Extends PerformPanel to stop learning action on close().
 * @author Lyko
 */
public class GeneticBasedSelfConfigPanel extends SelfConfigExecutionPanel {
	private static final long serialVersionUID = -6889241029277079008L;
	Metric learnedMetric;
	// configuration
	SelfConfigGeneticBasedBean bean = new SelfConfigGeneticBasedBean();
	SelfConfigGeneticBasedForm form;
	//perform
	Thread thread;

	
	public GeneticBasedSelfConfigPanel(SAIMApplication application, final Messages messages) {
		super(application, messages);
	}


	@Override
	protected void performSelfConfiguration() {
		indicatorLayout.addComponent(indicator);
		indicatorLayout.addComponent(stepPanel);
		mainLayout.addComponent(indicatorLayout);
		thread = new Thread() {
			public void run() {
				start.setEnabled(false);
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
				config.setMetricExpression(learnedMetric.getExpression());
				config.setAcceptanceThreshold(learnedMetric.getThreshold());
				
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
		start.setEnabled(true);
		close.setEnabled(true);
		
		showMapping.addListener(new ShowPseudoMappingClickListener(sC, tC, learnedMapping, messages, getApplication().getMainWindow()));
		if(learnedMapping!= null && learnedMapping.size()>0) {
			showMapping.setEnabled(true);
		}			
	}

	@Override
	public void attach() {
		this.config = ((SAIMApplication)getApplication()).getConfig();
		super.init();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onClose() {
		if(thread != null)
			if(thread.isAlive())
				thread.stop();
		application.refresh();
	}

	@Override
	public void start() {
		//nothing to do here so far...
	}

	@Override
	protected Component getDescriptionComponent() {
		System.out.println("Calling getDescriptionComponent() in Gen");
		Label descr = new Label("Configure and run a selfconfiguration based on Genetic Programming.");
		if(!config.propertyMapping.wasSet()) {
			Panel info = new Panel("No property mapping was set. So we use a default.");
			config.propertyMapping.setDefault(config.getSource(), config.getTarget());
			VerticalLayout vl = new VerticalLayout();
			vl.addComponent(descr);
			vl.addComponent(info);
			return vl;
		} else {
			return descr;
		}
	}	


	@Override
	protected Component getConfigPanel() {
		System.out.println("generating genetic form");
		form = new SelfConfigGeneticBasedForm(bean, messages);
		return form;
	}


	@Override
	protected Component getPerformPanel() {
		resultPanel = new Panel();
		VerticalLayout resultLayout = new VerticalLayout();
		resultPanel.setContent(resultLayout);
		return resultPanel;
	}
}
