package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.panel.ActiveLearningPanel;
/**
 * @deprecated
 * @author Lyko
 *
 */
public class ActiveLearningStep implements WizardStep
{

	@Override
	public String getCaption() {return Messages.getString("activelearning");}

	@Override
	public Component getContent()
	{
		return new ActiveLearningPanel(null);
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