package de.uni_leipzig.simba.saim.gui.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.DefaultEndpointLoader;
import de.uni_leipzig.simba.saim.core.Endpoints;
import de.uni_leipzig.simba.saim.gui.validator.EndpointURLValidator;
import de.uni_leipzig.simba.saim.gui.validator.PageSizeValidator;
/** Allows the user to manually set the properties of a knowledge base, which are endpoint URL, graph URI, page size, restrictions */
@SuppressWarnings("serial")
public class KBInfoForm extends Form
{	
	protected final static String WIDTH = "35em"; //$NON-NLS-1$
	protected final ComboBox presetComboBox = new ComboBox(Messages.getString("preset")); //$NON-NLS-1$
	protected final ComboBox url = new ComboBox(Messages.getString("endpointurl")); //$NON-NLS-1$
	protected final TextField id = new TextField(Messages.getString("idnamespace")); //$NON-NLS-1$
	protected final TextField graph = new TextField(Messages.getString("graph")); //$NON-NLS-1$
	protected final TextField pageSize = new TextField("Page size", "-1"); //$NON-NLS-1$ //$NON-NLS-2$
	protected final TextField textFields[] = {graph, id, pageSize};	
	protected final Button next = new Button(Messages.getString("ok") ); //$NON-NLS-1$
	protected final Component components[] = {url, graph, pageSize, next};
	KBInfo kbInfo;
	/** the knowledge base presets*/
	protected final Map<String,KBInfo> presetToKB = new HashMap<>();

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
		buttonBar.addComponent(new Button(Messages.getString("reset"), this,"reset")); //$NON-NLS-1$ //$NON-NLS-2$
		getLayout().setMargin(true);
		for(TextField field: textFields)
		{
			field.setWidth("100%"); //$NON-NLS-1$
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
			kbInfo.endpoint = ""; //$NON-NLS-1$
		}
	}

	protected void presets()
	{
		presetComboBox.setMultiSelect(false);
		presetComboBox.setRequired(false);
		presetComboBox.setWidth("100%"); //$NON-NLS-1$
		presetComboBox.setNewItemsAllowed(false);		
		for(String preset : presetToKB.keySet())
		{
			presetComboBox.addItem(preset);
		}
		presetComboBox.addListener(new ValueChangeListener()
		{
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
			{
				if(presetToKB.containsKey(presetComboBox.getValue()))
				{
					KBInfo kb = presetToKB.get(presetComboBox.getValue());
					if(kb.endpoint!=null)	{url.addItem(kb.endpoint);url.select(kb.endpoint);}
					if(kb.id!=null)			{id.setValue(kb.id);}
					if(kb.graph!=null)		{graph.setValue(kb.graph);}
					pageSize.setValue(Integer.toString(kb.pageSize));
				}
			}
			
		});
	}
	
	private void addFormFields()
	{
		setDefaultEndpoints();
		presets();
		addField(Messages.getString("presets"),presetComboBox);			 //$NON-NLS-1$
		
		addField(Messages.getString("endpointurl"),url); //$NON-NLS-1$
		
		url.addValidator(new EndpointURLValidator(url));
		url.setRequired(true);
		url.setRequiredError(Messages.getString("endpointurlmaynotbeempty")); //$NON-NLS-1$
		url.setWidth("100%"); //$NON-NLS-1$
		url.setReadOnly(false);
		url.setImmediate(true);
		url.setTextInputAllowed(true);
		
		for(String endpoint: Endpoints.endpointArray) {url.addItem(endpoint);}
		url.addListener(new BlurListener(){
			@Override
			public void blur(BlurEvent event) {
				if(url.isValid())
				{
					if(!presetToKB.containsKey(url.getValue()))
					{
						try {
							String val = (String) url.getValue();
							String idSuggestion = val.substring(val.indexOf("http://")+7); //$NON-NLS-1$
							idSuggestion = idSuggestion.substring(0, idSuggestion.indexOf("/")); //$NON-NLS-1$
							if(idSuggestion.indexOf(".") > 0) //$NON-NLS-1$
								idSuggestion = idSuggestion.substring(0, idSuggestion.indexOf(".")); //$NON-NLS-1$
							id.setValue(idSuggestion);
							// if string is not long enough and thus substring fails
						} catch(IndexOutOfBoundsException e) {id.setValue(url.getValue());}
					}
				}
			}
		});
		addField(Messages.getString("idnamespace"), id); //$NON-NLS-1$
		addField(Messages.getString("graph"),graph); //$NON-NLS-1$
		addField(Messages.getString("pagesize"),pageSize); //$NON-NLS-1$
		pageSize.addValidator(new PageSizeValidator(Messages.getString("pagesizeneedstobeaninteger"))); //$NON-NLS-1$
	}

	protected void setupContextHelp()
	{
		ContextHelp contextHelp = new ContextHelp();
		getLayout().addComponent(contextHelp);
		contextHelp.addHelpForComponent(url, Messages.getString("contexthelp.endpointurl")); //$NON-NLS-1$
		contextHelp.addHelpForComponent(id, Messages.getString("contexthelp.idnamespace")); //$NON-NLS-1$
		contextHelp.addHelpForComponent(graph, Messages.getString("contexthelp.graph")); //$NON-NLS-1$				
		contextHelp.addHelpForComponent(pageSize, Messages.getString("contexthelp.pagesize")); //$NON-NLS-1$
		//contextHelp.setFollowFocus(true);
	}

	public void reset()
	{
		for(TextField field: textFields)
		{
			field.setValue(""); //$NON-NLS-1$
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
		presetToKB.clear();
		presetComboBox.removeAllItems();
		for(Entry<String, KBInfo> kb : DefaultEndpointLoader.getDefaultEndpoints().entrySet())
		{
			presetToKB.put(kb.getKey(), kb.getValue());
			presetComboBox.addItem(kb.getKey());
		}		
	}
	
	public void setValuesFromKBInfo(KBInfo info) {
		this.kbInfo = info;
		url.setValue(kbInfo.endpoint);
		graph.setValue(kbInfo.graph);
		id.setValue(kbInfo.id);
		pageSize.setValue(kbInfo.pageSize);
	}

} 