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
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.io.KBInfo;
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
	Select resultSelect = new Select();

	
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

		resultSelect.setCaption("Computed classifiers");
		resultSelect.setNullSelectionAllowed(false);
		

		mainLayout.removeComponent(indicator);
		mainLayout.removeComponent(stepPanel);
		resultPanel = new Panel();
		mainLayout.addComponent(resultPanel);
		// Buttons
		VerticalLayout resultLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		resultLayout.addComponent(resultSelect);
		resultLayout.addComponent(buttonLayout);
		resultPanel.setContent(resultLayout);		
		nextRound = new Button("Compute next round");
		nextRound.addListener(new NextRoundButtonClickListener());
		generateMetrik = new Button("Generate Metric");
		generateMetrik.addListener(new GenerateMetricButtonClickListener(mainLayout));
		buttonLayout.addComponent(nextRound);
		buttonLayout.addComponent(generateMetrik);
		
		performSelfConfiguration();
	}
	
	/**
	 * PerformsSelfConfiguration
	 */
	protected void performSelfConfiguration() {
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
		Configuration config = Configuration.getInstance();
		for(SimpleClassifier cl : classifiers) {
			resultSelect.addItem(cl);
			resultSelect.select(cl);
			config.addPropertiesMatch(cl.sourceProperty, cl.targetProperty);
		}
		
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
	class GenerateMetricButtonClickListener implements Button.ClickListener {
		Layout l;
		public GenerateMetricButtonClickListener(Layout content) {
			l = content;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			SimpleClassifier cl = (SimpleClassifier) resultSelect.getValue();
			String metric = generateMetric(cl);
			System.out.println(metric + " >= "+cl.threshold);
			Configuration.getInstance().setMetricExpression(metric);
			Configuration.getInstance().setAcceptanceThreshold(cl.threshold);
			l.removeAllComponents();
			l.addComponent(new ExecutionPanel());
		}		
		private String generateMetric(SimpleClassifier cl) {
			KBInfo source=Configuration.getInstance().getSource();
			KBInfo target=Configuration.getInstance().getTarget();
			String metric = "";
			
			metric += cl.measure+"("+source.var.replaceAll("\\?", "")+"."+cl.sourceProperty;
			metric +=","+target.var.replaceAll("\\?", "")+"."+cl.targetProperty+")";
			return metric;
		}		
	}
	/**Controls Action taken by nextRound Button.*/
	class NextRoundButtonClickListener implements Button.ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			classifiers = bsc.learnClassifer(classifiers);
			showResults();
		}		
	}
}
