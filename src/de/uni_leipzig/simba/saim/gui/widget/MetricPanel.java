package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.learning.query.PropertyMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class MetricPanel extends Panel
{	
	TextField metricTextField = new TextField("Insert metric here");
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
		Panel accordionPanel = new Panel();
		accordionPanel.setWidth("40em"); //$NON-NLS-1$
		Panel graphPanel = new Panel();
		addComponent(accordionPanel);
		addComponent(graphPanel);
		final Accordion accordion = new Accordion();
		accordionPanel.addComponent(accordion);

		new Thread()
		{			
			@Override
			public void run()
			{
				performPropertyMapping();


				{
					{
						VerticalLayout sourceLayout = new VerticalLayout();
						Tab sourceTab = accordion.addTab(sourceLayout,Messages.getString("MetricPanel.sourceproperties")); //$NON-NLS-1$
						for(String s : sourceProps)
							sourceLayout.addComponent(new Label(s)); //$NON-NLS-1$
						//				sourceLayout.addComponent(new Label("example:bla")); //$NON-NLS-1$
					}
					{
						VerticalLayout targetLayout = new VerticalLayout();
						Tab targetTab = accordion.addTab(targetLayout,Messages.getString("MetricPanel.targetproperties")); //$NON-NLS-1$
						for(String t : targetProps)
							targetLayout.addComponent(new Label(t)); //$NON-NLS-1$
						//				targetLayout.addComponent(new Label("example:blu")); //$NON-NLS-1$
					}
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
				}
			}
		}.start();

		{
			//		setupContextHelp();
		}
	}

	private void performPropertyMapping() {
		TextArea textArea = new TextArea();
		Configuration config = Configuration.getInstance();
		String out = "";
		for(String s : config.getSource().restrictions)
			out+=s;
		textArea.setValue(out);
		layout.addComponent(textArea);
		PropertyMapper propMapper = new PropertyMapper();
		String classSource = getClassOfEndpoint(config.getSource());
		String classTarget = getClassOfEndpoint(config.getTarget());
		if(classSource != null && classTarget != null) {
			showErrorMessage("Getting property mapping...");
			propMapping = propMapper.getPropertyMapping(config.getSource().endpoint,
					config.getTarget().endpoint, classSource, classTarget);
			for(String s : propMapping.map.keySet())
				for(Entry<String, Double> e : propMapping.map.get(s).entrySet()) {
					sourceProps.add(s);
					targetProps.add(e.getKey());
				}
			textArea.setValue(propMapping.toString());
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
