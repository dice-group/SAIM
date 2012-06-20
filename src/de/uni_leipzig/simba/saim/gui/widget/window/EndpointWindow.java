package de.uni_leipzig.simba.saim.gui.widget.window;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
import de.uni_leipzig.simba.saim.gui.widget.step.PropertyMatchingStep;
/**
 * Window used to define the endpoints.
 * @author Lyko
 *
 */
public class EndpointWindow extends Window
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2991623255901308493L;
	Layout mainLayout;
	Wizard wizard;
	SAIMApplication app;
	
	public EndpointWindow(SAIMApplication app)
	{
		super(app.messages.getString("createnewconfiguration"));
		this.app = app;
		this.setSizeFull();
		wizard = new Wizard();
		wizard.addListener(new WizardProgressListener()
		{			
			@Override public void wizardCompleted(WizardCompletedEvent event){}
			@Override public void wizardCancelled(WizardCancelledEvent event)
			{
				EndpointWindow.this.close();
			}
			@Override public void stepSetChanged(WizardStepSetChangedEvent event){}
			@Override public void activeStepChanged(WizardStepActivationEvent event){}
		});
		wizardFull();
		this.addListener(new EndpointWindowCloseListener());
		init();
	}
	
	private void init()
	{
		mainLayout = new VerticalLayout();
		this.addComponent(mainLayout);
		mainLayout.addComponent(wizard);
		setTheme("saim"); 
	}
	
	private void wizardFull()
	{
		wizard.addStep(new EndpointStep(app));		
		wizard.addStep(new ClassMatchingStep(app));
		wizard.addStep(new PropertyMatchingStep(this,app.messages));
	}
	
	public class EndpointWindowCloseListener implements CloseListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7712910111369830002L;

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