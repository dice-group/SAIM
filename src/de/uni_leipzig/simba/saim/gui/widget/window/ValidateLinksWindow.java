package de.uni_leipzig.simba.saim.gui.widget.window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.util.DataCleaner;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.uni_leipzig.simba.saim.util.LinkQualityComputer;
/**
 * Custom component presents list of available gold standards a user can choose to compare his mapping to.
 * This Component holds a vertical layout with the Select and the button.
 * @author Klaus Lyko
 *
 */
public class ValidateLinksWindow extends CustomComponent {
	private static final long serialVersionUID = -4759702870219265001L;
	private final String storageFile = "reference.list";//$NON-NLS-1$
	SAIMApplication app;
	private VerticalLayout mainLayout;//$NON-NLS-1$
	Select refSelect;
	private HashMap<String, String> examples;
	private Button compare;
	Mapping mapping;
	
	/**
	 * Constructor
	 * @param app SAIMApplication instance needed for localizinf messages.
	 * @param mapping Mapping which should be evaluated
	 */
	public ValidateLinksWindow(SAIMApplication app, Mapping mapping) {
		
		this.app = app;
		this.mapping = mapping;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	/**
	 * Inits layout
	 */
	private void buildMainLayout() {		
		mainLayout = new VerticalLayout();
		try {
			refSelect = loadReferenceFile();
			compare = new Button(app.messages.getString("ValidateLinksWindow.button"));//$NON-NLS-1$
			compare.addListener(new CompareClickListener(app));
			mainLayout.addComponent(refSelect);
			mainLayout.addComponent(compare);
		} catch (URISyntaxException e) {
			app.getMainWindow().showNotification("Error loading reference files");//$NON-NLS-1$
			e.printStackTrace();
		} catch (IOException e) {
			app.getMainWindow().showNotification("Error loading reference files");//$NON-NLS-1$
			e.printStackTrace();
		}
	}

	/**
	 * Generates Select for gold standard files.
	 * @return Select with Listener;
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private Select loadReferenceFile() throws IOException, URISyntaxException {
		Select select = new Select(app.messages.getString("ValidateLinksWindow.select"));//$NON-NLS-1$
		// load list
		URL url;String path;examples=new HashMap<String,String>();
		url = getClass().getClassLoader().getResource(storageFile);
		path = new File(url.toURI()).getAbsolutePath();
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream (path));
		BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
		String line = bufferedReader.readLine();
		while(line != null) {
			String parts[] = DataCleaner.separate(line, ";", 2);//$NON-NLS-1$
			if(line.length()>0 && parts.length==2) {
				String name=parts[0].replaceAll("\"", "");//$NON-NLS-1$
				examples.put(name, parts[1].replaceAll("\"", ""));//$NON-NLS-1$
				select.addItem(name);
				line = bufferedReader.readLine();				
			}
		}
		bufferedReader.close();inputStreamReader.close();
		return select;
	}
	
	/**
	 * Click Listener handles click of button. Loads and presents evaluation results.
	 * @author Klaus Lyko
	 *
	 */
	class CompareClickListener implements Button.ClickListener {
		private static final long serialVersionUID = 8281776849807276687L;
		SAIMApplication app;
		/**
		 * Constructor to get access to Messages
		 */
		public CompareClickListener(SAIMApplication app) {
			this.app = app;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			// read gold standard
			String path = examples.get(refSelect.getValue());
			URL url = getClass().getClassLoader().getResource(path);
			Mapping ref = LinkQualityComputer.getMapping(url.getPath());
			LinkQualityComputer lc = new LinkQualityComputer(mapping, ref);
			// present results
			Window sub = new Window(app.messages.getString("ValidateLinksWindow.caption"));//$NON-NLS-1$
			Label label = new Label(app.messages.getString("ValidateLinksWindow.fscore")+lc.computeFScore()+//$NON-NLS-1$
					app.messages.getString("ValidateLinksWindow.precision")+lc.computePrecision()+//$NON-NLS-1$
					app.messages.getString("ValidateLinksWindow.recall")+lc.computeRecall()//$NON-NLS-1$
					, Label.CONTENT_XHTML);//$NON-NLS-1$
			sub.addComponent(label);
			app.getMainWindow().addWindow(sub);
		}
		
	}
}
