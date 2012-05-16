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
public class ResultPanel extends Panel{
	InstanceMappingTable data;
	VerticalLayout layout;
	Button downloadResults;
	Configuration config;// = Configuration.getInstance();
	
	public ResultPanel(InstanceMappingTable iT) {
		super(Messages.getString("results")); //$NON-NLS-1$
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
		downloadResults = new Button(Messages.getString("save"));
		downloadResults.addListener(new DownLoadButtonClickListener());		
		layout.addComponent(data.getTable());
		layout.addComponent(downloadResults);
	}
	
	
	/**ClickListener for the Button to download results**/	
	class DownLoadButtonClickListener implements Button.ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			
			Window download = new SerializationWindow(data.getMapping());
			SAIMApplication.getInstance().getMainWindow().addWindow(download);
		}
		
	}
}