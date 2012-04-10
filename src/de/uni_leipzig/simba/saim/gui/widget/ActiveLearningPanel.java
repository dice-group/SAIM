package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jgap.InvalidConfigurationException;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.learner.GeneticActiveLearner;
import de.uni_leipzig.simba.saim.core.Configuration;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class ActiveLearningPanel extends Panel
{	
	static Logger logger = Logger.getLogger("LIMES");
	Configuration config = Configuration.getInstance();
	GeneticActiveLearner learner;
	VerticalLayout layout;
	Button learn;
	Button terminate;
	//DetailedInstanceMappingTable iMapTable = null;
	InstanceMappingTable iMapTable = null;

	//	protected void setupContextHelp()
	//	{
	//		ContextHelp contextHelp = new ContextHelp();
	//		getContent().addComponent(contextHelp);
	//		contextHelp.addHelpForComponent(suggestionComboBox, Messages.getString("classpairsfromlimes")); //$NON-NLS-1$
	//	}

	public ActiveLearningPanel()
	{
		logger.setLevel(Level.WARN);
		layout = new VerticalLayout();
		layout.setWidth("100%");
		setContent(layout);
		//addComponent(new ActiveLearningRow("bla","blubb"));

		Label l;
		Configuration config = Configuration.getInstance();
		Layout learnLayout;
		learnLayout = new HorizontalLayout();
		learnLayout.setWidth("100%");
		layout.addComponent(learnLayout);
		// add Button
		learn = new Button("start Learning");
		learn.addListener(new ActiveLearnButtonClickListener(learnLayout));
		layout.addComponent(learn);
		learn.setEnabled(true);

		terminate = new Button("Get best solution so far");
		terminate.addListener(new ActiveTerminateButtonClickListener(learnLayout));
		terminate.setEnabled(false);
		layout.addComponent(terminate);

		// configure
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("populationSize", 20);
		param.put("generations", 50);
		param.put("mutationRate", 0.5f);
		param.put("preserveFittest",true);
		param.put("propertyMapping", null);
		param.put("trainingDataSize", 5);
		param.put("granularity", 2);
		param.put("config", config.getLimesConfiReader());
		learner = new GeneticActiveLearner();
		try {
			learner.init(config.getSource(), config.getTarget(), param);
		} catch (InvalidConfigurationException e) {
			layout.setComponentError(new UserError(e.getMessage()));
			e.printStackTrace();
		}
		Mapping map = learner.learn(new Mapping());
		iMapTable = new InstanceMappingTable(map);
		if (map.size()>0)
		{
			learnLayout.removeAllComponents();
			learnLayout.addComponent(iMapTable.getTable());

			learnLayout.removeAllComponents();
			learnLayout.addComponent(iMapTable.getTable());
			terminate.setEnabled(true);	
		}
	}

	/** Listener for learn buttton @author Lyko */
	public class ActiveLearnButtonClickListener implements Button.ClickListener
	{
		Layout l;
		/** Constructor with the Component to hold the Table.*/
		public ActiveLearnButtonClickListener(Layout l) {this.l = l;}

		@Override
		public void buttonClick(ClickEvent event) {		
			Mapping map;
			if(iMapTable == null) // on start
			{
				logger.info("Starting Active Learning");
				map = learner.learn(new Mapping());
			}
			else
			{
				logger.info("Starting round");
				map = iMapTable.tabletoMapping();
				map = learner.learn(map);
			}
			
			//iMapTable = new DetailedInstanceMappingTable(map,learner.getFitnessFunction().getSourceCache(),learner.getFitnessFunction().getTargetCache());
			iMapTable = new InstanceMappingTable(map);
			if (map.size()>0)
			{
				l.removeAllComponents();
				l.addComponent(iMapTable.getTable());

				l.removeAllComponents();
				l.addComponent(iMapTable.getTable());
				terminate.setEnabled(true);	
			}
		}		
	}

	public class ActiveTerminateButtonClickListener implements Button.ClickListener {
		Layout l;
		public ActiveTerminateButtonClickListener(Layout l) {
			this.l = l;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			Metric metric = learner.terminate();
			Label label = new Label();
			label.setCaption("Best solution");
			label.setValue(metric.expression+" with threshold "+metric.threshold);
			l.addComponent(label);
		}
	}
}