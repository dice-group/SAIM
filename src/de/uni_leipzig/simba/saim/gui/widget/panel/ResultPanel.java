package de.uni_leipzig.simba.saim.gui.widget.panel;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.window.SerializationWindow;

/**Panel to show a Table with computed mappings**/
public class ResultPanel extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7935224905304705467L;
	private final Messages messages;
	InstanceMappingTable data;
	VerticalLayout layout;
	Button downloadResults;
	Configuration config;// = Configuration.getInstance();
	
	public ResultPanel(final InstanceMappingTable iT,final Messages messages)
	{
		super(messages.getString("results")); //$NON-NLS-1$
		this.messages=messages;
		this.data = iT;
	}
	
	@Override
	public void attach() {
		config = ((SAIMApplication)getApplication()).getConfig();
		init();
	}
	
	private void init() {
		layout = new VerticalLayout();
		layout.setWidth("100%");
		this.setContent(layout);
		downloadResults = new Button(messages.getString("save"));
		downloadResults.addListener(new DownLoadButtonClickListener());		
		layout.addComponent(data.getTable());
		layout.addComponent(downloadResults);
	}
	
	/**ClickListener for the Button to download results**/	
	class DownLoadButtonClickListener implements Button.ClickListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2521504101562533617L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			Window download = new SerializationWindow(data.getMapping(),messages);
			SAIMApplication.getInstance().getMainWindow().addWindow(download);
		}
	}
}