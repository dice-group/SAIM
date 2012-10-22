package de.uni_leipzig.simba.saim.gui.widget.panel;

import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
/** Shown to the user when the application starts.
 */
public class StartPanel extends Panel
{
	private static final long	serialVersionUID	= 4057340876075722120L;

	public StartPanel()
	{
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		this.setContent(layout);

		Embedded image = new Embedded("",new ClassResource("saimlogo.jpg",	getApplication()));
		addComponent(image);
		layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	}

}
