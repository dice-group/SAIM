package de.uni_leipzig.simba.saim.gui.widget;

import static com.vaadin.terminal.gwt.client.ui.AlignmentInfo.Bits.*;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbstractComponent.ComponentErrorEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

/** Allows the user to manually set the properties of a knowledge base, which are endpoint URL, graph URI, page size, restrictions */
public class KBInfoDialog extends Window {
	
	protected final static String TEXTFIELD_WIDTH = "50em";
	protected final static String WIDTH = "60em";
	protected final static String URL_DEFAULT = "http://lgd.aksw.org:5678/sparql";
	protected final static String GRAPH_DEFAULT = "http://www.instancematching.org/oaei/di/drugbank/";
	
	protected final Window parentWindow;
	protected final VerticalLayout layout = new VerticalLayout();
	protected final TextField url = new TextField("Endpoint URL");
	protected final TextField  graph = new TextField("Graph");
	protected final TextField  pageSize = new TextField("Page size");
	protected final Button next = new Button("OK" );
	protected final Component components[] = {url, graph, pageSize, next};
	
	public KBInfoDialog(Window parentWindow, String title) {
		super(title);		
		this.setModal(true);
		this.parentWindow = parentWindow;
		this.setContent(layout);
		addTextFields();
		addButtons();
		layout.setSpacing(true);
		this.setWidth(WIDTH);

	}
	
	@SuppressWarnings("serial")
	private void addTextFields() {
		url.setWidth(TEXTFIELD_WIDTH);
		graph.setWidth(TEXTFIELD_WIDTH);
		url.setValue(URL_DEFAULT);
		graph.setValue(GRAPH_DEFAULT);	
		pageSize.setValue(1000);
		
		for(Component tf : this.components) {
			layout.addComponent(tf);
			layout.setComponentAlignment(tf, ALIGNMENT_VERTICAL_CENTER, ALIGNMENT_HORIZONTAL_CENTER);
		}
	}
	
	private void addButtons() {
		next.setSizeFull();
		next.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				if(checkValues()) {
					parentWindow.removeWindow(KBInfoDialog.this);
					parentWindow.showNotification("Succesfully defined the endpoint...");
				}				
			}
			});				
	}
	
	/**
	 * Method to check values and trigger user notifications.
	 */
	private boolean checkValues() {
		String url_value = (String)url.getValue();
		String graphUri = (String)graph.getValue();
		String pageSizeString = pageSize.getValue().toString();
		
		if(url_value.length()>0)  { //add check if URL is valid
			if(pageSizeString.length()>0) {
				int pageSize;
				try {
					pageSize = Integer.parseInt(pageSizeString);
					if(graphUri.length()>0) {
						return true;
					}
					else {
						// no graph entered
						this.parentWindow.showNotification("No graph entered.");
						return true;
					}
				}catch(NumberFormatException e) {
					this.pageSize.setCaption("Please Enter a valid page size.");
					return false;
				}
			}
			else {//pageSize is empty
				return false;
			}
		} else {
			url.setCaption("Please enter a valid URL.");
			return false;
		}
	}
} 
