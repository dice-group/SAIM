package de.uni_leipzig.simba.saim.gui.widget;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.core.InstanceMatch;
public class InstanceMappingTable implements Serializable{
	Mapping data;
	List <InstanceMatch> dataList = new LinkedList<InstanceMatch>();
	BeanItemContainer beanItemContainer;
	
	public InstanceMappingTable(Mapping m) {
		data = m;
		for(String uri1 : data.map.keySet())
			for(Entry<String, Double> uri2 : data.map.get(uri1).entrySet()) {
				dataList.add(new InstanceMatch(uri1, uri2.getKey(), uri2.getValue()));
			}
	}
	
//	public Table getTable() {
//		BeanContainer<Integer, Pair<String>> instances = new BeanContainer<Integer, Pair<String>>(Pair.class);
//		instances.setBeanIdProperty("id");
//		for(String uri1 : data.map.keySet())
//			for(Entry<String, Double> uri2 : data.map.get(uri1).entrySet())
//				instances.addBean(new Pair<String>(uri1, uri2.getKey()));
//		return new Table("Instances", instances);
//	}
//	
	public Table getFlatTable() {
		
		//add columns
//		t.addContainerProperty("uri1", String.class, null);
//		t.addContainerProperty("uri2", String.class, null);
//		t.addContainerProperty("value", Double.class, null);
		// add data
		
		beanItemContainer = new BeanItemContainer<InstanceMatch>(InstanceMatch.class);
		beanItemContainer.addAll(dataList);
		Table t = new Table("Instances", beanItemContainer);
		t.setColumnWidth("uri1", 150);
		t.setColumnWidth("uri2", 150);
		t.addGeneratedColumn("Is a match?", new ColumnGenerator() {
            @Override
            public Component generateCell(final Table source, final Object itemId, final Object columnId) {
            	final InstanceMatch bean = (InstanceMatch) itemId;
                final CheckBox checkBox = new CheckBox();
                checkBox.setImmediate(true);
                checkBox.addListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(final ValueChangeEvent event) {
                        bean.setSelected((Boolean) event.getProperty().getValue());
                        System.out.println("Selected " + bean);
                    }
                });

                if (bean.isSelected()) {
                    checkBox.setValue(true);
                } else {
                    checkBox.setValue(false);
                }
                return checkBox;
            }
        });
		
		
		//t.setVisibleColumns();//set visible columns);
		t.setVisibleColumns(new Object[] {"uri1", "uri2", "value", "Is a match?"});
		// Allow selecting items from the table.
		t.setSelectable(true);
		// Send changes in selection immediately to server.
		t.setImmediate(true);
		return t;
	}
}
