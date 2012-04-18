package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;
import com.vaadin.ui.Component;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.panel.ClassMatchingPanel;

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
			String value1 = SPARQLHelper.wrapIfNecessary(PrefixHelper.expand(panel.sourceClassForm.getField("textfield").getValue().toString()));
			if(value1 != null && value1.length()>0)
				source.restrictions.add(restr + value1);
			//target
			restr = target.var+" rdf:type ";
			String value2 = SPARQLHelper.wrapIfNecessary(PrefixHelper.expand(panel.targetClassForm.getField("textfield").getValue().toString()));
			if(value2 != null && value2.length()>0)
				target.restrictions.add(restr + value2);
			target.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
			if(value1 != null && value2 !=  null && value1.length()>0 && value2.length()>0) {
				System.out.println("Added class restrictions:\nSource: "+source.restrictions.get(0)+" \nTarget: "+target.restrictions.get(0));
				panel.close();
				return true;
			}
		}		
		return false;
	}

	@Override
	public boolean onBack()
	{
		panel.close();
		return true;
	}

}