package de.uni_leipzig.simba.saim.gui.widget.panel;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.io.Serializer;
import de.uni_leipzig.simba.io.SerializerFactory;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
/**Panel to show a Table with computed mappings**/
public class ResultPanel extends Panel{
	InstanceMappingTable data;
	VerticalLayout layout;
	Button downloadResults;
	Configuration config = Configuration.getInstance();
	
	public ResultPanel(InstanceMappingTable iT) {
		super(Messages.getString("results")); //$NON-NLS-1$
		this.data = iT;
		init();
	}
	
	private void init() {
		layout = new VerticalLayout();
		layout.setWidth("100%");
		this.setContent(layout);
		downloadResults = new Button(Messages.getString(Messages.getString("save"))); //$NON-NLS-1$
		downloadResults.addListener(new DownLoadButtonClickListener());		
		layout.addComponent(data.getTable());
		layout.addComponent(downloadResults);
	}
	
	
	/**ClickListener for the Button to download results**/	
	class DownLoadButtonClickListener implements Button.ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			
			Window download = new Window();
			download.setWidth("700px"); //$NON-NLS-1$
			download.setCaption(Messages.getString("downloadresults")); //$NON-NLS-1$
			Serializer serial = SerializerFactory.getSerializer("N3"); //$NON-NLS-1$
			String fileName   = ""; //$NON-NLS-1$
			fileName += config.getSource().id+"_"+config.getTarget().id+".nt"; //$NON-NLS-1$ //$NON-NLS-2$
			serial.open(fileName);
			String predicate = config.getLimesConfiReader().acceptanceRelation;
			// print prefixes
			System.out.println(config.getLimesConfiReader().prefixes);
			serial.setPrefixes(config.getLimesConfiReader().prefixes);
			Mapping m = data.getMapping();
			for(String uri1 : m.map.keySet()) {
				for(Entry<String, Double> e : m.map.get(uri1).entrySet()) {
					serial.printStatement(PrefixHelper.expand(uri1), PrefixHelper.expand(predicate), PrefixHelper.expand(e.getKey()), e.getValue());
				}
			}
			serial.close();
			download.addComponent(new Link(Messages.getString("downloadlinkspec"),new FileResource(new File(fileName), SAIMApplication.getInstance()))); //$NON-NLS-1$
			download.setModal(true);
			getWindow().addWindow(download);
		}
		
	}
}
//
////5.1 Get Writer ready
//Serializer accepted = SerializerFactory.getSerializer(cr.outputFormat);
//Serializer toReview = SerializerFactory.getSerializer(cr.outputFormat);
//
//accepted.open(cr.acceptanceFile);
//accepted.printPrefixes(cr.prefixes);
//toReview.open(cr.verificationFile);
//toReview.printPrefixes(cr.prefixes);
//
////5.2 Now write results
//HashMap<String, Double> results;
//Iterator<String> resultIterator;
//String s;
//for (int i = 0; i < uris.size(); i++) {
//
//    if ((i + 1) % 1000 == 0) {
//        logger.info(((i * 100) / uris.size()) + "% of links computed ...");
//    }
//    results = organizer.getSimilarInstances(source.getInstance(uris.get(i)), cr.verificationThreshold, mf);
//    //logger.info("Getting results for "+sourceInfo.getInstance(uris.get(i)));
//    resultIterator = results.keySet().iterator();
//    while (resultIterator.hasNext()) {
//        s = resultIterator.next();
//        if (results.get(s) >= cr.acceptanceThreshold) {
//            accepted.printStatement(uris.get(i), cr.acceptanceRelation, s, results.get(s));
//        } else if (results.get(s) >= cr.verificationThreshold) {
//            toReview.printStatement(uris.get(i), cr.acceptanceRelation, s, results.get(s));
//        }
//    }
//}
//
////5.3 Close writers
//accepted.close();
//toReview.close();
