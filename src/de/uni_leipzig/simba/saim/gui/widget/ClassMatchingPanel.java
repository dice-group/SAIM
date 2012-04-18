package de.uni_leipzig.simba.saim.gui.widget;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.learning.query.ClassMapper;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.Pair;
import de.uni_leipzig.simba.saim.util.SortedMapping;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class ClassMatchingPanel extends Panel
{	
	protected static final boolean CACHING	= true;
	Configuration config = Configuration.getInstance();
	final ComboBox suggestionComboBox = new ComboBox();
	public ClassMatchingForm sourceClassForm;
	public ClassMatchingForm targetClassForm;
	Cache cache = null;
	
	public void close()
	{
		if(cache != null)
		{							
//			Cache cache = CacheManager.getInstance().getCache("classmatching");
			cache.flush();
			cache = null;
		}
	}
	
	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getContent().addComponent(contextHelp);
		contextHelp.addHelpForComponent(suggestionComboBox, Messages.getString("classpairsfromlimes")); //$NON-NLS-1$
	}

	public ClassMatchingPanel()
	{
		setContent(new VerticalLayout());

		//FormLayout layout = new FormLayout(); // have the label to the right of the combobox and not on top
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(false);
		layout.setWidth("100%");		 //$NON-NLS-1$
		final ProgressIndicator progress = new ProgressIndicator();
		progress.setSizeUndefined();
		final Label suggestionLabel = new Label(Messages.getString("suggestions"));		 //$NON-NLS-1$
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
		{
			suggestionComboBox.setEnabled(false);
			// set listener in the thread because the programmatical select must not trigger a select in the class forms because
			// the user may have already entered something there
			suggestionComboBox.addListener(new ValueChangeListener() {								
				@Override
				public void valueChange(ValueChangeEvent event) {
					//get Value
					@SuppressWarnings("unchecked")
					Entry<Double, Pair<String>> entry = (Entry<Double, Pair<String>>) suggestionComboBox.getValue();
					sourceClassForm.addItem(entry.getValue().getA(),true);
					targetClassForm.addItem(entry.getValue().getB(),true);

					sourceClassForm.requestRepaint();
					targetClassForm.requestRepaint();
				}
			});
			new Thread()
			{
				
				@SuppressWarnings("unchecked")
				@Override
				public void run()
				{
					Configuration config = Configuration.getInstance();
					Refresher refresher = new Refresher();
					SuggestionsRefreshListener listener = new SuggestionsRefreshListener();
					refresher.addListener(listener);
					addComponent(refresher);

					Mapping classMatching = null;

					ClassMapper classMapper = new ClassMapper(10);
					if(CACHING)
					{	
						if(cache == null) 
							{cache = CacheManager.getInstance().getCache("classmatching");}
						List<Object> parameters = Arrays.asList(new Object[] {config.getSource().endpoint,config.getTarget().endpoint,config.getSource().id,config.getTarget().id});
						try {
							if(cache.isKeyInCache(parameters))
							{		
								classMatching = new Mapping();
								
								classMatching.map = ((HashMap<String,HashMap<String,Double>>) cache.get(parameters).getValue());
								System.out.println("loading map of size "+classMatching.map.size());
								System.out.println("cache hit");
							}
						}catch(Exception e){}
					}									
					if(classMatching==null)
					{
						System.out.println("cache miss");
						classMatching = classMapper.getMappingClasses(config.getSource().endpoint, config.getTarget().endpoint, config.getSource().id, config.getTarget().id);
						if(CACHING)
						{
							cache = CacheManager.getInstance().getCache("classmatching");
							if(cache.getStatus()==net.sf.ehcache.Status.STATUS_UNINITIALISED)
								{cache.initialise();}					
							List<Object> parameters = Arrays.asList(new Object[] {config.getSource().endpoint,config.getTarget().endpoint,config.getSource().id,config.getTarget().id});
							System.out.println("cache saving map of size "+classMatching.map.size());
							cache.put(new Element(parameters,classMatching.map));
							cache.flush();							
						}
					}
					if(classMatching.map.size()==0)
					{
						suggestionLabel.setCaption(Messages.getString("ClassMatchingPanel.nosuggestionsfound"));
					}
					else
					{
						suggestionComboBox.removeAllItems();
						SortedMapping sorter = new SortedMapping(classMatching);
						for(Entry<Double, Pair<String>> e: sorter.sort().descendingMap().entrySet()) {
							// need to swap a and b
							String b = e.getValue().getA();
							e.getValue().setA(e.getValue().getB());
							e.getValue().setB(b);
							suggestionComboBox.addItem(e);
							suggestionComboBox.select(e);
						}
						//					for(String class1 : sugg.map.keySet())
						//						for(Entry<String, Double> class2 : sugg.map.get(class1).entrySet()) {
						//							suggestionComboBox.addItem(class1+" - "+class2.getKey()+" : "+class2.getValue());
						//						}

						suggestionComboBox.setVisible(true);
						suggestionComboBox.setEnabled(true);
						suggestionComboBox.setNullSelectionAllowed(false);					
						suggestionComboBox.setTextInputAllowed(false);
						{
							Entry<Double, Pair<String>> entry = (Entry<Double, Pair<String>>) suggestionComboBox.getItemIds().iterator().next(); 
							suggestionComboBox.select(entry);
							sourceClassForm.addItem(entry.getValue().getA(),false);
							targetClassForm.addItem(entry.getValue().getB(),false);
						}


						// set listener in the thread because the programmatical select must not trigger a select in the class forms because
						// the user may have already entered something there
						suggestionComboBox.addListener(new ValueChangeListener() {								
							@Override
							public void valueChange(ValueChangeEvent event) {
								//get Value								
								Entry<Double, Pair<String>> entry = (Entry<Double, Pair<String>>) suggestionComboBox.getValue();
								sourceClassForm.addItem(entry.getValue().getA(),true);
								targetClassForm.addItem(entry.getValue().getB(),true);

								sourceClassForm.requestRepaint();
								targetClassForm.requestRepaint();
							}
						});
						System.out.println("suggested enabled: "+suggestionComboBox.size()+" items");					 //$NON-NLS-1$ //$NON-NLS-2$
					}
					progress.setEnabled(false);
					removeComponent(progress);
					listener.running=false;					
				}
			}.start();
			sourceClassForm = new ClassMatchingForm(Messages.getString("ClassMatchingPanel.sourceclass"), config.getSource());
			targetClassForm = new ClassMatchingForm(Messages.getString("ClassMatchingPanel.targetclass"), config.getTarget());

			HorizontalLayout hori = new HorizontalLayout();
			hori.setWidth("100%"); //$NON-NLS-1$

			sourceClassForm.setRequired(true);
			targetClassForm.setRequired(true);

			hori.addComponent(sourceClassForm);
			hori.addComponent(targetClassForm);
			this.getContent().addComponent(hori);
			// add Listener to set Items in the ClassMatchingForm

		}
		setupContextHelp();
	}

	public class SuggestionsRefreshListener implements RefreshListener
	{
		boolean running = true; 
		private static final long serialVersionUID = -8765221895426102605L;		    
		@Override public void refresh(final Refresher source)	{if(!running) {removeComponent(source);source.setEnabled(false);}}
	}	
}