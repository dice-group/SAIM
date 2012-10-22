package de.uni_leipzig.simba.saim.gui.widget.form;

import java.util.HashMap;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.Slider;

import de.uni_leipzig.simba.saim.Messages;

public class SelfConfigGeneticBasedForm extends Form {
	private static final long serialVersionUID = 5011946239985166902L;
	private final Messages messages;
	public static final String elementsWidth = "100px"; //$NON-NLS-1$
	//names of measure
	public static final String pseudoMeasureName="Pseudo F-Measure (Dr. Ngonga)";
	public static final String referenceMeasureName="Reference pseudo F-Measure";
	// Map of measures and indices
	public static HashMap<Integer, String> measures = new HashMap<Integer,String>();
	
	public SelfConfigGeneticBasedForm(SelfConfigGeneticBasedBean bean, final Messages messages) {
		measures.put(0, pseudoMeasureName);
		measures.put(1, referenceMeasureName);
		this.messages = messages;
		
		setFormFieldFactory(new SelfConfigGeneticBasedFormFieldFactory());
		
		@SuppressWarnings("unchecked")
		BeanItem item = new BeanItem(bean);
		
		setItemDataSource(item);
		this.setWriteThrough(true);
	}

	/**
	 * Class to construc fields.	
	 * @author Lyko
	 */
	class SelfConfigGeneticBasedFormFieldFactory implements FormFieldFactory {

		/**
		 */
		private static final long serialVersionUID = 1990806900381737931L;

		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {
			 // Identify the fields by their Property ID. This is the name of the field of the corresponding bean.
	        String pid = (String) propertyId;
			if("beta".equals(pid)) {//$NON-NLS-1$
				ShowingValueSlider slider = new ShowingValueSlider(messages.getString("SelfConfigMeshBasedForm.beta"), 0.1d, 2d, 1);
	        	slider.setWidth(elementsWidth);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
			}
	        else if("measure".equals(pid)) {//$NON-NLS-1$
	    		measures.put(0, pseudoMeasureName);
	    		measures.put(1, referenceMeasureName);
	    		
				Select s = new Select("Choose a classifier");
				Object id = s.addItem(0);
				s.addItem(1);
				s.setItemCaption(0, measures.get(0));
				s.setItemCaption(1, measures.get(1));			
				s.setNullSelectionAllowed(false);
				s.select(id);
				return s;
			}
			else if ("generations".equals(pid)) { //$NON-NLS-1$
	        	IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(5);
	            intStepper.setMaxValue(100);
	            intStepper.setMinValue(5);
	            intStepper.setCaption(messages.getString("LearnerConfigurationForm.generations")); //$NON-NLS-1$
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
	        } 
	        else if ("population".equals(pid)) { //$NON-NLS-1$
	        	IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(5);
	            intStepper.setMaxValue(100);
	            intStepper.setMinValue(5);
	            intStepper.setCaption(messages.getString("LearnerConfigurationForm.population")); //$NON-NLS-1$
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
	        }
	        else if("mutationRate".equals(pid)) { //$NON-NLS-1$
	        	ShowingValueSlider slider = new ShowingValueSlider(messages.getString("LearnerConfigurationForm.mutation"), 0d, 1d, 1);
	        	slider.setWidth(elementsWidth);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
	        }
	        else if("crossoverRate".equals(pid)) { //$NON-NLS-1$
	        	ShowingValueSlider slider = new ShowingValueSlider(messages.getString("LearnerConfigurationForm.crossover"), 0d, 1d, 1);
	        	slider.setWidth(elementsWidth);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
	        }
	        else
	        	return null;
		}		
	}
}
