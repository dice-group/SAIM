package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.gui.widget.panel.EndpointPanel;

public class PropertyMatchingStep implements WizardStep
{

	@Override
	public String getCaption() {return "Property Matching";}

	@Override
	public Component getContent()
	{
		return new EndpointPanel();
	}

	@Override
	public boolean onAdvance()
	{
		return false;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
