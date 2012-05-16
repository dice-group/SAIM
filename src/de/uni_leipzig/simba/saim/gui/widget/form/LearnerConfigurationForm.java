package de.uni_leipzig.simba.saim.gui.widget.form;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;

public class LearnerConfigurationForm extends Form{

	public static final String elementsWidth = "100px";
	
	public LearnerConfigurationForm(LearnerConfigurationBean bean) {
		
		setCaption("Learner Configuration");
		setDescription("Specify parameters for the genetic metric learner. Note that higher values for " +
				"the number of generations and the population size will lead in longer execution time.");
		 
		setFormFieldFactory(new LearnerCofigurationFormFieldFactory());
		 // Create the custom bean. 
		 
		 // Create a bean item that is bound to the bean. 
		 BeanItem item = new BeanItem(bean);
		 
		 // Bind the bean item as the data source for the form. 
		 setItemDataSource(item);
		 this.setWriteThrough(true);
	}
	
	
	class LearnerCofigurationFormFieldFactory implements FormFieldFactory {
	    public Field createField(Item item, Object propertyId,
	                             Component uiContext) {
	        // Identify the fields by their Property ID.
	        String pid = (String) propertyId;
	        if ("generations".equals(pid)) {
	        	IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(5);
	            intStepper.setMaxValue(100);
	            intStepper.setMinValue(5);
	            intStepper.setCaption("Number Of Generations");
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
	        } 
	        else if ("population".equals(pid)) {
	        	IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(5);
	            intStepper.setMaxValue(100);
	            intStepper.setMinValue(5);
	            intStepper.setCaption("Size of population");
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
	        }
	        else if("mutationRate".equals(pid)) {
	        	Slider slider = new Slider(0d, 1d, 1);
	        	slider.setCaption("Mutation rate");
	        	slider.setWidth(elementsWidth);
	        	slider.setOrientation(Slider.ORIENTATION_HORIZONTAL);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
	        }
	        else if("numberOfInqueriesPerRun".equals(pid)) {
	        	IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(10);
	            intStepper.setMaxValue(100);
	            intStepper.setMinValue(10);
	            intStepper.setCaption("Number Of inqueries to user");
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
	        }
	        
	        return null; // Invalid field (property) name.
	    }
	}
}
