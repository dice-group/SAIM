package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.gui.widget.EndpointPanel;

public class ClassMatchingStep implements WizardStep
{

	@Override
	public String getCaption() {return "Class Matching";}

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
