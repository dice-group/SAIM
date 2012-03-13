package de.uni_leipzig.simba.saim;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.uni_leipzig.simba.saim.gui.widget.KBInfoForm;

public class SAIMApplication extends Application {
	
	private final Window mainWindow;
	private AbsoluteLayout mainLayout;
	private GridLayout gridLayout;
	
	
	
	public SAIMApplication() {
		mainWindow = new Window("saim");		
		mainLayout = buildMainLayout();
		mainWindow.setContent(mainLayout);
	}
	
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// gridLayout
		gridLayout = new GridLayout();
		gridLayout.setImmediate(false);
		gridLayout.setWidth("100%");
		gridLayout.setHeight("100%");
		gridLayout.setMargin(false);		
		gridLayout.setRows(3);
		
		mainLayout.addComponent(gridLayout);
		// filling Layouts
		Label label = new Label("SAIM user interface");
		Label label2 = new Label("GridPos(0, 2)");
		gridLayout.addComponent(label, 0, 0);
		gridLayout.addComponent(label2, 0, 2);
	
		return mainLayout;
	}
	
	@Override
	public void init() {		
		addButtons();				
		setMainWindow(mainWindow);		
	}
	
	public void addButtons() {
		Button openEndpointDialoge = new Button("Configure endpoints");
		openEndpointDialoge.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				KBInfoForm kbISource = new KBInfoForm(mainWindow, "Configure Source endpoint");
				KBInfoForm kbITarget = new KBInfoForm(mainWindow, "Configure Target endpoint");
				VerticalLayout vert = new VerticalLayout();
				vert.addComponent(kbISource);
				vert.addComponent(kbITarget);
				gridLayout.removeComponent(0,  2);
				gridLayout.addComponent(vert, 0, 2);				
				}
		});
		
		
		HorizontalLayout hor = new HorizontalLayout();
		hor.addComponent(openEndpointDialoge);
		gridLayout.addComponent(hor, 0, 1);
	}

}
