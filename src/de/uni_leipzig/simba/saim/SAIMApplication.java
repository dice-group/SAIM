package de.uni_leipzig.simba.saim;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class SAIMApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("SAIM");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
