package de.uni_leipzig.simba.saim.gui.widget;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

import de.uni_leipzig.simba.saim.core.Pair;

/** The class matching consists of two sets of classes from both knowledge bases and a match between them.
 * In RAVEN, the mapping is injective and uniquely defined (there is at most one match for a class).
 * The user gets shown some classes and can add some manually or create the matching manually.*/
public class ClassMatchingForm extends Form
{	
	List<Pair<TextField>> classMatchings = new ArrayList<>();

	
	
	public ClassMatchingForm() {
		this.setLayout(new GridLayout(0,4));
		addFieldPair();
	}
	
	public void addFieldPair() {
		Pair<TextField> newPair = new Pair<>(new TextField("Source class"), new TextField("Target class"));
		classMatchings.add(newPair);
		
//		Panel matchPanel = new Panel();
//		matchPanel.setContent(new HorizontalLayout());
		addField("src"+(classMatchings.size()-1), newPair.a);
		addField("dest"+(classMatchings.size()-1), newPair.b);		
		
	}
}
