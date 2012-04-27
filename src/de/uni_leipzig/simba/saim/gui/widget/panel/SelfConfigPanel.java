package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Displays self configuration panel. Loads data as in Configuration specified and the runs the 
 * MeshBasedSelfConfigurator.
 * @author Lyko
 *
 */
public class SelfConfigPanel extends Panel{
	static final Logger logger = LoggerFactory.getLogger(SelfConfigPanel.class);
	private Component parentComponent;
	private Layout mainLayout;
	MeshBasedSelfConfigurator bsc;
	List<SimpleClassifier> classifiers;
	ComplexClassifier cc;
	final ProgressIndicator indicator = new ProgressIndicator();
	final Panel stepPanel = new Panel();
	Panel resultPanel;
//	Button nextRound;
	Button generateMetric;
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
		generateMetric = new Button(Messages.getString("SelfConfigPanel.generatemetricbutton")); //$NON-NLS-1$
		generateMetric.addListener(new GenerateMetricButtonClickListener(mainLayout));
		generateMetric.setEnabled(false);
//		buttonLayout.addComponent(nextRound);
		buttonLayout.addComponent(generateMetric);
		
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
				showSimpleClassifiers();
				indicator.setValue(new Float(4f/steps));
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.gotinitialclassifiers")); //$NON-NLS-1$
				
//				classifiers = bsc.learnClassifer(classifiers);
				cc = bsc.getZoomedHillTop(5, 5, classifiers);
				System.out.println(cc);
				for(SimpleClassifier co:cc.classifiers) {
					System.out.println(co);
				}
//				classifiers = cc.classifiers;
				indicator.setValue(new Float(5f/steps));
				stepPanel.setCaption(Messages.getString("SelfConfigPanel.complexclassifiercaption")); //$NON-NLS-1$
				showComplexClassifier();

			}
		}.start();
	}
	
	/**
	 * Method to show results after initialization.
	 */
	private void showSimpleClassifiers() {
		Configuration config = Configuration.getInstance();
		for(SimpleClassifier cl : classifiers) {
			resultSelect.addItem(cl);
			resultSelect.select(cl);
			if(cl.measure.equalsIgnoreCase("euclidean")) {
				logger.info("Adding number propertyMatch between: "+cl.sourceProperty +" - "+ cl.targetProperty);
				config.addPropertiesMatch(cl.sourceProperty, cl.targetProperty, false);
			}else {
				config.addPropertiesMatch(cl.sourceProperty, cl.targetProperty, true);
				logger.info("Adding string propertyMatch between: "+cl.sourceProperty +" - "+ cl.targetProperty);
			}
		}			
	}
	
	/**Method shows complex classifier*/
	private void showComplexClassifier() {
		if(this.cc == null)
			return;
		Panel result = new Panel("Classifier: "+generateMetric(cc.classifiers, "")
				+ " with pseudo f-measure="+cc.fMeasure);
		stepPanel.addComponent(result);
		generateMetric.setEnabled(true);
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
			if(cc.classifiers.size()==1) {
				Configuration.getInstance().setAcceptanceThreshold(cc.classifiers.get(0).threshold);
			}
			String metric = generateMetric(cc.classifiers, "");
		
			Configuration.getInstance().setMetricExpression(metric);
//			Configuration.getInstance().setAcceptanceThreshold(cl.threshold);
			l.removeAllComponents();
			l.addComponent(new ExecutionPanel());
		}
	}
	
	
	/**
	 * Generates Metric out of one SimpleClassifier
	 * @param sl 
	 * @return String like: <i>measure(sourceProp,targetProp)|threshold</i>
	 */
	private String generateMetric(SimpleClassifier sl) {
		KBInfo source=Configuration.getInstance().getSource();
		KBInfo target=Configuration.getInstance().getTarget();
		String metric = ""; //$NON-NLS-1$
		
		metric += sl.measure+"("+source.var.replaceAll("\\?", "")+"."+sl.sourceProperty; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		metric +=","+target.var.replaceAll("\\?", "")+"."+sl.targetProperty+")|"+sl.threshold; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return metric;
	}

	/**
	 * Recursive function to produce metric out of complex classifier.
	 * @param sCList List of simple classifier which remain to be added.
	 * @param expr The metric expression so far.
	 * @return
	 */
	private String generateMetric(List<SimpleClassifier> sCList, String expr) {
		if(sCList.size()==0)
			return expr;
		if(expr.length() == 0) {// nothing generated before
			if(sCList.size()==1) {
				String metric = generateMetric(sCList.get(0));
				return metric.substring(0, metric.lastIndexOf("|"));
			}
			else {// recursive
				String nestedExpr = "AND("+generateMetric(sCList.remove(0))+","+generateMetric(sCList.remove(0))+")";
				return generateMetric(sCList, nestedExpr);
			}
		} else { // have to combine, recursive
			String nestedExpr = "AND("+expr+","+generateMetric(sCList.remove(0))+")";
			return generateMetric(sCList, nestedExpr);
			
		}
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
