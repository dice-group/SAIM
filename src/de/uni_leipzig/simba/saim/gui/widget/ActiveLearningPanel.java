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
public class ActiveLearningPanel extends Panel
{	
	Configuration config = Configuration.getInstance();

//	protected void setupContextHelp()
//	{
//		ContextHelp contextHelp = new ContextHelp();
//		getContent().addComponent(contextHelp);
//		contextHelp.addHelpForComponent(suggestionComboBox, Messages.getString("classpairsfromlimes")); //$NON-NLS-1$
//	}

	public ActiveLearningPanel()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		setContent(layout);
		addComponent(new ActiveLearningRow("bla","blubb"));
	}

}