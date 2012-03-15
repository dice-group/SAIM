package de.uni_leipzig.simba.saim.gui.widget;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.DefaultEndpointLoader;
import de.uni_leipzig.simba.saim.gui.validator.EndpointURLValidator;
import de.uni_leipzig.simba.saim.gui.validator.PageSizeValidator;

/** Allows the user to manually set the properties of a knowledge base, which are endpoint URL, graph URI, page size, restrictions */
@SuppressWarnings("serial")
public class KBInfoForm extends Form
{	
	protected final static String WIDTH = "35em";
	protected final ComboBox url = new ComboBox("Endpoint URL");
	protected final TextField  graph = new TextField("Graph");
	protected final TextField  pageSize = new TextField("Page size", "-1");
	protected final TextField textFields[] = {graph, pageSize};	
	protected final Button next = new Button("OK" );
	protected final Component components[] = {url, graph, pageSize, next};


	public KBInfoForm(String title)
	{
		this.setImmediate(true);
		this.setCaption(title);
		this.setWidth(WIDTH);
		addField("Endpoint URL",url);
		setDefaultEndpoints();
		url.addValidator(new EndpointURLValidator());
		url.setRequired(true);
		url.setRequiredError("The endpoint URL may not be empty.");
		url.setWidth("100%");
		addField("Graph",graph);
		addField("Page size",pageSize);
		pageSize.addValidator(new PageSizeValidator("Page size needs to be an integer."));		
		// Have a button bar in the footer.
		HorizontalLayout buttonBar = new HorizontalLayout();
		//buttonBar.setHeight("25px");
		getFooter().addComponent(buttonBar);		 
		// Add an Ok (commit), Reset (discard), and Cancel buttons
		setValidationVisible(true);
		buttonBar.addComponent(new Button("Reset", this,"reset"));
		getLayout().setMargin(true);
		for(TextField field: textFields)
		{
			field.setWidth("100%");
		}
		setupContextHelp();
	}
	
	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getLayout().addComponent(contextHelp);
		contextHelp.addHelpForComponent(url, "Fill in the URL of the SPARQL endpoint, e.g. <b>http://dbpedia.org/sparql</b>.");
		contextHelp.addHelpForComponent(graph, "<em>(optional)</em> The Default Data Set Name (Graph IRI), e.g. <b>http://dbpedia.org</b>. " +
				"Providing a graph is optional and only needed if you want to exclude some data or speed up the process.");
		contextHelp.addHelpForComponent(pageSize, "<em>(optional)</em> Use a small page size if you get time outs while matching " +
				"and a big page size if you want more speed.");
		contextHelp.setFollowFocus(true);
	}
	
	public void reset()
	{
		for(TextField field: textFields)
		{
			field.setValue("");
		}
	}

	public KBInfo getKBInfo() {
		KBInfo kbInfo = new KBInfo();
		kbInfo.endpoint = url.getValue().toString();
		kbInfo.graph = graph.getValue().toString();
		int pageSizeInt = Integer.parseInt((String)pageSize.getValue());
		kbInfo.pageSize = pageSizeInt;
		return kbInfo;
	}
	
	private void setDefaultEndpoints() {
		for(String epUrl : DefaultEndpointLoader.getDefaultEndpoints()) {
			url.addItem(epUrl);
		}
	}


} 
