package de.uni_leipzig.simba.saim.gui.widget.window;

import java.io.File;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.Serializer;
import de.uni_leipzig.simba.io.SerializerFactory;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;

public class SerializationWindow extends Window {
	private static final long serialVersionUID = 949091830343396212L;
	private final Messages messages;
	VerticalLayout mainLayout;
	HashMap<String, Serializer> serializerNames;
	NativeSelect serializerSelect;
	Button generate = new Button("Generate");
	Mapping mapping;

	public SerializationWindow(Mapping m, final Messages messages)
	{
		super();
		this.messages=messages;
		mapping = m;
		mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		setWidth("700px");
		setCaption(messages.getString("downloadresults"));
		setModal(true);
		HorizontalLayout hl = new HorizontalLayout();
		serializerSelect = getSerializerSelect();
//		generate.addListener(new ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				Logger logger = LoggerFactory.getLogger(SerializerSelectListener.class);
//				String key = "N3";				logger.info("Getting serializer "+key);
//				Serializer serial = serializerNames.get(key);
//				Link l = getLinkToFile(mapping, serial);
//				System.out.println("Adding Link: "+l.getData()+"");
//				mainLayout.addComponent(l);
//			}
//		});
		
		//ttl, tab, nt
		hl.addComponent(serializerSelect);
//		hl.addComponent(generate);
		mainLayout.addComponent(hl);

	}

	private Link getLinkToFile(Mapping m, Serializer serial) {
		Configuration config = ((SAIMApplication) getApplication()).getConfig();//Configuration.getInstance();
		String fileName   = "";
		fileName += config.getSource().id+"_"+config.getTarget().id+"."+serial.getFileExtension();
		File f = new File(fileName);
		boolean openable = serial.open(fileName);
		if(!openable) {
			showNotification("Not able to serialize to file"+f.getAbsolutePath()+" ", Notification.TYPE_ERROR_MESSAGE);
		} else {
			showNotification("Successfully opened file "+f.getAbsolutePath(), Notification.TYPE_HUMANIZED_MESSAGE);
		}
//		
		System.out.println("serializing...");
		System.out.println(f.getAbsolutePath()+" read?"+ f.canRead()+" write?"+f.canWrite());
		String predicate = "owl:sameAs";
		// print prefixes
//		System.out.println(config.getLimesConfiReader().prefixes);
		serial.setPrefixes(config.getLimesConfiReader().prefixes);
//		serial.
		serial.writeToFile(m, predicate, fileName);
//		for(String uri1 : m.map.keySet()) {
//			for(Entry<String, Double> e : m.map.get(uri1).entrySet()) {
//				serial.printStatement(PrefixHelper.expand(uri1), predicate, PrefixHelper.expand(e.getKey()), e.getValue());
//			}
//		}
		serial.close();
		/**No permission?**/
		return new Link(messages.getString("downloadlinkspec"), new FileResource(new File(fileName), getApplication()));
	}

	private NativeSelect getSerializerSelect() {
		NativeSelect select = new NativeSelect(messages.getString("SerializationWindow.serializerselectcaption"));
		serializerNames = new HashMap<String, Serializer>();
		serializerNames.put(messages.getString("SerializationWindow.turtle"), SerializerFactory.getSerializer("ttl"));
		serializerNames.put(messages.getString("SerializationWindow.n3"), SerializerFactory.getSerializer("N3"));
		serializerNames.put(messages.getString("SerializationWindow.tabseparated"), SerializerFactory.getSerializer("tab"));
		for(String s : serializerNames.keySet())
			select.addItem(s);
//		select.select(messages.getString("SerializationWindow.n3"));
		select.addListener(new SerializerSelectListener());
		select.setNullSelectionAllowed(false);
		select.setImmediate(true);
		return select;
	}

	private class SerializerSelectListener implements ValueChangeListener {
		/**
		 */
		private static final long serialVersionUID = -4905589098233429238L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			Logger logger = LoggerFactory.getLogger(SerializerSelectListener.class);
			String key = event.getProperty().toString();
			logger.info("Getting serializer "+key);
			Serializer serial = serializerNames.get(key);
			Link l = getLinkToFile(mapping, serial);
			System.out.println("Adding Link: "+l.getData()+"");
			mainLayout.addComponent(l);
		}

	}
}
