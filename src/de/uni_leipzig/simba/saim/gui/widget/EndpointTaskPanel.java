package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Configuration;

public class EndpointTaskPanel extends TaskPanel
{
	KBInfoForm kbISource, kbITarget;
	public EndpointTaskPanel()
	{
	kbISource = new KBInfoForm("Configure Source endpoint");
	kbITarget = new KBInfoForm("Configure Target endpoint");
	
	HorizontalLayout layout = new HorizontalLayout();
	layout.setSpacing(true);
	content.setContent(layout);
	content.addComponent(kbISource);
	content.addComponent(kbITarget);
	
	nextButton.addListener(new ClickListener()
	{
		@Override
		public void buttonClick(ClickEvent event)
		{
			if(kbISource.isValid() && kbITarget.isValid()) {
				// Proceed
				Configuration config = Configuration.getInstance();
				config.setSourceEndpoint(kbISource.getKBInfo());
				config.setTargetEndpoint(kbITarget.getKBInfo());
			}
		}
	});
	}
}