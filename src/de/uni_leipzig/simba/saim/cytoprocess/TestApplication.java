package de.uni_leipzig.simba.saim.cytoprocess;

//import giny.view.NodeView;
//import giny.view.NodeView;

import com.vaadin.Application;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import csplugins.layout.algorithms.force.ForceDirectedLayout;

public class TestApplication extends Application{
	private static final long serialVersionUID = -7506990364469416142L;

	private Window mainWindow;
	@Override
	public void init() {
		setTheme("Cytoprocess");

		mainWindow = new Window("Test Application");
		setMainWindow(mainWindow);
		Panel  panel = new Panel();
		final Cytoprocess cp = new Cytoprocess(900, 600);
		panel.addComponent(cp);
		mainWindow.addComponent(cp);

		int n = 4;
		int k = 5;
		Integer nodes[] = new Integer[n*k];
		for(int i = 0; i < n*k; i++)
			nodes[i] = 	cp.addNode("Node", 100, 100, ViewNode.DIAMOND, "rgb(255,0,0)", false);


		for(int m = 0 ; m < k ; m++)
			for (int i = n * m; i < n*(m+1); i++)
				for (int j = i; j < n*(m+1); j++)
					if ( i != j )
						cp.addEdge(nodes[i], nodes[j],"");

		for(int oc = 0; oc < k; oc++)
			for(int ic = oc; ic < k - 1; ic++)
				for(int v = 0; v < n; v++)
					cp.addEdge(nodes[oc * n + v], nodes[ic * n + v + n],"");

//		cp.applyLayoutAlgorithm(new ForceDirectedLayout());
		cp.repaintGraph();
	}
}
