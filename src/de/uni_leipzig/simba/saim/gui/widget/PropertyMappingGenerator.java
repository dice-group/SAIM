package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

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
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.panel.MetricPanel.SelfConfigRefreshListener;

public class PropertyMappingGenerator extends Panel {
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
		KBInfo info = Configuration.getInstance().getSource();
		String className = info.restrictions.get(0).substring(info.restrictions.get(0).indexOf("rdf:type")+8);
		for(String prop : SPARQLHelper.properties(info.endpoint, info.graph, className)) {
			String s_abr=PrefixHelper.abbreviate(prop);
			sourceProps.add(s_abr);
		}
		//for target
		info = Configuration.getInstance().getTarget();
		className = info.restrictions.get(0).substring(info.restrictions.get(0).indexOf("rdf:type")+8);
		for(String prop : SPARQLHelper.properties(info.endpoint, info.graph, className)) {
			String s_abr=PrefixHelper.abbreviate(prop);
			targetProps.add(s_abr);
		}	
	}
	
	private class addPropertyMatchListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			String sourceProp = sourceSelector.getValue().toString();
			String targetProp = targetSelector.getValue().toString();
			// adding Properties
			if(sourceProp != null && sourceProp.length()>0 && targetProp != null && targetProp.length()>0 )
				Configuration.getInstance().addPropertiesMatch(sourceProp, targetProp);
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
