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
		gridLayout.setWidth("320px");
		gridLayout.setHeight("240px");
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
		Button openKBSourceDialoge = new Button("Configure source");
		openKBSourceDialoge.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				KBInfoForm kbI = new KBInfoForm(mainWindow, "Configure Source endpoint");
				gridLayout.removeComponent(0,  2);
				gridLayout.addComponent(kbI, 0, 2);				
				}
		});
		
		Button openKBTargetDialoge = new Button("Configure target");
		openKBTargetDialoge.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				KBInfoForm kbI = new KBInfoForm(mainWindow, "Configure Target endpoint");
				gridLayout.removeComponent(0,  2);
				gridLayout.addComponent(kbI, 0, 2);	
			}
		});
		HorizontalLayout hor = new HorizontalLayout();
		hor.addComponent(openKBSourceDialoge);
		hor.addComponent(openKBTargetDialoge);
		gridLayout.addComponent(hor, 0, 1);
	}

}
