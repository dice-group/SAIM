package de.uni_leipzig.simba.saim.gui.widget;

import java.util.List;

import com.vaadin.ui.NativeSelect;

public class PropertyComboBox extends NativeSelect
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2710241205403191988L;

	//public PropertyComboBox(String title) {super(title);this();}
	public PropertyComboBox(List<String> properties)
	{
		super();
		//this.setWidth("100%");
//		addItem("test");
//		setNullSelectionItemId("test");	//if this should fail autoboxing could be the source of the error 
//		setItemCaption("test", messages.getString("property"));
//		this.setValue("test");
		for(String property: properties) {this.addItem(property);}
		System.out.println(properties);
		this.setImmediate(true);
		this.setMultiSelect(false);

		this.setNewItemsAllowed(false);
		
		//this.setInputPrompt(messages.getString("property"));
		//this.setReadOnly(true);
//
//		this.setNullSelectionAllowed(false);
	}
}
