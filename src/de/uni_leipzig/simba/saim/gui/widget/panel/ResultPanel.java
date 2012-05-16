package de.uni_leipzig.simba.saim.gui.widget.panel;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.saim.gui.widget.InstanceMappingTable;
import de.uni_leipzig.simba.saim.gui.widget.window.SerializationWindow;

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
		downloadResults = new Button(Messages.getString("save"));
		downloadResults.addListener(new DownLoadButtonClickListener());		
		layout.addComponent(data.getTable());
		layout.addComponent(downloadResults);
	}
	
	
	/**ClickListener for the Button to download results**/	
	class DownLoadButtonClickListener implements Button.ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			
			Window download = new SerializationWindow(data.getMapping());
			SAIMApplication.getInstance().getMainWindow().addWindow(download);
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
