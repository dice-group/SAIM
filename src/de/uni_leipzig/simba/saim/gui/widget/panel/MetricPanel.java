package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.cytographer.Cytographer;
import org.vaadin.cytographer.GraphProperties;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import csplugins.layout.algorithms.force.ForceDirectedLayout;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import cytoscape.visual.VisualPropertyType;
import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.Listener.MetricPanelListeners;
import de.uni_leipzig.simba.saim.gui.widget.form.ManualMetricForm;
import de.uni_leipzig.simba.saim.gui.widget.form.PreprocessingForm;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class MetricPanel extends Panel
{
	ManualMetricForm manualMetricForm;
	private static final long	serialVersionUID	= 6766679517868840795L;
	private static final boolean CACHING = true;
	Mapping propMapping;
	VerticalLayout mainLayout = new VerticalLayout();
	HorizontalLayout layout = new HorizontalLayout();
	HorizontalLayout buttonLayout = new HorizontalLayout();
	
	Set<String> sourceProps = new HashSet<String>();
	Set<String> targetProps = new HashSet<String>();

	final static Logger logger = LoggerFactory.getLogger(MetricPanel.class);
	
	Button selfConfigButton;
	Button learnButton;
	Button startMapping;
	
	Cytographer cytographer;
	CyNetworkView cyNetworkView;
	final VerticalLayout sourceLayout =  new VerticalLayout();
	final VerticalLayout targetLayout =  new VerticalLayout();
	final VerticalLayout metricsLayout =  new VerticalLayout();
	final VerticalLayout operatorsLayout =  new VerticalLayout();

	public MetricPanel()
	{
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
//		mainLayout.setWidth("100%");
//		mainLayout.setHeight("100%");
//		
		final VerticalLayout accordionLayout = new VerticalLayout();
//		accordionLayout.setHeight("100%");
		layout.addComponent(accordionLayout);
//		for(int i=0;i<5;i++) {accordionLayout.addComponent(new Button());}
		
		layout.setSpacing(false);
		layout.setMargin(false);
		setContent(mainLayout);
//		layout.setWidth("100%");
		mainLayout.addComponent(layout);
//		mainLayout.setComponentAlignment(layout, Alignment.TOP_LEFT);
		
//		layout.setComponentAlignment(accordionLayout, Alignment.TOP_LEFT);
		final ProgressIndicator progress = new ProgressIndicator();
		progress.setIndeterminate(false);
		accordionLayout.addComponent(progress);
		// self config
		mainLayout.addComponent(manualMetricForm=new ManualMetricForm());
		
		//buttons		
		mainLayout.addComponent(getButtonLayout());

		// accordion panel
		Panel accordionPanel = new Panel();
		accordionPanel.setHeight("100%");
		accordionLayout.addComponent(accordionPanel);
		accordionPanel.setWidth("25em");

		final Accordion accordion = new Accordion();		
		accordion.setHeight("100%");

		accordionPanel.addComponent(accordion);
		
		accordion.addTab(sourceLayout,Messages.getString("MetricPanel.sourceproperties"));		
		accordion.addTab(targetLayout,Messages.getString("MetricPanel.targetproperties"));
		accordion.addTab(metricsLayout,Messages.getString("MetricPanel.metrics")); 
		accordion.addTab(operatorsLayout,Messages.getString("MetricPanel.operators"));	
				
		// add Cytographer
		layout.addComponent(getCytographer());
		
		new Thread(){			
			@Override
			public void run(){
				//	performPropertyMapping();
				getAllProps();

				for(String s : sourceProps) {
					final Label check = new Label(s);
					sourceLayout.addComponent(check); 
				}
						
				for(String t : targetProps) {
					final Label check = new Label(t);
					targetLayout.addComponent(check);
				}
				accordionLayout.removeComponent(progress);
				progress.setEnabled(false);
			}
		}.start();
		metricsLayout.addComponent( new Label("a metric"));
		operatorsLayout.addComponent( new Label("an operator"));
		
		sourceLayout.addListener(new AccordionLayoutClickListener(cytographer,cyNetworkView,GraphProperties.Shape.SOURCE));
		targetLayout.addListener(new AccordionLayoutClickListener(cytographer,cyNetworkView,GraphProperties.Shape.TARGET));
		metricsLayout.addListener(new AccordionLayoutClickListener(cytographer,cyNetworkView,GraphProperties.Shape.METRIC));
		operatorsLayout.addListener(new AccordionLayoutClickListener(cytographer,cyNetworkView,GraphProperties.Shape.OPERATOR));
		
		this.checkButtons();
	}
	private Cytographer getCytographer(){
		
		final int HEIGHT = 450;
		final int WIDTH = 800;
		final int NODESIZE = 100;
		final double EDGE_LABEL_OPACITY = 0d;
		
		Cytoscape.createNewSession();	
		Cytoscape.getVisualMappingManager().getVisualStyle().getGlobalAppearanceCalculator().setDefaultBackgroundColor(Color.WHITE);
		Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_COLOR,Color.BLACK);
		Cytoscape.getVisualMappingManager().getVisualStyle().getEdgeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.EDGE_LABEL_OPACITY,EDGE_LABEL_OPACITY);
		Cytoscape.getVisualMappingManager().getVisualStyle().getNodeAppearanceCalculator().getDefaultAppearance().set(VisualPropertyType.NODE_SIZE, NODESIZE);
		
		String name = "MyName";
		CyNetwork cyNetwork = Cytoscape.createNetwork(name, false);		
		cyNetworkView = Cytoscape.createNetworkView(cyNetwork);

		cytographer = new Cytographer(cyNetwork, cyNetworkView, name, WIDTH, HEIGHT,SAIMApplication.getInstance().getMainWindow());
		cytographer.setImmediate(true);
		cytographer.setWidth(WIDTH + "px");
		cytographer.setHeight(HEIGHT + "px");
		cytographer.setTextVisible(true);		
		cytographer.setNodeSize(NODESIZE, true);	
		
		return cytographer;		
	}

	
	private void getAllProps() {
		Configuration config = Configuration.getInstance();
//		if(config.isLocal) {
//			logger.info("Local data - using specified properties");
	
			if( config.getSource() != null && config.getSource().properties != null && config.getSource().properties.size()>0 &&
					config.getTarget() != null && config.getTarget().properties != null && config.getTarget().properties.size()>0) {
				for(String prop : config.getSource().properties) {
					if(prop.trim().length()>0) {
						String s_abr=PrefixHelper.abbreviate(prop);
						sourceProps.add(s_abr);
					}
				}
				
				for(String prop : config.getTarget().properties) {
					if(prop.trim().length()>0) {
						String s_abr=PrefixHelper.abbreviate(prop);
						targetProps.add(s_abr);
					}
				}
				selfConfigButton.setEnabled(true);
			}			
			return;
	}


