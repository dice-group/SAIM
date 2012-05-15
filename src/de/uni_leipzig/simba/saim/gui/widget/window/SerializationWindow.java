package de.uni_leipzig.simba.saim.gui.widget.window;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Window;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.Serializer;
import de.uni_leipzig.simba.io.SerializerFactory;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;

public class SerializationWindow extends Window {
	
	public SerializationWindow(Mapping m) {
		super();
		setWidth("700px"); //$NON-NLS-1$
		setCaption(Messages.getString("downloadresults")); //$NON-NLS
		setModal(true);
		List<Link> links = getLinks(m);
		for(Link l : links) {
			addComponent(l);
		}
	}
	
	private List<Link> getLinks(Mapping m) {
		Configuration config = Configuration.getInstance();
		Serializer serial = SerializerFactory.getSerializer("N3"); //$NON-NLS-1$
		String fileName   = ""; //$NON-NLS-1$
		fileName += config.getSource().id+"_"+config.getTarget().id+".nt"; //$NON-NLS-1$ //$NON-NLS-2$
		serial.open(fileName);
		String predicate = config.getLimesConfiReader().acceptanceRelation;
		// print prefixes
		System.out.println(config.getLimesConfiReader().prefixes);
		serial.setPrefixes(config.getLimesConfiReader().prefixes);
		for(String uri1 : m.map.keySet()) {
			for(Entry<String, Double> e : m.map.get(uri1).entrySet()) {
				serial.printStatement(PrefixHelper.expand(uri1), PrefixHelper.expand(predicate), PrefixHelper.expand(e.getKey()), e.getValue());
			}
		}
		serial.close();
		List<Link> links = new LinkedList<Link>();
		links.add(new Link(Messages.getString("downloadlinkspec"),new FileResource(new File(fileName), SAIMApplication.getInstance()))); //$NON-NLS-1$
		return links;
	}
}
