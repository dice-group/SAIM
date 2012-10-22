package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
/**
 * @author rspeck
 */
public class VContextMenu extends PopupPanel {

	private final ContextListener listener;

	public VContextMenu(final ContextListener contextListener) {
		super();
		listener = contextListener;
		listener.initCommands(this);
		sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
				| Event.ONCLICK);
		setStyleName("contextmenu");
	}

	public void showMenu(final int x, final int y) {
		final MenuBar contextMenu = new MenuBar(true);
		for (final Command command : listener.getCommands())
			contextMenu.addItem(new MenuItem(listener.getCommandName(command),
					true, command));

		contextMenu.setVisible(true);
		add(contextMenu);
		setPopupPosition(x, y);
		show();
	}

	public class ContextMenuCommand implements Command {
		@Override
		public void execute() {
			VContextMenu.this.hide();
		}
	}
}