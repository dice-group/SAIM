package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.panel.PropertyMatchingPanel;

public class PropertyMatchingStep implements WizardStep
{
	PropertyMatchingPanel panel;
	
	@Override
	public String getCaption() {return Messages.getString("propertymatching");}

	@Override
	public Component getContent()
	{
		return (panel=new PropertyMatchingPanel());
	}

	@Override
	public boolean onAdvance()
	{
		return panel.isValid();
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
