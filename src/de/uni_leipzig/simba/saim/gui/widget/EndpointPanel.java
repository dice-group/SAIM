package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class EndpointPanel extends Panel
{
	public final KBInfoForm kbISource;
	public final KBInfoForm kbITarget;

	public EndpointPanel()
	{
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		this.setContent(layout);

		kbISource = new KBInfoForm("Configure Source endpoint");
		kbITarget = new KBInfoForm("Configure Target endpoint");
		
		this.addComponent(kbISource);
		this.addComponent(kbITarget);
	}
}