package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.panel.PropertyMatchingPanel;

public class PropertyMatchingStep implements WizardStep
{
	PropertyMatchingPanel panel;
	Window sub = null;
	public PropertyMatchingStep() {
		
	}
	public PropertyMatchingStep(Window sub) {
		this.sub = sub;
	}
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
		
		if(sub != null) {
			SAIMApplication.getInstance().getMainWindow().removeWindow(sub);
			((SAIMApplication) SAIMApplication.getInstance()).refresh();
		}
		return panel.isValid();
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
