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
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Instance;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.InstanceMatch;
import de.uni_leipzig.simba.saim.gui.widget.panel.InstanceInfoPanel;

public class InstanceMappingTable implements Serializable
{
	private static final long serialVersionUID	= 4443146911119590775L;
	final HybridCache sourceCache;
	final HybridCache targetCache;
	final Mapping data;
	final List <InstanceMatch> dataList = new LinkedList<InstanceMatch>();
	private boolean showBoxes = true;
	BeanItemContainer<InstanceMatch> beanItemContainer;
	Table t;
	// TODO:  t.setColumnCollapsingAllowed(true);
	public InstanceMappingTable(Mapping m, HybridCache sourceCache, HybridCache targetCache, boolean showBoxes)
	{
		this.showBoxes = showBoxes;
		data = m;
		this.sourceCache=sourceCache;
		this.targetCache=targetCache;
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
		if(showBoxes) {
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

	                if (bean.isSelected() || bean.getValue()>=0.75d) {
	                    checkBox.setValue(true);
	                } else {
	                    checkBox.setValue(false);
	                }
	                return checkBox;
	            }
	        });
		}
		
		// add column to display addition info
		t.addGeneratedColumn("info",new Table.ColumnGenerator() {			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
				// TODO is there a more elegant way for this?
				ClassResource cRes = new ClassResource("../../../../icons/emblem-notice.png", SAIMApplication.getInstance());
				Embedded image = new Embedded("",
						cRes);
				image.addListener(new InfoIconClickListener(bean));
				return image;
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
		if(showBoxes)
			t.setVisibleColumns(new Object[] {"info", Messages.getString("InstanceMappingTable.sourceuri"), Messages.getString("InstanceMappingTable.targeturi"), Messages.getString("value"), Messages.getString("InstanceMappingTable.isamatch")});
		else
			t.setVisibleColumns(new Object[] {"info", Messages.getString("InstanceMappingTable.sourceuri"), Messages.getString("InstanceMappingTable.targeturi"), Messages.getString("value")});
		
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
	
	/**Listener reacts on clicks on the info image*/
	class InfoIconClickListener implements ClickListener {
		InstanceMatch row; 
		public InfoIconClickListener(InstanceMatch row) {this.row = row;}
		@Override
		public void click(ClickEvent event) {
			
			Instance i1 = sourceCache.getInstance(row.getOriginalUri1());
			Instance i2 = targetCache.getInstance(row.getOriginalUri2());
			if(i1!=null && i2 != null) {
				Window sub = new Window();
				Panel p = new InstanceInfoPanel(i1, i2);
				sub.setWidth(p.getWidth()+2f, p.getWidthUnits());
				sub.addComponent(p);
				SAIMApplication.getInstance().getMainWindow().addWindow(sub);
				}
		}
		
	}
}