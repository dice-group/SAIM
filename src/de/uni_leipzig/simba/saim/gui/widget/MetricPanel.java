package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class MetricPanel extends Panel
{	
	//	protected void setupContextHelp()
	//	{
	//		ContextHelp contextHelp = new ContextHelp();
	//		getContent().addComponent(contextHelp);
	//		//contextHelp.addHelpForComponent(proposals, "Source properties from the knowledgebase <insert id here> ....");
	//	}

	public MetricPanel()
	{
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(false);
		layout.setMargin(false);
		setContent(layout);
		Panel accordionPanel = new Panel();
		accordionPanel.setWidth("40em"); //$NON-NLS-1$
		Panel graphPanel = new Panel();
		this.getContent().addComponent(accordionPanel);
		TextArea textArea = new TextArea();
		Configuration config = Configuration.getInstance();
		String out = "";
		for(String s : config.getSource().restrictions)
			out+=s;
		textArea.setValue(out);
		layout.addComponent(textArea);
		this.getContent().addComponent(graphPanel);
		{
			Accordion accordion = new Accordion();
			accordionPanel.addComponent(accordion);
			{
				VerticalLayout sourceLayout = new VerticalLayout();
				Tab sourceTab = accordion.addTab(sourceLayout,Messages.getString("MetricPanel.sourceproperties")); //$NON-NLS-1$
				sourceLayout.addComponent(new Label("rdfs:label")); //$NON-NLS-1$
				sourceLayout.addComponent(new Label("example:bla")); //$NON-NLS-1$
			}
			{
				VerticalLayout targetLayout = new VerticalLayout();
				Tab targetTab = accordion.addTab(targetLayout,Messages.getString("MetricPanel.targetproperties")); //$NON-NLS-1$
				targetLayout.addComponent(new Label("rdfs:label")); //$NON-NLS-1$
				targetLayout.addComponent(new Label("example:blu")); //$NON-NLS-1$
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
		{
			//		setupContextHelp();
		}
	}
}
