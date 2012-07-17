package de.uni_leipzig.simba.saim.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import de.uni_leipzig.simba.saim.SAIMApplication;
import de.uni_leipzig.simba.util.DataCleaner;

/**
 * Class to load from a persistent storage. As of now we use a simple file holding the informations.
 * We use this extra class to load to support an easy integration of an database. 
 * @author Lyko
 *
 */
public class ExampleLoader {
	private final String storageFile = "example.list";
	SAIMApplication app;
	
	public ExampleLoader(SAIMApplication app) {
		this.app = app;
	}
	
	/**
	 * Public method to get the examples from store. As of now a file.
	 * @return
	 */
	public List<ExampleConfig> getExamples() {
		try {
			return readFile();
		} catch (IOException e) {
			e.printStackTrace();
			return new LinkedList<ExampleConfig>();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new LinkedList<ExampleConfig>();
		}
	}
	
	/**
	 * Reads the example config file.
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<ExampleConfig> readFile() throws IOException, URISyntaxException {
		URL url;String path;
		url = getClass().getClassLoader().getResource(storageFile);
		path = new File(url.toURI()).getAbsolutePath();
		List<ExampleConfig> list = new LinkedList<ExampleConfig>();
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream (path));
		BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
		String line = bufferedReader.readLine();
		while(line != null) {
			String parts[] = DataCleaner.separate(line, ";", 2);
			if(line.length()>0 && parts.length==2) {				
				list.add(new ExampleConfig(parts[0].replaceAll("\"", ""), parts[1].replaceAll("\"", "")));
				line = bufferedReader.readLine();
			}			
		}
		return list;
	}
	
	/**
	 * Method to add a new entry into the examples file.
	 * @param c
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public void addEntry(ExampleConfig c) throws URISyntaxException, IOException {
		URL url;String path;
		url = getClass().getClassLoader().getResource(storageFile);
		path = new File(url.toURI()).getAbsolutePath();
	//	InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream (path));
		BufferedWriter writer = new BufferedWriter (new FileWriter(path, true));
		writer.newLine();
		writer.write("\""+c.getName()+"\";\""+c.getFilePath()+"\"");
		writer.close();
	}
}
