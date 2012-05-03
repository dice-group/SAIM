package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.Arrays;
import java.util.List;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
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
	Configuration config = Configuration.getInstance();

	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getContent().addComponent(contextHelp);
	}

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
	
	

		//		Label sourcePropertyLabel = new Label(Messages.getString("sourceproperty"));
		//		sourcePropertyLabel.setWidth("50%");
		//		Label targetPropertyLabel = new Label(Messages.getString("targetproperty"));
		//		sourcePropertyLabel.setWidth("50%");
		//		addComponent(sourcePropertyLabel);
		//		addComponent(targetPropertyLabel);
		//		addComponent(new PropertyPairPanel());
		setupContextHelp();
	}
	
	@Override
	public void attach()
	{
		super.attach();
		KBInfo source = Configuration.getInstance().source;
		KBInfo target = Configuration.getInstance().target;

		Table table = new Table();
		//table.setWidth("100%");
		addComponent(table);
		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */		
		table.addContainerProperty(Messages.getString("sourceproperty"), PropertyComboBox.class,  null);
		table.addContainerProperty(Messages.getString("targetproperty"), PropertyComboBox.class,  null);
//		table.addContainerProperty("", Embedded.class,  null);
//		table.setColumnWidth("",20);

		/* Add a few items in the table. */
		PropertyComboBox sourceBox = new PropertyComboBox(mockAllPropertiesFromKBInfo(source));

		sourceBox.addListener(new ValueChangeListener()
		{			
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				System.out.println("value changed");				
			}
		});
		PropertyComboBox targetBox = new PropertyComboBox(mockAllPropertiesFromKBInfo(target));
//		ClassResource resource = new ClassResource("img/no_crystal_clear_16.png",getApplication());
//		Embedded embedded = new Embedded("",resource);
//		Button closeButton = new Button();
//		closeButton.setWidth("16px");
//		closeButton.setHeight("16px");		
//		closeButton.setIcon(resource);
		table.addItem(new Object[]{sourceBox,targetBox},null);// TODO: nicer close button
		addComponent(new PropertyComboBox(mockAllPropertiesFromKBInfo(null)));
		
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