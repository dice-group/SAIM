package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.DefaultEndpointLoader;
import de.uni_leipzig.simba.saim.gui.validator.EndpointURLValidator;
import de.uni_leipzig.simba.saim.gui.validator.PageSizeValidator;

/** Allows the user to manually set the properties of a knowledge base, which are endpoint URL, graph URI, page size, restrictions */
@SuppressWarnings("serial")
public class KBInfoForm extends Form
{	
	protected final static String WIDTH = "35em";
	protected final ComboBox url = new ComboBox("Endpoint URL");
	protected final TextField id = new TextField("Id / Namespace");
	protected final TextField graph = new TextField("Graph");
	protected final TextField pageSize = new TextField("Page size", "-1");
	protected final TextField textFields[] = {graph, id, pageSize};	
	protected final Button next = new Button("OK" );
	protected final Component components[] = {url, graph, pageSize, next};
	KBInfo kbInfo;
	protected final Map<String,KBInfo> kbs = new HashMap<>();

	public KBInfoForm(String title)
	{
		this.setImmediate(true);
		this.setCaption(title);
		this.setWidth(WIDTH);
		addFormFields();
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
	/**
	 * Constructor to set default values of the fields.
	 * @param title
	 * @param defaultValues
	 */
	public KBInfoForm(String title, KBInfo defaultValues) {
		this(title);
		if(defaultValues != null) {
			kbInfo = defaultValues;
		} else {
			kbInfo = new KBInfo();
			kbInfo.endpoint = "";
		}
	}

	private void addFormFields() {
		addField("Endpoint URL",url);
		setDefaultEndpoints();
		url.addValidator(new EndpointURLValidator(url));
		url.setRequired(true);
		url.setRequiredError("The endpoint URL may not be empty.");
		url.setWidth("100%");
		url.setNewItemsAllowed(true);
		url.addListener(new ValueChangeListener()
		{
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
			{
				if(kbs.containsKey(url.getValue()))
				{
					KBInfo kb = kbs.get(url.getValue());
					if(kb.id!=null)			{id.setValue(kb.id);}
					if(kb.graph!=null)		{graph.setValue(kb.graph);}
					pageSize.setValue(Integer.toString(kb.pageSize));
				}
			}
		});
		url.addListener(new BlurListener(){
			@Override
			public void blur(BlurEvent event) {
				if(url.isValid())
				{
					if(!kbs.containsKey(url.getValue()))
					{
						try {
							String val = (String) url.getValue();
							String idSuggestion = val.substring(val.indexOf("http://")+7);
							idSuggestion = idSuggestion.substring(0, idSuggestion.indexOf("/"));
							if(idSuggestion.indexOf(".") > 0)
								idSuggestion = idSuggestion.substring(0, idSuggestion.indexOf("."));
							id.setValue(idSuggestion);
							// if string is not long enough and thus substring fails
						} catch(IndexOutOfBoundsException e) {id.setValue(url.getValue());}
					}
				}
			}
		});
		addField("ID / Namespace", id);
		addField("Graph",graph);
		addField("Page size",pageSize);
		pageSize.addValidator(new PageSizeValidator("Page size needs to be an integer."));
	}

	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getLayout().addComponent(contextHelp);
		contextHelp.addHelpForComponent(url, "Fill in the URL of the SPARQL endpoint, e.g. <b>http://dbpedia.org/sparql</b>.");
		contextHelp.addHelpForComponent(id, "Is used by the class matcher to find sameAs links. Only instances whose url contains the id are chosen to count as original instances of this endpoint.");
		contextHelp.addHelpForComponent(graph, "<em>(optional)</em> The Default Data Set Name (Graph IRI), e.g. <b>http://dbpedia.org</b>. " +
				"Providing a graph is optional and only needed if you want to exclude some data or speed up the process.");
		contextHelp.addHelpForComponent(pageSize, "<em>(optional)</em> Use a small page size if you get time outs while matching " +
				"and a big page size if you want more speed.");
		//contextHelp.setFollowFocus(true);
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
		kbInfo.id = id.getValue().toString();
		kbInfo.endpoint = url.getValue().toString();
		kbInfo.graph = graph.getValue().toString();
		int pageSizeInt = Integer.parseInt((String)pageSize.getValue());
		kbInfo.pageSize = pageSizeInt;
		return kbInfo;
	}

	private void setDefaultEndpoints()
	{
		kbs.clear();
		url.removeAllItems();
		for(KBInfo kb : DefaultEndpointLoader.getDefaultEndpoints())
		{
			kbs.put(kb.endpoint,kb);
			url.addItem(kb.endpoint);
		}
	}
	
	public void setValuesFromKBInfo(KBInfo info) {
		this.kbInfo = info;
		url.addItem(kbInfo.endpoint);	
		url.setValue(kbInfo.endpoint);
		graph.setValue(kbInfo.graph);
		id.setValue(kbInfo.id);
		pageSize.setValue(kbInfo.pageSize);
	}

} 