package org.vaadin.cytographer.widgetset.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
/**
 * It's a PopupPanel
 */
public class VContextMenu extends PopupPanel {

	private final ContextListener listener;

	public VContextMenu(final ContextListener listener) {
		super();
		this.listener = listener;
		this.listener.initCommands(this);
		sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU | Event.ONCLICK);
		setStyleName("contextmenu");
	}

	public void showMenu(final int x, final int y) {
		final MenuBar contextMenu = new MenuBar(true);
		for (final Command command : listener.getCommands()) {
			final VContextMenuItem commandItem = new VContextMenuItem(listener.getCommandName(command), true, command);
			contextMenu.addItem(commandItem);
		}

		contextMenu.setVisible(true);
		add(contextMenu);
		super.setPopupPosition(x, y);
		super.show();
	}

	public class ContextMenuCommand implements Command {

		@Override
		public void execute() {
			VContextMenu.this.hide();
		}

	}
}
