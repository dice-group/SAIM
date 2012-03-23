package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;
import com.vaadin.ui.Component;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.ClassMatchingPanel;

public class ClassMatchingStep implements WizardStep
{

	ClassMatchingPanel panel;
	@Override
	public String getCaption() {return Messages.getString("classmatching");}

	@Override
	public Component getContent()
	{
		return  panel = new ClassMatchingPanel();
	}

	@Override
	public boolean onAdvance()
	{
		if(panel.sourceClassForm.isValid() && panel.targetClassForm.isValid()) {
			Configuration config = Configuration.getInstance();
			KBInfo source = config.getSource();
			KBInfo target = config.getTarget();
			String restr = source.var+" rdf:type ";
			source.restrictions.add(restr+(String) panel.sourceClassForm.getValue());
			restr = target.var+" rdf:type ";
			target.restrictions.add(restr+(String) panel.targetClassForm.getValue());
			return true;
		}		
		return false;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
