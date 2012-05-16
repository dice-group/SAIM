package de.uni_leipzig.simba.saim.gui.widget.window;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.gui.widget.panel.PerformPanel;
import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
import de.uni_leipzig.simba.saim.gui.widget.step.PropertyMatchingStep;
/**
 * Window used to define the endpoints.
 * @author Lyko
 *
 */
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
		setTheme("saim"); 
	}
	
	private void wizardFull() {
		wizard.addStep(new EndpointStep());		
		wizard.addStep(new ClassMatchingStep());
		wizard.addStep(new PropertyMatchingStep(this));
	}
	
	
	public class EndpointWindowCloseListener implements CloseListener {
		@Override
		public void windowClose(CloseEvent e) {
//			for(WizardStep s : wizard.getSteps()) {
//				if(wizard.isActive(s)) {
//					PerformPanel p = (PerformPanel) s.getContent();
//					p.onClose();
//				}
//			}
		}
		
	}
}
