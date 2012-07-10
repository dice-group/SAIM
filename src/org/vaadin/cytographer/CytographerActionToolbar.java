package org.vaadin.cytographer;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

import csplugins.layout.algorithms.circularLayout.CircularLayoutAlgorithm;
import csplugins.layout.algorithms.force.ForceDirectedLayout;
import csplugins.layout.algorithms.hierarchicalLayout.HierarchicalLayoutAlgorithm;
import cytoscape.layout.CyLayoutAlgorithm;
import cytoscape.layout.algorithms.GridNodeLayout;

public class CytographerActionToolbar extends VerticalLayout {
	private static final long serialVersionUID = -2390577303164805877L;
	
	private NativeSelect layoutSelect;
	private Cytographer cytographer;

	public CytographerActionToolbar(Cytographer cytographer) {
		this.cytographer = cytographer;
		
		setSpacing(true);
		
		Component layout  = getLayoutSelect();
		addComponent(layout);
		
		Component fitb  = getFitToViewButton();
		addComponent(fitb);
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(getZoomInButton());
		hl.addComponent(getZoomOutButton());
		
		//addComponent(hl);		
		
		setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		setComponentAlignment(fitb, Alignment.MIDDLE_CENTER);
		//setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
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
	private Component getFitToViewButton() {
		final Button button = new Button("Fit to view");
		button.setImmediate(true);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8874905593085298508L;

			@Override
			public void buttonClick(final ClickEvent event) {
				cytographer.fitToView();
			}
		});
		return button;
	}
	private Component getZoomOutButton() {
		final Button button = new Button("-");
		button.setImmediate(true);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -2424360452782278429L;

			@Override
			public void buttonClick(final ClickEvent event) {
				cytographer.zoomOut();
			}
		});
		return button;
	}

	private Component getZoomInButton() {
		final Button button = new Button("+");
		button.setImmediate(true);
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -1990896114255311865L;

			@Override
			public void buttonClick(final ClickEvent event) {
				cytographer.zoomIn();
			}
		});
		return button;
	}
}