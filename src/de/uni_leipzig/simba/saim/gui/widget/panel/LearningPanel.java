package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.Map.Entry;

import sun.security.krb5.Config;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.util.Pair;
import de.uni_leipzig.simba.learning.query.PropertyMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.PropertyMappingGenerator;
import de.uni_leipzig.simba.saim.gui.widget.Listener.MetricPanelListeners;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationBean;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationForm;
import de.uni_leipzig.simba.saim.gui.widget.window.EndpointWindow;
/**
 * Panel for Learner Configuration.
 * @author Lyko
 *
 */
public class LearningPanel extends PerformPanel {	
	Layout mainLayout = new VerticalLayout();
	PerformPanel learnerPanel = null;
	Select learnerSelect;
	Panel propMappingPanel;	
	LearnerConfigurationForm configForm;
	LearnerConfigurationBean learnerConfigBean = new LearnerConfigurationBean();
	
	public LearningPanel() {
	
	}
	@Override
	public void attach() {
		generateLearnerSelect();
		setContent(mainLayout); 
		performPropertyMapping();
		propMappingPanel = new Panel();
		fillpropMappingPanel();
		mainLayout.addComponent(configForm = new LearnerConfigurationForm(learnerConfigBean));
		mainLayout.addComponent(propMappingPanel);
		mainLayout.addComponent(learnerSelect);
		
	}
	
	/**
	 * Generate Selection List of Learner.
	 */
	private void generateLearnerSelect() {
		learnerSelect = new Select(Messages.getString("LearningPanel.learnerselect")); //$NON-NLS-1$
		learnerSelect.addItem(Messages.getString("LearningPanel.gpbatchlearner")); //$NON-NLS-1$
		learnerSelect.addItem(Messages.getString("LearningPanel.gpactivelearner")); //$NON-NLS-1$
		learnerSelect.setNullSelectionAllowed(false);
		learnerSelect.addListener(new LearnerSelectListener());
		learnerSelect.setEnabled(true);
		learnerSelect.setImmediate(true);
	}
	
	/**
	 * If no PropertyMapping was set. Try to calculate.
	 * @return true if we can calculate.
	 * @TODO Multithreading.
	 */
	private boolean performPropertyMapping() {
		Configuration config = ((SAIMApplication)getApplication()).getConfig();//Configuration.getInstance();
		if(!config.propertyMapping.wasSet()) {
			PropertyMapper propMapper = new PropertyMapper();
			String classSource = config.getSource().getClassOfendpoint();
			String classTarget = config.getTarget().getClassOfendpoint();
			if(classSource != null && classTarget != null) {
				System.out.println("Getting PropertyMapping of: "+config.getSource().endpoint+", "+config.getTarget().endpoint+" : "+classSource+" - "+classTarget);
				Mapping propertyMapping = propMapper.getPropertyMapping(config.getSource().endpoint,
						config.getTarget().endpoint, classSource, classTarget);
				for(String uri1 : propertyMapping.map.keySet()) {
					for(Entry<String, Double> e : propertyMapping.map.get(uri1).entrySet()) {
						config.addPropertiesMatch(uri1, e.getKey(), true);
					}
				}
				return true;
			} else {
				System.out.println("Cannot perform automatic property mapping due to missing class specifications.");
				return false;
			}
		} else {
			return true;
		}					
	}
	
	/**
	 * To show Property Mapping.
	 */
	private void fillpropMappingPanel() {
		Configuration config = ((SAIMApplication)getApplication()).getConfig();
		VerticalLayout vertl = new VerticalLayout();
		if(config.propertyMapping.wasSet()) {
			String caption = "Property Mapping:";
			this.propMappingPanel.setCaption(caption);			
			propMappingPanel.setContent(vertl);
		} else {
			String caption = "No Property defined and unable to calculate - using default:";
			this.propMappingPanel.setCaption(caption);		
			config.propertyMapping.setDefault(config.getSource(), config.getTarget());
			System.out.println(	config.propertyMapping);
			propMappingPanel.setContent(vertl);
		}
		Table t = new Table();
		t.addContainerProperty("Source property", String.class, "");
		t.addContainerProperty("Target property", String.class, "");
		t.addContainerProperty("Property Types", String.class, "");
		int count = 0;
		for(Pair<String> pair : config.propertyMapping.stringPropPairs) {
			t.addItem(new Object[]{pair.a,pair.b,"String"},new Integer(count));
			count++;
		}
		for(Pair<String> pair : config.propertyMapping.numberPropPairs) {
			t.addItem(new Object[]{pair.a,pair.b,"Number"}, new Integer(count));
			count++;
		}
		t.setPageLength(Math.min(count, 5));
		vertl.addComponent(t);
	}
	
	/**
	 * Reacts on selection of a learner.
	 * @author Lyko
	 *
	 */
	public class LearnerSelectListener implements ValueChangeListener {
		@Override
		public void valueChange(ValueChangeEvent event) {
			if(event.getProperty().toString().equals(Messages.getString("LearningPanel.gpbatchlearner"))) {
				learnerPanel = new BatchLearningPanel(learnerConfigBean);
			}
			if(event.getProperty().toString().equals(Messages.getString("LearningPanel.gpactivelearner"))) {
				learnerPanel = new ActiveLearningPanel(learnerConfigBean);
			}
			mainLayout.removeAllComponents();
			mainLayout.addComponent(learnerPanel);
			learnerPanel.start();
		}		
	}


	@Override
	public void onClose() {
		((SAIMApplication) SAIMApplication.getInstance()).refresh();
		if(learnerPanel!=null) {
			learnerPanel.onClose();
		}
	}

	@Override
	public void start() {
		generateLearnerSelect();
	}
	
	
}
