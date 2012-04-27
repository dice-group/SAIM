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
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.selfconfig.ComplexClassifier;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.SimpleClassifier;
/**
 * Displays self configuration panel.
 * @author Lyko
 *
 */
public class SelfConfigPanel extends Panel{
	
	private Component parentComponent;
	private Layout mainLayout;
	MeshBasedSelfConfigurator bsc;
	List<SimpleClassifier> classifiers;
	ComplexClassifier cc;
	final ProgressIndicator indicator = new ProgressIndicator();
	final Panel stepPanel = new Panel();
	Panel resultPanel;
//	Button nextRound;
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
		this.setCaption(Messages.getString("SelfConfigPanel.caption")); //$NON-NLS-1$
		mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		Label descriptor = new Label(Messages.getString("SelfConfigPanel.description")); //$NON-NLS-1$
		mainLayout.addComponent(descriptor);
		Refresher refresher = new Refresher();
		SelfConfigRefreshListener listener = new SelfConfigRefreshListener();
		refresher.addListener(listener);
		addComponent(refresher);

		
		indicator.setCaption(Messages.getString("SelfConfigPanel.progress")); //$NON-NLS-1$
		mainLayout.addComponent(indicator);
		indicator.setImmediate(true);

		
		stepPanel.setCaption(Messages.getString("SelfConfigPanel.panelcaption")); //$NON-NLS-1$
		mainLayout.addComponent(stepPanel);

		resultSelect.setCaption(Messages.getString("SelfConfigPanel.classifierlistcaption")); //$NON-NLS-1$
		resultSelect.setNullSelectionAllowed(false);
		

		
		resultPanel = new Panel();
		mainLayout.addComponent(resultPanel);
		// Buttons
		VerticalLayout resultLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		resultLayout.addComponent(resultSelect);
		resultLayout.addComponent(buttonLayout);
		resultPanel.setContent(resultLayout);		
//		nextRound = new Button(Messages.getString("SelfConfigPanel.nextroundbutton")); //$NON-NLS-1$
//		nextRound.addListener(new NextRoundButtonClickListener());
		generateMetrik = new Button(Messages.getString("SelfConfigPanel.generatemetricbutton")); //$NON-NLS-1$
		generateMetrik.addListener(new GenerateMetricButtonClickListener(mainLayout));
//		buttonLayout.addComponent(nextRound);
		buttonLayout.addComponent(generateMetrik);
		
		performSelfConfiguration();
	}
	
	/**
	 * PerformsSelfConfiguration
	 */
	protected void performSelfConfiguration() {
		mainLayout.addComponent(indicator);
		mainLayout.addComponent(stepPanel);
		new Thread() {
			public void run() {

				float steps = 5f;
				indicator.setValue(new Float(1f/steps));
				indicator.requestRepaint();
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.sourcecache")); //$NON-NLS-1$
				HybridCache sourceCache = HybridCache.getData(Configuration.getInstance().getSource());
				indicator.setValue(new Float(2f/steps));
				indicator.requestRepaint();
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.targetcache")); //$NON-NLS-1$
				HybridCache targetCache = HybridCache.getData(Configuration.getInstance().getTarget());
				indicator.setValue(new Float(3f/steps));
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.performselfconfig")); //$NON-NLS-1$
				
				bsc = new MeshBasedSelfConfigurator(sourceCache, targetCache, 0.6, 0.5);
				classifiers = bsc.getBestInitialClassifiers();
				showResults();
				indicator.setValue(new Float(4f/steps));
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.gotinitialclassifiers")); //$NON-NLS-1$
				
//				classifiers = bsc.learnClassifer(classifiers);
				cc = bsc.getZoomedHillTop(5, 5, classifiers);
				System.out.println(cc);
				for(SimpleClassifier co:cc.classifiers) {
					System.out.println(co);
				}
				System.out.println(cc.fMeasure);
//				classifiers = cc.classifiers;
				indicator.setValue(new Float(5f/steps));
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.complexclassifiercaption")+cc); //$NON-NLS-1$
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
			System.out.println(metric + " >= "+cl.threshold); //$NON-NLS-1$
			Configuration.getInstance().setMetricExpression(metric);
			Configuration.getInstance().setAcceptanceThreshold(cl.threshold);
			l.removeAllComponents();
			l.addComponent(new ExecutionPanel());
		}		
		private String generateMetric(SimpleClassifier sl) {
			KBInfo source=Configuration.getInstance().getSource();
			KBInfo target=Configuration.getInstance().getTarget();
			String metric = ""; //$NON-NLS-1$
			
			metric += sl.measure+"("+source.var.replaceAll("\\?", "")+"."+sl.sourceProperty; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			metric +=","+target.var.replaceAll("\\?", "")+"."+sl.targetProperty+")|"+sl.threshold; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			return metric;
		}

//		private String generateMetric(ComplexClassifier cl) {
//			if(cl.classifiers.size()>1) {//more than a measure
//				String complexMetric = "AND(";
//				complexMetric+=generateMetric(cl.classifiers.get(0))+","+generateMetric(cl.classifiers.get(1));
//				complexMetric+=")";
//				return complexM
//			} else {//multiple measures
//				return generateMetric(cl.classifiers.get(0));
//			}
//		}
	}
//	/**Controls Action taken by nextRound Button.*/
//	class NextRoundButtonClickListener implements Button.ClickListener {
//		@Override
//		public void buttonClick(ClickEvent event) {
//			classifiers = bsc.learnClassifer(classifiers);
//			showResults();
//		}		
//	}
}
