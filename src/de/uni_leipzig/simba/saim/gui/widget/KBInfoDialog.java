package de.uni_leipzig.simba.saim.gui.widget;

import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.gui.validator.EndpointURLValidator;

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
	Form form;
	
	protected Form createForm()
	{
		Form form = new Form();
//		form.setWidth("500px");
		//form.setCaption("Source");
		form.addField("endpoint URL",url);
		url.addValidator(new EndpointURLValidator());
		url.setRequired(true);
		url.setRequiredError("The endpoint URL may not be empty.");
		form.addField("graph",graph);
		form.addField("page size",pageSize);
		pageSize.addValidator(new IntegerValidator("page size needs to be an integer"));		
		// Have a button bar in the footer.
		 HorizontalLayout buttonBar = new HorizontalLayout();
		//buttonBar.setHeight("25px");
		form.getFooter().addComponent(buttonBar);		 
		 // Add an Ok (commit), Reset (discard), and Cancel buttons
		// for the form.
		
		Button okbutton = new Button("OK", form, "commit");
		buttonBar.addComponent(okbutton);
		//buttonBar.setComponentAlignment(okbutton, Alignment.TOP_LEFT);
		buttonBar.addComponent(new Button("Reset", form,"discard"));
		//buttonBar.addComponent(new Button("Cancel",form,"cancel"));
		return form;
	}
	
//	public void discard()
//	{
//		form.discard();
//	}
//	
	public KBInfoDialog(Window parentWindow, String title) {
		super(title);		
		this.setModal(true);
		this.parentWindow = parentWindow;
		this.setContent(layout);
		addButtons();
		layout.setSpacing(true);
		this.setWidth(WIDTH);
		form=createForm();
		layout.addComponent(form);
	}
			
	@SuppressWarnings("serial")
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
