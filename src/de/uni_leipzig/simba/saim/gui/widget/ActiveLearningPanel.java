package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.saim.core.Configuration;

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