package de.uni_leipzig.simba.saim.gui.widget;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.saim.core.Configuration;

/** Contains instances of ClassMatchingForm and lays them out vertically.*/
public class ClassMatchingPanel extends Panel
{	
	Configuration config = Configuration.getInstance();
	final ComboBox proposals = new ComboBox("Proposals");

	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getContent().addComponent(contextHelp);
		contextHelp.addHelpForComponent(proposals, "Class pairs from LIMES.");
	}

	public ClassMatchingPanel()
	{
		setContent(new VerticalLayout());
		{
			FormLayout layout = new FormLayout(); // have the label to the right of the combobox and not on top
			this.getContent().addComponent(layout);
			proposals.setWidth("100%");
			layout.addComponent(proposals);
		}
		{
			HorizontalLayout hori = new HorizontalLayout();
			hori.setWidth("100%");
			proposals.setEnabled(false);
			new Thread()
			{

				@Override
				public void run()
				{
				//	try{Thread.sleep(5000);} catch (InterruptedException e) {	e.printStackTrace();}
					proposals.setEnabled(true);
					System.out.println("bla");
				}
			}.start();
			hori.addComponent(new ClassMatchingForm("Source Class", config.getSource()));
			hori.addComponent(new ClassMatchingForm("Target Class", config.getTarget()));
			this.getContent().addComponent(hori);
		}
		setupContextHelp();
	}	
}
