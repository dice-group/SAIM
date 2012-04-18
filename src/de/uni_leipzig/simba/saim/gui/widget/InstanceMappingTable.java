package de.uni_leipzig.simba.saim.gui.widget;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

import de.uni_leipzig.simba.data.Instance;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.InstanceMatch;

public class InstanceMappingTable implements Serializable
{
	private static final long serialVersionUID	= 4443146911119590775L;
	final Mapping data;
	final List <InstanceMatch> dataList = new LinkedList<InstanceMatch>();
	
	BeanItemContainer<InstanceMatch> beanItemContainer;
	Table t;
	// TODO:  t.setColumnCollapsingAllowed(true);
	public InstanceMappingTable(Mapping m)
	{
		data = m;
		
		for(String uri1 : data.map.keySet())
			for(Entry<String, Double> uri2 : data.map.get(uri1).entrySet())
			{dataList.add(new InstanceMatch(uri1, uri2.getKey(), uri2.getValue()));}
	}
	
	@SuppressWarnings({ "serial" })
	public Table getTable()
	{	
		beanItemContainer = new BeanItemContainer<InstanceMatch>(InstanceMatch.class);
		beanItemContainer.addAll(dataList);
//		t = new Table(Messages.getString("InstanceMappingTable.instances"), beanItemContainer); //$NON-NLS-1$
		t = new Table("", beanItemContainer);
//		t.addItem();
		t.setWidth("100%"); //$NON-NLS-1$
		t.setColumnExpandRatio(Messages.getString("InstanceMappingTable.sourceuri"), 0.5f); //$NON-NLS-1$
		t.setColumnExpandRatio(Messages.getString("InstanceMappingTable.targeturi"), 0.5f); //$NON-NLS-1$
		t.setColumnAlignment(Messages.getString("value"), Table.ALIGN_RIGHT); //$NON-NLS-1$
		t.setColumnAlignment(Messages.getString("InstanceMappingTable.isamatch"), Table.ALIGN_CENTER); //$NON-NLS-1$
//		t.setColumnWidth("Is a match?", "3em");
//		t.setColumnWidth("uri2", 150);
		
		t.addListener(new ItemClickListener() {			
			@Override
			public void itemClick(ItemClickEvent event) {
				InstanceMatch m = (InstanceMatch) event.getItemId();
				//@TODO 
			//	SAIMApplication.getInstance().getMainWindow().open(new ExternalResource(m.getUri1()), "_");
			}
		});
		t.addGeneratedColumn(Messages.getString("InstanceMappingTable.isamatch"), new ColumnGenerator() { //$NON-NLS-1$
            @Override
            public Component generateCell(final Table source, final Object itemId, final Object columnId) {
            	final InstanceMatch bean = (InstanceMatch) itemId;
                final CheckBox checkBox = new CheckBox();
                checkBox.setImmediate(true);
                checkBox.addListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(final ValueChangeEvent event) {
                        bean.setSelected((Boolean) event.getProperty().getValue());
                        System.out.println("Selected " + bean); //$NON-NLS-1$
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
		// add column for source uri
		t.addGeneratedColumn(Messages.getString("InstanceMappingTable.sourceuri"), new Table.ColumnGenerator() { //$NON-NLS-1$
			  @Override
			  public Object generateCell(Table source, final Object itemId, final Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
			    String uri = String.valueOf(bean.getUri1());
			    return InstanceMatch.getLinkLabelToUri(uri);  
			  }
			});
		t.addGeneratedColumn(Messages.getString("InstanceMappingTable.targeturi"), new Table.ColumnGenerator() { //$NON-NLS-1$
			  @Override
			  public Object generateCell(Table source, final Object itemId, final Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
				String uri = String.valueOf(bean.getUri2());
			    return InstanceMatch.getLinkLabelToUri(uri);  
			  }
			});
		t.addGeneratedColumn(Messages.getString("value"), new Table.ColumnGenerator() { //$NON-NLS-1$
			  @Override
			  public Object generateCell(Table source, final Object itemId, final Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
			    return bean.getValue();
			  }
			});
		t.setColumnReorderingAllowed(true);
		t.setVisibleColumns(new Object[] {Messages.getString("InstanceMappingTable.sourceuri"), Messages.getString("InstanceMappingTable.targeturi"), Messages.getString("value"), Messages.getString("InstanceMappingTable.isamatch")}); 
		// Allow selecting items from the table.
		t.setSelectable(true);
		// Send changes in selection immediately to server.
		t.setImmediate(true);
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
		for(InstanceMatch bean : beanItemContainer.getItemIds()) {
			if(bean.isSelected()) {
				result.add(bean.getOriginalUri1(), bean.getOriginalUri2(), 1.0d);
			}
		}
		return result;
	}
	
	/**
	 * Method to return the source data of the Table.
	 * @return
	 */
	public Mapping getMapping() {
		return data;
	}
}