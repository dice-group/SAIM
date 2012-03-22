package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.EndpointPanel;
import de.uni_leipzig.simba.saim.Messages;
public class EndpointStep implements WizardStep
{
	EndpointPanel panel = new EndpointPanel();

	@Override
	public String getCaption() {return Messages.getString("selectsparqlendpoints");}

	@Override
	public Component getContent()
	{
		return panel;
	}

	@Override
	public boolean onAdvance()
	{
		if(panel.kbISource.isValid() && panel.kbITarget.isValid())
		{
			// Proceed
			Configuration config = Configuration.getInstance();
			KBInfo source = panel.kbISource.getKBInfo();
			source.var = "?src";
			KBInfo target = panel.kbITarget.getKBInfo();
			target.var = "?dest";
			config.setSourceEndpoint(source);
			config.setTargetEndpoint(target);
			//panel.getWindow().showNotification(source + "\n\n" + target);
			return true;
		}
		return false;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

}
