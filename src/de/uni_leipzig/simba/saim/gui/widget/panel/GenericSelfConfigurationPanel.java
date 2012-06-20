package de.uni_leipzig.simba.saim.gui.widget.panel;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

import de.uni_leipzig.simba.saim.Messages;

public class GenericSelfConfigurationPanel extends PerformPanel{

	private static final long serialVersionUID = -2272006849293767043L;
	private final Messages messages;
	private final Layout mainLayout = new VerticalLayout();
	private PerformPanel sub = null;
	private Select configuratorSelect;
	private String MESH;
	private String GENETICSELFCONFIG;
	
	public GenericSelfConfigurationPanel(final Messages messages) {
		this.messages = messages;
		MESH = messages.getString("GenericSelfConfigurationPanel.meshbased"); //$NON-NLS-1$
		GENETICSELFCONFIG = messages.getString("GenericSelfConfigurationPanel.GeneticSelfConfig"); //$NON-NLS-1$
	}
	@Override
	public void attach() {
		this.setCaption(messages.getString("MeshBasedSelfConfigPanel.caption")); //$NON-NLS-1$
		setContent(mainLayout);
		configuratorSelect = getConfigurationSelection();
		mainLayout.addComponent(configuratorSelect);
	}
	
	
	private Select getConfigurationSelection() {
		Select select = new Select(messages.getString("GenericSelfConfigurationPanel.selectcaption")); //$NON-NLS-1$
		select.addItem(MESH);		
		select.addItem(GENETICSELFCONFIG);
		select.setImmediate(true);
		select.addListener(new ConfigurationSelectionListener());
		return select;
	}
	
	private void doMeshBasedSelfConfiguration() {
		mainLayout.removeAllComponents();
		sub=new MeshBasedSelfConfigPanel(messages);
		mainLayout.addComponent(sub);
		sub.start();
	}
	
	private void doGeneticBasedSelfConfiguration() {
		mainLayout.removeAllComponents();
		sub = new GeneticBasedSelfConfigPanel(messages);
		mainLayout.addComponent(sub);
		sub.start();
	}
	
	class ConfigurationSelectionListener implements ValueChangeListener {
		private static final long serialVersionUID = -8110159175024579415L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			System.out.println(event.getProperty().toString());
			if(event.getProperty().toString().equals(MESH)) {
				doMeshBasedSelfConfiguration();
			}
			if(event.getProperty().toString().equals(GENETICSELFCONFIG)) {
				doGeneticBasedSelfConfiguration();
			}
		}
		
	}

	@Override
	public void onClose() {
		if(sub != null)
			sub.onClose();		
	}
	@Override
	public void start() {
		if(sub != null)
			sub.start();		
	}
}
