package de.uni_leipzig.simba.saim;

import org.vaadin.teemu.wizards.Wizard;

import com.vaadin.Application;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.gui.widget.EndpointPanel;
import de.uni_leipzig.simba.saim.gui.widget.step.ClassMatchingStep;
import de.uni_leipzig.simba.saim.gui.widget.step.EndpointStep;

public class SAIMApplication extends Application {

	private final Window mainWindow;
	private AbsoluteLayout mainLayout;
	private GridLayout gridLayout;

	public SAIMApplication() {
		mainWindow = new Window("SAIM \u2013 Semi Automatic Instance Matcher");	
		mainLayout = buildMainLayout();
		mainWindow.setContent(mainLayout);
		Wizard wizard = new Wizard();
		wizard.addStep(new EndpointStep());
		wizard.addStep(new ClassMatchingStep());
		
		mainLayout.addComponent(wizard);
		setTheme("saim");
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
//		Label label = new Label("SAIM user interface");
//		Label label2 = new Label("GridPos(0, 2)");
//		gridLayout.addComponent(label, 0, 0);
//		gridLayout.addComponent(label2, 0, 2);

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
			
				VerticalLayout vert = new VerticalLayout();
			
				gridLayout.removeComponent(0,  2);
				gridLayout.addComponent(vert, 0, 2);
				vert.addComponent(new EndpointPanel());
			}
		});


		HorizontalLayout hor = new HorizontalLayout();
		hor.addComponent(openEndpointDialoge);
		gridLayout.addComponent(hor, 0, 1);
	}

}
