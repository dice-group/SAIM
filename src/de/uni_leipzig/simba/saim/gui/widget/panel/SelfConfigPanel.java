package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.List;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.SimpleClassifier;

public class SelfConfigPanel extends Panel{
	
	private Component parentComponent;
	private Layout mainLayout;
	MeshBasedSelfConfigurator bsc;
	List<SimpleClassifier> classifiers;
	final ProgressIndicator indicator = new ProgressIndicator();
	final Panel stepPanel = new Panel();
	Panel resultPanel;
	Button nextRound;
	Button generateMetrik;
	/**
	 * Constructor to may embed Panel in a parent component, e.g. an existing WizardStep Component.
	 * @param parentComponent
	 */
	public SelfConfigPanel(Component parentComponent) {
		super();
		this.parentComponent = parentComponent;
		init();
	}
	/**
	 * Initialize all Panel.
	 */
	private void init() {
		this.setCaption("Self Configuration interface");
		mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		Label descriptor = new Label("Runs and controls the self configuration approach.");
		mainLayout.addComponent(descriptor);
		Refresher refresher = new Refresher();
		SelfConfigRefreshListener listener = new SelfConfigRefreshListener();
		refresher.addListener(listener);
		addComponent(refresher);

		
		indicator.setCaption("Progress");
		mainLayout.addComponent(indicator);
		indicator.setImmediate(true);

		
		stepPanel.setCaption("Starting self configuration");
		mainLayout.addComponent(stepPanel);

		new Thread() {
			public void run() {

				float steps = 5f;
				indicator.setValue(new Float(1f/steps));
				indicator.requestRepaint();
				stepPanel.setCaption("Getting source cache...");
				HybridCache sourceCache = HybridCache.getData(Configuration.getInstance().getSource());
				indicator.setValue(new Float(2f/steps));
				indicator.requestRepaint();
				stepPanel.setCaption("Getting target cache...");
				HybridCache targetCache = HybridCache.getData(Configuration.getInstance().getTarget());
				indicator.setValue(new Float(3f/steps));
				stepPanel.setCaption("Performing self configuration...");
				
				bsc = new MeshBasedSelfConfigurator(sourceCache, targetCache, 0.6, 0.5);
				classifiers = bsc.getBestInitialClassifiers();						
				indicator.setValue(new Float(4f/steps));
				
				stepPanel.setCaption("Performed self configuration:");
				showResults();

			}
		}.start();
	}
	/**
	 * Method to show results after initialization.
	 */
	private void showResults() {
		String outString  = "";
		System.out.println("Computed classifiers:");
		for(SimpleClassifier cl : classifiers) {
			System.out.println(cl);
			outString +=cl.toString()+"<\br>";
		}
		mainLayout.removeComponent(indicator);
		mainLayout.removeComponent(stepPanel);
		resultPanel = new Panel();
		mainLayout.addComponent(resultPanel);
		// Buttons
		HorizontalLayout buttonLayout = new HorizontalLayout();
		resultPanel.setCaption(outString);
		resultPanel.setContent(buttonLayout);		
		nextRound = new Button("Compute next round");
		generateMetrik = new Button("Generate Metric");
		if(classifiers.isEmpty())
			generateMetrik.setEnabled(false);
		
		
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
	
	/**Implements Listener for generateMetrik Button*/
	class generateMetricButtonClickListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		private String generateMetric() {
			String metric = "";
			for(SimpleClassifier cl : classifiers) {
				
			}
			return metric;
		}
		
	}
}
