package de.uni_leipzig.simba.saim;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.spi.LoggerFactory;

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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.ConfigUploader;
import de.uni_leipzig.simba.saim.gui.widget.panel.StartPanel;
import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.DevelopMetricStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
import de.uni_leipzig.simba.saim.gui.widget.step.ExecutionStep;
import de.uni_leipzig.simba.saim.gui.widget.step.MetricStep;

public class SAIMApplication extends Application
{
	private static final long	serialVersionUID	= -7665596682464881860L;
	private static SAIMApplication application = null; 
	private final Window mainWindow;
	private Layout mainLayout;
//	private GridLayout gridLayout;
	private Wizard wizard;
	Window sub;
	static Logger logger;
	public static Application getInstance() {return application;}
	
	public SAIMApplication()
	{		
		BasicConfigurator.configure();
		logger = Logger.getLogger("SAIM");
		
		application=this;
		mainWindow = new Window(Messages.getString("title")); //$NON-NLS-1$
		mainLayout = buildMainLayout();
		mainWindow.setContent(mainLayout);
		mainWindow.addComponent(buildMainMenu());

		mainLayout.addComponent(new StartPanel());
		wizard = new Wizard();

//		wizardDevelopment();
		wizardFull();

		mainLayout.addComponent(wizard);
		setTheme("saim"); //$NON-NLS-1$
	}
	
	protected void wizardDevelopment()
	{
		wizard.addStep(new EndpointStep());
//		HashMap<String,KBInfo> endpoints = DefaultEndpointLoader.getDefaultEndpoints();
//		KBInfo info_s = endpoints.get("lgd.aksw - Drugbank");
//		KBInfo info_t = endpoints.get("lgd.aksw - Sider");
//		info_s.var = "?src";
//		info_t.var = "?dest";
//		info_s.type = "SPARQL";
//		info_t.type = "SPARQL";
//		Configuration.getInstance().setSourceEndpoint(info_s);
//		Configuration.getInstance().setTargetEndpoint(info_t);

		wizard.addStep(new ClassMatchingStep());
//		wizard.addStep(new MetricStep());
//		wizard.addStep(new ActiveLearningStep());
		wizard.addStep(new DevelopMetricStep());
	}
	
	protected void wizardFull()
	{
		wizard.addStep(new EndpointStep());		
		wizard.addStep(new ClassMatchingStep());
		wizard.addStep(new MetricStep());
//		wizard.addStep(new ActiveLearningStep());
		wizard.addStep(new ExecutionStep());
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
		
		MenuItem controlMenu= menuBar.addItem(Messages.getString("SAIMApplication.menurestart"), null, new Command() {			 //$NON-NLS-1$
			@Override
			public void menuSelected(MenuItem selectedItem) {
				((SAIMApplication)SAIMApplication.getInstance()).returnToBegin();
			}
		});
		
		return menuBar;
	}

	protected Layout buildMainLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		
		// common part: create layout
//		mainLayout = new AbsoluteLayout();
//		mainLayout.setWidth("100%");
//		mainLayout.setHeight("100%");
//		mainLayout.setMargin(false);
//
//		// gridLayout
//		gridLayout = new GridLayout();
//		gridLayout.setImmediate(false);
//		gridLayout.setWidth("100%");
//		gridLayout.setHeight("100%");
//		gridLayout.setMargin(false);		
//		gridLayout.setRows(3);
//
//		mainLayout.addComponent(gridLayout);
//		// filling Layouts
//		//		Label label = new Label("SAIM user interface");
//		//		Label label2 = new Label("GridPos(0, 2)");
//		//		gridLayout.addComponent(label, 0, 0);
//		//		gridLayout.addComponent(label2, 0, 2);
//
		return layout;
	}

	@Override
	public void init() {		
		//addButtons();				
		setMainWindow(mainWindow);		
	}

//	public void addButtons() {
//		Button openEndpointDialoge = new Button("Configure endpoints");
//		openEndpointDialoge.addListener(new ClickListener() {			
//			@Override
//			public void buttonClick(ClickEvent event) {
//
//				VerticalLayout vert = new VerticalLayout();
//
//				gridLayout.removeComponent(0,  2);
//				gridLayout.addComponent(vert, 0, 2);
//				vert.addComponent(new EndpointPanel());
//			}
//		});
//
//
//		HorizontalLayout hor = new HorizontalLayout();
//		hor.addComponent(openEndpointDialoge);
//		gridLayout.addComponent(hor, 0, 1);
//	}
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
	    	Configuration.getInstance().saveToXML("linkspec.xml"); //$NON-NLS-1$
	    	
	    	sub.addComponent(new Link(Messages.getString("SAIMApplication.menudownloadlinkspec"),new FileResource(new File("linkspec.xml"),SAIMApplication.this))); //$NON-NLS-1$ //$NON-NLS-2$
	    	getMainWindow().addWindow(sub);
	    }  
	};
	/**
	 * Show a component instead of the wizard. Finishes the Wizard.
	 * @param c
	 */
	public void showComponent(Component c) {
		mainWindow.removeWindow(sub);
		wizard.finish();
		mainWindow.removeAllComponents();
		mainWindow.addComponent(buildMainMenu());
		mainWindow.addComponent(c);
//		mainWindow.addComponent(new ExecutionPanel());
	}
	/**
	 * Return view to the beginning: showing wizard.
	 */
	public void returnToBegin() {
		mainWindow.removeAllComponents();
		mainWindow.addComponent(buildMainMenu());

		
		mainLayout.addComponent(new StartPanel());
		mainLayout.addComponent(wizard);

	}

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
}
