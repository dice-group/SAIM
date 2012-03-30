package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jgap.InvalidConfigurationException;

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
	Layout learnLayout;
	InstanceMappingTable iMapTable;

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
		l = new Label(config.toString(), Label.CONTENT_XHTML);
		addComponent(l);
		
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
		param.put("populationSize", 10);
		param.put("generations", 10);
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
		
	}
	
	/**
	 * Listener for learn buttton
	 * @author Lyko
	 *
	 */
	public class ActiveLearnButtonClickListener implements Button.ClickListener {
		Layout l;
		/**
		 * Constructor with the Component to hold the Table.
		 * @param c
		 */
		public ActiveLearnButtonClickListener(Layout l) {
			this.l = l;		}
		
		@Override
		public void buttonClick(ClickEvent event) {		
			Mapping map;
			if(iMapTable == null) { // on start
				logger.info("Starting AL");
				map = learner.learn(new Mapping());
				iMapTable = new InstanceMappingTable(map);
				l.removeAllComponents();
				l.addComponent(iMapTable.getTable());
				return;
			}
			logger.info("Starting round");
			map = iMapTable.tabletoMapping();
			map = learner.learn(map);
			if (map.size()>0) {
				iMapTable = new InstanceMappingTable(map);
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