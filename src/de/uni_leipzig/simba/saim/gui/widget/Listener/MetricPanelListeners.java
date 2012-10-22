package de.uni_leipzig.simba.saim.gui.widget.Listener;

import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.gui.widget.panel.PerformPanel;
/**
 * Class with common methods of the metricPanel Buttons ClickListeners
 * @author Lyko
 */
public class MetricPanelListeners
{
	protected final Messages messages;
	protected SAIMApplication application;
	public MetricPanelListeners(SAIMApplication application, final Messages messages){
		this.messages=messages;
		this.application = application;
	}

	/**
	 * Show Window on applications main window
	 * @param sub Window to show
	 * @param application Application to get the Window.
	 */
	public void showWindow(Window sub) {
		application.getMainWindow().addWindow(sub);
	}

	/**
	 * Method to create sub Windows and show them.
	 * @param content
	 */
	public void getWindow(final PerformPanel content) {
		Window sub = new Window();
		sub.addListener(new CloseListener() {
			/**
			 */
			private static final long serialVersionUID = 5880054040870696798L;

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