//	/**
//	 * Little helper function to retrieve classes out of restrictions of the LIMES SPEC. Whereas, a
//	 * class is in a restriction of the format "?var rdf:type <class>".
//	 * @param ep
//	 * @return
//	 */
//	private String getClassOfEndpoint(KBInfo ep) {
//		for(String rest : ep.restrictions) {
//			if(rest.matches(".* rdf:type .*"))
//				return rest.substring(rest.indexOf("rdf:type")+8).replaceAll("<", "").replaceAll(">", "").trim();
//		}
//		return null;
//	}

	private void showErrorMessage(String message) {
		layout.setComponentError(new UserError(message));
	}

	/**
	 * To decide whether we can proceed to the execution panel
	 * @return
	 */
	public boolean isValid() {
		try{
			manualMetricForm.validate();
		}catch(InvalidValueException e) {}
		if(manualMetricForm.isValid()) {
			Configuration.getInstance().setMetricExpression(manualMetricForm.metricTextField.getValue().toString());
			Configuration.getInstance().setAcceptanceThreshold(Double.parseDouble(manualMetricForm.thresholdTextField.getValue().toString()));
			return true;
		} else {
		//	manualMetricForm.setComponentError();
		}
		return false;
	}
	
	public Layout getButtonLayout() {
		selfConfigButton = new Button("Start SelfConfiguration");
		selfConfigButton.setEnabled(false);
		selfConfigButton.addListener(new MetricPanelListeners.SelfConfigClickListener());
		this.learnButton = new Button("Learn Metric");
		learnButton.setEnabled(false);
		learnButton.addListener(new MetricPanelListeners.LearnClickListener());
		this.startMapping = new Button("Start Mapping process");
		startMapping.setEnabled(false);
		startMapping.addListener(new MetricPanelListeners.StartMappingListener());
		buttonLayout.addComponent(selfConfigButton);
		buttonLayout.addComponent(learnButton);
		buttonLayout.addComponent(startMapping);
		return buttonLayout;		
	}
	
	/**
	 * Checks whether the Buttons (selfconfig, learning and startMapping) could be activated.
	 */
	public void checkButtons() {
		Configuration config = Configuration.getInstance();
		if( config.getSource() != null && config.getSource().properties != null && config.getSource().properties.size()>0 &&
				config.getTarget() != null && config.getTarget().properties != null && config.getTarget().properties.size()>0) {
			selfConfigButton.setEnabled(true);
			if(config.getMetricExpression() != null && config.getMetricExpression().length()>0) {
				learnButton.setEnabled(true);
				startMapping.setEnabled(true);

				manualMetricForm.metricTextField.setValue(config.getMetricExpression());
				manualMetricForm.thresholdTextField.setValue(config.getAcceptanceThreshold());
			}
		}
	}
}


