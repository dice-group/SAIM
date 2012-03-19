package de.uni_leipzig.simba.saim.gui.widget;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.uni_leipzig.simba.saim.core.Pair;

/** The class matching consists of two sets of classes from both knowledge bases and a match between them.
 * In RAVEN, the mapping is injective and uniquely defined (there is at most one match for a class).
 * The user gets shown some classes and can add some manually or create the matching manually.*/
public class ClassMatchingForm extends Form
{	
	protected final TextField source; 
	protected final TextField target; 
	
	
	public ClassMatchingForm(boolean displayCaption) {
		Layout layout = new HorizontalLayout();
		layout.setWidth("100%");
		setLayout(layout);
		if(displayCaption) {
			source = new TextField("Source Class");
			target = new TextField("Target class");
		}
		else {
			source = new TextField();
			target = new TextField();
		}
		addField("source", source);
		addField("target", target);		
	}
	
//	public void addFieldPair() {
//		Pair<TextField> newPair = new Pair<>(new TextField("Source class"), new TextField("Target class"));
//		classMatchings.add(newPair);
//		
////		Panel matchPanel = new Panel();
////		matchPanel.setContent(new HorizontalLayout());
//		addField("src"+(classMatchings.size()-1), newPair.a);
//		addField("dest"+(classMatchings.size()-1), newPair.b);		
//		
//		newPair.a.addListener(new Property.ValueChangeListener() {
//		    public void valueChange(ValueChangeEvent event) {		    	
//		    	TextField tf = (TextField) event.getComponent();
//		    	
//		        String value = (String) tf.getValue();
//
//		        // Do something with the value
//		        getWindow().showNotification("Value is:", value);
//		    }
//		});
//	}
}
