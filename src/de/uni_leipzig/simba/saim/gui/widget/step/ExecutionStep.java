package de.uni_leipzig.simba.saim.gui.widget.step;

import java.io.Serializable;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.ExecutionPanel;

public class ExecutionStep implements WizardStep, Serializable{
	Panel executionPanel;
	@Override
	public String getCaption() {
		return "Execution";	
	}

	@Override
	public Component getContent() {
		/** Need a window to ask user about next step  **/
		Window sub = new Window("Decide upon next step");
		ListSelect selector = new ListSelect("Select next step:");
		selector.addItem("Execute current Link Specification.");
		selector.addItem("Learn Link Specification.");
		selector.setNullSelectionAllowed(false);
		sub.addComponent(selector);
		sub.setModal(true);
		SAIMApplication.getInstance().getMainWindow().addWindow(sub);
		selector.addListener(new ValueChangeListener() {			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object o = event.getProperty();
				System.out.println(o);
			}
		});		
		// TODO Auto-generated method stub
		return executionPanel = new ExecutionPanel();
	}

	@Override
	public boolean onAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}
}