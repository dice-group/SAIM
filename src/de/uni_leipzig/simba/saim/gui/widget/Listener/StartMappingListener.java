package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.panel.ExecutionPanel;

/**Listener for SelfConfig button.*/
public  class StartMappingListener extends MetricPanelListeners implements Button.ClickListener
{
	private static final long serialVersionUID = -688724646561873984L;
	public StartMappingListener(Application app, final Messages messages){
		super(app, messages);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		getWindow(new ExecutionPanel(messages));
	}			
}