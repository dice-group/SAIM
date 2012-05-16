package de.uni_leipzig.simba.saim;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.ConfigUploader;
import de.uni_leipzig.simba.saim.gui.widget.panel.MetricPanel;
import de.uni_leipzig.simba.saim.gui.widget.panel.StartPanel;
import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.DevelopMetricStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
import de.uni_leipzig.simba.saim.gui.widget.step.ExecutionStep;
import de.uni_leipzig.simba.saim.gui.widget.step.MetricStep;
import de.uni_leipzig.simba.saim.gui.widget.step.PropertyMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.window.EndpointWindow;

@SuppressWarnings("serial")
public class SAIMApplication extends Application
{
	private static final long	serialVersionUID	= -7665596682464881860L;
	private static SAIMApplication application = null; 
	private final Window mainWindow;
	private Layout mainLayout;
	private Wizard wizard;
	Window sub;
	Configuration config = new Configuration();
	Panel content;
	
	static final Logger logger = LoggerFactory.getLogger(SAIMApplication.class);
	public static Application getInstance() {return application;}
	
	public SAIMApplication()
	{		
		application=this;
		
		mainWindow = new Window(Messages.getString("title")); //$NON-NLS-1$
		mainLayout = buildMainLayout();
		mainWindow.setContent(mainLayout);
		mainWindow.addComponent(buildMainMenu());
		content = new MetricPanel();
		mainLayout.addComponent(content);
		wizard = new Wizard();	
		setTheme("saim"); 
	}


	protected MenuBar buildMainMenu()
	{
		final MenuBar menuBar = new MenuBar();
		menuBar.setWidth("100%"); //$NON-NLS-1$
		//menuBar.setStyleName("margin1em");
		
		MenuItem fileMenu = menuBar.addItem(Messages.getString("file"), null, null); //$NON-NLS-1$
		fileMenu.addItem(Messages.getString("open"), null, null).setEnabled(false); //$NON-NLS-1$
		fileMenu.addItem(Messages.getString("save"), null, null).setEnabled(false); //$NON-NLS-1$
		
		fileMenu.addItem(Messages.getString("importlimes"), null, importLIMESCommand).setEnabled(true);		 //$NON-NLS-1$
		fileMenu.addItem(Messages.getString("exportlimes"), null, exportLIMESCommand).setEnabled(true); //$NON-NLS-1$
		
		MenuItem startMenu = menuBar.addItem(Messages.getString("startnewconfig"), null, new StartCommand(this));
		
		return menuBar;
	}

	protected Layout buildMainLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		return layout;
	}

	@Override
	public void init() {		
		setMainWindow(mainWindow);		
	}

	MenuBar.Command importLIMESCommand = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	sub = new Window(Messages.getString("limesupload")); //$NON-NLS-1$
	    	sub.setWidth("700px"); //$NON-NLS-1$
	    	sub.setModal(true);
	    	final ConfigUploader upload = new ConfigUploader();
	    	sub.addComponent(upload);
	    	Button ok = new Button("ok"); //$NON-NLS-1$
	    	sub.addComponent(ok);
	    	ok.addListener(new ClickListener() {				
				@Override
				public void buttonClick(ClickEvent event) {
					getMainWindow().removeWindow(sub);
				}
			});
	    	getMainWindow().addWindow(sub);
	    }  
	};

	
	MenuBar.Command exportLIMESCommand = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	sub = new Window(Messages.getString("limesdownload")); //$NON-NLS-1$
	    	sub.setWidth("700px"); //$NON-NLS-1$
	    	sub.setModal(true);
	    	config.saveToXML("linkspec.xml"); //$NON-NLS-1$
	    	
	    	sub.addComponent(new Link(Messages.getString("SAIMApplication.menudownloadlinkspec"),new FileResource(new File("linkspec.xml"),SAIMApplication.this))); //$NON-NLS-1$ //$NON-NLS-2$
	    	getMainWindow().addWindow(sub);
	    }  
	};
	public Configuration getConfig() {
		if(config == null)
			config = new Configuration();
		return config;
	}

	/**
	 * Show a component instead of the wizard. Finishes the Wizard.
	 * @param c
	 * @deprecated
	 */
	public void showComponent(Component c) {
		mainWindow.removeWindow(sub);
		wizard.finish();
		mainWindow.removeAllComponents();
		mainWindow.addComponent(buildMainMenu());
		mainWindow.addComponent(c);
	}
	/**
	 * Return view to the beginning: showing wizard.
	 * @deprecated
	 */
	public void returnToBegin() {
		mainWindow.removeAllComponents();
		mainWindow.addComponent(buildMainMenu());

		
		mainLayout.addComponent(new StartPanel());
		mainLayout.addComponent(wizard);

	}

	/**
	 * @deprecated
	 * @param oldStep
	 * @param newStep
	 */
	public void setStep(WizardStep oldStep, WizardStep newStep) {
		List<WizardStep> olderSteps = new LinkedList<WizardStep>();
		boolean found = false;
		for(WizardStep s:wizard.getSteps()) {
			if(s == oldStep) {
				found = true;
				continue;
			}
			if(found) {
				olderSteps.add(s);
			}			
		}
		wizard.addStep(newStep);
		wizard.activateStep(newStep);
		for(WizardStep os : olderSteps) {
			wizard.removeStep(os);
		}
	}
	/**
	 * @deprecated
	 * @param oldStep
	 */
	public void removeOlderSteps(WizardStep oldStep) {
		List<WizardStep> olderSteps = new LinkedList<WizardStep>();
		boolean found = false;
		for(WizardStep s:wizard.getSteps()) {
			if(s == oldStep) {
				found = true;
				continue;
			}
			if(found) {
				olderSteps.add(s);
			}			
		}
		for(WizardStep os : olderSteps) {
			wizard.removeStep(os);
		}
	}
	
	/**
	 * Method is called if any action was taken in a subwindow that needs the main content to update.
	 */
	public void refresh() {
		mainLayout.removeComponent(content);
		//FIXME call refresh() method instead of constructing completely new?
		content = new MetricPanel();
		mainLayout.addComponent(content);
	}
	
	public class StartCommand implements Command {

		SAIMApplication app;
		public StartCommand(SAIMApplication app) {
			this.app=app;
		}
		
		@Override
		public void menuSelected(MenuItem selectedItem) {
			EndpointWindow endpointWindow = new EndpointWindow(app);
			endpointWindow.setModal(true);
			endpointWindow.setVisible(true);
			app.getMainWindow().addWindow(endpointWindow);
		}
		
	}
}
