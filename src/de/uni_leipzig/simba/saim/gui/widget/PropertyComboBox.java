package de.uni_leipzig.simba.saim.gui.widget;

import java.util.List;

import com.vaadin.ui.ComboBox;

import de.uni_leipzig.simba.saim.Messages;

public class PropertyComboBox extends ComboBox
{
	//public PropertyComboBox(String title) {super(title);this();}
	public PropertyComboBox(List<String> properties)
	{
		super();
		//this.setWidth("100%");
//		addItem("test");
//		setNullSelectionItemId("test");	//if this should fail autoboxing could be the source of the error 
//		setItemCaption("test", Messages.getString("property"));
//		this.setValue("test");
		for(String property: properties) {this.addItem(property);}
		System.out.println(properties);
		this.setImmediate(true);
		this.setMultiSelect(false);

		this.setNewItemsAllowed(false);
		this.setInputPrompt(Messages.getString("property"));
		this.setReadOnly(true);
//
//		this.setNullSelectionAllowed(false);
	}
}
