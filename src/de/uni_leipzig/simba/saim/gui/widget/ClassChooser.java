package de.uni_leipzig.simba.saim.gui.widget;

import static de.konrad.commons.sparql.SPARQLHelper.*;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

/** Lets the user choose a class from a given SPARQL endpoint. Queries the endpoint for classes and presents them as a tree. */
public class ClassChooser extends Panel
{

	public ClassChooser()
	{
		Tree tree = new Tree("DBpedia classes");
		tree.addItem("owl:Thing");
		tree.addItem("Animal");
		tree.addItem("Plant");
		tree.addItem("Grass");
		tree.setParent("Animal", "owl:Thing");
		tree.setParent("Plant", "owl:Thing");
		tree.setParent("Grass", "Plant");
		this.addComponent(tree);
		tree.setChildrenAllowed("Grass",false);
		tree.setChildrenAllowed("Animal",false);
	}

	
	
	public ClassChooser(String endpoint, String graph)
	{
		Tree tree = new Tree("DBpedia classes");
		tree.setImmediate(true);
		tree.addListener(new ExpandListener()
		{			
			@Override
			public void nodeExpand(ExpandEvent event)
			{
			}
		});
		addComponent(tree);
		String superClass = "owl:Thing";
		tree.addItem(superClass);

		for(String clazz: subclassesOf(superClass, endpoint, graph))
		{
			clazz = lastPartOfURL(clazz);
			tree.addItem(clazz);
			tree.setParent(clazz,superClass);
		}		
	}

}