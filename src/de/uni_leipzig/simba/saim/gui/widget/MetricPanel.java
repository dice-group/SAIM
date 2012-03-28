package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.learning.query.PropertyMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class MetricPanel extends Panel
{	
	private static final long	serialVersionUID	= 6766679517868840795L;
//	TextField metricTextField = new TextField("Insert metric here");
	Mapping propMapping;
	HorizontalLayout layout = new HorizontalLayout();
	Set<String> sourceProps = new HashSet<String>();
	Set<String> targetProps = new HashSet<String>();
	//	protected void setupContextHelp()
	//	{
	//		ContextHelp contextHelp = new ContextHelp();
	//		getContent().addComponent(contextHelp);
	//		//contextHelp.addHelpForComponent(proposals, "Source properties from the knowledgebase <insert id here> ....");
	//	}

	public MetricPanel()
	{
		layout.setSpacing(false);
		layout.setMargin(false);
		setContent(layout);
		layout.addComponent(new ManualMetricForm());
		final VerticalLayout accordionLayout = new VerticalLayout();
		addComponent(accordionLayout);		
		final ProgressIndicator progress = new ProgressIndicator();
		progress.setIndeterminate(false);
		accordionLayout.addComponent(progress);
		Panel accordionPanel = new Panel();
		accordionLayout.addComponent(accordionPanel);
		//accordionPanel.setVisible(false);
		accordionPanel.setWidth("40em"); //$NON-NLS-1$
		
		Panel graphPanel = new Panel();		
		addComponent(graphPanel);
		final Accordion accordion = new Accordion();		
		accordionPanel.addComponent(accordion);

		final VerticalLayout sourceLayout = new VerticalLayout();
		Tab sourceTab = accordion.addTab(sourceLayout,Messages.getString("MetricPanel.sourceproperties")); //$NON-NLS-1$

		final VerticalLayout targetLayout = new VerticalLayout();
		{Tab targetTab = accordion.addTab(targetLayout,Messages.getString("MetricPanel.targetproperties"));} //$NON-NLS-1$}

		{
			VerticalLayout functionLayout = new VerticalLayout();
			Tab functionTab = accordion.addTab(functionLayout,Messages.getString("MetricPanel.functions")); //$NON-NLS-1$
		}
		{
			VerticalLayout metricLayout = new VerticalLayout();
			Tab metricTab = accordion.addTab(metricLayout,Messages.getString("MetricPanel.metrics")); //$NON-NLS-1$
		}
		{
			VerticalLayout operatorLayout = new VerticalLayout();
			Tab operatorTab = accordion.addTab(operatorLayout,Messages.getString("MetricPanel.operators"));			 //$NON-NLS-1$
		}

		new Thread()
		{			
			@Override
			public void run()
			{
				performPropertyMapping();
				{

					for(String s : sourceProps) {
						sourceLayout.addComponent(new Label(s)); //$NON-NLS-1$
					}

					for(String t : targetProps) {
						targetLayout.addComponent(new Label(t));//$NON-NLS-1$
					} 
				}
				accordionLayout.removeComponent(progress);
				progress.setEnabled(false);
			}
		}.start();

		//		{
		//			//		setupContextHelp();
		//		}
		}

		private void performPropertyMapping() {
			Configuration config = Configuration.getInstance();
			config.getSource().properties.clear();
			config.getTarget().properties.clear();
			PropertyMapper propMapper = new PropertyMapper();
			String classSource = getClassOfEndpoint(config.getSource());
			String classTarget = getClassOfEndpoint(config.getTarget());
			if(classSource != null && classTarget != null) {
				showErrorMessage("Getting property mapping...");
				propMapping = propMapper.getPropertyMapping(config.getSource().endpoint,
						config.getTarget().endpoint, classSource, classTarget);
				for(String s : propMapping.map.keySet())
					for(Entry<String, Double> e : propMapping.map.get(s).entrySet()) {
						s=PrefixHelper.abbreviate(s);
						sourceProps.add(s);
						config.getSource().properties.add(s);
						System.out.println("Adding source property: "+s);
						String t = PrefixHelper.abbreviate(e.getKey());
						targetProps.add(t);
						config.getTarget().properties.add(t);
						System.out.println("Adding target property: "+t);
					}
			} else {
				showErrorMessage("Cannot perform automatic property mapping due to missing class specifications.");
			}		
		}

		/**
		 * Little helper function to retrieve classes out of restrictions of the LIMES SPEC. Whereas, a
		 * class is in a restriction of the format "?var rdf:type <class>".
		 * @param ep
		 * @return
		 */
		private String getClassOfEndpoint(KBInfo ep) {
			for(String rest : ep.restrictions) {
				if(rest.matches(".* rdf:type .*"))
					return rest.substring(rest.indexOf("rdf:type")+8).replaceAll("<", "").replaceAll(">", "").trim();
			}
			return null;
		}

		private void showErrorMessage(String message) {
			layout.setComponentError(new UserError(message));
		}
	}
