package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;
import com.vaadin.ui.Component;
import de.uni_leipzig.simba.saim.gui.widget.ClassMatchingPanel;
import de.uni_leipzig.simba.saim.gui.widget.MetricPanel;

public class MetricStep implements WizardStep
{

	@Override
	public String getCaption() {return "Configure Metric";}

	@Override
	public Component getContent()
	{
		return new MetricPanel();
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
