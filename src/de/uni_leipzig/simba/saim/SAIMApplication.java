package de.uni_leipzig.simba.saim;

import org.vaadin.teemu.wizards.Wizard;

import com.vaadin.Application;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;
import de.uni_leipzig.simba.saim.gui.widget.step.PropertyMatchingStep;

public class SAIMApplication extends Application {

	private final Window mainWindow;
	private Layout mainLayout;
	private GridLayout gridLayout;

	public SAIMApplication()
	{
		mainWindow = new Window("SAIM \u2013 Semi Automatic Instance Matcher");	
		mainLayout = buildMainLayout();
		mainWindow.setContent(mainLayout);
		mainWindow.addComponent(buildMainMenu());

		Wizard wizard = new Wizard();
		
		wizard.addStep(new EndpointStep());
		wizard.addStep(new ClassMatchingStep());
		wizard.addStep(new PropertyMatchingStep());
		mainLayout.addComponent(wizard);
		

		setTheme("saim");

	}

	protected MenuBar buildMainMenu()
	{
		final MenuBar menuBar = new MenuBar();
		menuBar.setWidth("100%");
		//menuBar.setStyleName("margin1em");
		
		MenuItem fileMenu = menuBar.addItem("File", null, null);
		fileMenu.addItem("Open", null, null).setEnabled(false);
		fileMenu.addItem("Save", null, null).setEnabled(false);
		fileMenu.addItem("Import LIMES linkspec", null, null).setEnabled(false);
		
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

}
