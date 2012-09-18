package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph;

import com.google.gwt.user.client.Command;
/**
 * 
 * @author rspeck
 *
 */
public interface ContextListener {

	public Command[] getCommands();

	public String getCommandName(Command command);

	public void initCommands(VContextMenu contextMenu);
}
