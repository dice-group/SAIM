package de.uni_leipzig.simba.saim.gui.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Pair;
import de.uni_leipzig.simba.saim.gui.widget.ClassChooser.ClassNode;

/** The class matching consists of two sets of classes from both knowledge bases and a match between them.
 * In RAVEN, the mapping is injective and uniquely defined (there is at most one match for a class).
 * The user gets shown some classes and can add some manually or create the matching manually.*/
public class ClassMatchingForm extends Form
{	
	protected final ComboBox field; 
//	protected final TextField target; 
	protected final ClassChooser chooser;
	
	public ClassMatchingForm(String caption, KBInfo info) {
		Layout layout = new VerticalLayout();
		layout.setWidth("100%");
		setLayout(layout);
		field = new ComboBox(caption);
		field.setWidth("100%");
		chooser = new ClassChooser(info.endpoint, info.id, info.graph);
		
		chooser.tree.addListener(new ItemClickListener() {	
			@Override
			public void itemClick(ItemClickEvent event) {
				field.setValue(event.getItemId());
				ClassNode node = (ClassNode) event.getItemId();
				field.addItem(node.url);
				field.setValue(node.url);//				
			}
		});
		addField("textfield", field);
		this.getLayout().addComponent(chooser);
	}
}
