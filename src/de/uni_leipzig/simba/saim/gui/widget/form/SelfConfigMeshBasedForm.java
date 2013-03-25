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
import de.uni_leipzig.simba.saim.Messages;
/**
 * Configurator for the Meshbased selfconfigurator.
 * @author Lyko
 */
public class SelfConfigMeshBasedForm extends Form {
	/**
	 */
	private static final long serialVersionUID = 3184156595397084333L;
	private final Messages messages;
	public static final String elementsWidth = "100px"; //$NON-NLS-1$
	public static final String pseudoMeasureName="Pseudo F-Measure NGLY12";
	public static final String referenceMeasureName="Pseudo F-Measure NIK+12";
	// Map of measures and indices
	public static HashMap<Integer, String> measures = new HashMap<Integer,String>();

	/**
	 * Constructor
	 * @param bean Bean used to hold configure data.
	 * @param messages Messages instance to externalize Strings.
	 */
	public SelfConfigMeshBasedForm(SelfConfigMeshBasedBean bean, final Messages messages) {
		measures.put(0, pseudoMeasureName);
		measures.put(1, referenceMeasureName);
		this.messages = messages;
		setFormFieldFactory(new SelfConfigMeshBasedFormFieldFactory());
		BeanItem<SelfConfigMeshBasedBean> item = new BeanItem<SelfConfigMeshBasedBean>(bean);
		setItemDataSource(item);
		this.setWriteThrough(true);
	}

	/**
	 * Used to generate items of the form for the MeshBasedSelfConfigForm form.
	 * @author Lyko
	 */
	class SelfConfigMeshBasedFormFieldFactory implements FormFieldFactory {
		private static final long serialVersionUID = 52534782115538460L;

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
			if("minCoverage".equals(pid)) {//$NON-NLS-1$
	        	ShowingValueSlider slider = new ShowingValueSlider(messages.getString("SelfConfigMeshBasedForm.mincoverage"), 0d, 1d, 1);
	        	slider.setWidth(elementsWidth);
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
	            intStepper.setMinValue(1);
	            intStepper.setCaption(messages.getString("SelfConfigMeshBasedForm.gridpoints"));  //$NON-NLS-1$
	            intStepper.setWidth(elementsWidth);
	            intStepper.setPropertyDataSource(item.getItemProperty(pid));
	            return intStepper;
			}
			if("classifierName".equals(pid)) {//$NON-NLS-1$
				Select s = new Select("Choose a classifier");
				Object id = s.addItem(0);
				s.addItem(1);
				s.addItem(2);
				s.setItemCaption(0, "MeshBased SelfConfigurator");
				s.setItemCaption(1, "Linear MeshBased Selfconfigurator");
				s.setItemCaption(2, "Disjunctive MeshBased Selfconfigurator");

				s.setNullSelectionAllowed(false);
				s.select(id);
				return s;
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
			return null;//unknown field.
		}

	}
}
