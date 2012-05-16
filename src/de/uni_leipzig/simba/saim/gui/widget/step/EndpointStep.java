package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.panel.EndpointPanel;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
public class EndpointStep implements WizardStep
{
	EndpointPanel panel;
	SAIMApplication app;
	public EndpointStep(SAIMApplication app) {
		this.app = app;
	}
	
	@Override
	public String getCaption() {return Messages.getString("selectsparqlendpoints");}

	@Override
	public Component getContent()
	{
		return panel = new EndpointPanel();
	}

	@Override
	public boolean onAdvance()
	{
		Configuration config = app.getConfig();
		if(config.isLocal)
			return true;
		else
			if(panel.kbISource.isValid() && panel.kbITarget.isValid())
			{
				// Proceed			
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
