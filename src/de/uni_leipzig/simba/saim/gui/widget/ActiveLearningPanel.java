package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashMap;

import org.jgap.InvalidConfigurationException;

import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.UserError;
import com.vaadin.terminal.Paintable.RepaintRequestListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.learner.GeneticActiveLearner;
import de.uni_leipzig.simba.saim.core.Configuration;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class ActiveLearningPanel extends Panel
{	
	Configuration config = Configuration.getInstance();
	GeneticActiveLearner learner;
	VerticalLayout layout;
	Button learn;
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
				map = learner.learn(new Mapping());
				iMapTable = new InstanceMappingTable(map);
				l.removeAllComponents();
				l.addComponent(iMapTable.getTable());
				return;
			}
		
			map = iMapTable.tabletoMapping();
			iMapTable = new InstanceMappingTable(map);
			l.removeAllComponents();
			l.addComponent(iMapTable.getTable());
		}
		
	}

}