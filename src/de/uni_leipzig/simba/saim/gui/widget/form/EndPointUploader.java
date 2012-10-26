package de.uni_leipzig.simba.saim.gui.widget.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.query.ModelRegistry;
import de.uni_leipzig.simba.query.QueryModule;
import de.uni_leipzig.simba.query.QueryModuleFactory;

import de.uni_leipzig.simba.saim.core.FileStore;
/**
 * Endpoint upload componenet with type select. Writes File to store, attempts to
 * register Model of the selected type.
 * @author Lyko
 */
public class EndPointUploader extends CustomComponent implements Upload.SucceededListener, 
Upload.FailedListener, Upload.Receiver {
	VerticalLayout l = new VerticalLayout();
	Panel root;
	Select typeSelect;
	File file;
	Logger logger = LoggerFactory.getLogger(EndPointUploader.class);
	
	public EndPointUploader() {
		FileStore.setUp();
		root = new Panel("Upload dumped endpoint to server");

		l.addComponent(root);
	    setCompositionRoot(l);

	    // Create the Upload component.
        final Upload upload = new Upload("Please select a dumped endpoint and it's type to upload", this);

	    // Use a custom button caption instead of plain "Upload".
	    upload.setButtonCaption("Upload Now");

	    // Listen for events regarding the success of upload.
	    upload.addListener((Upload.SucceededListener) this);
	    upload.addListener((Upload.FailedListener) this);

	    root.addComponent(upload);

	    typeSelect = new Select("Type");
		typeSelect.addItem("N-Triple");
		typeSelect.addItem("N3");
		typeSelect.addItem("Turtle");
		typeSelect.addItem("RDF/XML");

		typeSelect.select("N3");
		typeSelect.setNullSelectionAllowed(false);

		root.addComponent(typeSelect);
	}


	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
//		String suffix = filename.substring(filename.lastIndexOf(".")+1);
	    FileOutputStream fos = null; // Output stream to write to
	    file = new File(FileStore.getPathToEPStore() + "/" + filename);
	    logger.info("Attempt to upload file "+filename);
	    try {
	        fos = new FileOutputStream(file);
	    } catch (final java.io.FileNotFoundException e) {
	    	// Error while opening the file. Not reported here.
	        e.printStackTrace();
	        return null;
	    }
	    return fos; // Return the output stream to write to
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		System.out.println("Upload failed:\n"+event.getReason().getMessage());
		getWindow().showNotification("Sorry there was an error uploading the file.");
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		root.removeAllComponents();
		Label l = new Label();
		l.setCaption("Successfull uploaded file "+file.getName()+" trying to parse data...");
		root.addComponent(l);
		KBInfo info = new KBInfo();
		info.endpoint = file.getAbsolutePath();
		try {
			QueryModule fQModule = QueryModuleFactory.getQueryModule((String) typeSelect.getValue(), info);
			Model model = ModelRegistry.getInstance().getMap().get(info.endpoint);
             if (model == null) {
                 throw new RuntimeException("No model with id '" + info.endpoint + "' registered");
             } else  {
            	 l.setCaption("File parsed correctly. Model is registered!");
     			logger.info("Successfully read data of type: "+info.type);
     			logger.info("Registered Model of size ... "+model.size());
     			rememberEndpoint(info);
             }
		} catch(Exception  e) {
//			getWindow().showNotification("Sorry there was an error reading the file. Abborting...");
			l.setCaption("Sorry there was an error reading the file. Abborting...");
			e.printStackTrace();
			file.delete();
		}
		/** @TODO provide form for name and additional informations.*/

		/** @TODO Store prebuild KBInfo for endpoint,
		 *  profide shortcut in predefined KBInfos set in the EndpointPanel
		 */
	}
		
	private void rememberEndpoint(KBInfo info) {
		LinkedList<KBInfo> list = FileStore.getListOfInfos();
		if(list.size()>0) {
			// try to avoid double entries
			boolean exists = false;
			for(KBInfo other : list) {
				if(other.equals(info))
					exists = true;
			}
			if(!exists)
				list.add(info);
			else {
				logger.info("KBInfo was already saved");
			}
		} else {
			list.add(info);
		}
		logger.info("Saving list of size "+list.size());
		FileStore.saveInfoList(list);
	}
	
}
