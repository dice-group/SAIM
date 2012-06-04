package de.uni_leipzig.simba.saim.gui.widget.form;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Slider;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.gui.widget.form.LearnerConfigurationForm.LearnerCofigurationFormFieldFactory;

/**
 * Configurator for the Meshbased selfconfigurator.
 * @author Lyko
 */
public class SelfConfigMeshBasedForm extends Form {
	private final Messages messages;
	public static final String elementsWidth = "100px"; //$NON-NLS-1$
	/**
	 * Constructor
	 * @param bean Bean used to hold configure data.
	 * @param messages Messages instance to externalize Strings.
	 */
	public SelfConfigMeshBasedForm(SelfConfigMeshBasedBean bean, final Messages messages) {
		this.messages = messages;
		
		setFormFieldFactory(new SelfConfigMeshBasedFormFieldFactory());
		
		 BeanItem item = new BeanItem(bean);
		
		 setItemDataSource(item);
		 this.setWriteThrough(true);
	}
	
	/**
	 * Used to generate items of the form for the MeshBasedSelfConfigForm form.
	 * @author Lyko
	 *
	 */
	class SelfConfigMeshBasedFormFieldFactory implements FormFieldFactory {

		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {
			 // Identify the fields by their Property ID. This is the name of the field of the corresponding bean.
	        String pid = (String) propertyId;
			if("beta".equals(pid)) {//$NON-NLS-1$
				Slider slider = new Slider(0d, 1d, 1);
	        	slider.setCaption(messages.getString("SelfConfigMeshBasedForm.beta"));  //$NON-NLS-1$
	        	slider.setWidth(elementsWidth);
	        	slider.setOrientation(Slider.ORIENTATION_HORIZONTAL);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
			}
			if("minCoverage".equals(pid)) {//$NON-NLS-1$
				Slider slider = new Slider(0d, 1d, 1);
	        	slider.setCaption(messages.getString("SelfConfigMeshBasedForm.mincoverage"));  //$NON-NLS-1$
	        	slider.setWidth(elementsWidth);
	        	slider.setOrientation(Slider.ORIENTATION_HORIZONTAL);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
			}
			if("iterations".equals(pid)) {//$NON-NLS-1$
				IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(1);
	            intStepper.setMaxValue(10);
	            intStepper.setMinValue(1);
	            intStepper.setCaption(messages.getString("SelfConfigMeshBasedForm.iterations"));  //$NON-NLS-1$
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
			}
			if("gridPoints".equals(pid)) {//$NON-NLS-1$
				IntStepper intStepper = new IntStepper();
	            intStepper.setStepAmount(1);
	            intStepper.setMaxValue(100);
	            intStepper.setMinValue(5);
	            intStepper.setCaption(messages.getString("SelfConfigMeshBasedForm.gridpoints"));  //$NON-NLS-1$
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
			}
			return null;//unknown field.
		}
		
	}
}
