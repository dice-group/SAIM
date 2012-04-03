package org.vaadin.cytographer.widgetset.client.ui;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
/**
 * It's a MenuItem
 */
public class VContextMenuItem extends MenuItem implements MouseOverHandler, MouseOutHandler {

	public VContextMenuItem(final String commandName, final boolean asHTML, final Command command) {
		super(commandName, asHTML, command);
	}

	@Override
	public void onMouseOut(final MouseOutEvent event) {
		removeStyleName("mouseover");
	}

	@Override
	public void onMouseOver(final MouseOverEvent event) {
		addStyleName("mouseover");
	}

}
