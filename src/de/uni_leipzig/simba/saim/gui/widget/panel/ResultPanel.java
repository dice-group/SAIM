package de.uni_leipzig.simba.saim.gui.widget.panel;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.window.SerializationWindow;
import de.uni_leipzig.simba.saim.gui.widget.window.ValidateLinksWindow;
/**Panel to show a Table with computed mappings**/
public class ResultPanel extends Panel
{
	/**
	 */
	private static final long serialVersionUID = -7935224905304705467L;
	private final Messages messages;
	InstanceMappingTable data;
	VerticalLayout layout;
	Button downloadResults;
	Button validate;
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
		layout.addComponent(data.getTable(layout));
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(downloadResults);
		// if admin show validation component
		WebApplicationContext ctx = ((WebApplicationContext) ((SAIMApplication)getApplication()).getContext());
    	HttpSession session = ctx.getHttpSession();
		if(session.getAttribute("userrole")!= null &&  //$NON-NLS-1$
    			session.getAttribute("userrole").toString().equalsIgnoreCase("admin")) //$NON-NLS-1$ //$NON-NLS-2$
			buttonLayout.addComponent(new ValidateLinksWindow((SAIMApplication) getApplication(), data.getMapping()));
		layout.addComponent(buttonLayout);
	}

	/**ClickListener for the Button to download results**/
	class DownLoadButtonClickListener implements Button.ClickListener
	{
		/**
		 */
		private static final long serialVersionUID = -2521504101562533617L;

		@Override
		public void buttonClick(ClickEvent event) {

			Window download = new SerializationWindow(data.getMapping(),messages);
			getApplication().getMainWindow().addWindow(download);
		}
	}
}
