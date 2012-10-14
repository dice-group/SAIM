package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.panel.LearningPanel;
import de.uni_leipzig.simba.saim.gui.widget.panel.MetricPanel;

/**Listener for SelfConfig button.*/
public  class LearnClickListener extends MetricPanelListeners implements Button.ClickListener
{
	private static final long serialVersionUID = -3099913074308209584L;
	private MetricPanel mp = null;
	
	public LearnClickListener(SAIMApplication application, final Messages messages, MetricPanel mp) {
		super(application, messages);
		this.mp = mp;
	}
	@Override
	public void buttonClick(ClickEvent event) {
		if(mp.setMetricFromGraph())	
			getWindow(new LearningPanel(application, messages));
	}			
}