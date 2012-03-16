package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class ClassMatchingPanel extends Panel
{	
	public ClassMatchingPanel()
	{
		setContent(new VerticalLayout());
		for(int i = 0; i<5;i++)
			addForm(i==0);
		this.getContent().addComponent(new ClassChooser("http://dbpedia.org/sparql","http://dbpedia.org"));
	}
	
	public void addForm(boolean addCaption) {
		ClassMatchingForm form = new ClassMatchingForm(addCaption);
		form.setWidth("100%");
		this.addComponent(form);
	}
}