/**Listener to react on clicks in the accordion panel.*/
class AccordionLayoutClickListener implements LayoutClickListener{

	private static final long serialVersionUID = -3498649095113131161L;
	private Cytographer cytographer;
	private CyNetworkView cyNetworkView;
	private GraphProperties.Shape shape;
	
	public AccordionLayoutClickListener(Cytographer cytographer, CyNetworkView cyNetworkView,GraphProperties.Shape shape){
		this.cytographer = cytographer;
		this.cyNetworkView = cyNetworkView;
		this.shape = shape;
	}
	
	@Override
	public void layoutClick(LayoutClickEvent event) {
		// its left button
		if(event.getButtonName().equalsIgnoreCase("left") && event.getClickedComponent() instanceof Label ){
			String label = ((Label)event.getClickedComponent()).getValue().toString();
			switch(shape){
			case SOURCE :{
				String pref = Configuration.getInstance().getSource().var.replaceAll("\\?", "");
				cytographer.addNode(pref+"."+label, 0, 0, shape);
				addProperty(label, Configuration.getInstance().getSource());
				break;
			}
			case TARGET : {
				String pref = Configuration.getInstance().getTarget().var.replaceAll("\\?", "");
				cytographer.addNode(pref+"."+label, 0, 0, shape);
				addProperty(label, Configuration.getInstance().getTarget());
				break;
			}
			default :
					cytographer.addNode(label, 0, 0, shape);
			}

			cyNetworkView.applyLayout(new ForceDirectedLayout());		
			cytographer.fitToView();
			// repaint
			cytographer.repaintGraph();
		}
	}
	
	/**
	 * Method to add Properties to according KBInfo. 
	 * @param s URI of the property. May or may not be abbreviated.
	 * @param info KBInfo of endpoint property belongs to.
	 */
	private void addProperty(String s, KBInfo info) {
		String prop;
		
		if(s.startsWith("http:")) {//do not have a prefix, so we generate one
			PrefixHelper.generatePrefix(s);
			prop = PrefixHelper.abbreviate(s);
		} else {// have the prefix already
			prop = s;
			s = PrefixHelper.expand(s);
		}
		if(!info.properties.contains(prop))
			info.properties.add(prop);
//		info.functions.put(prop, "");
		//show preprocessing window
		Window sub = new Window("Define property "+prop);
		sub.setModal(true);
		sub.addComponent(new PreprocessingForm(info, prop));
		SAIMApplication.getInstance().getMainWindow().addWindow(sub);
				
		String base = PrefixHelper.getBase(s);
		info.prefixes.put(PrefixHelper.getPrefix(base), PrefixHelper.getURI(PrefixHelper.getPrefix(base)));
	
		LoggerFactory.getLogger(AccordionLayoutClickListener.class).info(info.var+": adding property: "+prop+" with prefix "+PrefixHelper.getPrefix(base)+" - "+PrefixHelper.getURI(PrefixHelper.getPrefix(base)));
	}
}