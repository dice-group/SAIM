package de.uni_leipzig.simba.saim;



import org.apache.log4j.Logger;

import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.core.metric.Operator;
import de.uni_leipzig.simba.saim.core.metric.Output;
import de.uni_leipzig.simba.saim.gui.widget.form.PreprocessingForm;
/**
 * 
 * @author rspeck
 *
 */
public class SAIMCytoprocessModalWindows {

	private static final Logger LOGGER = Logger.getLogger(SAIMCytoprocessModalWindows.class);
	
	//SAIMApplication
	private final Messages messages;
	//SAIMApplication
	private Configuration config;
	//SAIMApplication
	private Window mainWindow;

	public SAIMCytoprocessModalWindows(Messages messages, Configuration config,	Window mainWindow) {
		this.messages = messages;
		this.config = config;
		this.mainWindow = mainWindow;
	}
	
	/**
	 * Method to add Properties to according KBInfo. 
	 * @param s URI of the property. May or may not be abbreviated.
	 * @param info KBInfo of endpoint property belongs to.
	 */
	public void makePropertiesModalWindow(String s, KBInfo info){ 
		String prop;
		
		if(s.startsWith("http:")) {//do not have a prefix, so we generate one
			PrefixHelper.generatePrefix(s);
			prop = PrefixHelper.abbreviate(s);
		} else {// have the prefix already
			prop = s;
			s = PrefixHelper.expand(s);
		}
		if(!info.properties.contains(prop)) {
			info.properties.add(prop);
		}

		final Window sub = new Window(messages.getString("Cytographer.definepreprocessingsubwindowname")+prop);
		sub.setModal(true);
		sub.addComponent(new PreprocessingForm(info, prop));
		sub.setResizable(false);
		sub.addStyleName(Reindeer.WINDOW_BLACK);
		sub.setHeight("250px");
		sub.setWidth("250px");	
		mainWindow.addWindow(sub);

		final Button btnok = new Button("Ok");	
		 btnok.addListener(new ClickListener(){

			private static final long serialVersionUID = -5798219584876778441L;

			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.removeWindow(sub); // close
			}		 
		 });
		 sub.addComponent(btnok);
		String base = PrefixHelper.getBase(s);
		info.prefixes.put(PrefixHelper.getPrefix(base), PrefixHelper.getURI(PrefixHelper.getPrefix(base)));	
	}
	
	
	public void makeOperatorModalWindow(final SAIMCytoprocess cyto, final int nodeid, final Operator n){

		final Window window = new Window("");
		
		final TextField[] textField = new TextField[2];
		
		Double[] values = new Double[2];
		values[0] = n.param1;
		values[1] = n.param2;
		for(int i = 0 ; i < values.length ; i++){
		
			String old = (values[i] != null)? values[i].toString() : "";
			
			textField[i] = new TextField( messages.getString("Cytographer.modalWindowTextField" + (i+1) + "LabelOperator"),old);
			window.addComponent(textField[i]);
			textField[i].addValidator( new MyDoubleValidator(messages.getString("Cytographer.thresholdWarningOperator")));
			textField[i].setMaxLength(4);
			textField[i].setImmediate(true);
		}


		window.setResizable(false);
		window.setModal(true); 
		window.addStyleName(Reindeer.WINDOW_BLACK);
		window.setHeight("180px");
		window.setWidth("200px");	

				
		final Button btnok = new Button("Ok");	
		btnok.setEnabled(true);
		btnok.setImmediate(true);
		btnok.setVisible(true);	 
		
		final Button btncancel = new Button("Cancel");
		btncancel.setEnabled(true);
		btncancel.setImmediate(true);
		btncancel.setVisible(true);	
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(btnok);
		layout.addComponent(btncancel);
		
		window.addComponent(layout);
		mainWindow.addWindow(window);
	
	
		// implement button listeners
		btnok.addListener(new ClickListener(){

			private static final long serialVersionUID = -5167638371945850091L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(
						textField[0].isValid() && 
						textField[1].isValid() && 
						!((String) textField[0].getValue()).trim().isEmpty() && 
						!((String) textField[1].getValue()).trim().isEmpty())
				{
					
					cyto.setOperatorValues(nodeid,Double.valueOf((String) textField[0].getValue()),Double.valueOf((String) textField[1].getValue()));
					
					n.param1 = Double.valueOf((String) textField[0].getValue());
					n.param2 = Double.valueOf((String) textField[1].getValue());
					
					cyto.setOperatorEdgeLabels(n);
					
					//cyto.updateNode();
					cyto.repaintGraph();
										
					if(LOGGER.isDebugEnabled())LOGGER.debug("set new operator values");
					mainWindow.removeWindow(window); // close
				}else{
					mainWindow.showNotification(messages.getString("Cytographer.thresholdWarningOperator"), Notification.TYPE_WARNING_MESSAGE);
				}
			}
			
		});
		
		btncancel.addListener(new ClickListener(){
			private static final long serialVersionUID = -1837863358332352971L;

			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.removeWindow(window); // close
			}
		});
	}
	
	public void makeOutputModalWindow(final SAIMCytoprocess cyto, final int nodeid, final Output n){

		final Window window = new Window("");
		
		final TextField[] textField = new TextField[2];
		
		Double[] values = new Double[2];
		values[0] = n.param1;
		values[1] = n.param2;
		for(int i = 0 ; i < values.length ; i++){
		
			String old = (values[i] != null)? values[i].toString() : "";
			
			textField[i] = new TextField( messages.getString("Cytographer.modalWindowTextField"+ (i+1) +"LabelOutput"),old);
			window.addComponent(textField[i]);
			textField[i].addValidator( new MyDoubleValidator(messages.getString("Cytographer.thresholdWarningOutput")));
			textField[i].setMaxLength(4);
			textField[i].setImmediate(true);
		}


		window.setResizable(false);
		window.setModal(true); 
		window.addStyleName(Reindeer.WINDOW_BLACK);
		window.setHeight("180px");
		window.setWidth("200px");	

				
		final Button btnok = new Button("Ok");	
		btnok.setEnabled(true);
		btnok.setImmediate(true);
		btnok.setVisible(true);	 
		
		final Button btncancel = new Button("Cancel");
		btncancel.setEnabled(true);
		btncancel.setImmediate(true);
		btncancel.setVisible(true);	
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(btnok);
		layout.addComponent(btncancel);
		
		window.addComponent(layout);
		mainWindow.addWindow(window);
	
	
		// implement button listeners
		btnok.addListener(new ClickListener(){

			private static final long serialVersionUID = -5167638371945850091L;

			@Override
			public void buttonClick(ClickEvent event) {
			
				if(
						textField[0].isValid() && 
						textField[1].isValid() && 
						!((String) textField[0].getValue()).trim().isEmpty() && 
						!((String) textField[1].getValue()).trim().isEmpty())
				{
					
					cyto.setOutputValues(nodeid, Double.valueOf((String) textField[0].getValue()),Double.valueOf((String) textField[1].getValue()));
					
					n.param1 = Double.valueOf((String) textField[0].getValue());
					n.param2 = Double.valueOf((String) textField[1].getValue());
					
					cyto.updateNode();
										
					if(LOGGER.isDebugEnabled())LOGGER.debug("set new output values");
					mainWindow.removeWindow(window); // close
				}else{
					mainWindow.showNotification(messages.getString("Cytographer.thresholdWarningOutput"), Notification.TYPE_WARNING_MESSAGE);
				}
			}
			
		});
		
		btncancel.addListener(new ClickListener(){

			private static final long serialVersionUID = 3952212986415782003L;

			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.removeWindow(window); // close
			}
		});
	}
}




class MyDoubleValidator extends DoubleValidator {
	private static final long serialVersionUID = -5585916227598767457L;
	public MyDoubleValidator(String msg) {
		super(msg);
	}		
	
	@Override
	protected boolean isValidString(String value) {
		if(value == null) return false;
		if(value.trim().isEmpty()) return false;
		try {
			double d = Double.parseDouble(value);
			return d>=0 && d <=1;
		} catch (Exception e) {
			return false;
		}
	}
}