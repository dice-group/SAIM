package de.uni_leipzig.simba.saim.gui.widget.form;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Slider;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.Configuration;

/**
 * Form for setting acceptance thresholds.
 * @author Lyko
 *
 */
public class ThresholdForm {

	private final Messages messages;
	public static final String elementsWidth = "100px"; //$NON-NLS-1$
	
	public ThresholdForm(final Messages messages) {
		this.messages = messages;
	}
	
	class ThresholdFormFieldFactory implements FormFieldFactory {		
		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {
			//  // Identify the fields by their Property ID.
	        String pid = (String) propertyId;
	        if("acceptanceThreshold".equals(pid)) { //$NON-NLS-1$
	        	Slider slider = new Slider(0d, 1d, 2);
	        	slider.setCaption(messages.getString("ThresholdForm.acceptancethreshold")); //$NON-NLS-1$
	        	slider.setWidth(elementsWidth);
	        	slider.setOrientation(Slider.ORIENTATION_HORIZONTAL);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
	        }
	        else if("verificationThreshold".equals(pid)) { //$NON-NLS-1$
	        	Slider slider = new Slider(0d, 1d, 2);
	        	slider.setCaption(messages.getString("ThresholdForm.verificationthreshold")); //$NON-NLS-1$
	        	slider.setWidth(elementsWidth);
	        	slider.setOrientation(Slider.ORIENTATION_HORIZONTAL);
	        	slider.setPropertyDataSource(item.getItemProperty(pid));
	        	return slider;
	        }
			return null;
		}
		
	}
		 
	/**
	 * Bean used by class ThresholdForm to remember settings.
	 * @author Lyko
	 *
	 */
	public class ThresholdBean {
		private double acceptanceThreshold;
		private double verificationThreshold;
		Configuration config;
		public ThresholdBean(Configuration config) {
			setDefaults();
		}
		private void setDefaults() {
			setAcceptanceThreshold(config.getAcceptanceThreshold());
			setVerificationThreshold(config.getVerificationThreshold());
		}
		public double getAcceptanceThreshold() {
			return acceptanceThreshold;
		}
		public void setAcceptanceThreshold(double acceptanceThreshold) {
			this.acceptanceThreshold = acceptanceThreshold;
		}
		public double getVerificationThreshold() {
			return verificationThreshold;
		}
		public void setVerificationThreshold(double verificationThreshold) {
			this.verificationThreshold = verificationThreshold;
		}
	}
}
