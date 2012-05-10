package de.uni_leipzig.simba.saim.gui.widget.window;

import org.vaadin.teemu.wizards.Wizard;

import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
import de.uni_leipzig.simba.saim.gui.widget.step.PropertyMatchingStep;

public class EndpointWindow extends Window{
	
	Layout mainLayout;
	Wizard wizard;
	
	
	public EndpointWindow() {
		init();
		this.setSizeFull();
		this.addListener(new EndpointWindowCloseListener());
	}
	
	private void init() {
		mainLayout = new VerticalLayout();
		this.addComponent(mainLayout);
		wizard = new Wizard();
		wizardFull();
		
		mainLayout.addComponent(wizard);
		setTheme("saim"); //$NON-NLS-1$
	}
	
	private void wizardFull() {
		wizard.addStep(new EndpointStep());		
		wizard.addStep(new ClassMatchingStep());
		wizard.addStep(new PropertyMatchingStep(this));
	}
	
	
	public class EndpointWindowCloseListener implements CloseListener {

		@Override
		public void windowClose(CloseEvent e) {
			// do we have to react on this???
		}
		
	}
}
