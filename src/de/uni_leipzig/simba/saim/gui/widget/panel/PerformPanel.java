package de.uni_leipzig.simba.saim.gui.widget.panel;

import com.vaadin.ui.Panel;

public abstract class PerformPanel extends Panel{
	/**
	 */
	private static final long serialVersionUID = -7297618651638896173L;

	public PerformPanel(String string) {
		super(string);
	}

//	public PerformPanel() {
//		super();
//	}

	/**
	 * Action to be performed if Window holding Panel is closed.
	 */
	public abstract void onClose();

	/**
	 * Called after Window is showed.
	 */
	public abstract void start();
}
