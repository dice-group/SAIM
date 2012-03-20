package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.saim.core.Configuration;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class ClassMatchingPanel extends Panel
{	
	Configuration config = Configuration.getInstance();
	
	public ClassMatchingPanel()
	{
		setContent(new VerticalLayout());
		HorizontalLayout hori = new HorizontalLayout();
		hori.setWidth("100%");
		hori.addComponent(new ClassMatchingForm("Source Class", config.getSource()));
		hori.addComponent(new ClassMatchingForm("Target Class", config.getTarget()));
		this.getContent().addComponent(hori);
	}	
}
