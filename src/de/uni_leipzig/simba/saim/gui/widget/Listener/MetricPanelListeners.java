package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.panel.ExecutionPanel;
import de.uni_leipzig.simba.saim.gui.widget.panel.LearningPanel;
import de.uni_leipzig.simba.saim.gui.widget.panel.MetricPanel;
import de.uni_leipzig.simba.saim.gui.widget.panel.PerformPanel;
import de.uni_leipzig.simba.saim.gui.widget.panel.MeshBasedSelfConfigPanel;

public class MetricPanelListeners
{
	private final Messages messages;	
	public MetricPanelListeners(final Messages messages){this.messages=messages;}

	/**Listener for SelfConfig button.*/
	public static class SelfConfigClickListener implements Button.ClickListener
	{		
		private final Messages messages;
		public SelfConfigClickListener(final Messages messages) {this.messages=messages;}

		@Override
		public void buttonClick(ClickEvent event)
		{
			getWindow(new MeshBasedSelfConfigPanel(messages));
		}			
	}

	/**Listener for SelfConfig button.*/
	public static class LearnClickListener implements Button.ClickListener
	{
		private final Messages messages;
		public LearnClickListener(final Messages messages) {this.messages=messages;}
		@Override
		public void buttonClick(ClickEvent event) {
			getWindow(new LearningPanel(messages));
		}			
	}

	/**Listener for SelfConfig button.*/
	public static class StartMappingListener implements Button.ClickListener
	{
		private final Messages messages;	
		public StartMappingListener(final Messages messages){this.messages=messages;}

		@Override
		public void buttonClick(ClickEvent event) {
			getWindow(new ExecutionPanel(messages));
		}			
	}

	public static void showWindow(Window sub) {
		SAIMApplication.getInstance().getMainWindow().addWindow(sub);
	}

	public static void getWindow(final PerformPanel content) {
		Window sub = new Window();
		sub.addListener(new CloseListener() {			
			@Override
			public void windowClose(CloseEvent e) {
				content.onClose();
			}
		});
		sub.setWidth("80%");
		sub.setHeight("80%");
		sub.setCaption(content.getCaption());
		sub.setContent(content);
		sub.setImmediate(true);
		sub.setVisible(true);
		sub.setModal(true);	
		showWindow(sub);
		content.start();

	}
}
