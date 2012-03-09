package de.uni_leipzig.simba.saim.gui;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.Application;
import com.vaadin.ui.*;

import de.uni_leipzig.simba.saim.gui.widget.KBInfoDialog;

public class SaimApplication extends Application {
	
	private final Window mainWindow;
	private Layout mainLayout;
	
	public SaimApplication() {
		mainWindow = new Window("saim");		
		mainLayout = new HorizontalLayout();		
		mainWindow.setContent(mainLayout);
	}
	
	@Override
	public void init() {

		
		Label label = new Label("SAIM user interface");
		
		
		
		mainLayout.addComponent(label);
		
		KBInfoDialog kbI = new KBInfoDialog(mainWindow, "Configure Source endpoint");
		mainWindow.addWindow(kbI);
				
		setMainWindow(mainWindow);
		
	}

}
