package de.uni_leipzig.simba.saim.gui.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.LimesRunner;
import de.uni_leipzig.simba.saim.Messages;
public class ExecutionPanel extends Panel implements PropertyChangeListener {
	LimesRunner lR;
	Label progressLabel;
	ProgressIndicator progress;
	private Mapping m = new Mapping();
	float maxSteps = 5;
	Button start;
	
	public ExecutionPanel() {
		super(Messages.getString("ExecutionPanel.executelinkspecification")); //$NON-NLS-1$
		lR = new LimesRunner();
		lR.addPropertyChangeListener(this);
		progressLabel = new Label(Messages.getString("ExecutionPanel.initialized")); //$NON-NLS-1$
		progress = new ProgressIndicator();
	//	progress.setCaption("Progress");
		progress.setValue(0);
		start = new Button(Messages.getString("ExecutionPanel.startmapping")); //$NON-NLS-1$
		start.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				new Thread() {
					@Override
					public void run() {
						m = lR.runConfig(Configuration.getInstance());	
						start.setEnabled(false);
						progress.setValue(1f);
						progressLabel.setValue(Messages.getString("ExecutionPanel.mappingperformed")); //$NON-NLS-1$
					}
				}.start();				
			}
		});
		setWidth("100%"); //$NON-NLS-1$
		this.setContent(new VerticalLayout());
		this.getContent().addComponent(progressLabel);
		this.getContent().addComponent(progress);
		this.getContent().addComponent(start);
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
	}
}