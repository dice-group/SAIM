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