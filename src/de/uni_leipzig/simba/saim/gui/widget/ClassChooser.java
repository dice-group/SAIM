package de.uni_leipzig.simba.saim.gui.widget;

import static de.konrad.commons.sparql.SPARQLHelper.lastPartOfURL;
import static de.konrad.commons.sparql.SPARQLHelper.rootClasses;
import static de.konrad.commons.sparql.SPARQLHelper.subclassesOf;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

/** Lets the user choose a class from a given SPARQL endpoint. Queries the endpoint for classes and presents them as a tree. */
public class ClassChooser extends Panel
{
	protected static final boolean PRELOAD = false;
	protected final String endpoint,graph;
	protected final Tree tree;

	/**
	 * @param endpoint
	 * @param id
	 * @param graph
	 */
	public ClassChooser(final String endpoint, String id, final String graph)
	{
		tree = new Tree(id+" classes");
		this.endpoint = endpoint;
		this.graph = graph;
		addComponent(tree);
		tree.setImmediate(true);
		
		final ProgressIndicator progress = new ProgressIndicator();
		progress.setImmediate(true);
		progress.setIndeterminate(true);
		addComponent(progress);

		new Thread()
		{
			@Override
			public void run()
			{
				final Refresher refresher = new Refresher();
				TreeRefreshListener listener = new TreeRefreshListener();
				refresher.addListener(listener);
				addComponent(refresher);
				System.out.println("x");
				List<String> rootClasses = rootClasses(endpoint, graph);

				for(String clazz: rootClasses)
				{
					System.out.println(clazz);
					tree.addItem(new ClassNode(clazz));
				}
				listener.running=false;
				tree.setImmediate(true);

				System.out.println("y");
				progress.setEnabled(false);
				ClassChooser.this.removeComponent(progress);
				
				tree.addListener(new ExpandListener()
				{			
					@Override
					public void nodeExpand(ExpandEvent event)
					{
						System.out.println("expanding node "+event.getItemId());
						expandNode((ClassNode) event.getItemId(),PRELOAD?1:0);				
					}
				});
			}
		}.start();
		//		
		//tree.setDragMode(TreeDragMode.NODE);
	}

	protected void expandNode(ClassNode node, final int depth)
	{
		if(tree.hasChildren(node)&&depth<1) return;
		final List<ClassNode> subNodes = new LinkedList<ClassNode>();
		if(tree.hasChildren(node))
		{
			for(Object o: tree.getChildren(node))
			{
				subNodes.add((ClassNode)node);
			}
		}
		else
		{
			List<String> subClasses;
			try
			{
				subClasses  = subclassesOf(node.url, endpoint, graph);
				System.out.println(subClasses);
				Collections.sort(subClasses); // sorting in java and not in the SPARQL query because the sort order may be different for the short short
				for(String subClass: subClasses)
				{

					ClassNode subNode = new ClassNode(subClass);
					tree.addItem(subNode);
					tree.setParent(subNode,node);
					subNodes.add(subNode);
				}						
			}
			catch(Exception e){System.err.println("Error expanding node "+node.url);e.printStackTrace();}
		}

		if(subNodes.isEmpty())
		{
			tree.setChildrenAllowed(node, false);
			return;
		}
		if(depth>0)
		{
			new Thread()
			{			
				@Override
				public void run()
				{
					{for(ClassNode subNode: subNodes) {expandNode(subNode, depth-1);}}			
				}
			}.start();
		}
	}

	/** Wraps a class URL and it's displayed short form, e.g. http://dbpedia.org/ontology/City -> City. */
	protected static class ClassNode
	{
		public final String url, shortURL;
		public ClassNode(String longClass, String shortClass)
		{
			this.url = longClass;
			this.shortURL = shortClass;
		}
		public ClassNode(String longClass)
		{
			this(longClass,lastPartOfURL(longClass));
		}
		@Override public String toString() {return shortURL;}
		@Override public boolean equals(Object o) {return (o instanceof ClassNode) && ((ClassNode)o).url.equals(url);}
	}

	public class TreeRefreshListener implements RefreshListener
	{
		boolean running = true; 
		private static final long serialVersionUID = -8765221895426102605L;		    
		@Override public void refresh(final Refresher source)	{if(!running) {removeComponent(source);source.setEnabled(false);}}
	}


	//	public ClassChooser()
	//	{
	//		Tree tree = new Tree("DBpedia classes");
	//		tree.addItem("owl:Thing");
	//		tree.addItem("Animal");
	//		
	//		tree.addItem("Plant");
	//		tree.addItem("Grass");
	//		tree.setParent("Animal", "owl:Thing");
	//		tree.setParent("Plant", "owl:Thing");
	//		tree.setParent("Grass", "Plant");
	//		this.addComponent(tree);
	//		tree.setChildrenAllowed("Grass",false);
	//		tree.setChildrenAllowed("Animal",false);
	//	}
}