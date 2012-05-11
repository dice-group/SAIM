package de.uni_leipzig.simba.saim.gui.widget.panel;

import com.vaadin.ui.Panel;

public abstract class PerformPanel extends Panel{
	public PerformPanel(String string) {
		super(string);
	}
	
	public PerformPanel() {
		super();
	}

	/**
	 * Action to be performed if Window holding Panel is closed.
	 */
	public abstract void onClose();
	
	/**
	 * Called after Window is showed.
	 */
	public abstract void start();
}
