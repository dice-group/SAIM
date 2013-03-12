package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.jonatan.contexthelp.ContextHelp;
import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.learning.query.DefaultClassMapper;
import de.uni_leipzig.simba.learning.query.LabelBasedClassMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.Pair;
import de.uni_leipzig.simba.saim.gui.widget.form.ClassMatchingForm;
import de.uni_leipzig.simba.saim.util.SortedMapping;
/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class ClassMatchingPanel extends Panel
{
	private final Messages messages;
	public static final boolean CACHING	= true;
	Configuration config;// = Configuration.getInstance();

	Label suggestionLabel;
	ProgressIndicator progress;
	Refresher refresher;
	SuggestionsRefreshListener listener;

	ComboBox suggestionComboBox = new ComboBox();
	ValueChangeListener comboListener;
	Button computeStringBased;
	Button computeLinkBased;

	public ClassMatchingForm sourceClassForm;
	public ClassMatchingForm targetClassForm;
	Cache cache = null;
	static transient final Logger logger = LoggerFactory.getLogger(ClassMatchingPanel.class);
	/** Thread computing auto matches. Maybe replaced on a user interaction with another one.*/
	Thread computer;

	public void close()
	{
		if(cache != null)
		{
//			Cache cache = CacheManager.getInstance().getCache("classmatching");
			cache.flush();
			cache = null;
		}
	}
	/**
	 * Sets Up the ContextHelp.
	 */
	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getContent().addComponent(contextHelp);
		contextHelp.addHelpForComponent(suggestionComboBox, messages.getString("classpairsfromlimes")); //$NON-NLS-1$
	}

	public ClassMatchingPanel(final Messages messages) {this.messages=messages;}

	@Override
	public void attach() {
		setContent(new VerticalLayout());
		this.config=((SAIMApplication)getApplication()).getConfig();

		// Buttons to call a new computation of suggestions
		computeStringBased = new Button(messages.getString("ClassMatchingPanel.0")); //$NON-NLS-1$
		computeLinkBased = new Button(messages.getString("ClassMatchingPanel.1")); //$NON-NLS-1$
		computeStringBased.addListener(new ComputeButtonClickListener(true));
		computeLinkBased.addListener(new ComputeButtonClickListener(false));
		HorizontalLayout bLayout = new HorizontalLayout();
		bLayout.addComponent(computeStringBased);
		bLayout.addComponent(computeLinkBased);
		getContent().addComponent(bLayout);

		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(false);
		layout.setWidth("100%");	 //$NON-NLS-1$
		progress = new ProgressIndicator();
		progress.setSizeUndefined();
		suggestionLabel = new Label("");//messages.getString("suggestions"));		 //$NON-NLS-1$
		suggestionLabel.setSizeUndefined();
		layout.addComponent(suggestionLabel);
		layout.addComponent(progress);
		progress.setIndeterminate(true);
		suggestionComboBox.setWidth("100%");		 //$NON-NLS-1$
		suggestionComboBox.setImmediate(true);
		layout.addComponent(suggestionComboBox);
		layout.setExpandRatio(suggestionComboBox, 1f);
		suggestionComboBox.setVisible(false);
		this.addComponent(layout);

		suggestionComboBox.setEnabled(false);
		comboListener = new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				//get Value
				@SuppressWarnings("unchecked")
				Pair<String> entry = (Pair<String>) suggestionComboBox.getValue();
				sourceClassForm.addItem(entry.getA(),true);
				targetClassForm.addItem(entry.getB(),true);

				sourceClassForm.requestRepaint();
				targetClassForm.requestRepaint();
			}
		};
		sourceClassForm = new ClassMatchingForm(messages.getString("ClassMatchingPanel.sourceclass"), config.getSource(), config.sourceModel); //$NON-NLS-1$
		targetClassForm = new ClassMatchingForm(messages.getString("ClassMatchingPanel.targetclass"), config.getTarget(), config.targetModel); //$NON-NLS-1$

		HorizontalLayout hori = new HorizontalLayout();
		hori.setWidth("100%"); //$NON-NLS-1$
		sourceClassForm.setRequired(true);
		targetClassForm.setRequired(true);
		hori.addComponent(sourceClassForm);
		hori.addComponent(targetClassForm);
		this.getContent().addComponent(hori);
		// add Listener to set Items in the ClassMatchingForm

		refresher = new Refresher();
		listener = new SuggestionsRefreshListener();
		refresher.addListener(listener);
		addComponent(refresher);
		computer = new ComputeClassMapping(true);
