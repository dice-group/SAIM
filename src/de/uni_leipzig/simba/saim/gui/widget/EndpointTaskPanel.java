package de.uni_leipzig.simba.saim.gui.widget;

public class EndpointTaskPanel extends TaskPanel
{
	public EndpointTaskPanel()
	{
	KBInfoForm kbISource = new KBInfoForm("Configure Source endpoint");
	KBInfoForm kbITarget = new KBInfoForm("Configure Target endpoint");
	this.addComponent(kbISource);
	this.addComponent(kbITarget);
	
	}
}