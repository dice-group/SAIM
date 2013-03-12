package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.LimesRunner;
import de.uni_leipzig.simba.saim.core.LimesRunner.CachingException;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
/**
 * Panel to execute a Mapping.
 * @author Klaus Lyko
 */
public class ExecutionPanel extends PerformPanel implements PropertyChangeListener {
	static final long serialVersionUID = -6418644523043046619L;
	private final Messages messages;
	private LimesRunner lR;
	private Label progressLabel;
	private ProgressIndicator progress;
	private Mapping m = new Mapping();
	private float maxSteps = LimesRunner.MAX_STEPS;

	Layout mainLayout = new VerticalLayout();
	Thread thread;
	public ExecutionPanel(final Messages messages)
	{
		super(messages.getString("ExecutionPanel.executelinkspecification")); //$NON-NLS-1$
		this.messages=messages;
//		Label l;
		lR = new LimesRunner();
		lR.addPropertyChangeListener(this);
		progressLabel = new Label(messages.getString("ExecutionPanel.initialized")); //$NON-NLS-1$
		progress = new ProgressIndicator();
		progress.setValue(0);

		setWidth("100%"); //$NON-NLS-1$
		this.setContent(mainLayout);
		mainLayout.addComponent(progressLabel);
		mainLayout.addComponent(progress);
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(LimesRunner.MESSAGE)) {
			progressLabel.setValue(evt.getNewValue());
			progressLabel.requestRepaint();
		}
		if(evt.getPropertyName().equals(LimesRunner.STEP)) {
			float newV = Float.valueOf(evt.getNewValue().toString());
			progress.setValue(newV/maxSteps);
			progress.requestRepaint();
		}
		if(evt.getPropertyName().equalsIgnoreCase(LimesRunner.ERROR)) {
			String mess = evt.getNewValue().toString();
			mainLayout.addComponent(new Label(messages.getString("ExecutionPanel.errorNotification")+mess, Label.CONTENT_XHTML)); //$NON-NLS-1$
		}
	}


	private void runMapping() {
		final Configuration config = ((SAIMApplication)getApplication()).getConfig();
		thread = new Thread() {
			@Override
			public void run() {
				try {
					m = lR.runConfig(((SAIMApplication)getApplication()).getConfig());
				} catch (CachingException cE) {
					getWindow().showNotification(cE.toString());
				}
				progress.setValue(1f);
				progressLabel.setValue(messages.getString("ExecutionPanel.mappingperformed")); //$NON-NLS-1$
				InstanceMappingTable iT = new InstanceMappingTable(getApplication(), config, m, lR.getSourceCache(), lR.getTargetCache(), false,messages);
				mainLayout.removeAllComponents();
				ResultPanel results = new ResultPanel(iT,messages);
				mainLayout.addComponent(results);
//				mainLayout.removeComponent(start);
			}
		};
		thread.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClose() {
		thread.stop();
	}
	@Override
	public void start() {
		runMapping();
	}
}
