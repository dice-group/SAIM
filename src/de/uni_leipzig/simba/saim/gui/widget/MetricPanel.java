package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

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
		accordionPanel.setWidth("40em");
		Panel graphPanel = new Panel();
		this.getContent().addComponent(accordionPanel);
		
		this.getContent().addComponent(graphPanel);
		{
			Accordion accordion = new Accordion();
			accordionPanel.addComponent(accordion);
			{
				VerticalLayout sourceLayout = new VerticalLayout();
				Tab sourceTab = accordion.addTab(sourceLayout,"Source Properties");
				sourceLayout.addComponent(new Label("rdfs:label"));
				sourceLayout.addComponent(new Label("example:bla"));
			}
			{
				VerticalLayout targetLayout = new VerticalLayout();
				Tab targetTab = accordion.addTab(targetLayout,"Target Properties");
				targetLayout.addComponent(new Label("rdfs:label"));
				targetLayout.addComponent(new Label("example:blu"));
			}
			{
				VerticalLayout functionLayout = new VerticalLayout();
				Tab functionTab = accordion.addTab(functionLayout,"Functions");
			}
			{
				VerticalLayout metricLayout = new VerticalLayout();
				Tab metricTab = accordion.addTab(metricLayout,"Metrics");
			}
			{
				VerticalLayout operatorLayout = new VerticalLayout();
				Tab operatorTab = accordion.addTab(operatorLayout,"Operators");			
			}
		}
		{
			//		setupContextHelp();
		}
	}
}
