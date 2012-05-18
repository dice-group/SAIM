package de.uni_leipzig.simba.saim.gui.widget.step;

import java.io.Serializable;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.panel.MetricPanel;
/**
 * @deprecated
 * @author Lyko
 *
 */
public class MetricStep implements WizardStep, Serializable
{
	private final Messages messages;	
	public MetricStep(final Messages messages) {this.messages=messages;}
	
	MetricPanel metricPanel;
	@Override
	public String getCaption() {return messages.getString("configuremetric");}

	@Override
	public Component getContent()
	{
		return metricPanel = new MetricPanel(messages);
	}

	@Override
	public boolean onAdvance()
	{
//		System.out.println(Configuration.getInstance().toString());
		if(metricPanel.isValid())
			return true;
		else
			return false;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
