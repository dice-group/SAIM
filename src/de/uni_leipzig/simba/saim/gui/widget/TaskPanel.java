package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;

public abstract class TaskPanel extends Panel
{
	protected Panel content = new Panel();
	
	public TaskPanel()
	{
		this.setContent(new VerticalLayout());
		this.addComponent(content);
		Button nextButton = new Button("Next >>");
		
		this.addComponent(nextButton);
		nextButton.addListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
			}
		});
	}

}