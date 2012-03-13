package de.uni_leipzig.simba.saim;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.uni_leipzig.simba.saim.gui.widget.KBInfoDialog;

public class SAIMApplication extends Application {
	
	private final Window mainWindow;
	private Layout mainLayout;
	
	public SAIMApplication() {
		mainWindow = new Window("saim");		
		mainLayout = new HorizontalLayout();		
		mainWindow.setContent(mainLayout);
		
		//mainWindow.addComponent(form);
	}
	
	@Override
	public void init() {

		
		Label label = new Label("SAIM user interface");
		
		addButtons();
		
		mainLayout.addComponent(label);
				
		setMainWindow(mainWindow);		
	}
	
	public void addButtons() {
		Button openKBSourceDialoge = new Button("Configure source");
		openKBSourceDialoge.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				KBInfoDialog kbI = new KBInfoDialog(mainWindow, "Configure Source endpoint");
				mainWindow.addWindow(kbI);
			}
		});
		
		Button openKBTargetDialoge = new Button("Configure source");
		openKBTargetDialoge.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				KBInfoDialog kbI = new KBInfoDialog(mainWindow, "Configure Target endpoint");
				mainWindow.addWindow(kbI);
			}
		});
		mainLayout.addComponent(openKBSourceDialoge);
		mainLayout.addComponent(openKBTargetDialoge);
	}

}
