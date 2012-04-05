package de.uni_leipzig.simba.saim.gui.widget;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.vaadin.cytographer.Cytographer;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.google.gwt.user.client.Element;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import csplugins.layout.algorithms.force.ForceDirectedLayout;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.learning.query.PropertyMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.ClassMatchingPanel.SuggestionsRefreshListener;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.SimpleClassifier;
/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class MetricPanel extends Panel
{	
	
	ManualMetricForm manualMetricForm;
	private static final long	serialVersionUID	= 6766679517868840795L;
//	TextField metricTextField = new TextField("Insert metric here");
	Mapping propMapping;
	VerticalLayout mainLayout = new VerticalLayout();
	HorizontalLayout layout = new HorizontalLayout();
	Set<String> sourceProps = new HashSet<String>();
	Set<String> targetProps = new HashSet<String>();
	Button selfconfig;
	Cytographer cytographer;
	

	// should prevent browser's default right click action
	public static native void disableContextMenu(final Element elem) /*-{
																		elem.oncontextmenu=function() {return false};
																		}-*/;
	
	//	protected void setupContextHelp()
	//	{
	//		ContextHelp contextHelp = new ContextHelp();
	//		getContent().addComponent(contextHelp);
	//		//contextHelp.addHelpForComponent(proposals, "Source properties from the knowledgebase <insert id here> ....");
	//	}

	
	
	private Panel getGraphPanel(){
		
		 final int HEIGHT = 450;
		 final int WIDTH = 800;
		 final int NODESIZE = 100;
		 
		 Panel graphPanel = new Panel();
		 Layout graphLayout = new VerticalLayout();
		 Layout buttonLayout = getButtonLayout();
		 graphPanel.setContent(graphLayout);
		 graphPanel.setWidth("100%");
		 graphLayout.addComponent(buttonLayout);
		 
		 Cytoscape.createNewSession();	
		 String name = "MyName";
		 CyNetwork cyNetwork = Cytoscape.createNetwork(name, false);		
		 CyNetworkView cyNetworkView = Cytoscape.createNetworkView(cyNetwork);

		 cytographer = new Cytographer(cyNetwork, cyNetworkView, name, WIDTH, HEIGHT);
		 cytographer.setImmediate(true);
		 cytographer.setWidth(WIDTH + "px");
		 cytographer.setHeight(HEIGHT + "px");
		 cytographer.setTextVisible(true);		
		 cytographer.setNodeSize(NODESIZE, true);
				
		 // add nodes
		 cytographer.addNode("p.1", 100, 100);
		 cytographer.addNode("c.1", 100, 200);
		 cytographer.addNode("o.1", 100, 300);
		
		 cytographer.addNode("p.2", 200, 100);
		 cytographer.addNode("c.2", 200, 200);
		 cytographer.addNode("o.2", 200, 300);

		 // add edges
		 cytographer.createAnEdge( new String[]{"p.1","c.1","e1"});
		 cytographer.createAnEdge( new String[]{"p.2","c.1","e2"});
		 
		 cytographer.createAnEdge( new String[]{"c.1","o.1","e3"});
		 cytographer.createAnEdge( new String[]{"c.2","o.1","e4"});
		 
		 // set view
		 cyNetworkView.applyLayout(new ForceDirectedLayout());		
		 cytographer.fitToView();
		
		 // repaint
		 cytographer.repaintGraph();
		 graphLayout.addComponent(cytographer);
		 //.addListener(MouseEvents.ClickEvent.class, new DummyClickListener(), "click");
		 return graphPanel;		
	}
	
	public MetricPanel()
	{
		
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		layout.setSpacing(false);
		layout.setMargin(false);
		setContent(mainLayout);
		layout.setWidth("100%");
		mainLayout.addComponent(layout);
		
		final VerticalLayout accordionLayout = new VerticalLayout();
		layout.addComponent(accordionLayout);		
		final ProgressIndicator progress = new ProgressIndicator();
		progress.setIndeterminate(false);
		accordionLayout.addComponent(progress);
		// self config
		mainLayout.addComponent(manualMetricForm=new ManualMetricForm());
		selfconfig = new Button("Start SelfConfiguration");
		selfconfig.setEnabled(false);
		selfconfig.addListener(new SelfConfigClickListener(layout));
		mainLayout.addComponent(selfconfig);
		
		// accordion panel
		Panel accordionPanel = new Panel();
		accordionLayout.addComponent(accordionPanel);
		accordionPanel.setWidth("25em"); //$NON-NLS-1$
		
		// add graph panel		
		layout.addComponent(getGraphPanel());
		final Accordion accordion = new Accordion();		
		accordionPanel.addComponent(accordion);

		final VerticalLayout sourceLayout = new VerticalLayout();
		Tab sourceTab = accordion.addTab(sourceLayout,Messages.getString("MetricPanel.sourceproperties")); //$NON-NLS-1$

		final VerticalLayout targetLayout = new VerticalLayout();
		{Tab targetTab = accordion.addTab(targetLayout,Messages.getString("MetricPanel.targetproperties"));} //$NON-NLS-1$}

		{
			VerticalLayout functionLayout = new VerticalLayout();
			Tab functionTab = accordion.addTab(functionLayout,Messages.getString("MetricPanel.functions")); //$NON-NLS-1$
		}
		{
			VerticalLayout metricLayout = new VerticalLayout();
			Tab metricTab = accordion.addTab(metricLayout,Messages.getString("MetricPanel.metrics")); //$NON-NLS-1$
		}
		{
			VerticalLayout operatorLayout = new VerticalLayout();
			Tab operatorTab = accordion.addTab(operatorLayout,Messages.getString("MetricPanel.operators"));			 //$NON-NLS-1$
		}

		new Thread()
		{			
			@Override
			public void run()
			{
			//	performPropertyMapping();
				getAllProps();
				{
					for(String s : sourceProps) {
						final CheckBox check = new CheckBox(s, false);
					//	check.setCaption(s);
						check.addListener(new Property.ValueChangeListener() {							
							@Override
							public void valueChange(ValueChangeEvent event) {
								String prop = check.getCaption();
								if(check.booleanValue() && prop != null && prop.length()>0) {
									String s_abr=PrefixHelper.abbreviate(prop);
									sourceProps.add(s_abr);
									Configuration.getInstance().getSource().properties.add(s_abr);
									Configuration.getInstance().getSource().prefixes.put(PrefixHelper.getPrefixFromURI(s_abr), PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
									Configuration.getInstance().getSource().functions.put(s_abr, "lowercase");
									System.out.println("Adding source property: "+s_abr+"::::"+PrefixHelper.getPrefixFromURI(s_abr)+" -- "+PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
								} else {
									System.out.println("Tried to add a source false property");
								}
							}
						});
						sourceLayout.addComponent(check); //$NON-NLS-1$
					}

					for(String t : targetProps) {
						final CheckBox check = new CheckBox(t, false);
						check.setEnabled(true);
						check.addListener(new Property.ValueChangeListener() {							
							@Override
							public void valueChange(ValueChangeEvent event) {
								String prop = check.getCaption();
								if(check.booleanValue() && prop != null && prop.length()>0) {
									String s_abr=PrefixHelper.abbreviate(prop);
									Configuration.getInstance().getTarget().properties.add(s_abr);
									Configuration.getInstance().getTarget().prefixes.put(PrefixHelper.getPrefixFromURI(s_abr), PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
									Configuration.getInstance().getTarget().functions.put(s_abr, "lowercase");
									System.out.println("Adding target property: "+s_abr+"::::"+PrefixHelper.getPrefixFromURI(s_abr)+" -- "+PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
								} else {
									System.out.println("Tried to add a target false property");
								}
							}
						});
						targetLayout.addComponent(check);//$NON-NLS-1$
					} 
				}
				accordionLayout.removeComponent(progress);
				progress.setEnabled(false);
			}
		}.start();

		//		{
		//			//		setupContextHelp();
		//		}
		}

		private void performPropertyMapping() {
			Configuration config = Configuration.getInstance();
			config.getSource().properties.clear();
			config.getTarget().properties.clear();
			PropertyMapper propMapper = new PropertyMapper();
			String classSource = config.getSource().getClassOfendpoint();
			String classTarget = config.getTarget().getClassOfendpoint();
			if(classSource != null && classTarget != null) {
				showErrorMessage("Getting property mapping...");
				propMapping = propMapper.getPropertyMapping(config.getSource().endpoint,
						config.getTarget().endpoint, classSource, classTarget);
				for(String s : propMapping.map.keySet())
					for(Entry<String, Double> e : propMapping.map.get(s).entrySet()) {
						System.out.println(s + " - " + e.getKey());
						String s_abr=PrefixHelper.abbreviate(s);
						sourceProps.add(s_abr);
						config.getSource().properties.add(s_abr);
						config.getSource().prefixes.put(PrefixHelper.getPrefixFromURI(s_abr), PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
						System.out.println("Adding source property: "+s_abr+"::::"+PrefixHelper.getPrefixFromURI(s_abr)+" -- "+PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s_abr)));
						targetProps.add(PrefixHelper.abbreviate(e.getKey()));
						String t_abr=PrefixHelper.abbreviate(e.getKey());
						config.getTarget().properties.add(t_abr);
						config.getTarget().prefixes.put(PrefixHelper.getPrefixFromURI(t_abr), PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(t_abr)));
						System.out.println("Adding target property: "+t_abr+"::::"+PrefixHelper.getPrefixFromURI(t_abr)+" -- "+PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(t_abr)));
					}
			} else {
				showErrorMessage("Cannot perform automatic property mapping due to missing class specifications.");
			}		
		}
		
		private void getAllProps() {
			//for source
			KBInfo info = Configuration.getInstance().getSource();
			String className = info.restrictions.get(0).substring(info.restrictions.get(0).indexOf("rdf:type")+8);
			for(String prop : SPARQLHelper.properties(info.endpoint, info.graph, className)) {
				String s_abr=PrefixHelper.abbreviate(prop);
				sourceProps.add(s_abr);
			}
			//for target
			info = Configuration.getInstance().getTarget();
			className = info.restrictions.get(0).substring(info.restrictions.get(0).indexOf("rdf:type")+8);
			for(String prop : SPARQLHelper.properties(info.endpoint, info.graph, className)) {
				String s_abr=PrefixHelper.abbreviate(prop);
				targetProps.add(s_abr);
			}	
			//enable selfconfig
			selfconfig.setEnabled(true);
		}

		private void showErrorMessage(String message) {
			layout.setComponentError(new UserError(message));
		}

		public boolean isValid() {
			manualMetricForm.validate();
			if(manualMetricForm.isValid()) {
				Configuration.getInstance().setMetricExpression(manualMetricForm.metricTextField.getValue().toString());
				Configuration.getInstance().setAcceptanceThreshold(Double.parseDouble(manualMetricForm.thresholdTextField.getValue().toString()));
				return true;
			} else {
				manualMetricForm.setComponentError(new UserError("Please insert something..."));
			}
			return false;
		}
		
		public class SelfConfigClickListener implements Button.ClickListener {
			Layout l;
			public SelfConfigClickListener(Layout l) {
				this.l=l;
			}
			@Override
			public void buttonClick(ClickEvent event) {
				// add all properties
				for(String s : sourceProps) {
					Configuration.getInstance().getSource().properties.add(s);
					Configuration.getInstance().getSource().prefixes.put(PrefixHelper.getPrefixFromURI(s), PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s)));
					Configuration.getInstance().getSource().functions.put(s, "");
				}
				for(String s : targetProps) {
					Configuration.getInstance().getTarget().properties.add(s);
					Configuration.getInstance().getTarget().prefixes.put(PrefixHelper.getPrefixFromURI(s), PrefixHelper.getURI(PrefixHelper.getPrefixFromURI(s)));
					Configuration.getInstance().getTarget().functions.put(s, "");
				}
				// run selfconfig
				l.removeAllComponents();
				Refresher refresher = new Refresher();
				SelfConfigRefreshListener listener = new SelfConfigRefreshListener();
				refresher.addListener(listener);
				addComponent(refresher);
				
				final ProgressIndicator indicator = new ProgressIndicator();
				indicator.setCaption("Progress");
				l.addComponent(indicator);
				indicator.setImmediate(true);
				
				final Panel stepPanel = new Panel("Starting self configuration");
				l.addComponent(stepPanel);
				
				new Thread() {
					public void run() {
						
						float steps = 5f;
						indicator.setValue(new Float(1f/steps));
						indicator.requestRepaint();
						stepPanel.setCaption("Getting source cache...");
						HybridCache sourceCache = HybridCache.getData(Configuration.getInstance().getSource());
						indicator.setValue(new Float(2f/steps));
						indicator.requestRepaint();
						stepPanel.setCaption("Getting target cache...");
						HybridCache targetCache = HybridCache.getData(Configuration.getInstance().getTarget());
						indicator.setValue(new Float(3f/steps));
						stepPanel.setCaption("Performing self configuration...");
						MeshBasedSelfConfigurator bsc = new MeshBasedSelfConfigurator(sourceCache, targetCache, 0.6, 0.5);
					    List<SimpleClassifier> cp = bsc.getBestInitialClassifiers();						
						indicator.setValue(new Float(4f/steps));
						stepPanel.setCaption("Performed self configuration:");
					  	for(SimpleClassifier cl : cp) {
					  		System.out.println(cl);
					  	}
						
					}
				}.start();
			}			
		}
		
		private Layout getButtonLayout() {
			Layout l = new HorizontalLayout();
			Button addPropertyButton = new Button("Add Property");	
			addPropertyButton.addListener(new Button.ClickListener() {			
				@Override
				public void buttonClick(ClickEvent event) {
					//TODO Window to configure
					cytographer.addNode("p.rdfs:label", 100, 100);
					cytographer.repaintGraph();
				}
			});
			l.addComponent(addPropertyButton);
			Button addMeasureButton = new Button("Add Measure");
			addMeasureButton.addListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					cytographer.addNode("m.trigram", 100, 100);
					cytographer.repaintGraph();
				}
			});
			l.addComponent(addMeasureButton);
			return l;
		}
		
		public class SelfConfigRefreshListener implements RefreshListener
		{
			boolean running = true; 
			private static final long serialVersionUID = -8765221895426102605L;		    
			@Override public void refresh(final Refresher source)	{if(!running) {removeComponent(source);source.setEnabled(false);}}
		}
	}
