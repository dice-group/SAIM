package de.uni_leipzig.simba.saim.gui.widget.form;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Form;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.io.KBInfo;
/**
 * Form to define preprocessing functions for properties.
 * @author Lyko
 *
 */
public class PreprocessingForm extends Form{
	KBInfo info;
	String prop;
	static final Logger logger = LoggerFactory.getLogger(PreprocessingForm.class);
	HashMap<String, CheckBox> boxes = new HashMap<String, CheckBox>();
	
	/**
	 * Default constructor with KBInfo of the corresponding endpoint and name of the property.
	 * @param info
	 * @param prop Name of the property, should be abbreviated!
	 */
	public PreprocessingForm(KBInfo info, String prop) {
		this.info=info;
		prop = PrefixHelper.abbreviate(prop);
		this.prop=prop;
		if(!info.properties.contains(prop))
			info.properties.add(prop);
		setImmediate(true);
		setCaption("Select property preprocessing");
		init();
	}
	
	private void init() {
		Set<String> choosen = new HashSet<String>();
		if(info.functions.get(prop)!=null &&info.functions.get(prop).trim().length()>0) {
			String split[] = info.functions.get(prop).split("->");
			for(String preproc : split) {
				if(preproc.trim().length()>0)
					choosen.add(preproc.trim());		
			}
		}
		boxes.put("lowercase", new CheckBox("lowercase"));
		boxes.put("uppercase", new CheckBox("uppercase"));
		boxes.put("nolang", new CheckBox("nolang"));
		boxes.put("number", new CheckBox("number"));
		
		for(String choosed: choosen ) {
			if(boxes.get(choosed) != null) {
				boxes.get(choosed).setValue(true);
			}
		}
		
		for(final Entry<String, CheckBox> box : boxes.entrySet()) {
			box.getValue().addListener(new ValueChangeListener() {
				@Override
				public void valueChange(
						com.vaadin.data.Property.ValueChangeEvent event) {
					if(box.getValue().booleanValue())
						addPreprocessing(box.getKey());
					else
						removePreprocessing(box.getKey());
				}
			});
			this.addField(box.getKey(), box.getValue());
		}
	}
	/**
	 * Function adds the preprocessing function name to the KBInfo.
	 * @param name Name of the preprocessing function.
	 */
	private void addPreprocessing(String name) {
		if(!info.functions.containsKey(prop)) {
			logger.info("Adding preprocessing "+name+" to property "+prop+" for "+info.id);
			info.functions.put(prop, name);
		} else {
			String funcChain = info.functions.get(prop);
			if(funcChain.trim().length()>0) {
				funcChain+="->"+name;
			} else {
				funcChain = name;
			}
			logger.info("Adding preprocessing "+funcChain+" to property "+prop+" for "+info.id);
			info.functions.put(prop, funcChain);
		}
	}
	
	/**
	 * Remove preprocessing functions.
	 * @param name
	 */
	private void removePreprocessing(String name) {
		if(info.functions.containsKey(prop)) {
			logger.info("removing preprocessing function "+name);
			String split[] = info.functions.get(prop).split("->");
			List<String> funcList = new LinkedList();
			for(int i = 0; i<split.length; i++) {
				if(!split[i].equals(name))
					funcList.add(split[i]);
			}
			info.functions.remove(prop);
			for(String func:funcList)
				addPreprocessing(func);
		}
	}
}
