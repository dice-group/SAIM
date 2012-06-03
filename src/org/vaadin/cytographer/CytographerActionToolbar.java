package org.vaadin.cytographer;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;

import csplugins.layout.algorithms.circularLayout.CircularLayoutAlgorithm;
import csplugins.layout.algorithms.force.ForceDirectedLayout;
import csplugins.layout.algorithms.hierarchicalLayout.HierarchicalLayoutAlgorithm;
import cytoscape.layout.CyLayoutAlgorithm;
import cytoscape.layout.algorithms.GridNodeLayout;

public class CytographerActionToolbar extends HorizontalLayout {
	private static final long serialVersionUID = -2390577303164805877L;
	
	private NativeSelect layoutSelect;
	private Cytographer cytographer;

	public CytographerActionToolbar(Cytographer cytographer) {
		setSpacing(true);
		addComponent(getLayoutSelect());
		this.cytographer = cytographer;
	}

	private Component getLayoutSelect() {
		layoutSelect = new NativeSelect();
		layoutSelect.setCaption("Layout algorithm");
		layoutSelect.addContainerProperty("alg", CyLayoutAlgorithm.class, null);

		Item i = layoutSelect.addItem("Force Directed");
		i.getItemProperty("alg").setValue(new ForceDirectedLayout());
		i = layoutSelect.addItem("Hierarchical");
		i.getItemProperty("alg").setValue(new HierarchicalLayoutAlgorithm());
		i = layoutSelect.addItem("Grid");
		i.getItemProperty("alg").setValue(new GridNodeLayout());
		i = layoutSelect.addItem("Circular");
		i.getItemProperty("alg").setValue(new CircularLayoutAlgorithm());

		layoutSelect.setNullSelectionAllowed(true);
		layoutSelect.setNullSelectionItemId("[select]");
		layoutSelect.setImmediate(true);
		layoutSelect.select(null);

		layoutSelect.addListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 3668584778868323776L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (layoutSelect.getValue() != null) {
					final CyLayoutAlgorithm loAlgorithm = (CyLayoutAlgorithm) layoutSelect.getItem(layoutSelect.getValue())
							.getItemProperty("alg").getValue();
					cytographer.applyLayoutAlgorithm(loAlgorithm);
					layoutSelect.select(null);
				}
			}
		});
		return layoutSelect;
	}
}