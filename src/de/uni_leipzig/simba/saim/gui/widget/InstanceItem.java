package de.uni_leipzig.simba.saim.gui.widget;

import java.util.Collection;

import lombok.AllArgsConstructor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Label;

import de.uni_leipzig.simba.data.Instance;

@AllArgsConstructor
public class InstanceItem implements Item
{
	public final Instance instance;
	
	@Override public Property getItemProperty(Object id) {return new Label(instance.getProperty(id.toString()).first());}

	@Override public Collection<?> getItemPropertyIds() {return instance.getAllProperties();}

	@Override
	public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException
	{
		return false;
	}

	@Override
	public boolean removeItemProperty(Object id) throws UnsupportedOperationException
	{
		return false;
	}

}