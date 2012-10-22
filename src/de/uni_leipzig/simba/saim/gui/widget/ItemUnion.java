package de.uni_leipzig.simba.saim.gui.widget;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
/** An item that is a union of items. properties will be returned from
 * the first Item in the given collection that contains that property id.*/

public class ItemUnion implements Item
{
	/**
	 */
	private static final long serialVersionUID = 8975349959967352635L;
	// todo: wenn zwei den gleiche namen haben darf keins wegfallen
	public final List<Item> items;
	public final List<String> ids = new LinkedList<String>();



	public ItemUnion(List<Item> items)
	{
		this.items = items;
		int i = 0;
		for(Item item: items)
		{
			for(Object o : item.getItemPropertyIds())
			{
				ids.add(String.valueOf(i)+o.toString());
			}
			i++;
		}
	}

	public ItemUnion(Item... items)
	{
		this.items = new Vector<Item>();
		int i = 0;
		for(Item item: items)
		{
			this.items.add(item);
			for(Object o : item.getItemPropertyIds())
			{
				ids.add(String.valueOf(i)+o.toString());
			}
			i++;
		}
	}
	@Override public Property getItemProperty(Object id)
	{
		Item item = items.get(Integer.valueOf(id.toString().charAt(0)));
		return item.getItemProperty(id.toString().substring(1));
	}

	@Override public Collection<?> getItemPropertyIds()
	{
		return ids;
	}

	@Override public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException
	{
		return false;
	}

	@Override
	public boolean removeItemProperty(Object id) throws UnsupportedOperationException
	{
		return false;
	}

}
