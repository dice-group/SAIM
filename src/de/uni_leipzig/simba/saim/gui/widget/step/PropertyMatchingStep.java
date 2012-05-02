package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.panel.PropertyMatchingPanel;

public class PropertyMatchingStep implements WizardStep
{

	@Override
	public String getCaption() {return Messages.getString("propertymatching");}

	@Override
	public Component getContent()
	{
		return new PropertyMatchingPanel();
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
