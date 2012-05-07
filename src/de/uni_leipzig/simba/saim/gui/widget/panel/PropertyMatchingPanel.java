package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.PropertyComboBox;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class PropertyMatchingPanel extends Panel
{		
	private List<Object[]> rows = new Vector<Object[]>();
	private KBInfo source = Configuration.getInstance().source;
	private KBInfo target = Configuration.getInstance().target;	
	private ClassResource closeImageResource;
	private Table table = new Table();
	private List<String> sourceProperties;
	private List<String> targetProperties;

	private Object columnValue(Object o)
	{
		return ((PropertyComboBox)o).getValue();
	}

	public boolean isValid() // empty and full pairs ok, half full ones are not
	{		
		for(Object[] row: rows)
		{if(columnValue(row[0])==null^columnValue(row[1])==null) {return false;}}
		return true;
	}

	//	protected void setupContextHelp()
	//	{
	//		ContextHelp contextHelp = new ContextHelp();
	//		getContent().addComponent(contextHelp);
	//	}

	private String classRestrictionToClass(String classRestriction)
	{
		return classRestriction.substring((classRestriction.lastIndexOf(' ')+1)).replace("<","").replace(">","");
	}

	/**	returns all properties (not just the ones from the property matching) that instances of the knowledge base of the 
	 * class restriction specified in the KBInfo have. <b>May break if the class restriction is not set.</b>*/
	private List<String> allPropertiesFromKBInfo(KBInfo kb)
	{
		return SPARQLHelper.properties(
				kb.endpoint,
				kb.graph,
				classRestrictionToClass(kb.getClassRestriction()));
	}

	private List<String> mockAllPropertiesFromKBInfo(KBInfo kb)
	{
		return Arrays.asList(new String[] {"rdfs:label","rdfs:schmabel"});
	}

	public PropertyMatchingPanel()
	{
		setContent(new VerticalLayout());
		getContent().setWidth("100%");
		/* Create the table with a caption. */

		//	setupContextHelp();
	}

	private class RowChangeListener implements ValueChangeListener
	{
		final Object[] row;

		public RowChangeListener(Object[] source)
		{			
			if(source.length!=3) throw new IllegalArgumentException();
			this.row=source;
		}

		@Override
		public void valueChange(ValueChangeEvent event)
		{			
			if(row==rows.get(rows.size()-1)) // complete last row -> create new
			{
				if(!(((PropertyComboBox)row[0]).getValue()==null||((PropertyComboBox)row[1]).getValue()==null))
				{
					Object[] row = createTableRow();
					table.addItem(row,row);
				}
			}
			else if(columnValue(row[0])==null&&columnValue(row[1])==null) // remove empty rows at non-last position 
			{
				removeRow(row);
			}
		}
	}

	private Object[] createTableRow()
	{
		PropertyComboBox sourceBox = new PropertyComboBox(sourceProperties);		
		PropertyComboBox targetBox = new PropertyComboBox(targetProperties);
		//Embedded closeImage = new Embedded("",closeImageResource);
		Button closeRowButton = new Button();
		closeRowButton.setIcon(closeImageResource);		
		final Object[] row = {sourceBox,targetBox,closeRowButton};
		sourceBox.addListener(new RowChangeListener(row));
		targetBox.addListener(new RowChangeListener(row));
		closeRowButton.addListener(new ClickListener()
		{			
			@Override
			public void buttonClick(ClickEvent event)
			{
				if(rows.size()>1) {removeRow(row);}
			}
		});
		rows.add(row);
		return row;
	}

	private void removeRow(Object[] row)
	{
		rows.remove(row);
		if(!table.removeItem(row));		
	}

	@Override
	public void attach()
	{
		super.attach();			
		sourceProperties = allPropertiesFromKBInfo(source);
		targetProperties = allPropertiesFromKBInfo(target);
		//table.setWidth("100%");
		addComponent(table);		
		closeImageResource = new ClassResource("img/no_crystal_clear_16.png",getApplication());		
		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */		
		table.addContainerProperty(Messages.getString("sourceproperty"), PropertyComboBox.class,  null);
		table.addContainerProperty(Messages.getString("targetproperty"), PropertyComboBox.class,  null);
		table.addContainerProperty("", Button.class,  null);
		//		table.setColumnWidth("",20);

		/* Add a few items in the table. */

		//		Button closeButton = new Button();
		//		closeButton.setWidth("16px");
		//		closeButton.setHeight("16px");		
		//		closeButton.setIcon(resource);
		Object[] row = createTableRow();
		table.addItem(row,row);
		//addComponent(new PropertyComboBox(mockAllPropertiesFromKBInfo(null)));

	}

	//	private class PropertyPairPanel extends Panel
	//	{
	//		public PropertyPairPanel()
	//		{
	//			setContent(new HorizontalLayout());
	//			ComboBox sourcePropertyComboBox = new ComboBox();
	//			sourcePropertyComboBox.setWidth("50%");
	//			ComboBox targetPropertyComboBox = new ComboBox();
	//			targetPropertyComboBox.setWidth("50%");
	//			addComponent(sourcePropertyComboBox);
	//			addComponent(targetPropertyComboBox);
	//		}
	//	}
}