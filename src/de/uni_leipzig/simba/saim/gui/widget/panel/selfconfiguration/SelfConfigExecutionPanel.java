package de.uni_leipzig.simba.saim.gui.widget.panel.selfconfiguration;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.panel.PerformPanel; 

/**
 * Implements the idea of a generic Execution window for SelfConfigurators.
 * @author Lyko
 *
 */
public abstract class SelfConfigExecutionPanel extends PerformPanel{
	private static final long serialVersionUID = 1L;
	SAIMApplication application;
	final Messages messages;
	Layout mainLayout;
	Configuration config;
	//to show progress
	Layout indicatorLayout;
	final ProgressIndicator indicator = new ProgressIndicator();
	final Panel stepPanel = new Panel();
	Panel resultPanel;
	//buttons
	Button start;
	Button close;
	Button showMapping;
	
	public SelfConfigExecutionPanel(SAIMApplication application, final Messages messages) {
		this.application = application;
		this.messages = messages;
	}
	
	/**
	 * Should be called by extending classes on attachements. 
	 * Builds the component (mainLayout) by calling the abstract methods to create
	 * child specific components and adding them to the layout.
	 */
	public void init() {
		mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		// description
		mainLayout.addComponent(getDescriptionComponent());
		// form
		mainLayout.addComponent(getConfigPanel());
		// progress
		Refresher refresher = new Refresher();
		SelfConfigRefreshListener listener = new SelfConfigRefreshListener();
		refresher.addListener(listener);
		addComponent(refresher);
		indicatorLayout = new VerticalLayout();		
		indicator.setCaption("Current action"); 
		mainLayout.addComponent(indicator);
		indicator.setImmediate(true);
		indicator.setVisible(false);

		mainLayout.addComponent(stepPanel);
		stepPanel.setVisible(false);
		

		// results
		mainLayout.addComponent(getPerformPanel());		
		// buttons
		mainLayout.addComponent(getButtonComponent());
	}
	
	/**
	 * Called by the start button implements and runs the specific selfconfiguration
	 * method.
	 */
	protected abstract void performSelfConfiguration();
	
	/**
	 * Creates Component (most often some kind of Form) to configure the specific self
	 * configuration method.
	 * @return Component to change parameters of the specific self configuration method.
	 */
	protected abstract Component getConfigPanel();
	/**
	 * Creates Component in the middle, just top of the controlling buttons
	 * @return Component used by the self configuration method to (intermediate) show results.
	 */
	protected abstract Component getPerformPanel();

	/**
	 * Creates the description of the component. Should hold an label describing the specific selconfiguration
	 * method provided by the component.
	 * @return Component describing the method (e.g. a Label).
	 */
	protected abstract Component getDescriptionComponent();
	
	/**
	 * Creates buttons to start and close the windows.
	 * @return A Component (a HorizontalLayout) with all Buttons.
	 */
	private Component getButtonComponent() {
		start = new Button("Start learning");
		start.addListener(new ClickListener() {
			private static final long serialVersionUID = 5899998766641774597L;
			@Override
			public void buttonClick(ClickEvent event) {
				mainLayout.removeComponent(start);
				indicator.setVisible(true);
				stepPanel.setVisible(true);
				performSelfConfiguration();
			}
		});
		close = new Button("Close");
		close.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 288995511804810376L;

			@Override
			public void buttonClick(ClickEvent event) {
				onClose();
				//@FIXME not best practice here!
				application.getMainWindow().removeWindow(getWindow());
			}
		});
		close.setEnabled(false);
		
		showMapping = new Button("Show Mapping");
		showMapping.setEnabled(false);
		
		HorizontalLayout finishButtonsLayout = new HorizontalLayout();
		finishButtonsLayout.addComponent(start);
		finishButtonsLayout.addComponent(showMapping);
		finishButtonsLayout.addComponent(close);
		return finishButtonsLayout;
	}
	
	
	/**To enable refreshing while multithreading*/
	public class SelfConfigRefreshListener implements RefreshListener  {
		boolean running = true; 
		private static final long serialVersionUID = -8765221895426102605L;		    
		@Override 
		public void refresh(final Refresher source)	{
			if(!running) {
				removeComponent(source);
				source.setEnabled(false);
			}
		}
	}

}
