package de.uni_leipzig.simba.saim.gui.widget.form;

import org.apache.log4j.Logger;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import de.uni_leipzig.simba.controller.Parser;
import de.uni_leipzig.simba.genetics.util.Pair;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;

public class ManualMetricForm extends Form{
	public final TextField metricTextField = new TextField("Metric Expression");
	public final TextField thresholdTextField = new TextField("Acceptance threshold");
	static Logger logger = Logger.getLogger("SAIM");
	public ManualMetricForm() {
		HorizontalLayout perform = new HorizontalLayout();
		perform.addComponent(metricTextField);
		perform.addComponent(thresholdTextField);
		setLayout(perform);
		HorizontalLayout okbar = new HorizontalLayout();
		okbar.setHeight("25px");
		
		setDefaultValues();
		addValidators();
		
		this.addField("metric", metricTextField);
		this.addField("threshold", thresholdTextField);
		getFooter().addComponent(okbar);
//		Button okbutton = getOKButton();
//		okbar.addComponent(okbutton);
//		okbar.setComponentAlignment(okbutton, Alignment.TOP_RIGHT);
//		okbar.addComponent(new Button("Reset", this, "setDefaultValues"));
	}
	

	private void addValidators() {
		thresholdTextField.addValidator(new ThresHoldValidator());
		metricTextField.addValidator(new MetricValidator());
	}
	
	private Button getOKButton() {
		Button b = new Button("OK", this, "commit");
		b.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				// get and store values
				String metric = metricTextField.getValue().toString();
				Double threshold = Double.parseDouble(thresholdTextField.getValue().toString());
				Configuration config = ((SAIMApplication)getApplication()).getConfig();//Configuration.getInstance();
				config.setMetricExpression(metric);
				config.setAcceptanceThreshold(threshold);
//				config.setDefaultNameSpaces();
				// run it
//				SAIMApplication appl = (SAIMApplication) getApplication();
//				appl.showComponent(new ExecutionPanel());
			}
		});
		return b;
	}
	
	public class ThresHoldValidator extends DoubleValidator {
		public ThresHoldValidator() {
			this("A Threshold must be a value between 1 and 0.");
		}		
		public ThresHoldValidator(String errorMessage) {
			super(errorMessage);
		}		
		@Override
	    protected boolean isValidString(String value) {
	        try {
	            double d = Double.parseDouble(value);
	            return d>0;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	}
	
	public class MetricValidator implements Validator {	
		@Override
		public void validate(Object value) throws InvalidValueException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isValid(Object value) {
			if(value == null || value.toString().length()==0 || !thresholdTextField.isValid()) return false;
			else {
				return testPropertiesAreSet(value.toString(), Double.parseDouble(thresholdTextField.getValue().toString()));
			}
		}
	}
	
	/**Recursive function using LIMES Parser to test whether all properties of the metric expression are set.*/
	public boolean testPropertiesAreSet(String expr, double threshold) {
		logger.info("testing if properties are set...");
		Configuration config = ((SAIMApplication)getApplication()).getConfig();//Configuration.getInstance();
		Parser parser = new Parser(expr, threshold);
		if(parser.isAtomic()) {
			if(config.isPropertyDefined(parser.getTerm1())) {
				if(config.isPropertyDefined(parser.getTerm2()))
					return true;
				else {
					logger.info("Property "+parser.getTerm2()+" not defined.");
					this.setComponentError(new UserError("Property "+parser.getTerm2()+" not defined."));
				}
			}
			else {
				logger.info("Property "+parser.getTerm1()+" not defined.");
				this.setComponentError(new UserError("Property "+parser.getTerm2()+" not defined."));
			}
			
			return false;
		} else {
			logger.info("recursive calls");
			return testPropertiesAreSet(parser.getTerm1(), parser.threshold1) && testPropertiesAreSet(parser.getTerm2(), parser.threshold2);
		}
	}
	
	public void setDefaultValues() {
		if((SAIMApplication)getApplication() != null) {
			Configuration config = ((SAIMApplication)getApplication()).getConfig();//Configuration.getInstance();
			if(config.getMetricExpression() == null) {
				if (config.propertyMapping == null || config.propertyMapping.stringPropPairs.size() == 0) {
					return;
				} else {
					Pair<String> propPair = config.propertyMapping.stringPropPairs.get(0);
					String metric = "trigram(";
					metric += config.getSource().var.replaceAll("\\?", "")+"."+propPair.a;
					metric +=",";
					metric += config.getTarget().var.replaceAll("\\?", "")+"."+propPair.b;
					metric += ")";
					metricTextField.setValue(metric);
					thresholdTextField.setValue("0.5d");
				}			
			} else {
				metricTextField.setValue(config.getMetricExpression());
				thresholdTextField.setValue(config.getAcceptanceThreshold());
			}
		}		
	}
}
