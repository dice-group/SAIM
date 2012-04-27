package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.HashMap;

import org.jgap.InvalidConfigurationException;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.learner.GeneticBatchLearner;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;

public class BatchLearningPanel extends MetricLearnPanel {
	public BatchLearningPanel() {
		super();
		learn.addListener(new BatchLearnButtonClickListener(learnLayout));
		init();
	}
	
	private void init() {
		if(learner != null) {
			learner.getFitnessFunction().destroy();
		}
		// configure
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("populationSize", 50);
		param.put("generations", 50);
		param.put("mutationRate", 0.5f);
		param.put("preserveFittest",true);
		param.put("propertyMapping", config.propertyMapping);
		param.put("trainingDataSize", 20);
		param.put("granularity", 2);
		param.put("config", config.getLimesConfiReader());
		learner = new GeneticBatchLearner();
		try {
			learner.init(config.getSource(), config.getTarget(), param);
		} catch (InvalidConfigurationException e) {
			layout.setComponentError(new UserError(e.getMessage()));
			e.printStackTrace();
		}
		Mapping map = learner.learn(new Mapping());
		iMapTable = new InstanceMappingTable(map, learner.getFitnessFunction().getSourceCache(), learner.getFitnessFunction().getTargetCache());
		if (map.size()>0)
		{
			learnLayout.removeAllComponents();
			learnLayout.addComponent(iMapTable.getTable());

			learnLayout.removeAllComponents();
			learnLayout.addComponent(iMapTable.getTable());
		}		
	}
		
	/** Listener for learn buttton @author Lyko */
	public class BatchLearnButtonClickListener implements Button.ClickListener
	{
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
			iMapTable = new InstanceMappingTable(map, learner.getFitnessFunction().getSourceCache(), learner.getFitnessFunction().getTargetCache());
			l.removeAllComponents();
			l.addComponent(iMapTable.getTable());
			l.removeAllComponents();
			l.addComponent(iMapTable.getTable());
			if (map.size()>0)
			{
				SAIMApplication.getInstance().getMainWindow().showNotification("Learning without additional training data");
				terminate.setEnabled(true);	
			}
		}		
	}
}
