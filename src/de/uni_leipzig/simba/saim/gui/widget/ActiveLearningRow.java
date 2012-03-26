package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.saim.Messages;

public class ActiveLearningRow extends Panel
{	
	public ActiveLearningRow(final String uri1,final String uri2)
	{
		String x = "hello world";
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		this.setContent(layout);
		Label uri1Label = new Label(uri1);
		Label uri2Label = new Label(uri2);
		Layout buttonLayout = new HorizontalLayout();
		
		Button sameButton = new Button("=");		
		sameButton.setStyleName("valid");
		Button unsureButton = new Button("?");
		sameButton.setStyleName("invalid");
		Button differentButton = new Button("!=");
		sameButton.setStyleName("unsure");
		
		Button[] buttons = {sameButton,differentButton,unsureButton};
		for(Button button: buttons)
		{
			buttonLayout.addComponent(button);
		}
				
		addComponent(uri1Label);
		addComponent(buttonLayout);
		addComponent(uri2Label);	
	}

}