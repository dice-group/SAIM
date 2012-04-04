package org.vaadin.cytographer.widgetset.client.ui;

import com.google.gwt.user.client.Command;

public interface ContextListener {

	public Command[] getCommands();

	public String getCommandName(Command command);

	public void initCommands(VContextMenu contextMenu);

}
