package de.uni_leipzig.simba.saim.backend;

import java.io.Serializable;
/**
 * An example config as of now has only 2 fields: name and fileParth;
 * @author Lyko
 */
public class ExampleConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String filePath;
	/**
	 * Default constructor.
	 * @param name Name of the Limes configuration.
	 * @param filePath Path to the Limes configuration XML file on this system.
	 */
	public ExampleConfig(String name, String filePath) {
		this.name = name;
		this.filePath = filePath;
	}
	/**
	 * Construct without name specification uses file name as name of the config.
	 * @param filePath
	 */
	public ExampleConfig(String filePath) {
		String name = filePath;
		if(filePath.lastIndexOf("/")>=0 && filePath.lastIndexOf("/")<filePath.length()-1)
			name = name.substring(filePath.lastIndexOf("/")+1);
		if(name.lastIndexOf(".")>-1)
			name = name.substring(0, name.lastIndexOf("."));
		this.filePath = filePath;
		this.name = name.length()>0?name:filePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Override
	public String toString() {
		return name + " - " + filePath;
	}
}
