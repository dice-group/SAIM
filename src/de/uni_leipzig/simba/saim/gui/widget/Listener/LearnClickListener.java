package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.panel.LearningPanel;

/**Listener for SelfConfig button.*/
public  class LearnClickListener extends MetricPanelListeners implements Button.ClickListener
{
	private static final long serialVersionUID = -3099913074308209584L;
	public LearnClickListener(Application app, final Messages messages) {
		super(app, messages);
	}
	@Override
	public void buttonClick(ClickEvent event) {
		getWindow(new LearningPanel(messages));
	}			
}