//		/*FIXME some what redundant code: we may enhance the ComputeClassMapping with
//		 * caching mechanisms: need to memorize if the mapping is based on strings or
//		 * the other algorithm
//		 */
//		computer = new Thread()	{
//			@SuppressWarnings("unchecked")
//			@Override
//			public void run()
//			{
//				Mapping classMatching = null;
//				if(CACHING)
//				{
//					if(cache == null)
//						{cache = CacheManager.getInstance().getCache("classmatching");}
//					List<Object> parameters = Arrays.asList(new Object[] {config.getSource().endpoint,config.getTarget().endpoint,config.getSource().id,config.getTarget().id});
//					try {
//						if(cache.isKeyInCache(parameters)) {
//							classMatching = new Mapping();
//							classMatching.map = ((HashMap<String,HashMap<String,Double>>) cache.get(parameters).getValue());
//							logger.info("Class Mapping Cache hit: "+"loading map of size "+classMatching.map.size());
//						}
//					}catch(Exception e){}
//				}
//				if(classMatching==null) {
//					logger.info("Class Mapping Cache miss.");
//					LabelBasedClassMapper mapper = new LabelBasedClassMapper();
//					classMatching = mapper.getEntityMapping(config.getSource().endpoint, config.getTarget().endpoint, config.getSource().id, config.getTarget().id);
////					DefaultClassMapper classMapper = new DefaultClassMapper(10);
////					classMatching = classMapper.getEntityMapping(config.getSource().endpoint, config.getTarget().endpoint, config.getSource().id, config.getTarget().id).reverseSourceTarget();
//					if(CACHING)	{
//						cache = CacheManager.getInstance().getCache("classmatching");
//						if(cache.getStatus()==net.sf.ehcache.Status.STATUS_UNINITIALISED)
//							{cache.initialise();}
//						List<Object> parameters = Arrays.asList(new Object[] {config.getSource().endpoint,config.getTarget().endpoint,config.getSource().id,config.getTarget().id});
//						System.out.println("cache saving map of size "+classMatching.map.size());
//						cache.put(new Element(parameters,classMatching.map));
//						cache.flush();
//					}
//				}
//				display(classMatching);
//				progress.setEnabled(false);
////				removeComponent(progress);
////				listener.running=false;
//			}
//		};
		computer.start();
		setupContextHelp();
	}

	/**
	 * Method to display auto suggestions once they were computed.
	 * @param classMapping
	 */
	public void display(Mapping classMapping) {
		int matchesFound = 0;
		logger.info("Show Match" + classMapping); //$NON-NLS-1$
		suggestionComboBox.removeListener(comboListener);
		suggestionComboBox.removeAllItems();
		if(classMapping!=null && classMapping.map.size()>0) {
			for(Entry<String, HashMap<String, Double>> e : classMapping.map.entrySet()) {
				for(Entry<String, Double> sE : e.getValue().entrySet()) {
					if(sE.getValue()>=0) {
						matchesFound++;
					}
				}
			}
			if(suggestionComboBox != null)
				suggestionComboBox.removeAllItems();
			else
				suggestionComboBox = new ComboBox();
			SortedMapping sorter = new SortedMapping(classMapping);
			
			for(Entry<Double, List<Pair<String>>> e: sorter.sort().entrySet()) {
				for(Pair<String> pair : e.getValue()) {
					if(e.getKey()>0) {
						Object itemId = suggestionComboBox.addItem(pair);
						suggestionComboBox.setItemCaption(itemId, e.getValue()+"= "+pair);
						suggestionComboBox.select(itemId);
					} else {
						matchesFound--;
					}
				}				
			}
			suggestionLabel.setCaption(matchesFound + messages.getString("ClassMatchingPanel.5")); //$NON-NLS-1$
			suggestionComboBox.setVisible(true);
			suggestionComboBox.setEnabled(true);
			suggestionComboBox.setNullSelectionAllowed(false);
			suggestionComboBox.setTextInputAllowed(false);
			suggestionComboBox.addListener(comboListener);
			{// auto select first item
				@SuppressWarnings("unchecked")
				Pair<String> entry = (Pair<String>) suggestionComboBox.getItemIds().iterator().next();
				suggestionComboBox.select(entry);
				sourceClassForm.addItem(entry.getA(),false);
				targetClassForm.addItem(entry.getB(),false);
			}
		}
	}

	/**
	 * Listener for buttons to compute mappings again.
	 * @author Lyko
	 */
	public class ComputeButtonClickListener implements Button.ClickListener {
		boolean simple = true;
		public ComputeButtonClickListener(boolean simple) {
			this.simple=simple;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			if(computer != null) { // if a thread is already computing, stop and replace it
				computer.stop();
				computer = new ComputeClassMapping(simple);
				computer.start();
			}
		}
	}

	public class SuggestionsRefreshListener implements RefreshListener
	{
		boolean running = true;
		private static final long serialVersionUID = -8765221895426102605L;
		@Override public void refresh(final Refresher source)	{if(!running) {removeComponent(source);source.setEnabled(false);}}
	}

	/**
	 * Thread to compute class mappings.
	 * @author Klaus Lyko
	 */
	public class ComputeClassMapping extends Thread {
		boolean stringBased = true;
		public ComputeClassMapping(boolean stringBased) {
			this.stringBased = stringBased;
		}
		@Override
		public void run() {
			progress.setEnabled(true);
			// Cache parameter
			List<Object> parameters = Arrays.asList(new Object[] {config.getSource().endpoint,
					config.getTarget().endpoint,
					config.getSource().id,
					config.getTarget().id,
					stringBased});

			Mapping classMapping = null;
			if(CACHING)
			{
				if(cache == null)
					{cache = CacheManager.getInstance().getCache("classmatching");} //$NON-NLS-1$

				try {
					if(cache.isKeyInCache(parameters)) {
						classMapping = new Mapping();
						classMapping.map = ((HashMap<String,HashMap<String,Double>>) cache.get(parameters).getValue());
						logger.info("Class Mapping Cache hit: "+"loading map of size "+classMapping.map.size()); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}catch(Exception e){}
			}
			if(classMapping == null) {
				if(stringBased) {
					//FIXME are returned in the right order
					LabelBasedClassMapper mapper = new LabelBasedClassMapper();
					mapper.setSourceModel(config.sourceModel);
					mapper.setTargetModel(config.targetModel);
					classMapping = mapper.getEntityMapping(config.getSource().endpoint, config.getTarget().endpoint);
				} else {
					//FIXME are returned in the wrong order: call reverseSourceTarget().
					DefaultClassMapper classMapper = new DefaultClassMapper(10);
					classMapper.setSourceModel(config.sourceModel);
					classMapper.setTargetModel(config.targetModel);
					classMapping = classMapper.getEntityMapping(config.getSource().endpoint, config.getTarget().endpoint, getNameSpace(config.getSource()), getNameSpace(config.getTarget())).reverseSourceTarget();
				}
				if(CACHING)	{
					cache = CacheManager.getInstance().getCache("classmatching"); //$NON-NLS-1$
					if(cache.getStatus()==net.sf.ehcache.Status.STATUS_UNINITIALISED)
						{cache.initialise();}
					logger.info("Cache saving class matching map of size " + classMapping.map.size()); //$NON-NLS-1$
					cache.put(new Element(parameters, classMapping.map));
					cache.flush();
				}
			}
			display(classMapping);
			progress.setEnabled(false);
		}
		
		private String getNameSpace(KBInfo info) {

			String ns = info.id.replaceAll("\\(.*\\)", "").trim();
			if(ns.length()>0)
				return ns;
			else
				return info.id;
					
			
		}
	}
}
