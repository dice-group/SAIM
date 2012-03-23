package de.uni_leipzig.simba.saim.gui.widget;

import java.util.Map.Entry;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
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
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Pair;
import de.uni_leipzig.simba.saim.util.SortedMapping;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
@SuppressWarnings("serial")
public class ClassMatchingPanel extends Panel
{	
	Configuration config = Configuration.getInstance();
	final ComboBox suggestionComboBox = new ComboBox();
	private ClassMatchingForm sourceClassForm;
	private ClassMatchingForm targetClassForm;

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
		Label suggestionLabel = new Label(Messages.getString("suggestions"));		 //$NON-NLS-1$
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
			new Thread()
			{
				@Override
				public void run()
				{
					Configuration config = Configuration.getInstance();
					Refresher refresher = new Refresher();
					SuggestionsRefreshListener listener = new SuggestionsRefreshListener();
					refresher.addListener(listener);
					addComponent(refresher);

					ClassMapper classMapper = new ClassMapper();
					Mapping sugg = classMapper.getMappingClasses(config.getSource().endpoint, config.getTarget().endpoint, config.getSource().id, config.getTarget().id);

					if(sugg.size()==0)
					{
						Label errorLabel = new Label(Messages.getString("ClassMatchingPanel.nosuggestionsfound"));
						layout.addComponent(errorLabel);
						
					}
					else
					{
						suggestionComboBox.removeAllItems();
						SortedMapping sorter = new SortedMapping(sugg);
						for(Entry<Double, Pair<String>> e: sorter.sort().descendingMap().entrySet()) {
							suggestionComboBox.addItem(e);
						}
						//					for(String class1 : sugg.map.keySet())
						//						for(Entry<String, Double> class2 : sugg.map.get(class1).entrySet()) {
						//							suggestionComboBox.addItem(class1+" - "+class2.getKey()+" : "+class2.getValue());
						//						}

						progress.setEnabled(false);
						removeComponent(progress);
						suggestionComboBox.setVisible(true);
						suggestionComboBox.setEnabled(true);
						suggestionComboBox.setNullSelectionAllowed(false);					
						suggestionComboBox.setTextInputAllowed(false);
						suggestionComboBox.select(0);
						System.out.println("suggested enabled: "+suggestionComboBox.size()+" items");					 //$NON-NLS-1$ //$NON-NLS-2$
					}
					listener.running=false;					
				}
			}.start();
			sourceClassForm = new ClassMatchingForm(Messages.getString("ClassMatchingPanel.sourceclass"), config.getSource());
			targetClassForm = new ClassMatchingForm(Messages.getString("ClassMatchingPanel.targetclass"), config.getTarget());
			HorizontalLayout hori = new HorizontalLayout();
			hori.setWidth("100%"); //$NON-NLS-1$
			hori.addComponent(sourceClassForm);
			hori.addComponent(targetClassForm);
			this.getContent().addComponent(hori);
			// add Listener to set Items in the ClassMatchingForm
			suggestionComboBox.addListener(new ValueChangeListener() {								
				@Override
				public void valueChange(ValueChangeEvent event) {
					//get Value
					Entry<Double, Pair<String>> entry = (Entry<Double, Pair<String>>) suggestionComboBox.getValue();
					sourceClassForm.addItem(entry.getValue().getA());
					targetClassForm.addItem(entry.getValue().getB());
					sourceClassForm.requestRepaint();
					targetClassForm.requestRepaint();
				}
			});
			
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