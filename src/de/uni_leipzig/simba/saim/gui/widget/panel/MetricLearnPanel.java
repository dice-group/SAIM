package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.learner.LinkSpecificationLearner;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationBean;
/**
 * Panel used for metric genetic learner.
 * @author Lyko
 */
public class MetricLearnPanel extends  PerformPanel{
	/**
	 */
	private static final long serialVersionUID = -2043563912763885666L;
	public static Logger logger = Logger.getLogger("LIMES");
	SAIMApplication application;
	protected Configuration config;// = Configuration.getInstance();
	public LinkSpecificationLearner learner;
	protected VerticalLayout layout;
	protected Button learn;
	public Button terminate;
	//DetailedInstanceMappingTable iMapTable = null;
	public InstanceMappingTable iMapTable = null;
	protected Layout learnLayout;
	protected HashMap<String, Object> params;

	public MetricLearnPanel(SAIMApplication application) {
		this.application = application;
		logger.setLevel(Level.WARN);
		layout = new VerticalLayout();
		layout.setWidth("100%");
		setContent(layout);


		// add Button
		learn = new Button("learn");
		layout.addComponent(learn);
		learn.setEnabled(true);

		HorizontalLayout solution = new HorizontalLayout();

		terminate = new Button("Get best solution so far");
		terminate.addListener(new TerminateButtonClickListener(solution));
		terminate.setEnabled(false);
		layout.addComponent(terminate);
		layout.addComponent(solution);
		learnLayout = new HorizontalLayout();
		learnLayout.setWidth("100%");
		layout.addComponent(learnLayout);
	}
	@Override
	public void attach() {
		config = ((SAIMApplication)getApplication()).getConfig();
	}

	public MetricLearnPanel(SAIMApplication application, LearnerConfigurationBean learnerConfigBean) {
		this(application);
		params = learnerConfigBean.createParams();
	}

	public class TerminateButtonClickListener implements Button.ClickListener {
		/**
		 */
		private static final long serialVersionUID = 6943435309453349530L;
		Layout l;
		Label label = new Label();
		public TerminateButtonClickListener(Layout l) {
			this.l = l;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			boolean alreadyDisplayed = false;
			Iterator<Component> iter = l.getComponentIterator();
			while(iter.hasNext()) {
				if(iter.next().equals(label))
					alreadyDisplayed = true;
			}
			if(alreadyDisplayed)
				l.removeComponent(label);
			else {
				// get expression and set it
				Metric metric = learner.terminate();
				label.setCaption("Best solution:");

				label.setValue(metric.getExpression()+" with threshold "+ metric.getThreshold());
				config.setMetricExpression(metric.getExpression());
				config.setAcceptanceThreshold(metric.getThreshold());
				((SAIMApplication) getApplication()).refresh();
				l.addComponent(label);
			}
		}
	}

	@Override
	public void onClose() {
		learner.getFitnessFunction().destroy();

		learner = null;
		application.refresh();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}
}
