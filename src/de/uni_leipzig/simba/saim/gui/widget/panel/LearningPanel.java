package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.Map.Entry;

import sun.security.krb5.Config;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.learning.query.PropertyMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.ActiveLearningPanel;
import de.uni_leipzig.simba.saim.gui.widget.PropertyMappingGenerator;
/**
 * Need to configure
 * @author Lyko
 *
 */
public class LearningPanel extends Panel {
	
	
	Layout mainLayout = new VerticalLayout();
	ListSelect learnerSelect;
	Mapping propertyMapping;
	Panel propMappingPanel;
	PropertyMappingGenerator propertyGenerator;
	
	public LearningPanel() {
		setContent(mainLayout);
		generateLearnerSelect();
		performPropertyMapping();
		propMappingPanel = new Panel();
		fillpropMappingPanel();
		mainLayout.addComponent(propMappingPanel);
		mainLayout.addComponent(propertyGenerator = new PropertyMappingGenerator());
		mainLayout.addComponent(learnerSelect);
		
	}
	
	private void generateLearnerSelect() {
		learnerSelect = new ListSelect(Messages.getString("LearningPanel.learnerselect")); //$NON-NLS-1$
		learnerSelect.addItem(Messages.getString("LearningPanel.gpbatchlearner")); //$NON-NLS-1$
		learnerSelect.addItem(Messages.getString("LearningPanel.gpactivelearner")); //$NON-NLS-1$
		learnerSelect.addListener(new LearnerSelectListener());
		learnerSelect.setNullSelectionAllowed(false);
	}
	
	private void performPropertyMapping() {
		Configuration config = Configuration.getInstance();
		PropertyMapper propMapper = new PropertyMapper();
		String classSource = config.getSource().getClassOfendpoint();
		String classTarget = config.getTarget().getClassOfendpoint();
		if(classSource != null && classTarget != null) {
			System.out.println("Getting PropertyMapping of: "+config.getSource().endpoint+", "+config.getTarget().endpoint+" : "+classSource+" - "+classTarget);
			propertyMapping = propMapper.getPropertyMapping(config.getSource().endpoint,
					config.getTarget().endpoint, classSource, classTarget);
		} else {
			System.out.println("Cannot perform automatic property mapping due to missing class specifications.");
		}			
	}
	
	private void fillpropMappingPanel() {
		String caption = "Computed Property Mapping:";
		this.propMappingPanel.setCaption(caption);
		VerticalLayout vertl = new VerticalLayout();
		propMappingPanel.setContent(vertl);
		for(String s : propertyMapping.map.keySet()) {
			for(Entry<String, Double> e : propertyMapping.map.get(s).entrySet()) {
				Panel p = new Panel(s + " - "+e.getKey()+": "+e.getValue());
				vertl.addComponent(p);
			}
		}
		if(propertyMapping.size()>0) {
			//propMappingPanel.add
			final Button usePropMapping = new Button("Use Mapping");
			usePropMapping.addListener(new Button.ClickListener() {				
				@Override
				public void buttonClick(ClickEvent event) {
					for(String s : propertyMapping.map.keySet()) {
						for(Entry<String, Double> e : propertyMapping.map.get(s).entrySet()) {
							Configuration.getInstance().addPropertiesMatch(s, e.getKey());
						}
					}
					usePropMapping.setEnabled(false);
				}
			});
			vertl.addComponent(usePropMapping);
		} else {
			vertl.addComponent(new Label("No suggestions available"));
		}
	}
	
	
	public class LearnerSelectListener implements ValueChangeListener {

		@Override
		public void valueChange(ValueChangeEvent event) {
			if(event.getProperty().toString().equals(Messages.getString("LearningPanel.gpbatchlearner"))) {
				mainLayout.removeComponent(learnerSelect);
				mainLayout.removeComponent(propMappingPanel);
				mainLayout.removeComponent(propertyGenerator);
			}
			if(event.getProperty().toString().equals(Messages.getString("LearningPanel.gpactivelearner"))) {
				mainLayout.removeComponent(learnerSelect);
				mainLayout.removeComponent(propMappingPanel);
				mainLayout.removeComponent(propertyGenerator);
				mainLayout.addComponent(new ActiveLearningPanel());
			}
		}
		
	}
	
	
}
