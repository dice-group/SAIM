package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashSet;
import java.util.Set;
import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.SAIMApplication;

public class PropertyMappingGenerator extends Panel {
	/**
	 */
	private static final long serialVersionUID = -2569976837556090479L;
	VerticalLayout layout = new VerticalLayout();
	Set<String> sourceProps = new HashSet<String>();
	Set<String> targetProps = new HashSet<String>();
	ListSelect sourceSelector = new ListSelect("Source Properties");
	ListSelect targetSelector = new ListSelect("Target Properties");
	Button addPropertyMatch = new Button("Add Property Match");
	Mapping propertyMapping = new Mapping();

	public PropertyMappingGenerator() {
		setContent(layout);
		fillProperties();
		HorizontalLayout hL = new HorizontalLayout();
		sourceSelector.setNullSelectionAllowed(false);
		targetSelector.setNullSelectionAllowed(false);
		hL.addComponent(sourceSelector);
		hL.addComponent(targetSelector);
		hL.addComponent(addPropertyMatch);
		addPropertyMatch.setEnabled(false);
		addPropertyMatch.addListener(new addPropertyMatchListener());
		Refresher refresher = new Refresher();
		PropertyMappingGeneratorRefreshListener listener = new PropertyMappingGeneratorRefreshListener();
		refresher.addListener(listener);
		layout.addComponent(hL);
		addComponent(refresher);
	}


	private void fillProperties() {
		new Thread()
		{
			@Override
			public void run()
			{
				getAllProps();
				{
					for(String s : sourceProps) {
						sourceSelector.addItem(s);
					}

					for(String t : targetProps) {
						targetSelector.addItem(t);
					}
				}
				addPropertyMatch.setEnabled(true);
			}
		}.start();
	}

	/**
	 * Get the properties.
	 */
	private void getAllProps() {
		//for source
		KBInfo info = ((SAIMApplication) getApplication()).getConfig().getSource();
		String className = info.getClassOfendpoint();
		for(String prop : SPARQLHelper.properties(info.endpoint, info.graph, className)) {
			String s_abr=PrefixHelper.abbreviate(prop);
			sourceProps.add(s_abr);
		}
		//for target
		info = ((SAIMApplication) getApplication()).getConfig().getTarget();
		className = info.getClassOfendpoint();
		for(String prop : SPARQLHelper.properties(info.endpoint, info.graph, className)) {
			String s_abr=PrefixHelper.abbreviate(prop);
			targetProps.add(s_abr);
		}
	}

	private class addPropertyMatchListener implements Button.ClickListener {

		/**
		 */
		private static final long serialVersionUID = 1931071630998923715L;

		@Override
		public void buttonClick(ClickEvent event) {
			String sourceProp = sourceSelector.getValue().toString();
			String targetProp = targetSelector.getValue().toString();
			// adding Properties
			if(sourceProp != null && sourceProp.length()>0 && targetProp != null && targetProp.length()>0 )
				((SAIMApplication) getApplication()).getConfig().addPropertiesMatch(sourceProp, targetProp, true);
		}
	}

	public boolean isValid() {
		if(propertyMapping.size()>0)
			return true;
		return false;
	}

	public class PropertyMappingGeneratorRefreshListener implements RefreshListener
	{
		boolean running = true;
		private static final long serialVersionUID = -8765221895426102605L;
		@Override public void refresh(final Refresher source)	{if(!running) {removeComponent(source);source.setEnabled(false);}}
	}
}
