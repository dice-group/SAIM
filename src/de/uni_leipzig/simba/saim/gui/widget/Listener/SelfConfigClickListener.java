package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.panel.GenericSelfConfigurationPanel;

/**Listener for SelfConfig button.*/
public class SelfConfigClickListener extends MetricPanelListeners implements Button.ClickListener
{		
	public SelfConfigClickListener(Application app, final Messages messages) {
		super(app, messages);
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		getWindow(new GenericSelfConfigurationPanel(messages));
	}			
}
