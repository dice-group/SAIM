package de.uni_leipzig.simba.saim.gui.widget.panel;

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
import de.uni_leipzig.simba.genetics.learner.SupervisedLearnerParameters;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationBean;
/**
 * Panel used for metric genetic learner.
 * @author Klaus Lyko
 */
public class MetricLearnPanel extends  PerformPanel{
	private static final long serialVersionUID = -2043563912763885666L;
	public static Logger logger = Logger.getLogger("LIMES"); //$NON-NLS-1$
	SAIMApplication application;
	protected Configuration config;// = Configuration.getInstance();
	public LinkSpecificationLearner learner;
	protected VerticalLayout layout;
	protected Button learn;
	public Button terminate;
	Label warn;
	public InstanceMappingTable iMapTable = null;
	protected Layout learnLayout;
	protected SupervisedLearnerParameters params;

	public MetricLearnPanel(SAIMApplication application) {
		super(application.messages.getString("MetricLearnPanel.caption"));
		this.application = application;
		logger.setLevel(Level.WARN);
		layout = new VerticalLayout();
		layout.setWidth("100%"); //$NON-NLS-1$
		setContent(layout);

		// add Button
		learn = new Button(application.messages.getString("MetricLearnPanel.learnButton")); //$NON-NLS-1$
		learn.setEnabled(true);
	
		HorizontalLayout solution = new HorizontalLayout();

		terminate = new Button(application.messages.getString("MetricLearnPanel.getBestSolutionButton")); //$NON-NLS-1$
		terminate.addListener(new TerminateButtonClickListener(solution));
		terminate.setEnabled(false);
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(learn);
		buttonLayout.addComponent(terminate);
		layout.addComponent(buttonLayout);
		layout.addComponent(solution);
		learnLayout = new VerticalLayout();
		learnLayout.setWidth("100%"); //$NON-NLS-1$
		layout.addComponent(learnLayout);
	}
	@Override
	public void attach() {
		config = ((SAIMApplication)getApplication()).getConfig();
	}

	/**
	 * Constructor with bean holding the parameters for the learner.
	 * @param application
	 * @param learnerConfigBean
	 */
	public MetricLearnPanel(SAIMApplication application, LearnerConfigurationBean learnerConfigBean) {
		this(application);
		params = learnerConfigBean.createParams();
	}

	public class TerminateButtonClickListener implements Button.ClickListener {
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
			// get expression and set it
			Metric metric = learner.terminate();
			label.setCaption(application.messages.getString("MetricLearnPanel.SolutionLabelCaption")); //$NON-NLS-1$

			label.setValue(metric.getExpression()+" "+application.messages.getString("MetricLearnPanel.SolutionLabelWithThreshold")+" "+metric.getThreshold()); //$NON-NLS-1$
			config.setMetricExpression(metric.getExpression());
			config.setAcceptanceThreshold(metric.getThreshold());
			((SAIMApplication) getApplication()).refresh();
			if(!alreadyDisplayed) {
				l.addComponent(label);
			}
		}
	}

	protected void showWarning(String message) {
		warn = new Label("<h1>"+message+"</h1>", Label.CONTENT_XHTML); //$NON-NLS-1$ //$NON-NLS-2$
		layout.removeAllComponents();
		layout.addComponent(warn);
	}
	
	@Override
	public void onClose() {
		learner.getFitnessFunction().destroy();
		learner = null;
		application.refresh();
	}

	@Override
	public void start() {
		// Nothing to do here
	}
}
