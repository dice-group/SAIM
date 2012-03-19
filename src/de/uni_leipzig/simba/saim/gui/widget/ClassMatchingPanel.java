package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.DragAndDropWrapper;
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
		addForm(true);
		this.getContent().addComponent(new ClassChooser(config.getSource().endpoint, config.getSource().id, config.getSource().graph));
	}
	
	public void addForm(boolean addCaption) {
		ClassMatchingForm form = new ClassMatchingForm(addCaption);
		form.setWidth("100%");
		this.addComponent(form);
		final TextField tf = new TextField("TESTER");
		DragAndDropWrapper tfWrap = new DragAndDropWrapper(tf);
		tfWrap.setDropHandler(new DropHandler() {
			
			@Override
			public AcceptCriterion getAcceptCriterion() {
				return AcceptAll.get();
			}
			
			@Override
			public void drop(DragAndDropEvent event) {
				event.getTransferable().getSourceComponent();
				tf.setValue("HALLO");
			}
		});
		addComponent(tfWrap);
	}
}
