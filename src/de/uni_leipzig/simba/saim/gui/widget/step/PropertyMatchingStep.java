package de.uni_leipzig.simba.saim.gui.widget.step;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.panel.PropertyMatchingPanel;

public class PropertyMatchingStep implements WizardStep
{
	private final Messages messages;	

	PropertyMatchingPanel panel;
	Window sub = null;
	public PropertyMatchingStep(final Messages messages) {this.messages=messages;}		
	public PropertyMatchingStep(Window sub,final Messages messages)
	{		
		this(messages);
		this.sub = sub;
	}
	@Override
	public String getCaption() {return messages.getString("propertymatching");}

	@Override
	public Component getContent()
	{
		return (panel=new PropertyMatchingPanel(messages));
	}

	@Override
	public boolean onAdvance()
	{
		if(panel.isValid()) {
			panel.submit();
			if(sub != null) {
				SAIMApplication.getInstance().getMainWindow().removeWindow(sub);
				((SAIMApplication) SAIMApplication.getInstance()).refresh();
			}
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
