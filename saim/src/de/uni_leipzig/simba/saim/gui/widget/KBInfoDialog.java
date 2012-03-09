package de.uni_leipzig.simba.saim.gui.widget;

import org.vaadin.dialogs.ConfirmDialog;
import static com.vaadin.terminal.gwt.client.ui.AlignmentInfo.Bits.*;
import com.vaadin.ui.Button;
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
				parentWindow.removeWindow(KBInfoDialog.this);
			}
			});				
	}
} 
