package de.uni_leipzig.simba.saim.gui.widget.panel;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.ui.Panel;

import de.uni_leipzig.simba.saim.core.Configuration;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class PropertyMatchingPanel extends Panel
{		
	Configuration config = Configuration.getInstance();
	
	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getContent().addComponent(contextHelp);
	}

	public PropertyMatchingPanel()
	{
		setupContextHelp();
	}
}