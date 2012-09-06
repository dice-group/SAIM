package de.uni_leipzig.simba.saim.gui.widget.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.ui.*;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

import de.uni_leipzig.simba.query.FileQueryModule;
import de.uni_leipzig.simba.saim.core.FileStore;

public class EndPointUploader extends CustomComponent implements Upload.SucceededListener, 
	Upload.FailedListener, Upload.Receiver{

	VerticalLayout l = new VerticalLayout();
	Panel root;
	
	File file;
	
	public EndPointUploader() {
		FileStore.setUp();
		root = new Panel("Upload dumped endpoint to server");
		
		l.addComponent(root);
	    setCompositionRoot(l);

	    // Create the Upload component.
        final Upload upload = new Upload("Please upload dumped rdf graphs " +
        		"as files holding N-Trpiples to the server", this);

	    // Use a custom button caption instead of plain "Upload".
	    upload.setButtonCaption("Upload Now");
	        
	    // Listen for events regarding the success of upload.
	    upload.addListener((Upload.SucceededListener) this);
	    upload.addListener((Upload.FailedListener) this);

	    root.addComponent(upload);
	    root.addComponent(new Label("Click 'Browse' to "+
	    "select a file and then click 'Upload'."));
	}
	
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		String suffix = filename.substring(filename.lastIndexOf(".")+1);
	    System.out.println(suffix);
		if(!suffix.equalsIgnoreCase("nt")) {
			System.out.println("no supported format");
	       	return null;
	    } else {
	    	FileOutputStream fos = null; // Output stream to write to
	        file = new File(FileStore.getPathToEPStore() + "/" + filename);
	         
	        System.out.println("Attempt to upload file "+filename);	     
	        try {
	            // Open the file for writing.
	            fos = new FileOutputStream(file);
	        } catch (final java.io.FileNotFoundException e) {
	            // Error while opening the file. Not reported here.
	            e.printStackTrace();
	            return null;
	        }
	        return fos; // Return the output stream to write to
	    }
		
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		root.removeAllComponents();

		Label l = new Label("Successfull uploaded file "+file.getName());
		root.addComponent(l);
		
		/** @TODO test data*/
		
		/** @TODO provide form for name and additional informations.*/
		
		/** @TODO Store prebuild KBInfo for endpoint,
		 *  profide shortcut in predefined KBInfos set in the EndpointPanel
		 */
	}

}
