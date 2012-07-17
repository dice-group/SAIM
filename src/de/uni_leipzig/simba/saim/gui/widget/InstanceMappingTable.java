package de.uni_leipzig.simba.saim.gui.widget;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.data.Instance;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.util.Pair;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.InstanceMatch;
import de.uni_leipzig.simba.saim.gui.widget.panel.InstanceInfoPanel;

public class InstanceMappingTable implements Serializable
{
	private final Messages messages;
	private static final long serialVersionUID	= 4443146911119590775L;
	final Cache sourceCache;
	final Cache targetCache;
	final Mapping data;
	final List <InstanceMatch> dataList = new LinkedList<InstanceMatch>();
	private boolean showBoxes = true;
	BeanItemContainer<InstanceMatch> beanItemContainer;
	Table t;
	// TODO:  t.setColumnCollapsingAllowed(true);
	Application application;
	Configuration config;
	public InstanceMappingTable(Application application, Configuration config, Mapping m, Cache sourceCache, Cache targetCache, boolean showBoxes,final Messages messages)
	{
		this.application=application;
		this.config = config;
		this.messages=messages;
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
//		t = new Table(messages.getString("InstanceMappingTable.instances"), beanItemContainer); //$NON-NLS-1$
		t = new Table("", beanItemContainer);
//		t.addItem();
		t.setWidth("100%"); //$NON-NLS-1$
		t.setColumnExpandRatio("uri1", 0.5f); //$NON-NLS-1$
		t.setColumnExpandRatio("uri2", 0.5f); //$NON-NLS-1$
		t.setColumnAlignment("value", Table.ALIGN_RIGHT); //$NON-NLS-1$
		t.setColumnAlignment(messages.getString("InstanceMappingTable.isamatch"), Table.ALIGN_CENTER); //$NON-NLS-1$
//		t.setColumnWidth("Is a match?", "3em");
//		t.setColumnWidth("uri2", 150);
		
//		t.addListener(new ItemClickListener() {			
//			@Override
//			public void itemClick(ItemClickEvent event) {
//				InstanceMatch m = (InstanceMatch) event.getItemId();
//				SAIMApplication.getInstance().getMainWindow().open(new ExternalResource(m.getUri1()), "_");
//			}
//		});
		if(showBoxes) {
			t.addGeneratedColumn(messages.getString("InstanceMappingTable.isamatch"), new ColumnGenerator() { //$NON-NLS-1$
	            @Override
	            public Component generateCell(final Table source, final Object itemId, final Object columnId) {
	            	final InstanceMatch bean = (InstanceMatch) itemId;
	                final CheckBox checkBox = new CheckBox();
	                checkBox.setImmediate(true);
	                checkBox.addListener(new Property.ValueChangeListener() {
	                    @Override
	                    public void valueChange(final ValueChangeEvent event) {
	                        bean.setSelected((Boolean) event.getProperty().getValue());
//	                        System.out.println("Selected " + bean); //$NON-NLS-1$
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
				ClassResource cRes = new ClassResource("../../../../icons/emblem-notice.png", application);
				Embedded image = new Embedded("",
						cRes);
				image.addListener(new InfoIconClickListener(bean));
				return image;
			}
		});
		final List<Pair<String>> propPair = new LinkedList<Pair<String>>();
		Iterator<Pair<String>> propMapIterator = config.propertyMapping.stringPropPairs.iterator();
		if(propMapIterator.hasNext()) {
			propPair.add(propMapIterator.next());
		} else {
			//walkaround: just select some random prop
			propPair.add(new Pair<String>(config.getSource().functions.entrySet().iterator().next().getKey(), config.getTarget().functions.entrySet().iterator().next().getKey()));
		}
		// add column for source uri
		t.addGeneratedColumn("uri1", new Table.ColumnGenerator() { //$NON-NLS-1$
			  @Override
			  public Object generateCell(Table source, final Object itemId, final Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
			    String uri = String.valueOf(bean.getUri1());
			    String uri2 = bean.getOriginalUri1();
				TreeSet<String> labels = null;
				if(targetCache.containsUri(uri2)) {
			    	Instance instance =  sourceCache.getInstance(uri2);
			    	if(instance.getAllProperties().contains("rdfs:label"))
			    		labels = sourceCache.getInstance(uri2).getProperty("rdfs:label");
			    }
			    if(labels != null && labels.size()>=1)
			    	return InstanceMatch.getLinkLabelToUri(uri, labels.first());
			    return InstanceMatch.getLinkLabelToUri(uri);  
			  }
			});
		t.addGeneratedColumn("uri2", new Table.ColumnGenerator() { //$NON-NLS-1$
			  @Override
			  public Object generateCell(Table source, final Object itemId, final Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
				String uri = String.valueOf(bean.getUri2());
				String uri2 = bean.getOriginalUri2();
				TreeSet<String> labels = null;
				if(targetCache.containsUri(uri2)) {
			    	Instance instance =  targetCache.getInstance(uri2);
			    	if(instance.getAllProperties().contains("rdfs:label"))
			    		labels = targetCache.getInstance(uri2).getProperty("rdfs:label");
			    }
				if(labels != null && labels.size()>=1)
			    	return InstanceMatch.getLinkLabelToUri(uri, labels.first());
			    return InstanceMatch.getLinkLabelToUri(uri);  
			  }
			});
		t.addGeneratedColumn("value", new Table.ColumnGenerator() { //$NON-NLS-1$
			  @Override
			  public Object generateCell(Table source, final Object itemId, final Object columnId) {
				final InstanceMatch bean = (InstanceMatch) itemId;
			    return bean.getValue();
			  }
			});
		// for ordering renaming
		t.setColumnReorderingAllowed(true);
		t.setColumnHeader("uri1", messages.getString("InstanceMappingTable.sourceuri"));
		t.setColumnHeader("uri2", messages.getString("InstanceMappingTable.targeturi"));
		t.setColumnHeader("value", messages.getString("value"));
		
		if(showBoxes)
			t.setVisibleColumns(new Object[] {"info", "uri1", "uri2", "value", messages.getString("InstanceMappingTable.isamatch")});
		else
			t.setVisibleColumns(new Object[] {"info","uri1","uri2", "value"});
		
		// Allow selecting items from the table.
		t.setSelectable(true);
		t.setSortDisabled(false);
		t.setColumnReorderingAllowed(true);
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
		/**
		 * 
		 */
		private static final long serialVersionUID = 5370196171933436788L;
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
				application.getMainWindow().addWindow(sub);
				}
		}
		
	}
}