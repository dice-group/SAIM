package de.uni_leipzig.simba.saim;


import java.util.HashMap;

import org.vaadin.teemu.wizards.Wizard;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.DefaultEndpointLoader;
import de.uni_leipzig.simba.saim.gui.widget.ConfigUploader;
import de.uni_leipzig.simba.saim.gui.widget.StartPanel;
import de.uni_leipzig.simba.saim.gui.widget.step.ActiveLearningStep;
import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
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
	
	public static Application getInstance() {return application;}
	
	public SAIMApplication()
	{
		application=this;
		mainWindow = new Window(Messages.getString("title"));	
		mainLayout = buildMainLayout();
		mainWindow.setContent(mainLayout);
		mainWindow.addComponent(buildMainMenu());

		mainLayout.addComponent(new StartPanel());
		wizard = new Wizard();

//		wizardDevelopment();
		wizardFull();

		mainLayout.addComponent(wizard);
		
		setTheme("saim");
	}
	
	protected void wizardDevelopment()
	{
		wizard.addStep(new EndpointStep());
		HashMap<String,KBInfo> endpoints = DefaultEndpointLoader.getDefaultEndpoints();
		KBInfo info_s = endpoints.get("lgd.aksw - Drugbank");
		KBInfo info_t = endpoints.get("lgd.aksw - Sider");
		info_s.var = "?src";
		info_t.var = "?dest";
		info_s.type = "SPARQL";
		info_t.type = "SPARQL";
		Configuration.getInstance().setSourceEndpoint(info_s);
		Configuration.getInstance().setTargetEndpoint(info_t);
		wizard.addStep(new ClassMatchingStep());
		wizard.addStep(new MetricStep());
		wizard.addStep(new ActiveLearningStep());
	}
	
	protected void wizardFull()
	{
		wizard.addStep(new EndpointStep());		
		wizard.addStep(new ClassMatchingStep());
		wizard.addStep(new MetricStep());
		wizard.addStep(new ActiveLearningStep());
	}

	protected MenuBar buildMainMenu()
	{
		final MenuBar menuBar = new MenuBar();
		menuBar.setWidth("100%");
		//menuBar.setStyleName("margin1em");
		
		MenuItem fileMenu = menuBar.addItem(Messages.getString("file"), null, null);
		fileMenu.addItem(Messages.getString("open"), null, null).setEnabled(false);
		fileMenu.addItem(Messages.getString("save"), null, null).setEnabled(false);
		fileMenu.addItem(Messages.getString("importlimes"), null, uploadConfigCommand).setEnabled(true);		
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
	MenuBar.Command uploadConfigCommand = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	sub = new Window(Messages.getString("limesupload"));
	    	sub.setWidth("700px");
	    	sub.setModal(true);
	    	final ConfigUploader upload = new ConfigUploader();
	    	sub.addComponent(upload);
	    	getMainWindow().addWindow(sub);
	    }  
	};
	
	public void showComponent(Component c) {
		mainWindow.removeWindow(sub);
		wizard.finish();
		mainWindow.removeAllComponents();
		mainWindow.addComponent(buildMainMenu());
		mainWindow.addComponent(c);
//		mainWindow.addComponent(new ExecutionPanel());
	}
}
