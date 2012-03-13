package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.HorizontalLayout;

public class EndpointTaskPanel extends TaskPanel
{
	public EndpointTaskPanel()
	{
	KBInfoForm kbISource = new KBInfoForm("Configure Source endpoint");
	KBInfoForm kbITarget = new KBInfoForm("Configure Target endpoint");
	HorizontalLayout layout = new HorizontalLayout();
	layout.setSpacing(true);
	content.setContent(layout);
	content.addComponent(kbISource);
	content.addComponent(kbITarget);
	
	}
}