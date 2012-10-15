package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.form.KBInfoForm;
import de.uni_leipzig.simba.saim.Messages;
public class EndpointPanel extends Panel implements PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5883644284527880143L;
	private final Messages messages;
	public KBInfoForm kbSource = null;
	public KBInfoForm kbTarget = null;
	Configuration config;

	public EndpointPanel(final Messages messages)
	{
		this.messages = messages;
	}

	@Override
	public void attach()
	{		
		config = ((SAIMApplication)getApplication()).getConfig();
		if(kbSource==null) {kbSource = new KBInfoForm(messages.getString("EndpointPanel.configuresourceendpoint"), config.getSource(),messages);} //$NON-NLS-1$
		if(kbTarget==null) {kbTarget = new KBInfoForm(messages.getString("EndpointPanel.configuretargetendpoint"), config.getTarget(),messages);} //$NON-NLS-1$
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		this.setContent(layout);
		config.addPropertyChangeListener(this);
		
		this.addComponent(kbSource);
		this.addComponent(kbTarget);
	}

	public void close()
	{

		kbSource.close();
		kbTarget.close();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(Configuration.SETTING_CONFIG)) {
			kbSource.setValuesFromKBInfo(config.getSource());
			kbTarget.setValuesFromKBInfo(config.getTarget());
		}
	}
}