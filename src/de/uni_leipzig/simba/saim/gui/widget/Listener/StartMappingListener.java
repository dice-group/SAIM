package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.panel.ExecutionPanel;

/**Listener for SelfConfig button.*/
public  class StartMappingListener extends MetricPanelListeners implements Button.ClickListener
{
	private static final long serialVersionUID = -688724646561873984L;
	public StartMappingListener(SAIMApplication application, final Messages messages){
		super(application, messages);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		getWindow(new ExecutionPanel(messages));
	}			
}