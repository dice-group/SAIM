package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.HashMap;
import org.jgap.InvalidConfigurationException;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.learner.GeneticBatchLearner;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationBean;

public class BatchLearningPanel extends MetricLearnPanel {
	private static final long serialVersionUID = 8799521759150040510L;
	private final Messages messages;

	public BatchLearningPanel(SAIMApplication application, final Messages messages)
	{
		super(application);
		this.messages=messages;
	}

	public void attach() {
		learn.addListener(new BatchLearnButtonClickListener(learnLayout));
		config = ((SAIMApplication)getApplication()).getConfig();
		init();
	}

	public BatchLearningPanel(SAIMApplication application, LearnerConfigurationBean learnerConfigBean,final Messages messages)
	{
		super(application, learnerConfigBean);
		learn.addListener(new BatchLearnButtonClickListener(learnLayout));
		this.messages=messages;
	}


	private void init() {
		if(learner != null) {
			learner.getFitnessFunction().destroy();
		}
		if(params == null) {
			 HashMap<String, Object> params = new HashMap<String, Object>();
			 params = new HashMap<String, Object>();
			 params.put("populationSize", 10);
			 params.put("generations", 50);
			 params.put("mutationRate", 0.5f);
			 params.put("trainingDataSize", 10);
		}
		// configure

		params.put("preserveFittest",true);
		if(config.propertyMapping!=null)
			params.put("propertyMapping", config.propertyMapping);
		else {
			params.put("propertyMapping", new PropertyMapping());
		}
		params.put("granularity", 2);
		params.put("config", config.getLimesConfiReader());
		learner = new GeneticBatchLearner();
		try {
			learner.init(config.getSource(), config.getTarget(), params);
		} catch (InvalidConfigurationException e) {
			layout.setComponentError(new UserError(e.getMessage()));
			e.printStackTrace();
		}
		Mapping map = learner.learn(new Mapping());
		iMapTable = new InstanceMappingTable(getApplication(), config, map, learner.getFitnessFunction().getSourceCache(), learner.getFitnessFunction().getTargetCache(), true,messages);
		if (map.size()>0)
		{
			learnLayout.removeAllComponents();
			learnLayout.addComponent(iMapTable.getTable());
		}
	}

	/** Listener for learn buttton @author Lyko */
	public class BatchLearnButtonClickListener implements Button.ClickListener {
		private static final long serialVersionUID = -5750209861689708829L;
		Layout l;
		/** Constructor with the Component to hold the Table.*/
		public BatchLearnButtonClickListener(Layout l) {this.l = l;}

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
			iMapTable = new InstanceMappingTable(getApplication(), config, map, learner.getFitnessFunction().getSourceCache(), learner.getFitnessFunction().getTargetCache(), true,messages);
			l.removeAllComponents();
			l.addComponent(iMapTable.getTable());
			if (map.size()==0)
			{
				getApplication().getMainWindow().showNotification("Learning without additional training data");
			}
			terminate.setEnabled(true);
		}
	}
}
