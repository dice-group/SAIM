package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.LimesRunner;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
public class ExecutionPanel extends Panel implements PropertyChangeListener {
	LimesRunner lR;
	Label progressLabel;
	ProgressIndicator progress;
	private Mapping m = new Mapping();
	float maxSteps = LimesRunner.MAX_STEPS;
	Button start;
	//Button showResults;
	Button startActiveLearning;
	Button startBatchLearning;
	Button startSelfConfig;
	Layout mainLayout = new VerticalLayout();
	
	@SuppressWarnings("serial")
	public ExecutionPanel() {
		
		
		
		super(Messages.getString("ExecutionPanel.executelinkspecification")); //$NON-NLS-1$		
		Label l;
		Configuration config = Configuration.getInstance();

		lR = new LimesRunner();
		lR.addPropertyChangeListener(this);
		progressLabel = new Label(Messages.getString("ExecutionPanel.initialized")); //$NON-NLS-1$
		progress = new ProgressIndicator();
	//	progress.setCaption("Progress");
		progress.setValue(0);
//		showResults = new Button(Messages.getString("ExecutionPanel.showResults"));
//		showResults.setEnabled(false);
//		showResults.addListener(new ClickListener() {			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				SAIMApplication appl = (SAIMApplication) getApplication();
//				InstanceMappingTable iT = new InstanceMappingTable(m);
//				//DetailedInstanceMappingTable iT = new DetailedInstanceMappingTable(m,lR.getSourceCache(),lR.getTargetCache());
//				appl.showComponent(iT.getTable());
//			}
//		});
//		
		mainLayout.addComponent(showPropertyMatching());
		
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
						SAIMApplication appl = (SAIMApplication) getApplication();
						InstanceMappingTable iT = new InstanceMappingTable(m, lR.getSourceCache(), lR.getTargetCache());
//						DetailedInstanceMappingTable iT = new DetailedInstanceMappingTable(m,lR.getSourceCache(),lR.getTargetCache());
						ResultPanel results = new ResultPanel(iT);
						appl.showComponent(results);					
					}
				}.start();				
			}
		});
		startActiveLearning = new Button("start active learning");
		startActiveLearning.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				SAIMApplication appl = (SAIMApplication) getApplication();
				appl.showComponent(new ActiveLearningPanel());
			}
		});
		startBatchLearning = new Button("start batch learning");
		startBatchLearning.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				SAIMApplication appl = (SAIMApplication) getApplication();
				appl.showComponent(new BatchLearningPanel());
			}
		});
		startSelfConfig = new Button("Start self configuration");
		
		startSelfConfig.addListener(new MetricPanel.SelfConfigClickListener(mainLayout));
		
		
		setWidth("100%"); //$NON-NLS-1$
		this.setContent(mainLayout);
		mainLayout.addComponent(progressLabel);
		mainLayout.addComponent(progress);
		mainLayout.addComponent(start);
		mainLayout.addComponent(startActiveLearning);
		mainLayout.addComponent(startBatchLearning);
		mainLayout.addComponent(startSelfConfig);
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
	
	private Panel showPropertyMatching() {
		Panel p = new Panel();
		if(!Configuration.getInstance().propertyMapping.wasSet()) {
			p.setContent(new Panel("No Property Mapping defined."));
		} else {
			p.setCaption("Property Mapping");
			VerticalLayout panelLayout = new VerticalLayout();
			p.setContent(panelLayout);
		
			ListSelect stringSelect = new ListSelect("String properties");
			stringSelect.setNullSelectionAllowed(false);
			stringSelect.setRows(Configuration.getInstance().propertyMapping.getStringPropMapping().map.size());
			for(Entry<String, HashMap<String, Double>> entry : Configuration.getInstance().propertyMapping.getStringPropMapping().map.entrySet()) {
				for(String t : entry.getValue().keySet()) {
					stringSelect.addItem(entry.getKey() +" - "+t);
				}
			}
			ListSelect numberSelect = new ListSelect("NumberProperty");
			numberSelect.setNullSelectionAllowed(false);
			numberSelect.setRows(Configuration.getInstance().propertyMapping.getNumberPropMapping().map.size());
			for(Entry<String, HashMap<String, Double>> entry : Configuration.getInstance().propertyMapping.getNumberPropMapping().map.entrySet()) {
				for(String t : entry.getValue().keySet()) {
					numberSelect.addItem(entry.getKey() +" - "+t);
				}
			}
			if(Configuration.getInstance().propertyMapping.getStringPropMapping().size>0)
				panelLayout.addComponent(stringSelect);
			if(Configuration.getInstance().propertyMapping.getNumberPropMapping().size>0)
				panelLayout.addComponent(numberSelect);
		}
		return p;
	}
}
