package de.uni_leipzig.simba.saim.gui.widget.panel.selfconfiguration;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.form.SelfConfigMeshBasedBean;
import de.uni_leipzig.simba.saim.gui.widget.form.SelfConfigMeshBasedForm;
import de.uni_leipzig.simba.saim.gui.widget.panel.ExecutionPanel;
import de.uni_leipzig.simba.selfconfig.ComplexClassifier;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.SimpleClassifier;
/**
 * Displays self configuration panel. Loads data as in Configuration specified and runs the 
 * MeshBasedSelfConfigurator.
 * @author Lyko
 *
 */
public class MeshBasedSelfConfigPanel extends SelfConfigExecutionPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -457009966196619211L;
	private static transient final Logger logger = LoggerFactory.getLogger(MeshBasedSelfConfigPanel.class);
	
	MeshBasedSelfConfigurator bsc;
	List<SimpleClassifier> classifiers;
	ComplexClassifier cc;
	Select resultSelect = new Select();
	String generatedMetricexpression = "";
	Thread thread;
		
	// to config self config
	SelfConfigMeshBasedBean bean = new SelfConfigMeshBasedBean();
	SelfConfigMeshBasedForm form;
	
	/**
	 * Constructor to may embed Panel in a parent component, e.g. an existing WizardStep Component.
	 * @param parentComponent
	 */
	public MeshBasedSelfConfigPanel(SAIMApplication application, final Messages messages) {
		super(application, messages);
	}
	
	@Override
	public void attach() {
		this.config = ((SAIMApplication)getApplication()).getConfig();
		super.init();
	}
	
	@Override
	protected void performSelfConfiguration() {
		mainLayout.addComponent(indicator);
		mainLayout.addComponent(stepPanel);
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
				stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.performselfconfig")); //$NON-NLS-1$
				
				bsc = bean.getConfigurator(bean.getClassifierName(), sourceCache, targetCache, bean.getMinCoverage(), bean.getBeta());
			//			new MeshBasedSelfConfigurator(sourceCache, targetCache, bean.getMinCoverage(), bean.getBeta());
				classifiers = bsc.getBestInitialClassifiers();
				showSimpleClassifiers();
				indicator.setValue(new Float(4f/steps));
				stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.gotinitialclassifiers")); //$NON-NLS-1$
				if(classifiers.size()>0) {
					classifiers = bsc.learnClassifer(classifiers);
					//@TODO interface to change parameters
					cc = bsc.getZoomedHillTop(bean.getGridPoints(), bean.getIterations(), classifiers);
					System.out.println(cc);
					for(SimpleClassifier co:cc.classifiers) {
						System.out.println(co);
					}
//					classifiers = cc.classifiers;
					indicator.setValue(new Float(5f/steps));
					stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.complexclassifiercaption")); //$NON-NLS-1$
					generatedMetricexpression = generateMetric(cc.classifiers, "");
					showComplexClassifier();
					if(cc.mapping != null && cc.mapping.size()>0)
						learnedMapping = cc.mapping;
					config.setMetricExpression(generatedMetricexpression);
					config.setAcceptanceThreshold(getThreshold(cc.classifiers));
					System.out.println("SelfConfig class= "+bsc.getClass().getCanonicalName());

					onFinish(sourceCache, targetCache);
				} else {
					indicator.setValue(new Float(5f/steps));
					stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.nosimpleclassifiers"));
				}
			}
		};
		thread.start();
	}
	
	/**
	 * Method to show results after initialization.
	 */
	private void showSimpleClassifiers() {
		if(classifiers.size()>0) {
			logger.info("Replacing property mapping.");
			config.propertyMapping = new PropertyMapping();
		}
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
		resultSelect.setWidth("80%");//$NON-NLS-1$
		resultSelect.setVisible(true);		
	}
	
	/**Method shows complex classifier*/
	private void showComplexClassifier() {
		if(this.cc == null)
			return;		
		Panel result = new Panel("Classifier: "+generatedMetricexpression
				+ " with pseudo f-measure="+cc.fMeasure);
		stepPanel.addComponent(result);
	}
	
	private void onFinish(Cache sC, Cache tC) {
		start.setEnabled(true);
		close.setEnabled(true);
		
		showMapping.addListener(new ShowPseudoMappingClickListener(sC, tC, learnedMapping, messages, getApplication().getMainWindow()));
		if(learnedMapping!= null && learnedMapping.size()>0) {
			showMapping.setEnabled(true);
		}	
	}

	/**Implements Listener for generateMetrik Button*/
	class GenerateMetricButtonClickListener implements Button.ClickListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7458046435037113584L;
		Layout l;
		public GenerateMetricButtonClickListener(Layout content) {
			l = content;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {			
			if(cc.classifiers.size()==1) {
				config.setAcceptanceThreshold(cc.classifiers.get(0).threshold);
			}
			String metric = generatedMetricexpression;
		
			config.setMetricExpression(metric);
			l.removeAllComponents();
			l.addComponent(new ExecutionPanel(messages));
		}
	}
	
	
	/**
	 * Generates Metric out of one SimpleClassifier
	 * @param sl 
	 * @return String like: <i>measure(sourceProp,targetProp)|threshold</i>
	 */
	private String generateMetric(SimpleClassifier sl) {
		KBInfo source=config.getSource();
		KBInfo target=config.getTarget();
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
	private String generateMetric(List<SimpleClassifier> originalCCList, String expr) {
		// need to copy them
		List<SimpleClassifier> sCList = new LinkedList<SimpleClassifier>();
		for(SimpleClassifier sC: originalCCList)
			sCList.add(sC);
		
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
	
	private double getThreshold(List<SimpleClassifier> classifiers) {
		double min = Double.MAX_VALUE;
		for(SimpleClassifier sC : classifiers) {
			if(sC.threshold <= min)
				min = sC.threshold;
		}
		return min>1?0.5d:min;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClose() {
		//FIXME save stopping of thread
		if(thread != null)
			if(thread.isAlive())
				thread.stop();
		application.refresh();
		
	}
	@Override
	public void start() {
//		nothing to do here
	}

	@Override
	protected Component getConfigPanel() {
		System.out.println("generating mesh form");		
		form = new SelfConfigMeshBasedForm(bean, messages);
		return form;
	}

	@Override
	protected Component getPerformPanel() {
		resultSelect.setCaption(messages.getString("MeshBasedSelfConfigPanel.classifierlistcaption")); //$NON-NLS-1$
		resultSelect.setNullSelectionAllowed(false);
		resultSelect.setVisible(false);
		
		resultPanel = new Panel();
		VerticalLayout resultLayout = new VerticalLayout();
		resultLayout.addComponent(resultSelect);
		resultPanel.setContent(resultLayout);
		return resultPanel;
	}

	@Override
	protected Component getDescriptionComponent() {
		System.out.println("Calling getDescriptionComponent() in Mesh");
		return new Label(messages.getString("MeshBasedSelfConfigPanel.description")); //$NON-NLS-1$
	}
}
