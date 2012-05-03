package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.List;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.ui.ComboBox;
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

	public PropertyMatchingPanel()
	{
		setContent(new VerticalLayout());
		getContent().setWidth("100%");
		/* Create the table with a caption. */
		Table table = new Table();
		table.setWidth("100%");
		addComponent(table);
		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */		
		table.addContainerProperty(Messages.getString("sourceproperty"), ComboBox.class,  null);
		table.addContainerProperty(Messages.getString("targetproperty"), ComboBox.class,  null);

		KBInfo source = Configuration.getInstance().source;
		KBInfo target = Configuration.getInstance().target;

		/* Add a few items in the table. */		
		table.addItem(
				new Object[]
						{
						new PropertyComboBox(allPropertiesFromKBInfo(source)),
						new PropertyComboBox(allPropertiesFromKBInfo(target))
						}
				,null);

		//		Label sourcePropertyLabel = new Label(Messages.getString("sourceproperty"));
		//		sourcePropertyLabel.setWidth("50%");
		//		Label targetPropertyLabel = new Label(Messages.getString("targetproperty"));
		//		sourcePropertyLabel.setWidth("50%");
		//		addComponent(sourcePropertyLabel);
		//		addComponent(targetPropertyLabel);
		//		addComponent(new PropertyPairPanel());
		setupContextHelp();
	}

	private class PropertyPairPanel extends Panel
	{
		public PropertyPairPanel()
		{
			setContent(new HorizontalLayout());
			ComboBox sourcePropertyComboBox = new ComboBox();
			sourcePropertyComboBox.setWidth("50%");
			ComboBox targetPropertyComboBox = new ComboBox();
			targetPropertyComboBox.setWidth("50%");
			addComponent(sourcePropertyComboBox);
			addComponent(targetPropertyComboBox);
		}
	}
}