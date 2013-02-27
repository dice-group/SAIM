package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.Map.Entry;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.util.Pair;
import de.uni_leipzig.simba.learning.query.LabelBasedPropertyMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationBean;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationForm;
import de.uni_leipzig.simba.saim.gui.widget.panel.ActiveLearningPanel.LEARNER;
/**
 * Panel for Learner Configuration.
 * @author Lyko
 */
public class LearningPanel extends PerformPanel
{
	/**
	 */
	private static final long serialVersionUID = 1906531007896945738L;
	private final Messages messages;
	private final Layout mainLayout = new VerticalLayout();
	private PerformPanel learnerPanel = null;
	private Tree learnerSelect;
	private Panel propMappingPanel;
	@SuppressWarnings("unused")
	private LearnerConfigurationForm configForm;
	private LearnerConfigurationBean learnerConfigBean = new LearnerConfigurationBean();
	SAIMApplication application;

	public LearningPanel(SAIMApplication application, final Messages messages) {
		this.application = application;
		this.messages=messages;
	}

	@Override
	public void attach() {
		generateLearnerSelect();
		setContent(mainLayout);
		performPropertyMapping();
		propMappingPanel = new Panel();
		fillpropMappingPanel();
		mainLayout.addComponent(configForm = new LearnerConfigurationForm(learnerConfigBean,messages));
		mainLayout.addComponent(propMappingPanel);
		mainLayout.addComponent(learnerSelect);

	}

	/**
	 * Generate Selection List of Learner.
	 */
	private void generateLearnerSelect() {
		final Object[][] methods = new Object[][]{
		        new Object[]{messages.getString("LearningPanel.gpbatchlearner")},  //$NON-NLS-1$
		        new Object[]{messages.getString("LearningPanel.learnerselect"), 
		        		messages.getString("LearningPanel.ALEAGE"), 
		        		messages.getString("LearningPanel.ALCLUSTERING"), 
		        		messages.getString("LearningPanel.ALWEIGHTDECAY")} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		};
		learnerSelect = new Tree(); //$NON-NLS-1$
//		learnerSelect.addItem); //$NON-NLS-1$
//		learnerSelect.addItem(messages.getString("LearningPanel.gpactivelearner")); //$NON-NLS-1$
		for(int i = 0; i<methods.length; i++) {
			//first element is parent
			String method = (String) methods[i][0];
			learnerSelect.addItem(method);
			if(methods[i].length == 1) {
				learnerSelect.setChildrenAllowed(method, false);
			} else {
				for(int j = 1; j < methods[i].length; j++) {
					String subMethod = (String) methods[i][j];
					learnerSelect.addItem(subMethod);
					learnerSelect.setParent(subMethod, method);
					learnerSelect.setChildrenAllowed(subMethod, false);
				}
				learnerSelect.expandItemsRecursively(method);
			}
		}
		learnerSelect.setNullSelectionAllowed(false);
		learnerSelect.addListener(new LearnerSelectListener(messages));
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
//			PropertyMapper propMapper = new PropertyMapper();
			LabelBasedPropertyMapper propMapper = new LabelBasedPropertyMapper();
			String classSource = config.getSource().getClassOfendpoint();
			String classTarget = config.getTarget().getClassOfendpoint();
			if(classSource != null && classTarget != null) {
				System.out.println("Getting PropertyMapping of: "+config.getSource().endpoint+", "+config.getTarget().endpoint+" : "+classSource+" - "+classTarget); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				Mapping propertyMapping = propMapper.getPropertyMapping(config.getSource().endpoint,
						config.getTarget().endpoint, classSource, classTarget);
				for(String uri1 : propertyMapping.map.keySet()) {
					for(Entry<String, Double> e : propertyMapping.map.get(uri1).entrySet()) {
						config.addPropertiesMatch(uri1, e.getKey(), true);
					}
				}
				return true;
			} else {
				System.out.println("Cannot perform automatic property mapping due to missing class specifications."); //$NON-NLS-1$
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
			String caption = messages.getString("LearningPanel.propMapping"); //$NON-NLS-1$
			this.propMappingPanel.setCaption(caption);
			propMappingPanel.setContent(vertl);
		} else {
			String caption = messages.getString("LearningPanel.PropMappingDefault"); //$NON-NLS-1$
			this.propMappingPanel.setCaption(caption);
			config.propertyMapping.setDefault(config.getSource(), config.getTarget());
			System.out.println(	config.propertyMapping);
			propMappingPanel.setContent(vertl);
		}
		Table t = new Table();
		t.addContainerProperty(messages.getString("LearningPanel.tableSourceProp"), String.class, ""); //$NON-NLS-1$ //$NON-NLS-2$
		t.addContainerProperty(messages.getString("LearningPanel.tableTargetProp"), String.class, ""); //$NON-NLS-1$ //$NON-NLS-2$
		t.addContainerProperty(messages.getString("LearningPanel.tablePropType"), String.class, ""); //$NON-NLS-1$ //$NON-NLS-2$
		int count = 0;
		for(Pair<String> pair : config.propertyMapping.stringPropPairs) {
			t.addItem(new Object[]{pair.a,pair.b,"String"},new Integer(count)); //$NON-NLS-1$
			count++;
		}
		for(Pair<String> pair : config.propertyMapping.numberPropPairs) {
			t.addItem(new Object[]{pair.a,pair.b,"Number"}, new Integer(count)); //$NON-NLS-1$
			count++;
		}
		t.setPageLength(Math.min(count, 5));
		vertl.addComponent(t);
	}

	/**
	 * Reacts on selection of a learner.
	 * @author Lyko
	 */
	public class LearnerSelectListener implements ValueChangeListener
	{
		/**
		 */
		private static final long serialVersionUID = 4295499822581706211L;
		private final Messages messages;
		public LearnerSelectListener(final Messages messages) {this.messages=messages;}

		@Override
		public void valueChange(ValueChangeEvent event) {
			if(event.getProperty().toString().equals(messages.getString("LearningPanel.gpbatchlearner"))) { //$NON-NLS-1$
				learnerPanel = new BatchLearningPanel(application, learnerConfigBean, messages);
			}
			if(event.getProperty().toString().equals(messages.getString("LearningPanel.ALEAGE"))) { //$NON-NLS-1$
				learnerPanel = new ActiveLearningPanel(application, learnerConfigBean, LEARNER.AL_EAGLE, messages);
			}
			if(event.getProperty().toString().equals(messages.getString("LearningPanel.ALCLUSTERING"))) { //$NON-NLS-1$
				learnerPanel = new ActiveLearningPanel(application, learnerConfigBean, LEARNER.AL_CLUSTER, messages);
			}
			if(event.getProperty().toString().equals(messages.getString("LearningPanel.ALWEIGHTDECAY"))) { //$NON-NLS-1$
				learnerPanel = new ActiveLearningPanel(application, learnerConfigBean, LEARNER.AL_WD, messages);
			}
			mainLayout.removeAllComponents();
			mainLayout.addComponent(learnerPanel);
			learnerPanel.start();
		}
	}


	@Override
	public void onClose() {
		application.refresh();
		if(learnerPanel!=null) {
			learnerPanel.onClose();
		}
	}

	@Override
	public void start() {
		generateLearnerSelect();
	}


}
