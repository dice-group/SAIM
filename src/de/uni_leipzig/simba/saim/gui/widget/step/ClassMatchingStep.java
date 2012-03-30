package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;
import com.vaadin.ui.Component;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
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
		if(panel.sourceClassForm.isValid() && panel.targetClassForm.isValid())
		{
			Configuration config = Configuration.getInstance();
			KBInfo source = config.getSource();
			KBInfo target = config.getTarget();
			source.restrictions.clear();
			target.restrictions.clear();
			//source
			source.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
			String restr = source.var+" rdf:type ";		
			String value = SPARQLHelper.wrapIfNecessary(PrefixHelper.expand(panel.sourceClassForm.getField("textfield").getValue().toString()));
			source.restrictions.add(restr + value);
			//target
			restr = target.var+" rdf:type ";
			value = SPARQLHelper.wrapIfNecessary(PrefixHelper.expand(panel.targetClassForm.getField("textfield").getValue().toString()));
			target.restrictions.add(restr + value);
			target.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
			panel.close();
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