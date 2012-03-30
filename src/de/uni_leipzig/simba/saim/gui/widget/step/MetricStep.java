package de.uni_leipzig.simba.saim.gui.widget.step;

import java.io.Serializable;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.saim.Messages;
import org.vaadin.teemu.wizards.WizardStep;
import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.MetricPanel;

public class MetricStep implements WizardStep, Serializable
{

	@Override
	public String getCaption() {return Messages.getString("configuremetric");}

	@Override
	public Component getContent()
	{
		return new MetricPanel();
	}

	@Override
	public boolean onAdvance()
	{
		System.out.println(Configuration.getInstance().toString());
		Configuration config = Configuration.getInstance();
		HybridCache hC = HybridCache.getData(config.getSource());
		HybridCache tC = HybridCache.getData(config.getTarget());
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
