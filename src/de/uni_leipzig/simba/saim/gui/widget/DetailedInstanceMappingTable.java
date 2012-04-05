package de.uni_leipzig.simba.saim.gui.widget;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Instance;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.InstanceMatch;

public class DetailedInstanceMappingTable implements Serializable 
{
	private static final long serialVersionUID	= 4443146911119590775L;
	final Mapping data;
	final List <InstanceMatch> dataList = new LinkedList<InstanceMatch>();
	final HybridCache sourceCache;
	final HybridCache targetCache;

	BeanItemContainer<InstanceMatch> beanItemContainer;
	Table t;
	// TODO:  t.setColumnCollapsingAllowed(true);
	public DetailedInstanceMappingTable(Mapping m, HybridCache sourceCache, HybridCache targetCache)
	{
		data = m;
		this.sourceCache=sourceCache;
		this.targetCache=targetCache;

		for(String uri1 : data.map.keySet())
			for(Entry<String, Double> uri2 : data.map.get(uri1).entrySet())
			{dataList.add(new InstanceMatch(uri1, uri2.getKey(), uri2.getValue()));}
	}

	public Table getTable()
	{
		t = new Table(Messages.getString("InstanceMappingTable.instances")); //$NON-NLS-1$
	
		//Instance instance = null;
		for(String sourceURI: data.map.keySet())
		{
			t.addItem(new ItemUnion(new InstanceItem(sourceCache.getInstance(sourceURI)),
					new InstanceItem(targetCache.getInstance(data.map.get(sourceURI).keySet().iterator().next()))));
		}
		return t;
	}

	/**
	 * Method to get the Mapping out of the table, or for that matter out of the
	 * underlying BeanContainer.
	 * @return Mapping holding all checked instances.
	 */
	public Mapping tabletoMapping() {
		if(t == null)
			return new Mapping();
		Mapping result = new Mapping();
		//		for(InstanceMatch bean : beanItemContainer.getItemIds()) {
		//			if(bean.isSelected()) {
		//				result.add(bean.getOriginalUri1(), bean.getOriginalUri2(), 1.0d);
		//			}
		//		}
		return result;
	}
}